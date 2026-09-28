package pl.kea.parafia.ogloszenie;

import jakarta.validation.Valid;
import java.time.LocalDate;
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
public class OgloszenieController {

    private final OgloszenieService ogloszenia;

    public OgloszenieController(OgloszenieService ogloszenia) {
        this.ogloszenia = ogloszenia;
    }

    @GetMapping("/ogloszenia")
    public String lista(Model model) {
        model.addAttribute("ogloszenia", ogloszenia.wszystkie());
        return "ogloszenie/lista";
    }

    @GetMapping("/ogloszenia-publiczne")
    public String tablica(Model model) {
        model.addAttribute("ogloszenia", ogloszenia.publiczne(LocalDate.now()));
        return "ogloszenie/publiczne";
    }

    @GetMapping("/ogloszenia/nowe")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String nowe(Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", OgloszenieFormularz.puste());
        }
        return "ogloszenie/formularz";
    }

    @PostMapping("/ogloszenia")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String utworz(@Valid @ModelAttribute("formularz") OgloszenieFormularz formularz, BindingResult wynik, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            return "ogloszenie/formularz";
        }
        ogloszenia.zapisz(null, formularz);
        atrybuty.addFlashAttribute("sukces", "Opublikowano ogłoszenie.");
        return "redirect:/ogloszenia";
    }

    @GetMapping("/ogloszenia/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String edycja(@PathVariable Long id, Model model) {
        Ogloszenie ogloszenie = ogloszenia.znajdz(id);
        model.addAttribute("formularz", new OgloszenieFormularz(
                ogloszenie.getTytul(), ogloszenie.getTresc(), ogloszenie.getObowiazujeOd(),
                ogloszenie.getObowiazujeDo(), ogloszenie.isWidocznePublicznie()
        ));
        model.addAttribute("identyfikator", id);
        return "ogloszenie/formularz";
    }

    @PostMapping("/ogloszenia/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String aktualizuj(@PathVariable Long id, @Valid @ModelAttribute("formularz") OgloszenieFormularz formularz, BindingResult wynik, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            return "ogloszenie/formularz";
        }
        ogloszenia.zapisz(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano ogłoszenie.");
        return "redirect:/ogloszenia";
    }
}
