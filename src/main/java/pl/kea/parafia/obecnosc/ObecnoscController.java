package pl.kea.parafia.obecnosc;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kea.parafia.grupa.GrupaService;
import pl.kea.parafia.nabozenstwo.NabozenstwoService;
import pl.kea.parafia.osoba.OsobaService;

@Controller
@RequestMapping("/obecnosci")
public class ObecnoscController {

    private final ObecnoscService obecnosci;
    private final NabozenstwoService nabozenstwa;
    private final GrupaService grupy;
    private final OsobaService osoby;

    public ObecnoscController(ObecnoscService obecnosci, NabozenstwoService nabozenstwa, GrupaService grupy, OsobaService osoby) {
        this.obecnosci = obecnosci;
        this.nabozenstwa = nabozenstwa;
        this.grupy = grupy;
        this.osoby = osoby;
    }

    @GetMapping
    public String indeks(Model model) {
        model.addAttribute("nabozenstwa", nabozenstwa.najnowsze());
        model.addAttribute("grupy", grupy.lista());
        return "obecnosc/indeks";
    }

    @GetMapping("/nabozenstwo/{id}")
    public String nabozenstwo(@PathVariable Long id, Model model) {
        return karta(RodzajObecnosci.NABOZENSTWO, id, model);
    }

    @PostMapping("/nabozenstwo/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String zapiszNabozenstwo(@PathVariable Long id, @RequestParam(required = false) List<Long> osoby, @RequestParam(required = false) List<Long> obecni, RedirectAttributes atrybuty) {
        return zapisz(RodzajObecnosci.NABOZENSTWO, id, osoby, obecni, atrybuty);
    }

    @GetMapping("/grupa/{id}")
    public String grupa(@PathVariable Long id, Model model) {
        return karta(RodzajObecnosci.GRUPA, id, model);
    }

    @PostMapping("/grupa/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String zapiszGrupe(@PathVariable Long id, @RequestParam(required = false) List<Long> osoby, @RequestParam(required = false) List<Long> obecni, RedirectAttributes atrybuty) {
        return zapisz(RodzajObecnosci.GRUPA, id, osoby, obecni, atrybuty);
    }

    @GetMapping("/wydarzenie/{id}")
    public String wydarzenie(@PathVariable Long id, Model model) {
        model.addAttribute("osoby", osoby.wszyscy());
        return karta(RodzajObecnosci.WYDARZENIE, id, model);
    }

    @PostMapping("/wydarzenie/{id}/dopisz")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String dopisz(@PathVariable Long id, @RequestParam Long osobaId, RedirectAttributes atrybuty) {
        obecnosci.zapisz(RodzajObecnosci.WYDARZENIE, id, List.of(osobaId), List.of(osobaId));
        atrybuty.addFlashAttribute("sukces", "Dopisano osobę do listy.");
        return "redirect:/obecnosci/wydarzenie/" + id;
    }

    @PostMapping("/wydarzenie/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SEKRETARIAT','DUSZPASTERZ')")
    public String zapiszWydarzenie(@PathVariable Long id, @RequestParam(required = false) List<Long> osoby, @RequestParam(required = false) List<Long> obecni, RedirectAttributes atrybuty) {
        return zapisz(RodzajObecnosci.WYDARZENIE, id, osoby, obecni, atrybuty);
    }

    private String karta(RodzajObecnosci rodzaj, Long id, Model model) {
        model.addAttribute("rodzaj", rodzaj);
        model.addAttribute("celId", id);
        model.addAttribute("tytul", obecnosci.tytul(rodzaj, id));
        model.addAttribute("wiersze", obecnosci.lista(rodzaj, id));
        long obecnych = obecnosci.lista(rodzaj, id).stream().filter(WierszObecnosci::obecny).count();
        model.addAttribute("obecnych", obecnych);
        return "obecnosc/karta";
    }

    private String zapisz(RodzajObecnosci rodzaj, Long id, List<Long> osoby, List<Long> obecni, RedirectAttributes atrybuty) {
        obecnosci.zapisz(rodzaj, id, osoby, obecni);
        atrybuty.addFlashAttribute("sukces", "Zapisano obecności.");
        String sciezka = switch (rodzaj) {
            case NABOZENSTWO -> "/obecnosci/nabozenstwo/";
            case GRUPA -> "/obecnosci/grupa/";
            case WYDARZENIE -> "/obecnosci/wydarzenie/";
        };
        return "redirect:" + sciezka + id;
    }
}
