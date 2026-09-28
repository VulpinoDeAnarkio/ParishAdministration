package pl.kea.parafia.bezpieczenstwo;

import jakarta.validation.Valid;
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

@Controller
@RequestMapping("/uzytkownicy")
@PreAuthorize("hasRole('ADMIN')")
public class UzytkownikController {

    private final UzytkownikService usluga;

    public UzytkownikController(UzytkownikService usluga) {
        this.usluga = usluga;
    }

    @ModelAttribute("role")
    public Rola[] role() {
        return Rola.values();
    }

    @GetMapping
    public String lista(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("strona", usluga.lista(PageRequest.of(page, 20, Sort.by("nazwisko", "imie"))));
        return "uzytkownicy/lista";
    }

    @GetMapping("/nowy")
    public String nowy(Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", UzytkownikFormularz.pusty());
        }
        model.addAttribute("edycja", false);
        return "uzytkownicy/formularz";
    }

    @PostMapping
    public String utworz(
            @Valid @ModelAttribute("formularz") UzytkownikFormularz formularz,
            BindingResult wynik,
            Model model,
            RedirectAttributes atrybuty
    ) {
        if (formularz.haslo() == null || formularz.haslo().length() < 8) {
            wynik.rejectValue("haslo", "haslo", "Hasło musi mieć co najmniej 8 znaków.");
        }
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", false);
            return "uzytkownicy/formularz";
        }
        usluga.utworz(formularz);
        atrybuty.addFlashAttribute("sukces", "Dodano konto pracownika.");
        return "redirect:/uzytkownicy";
    }

    @GetMapping("/{id}/edycja")
    public String edycja(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", UzytkownikFormularz.z(usluga.znajdz(id)));
        }
        model.addAttribute("edycja", true);
        model.addAttribute("identyfikator", id);
        return "uzytkownicy/formularz";
    }

    @PostMapping("/{id}")
    public String zapisz(
            @PathVariable Long id,
            @Valid @ModelAttribute("formularz") UzytkownikFormularz formularz,
            BindingResult wynik,
            Model model,
            RedirectAttributes atrybuty
    ) {
        if (formularz.haslo() != null && !formularz.haslo().isBlank() && formularz.haslo().length() < 8) {
            wynik.rejectValue("haslo", "haslo", "Hasło musi mieć co najmniej 8 znaków.");
        }
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", true);
            model.addAttribute("identyfikator", id);
            return "uzytkownicy/formularz";
        }
        usluga.aktualizuj(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano konto.");
        return "redirect:/uzytkownicy";
    }
}
