package pl.kea.parafia.kalendarz;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.wspolne.NieZnalezionoException;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class KalendarzService {

    private static final Locale POLSKI = Locale.forLanguageTag("pl");

    private final WydarzenieRepository wydarzenia;
    private final SalaRepository sale;

    public KalendarzService(WydarzenieRepository wydarzenia, SalaRepository sale) {
        this.wydarzenia = wydarzenia;
        this.sale = sale;
    }

    @Transactional(readOnly = true)
    public List<List<DzienKalendarza>> miesiac(YearMonth miesiac) {
        LocalDate pierwszy = miesiac.atDay(1);
        LocalDate kursor = pierwszy.minusDays(pierwszy.getDayOfWeek().getValue() - 1L);
        LocalDateTime od = kursor.atStartOfDay();
        LocalDateTime doDnia = kursor.plusDays(42).atStartOfDay();
        List<Wydarzenie> wMiesiacu = wydarzenia.wOkresie(od, doDnia);
        List<List<DzienKalendarza>> tygodnie = new ArrayList<>();
        LocalDate dzisiaj = LocalDate.now();
        for (int tydzien = 0; tydzien < 6; tydzien++) {
            List<DzienKalendarza> dni = new ArrayList<>();
            for (int dzien = 0; dzien < 7; dzien++) {
                LocalDate data = kursor;
                List<Wydarzenie> tegoDnia = wMiesiacu.stream()
                        .filter(w -> !w.getPoczatek().toLocalDate().isAfter(data) && !w.getKoniec().toLocalDate().isBefore(data))
                        .toList();
                dni.add(new DzienKalendarza(data, miesiac.equals(YearMonth.from(data)), data.equals(dzisiaj), tegoDnia));
                kursor = kursor.plusDays(1);
            }
            tygodnie.add(dni);
        }
        return tygodnie;
    }

    public String nazwaMiesiaca(YearMonth miesiac) {
        String nazwa = miesiac.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, POLSKI);
        return nazwa.substring(0, 1).toUpperCase(POLSKI) + nazwa.substring(1) + " " + miesiac.getYear();
    }

    @Transactional(readOnly = true)
    public List<Wydarzenie> nadchodzace() {
        return wydarzenia.findTop8ByPoczatekGreaterThanEqualOrderByPoczatekAsc(LocalDateTime.now().minusHours(3));
    }

    @Transactional(readOnly = true)
    public Wydarzenie znajdz(Long id) {
        return wydarzenia.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego wydarzenia."));
    }

    @Transactional(readOnly = true)
    public List<Sala> sale() {
        return sale.findByAktywnaIsTrueOrderByNazwaAsc();
    }

    @Transactional
    public Wydarzenie zapisz(Long id, WydarzenieFormularz formularz) {
        if (!formularz.koniec().isAfter(formularz.poczatek())) {
            throw new RegulaParafiiException("Koniec wydarzenia musi być późniejszy niż początek.");
        }
        if (formularz.salaId() != null && !wydarzenia.kolizjeSali(
                formularz.salaId(), id, formularz.poczatek(), formularz.koniec()).isEmpty()) {
            throw new RegulaParafiiException("Sala jest już zajęta w tym terminie.");
        }
        Wydarzenie wydarzenie = id == null ? new Wydarzenie() : znajdz(id);
        wydarzenie.setTytul(formularz.tytul());
        wydarzenie.setOpis(formularz.opis());
        wydarzenie.setPoczatek(formularz.poczatek());
        wydarzenie.setKoniec(formularz.koniec());
        wydarzenie.setMiejsce(formularz.miejsce());
        wydarzenie.setSala(formularz.salaId() == null ? null : sale.findById(formularz.salaId())
                .orElseThrow(() -> new NieZnalezionoException("Nie ma takiej sali.")));
        wydarzenie.setRodzaj(formularz.rodzaj());
        wydarzenie.setWidocznePublicznie(Boolean.TRUE.equals(formularz.widocznePublicznie()));
        wydarzenie.setOrganizator(formularz.organizator());
        return wydarzenia.save(wydarzenie);
    }

    @Transactional
    public Sala dodajSale(SalaFormularz formularz) {
        Sala sala = new Sala();
        sala.setNazwa(formularz.nazwa());
        sala.setPojemnosc(formularz.pojemnosc());
        sala.setOpis(formularz.opis());
        sala.setAktywna(!Boolean.FALSE.equals(formularz.aktywna()));
        return sale.save(sala);
    }

    @Transactional
    public void usun(Long id) {
        wydarzenia.delete(znajdz(id));
    }
}
