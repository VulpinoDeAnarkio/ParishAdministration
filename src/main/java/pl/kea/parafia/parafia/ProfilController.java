package pl.kea.parafia.parafia;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfilController {

    private final ProfilParafiiService profile;

    public ProfilController(ProfilParafiiService profile) {
        this.profile = profile;
    }

    @GetMapping("/profil")
    public String formularz(Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", ProfilFormularz.z(profile.pobierz()));
        }
        return "profil/formularz";
    }

    @PostMapping("/profil")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ')")
    public String zapisz(
            @Valid @ModelAttribute("formularz") ProfilFormularz formularz,
            BindingResult wynik,
            RedirectAttributes atrybuty
    ) {
        if (wynik.hasErrors()) {
            return "profil/formularz";
        }
        profile.zapisz(formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano profil parafii.");
        return "redirect:/profil";
    }
}
