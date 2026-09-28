package pl.kea.parafia.finanse;

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
@RequestMapping("/finanse")
@PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SKARBNIK')")
public class FinanseController {

    private final FinanseService finanse;
    private final OsobaService osoby;

    public FinanseController(FinanseService finanse, OsobaService osoby) {
        this.finanse = finanse;
        this.osoby = osoby;
    }

    @ModelAttribute("rodzaje")
    public RodzajOperacji[] rodzaje() {
        return RodzajOperacji.values();
    }

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("salda", finanse.salda());
        model.addAttribute("ostatnie", finanse.ostatnie());
        model.addAttribute("sumaWplywow", finanse.sumaWplywow());
        return "finanse/lista";
    }

    @GetMapping("/fundusze/nowy")
    public String nowyFundusz(Model model) {
        model.addAttribute("formularz", new FunduszFormularz("", "", true));
        return "finanse/fundusz";
    }

    @PostMapping("/fundusze")
    public String utworzFundusz(@Valid @ModelAttribute("formularz") FunduszFormularz formularz, BindingResult wynik) {
        if (wynik.hasErrors()) {
            return "finanse/fundusz";
        }
        finanse.zapiszFundusz(null, formularz);
        return "redirect:/finanse";
    }

    @GetMapping("/fundusze/{id}")
    public String fundusz(@PathVariable Long id, Model model) {
        model.addAttribute("fundusz", finanse.znajdzFundusz(id));
        model.addAttribute("saldo", finanse.saldo(id));
        model.addAttribute("operacje", finanse.operacje(id));
        return "finanse/szczegoly";
    }

    @GetMapping("/operacje/nowa")
    public String nowaOperacja(Model model) {
        if (!model.containsAttribute("formularz")) {
            model.addAttribute("formularz", new OperacjaFormularz(null, RodzajOperacji.TACA, null, LocalDate.now(), "", "", null));
        }
        model.addAttribute("fundusze", finanse.salda());
        model.addAttribute("osoby", osoby.wszyscy());
        return "finanse/operacja";
    }

    @PostMapping("/operacje")
    public String utworzOperacje(@Valid @ModelAttribute("formularz") OperacjaFormularz formularz, BindingResult wynik, Model model, RedirectAttributes atrybuty) {
        if (wynik.hasErrors()) {
            model.addAttribute("fundusze", finanse.salda());
            model.addAttribute("osoby", osoby.wszyscy());
            return "finanse/operacja";
        }
        Operacja zapisana = finanse.zapiszOperacje(formularz);
        atrybuty.addFlashAttribute("sukces", "Zapisano operację.");
        return "redirect:/finanse/fundusze/" + zapisana.getFundusz().getId();
    }

    @PostMapping("/operacje/{id}/usun")
    public String usun(@PathVariable Long id, RedirectAttributes atrybuty) {
        finanse.usunOperacje(id);
        atrybuty.addFlashAttribute("sukces", "Usunięto operację.");
        return "redirect:/finanse";
    }
}
