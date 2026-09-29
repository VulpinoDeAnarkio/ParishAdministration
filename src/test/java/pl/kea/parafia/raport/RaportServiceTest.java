package pl.kea.parafia.raport;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.osoba.OsobaFormularz;
import pl.kea.parafia.osoba.OsobaService;
import pl.kea.parafia.osoba.Plec;
import pl.kea.parafia.osoba.StatusCzlonkostwa;

@SpringBootTest
@Transactional
class RaportServiceTest {

    @Autowired
    private RaportService raporty;

    @Autowired
    private OsobaService osoby;

    @Test
    void kartotekaTrafiaDoCsvZeSrednikiem() {
        osoby.utworz(new OsobaFormularz(
                "Ewa", null, "Polna", null, Plec.KOBIETA, null, null, "ewa@poznan.test", null, null, null, null,
                StatusCzlonkostwa.CZLONEK, null, null, null, false, false, null, null
        ));
        String csv = raporty.osobyCsv();
        assertThat(csv).contains("imię;nazwisko;status");
        assertThat(csv).contains("Ewa;Polna;Członek parafii;ewa@poznan.test;");
    }
}
