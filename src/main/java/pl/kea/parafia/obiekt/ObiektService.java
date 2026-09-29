package pl.kea.parafia.obiekt;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaRepository;
import pl.kea.parafia.wspolne.NieZnalezionoException;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class ObiektService {

    private final ObiektRepository obiekty;
    private final RezerwacjaObiektuRepository rezerwacje;
    private final OsobaRepository osoby;

    public ObiektService(ObiektRepository obiekty, RezerwacjaObiektuRepository rezerwacje, OsobaRepository osoby) {
        this.obiekty = obiekty;
        this.rezerwacje = rezerwacje;
        this.osoby = osoby;
    }

    @Transactional(readOnly = true)
    public List<Obiekt> lista(RodzajObiektu rodzaj) {
        return obiekty.wszystkieZRodzicem().stream()
                .filter(obiekt -> rodzaj == null || obiekt.getRodzaj() == rodzaj)
                .toList();
    }

    @Transactional(readOnly = true)
    public Obiekt znajdz(Long id) {
        return obiekty.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego obiektu parafii."));
    }

    @Transactional(readOnly = true)
    public Obiekt znajdzPoNazwie(String nazwa) {
        return obiekty.findFirstByNazwa(nazwa).orElseThrow(() -> new NieZnalezionoException("Nie ma obiektu " + nazwa + "."));
    }

    @Transactional(readOnly = true)
    public List<Obiekt> dzieci(Long id) {
        return obiekty.findByRodzicIdOrderByNazwaAsc(id);
    }

    @Transactional(readOnly = true)
    public List<RezerwacjaObiektu> rezerwacjeObiektu(Long id) {
        return rezerwacje.obiektu(id);
    }

    @Transactional(readOnly = true)
    public boolean saRezerwacje() {
        return rezerwacje.count() > 0;
    }

    @Transactional(readOnly = true)
    public List<RezerwacjaObiektu> nadchodzace() {
        return rezerwacje.nadchodzace(LocalDateTime.now()).stream().limit(8).toList();
    }

    @Transactional
    public Obiekt zapisz(Long id, ObiektFormularz formularz) {
        Obiekt obiekt = id == null ? new Obiekt() : znajdz(id);
        obiekt.setNazwa(formularz.nazwa());
        obiekt.setRodzaj(formularz.rodzaj());
        obiekt.setRodzic(rodzic(id, formularz.rodzicId()));
        obiekt.setPojemnosc(formularz.pojemnosc());
        obiekt.setLokalizacja(formularz.lokalizacja());
        obiekt.setOpis(formularz.opis());
        obiekt.setAktywny(!Boolean.FALSE.equals(formularz.aktywny()));
        return obiekty.save(obiekt);
    }

    @Transactional(readOnly = true)
    public RezerwacjaObiektu znajdzRezerwacje(Long id) {
        return rezerwacje.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej rezerwacji."));
    }

    @Transactional
    public RezerwacjaObiektu zapiszRezerwacje(Long id, RezerwacjaFormularz formularz) {
        if (!formularz.koniec().isAfter(formularz.poczatek())) {
            throw new RegulaParafiiException("Koniec rezerwacji musi być późniejszy niż początek.");
        }
        Obiekt obiekt = znajdz(formularz.obiektId());
        if (!obiekt.isAktywny()) {
            throw new RegulaParafiiException("Obiekt jest wyłączony z rezerwacji.");
        }
        if (formularz.liczbaOsob() != null && obiekt.getPojemnosc() != null && formularz.liczbaOsob() > obiekt.getPojemnosc()) {
            throw new RegulaParafiiException("Liczba osób przekracza pojemność obiektu.");
        }
        if (!rezerwacje.kolizje(zajeteRazem(obiekt), id, formularz.poczatek(), formularz.koniec()).isEmpty()) {
            throw new RegulaParafiiException("Ten obiekt albo część tego samego budynku jest już zarezerwowana w tym terminie.");
        }
        RezerwacjaObiektu rezerwacja = id == null ? new RezerwacjaObiektu() : znajdzRezerwacje(id);
        rezerwacja.setObiekt(obiekt);
        rezerwacja.setPoczatek(formularz.poczatek());
        rezerwacja.setKoniec(formularz.koniec());
        rezerwacja.setRezerwujacy(formularz.rezerwujacy());
        rezerwacja.setOsoba(osoba(formularz.osobaId()));
        rezerwacja.setCel(formularz.cel());
        rezerwacja.setLiczbaOsob(formularz.liczbaOsob());
        rezerwacja.setUwagi(formularz.uwagi());
        rezerwacja.setStatus(StatusRezerwacji.POTWIERDZONA);
        return rezerwacje.save(rezerwacja);
    }

    @Transactional
    public void anuluj(Long id) {
        RezerwacjaObiektu rezerwacja = znajdzRezerwacje(id);
        rezerwacja.setStatus(StatusRezerwacji.ANULOWANA);
        rezerwacje.save(rezerwacja);
    }

    private Obiekt rodzic(Long id, Long rodzicId) {
        if (rodzicId == null) {
            return null;
        }
        if (id != null && id.equals(rodzicId)) {
            throw new RegulaParafiiException("Obiekt nie może należeć do samego siebie.");
        }
        Obiekt kursor = znajdz(rodzicId);
        while (kursor != null) {
            if (id != null && id.equals(kursor.getId())) {
                throw new RegulaParafiiException("Obiekt nie może należeć do samego siebie.");
            }
            kursor = kursor.getRodzic();
        }
        return znajdz(rodzicId);
    }

    private Osoba osoba(Long osobaId) {
        if (osobaId == null) {
            return null;
        }
        return osoby.findById(osobaId).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej osoby w kartotece."));
    }

    private Set<Long> zajeteRazem(Obiekt obiekt) {
        Set<Long> identyfikatory = new LinkedHashSet<>();
        Obiekt kursor = obiekt;
        while (kursor != null) {
            identyfikatory.add(kursor.getId());
            kursor = kursor.getRodzic();
        }
        dolaczDzieci(identyfikatory, obiekt.getId());
        return identyfikatory;
    }

    private void dolaczDzieci(Set<Long> identyfikatory, Long rodzicId) {
        for (Obiekt dziecko : obiekty.findByRodzicIdOrderByNazwaAsc(rodzicId)) {
            identyfikatory.add(dziecko.getId());
            dolaczDzieci(identyfikatory, dziecko.getId());
        }
    }
}
