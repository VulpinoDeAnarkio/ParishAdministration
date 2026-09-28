package pl.kea.parafia.konfiguracja;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import pl.kea.parafia.duszpasterstwo.DuszpasterstwoService;
import pl.kea.parafia.duszpasterstwo.SprawaFormularz;
import pl.kea.parafia.duszpasterstwo.StatusSprawy;
import pl.kea.parafia.duszpasterstwo.WizytaFormularz;
import pl.kea.parafia.finanse.FinanseService;
import pl.kea.parafia.finanse.OperacjaFormularz;
import pl.kea.parafia.finanse.RodzajOperacji;
import pl.kea.parafia.grupa.CzlonekFormularz;
import pl.kea.parafia.grupa.GrupaFormularz;
import pl.kea.parafia.grupa.GrupaService;
import pl.kea.parafia.grupa.RodzajGrupy;
import pl.kea.parafia.ksiega.ChrzestFormularz;
import pl.kea.parafia.ksiega.KonfirmacjaFormularz;
import pl.kea.parafia.ksiega.KsiegaService;
import pl.kea.parafia.nabozenstwo.MomentPiesni;
import pl.kea.parafia.nabozenstwo.Nabozenstwo;
import pl.kea.parafia.nabozenstwo.NabozenstwoFormularz;
import pl.kea.parafia.nabozenstwo.NabozenstwoService;
import pl.kea.parafia.nabozenstwo.PiesnFormularz;
import pl.kea.parafia.nabozenstwo.RodzajNabozenstwa;
import pl.kea.parafia.obecnosc.ObecnoscService;
import pl.kea.parafia.obecnosc.RodzajObecnosci;
import pl.kea.parafia.ogloszenie.OgloszenieFormularz;
import pl.kea.parafia.ogloszenie.OgloszenieService;
import pl.kea.parafia.osoba.Gospodarstwo;
import pl.kea.parafia.osoba.GospodarstwoFormularz;
import pl.kea.parafia.osoba.GospodarstwoService;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaFormularz;
import pl.kea.parafia.osoba.OsobaRepository;
import pl.kea.parafia.osoba.OsobaService;
import pl.kea.parafia.osoba.Plec;
import pl.kea.parafia.osoba.RolaWRodzinie;
import pl.kea.parafia.osoba.StatusCzlonkostwa;
import pl.kea.parafia.postep.CelFormularz;
import pl.kea.parafia.postep.PostepService;
import pl.kea.parafia.postep.Rocznik;
import pl.kea.parafia.postep.ZrodloCelu;
import pl.kea.parafia.sluzba.DyzurFormularz;
import pl.kea.parafia.sluzba.RolaSluzby;
import pl.kea.parafia.sluzba.SluzbaService;
import pl.kea.parafia.sluzba.StatusZadania;
import pl.kea.parafia.sluzba.ZadanieFormularz;

@Component
@ConditionalOnProperty(name = "parafia.demo", havingValue = "true")
public class DemoZboru implements ApplicationRunner {

    private final OsobaRepository osobyRepo;
    private final OsobaService osoby;
    private final GospodarstwoService gospodarstwa;
    private final KsiegaService ksiegi;
    private final GrupaService grupy;
    private final NabozenstwoService nabozenstwa;
    private final ObecnoscService obecnosci;
    private final FinanseService finanse;
    private final DuszpasterstwoService duszpasterstwo;
    private final SluzbaService sluzba;
    private final PostepService postep;
    private final OgloszenieService ogloszenia;

    public DemoZboru(
            OsobaRepository osobyRepo,
            OsobaService osoby,
            GospodarstwoService gospodarstwa,
            KsiegaService ksiegi,
            GrupaService grupy,
            NabozenstwoService nabozenstwa,
            ObecnoscService obecnosci,
            FinanseService finanse,
            DuszpasterstwoService duszpasterstwo,
            SluzbaService sluzba,
            PostepService postep,
            OgloszenieService ogloszenia
    ) {
        this.osobyRepo = osobyRepo;
        this.osoby = osoby;
        this.gospodarstwa = gospodarstwa;
        this.ksiegi = ksiegi;
        this.grupy = grupy;
        this.nabozenstwa = nabozenstwa;
        this.obecnosci = obecnosci;
        this.finanse = finanse;
        this.duszpasterstwo = duszpasterstwo;
        this.sluzba = sluzba;
        this.postep = postep;
        this.ogloszenia = ogloszenia;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (osobyRepo.count() > 0) {
            return;
        }
        Gospodarstwo dom = gospodarstwa.utworz(new GospodarstwoFormularz(
                "Berger", "Lipowa 12", "05-100", "Lipowa", "500100200", "Dom przy kościele"
        ));
        Osoba jan = osoba("Jan", "Berger", Plec.MEZCZYZNA, LocalDate.of(1978, 4, 12), StatusCzlonkostwa.CZLONEK, RolaWRodzinie.GLOWA, true, dom.getId());
        Osoba maria = osoba("Maria", "Berger", Plec.KOBIETA, LocalDate.of(1981, 9, 3), StatusCzlonkostwa.CZLONEK, RolaWRodzinie.MALZONEK, true, dom.getId());
        Osoba adam = osoba("Adam", "Berger", Plec.MEZCZYZNA, LocalDate.of(2011, 2, 2), StatusCzlonkostwa.GOSC, RolaWRodzinie.DZIECKO, false, dom.getId());

        ksiegi.zapiszChrzest(null, chrzest(jan.getId(), LocalDate.of(1978, 5, 20), "CH-1978-1", "Jan Berger", "Anna Berger"));
        ksiegi.zapiszChrzest(null, chrzest(maria.getId(), LocalDate.of(1981, 10, 4), "CH-1981-4", "Piotr Nowak", "Ewa Nowak"));
        ksiegi.zapiszChrzest(null, chrzest(adam.getId(), LocalDate.of(2011, 4, 10), "CH-2011-2", "Jan Berger", "Maria Berger"));
        ksiegi.zapiszKonfirmacje(null, new KonfirmacjaFormularz(
                jan.getId(), LocalDate.of(1993, 5, 16), "Kościół Świętej Trójcy w Lipowej", "ks. Marek Lis",
                "1993", "J 3,16", "K-1993-1", null
        ));
        ksiegi.zapiszKonfirmacje(null, new KonfirmacjaFormularz(
                maria.getId(), LocalDate.of(1996, 5, 19), "Kościół Świętej Trójcy w Lipowej", "ks. Marek Lis",
                "1996", "Ps 23,1", "K-1996-1", null
        ));

        var chor = grupy.zapisz(null, new GrupaFormularz(
                "Chór parafialny", RodzajGrupy.CHOR, "Próby w czwartek", "czwartek 18:30", "Salka parafialna", jan.getId(), true
        ));
        grupy.dodajCzlonka(chor.getId(), new CzlonekFormularz(jan.getId(), "Dyrygent", LocalDate.of(2015, 1, 1)));
        grupy.dodajCzlonka(chor.getId(), new CzlonekFormularz(maria.getId(), "Alt", LocalDate.of(2016, 9, 1)));

        Nabozenstwo nabozenstwo = nabozenstwa.zapisz(null, new NabozenstwoFormularz(
                LocalDate.now().plusDays(6), LocalTime.of(10, 0), 75, RodzajNabozenstwa.GLOWNE,
                "Nabożeństwo niedzielne", "ks. Marek Lis", "Iz 55,6–11", "Ps 23", "Rz 8,14–17", "J 3,16–21",
                "Dar wiary", "J 3,16", true, "Wieczerza Pańska dla konfirmowanych.",
                List.of(new PiesnFormularz("365", "Warownym grodem", MomentPiesni.WEJSCIE))
        ));
        obecnosci.zapisz(RodzajObecnosci.NABOZENSTWO, nabozenstwo.getId(), List.of(jan.getId(), maria.getId()), List.of(jan.getId(), maria.getId()));
        sluzba.dodajDyzur(new DyzurFormularz(nabozenstwo.getId(), jan.getId(), RolaSluzby.ORGANISTA));
        sluzba.zapiszZadanie(null, new ZadanieFormularz(
                "Ułożyć śpiewniki", "Przed niedzielnym nabożeństwem", LocalDate.now().plusDays(5),
                StatusZadania.DO_ZROBIENIA, maria.getId(), nabozenstwo.getWydarzenie().getId()
        ));

        Rocznik rocznik = postep.zapiszRocznik("Rocznik 2027", 2027);
        postep.dodajUczestnika(rocznik.getId(), adam.getId());
        var lekcja = postep.dodajLekcje(rocznik.getId(), LocalDate.now().minusDays(7), "Chrzest i obietnica");
        postep.zapiszObecnoscLekcji(lekcja.getId(), List.of(adam.getId()), List.of(adam.getId()));
        postep.zapiszCel(new CelFormularz(
                "Członkowie zboru", "Cel na ten rok", ZrodloCelu.LICZBA_CZLONKOW, new BigDecimal("20"), null, LocalDate.now().getYear(), true
        ));

        Long kasa = finanse.salda().stream()
                .filter(saldo -> "Kasa parafialna".equals(saldo.fundusz().getNazwa()))
                .findFirst()
                .orElseThrow()
                .fundusz()
                .getId();
        finanse.zapiszOperacje(new OperacjaFormularz(kasa, RodzajOperacji.TACA, new BigDecimal("186.50"), LocalDate.now().minusDays(1), "Taca niedzielna", null, null));
        finanse.zapiszOperacje(new OperacjaFormularz(kasa, RodzajOperacji.SKLADKA, new BigDecimal("80.00"), LocalDate.now().minusDays(3), null, null, jan.getId()));

        var sprawa = duszpasterstwo.zapiszSprawe(null, new SprawaFormularz(maria.getId(), "Rozmowa przed rocznicą ślubu", "Spotkanie na plebanii.", StatusSprawy.W_TOKU));
        duszpasterstwo.zapiszWizyte(new WizytaFormularz(sprawa.getId(), maria.getId(), LocalDate.now().minusDays(2), "Plebania", "Ustalono datę rocznicy.", LocalDate.now().plusDays(14)));

        ogloszenia.zapisz(null, new OgloszenieFormularz(
                "Nabożeństwo w niedzielę o 10:00",
                "Zapraszamy na nabożeństwo główne z Wieczerzą Pańską. Szkółka niedzielna w salce.",
                LocalDate.now().minusDays(1), null, true
        ));
    }

    private Osoba osoba(String imie, String nazwisko, Plec plec, LocalDate urodzenie, StatusCzlonkostwa status, RolaWRodzinie rola, boolean wieczerza, Long dom) {
        return osoby.utworz(new OsobaFormularz(
                imie, null, nazwisko, null, plec, urodzenie, "Lipowa", null, null, "Lipowa 12", "05-100", "Lipowa",
                status, rola, LocalDate.of(2010, 1, 1), null, true, wieczerza, null, dom
        ));
    }

    private ChrzestFormularz chrzest(Long osobaId, LocalDate data, String numer, String rodzic1, String rodzic2) {
        return new ChrzestFormularz(osobaId, data, "Kościół Świętej Trójcy w Lipowej", "ks. Marek Lis", rodzic1, rodzic2, null, null, numer, null);
    }
}
