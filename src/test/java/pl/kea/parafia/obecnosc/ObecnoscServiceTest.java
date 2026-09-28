package pl.kea.parafia.obecnosc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.nabozenstwo.Nabozenstwo;
import pl.kea.parafia.nabozenstwo.NabozenstwoFormularz;
import pl.kea.parafia.nabozenstwo.NabozenstwoService;
import pl.kea.parafia.nabozenstwo.RodzajNabozenstwa;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaFormularz;
import pl.kea.parafia.osoba.OsobaService;
import pl.kea.parafia.osoba.Plec;
import pl.kea.parafia.osoba.StatusCzlonkostwa;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@SpringBootTest
@Transactional
class ObecnoscServiceTest {

    @Autowired
    private ObecnoscService obecnosci;

    @Autowired
    private OsobaService osoby;

    @Autowired
    private NabozenstwoService nabozenstwa;

    @Test
    void obecnoscAktualizujeWpisIOdrzucaZmarlego() {
        Osoba zywy = osoby.utworz(osoba("Jan", "Nowak", StatusCzlonkostwa.CZLONEK));
        Osoba zmarly = osoby.utworz(osoba("Ewa", "Lis", StatusCzlonkostwa.ZMARLY));
        Nabozenstwo nabozenstwo = nabozenstwa.zapisz(null, new NabozenstwoFormularz(
                LocalDate.of(2026, 11, 1), LocalTime.of(10, 0), 60, RodzajNabozenstwa.GLOWNE,
                null, null, null, null, null, null, null, null, true, null, List.of()
        ));

        obecnosci.zapisz(RodzajObecnosci.NABOZENSTWO, nabozenstwo.getId(), List.of(zywy.getId()), List.of(zywy.getId()));
        obecnosci.zapisz(RodzajObecnosci.NABOZENSTWO, nabozenstwo.getId(), List.of(zywy.getId()), List.of());

        boolean obecny = obecnosci.lista(RodzajObecnosci.NABOZENSTWO, nabozenstwo.getId()).stream()
                .filter(wiersz -> wiersz.osoba().getId().equals(zywy.getId()))
                .findFirst()
                .orElseThrow()
                .obecny();
        assertThat(obecny).isFalse();
        assertThat(obecnosci.sredniaObecnychNaNabozenstwie()).isZero();
        assertThatThrownBy(() -> obecnosci.zapisz(
                RodzajObecnosci.NABOZENSTWO, nabozenstwo.getId(), List.of(zmarly.getId()), List.of(zmarly.getId())
        )).isInstanceOf(RegulaParafiiException.class);
    }

    private OsobaFormularz osoba(String imie, String nazwisko, StatusCzlonkostwa status) {
        return new OsobaFormularz(
                imie, null, nazwisko, null, Plec.KOBIETA, null, null, null, null, null, null, null,
                status, null, null, null, false, false, null, null
        );
    }
}
