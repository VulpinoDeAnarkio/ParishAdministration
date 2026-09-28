package pl.kea.parafia.nabozenstwo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.kalendarz.RodzajWydarzenia;

@SpringBootTest
@Transactional
class NabozenstwoServiceTest {

    @Autowired
    private NabozenstwoService nabozenstwa;

    @Test
    void zapisTworzyWydarzenieWKoscieleIZachowujePiesn() {
        Nabozenstwo zapisane = nabozenstwa.zapisz(null, new NabozenstwoFormularz(
                LocalDate.of(2026, 10, 18),
                LocalTime.of(10, 0),
                75,
                RodzajNabozenstwa.GLOWNE,
                "18. niedziela po Trójcy Świętej",
                "ks. Anna Lis",
                "Iz 55,6–11",
                "Ps 23",
                "Rz 8,14–17",
                "J 3,16–21",
                "Dar wiary",
                "J 3,16",
                true,
                null,
                List.of(new PiesnFormularz("365", "Warownym grodem", MomentPiesni.WEJSCIE))
        ));

        Nabozenstwo odczytane = nabozenstwa.znajdz(zapisane.getId());
        assertThat(odczytane.getWydarzenie().getRodzaj()).isEqualTo(RodzajWydarzenia.NABOZENSTWO);
        assertThat(odczytane.getWydarzenie().getSala().getNazwa()).isEqualTo("Kościół");
        assertThat(odczytane.getPiesni()).hasSize(1);
        assertThat(odczytane.getPiesni().getFirst().getTytul()).isEqualTo("Warownym grodem");
    }
}
