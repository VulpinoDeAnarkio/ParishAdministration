package pl.kea.parafia.grupa;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kea.parafia.osoba.OsobaService;

@Controller
@RequestMapping("/grupy")
public class GrupaController {

    private final GrupaService grupy;
    private final OsobaService osoby;

    public GrupaController(GrupaService grupy, OsobaService osoby) {
        this.grupy = grupy;
        this.osoby = osoby;
    }

    @ModelAttribute("rodzaje")
    public RodzajGrupy[] rodzaje() {
        return RodzajGrupy.values();
    }

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("grupy", grupy.lista());
        return "grupy/lista";
    }

    @GetMapping("/nowa")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String nowa(Model model) {
        przygotuj(model, GrupaFormularz.pusty(), false, null);
        return "grupy/formularz";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String utworz(@Valid @ModelAttribute("formularz") GrupaFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            przygotuj(model, formularz, false, null);
            return "grupy/formularz";
        }
        Grupa grupa = grupy.zapisz(null, formularz);
        atrybuty.addFlashAttribute("sukces", "Utworzono grupę.");
        return "redirect:/grupy/" + grupa.getId();
    }

    @GetMapping("/{id}")
    public String szczegoly(@PathVariable Long id, Model model) {
        model.addAttribute("grupa", grupy.znajdz(id));
        model.addAttribute("czlonkowie", grupy.czlonkowie(id));
        if (!model.containsAttribute("czlonek")) {
            model.addAttribute("czlonek", new CzlonekFormularz(null, "Członek", null));
        }
        model.addAttribute("osoby", osoby.wszyscy());
        return "grupy/szczegoly";
    }

    @GetMapping("/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String edycja(@PathVariable Long id, Model model) {
        przygotuj(model, GrupaFormularz.z(grupy.znajdz(id)), true, id);
        return "grupy/formularz";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String zapisz(@PathVariable Long id, @Valid @ModelAttribute("formularz") GrupaFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            przygotuj(model, formularz, true, id);
            return "grupy/formularz";
        }
        grupy.zapisz(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano grupę.");
        return "redirect:/grupy/" + id;
    }

    @PostMapping("/{id}/czlonkowie")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String dodaj(@PathVariable Long id, @Valid @ModelAttribute("czlonek") CzlonekFormularz formularz, BindingResult wynik, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            atrybuty.addFlashAttribute("blad", "Wybierz osobę do grupy.");
            return "redirect:/grupy/" + id;
        }
        grupy.dodajCzlonka(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Dodano osobę do grupy.");
        return "redirect:/grupy/" + id;
    }

    @PostMapping("/czlonkowie/{czlonekId}/zakoncz")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String zakoncz(@PathVariable Long czlonekId, @org.springframework.web.bind.annotation.RequestParam Long grupaId, RedirectAttributes atrybuty) {
        grupy.zakoncz(czlonekId);
        atrybuty.addFlashAttribute("sukces", "Zakończono udział w grupie.");
        return "redirect:/grupy/" + grupaId;
    }

    private void przygotuj(Model model, GrupaFormularz formularz, boolean edycja, Long id) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", formularz);
        }
        model.addAttribute("edycja", edycja);
        model.addAttribute("identyfikator", id);
        model.addAttribute("osoby", osoby.wszyscy());
    }
}
