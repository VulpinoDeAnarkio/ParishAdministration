package pl.kea.parafia.sluzba;

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
import pl.kea.parafia.nabozenstwo.NabozenstwoService;
import pl.kea.parafia.osoba.OsobaService;

@Controller
@RequestMapping("/sluzba")
public class SluzbaController {

    private final SluzbaService sluzba;
    private final NabozenstwoService nabozenstwa;
    private final OsobaService osoby;

    public SluzbaController(SluzbaService sluzba, NabozenstwoService nabozenstwa, OsobaService osoby) {
        this.sluzba = sluzba;
        this.nabozenstwa = nabozenstwa;
        this.osoby = osoby;
    }

    @ModelAttribute("statusy")
    public StatusZadania[] statusy() {
        return StatusZadania.values();
    }

    @ModelAttribute("role")
    public RolaSluzby[] role() {
        return RolaSluzby.values();
    }

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("zadania", sluzba.zadania());
        model.addAttribute("nabozenstwa", nabozenstwa.najnowsze());
        return "sluzba/lista";
    }

    @GetMapping("/zadania/nowe")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String nowe(Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", new ZadanieFormularz("", "", null, StatusZadania.DO_ZROBIENIA, null, null));
        }
        model.addAttribute("osoby", osoby.wszyscy());
        return "sluzba/zadanie";
    }

    @PostMapping("/zadania")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String utworz(@Valid @ModelAttribute("formularz") ZadanieFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            model.addAttribute("osoby", osoby.wszyscy());
            return "sluzba/zadanie";
        }
        sluzba.zapiszZadanie(null, formularz);
        atrybuty.addFlashAttribute("sukces", "Dodano zadanie.");
        return "redirect:/sluzba";
    }

    @GetMapping("/nabozenstwo/{id}")
    public String nabozenstwo(@PathVariable Long id, Model model) {
        model.addAttribute("nabozenstwo", nabozenstwa.znajdz(id));
        model.addAttribute("dyzury", sluzba.dyzury(id));
        model.addAttribute("osoby", osoby.wszyscy());
        model.addAttribute("dyzur", new DyzurFormularz(id, null, RolaSluzby.LEKTOR));
        return "sluzba/dyzury";
    }

    @PostMapping("/dyzury")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String dyzur(@Valid DyzurFormularz formularz, BindingResult wynik, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            atrybuty.addFlashAttribute("blad", "Wybierz osobę i rolę.");
            return "redirect:/sluzba";
        }
        sluzba.dodajDyzur(formularz);
        atrybuty.addFlashAttribute("sukces", "Przypisano dyżur.");
        return "redirect:/sluzba/nabozenstwo/" + formularz.nabozenstwoId();
    }
}
