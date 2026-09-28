package pl.kea.parafia.kalendarz;

import jakarta.validation.Valid;
import java.time.YearMonth;
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
@RequestMapping("/kalendarz")
public class KalendarzController {

    private final KalendarzService kalendarz;

    public KalendarzController(KalendarzService kalendarz) {
        this.kalendarz = kalendarz;
    }

    @ModelAttribute("rodzaje")
    public RodzajWydarzenia[] rodzaje() {
        return RodzajWydarzenia.values();
    }

    @GetMapping
    public String miesiac(@RequestParam(required = false) Integer rok, @RequestParam(required = false) Integer miesiac, Model model) {
        YearMonth okres = rok == null || miesiac == null ? YearMonth.now() : YearMonth.of(rok, miesiac);
        model.addAttribute("okres", okres);
        model.addAttribute("nazwa", kalendarz.nazwaMiesiaca(okres));
        model.addAttribute("poprzedni", okres.minusMonths(1));
        model.addAttribute("nastepny", okres.plusMonths(1));
        model.addAttribute("tygodnie", kalendarz.miesiac(okres));
        model.addAttribute("nadchodzace", kalendarz.nadchodzace());
        return "kalendarz/miesiac";
    }

    @GetMapping("/nowe")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String nowe(Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", WydarzenieFormularz.pusty());
        }
        model.addAttribute("edycja", false);
        model.addAttribute("sale", kalendarz.sale());
        return "kalendarz/formularz";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String utworz(@Valid @ModelAttribute("formularz") WydarzenieFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        return zapisz(null, formularz, wynik, model, atrybuty);
    }

    @GetMapping("/{id}")
    public String szczegoly(@PathVariable Long id, Model model) {
        model.addAttribute("wydarzenie", kalendarz.znajdz(id));
        return "kalendarz/szczegoly";
    }

    @GetMapping("/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String edycja(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", WydarzenieFormularz.z(kalendarz.znajdz(id)));
        }
        model.addAttribute("edycja", true);
        model.addAttribute("identyfikator", id);
        model.addAttribute("sale", kalendarz.sale());
        return "kalendarz/formularz";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String aktualizuj(@PathVariable Long id, @Valid @ModelAttribute("formularz") WydarzenieFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        return zapisz(id, formularz, wynik, model, atrybuty);
    }

    @PostMapping("/{id}/usun")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String usun(@PathVariable Long id, RedirectAttributes atrybuty) {
        kalendarz.usun(id);
        atrybuty.addFlashAttribute("sukces", "Usunięto wydarzenie.");
        return "redirect:/kalendarz";
    }

    @PostMapping("/sale")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String sala(@Valid SalaFormularz formularz, BindingResult wynik, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            atrybuty.addFlashAttribute("blad", "Podaj nazwę sali.");
            return "redirect:/kalendarz/nowe";
        }
        kalendarz.dodajSale(formularz);
        atrybuty.addFlashAttribute("sukces", "Dodano salę.");
        return "redirect:/kalendarz/nowe";
    }

    private String zapisz(Long id, WydarzenieFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", id != null);
            model.addAttribute("identyfikator", id);
            model.addAttribute("sale", kalendarz.sale());
            return "kalendarz/formularz";
        }
        Wydarzenie zapisane = kalendarz.zapisz(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano wydarzenie.");
        return "redirect:/kalendarz/" + zapisane.getId();
    }
}
