package pl.kea.parafia.pulpit;

import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.kea.parafia.kalendarz.KalendarzService;
import pl.kea.parafia.ksiega.KsiegaService;
import pl.kea.parafia.obiekt.ObiektService;
import pl.kea.parafia.ogloszenie.OgloszenieService;
import pl.kea.parafia.osoba.OsobaService;
import pl.kea.parafia.postep.PostepService;
import pl.kea.parafia.sluzba.SluzbaService;

@Controller
public class WejscieController {

    private final OsobaService osoby;
    private final KsiegaService ksiegi;
    private final KalendarzService kalendarz;
    private final OgloszenieService ogloszenia;
    private final PostepService postep;
    private final SluzbaService sluzba;
    private final ObiektService obiekty;

    public WejscieController(
            OsobaService osoby,
            KsiegaService ksiegi,
            KalendarzService kalendarz,
            OgloszenieService ogloszenia,
            PostepService postep,
            SluzbaService sluzba,
            ObiektService obiekty
    ) {
        this.osoby = osoby;
        this.ksiegi = ksiegi;
        this.kalendarz = kalendarz;
        this.ogloszenia = ogloszenia;
        this.postep = postep;
        this.sluzba = sluzba;
        this.obiekty = obiekty;
    }

    @GetMapping("/login")
    public String logowanie() {
        return "logowanie";
    }

    @GetMapping("/")
    public String pulpit(Model model) {
        model.addAttribute("czlonkowie", osoby.liczbaCzlonkow());
        model.addAttribute("chrzty", ksiegi.liczbaChrztow());
        model.addAttribute("konfirmacje", ksiegi.liczbaKonfirmacji());
        model.addAttribute("zadania", sluzba.otwarteZadania());
        model.addAttribute("wydarzenia", kalendarz.nadchodzace());
        model.addAttribute("ogloszenia", ogloszenia.publiczne(LocalDate.now()));
        model.addAttribute("cele", postep.wyniki().stream().limit(3).toList());
        model.addAttribute("rezerwacje", obiekty.nadchodzace());
        return "pulpit/index";
    }

    @GetMapping("/brak-dostepu")
    public String brakDostepu() {
        return "error/403";
    }
}
