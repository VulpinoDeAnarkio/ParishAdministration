package pl.kea.parafia.obecnosc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.grupa.CzlonekGrupy;
import pl.kea.parafia.grupa.GrupaService;
import pl.kea.parafia.kalendarz.KalendarzService;
import pl.kea.parafia.nabozenstwo.NabozenstwoService;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaRepository;
import pl.kea.parafia.osoba.StatusCzlonkostwa;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class ObecnoscService {

    private static final List<StatusCzlonkostwa> NA_NABOZENSTWIE = List.of(
            StatusCzlonkostwa.CZLONEK,
            StatusCzlonkostwa.DZIECKO_OCHRZCZONE,
            StatusCzlonkostwa.KATECHUMEN
    );

    private final ObecnoscRepository obecnosci;
    private final OsobaRepository osoby;
    private final NabozenstwoService nabozenstwa;
    private final GrupaService grupy;
    private final KalendarzService kalendarz;

    public ObecnoscService(
            ObecnoscRepository obecnosci,
            OsobaRepository osoby,
            NabozenstwoService nabozenstwa,
            GrupaService grupy,
            KalendarzService kalendarz
    ) {
        this.obecnosci = obecnosci;
        this.osoby = osoby;
        this.nabozenstwa = nabozenstwa;
        this.grupy = grupy;
        this.kalendarz = kalendarz;
    }

    @Transactional(readOnly = true)
    public List<WierszObecnosci> lista(RodzajObecnosci rodzaj, Long celId) {
        sprawdzCel(rodzaj, celId);
        Map<Long, Boolean> zapisane = obecnosci.findByRodzajAndCelId(rodzaj, celId).stream()
                .collect(Collectors.toMap(o -> o.getOsoba().getId(), Obecnosc::isObecny, (a, b) -> a, LinkedHashMap::new));
        List<Osoba> kandydaci = kandydaci(rodzaj, celId);
        Set<Long> juzNaLiscie = kandydaci.stream().map(Osoba::getId).collect(Collectors.toSet());
        List<WierszObecnosci> wiersze = new ArrayList<>();
        for (Osoba osoba : kandydaci) {
            wiersze.add(new WierszObecnosci(osoba, Boolean.TRUE.equals(zapisane.get(osoba.getId()))));
        }
        for (Obecnosc wpis : obecnosci.findByRodzajAndCelId(rodzaj, celId)) {
            if (!juzNaLiscie.contains(wpis.getOsoba().getId())) {
                wiersze.add(new WierszObecnosci(wpis.getOsoba(), wpis.isObecny()));
            }
        }
        return wiersze;
    }

    @Transactional
    public void zapisz(RodzajObecnosci rodzaj, Long celId, List<Long> osobyNaLiscie, List<Long> obecni) {
        sprawdzCel(rodzaj, celId);
        Set<Long> zaznaczeni = obecni == null ? Set.of() : Set.copyOf(obecni);
        if (osobyNaLiscie == null) {
            return;
        }
        for (Long osobaId : osobyNaLiscie) {
            Osoba osoba = osoby.findById(osobaId).orElse(null);
            if (osoba == null) {
                continue;
            }
            boolean obecny = zaznaczeni.contains(osobaId);
            if (obecny && osoba.getStatus() == StatusCzlonkostwa.ZMARLY) {
                throw new RegulaParafiiException("Nie zapisujemy obecności osoby zmarłej.");
            }
            Obecnosc wpis = obecnosci.findByRodzajAndCelIdAndOsobaId(rodzaj, celId, osobaId).orElseGet(Obecnosc::new);
            wpis.setRodzaj(rodzaj);
            wpis.setCelId(celId);
            wpis.setOsoba(osoba);
            wpis.setObecny(obecny);
            obecnosci.save(wpis);
        }
    }

    @Transactional(readOnly = true)
    public int sredniaObecnychNaNabozenstwie() {
        List<Object[]> wyniki = obecnosci.obecniNaNabozenstwach();
        if (wyniki.isEmpty()) {
            return 0;
        }
        long suma = wyniki.stream().mapToLong(wiersz -> (Long) wiersz[1]).sum();
        return (int) Math.round(suma / (double) wyniki.size());
    }

    public String tytul(RodzajObecnosci rodzaj, Long celId) {
        return switch (rodzaj) {
            case NABOZENSTWO -> nabozenstwa.znajdz(celId).getRodzaj().getEtykieta();
            case GRUPA -> grupy.znajdz(celId).getNazwa();
            case WYDARZENIE -> kalendarz.znajdz(celId).getTytul();
        };
    }

    private void sprawdzCel(RodzajObecnosci rodzaj, Long celId) {
        switch (rodzaj) {
            case NABOZENSTWO -> nabozenstwa.znajdz(celId);
            case GRUPA -> grupy.znajdz(celId);
            case WYDARZENIE -> kalendarz.znajdz(celId);
        }
    }

    private List<Osoba> kandydaci(RodzajObecnosci rodzaj, Long celId) {
        return switch (rodzaj) {
            case NABOZENSTWO -> osoby.findByStatusInOrderByNazwiskoAscImieAsc(NA_NABOZENSTWIE);
            case GRUPA -> grupy.czlonkowie(celId).stream().filter(CzlonekGrupy::jestAktywny).map(CzlonekGrupy::getOsoba).toList();
            case WYDARZENIE -> List.of();
        };
    }
}
