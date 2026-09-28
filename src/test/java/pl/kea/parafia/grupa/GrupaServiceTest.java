package pl.kea.parafia.grupa;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaFormularz;
import pl.kea.parafia.osoba.OsobaService;
import pl.kea.parafia.osoba.Plec;
import pl.kea.parafia.osoba.StatusCzlonkostwa;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@SpringBootTest
@Transactional
class GrupaServiceTest {

    @Autowired
    private GrupaService grupy;

    @Autowired
    private OsobaService osoby;

    @Test
    void nieDodajeDwaRazyTejSamejOsoby() {
        Grupa chor = grupy.zapisz(null, new GrupaFormularz("Chór", RodzajGrupy.CHOR, null, "niedziela 9:00", "empora", null, true));
        Osoba anna = osoby.utworz(new OsobaFormularz(
                "Anna", null, "Maj", null, Plec.KOBIETA, LocalDate.of(1985, 2, 2), null, null, null,
                null, null, null, StatusCzlonkostwa.CZLONEK, null, null, null, false, true, null, null
        ));
        grupy.dodajCzlonka(chor.getId(), new CzlonekFormularz(anna.getId(), "Alt", LocalDate.now()));
        assertThatThrownBy(() -> grupy.dodajCzlonka(chor.getId(), new CzlonekFormularz(anna.getId(), "Alt", null)))
                .isInstanceOf(RegulaParafiiException.class);
    }
}
