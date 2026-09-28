package pl.kea.parafia.sluzba;

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
class SluzbaServiceTest {

    @Autowired
    private SluzbaService sluzba;

    @Autowired
    private OsobaService osoby;

    @Autowired
    private NabozenstwoService nabozenstwa;

    @Test
    void tenSamDyzurNieMozeSiePowtorzyc() {
        Osoba osoba = osoby.utworz(new OsobaFormularz(
                "Piotr", null, "Organ", null, Plec.MEZCZYZNA, null, null, null, null, null, null, null,
                StatusCzlonkostwa.CZLONEK, null, null, null, false, false, null, null
        ));
        Nabozenstwo nabozenstwo = nabozenstwa.zapisz(null, new NabozenstwoFormularz(
                LocalDate.of(2026, 12, 6), LocalTime.of(10, 0), 70, RodzajNabozenstwa.GLOWNE,
                null, null, null, null, null, null, null, null, false, null, List.of()
        ));
        DyzurFormularz formularz = new DyzurFormularz(nabozenstwo.getId(), osoba.getId(), RolaSluzby.ORGANISTA);
        sluzba.dodajDyzur(formularz);
        assertThatThrownBy(() -> sluzba.dodajDyzur(formularz)).isInstanceOf(RegulaParafiiException.class);
    }
}
