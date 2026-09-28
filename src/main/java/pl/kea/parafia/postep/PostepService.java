package pl.kea.parafia.postep;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.duszpasterstwo.DuszpasterstwoService;
import pl.kea.parafia.finanse.FinanseService;
import pl.kea.parafia.grupa.GrupaService;
import pl.kea.parafia.ksiega.ChrzestRepository;
import pl.kea.parafia.ksiega.KonfirmacjaRepository;
import pl.kea.parafia.obecnosc.ObecnoscService;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaRepository;
import pl.kea.parafia.osoba.OsobaService;
import pl.kea.parafia.osoba.StatusCzlonkostwa;
import pl.kea.parafia.sluzba.SluzbaService;
import pl.kea.parafia.wspolne.NieZnalezionoException;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class PostepService {

    private final CelRepository cele;
    private final RocznikRepository roczniki;
    private final UczestnikRocznikaRepository uczestnicy;
    private final LekcjaRepository lekcje;
    private final ObecnoscLekcjiRepository obecnosci;
    private final OsobaRepository osoby;
    private final OsobaService kartoteka;
    private final ChrzestRepository chrzty;
    private final KonfirmacjaRepository konfirmacje;
    private final ObecnoscService obecnoscNabozenstw;
    private final FinanseService finanse;
    private final DuszpasterstwoService duszpasterstwo;
    private final GrupaService grupy;
    private final SluzbaService sluzba;

    public PostepService(
            CelRepository cele,
            RocznikRepository roczniki,
            UczestnikRocznikaRepository uczestnicy,
            LekcjaRepository lekcje,
            ObecnoscLekcjiRepository obecnosci,
            OsobaRepository osoby,
            OsobaService kartoteka,
            ChrzestRepository chrzty,
            KonfirmacjaRepository konfirmacje,
            ObecnoscService obecnoscNabozenstw,
            FinanseService finanse,
            DuszpasterstwoService duszpasterstwo,
            GrupaService grupy,
            SluzbaService sluzba
    ) {
        this.cele = cele;
        this.roczniki = roczniki;
        this.uczestnicy = uczestnicy;
        this.lekcje = lekcje;
        this.obecnosci = obecnosci;
        this.osoby = osoby;
        this.kartoteka = kartoteka;
        this.chrzty = chrzty;
        this.konfirmacje = konfirmacje;
        this.obecnoscNabozenstw = obecnoscNabozenstw;
        this.finanse = finanse;
        this.duszpasterstwo = duszpasterstwo;
        this.grupy = grupy;
        this.sluzba = sluzba;
    }

    @Transactional(readOnly = true)
    public List<WynikCelu> wyniki() {
        return cele.findByAktywnyIsTrueOrderByRokDescNazwaAsc().stream()
                .map(cel -> {
                    BigDecimal aktualna = aktualna(cel);
                    return new WynikCelu(cel, aktualna, KalkulatorPostepu.procent(aktualna, cel.getWartoscDocelowa()));
                })
                .toList();
    }

    @Transactional
    public CelParafii zapiszCel(CelFormularz formularz) {
        CelParafii cel = new CelParafii();
        cel.setNazwa(formularz.nazwa());
        cel.setOpis(formularz.opis());
        cel.setZrodlo(formularz.zrodlo());
        cel.setWartoscDocelowa(formularz.wartoscDocelowa());
        cel.setWartoscReczna(formularz.wartoscReczna());
        cel.setRok(formularz.rok());
        cel.setAktywny(!Boolean.FALSE.equals(formularz.aktywny()));
        return cele.save(cel);
    }

    @Transactional(readOnly = true)
    public List<EtapDrogi> droga(Long osobaId) {
        Osoba osoba = osoby.findById(osobaId).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej osoby."));
        boolean wNauce = osoba.getStatus() == StatusCzlonkostwa.KATECHUMEN || uczestnicy.existsByOsobaId(osobaId);
        boolean sluzbaOsoby = grupy.sluzy(osobaId) || sluzba.pelniSluzbe(osobaId);
        return DrogaWiary.etapy(new DaneDrogi(
                chrzty.findByOsobaId(osobaId).isPresent(),
                wNauce,
                konfirmacje.findByOsobaId(osobaId).isPresent(),
                osoba.getStatus() == StatusCzlonkostwa.CZLONEK,
                osoba.isUczestniczyWWieczerzy(),
                sluzbaOsoby
        ));
    }

    @Transactional(readOnly = true)
    public List<Rocznik> roczniki() {
        return roczniki.findAllByOrderByRokDesc();
    }

    @Transactional(readOnly = true)
    public Rocznik znajdzRocznik(Long id) {
        return roczniki.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego rocznika."));
    }

    @Transactional(readOnly = true)
    public List<UczestnikRocznika> uczestnicy(Long rocznikId) {
        return uczestnicy.findByRocznikIdOrderByOsobaNazwiskoAsc(rocznikId);
    }

    @Transactional(readOnly = true)
    public List<Lekcja> lekcje(Long rocznikId) {
        return lekcje.findByRocznikIdOrderByDataLekcjiAsc(rocznikId);
    }

    @Transactional
    public Rocznik zapiszRocznik(String nazwa, int rok) {
        Rocznik rocznik = new Rocznik();
        rocznik.setNazwa(nazwa);
        rocznik.setRok(rok);
        return roczniki.save(rocznik);
    }

    @Transactional
    public void dodajUczestnika(Long rocznikId, Long osobaId) {
        if (uczestnicy.findByRocznikIdAndOsobaId(rocznikId, osobaId).isPresent()) {
            throw new RegulaParafiiException("Ta osoba jest już w roczniku.");
        }
        if (chrzty.findByOsobaId(osobaId).isEmpty()) {
            throw new RegulaParafiiException("Do nauki konfirmacyjnej przyjmujemy osoby ochrzczone.");
        }
        Osoba osoba = osoby.findById(osobaId).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej osoby."));
        if (!osoba.getStatus().jestWKartoteceCzynnej()) {
            throw new RegulaParafiiException("Osoba nie jest w czynnej kartotece.");
        }
        UczestnikRocznika uczestnik = new UczestnikRocznika();
        uczestnik.setRocznik(znajdzRocznik(rocznikId));
        uczestnik.setOsoba(osoba);
        if (osoba.getStatus() == StatusCzlonkostwa.DZIECKO_OCHRZCZONE || osoba.getStatus() == StatusCzlonkostwa.SYMPATYK) {
            osoba.setStatus(StatusCzlonkostwa.KATECHUMEN);
        }
        uczestnicy.save(uczestnik);
    }

    @Transactional
    public Lekcja dodajLekcje(Long rocznikId, java.time.LocalDate data, String temat) {
        Lekcja lekcja = new Lekcja();
        lekcja.setRocznik(znajdzRocznik(rocznikId));
        lekcja.setDataLekcji(data);
        lekcja.setTemat(temat);
        return lekcje.save(lekcja);
    }

    @Transactional(readOnly = true)
    public Lekcja znajdzLekcje(Long id) {
        return lekcje.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej lekcji."));
    }

    @Transactional(readOnly = true)
    public List<ObecnoscLekcji> obecnosciLekcji(Long lekcjaId) {
        return obecnosci.findByLekcjaId(lekcjaId);
    }

    @Transactional
    public void zapiszObecnoscLekcji(Long lekcjaId, List<Long> osobyNaLiscie, List<Long> obecni) {
        Lekcja lekcja = lekcje.findById(lekcjaId).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej lekcji."));
        java.util.Set<Long> zaznaczeni = obecni == null ? java.util.Set.of() : java.util.Set.copyOf(obecni);
        if (osobyNaLiscie == null) {
            return;
        }
        for (Long osobaId : osobyNaLiscie) {
            if (uczestnicy.findByRocznikIdAndOsobaId(lekcja.getRocznik().getId(), osobaId).isEmpty()) {
                continue;
            }
            ObecnoscLekcji wpis = obecnosci.findByLekcjaIdAndOsobaId(lekcjaId, osobaId).orElseGet(ObecnoscLekcji::new);
            wpis.setLekcja(lekcja);
            wpis.setOsoba(osoby.findById(osobaId).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej osoby.")));
            wpis.setObecny(zaznaczeni.contains(osobaId));
            obecnosci.save(wpis);
        }
    }

    @Transactional(readOnly = true)
    public int procentKonfirmacji() {
        return roczniki.findFirstByOrderByRokDesc()
                .map(rocznik -> {
                    List<UczestnikRocznika> lista = uczestnicy.findByRocznikIdOrderByOsobaNazwiskoAsc(rocznik.getId());
                    if (lista.isEmpty()) {
                        return 0;
                    }
                    long potwierdzonych = lista.stream()
                            .filter(uczestnik -> konfirmacje.findByOsobaId(uczestnik.getOsoba().getId()).isPresent())
                            .count();
                    return (int) Math.round(potwierdzonych * 100.0 / lista.size());
                })
                .orElse(0);
    }

    private BigDecimal aktualna(CelParafii cel) {
        return switch (cel.getZrodlo()) {
            case LICZBA_CZLONKOW -> BigDecimal.valueOf(kartoteka.liczbaCzlonkow());
            case SREDNIA_OBECNOSC -> BigDecimal.valueOf(obecnoscNabozenstw.sredniaObecnychNaNabozenstwie());
            case SUMA_WPLYWOW -> finanse.sumaWplywow();
            case KONFIRMACJA_PROCENT -> BigDecimal.valueOf(procentKonfirmacji());
            case WIZYTY -> BigDecimal.valueOf(duszpasterstwo.wizytyWRoku());
            case RECZNY -> cel.getWartoscReczna() == null ? BigDecimal.ZERO : cel.getWartoscReczna();
        };
    }
}
