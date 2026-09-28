package pl.kea.parafia.ksiega;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kea.parafia.osoba.OsobaService;

@Controller
@RequestMapping("/ksiegi")
public class KsiegaController {

    private final KsiegaService ksiegi;
    private final OsobaService osoby;

    public KsiegaController(KsiegaService ksiegi, OsobaService osoby) {
        this.ksiegi = ksiegi;
        this.osoby = osoby;
    }

    @GetMapping
    public String indeks(Model model) {
        model.addAttribute("chrzty", ksiegi.ostatnieChrzty());
        model.addAttribute("konfirmacje", ksiegi.ostatnieKonfirmacje());
        model.addAttribute("sluby", ksiegi.ostatnieSluby());
        model.addAttribute("pogrzeby", ksiegi.ostatniePogrzeby());
        model.addAttribute("przeniesienia", ksiegi.ostatniePrzeniesienia());
        return "ksiegi/indeks";
    }

    @GetMapping("/chrzty/nowy")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String nowyChrzest(@RequestParam(required = false) Long osobaId, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", ChrzestFormularz.pusty(osobaId, ksiegi.propozycjaNumeru("CH", ksiegi.liczbaChrztow())));
        }
        model.addAttribute("edycja", false);
        model.addAttribute("osoby", osoby.wszyscy());
        return "ksiegi/chrzest";
    }

    @GetMapping("/chrzty/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String edycjaChrztu(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", ChrzestFormularz.z(ksiegi.chrzest(id)));
        }
        model.addAttribute("edycja", true);
        model.addAttribute("identyfikator", id);
        model.addAttribute("osoby", osoby.wszyscy());
        return "ksiegi/chrzest";
    }

    @PostMapping("/chrzty")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String utworzChrzest(@Valid @ModelAttribute("formularz") ChrzestFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        return zapiszChrzest(null, formularz, wynik, model, atrybuty);
    }

    @PostMapping("/chrzty/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String aktualizujChrzest(@PathVariable Long id, @Valid @ModelAttribute("formularz") ChrzestFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        return zapiszChrzest(id, formularz, wynik, model, atrybuty);
    }

    @GetMapping("/konfirmacje/nowy")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String nowaKonfirmacja(@RequestParam(required = false) Long osobaId, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", KonfirmacjaFormularz.pusty(osobaId, ksiegi.propozycjaNumeru("K", ksiegi.liczbaKonfirmacji())));
        }
        model.addAttribute("edycja", false);
        model.addAttribute("osoby", osoby.wszyscy());
        return "ksiegi/konfirmacja";
    }

    @GetMapping("/konfirmacje/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String edycjaKonfirmacji(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", KonfirmacjaFormularz.z(ksiegi.konfirmacja(id)));
        }
        model.addAttribute("edycja", true);
        model.addAttribute("identyfikator", id);
        model.addAttribute("osoby", osoby.wszyscy());
        return "ksiegi/konfirmacja";
    }

    @PostMapping("/konfirmacje")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String utworzKonfirmacje(@Valid @ModelAttribute("formularz") KonfirmacjaFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", false);
            model.addAttribute("osoby", osoby.wszyscy());
            return "ksiegi/konfirmacja";
        }
        Konfirmacja wpis = ksiegi.zapiszKonfirmacje(null, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano konfirmację.");
        return "redirect:/osoby/" + wpis.getOsoba().getId();
    }

    @PostMapping("/konfirmacje/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String aktualizujKonfirmacje(@PathVariable Long id, @Valid @ModelAttribute("formularz") KonfirmacjaFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", true);
            model.addAttribute("identyfikator", id);
            model.addAttribute("osoby", osoby.wszyscy());
            return "ksiegi/konfirmacja";
        }
        ksiegi.zapiszKonfirmacje(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano konfirmację.");
        return "redirect:/ksiegi";
    }

    @GetMapping("/sluby/nowy")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String nowySlub(Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", SlubFormularz.pusty(ksiegi.propozycjaNumeru("S", ksiegi.liczbaSlubow())));
        }
        model.addAttribute("edycja", false);
        model.addAttribute("osoby", osoby.wszyscy());
        model.addAttribute("rodzaje", RodzajSlubu.values());
        return "ksiegi/slub";
    }

    @GetMapping("/sluby/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String edycjaSlubu(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", SlubFormularz.z(ksiegi.slub(id)));
        }
        model.addAttribute("edycja", true);
        model.addAttribute("identyfikator", id);
        model.addAttribute("osoby", osoby.wszyscy());
        model.addAttribute("rodzaje", RodzajSlubu.values());
        return "ksiegi/slub";
    }

    @PostMapping("/sluby")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String utworzSlub(@Valid @ModelAttribute("formularz") SlubFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        return zapiszSlub(null, formularz, wynik, model, atrybuty);
    }

    @PostMapping("/sluby/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String aktualizujSlub(@PathVariable Long id, @Valid @ModelAttribute("formularz") SlubFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        return zapiszSlub(id, formularz, wynik, model, atrybuty);
    }

    @GetMapping("/pogrzeby/nowy")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String nowyPogrzeb(@RequestParam(required = false) Long osobaId, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", PogrzebFormularz.pusty(osobaId, ksiegi.propozycjaNumeru("P", ksiegi.liczbaPogrzebow())));
        }
        model.addAttribute("edycja", false);
        model.addAttribute("osoby", osoby.wszyscy());
        return "ksiegi/pogrzeb";
    }

    @GetMapping("/pogrzeby/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String edycjaPogrzebu(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", PogrzebFormularz.z(ksiegi.pogrzeb(id)));
        }
        model.addAttribute("edycja", true);
        model.addAttribute("identyfikator", id);
        model.addAttribute("osoby", osoby.wszyscy());
        return "ksiegi/pogrzeb";
    }

    @PostMapping("/pogrzeby")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String utworzPogrzeb(@Valid @ModelAttribute("formularz") PogrzebFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", false);
            model.addAttribute("osoby", osoby.wszyscy());
            return "ksiegi/pogrzeb";
        }
        Pogrzeb wpis = ksiegi.zapiszPogrzeb(null, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano pogrzeb i oznaczono osobę jako zmarłą.");
        return "redirect:/osoby/" + wpis.getOsoba().getId();
    }

    @PostMapping("/pogrzeby/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String aktualizujPogrzeb(@PathVariable Long id, @Valid @ModelAttribute("formularz") PogrzebFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", true);
            model.addAttribute("identyfikator", id);
            model.addAttribute("osoby", osoby.wszyscy());
            return "ksiegi/pogrzeb";
        }
        ksiegi.zapiszPogrzeb(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano księgę zmarłych.");
        return "redirect:/ksiegi";
    }

    @GetMapping("/przeniesienia/nowy")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String nowePrzeniesienie(@RequestParam(required = false) Long osobaId, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", PrzeniesienieFormularz.pusty(osobaId, ksiegi.propozycjaNumeru("T", ksiegi.liczbaPrzeniesien())));
        }
        model.addAttribute("edycja", false);
        model.addAttribute("osoby", osoby.wszyscy());
        model.addAttribute("kierunki", KierunekPrzeniesienia.values());
        return "ksiegi/przeniesienie";
    }

    @GetMapping("/przeniesienia/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String edycjaPrzeniesienia(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", PrzeniesienieFormularz.z(ksiegi.przeniesienie(id)));
        }
        model.addAttribute("edycja", true);
        model.addAttribute("identyfikator", id);
        model.addAttribute("osoby", osoby.wszyscy());
        model.addAttribute("kierunki", KierunekPrzeniesienia.values());
        return "ksiegi/przeniesienie";
    }

    @PostMapping("/przeniesienia")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String utworzPrzeniesienie(@Valid @ModelAttribute("formularz") PrzeniesienieFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", false);
            model.addAttribute("osoby", osoby.wszyscy());
            model.addAttribute("kierunki", KierunekPrzeniesienia.values());
            return "ksiegi/przeniesienie";
        }
        Przeniesienie wpis = ksiegi.zapiszPrzeniesienie(null, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano przeniesienie.");
        return "redirect:/osoby/" + wpis.getOsoba().getId();
    }

    @PostMapping("/przeniesienia/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT')")
    public String aktualizujPrzeniesienie(@PathVariable Long id, @Valid @ModelAttribute("formularz") PrzeniesienieFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", true);
            model.addAttribute("identyfikator", id);
            model.addAttribute("osoby", osoby.wszyscy());
            model.addAttribute("kierunki", KierunekPrzeniesienia.values());
            return "ksiegi/przeniesienie";
        }
        ksiegi.zapiszPrzeniesienie(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano przeniesienie.");
        return "redirect:/ksiegi";
    }

    private String zapiszChrzest(Long id, ChrzestFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", id != null);
            model.addAttribute("identyfikator", id);
            model.addAttribute("osoby", osoby.wszyscy());
            return "ksiegi/chrzest";
        }
        Chrzest wpis = ksiegi.zapiszChrzest(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano chrzest.");
        return "redirect:/osoby/" + wpis.getOsoba().getId();
    }

    private String zapiszSlub(Long id, SlubFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            model.addAttribute("edycja", id != null);
            model.addAttribute("identyfikator", id);
            model.addAttribute("osoby", osoby.wszyscy());
            model.addAttribute("rodzaje", RodzajSlubu.values());
            return "ksiegi/slub";
        }
        ksiegi.zapiszSlub(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano ślub.");
        return "redirect:/ksiegi";
    }
}
