package pl.kea.parafia.kalendarz;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@SpringBootTest
@Transactional
class KalendarzServiceTest {

    @Autowired
    private KalendarzService kalendarz;

    @Test
    void salaNieMozeBycZajetaDwaRazy() {
        Long sala = kalendarz.sale().getFirst().getId();
        LocalDateTime start = LocalDateTime.of(2026, 10, 4, 10, 0);
        kalendarz.zapisz(null, new WydarzenieFormularz(
                "Nabożeństwo", null, start, start.plusHours(1), null, sala, RodzajWydarzenia.NABOZENSTWO, true, null
        ));
        assertThatThrownBy(() -> kalendarz.zapisz(null, new WydarzenieFormularz(
                "Próba chóru", null, start.plusMinutes(30), start.plusHours(2), null, sala, RodzajWydarzenia.SPOTKANIE, false, null
        ))).isInstanceOf(RegulaParafiiException.class);
    }
}
