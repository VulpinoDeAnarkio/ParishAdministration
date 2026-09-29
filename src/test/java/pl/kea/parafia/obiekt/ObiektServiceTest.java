package pl.kea.parafia.obiekt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@SpringBootTest
@Transactional
class ObiektServiceTest {

    @Autowired
    private ObiektService obiekty;

    @Test
    void pokojNieMozeBycZajetyDwaRazy() {
        Obiekt pokoj = obiekty.znajdzPoNazwie("Pokój 1");
        LocalDateTime od = LocalDateTime.of(2026, 11, 2, 15, 0);
        obiekty.zapiszRezerwacje(null, rezerwacja(pokoj.getId(), od, od.plusDays(1), "Anna", 1));
        assertThatThrownBy(() -> obiekty.zapiszRezerwacje(null, rezerwacja(pokoj.getId(), od.plusHours(2), od.plusDays(2), "Ewa", 1)))
                .isInstanceOf(RegulaParafiiException.class)
                .hasMessageContaining("zarezerwowana");
    }

    @Test
    void rezerwacjaHoteluBlokujePokojeAPokojNieBlokujeSasiada() {
        Obiekt hotel = obiekty.znajdzPoNazwie("Hotel parafialny");
        Obiekt pierwszy = obiekty.znajdzPoNazwie("Pokój 1");
        Obiekt drugi = obiekty.znajdzPoNazwie("Pokój 2");
        LocalDateTime od = LocalDateTime.of(2026, 12, 1, 15, 0);
        LocalDateTime koniec = od.plusDays(2);

        obiekty.zapiszRezerwacje(null, rezerwacja(pierwszy.getId(), od, koniec, "Jan", 2));
        obiekty.zapiszRezerwacje(null, rezerwacja(drugi.getId(), od, koniec, "Maria", 2));
        assertThatThrownBy(() -> obiekty.zapiszRezerwacje(null, rezerwacja(hotel.getId(), od.plusHours(1), koniec.minusHours(1), "Grupa", 6)))
                .isInstanceOf(RegulaParafiiException.class);

        obiekty.anuluj(obiekty.rezerwacjeObiektu(pierwszy.getId()).getFirst().getId());
        obiekty.anuluj(obiekty.rezerwacjeObiektu(drugi.getId()).getFirst().getId());
        RezerwacjaObiektu calyHotel = obiekty.zapiszRezerwacje(null, rezerwacja(hotel.getId(), od, koniec, "Grupa", 6));
        assertThatThrownBy(() -> obiekty.zapiszRezerwacje(null, rezerwacja(drugi.getId(), od.plusHours(3), od.plusDays(1), "Gość", 1)))
                .isInstanceOf(RegulaParafiiException.class);
        assertThat(calyHotel.getStatus()).isEqualTo(StatusRezerwacji.POTWIERDZONA);
    }

    @Test
    void anulowanaRezerwacjaZwalniaTermin() {
        Obiekt salka = obiekty.znajdzPoNazwie("Salka parafialna");
        LocalDateTime od = LocalDateTime.of(2027, 1, 7, 18, 0);
        RezerwacjaObiektu pierwsza = obiekty.zapiszRezerwacje(null, rezerwacja(salka.getId(), od, od.plusHours(2), "Chór", 10));
        obiekty.anuluj(pierwsza.getId());
        RezerwacjaObiektu druga = obiekty.zapiszRezerwacje(null, rezerwacja(salka.getId(), od, od.plusHours(2), "Rada", 8));
        assertThat(druga.getId()).isNotEqualTo(pierwsza.getId());
    }

    @Test
    void poprawkaWlasnejRezerwacjiNieKolidujeSamaZeSoba() {
        Obiekt pokoj = obiekty.znajdzPoNazwie("Pokój rodzinny");
        LocalDateTime od = LocalDateTime.of(2027, 2, 1, 15, 0);
        RezerwacjaObiektu zapisana = obiekty.zapiszRezerwacje(null, rezerwacja(pokoj.getId(), od, od.plusDays(1), "Rodzina", 3));
        RezerwacjaObiektu poprawiona = obiekty.zapiszRezerwacje(zapisana.getId(), rezerwacja(pokoj.getId(), od, od.plusDays(2), "Rodzina", 4));
        assertThat(poprawiona.getKoniec()).isEqualTo(od.plusDays(2));
    }

    @Test
    void zaDuzoOsobIOdwroconyTerminSaOdrzucane() {
        Obiekt pokoj = obiekty.znajdzPoNazwie("Pokój 1");
        LocalDateTime od = LocalDateTime.of(2027, 3, 1, 15, 0);
        assertThatThrownBy(() -> obiekty.zapiszRezerwacje(null, rezerwacja(pokoj.getId(), od, od.plusDays(1), "Tłum", 3)))
                .isInstanceOf(RegulaParafiiException.class)
                .hasMessageContaining("pojemność");
        assertThatThrownBy(() -> obiekty.zapiszRezerwacje(null, rezerwacja(pokoj.getId(), od, od.minusHours(1), "Gość", 1)))
                .isInstanceOf(RegulaParafiiException.class)
                .hasMessageContaining("późniejszy");
    }

    private RezerwacjaFormularz rezerwacja(Long obiektId, LocalDateTime od, LocalDateTime koniec, String kto, int osob) {
        return new RezerwacjaFormularz(obiektId, od, koniec, kto, null, "pobyt", osob, null);
    }
}
