package pl.kea.parafia.nabozenstwo;

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
@RequestMapping("/nabozenstwa")
public class NabozenstwoController {

    private final NabozenstwoService nabozenstwa;

    public NabozenstwoController(NabozenstwoService nabozenstwa) {
        this.nabozenstwa = nabozenstwa;
    }

    @ModelAttribute("rodzaje")
    public RodzajNabozenstwa[] rodzaje() {
        return RodzajNabozenstwa.values();
    }

    @ModelAttribute("momenty")
    public MomentPiesni[] momenty() {
        return MomentPiesni.values();
    }

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("nabozenstwa", nabozenstwa.najnowsze());
        return "nabozenstwo/lista";
    }

    @GetMapping("/nowe")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String nowe(Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", NabozenstwoFormularz.pusty());
        }
        model.addAttribute("edycja", false);
        return "nabozenstwo/formularz";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String utworz(@Valid @ModelAttribute("formularz") NabozenstwoFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        return zapisz(null, formularz, wynik, model, atrybuty);
    }

    @GetMapping("/{id}")
    public String szczegoly(@PathVariable Long id, Model model) {
        model.addAttribute("nabozenstwo", nabozenstwa.znajdz(id));
        return "nabozenstwo/szczegoly";
    }

    @GetMapping("/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String edycja(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", nabozenstwa.formularz(nabozenstwa.znajdz(id)));
        }
        model.addAttribute("edycja", true);
        model.addAttribute("identyfikator", id);
        return "nabozenstwo/formularz";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String aktualizuj(@PathVariable Long id, @Valid @ModelAttribute("formularz") NabozenstwoFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        return zapisz(id, formularz, wynik, model, atrybuty);
    }

    private String zapisz(Long id, NabozenstwoFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", id != null);
            model.addAttribute("identyfikator", id);
            return "nabozenstwo/formularz";
        }
        Nabozenstwo zapisane = nabozenstwa.zapisz(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano porządek nabożeństwa.");
        return "redirect:/nabozenstwa/" + zapisane.getId();
    }
}
