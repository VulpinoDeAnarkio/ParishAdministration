package pl.kea.parafia.ksiega;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaRepository;
import pl.kea.parafia.osoba.StatusCzlonkostwa;
import pl.kea.parafia.wspolne.NieZnalezionoException;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class KsiegaService {

    private final ChrzestRepository chrzty;
    private final KonfirmacjaRepository konfirmacje;
    private final SlubRepository sluby;
    private final PogrzebRepository pogrzeby;
    private final PrzeniesienieRepository przeniesienia;
    private final OsobaRepository osoby;

    public KsiegaService(
            ChrzestRepository chrzty,
            KonfirmacjaRepository konfirmacje,
            SlubRepository sluby,
            PogrzebRepository pogrzeby,
            PrzeniesienieRepository przeniesienia,
            OsobaRepository osoby
    ) {
        this.chrzty = chrzty;
        this.konfirmacje = konfirmacje;
        this.sluby = sluby;
        this.pogrzeby = pogrzeby;
        this.przeniesienia = przeniesienia;
        this.osoby = osoby;
    }

    @Transactional(readOnly = true)
    public List<Chrzest> ostatnieChrzty() {
        return chrzty.findTop20ByOrderByDataChrztuDesc();
    }

    @Transactional(readOnly = true)
    public List<Konfirmacja> ostatnieKonfirmacje() {
        return konfirmacje.findTop20ByOrderByDataKonfirmacjiDesc();
    }

    @Transactional(readOnly = true)
    public List<Slub> ostatnieSluby() {
        return sluby.findTop20ByOrderByDataSlubuDesc();
    }

    @Transactional(readOnly = true)
    public List<Pogrzeb> ostatniePogrzeby() {
        return pogrzeby.findTop20ByOrderByDataZgonuDesc();
    }

    @Transactional(readOnly = true)
    public List<Przeniesienie> ostatniePrzeniesienia() {
        return przeniesienia.findTop20ByOrderByDataCzynnosciDesc();
    }

    @Transactional(readOnly = true)
    public List<WpisKsiegi> historia(Long osobaId) {
        List<WpisKsiegi> wpisy = new ArrayList<>();
        chrzty.findByOsobaId(osobaId).ifPresent(wpis -> wpisy.add(new WpisKsiegi(
                wpis.getDataChrztu(), "Chrzest", wpis.getMiejsce() + " · " + wpis.getNumerKsiegi(),
                "/ksiegi/chrzty/" + wpis.getId() + "/edycja")));
        konfirmacje.findByOsobaId(osobaId).ifPresent(wpis -> wpisy.add(new WpisKsiegi(
                wpis.getDataKonfirmacji(), "Konfirmacja", wpis.getMiejsce() + " · " + wpis.getNumerKsiegi(),
                "/ksiegi/konfirmacje/" + wpis.getId() + "/edycja")));
        for (Slub slub : sluby.findByOsobaAIdOrOsobaBId(osobaId, osobaId)) {
            String druga = slub.getOsobaA().getId().equals(osobaId)
                    ? slub.getOsobaB().getImieINazwisko()
                    : slub.getOsobaA().getImieINazwisko();
            wpisy.add(new WpisKsiegi(slub.getDataSlubu(), slub.getRodzaj().getEtykieta(),
                    "z " + druga + " · " + slub.getNumerKsiegi(), "/ksiegi/sluby/" + slub.getId() + "/edycja"));
        }
        pogrzeby.findByOsobaId(osobaId).ifPresent(wpis -> wpisy.add(new WpisKsiegi(
                wpis.getDataZgonu(), "Zgon i pogrzeb", wpis.getMiejsce() + " · " + wpis.getNumerKsiegi(),
                "/ksiegi/pogrzeby/" + wpis.getId() + "/edycja")));
        for (Przeniesienie wpis : przeniesienia.findByOsobaIdOrderByDataCzynnosciAsc(osobaId)) {
            wpisy.add(new WpisKsiegi(wpis.getDataCzynnosci(), wpis.getKierunek().getEtykieta(),
                    wpis.getParafia() + " · " + wpis.getNumerKsiegi(), "/ksiegi/przeniesienia/" + wpis.getId() + "/edycja"));
        }
        wpisy.sort(Comparator.comparing(WpisKsiegi::data));
        return wpisy;
    }

    public String propozycjaNumeru(String prefiks, long ile) {
        return prefiks + "/" + LocalDate.now().getYear() + "/" + (ile + 1);
    }

    @Transactional
    public Chrzest zapiszChrzest(Long id, ChrzestFormularz formularz) {
        Chrzest wpis = id == null ? new Chrzest() : chrzty.findById(id)
                .orElseThrow(() -> new NieZnalezionoException("Nie ma takiego chrztu."));
        if (id != null && !wpis.getOsoba().getId().equals(formularz.osobaId())) {
            throw new RegulaParafiiException("Nie zmieniamy osoby przypisanej do wpisu w księdze.");
        }
        Osoba osoba = osoba(formularz.osobaId());
        chrzty.findByOsobaId(osoba.getId())
                .filter(inny -> id == null || !inny.getId().equals(id))
                .ifPresent(inny -> {
                    throw new RegulaParafiiException("Ta osoba ma już wpis w księdze chrztów.");
                });
        sprawdzNumer(formularz.numerKsiegi(), id, chrzty.existsByNumerKsiegi(formularz.numerKsiegi()),
                id != null && chrzty.existsByNumerKsiegiAndIdNot(formularz.numerKsiegi(), id));
        wpis.setOsoba(osoba);
        wpis.setDataChrztu(formularz.dataChrztu());
        wpis.setMiejsce(formularz.miejsce());
        wpis.setDuchowny(formularz.duchowny());
        wpis.setRodzicPierwszy(formularz.rodzicPierwszy());
        wpis.setRodzicDrugi(formularz.rodzicDrugi());
        wpis.setChrzestnyPierwszy(formularz.chrzestnyPierwszy());
        wpis.setChrzestnyDrugi(formularz.chrzestnyDrugi());
        wpis.setNumerKsiegi(formularz.numerKsiegi());
        wpis.setUwagi(formularz.uwagi());
        if (osoba.getStatus() == StatusCzlonkostwa.GOSC || osoba.getStatus() == StatusCzlonkostwa.SYMPATYK) {
            osoba.setStatus(StatusCzlonkostwa.DZIECKO_OCHRZCZONE);
        }
        return chrzty.save(wpis);
    }

    @Transactional
    public Konfirmacja zapiszKonfirmacje(Long id, KonfirmacjaFormularz formularz) {
        Konfirmacja wpis = id == null ? new Konfirmacja() : konfirmacje.findById(id)
                .orElseThrow(() -> new NieZnalezionoException("Nie ma takiej konfirmacji."));
        if (id != null && !wpis.getOsoba().getId().equals(formularz.osobaId())) {
            throw new RegulaParafiiException("Nie zmieniamy osoby przypisanej do wpisu w księdze.");
        }
        Osoba osoba = osoba(formularz.osobaId());
        if (chrzty.findByOsobaId(osoba.getId()).isEmpty()) {
            throw new RegulaParafiiException("Konfirmacja wymaga wcześniejszego chrztu.");
        }
        konfirmacje.findByOsobaId(osoba.getId())
                .filter(inny -> id == null || !inny.getId().equals(id))
                .ifPresent(inny -> {
                    throw new RegulaParafiiException("Ta osoba ma już wpis w księdze konfirmacji.");
                });
        sprawdzNumer(formularz.numerKsiegi(), id, konfirmacje.existsByNumerKsiegi(formularz.numerKsiegi()),
                id != null && konfirmacje.existsByNumerKsiegiAndIdNot(formularz.numerKsiegi(), id));
        wpis.setOsoba(osoba);
        wpis.setDataKonfirmacji(formularz.dataKonfirmacji());
        wpis.setMiejsce(formularz.miejsce());
        wpis.setDuchowny(formularz.duchowny());
        wpis.setRocznik(formularz.rocznik());
        wpis.setWerset(formularz.werset());
        wpis.setNumerKsiegi(formularz.numerKsiegi());
        wpis.setUwagi(formularz.uwagi());
        if (osoba.getStatus() == StatusCzlonkostwa.DZIECKO_OCHRZCZONE
                || osoba.getStatus() == StatusCzlonkostwa.KATECHUMEN
                || osoba.getStatus() == StatusCzlonkostwa.SYMPATYK) {
            osoba.setStatus(StatusCzlonkostwa.CZLONEK);
            if (osoba.getDataPrzyjecia() == null) {
                osoba.setDataPrzyjecia(formularz.dataKonfirmacji());
            }
        }
        return konfirmacje.save(wpis);
    }

    @Transactional
    public Slub zapiszSlub(Long id, SlubFormularz formularz) {
        if (formularz.osobaAId().equals(formularz.osobaBId())) {
            throw new RegulaParafiiException("Ślub wymaga dwóch różnych osób.");
        }
        Slub wpis = id == null ? new Slub() : sluby.findById(id)
                .orElseThrow(() -> new NieZnalezionoException("Nie ma takiego ślubu."));
        Osoba pierwsza = osoba(formularz.osobaAId());
        Osoba druga = osoba(formularz.osobaBId());
        sprawdzWolnyStan(pierwsza, id, formularz.dataSlubu());
        sprawdzWolnyStan(druga, id, formularz.dataSlubu());
        sprawdzNumer(formularz.numerKsiegi(), id, sluby.existsByNumerKsiegi(formularz.numerKsiegi()),
                id != null && sluby.existsByNumerKsiegiAndIdNot(formularz.numerKsiegi(), id));
        wpis.setOsobaA(pierwsza);
        wpis.setOsobaB(druga);
        wpis.setDataSlubu(formularz.dataSlubu());
        wpis.setMiejsce(formularz.miejsce());
        wpis.setDuchowny(formularz.duchowny());
        wpis.setRodzaj(formularz.rodzaj());
        wpis.setSwiadekPierwszy(formularz.swiadekPierwszy());
        wpis.setSwiadekDrugi(formularz.swiadekDrugi());
        wpis.setNumerKsiegi(formularz.numerKsiegi());
        wpis.setUwagi(formularz.uwagi());
        return sluby.save(wpis);
    }

    @Transactional
    public Pogrzeb zapiszPogrzeb(Long id, PogrzebFormularz formularz) {
        Pogrzeb wpis = id == null ? new Pogrzeb() : pogrzeby.findById(id)
                .orElseThrow(() -> new NieZnalezionoException("Nie ma takiego pogrzebu."));
        if (id != null && !wpis.getOsoba().getId().equals(formularz.osobaId())) {
            throw new RegulaParafiiException("Nie zmieniamy osoby przypisanej do wpisu w księdze.");
        }
        if (formularz.dataPogrzebu() != null && formularz.dataPogrzebu().isBefore(formularz.dataZgonu())) {
            throw new RegulaParafiiException("Pogrzeb nie może być wcześniejszy niż data zgonu.");
        }
        Osoba osoba = osoba(formularz.osobaId());
        pogrzeby.findByOsobaId(osoba.getId())
                .filter(inny -> id == null || !inny.getId().equals(id))
                .ifPresent(inny -> {
                    throw new RegulaParafiiException("Ta osoba ma już wpis w księdze zmarłych.");
                });
        sprawdzNumer(formularz.numerKsiegi(), id, pogrzeby.existsByNumerKsiegi(formularz.numerKsiegi()),
                id != null && pogrzeby.existsByNumerKsiegiAndIdNot(formularz.numerKsiegi(), id));
        wpis.setOsoba(osoba);
        wpis.setDataZgonu(formularz.dataZgonu());
        wpis.setDataPogrzebu(formularz.dataPogrzebu());
        wpis.setMiejsce(formularz.miejsce());
        wpis.setCmentarz(formularz.cmentarz());
        wpis.setDuchowny(formularz.duchowny());
        wpis.setNumerKsiegi(formularz.numerKsiegi());
        wpis.setUwagi(formularz.uwagi());
        osoba.setStatus(StatusCzlonkostwa.ZMARLY);
        osoba.setDataWystapienia(formularz.dataZgonu());
        return pogrzeby.save(wpis);
    }

    @Transactional
    public Przeniesienie zapiszPrzeniesienie(Long id, PrzeniesienieFormularz formularz) {
        Przeniesienie wpis = id == null ? new Przeniesienie() : przeniesienia.findById(id)
                .orElseThrow(() -> new NieZnalezionoException("Nie ma takiego przeniesienia."));
        Osoba osoba = osoba(formularz.osobaId());
        if (osoba.getStatus() == StatusCzlonkostwa.ZMARLY) {
            throw new RegulaParafiiException("Nie przenosimy osoby zmarłej.");
        }
        sprawdzNumer(formularz.numerKsiegi(), id, przeniesienia.existsByNumerKsiegi(formularz.numerKsiegi()),
                id != null && przeniesienia.existsByNumerKsiegiAndIdNot(formularz.numerKsiegi(), id));
        wpis.setOsoba(osoba);
        wpis.setDataCzynnosci(formularz.dataCzynnosci());
        wpis.setKierunek(formularz.kierunek());
        wpis.setParafia(formularz.parafia());
        wpis.setNumerKsiegi(formularz.numerKsiegi());
        wpis.setUwagi(formularz.uwagi());
        switch (formularz.kierunek()) {
            case PRZYJECIE -> {
                osoba.setStatus(StatusCzlonkostwa.CZLONEK);
                osoba.setDataPrzyjecia(formularz.dataCzynnosci());
                osoba.setDataWystapienia(null);
            }
            case PRZENIESIENIE -> {
                osoba.setStatus(StatusCzlonkostwa.PRZENIESIONY);
                osoba.setDataWystapienia(formularz.dataCzynnosci());
            }
            case WYSTAPIENIE -> {
                osoba.setStatus(StatusCzlonkostwa.WYSTAPIL);
                osoba.setDataWystapienia(formularz.dataCzynnosci());
            }
        }
        return przeniesienia.save(wpis);
    }

    @Transactional(readOnly = true)
    public Chrzest chrzest(Long id) {
        return chrzty.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego chrztu."));
    }

    @Transactional(readOnly = true)
    public Konfirmacja konfirmacja(Long id) {
        return konfirmacje.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej konfirmacji."));
    }

    @Transactional(readOnly = true)
    public Slub slub(Long id) {
        return sluby.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego ślubu."));
    }

    @Transactional(readOnly = true)
    public Pogrzeb pogrzeb(Long id) {
        return pogrzeby.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego pogrzebu."));
    }

    @Transactional(readOnly = true)
    public Przeniesienie przeniesienie(Long id) {
        return przeniesienia.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego przeniesienia."));
    }

    public long liczbaChrztow() {
        return chrzty.count();
    }

    public long liczbaKonfirmacji() {
        return konfirmacje.count();
    }

    public long liczbaSlubow() {
        return sluby.count();
    }

    public long liczbaPogrzebow() {
        return pogrzeby.count();
    }

    public long liczbaPrzeniesien() {
        return przeniesienia.count();
    }

    private void sprawdzWolnyStan(Osoba osoba, Long ignorowanySlub, LocalDate data) {
        if (osoba.getStatus() == StatusCzlonkostwa.ZMARLY) {
            throw new RegulaParafiiException(osoba.getImieINazwisko() + " jest osobą zmarłą.");
        }
        if (osoba.getDataUrodzenia() != null && Period.between(osoba.getDataUrodzenia(), data).getYears() < 18) {
            throw new RegulaParafiiException(osoba.getImieINazwisko() + " nie ma 18 lat w dniu ślubu.");
        }
        for (Slub slub : sluby.findByOsobaAIdOrOsobaBId(osoba.getId(), osoba.getId())) {
            if (slub.getId().equals(ignorowanySlub)) {
                continue;
            }
            Osoba druga = slub.getOsobaA().getId().equals(osoba.getId()) ? slub.getOsobaB() : slub.getOsobaA();
            if (druga.getStatus() != StatusCzlonkostwa.ZMARLY) {
                throw new RegulaParafiiException(osoba.getImieINazwisko() + " ma już wpis w księdze ślubów.");
            }
        }
    }

    private void sprawdzNumer(String numer, Long id, boolean istnieje, boolean istniejeInny) {
        boolean zajety = id == null ? istnieje : istniejeInny;
        if (zajety) {
            throw new RegulaParafiiException("Ten numer księgi jest już użyty.");
        }
    }

    private Osoba osoba(Long id) {
        return osoby.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej osoby w kartotece."));
    }
}
