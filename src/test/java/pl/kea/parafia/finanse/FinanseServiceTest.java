package pl.kea.parafia.finanse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
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
class FinanseServiceTest {

    @Autowired
    private FinanseService finanse;

    @Autowired
    private OsobaService osoby;

    @Test
    void skladkaWymagaOsobyASaldoUwzgledniaWydatek() {
        Fundusz kasa = finanse.salda().getFirst().fundusz();
        assertThatThrownBy(() -> finanse.zapiszOperacje(new OperacjaFormularz(
                kasa.getId(), RodzajOperacji.SKLADKA, new BigDecimal("50.00"), LocalDate.of(2026, 9, 1), null, null, null
        ))).isInstanceOf(RegulaParafiiException.class);

        Osoba osoba = osoby.utworz(new OsobaFormularz(
                "Anna", null, "Berg", null, Plec.KOBIETA, null, null, null, null, null, null, null,
                StatusCzlonkostwa.CZLONEK, null, null, null, false, false, null, null
        ));
        finanse.zapiszOperacje(new OperacjaFormularz(
                kasa.getId(), RodzajOperacji.SKLADKA, new BigDecimal("120.00"), LocalDate.of(2026, 9, 1), null, null, osoba.getId()
        ));
        finanse.zapiszOperacje(new OperacjaFormularz(
                kasa.getId(), RodzajOperacji.MEDIA, new BigDecimal("45.50"), LocalDate.of(2026, 9, 2), "prąd", "Zakład energetyczny", null
        ));

        assertThat(finanse.saldo(kasa.getId())).isEqualByComparingTo("74.50");
    }
}
