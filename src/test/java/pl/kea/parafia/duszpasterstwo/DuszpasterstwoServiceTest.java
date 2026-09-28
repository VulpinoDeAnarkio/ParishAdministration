package pl.kea.parafia.duszpasterstwo;

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
class DuszpasterstwoServiceTest {

    @Autowired
    private DuszpasterstwoService duszpasterstwo;

    @Autowired
    private OsobaService osoby;

    @Test
    void zamknietaSprawaNiePrzyjmujeWizytyAZmarlyNieDostajeNowej() {
        Osoba osoba = osoby.utworz(new OsobaFormularz(
                "Marek", null, "Wolf", null, Plec.MEZCZYZNA, null, null, null, null, null, null, null,
                StatusCzlonkostwa.CZLONEK, null, null, null, false, false, null, null
        ));
        SprawaDuszpasterska sprawa = duszpasterstwo.zapiszSprawe(null, new SprawaFormularz(
                osoba.getId(), "Rozmowa przed ślubem", null, StatusSprawy.ZAMKNIETA
        ));
        assertThatThrownBy(() -> duszpasterstwo.zapiszWizyte(new WizytaFormularz(
                sprawa.getId(), osoba.getId(), LocalDate.of(2026, 9, 1), "plebania", "notatka", null
        ))).isInstanceOf(RegulaParafiiException.class);

        Osoba zmarly = osoby.utworz(new OsobaFormularz(
                "Zofia", null, "Kern", null, Plec.KOBIETA, null, null, null, null, null, null, null,
                StatusCzlonkostwa.ZMARLY, null, null, null, false, false, null, null
        ));
        assertThatThrownBy(() -> duszpasterstwo.zapiszSprawe(null, new SprawaFormularz(
                zmarly.getId(), "Nowa sprawa", null, StatusSprawy.OTWARTA
        ))).isInstanceOf(RegulaParafiiException.class);
    }
}
