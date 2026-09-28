package pl.kea.parafia.osoba;

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

@Controller
@RequestMapping("/gospodarstwa")
public class GospodarstwoController {

    private final GospodarstwoService gospodarstwa;
    private final OsobaService osoby;

    public GospodarstwoController(GospodarstwoService gospodarstwa, OsobaService osoby) {
        this.gospodarstwa = gospodarstwa;
        this.osoby = osoby;
    }

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("gospodarstwa", gospodarstwa.lista());
        model.addAttribute("liczby", gospodarstwa.liczbyOsob());
        return "gospodarstwa/lista";
    }

    @GetMapping("/nowe")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String nowe(Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", GospodarstwoFormularz.pusty());
        }
        model.addAttribute("edycja", false);
        return "gospodarstwa/formularz";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String utworz(
            @Valid @ModelAttribute("formularz") GospodarstwoFormularz formularz,
            BindingResult wynik,
            Model model,
            RedirectAttributes atrybuty
    ) {
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", false);
            return "gospodarstwa/formularz";
        }
        Gospodarstwo gospodarstwo = gospodarstwa.utworz(formularz);
        atrybuty.addFlashAttribute("sukces", "Dodano gospodarstwo.");
        return "redirect:/gospodarstwa/" + gospodarstwo.getId();
    }

    @GetMapping("/{id}")
    public String szczegoly(@PathVariable Long id, Model model) {
        model.addAttribute("gospodarstwo", gospodarstwa.znajdz(id));
        model.addAttribute("domownicy", osoby.domownicy(id));
        return "gospodarstwa/szczegoly";
    }

    @GetMapping("/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String edycja(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", GospodarstwoFormularz.z(gospodarstwa.znajdz(id)));
        }
        model.addAttribute("edycja", true);
        model.addAttribute("identyfikator", id);
        return "gospodarstwa/formularz";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String zapisz(
            @PathVariable Long id,
            @Valid @ModelAttribute("formularz") GospodarstwoFormularz formularz,
            BindingResult wynik,
            Model model,
            RedirectAttributes atrybuty
    ) {
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", true);
            model.addAttribute("identyfikator", id);
            return "gospodarstwa/formularz";
        }
        gospodarstwa.aktualizuj(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano gospodarstwo.");
        return "redirect:/gospodarstwa/" + id;
    }

    @PostMapping("/{id}/usun")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String usun(@PathVariable Long id, RedirectAttributes atrybuty) {
        gospodarstwa.usun(id);
        atrybuty.addFlashAttribute("sukces", "Usunięto puste gospodarstwo.");
        return "redirect:/gospodarstwa";
    }
}
