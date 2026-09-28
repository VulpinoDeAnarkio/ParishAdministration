package pl.kea.parafia.postep;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
@RequestMapping("/postep")
public class PostepController {

    private final PostepService postep;
    private final OsobaService osoby;

    public PostepController(PostepService postep, OsobaService osoby) {
        this.postep = postep;
        this.osoby = osoby;
    }

    @ModelAttribute("zrodla")
    public ZrodloCelu[] zrodla() {
        return ZrodloCelu.values();
    }

    @GetMapping
    public String pulpit(Model model) {
        model.addAttribute("wyniki", postep.wyniki());
        model.addAttribute("roczniki", postep.roczniki());
        return "postep/pulpit";
    }

    @GetMapping("/cele/nowy")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ')")
    public String nowyCel(Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", new CelFormularz("", "", ZrodloCelu.LICZBA_CZLONKOW, new BigDecimal("100"), null, LocalDate.now().getYear(), true));
        }
        return "postep/cel";
    }

    @PostMapping("/cele")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ')")
    public String utworzCel(@Valid @ModelAttribute("formularz") CelFormularz formularz, BindingResult wynik, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            return "postep/cel";
        }
        postep.zapiszCel(formularz);
        atrybuty.addFlashAttribute("sukces", "Dodano cel. Wartość odświeża się z danych parafii.");
        return "redirect:/postep";
    }

    @GetMapping("/nauka")
    public String nauka(Model model) {
        model.addAttribute("roczniki", postep.roczniki());
        return "postep/nauka";
    }

    @PostMapping("/nauka")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','DUSZPASTERZ','SEKRETARIAT')")
    public String nowyRocznik(@RequestParam String nazwa, @RequestParam int rok, RedirectAttributes atrybuty) {
        Rocznik zapisany = postep.zapiszRocznik(nazwa, rok);
        atrybuty.addFlashAttribute("sukces", "Utworzono rocznik.");
        return "redirect:/postep/nauka/" + zapisany.getId();
    }

    @GetMapping("/nauka/{id}")
    public String rocznik(@PathVariable Long id, Model model) {
        model.addAttribute("rocznik", postep.znajdzRocznik(id));
        model.addAttribute("uczestnicy", postep.uczestnicy(id));
        model.addAttribute("lekcje", postep.lekcje(id));
        model.addAttribute("osoby", osoby.wszyscy());
        return "postep/rocznik";
    }

    @PostMapping("/nauka/{id}/uczestnicy")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','DUSZPASTERZ','SEKRETARIAT')")
    public String uczestnik(@PathVariable Long id, @RequestParam Long osobaId, RedirectAttributes atrybuty) {
        postep.dodajUczestnika(id, osobaId);
        atrybuty.addFlashAttribute("sukces", "Dopisano do nauki konfirmacyjnej.");
        return "redirect:/postep/nauka/" + id;
    }

    @PostMapping("/nauka/{id}/lekcje")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','DUSZPASTERZ','SEKRETARIAT')")
    public String lekcja(@PathVariable Long id, @RequestParam LocalDate dataLekcji, @RequestParam String temat, RedirectAttributes atrybuty) {
        postep.dodajLekcje(id, dataLekcji, temat);
        atrybuty.addFlashAttribute("sukces", "Dodano lekcję.");
        return "redirect:/postep/nauka/" + id;
    }

    @GetMapping("/lekcje/{id}")
    public String lekcja(@PathVariable Long id, Model model) {
        Lekcja lekcja = postep.znajdzLekcje(id);
        model.addAttribute("lekcja", lekcja);
        model.addAttribute("uczestnicy", postep.uczestnicy(lekcja.getRocznik().getId()));
        model.addAttribute("obecnosci", postep.obecnosciLekcji(id));
        return "postep/lekcja";
    }

    @PostMapping("/lekcje/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','DUSZPASTERZ','SEKRETARIAT')")
    public String obecnosc(@PathVariable Long id, @RequestParam(required = false) List<Long> osoby, @RequestParam(required = false) List<Long> obecni, RedirectAttributes atrybuty) {
        postep.zapiszObecnoscLekcji(id, osoby, obecni);
        atrybuty.addFlashAttribute("sukces", "Zapisano obecność na lekcji.");
        return "redirect:/postep/lekcje/" + id;
    }
}
