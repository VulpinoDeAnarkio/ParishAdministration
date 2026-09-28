package pl.kea.parafia.duszpasterstwo;

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
import pl.kea.parafia.osoba.OsobaService;

@Controller
@RequestMapping("/duszpasterstwo")
@PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','DUSZPASTERZ')")
public class DuszpasterstwoController {

    private final DuszpasterstwoService duszpasterstwo;
    private final OsobaService osoby;

    public DuszpasterstwoController(DuszpasterstwoService duszpasterstwo, OsobaService osoby) {
        this.duszpasterstwo = duszpasterstwo;
        this.osoby = osoby;
    }

    @ModelAttribute("statusy")
    public StatusSprawy[] statusy() {
        return StatusSprawy.values();
    }

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("sprawy", duszpasterstwo.lista());
        model.addAttribute("wizyty", duszpasterstwo.ostatnieWizyty());
        return "duszpasterstwo/lista";
    }

    @GetMapping("/osoba/{id}")
    public String osoba(@PathVariable Long id, Model model) {
        model.addAttribute("osoba", osoby.znajdz(id));
        model.addAttribute("sprawy", duszpasterstwo.sprawyOsoby(id));
        return "duszpasterstwo/osoba";
    }

    @GetMapping("/nowa")
    public String nowa(Model model) {
        przygotuj(model);
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", new SprawaFormularz(null, "", "", StatusSprawy.OTWARTA));
        }
        return "duszpasterstwo/sprawa";
    }

    @PostMapping
    public String utworz(@Valid @ModelAttribute("formularz") SprawaFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            przygotuj(model);
            return "duszpasterstwo/sprawa";
        }
        SprawaDuszpasterska zapisana = duszpasterstwo.zapiszSprawe(null, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano sprawę. Notatki widzą tylko duszpasterze.");
        return "redirect:/duszpasterstwo/" + zapisana.getId();
    }

    @GetMapping("/{id}")
    public String szczegoly(@PathVariable Long id, Model model) {
        SprawaDuszpasterska sprawa = duszpasterstwo.znajdz(id);
        model.addAttribute("sprawa", sprawa);
        model.addAttribute("wizyty", duszpasterstwo.wizyty(id));
        if (!model.containsAttribute("wizyta")) {
            model.addAttribute("wizyta", new WizytaFormularz(id, sprawa.getOsoba().getId(), LocalDate.now(), "", "", null));
        }
        return "duszpasterstwo/szczegoly";
    }

    @PostMapping("/{id}/status")
    public String status(@PathVariable Long id, @Valid SprawaFormularz formularz, BindingResult wynik, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            atrybuty.addFlashAttribute("blad", "Nie udało się zmienić statusu.");
            return "redirect:/duszpasterstwo/" + id;
        }
        duszpasterstwo.zapiszSprawe(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zaktualizowano sprawę.");
        return "redirect:/duszpasterstwo/" + id;
    }

    @PostMapping("/wizyty")
    public String wizyta(@Valid @ModelAttribute("wizyta") WizytaFormularz formularz, BindingResult wynik, RedirectAttributes atrybuty) {
        String powrot = formularz.sprawaId() == null ? "/duszpasterstwo" : "/duszpasterstwo/" + formularz.sprawaId();
        if (wynik.hasErrors()) {
            atrybuty.addFlashAttribute("blad", "Uzupełnij datę wizyty.");
            return "redirect:" + powrot;
        }
        duszpasterstwo.zapiszWizyte(formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano wizytę.");
        return "redirect:" + powrot;
    }

    private void przygotuj(Model model) {
        model.addAttribute("osoby", osoby.wszyscy());
    }
}
