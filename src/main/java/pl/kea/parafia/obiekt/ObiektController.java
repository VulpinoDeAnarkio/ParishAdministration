package pl.kea.parafia.obiekt;

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
@RequestMapping("/obiekty")
public class ObiektController {

    private final ObiektService obiekty;
    private final OsobaService osoby;

    public ObiektController(ObiektService obiekty, OsobaService osoby) {
        this.obiekty = obiekty;
        this.osoby = osoby;
    }

    @ModelAttribute("rodzaje")
    public RodzajObiektu[] rodzaje() {
        return RodzajObiektu.values();
    }

    @GetMapping
    public String lista(@RequestParam(required = false) RodzajObiektu rodzaj, Model model) {
        model.addAttribute("obiekty", obiekty.lista(rodzaj));
        model.addAttribute("wybranyRodzaj", rodzaj);
        model.addAttribute("rezerwacje", obiekty.nadchodzace());
        return "obiekty/lista";
    }

    @GetMapping("/nowy")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String nowy(Model model) {
        przygotujObiekt(model, ObiektFormularz.pusty(), false, null);
        return "obiekty/formularz";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String utworz(@Valid @ModelAttribute("formularz") ObiektFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            przygotujObiekt(model, formularz, false, null);
            return "obiekty/formularz";
        }
        Obiekt obiekt = obiekty.zapisz(null, formularz);
        atrybuty.addFlashAttribute("sukces", "Dodano obiekt parafii.");
        return "redirect:/obiekty/" + obiekt.getId();
    }

    @GetMapping("/{id}")
    public String szczegoly(@PathVariable Long id, Model model) {
        przygotujSzczegoly(id, model);
        return "obiekty/szczegoly";
    }

    @GetMapping("/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String edycja(@PathVariable Long id, Model model) {
        przygotujObiekt(model, ObiektFormularz.z(obiekty.znajdz(id)), true, id);
        return "obiekty/formularz";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String zapisz(@PathVariable Long id, @Valid @ModelAttribute("formularz") ObiektFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            przygotujObiekt(model, formularz, true, id);
            return "obiekty/formularz";
        }
        obiekty.zapisz(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano obiekt.");
        return "redirect:/obiekty/" + id;
    }

    @PostMapping("/{id}/rezerwacje")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String dodajRezerwacje(
            @PathVariable Long id,
            @Valid @ModelAttribute("rezerwacja") RezerwacjaFormularz formularz,
            BindingResult wynik,
            Model model,
            RedirectAttributes atrybuty
    ) {
        if (wynik.hasErrors()) {
            przygotujSzczegoly(id, model);
            return "obiekty/szczegoly";
        }
        obiekty.zapiszRezerwacje(null, new RezerwacjaFormularz(
                id,
                formularz.poczatek(),
                formularz.koniec(),
                formularz.rezerwujacy(),
                formularz.osobaId(),
                formularz.cel(),
                formularz.liczbaOsob(),
                formularz.uwagi()
        ));
        atrybuty.addFlashAttribute("sukces", "Zapisano rezerwację.");
        return "redirect:/obiekty/" + id;
    }

    @GetMapping("/rezerwacje/{id}/edycja")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String edycjaRezerwacji(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", RezerwacjaFormularz.z(obiekty.znajdzRezerwacje(id)));
        }
        model.addAttribute("identyfikator", id);
        model.addAttribute("obiekty", obiekty.lista(null));
        model.addAttribute("osoby", osoby.wszyscy());
        return "obiekty/rezerwacja";
    }

    @PostMapping("/rezerwacje/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String zapiszRezerwacje(
            @PathVariable Long id,
            @Valid @ModelAttribute("formularz") RezerwacjaFormularz formularz,
            BindingResult wynik,
            Model model,
            RedirectAttributes atrybuty
    ) {
        if (wynik.hasErrors()) {
            model.addAttribute("identyfikator", id);
            model.addAttribute("obiekty", obiekty.lista(null));
            model.addAttribute("osoby", osoby.wszyscy());
            return "obiekty/rezerwacja";
        }
        RezerwacjaObiektu zapisana = obiekty.zapiszRezerwacje(id, formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano rezerwację.");
        return "redirect:/obiekty/" + zapisana.getObiekt().getId();
    }

    @PostMapping("/rezerwacje/{id}/anuluj")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String anuluj(@PathVariable Long id, RedirectAttributes atrybuty) {
        RezerwacjaObiektu rezerwacja = obiekty.znajdzRezerwacje(id);
        obiekty.anuluj(id);
        atrybuty.addFlashAttribute("sukces", "Anulowano rezerwację.");
        return "redirect:/obiekty/" + rezerwacja.getObiekt().getId();
    }

    private void przygotujObiekt(Model model, ObiektFormularz formularz, boolean edycja, Long id) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", formularz);
        }
        model.addAttribute("edycja", edycja);
        model.addAttribute("identyfikator", id);
        model.addAttribute("obiekty", obiekty.lista(null));
    }

    private void przygotujSzczegoly(Long id, Model model) {
        model.addAttribute("obiekt", obiekty.znajdz(id));
        model.addAttribute("dzieci", obiekty.dzieci(id));
        model.addAttribute("rezerwacje", obiekty.rezerwacjeObiektu(id));
        if (!model.containsAttribute("rezerwacja")) {
            model.addAttribute("rezerwacja", RezerwacjaFormularz.pusty(id));
        }
        model.addAttribute("osoby", osoby.wszyscy());
    }
}
