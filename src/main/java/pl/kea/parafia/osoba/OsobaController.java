package pl.kea.parafia.osoba;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kea.parafia.grupa.GrupaService;
import pl.kea.parafia.ksiega.KsiegaService;
import pl.kea.parafia.postep.PostepService;

@Controller
@RequestMapping("/osoby")
public class OsobaController {

    private final OsobaService osoby;
    private final GospodarstwoService gospodarstwa;
    private final KsiegaService ksiegi;
    private final GrupaService grupy;
    private final PostepService postep;

    public OsobaController(OsobaService osoby, GospodarstwoService gospodarstwa, KsiegaService ksiegi, GrupaService grupy, PostepService postep) {
        this.osoby = osoby;
        this.gospodarstwa = gospodarstwa;
        this.ksiegi = ksiegi;
        this.grupy = grupy;
        this.postep = postep;
    }

    @ModelAttribute("statusy")
    public StatusCzlonkostwa[] statusy() {
        return StatusCzlonkostwa.values();
    }

    @ModelAttribute("plcie")
    public Plec[] plcie() {
        return Plec.values();
    }

    @ModelAttribute("roleWRodzinie")
    public RolaWRodzinie[] roleWRodzinie() {
        return RolaWRodzinie.values();
    }

    @GetMapping
    public String lista(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) StatusCzlonkostwa status,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        model.addAttribute("q", q);
        model.addAttribute("status", status);
        model.addAttribute("strona", osoby.szukaj(q, status, PageRequest.of(page, 15, Sort.by("nazwisko", "imie"))));
        model.addAttribute("liczbaCzlonkow", osoby.liczbaCzlonkow());
        return "osoby/lista";
    }

    @GetMapping("/nowa")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String nowa(Model model) {
        przygotujFormularz(model, OsobaFormularz.pusty());
        model.addAttribute("edycja", false);
        return "osoby/formularz";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String utworz(
            @Valid @ModelAttribute("formularz") OsobaFormularz formularz,
            BindingResult wynik,
            Model model,
            RedirectAttributes atrybuty
    ) {
        if (wynik.hasErrors()) {
            przygotujFormularz(model, formularz);
            model.addAttribute("edycja", false);
            return "osoby/formularz";
        }
        Osoba osoba = osoby.utworz(formularz);
        atrybuty.addFlashAttribute("sukces", "Dodano osobę do kartoteki.");
        return "redirect:/osoby/" + osoba.getId();
    }

    @GetMapping("/{id}")
    public String szczegoly(@PathVariable Long id, Model model) {
        Osoba osoba = osoby.znajdz(id);
        model.addAttribute("osoba", osoba);
        model.addAttribute("ksiegiOsoby", ksiegi.historia(id));
        model.addAttribute("grupyOsoby", grupy.grupyOsoby(id));
        model.addAttribute("droga", postep.droga(id));
        if (osoba.getGospodarstwo() != null) {
            List<Osoba> dom = osoby.domownicy(osoba.getGospodarstwo().getId());
            model.addAttribute("domownicy", dom);
        }
        return "osoby/szczegoly";
    }

    @GetMapping("/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String edycja(@PathVariable Long id, Model model) {
        przygotujFormularz(model, OsobaFormularz.z(osoby.znajdz(id)));
        model.addAttribute("edycja", true);
        model.addAttribute("identyfikator", id);
        return "osoby/formularz";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String zapisz(
            @PathVariable Long id,
            @Valid @ModelAttribute("formularz") OsobaFormularz formularz,
            BindingResult wynik,
            Model model,
            RedirectAttributes atrybuty
    ) {
        if (wynik.hasErrors()) {
            przygotujFormularz(model, formularz);
            model.addAttribute("edycja", true);
            model.addAttribute("identyfikator", id);
            return "osoby/formularz";
        }
        osoby.aktualizuj(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano kartotekę.");
        return "redirect:/osoby/" + id;
    }

    private void przygotujFormularz(Model model, OsobaFormularz formularz) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", formularz);
        }
        model.addAttribute("gospodarstwa", gospodarstwa.lista());
    }
}
