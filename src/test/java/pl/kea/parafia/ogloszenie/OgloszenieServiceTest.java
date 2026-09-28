package pl.kea.parafia.ogloszenie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@SpringBootTest
@Transactional
class OgloszenieServiceTest {

    @Autowired
    private OgloszenieService ogloszenia;

    @Test
    void wygasleOgłoszenieZnikaZTablicy() {
        ogloszenia.zapisz(null, new OgloszenieFormularz(
                "Próba chóru", "Czwartek, salka.", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31), true
        ));
        ogloszenia.zapisz(null, new OgloszenieFormularz(
                "Nabożeństwo", "Niedziela o 10:00.", LocalDate.of(2026, 9, 1), null, true
        ));
        assertThat(ogloszenia.publiczne(LocalDate.of(2026, 9, 28))).extracting(Ogloszenie::getTytul).containsExactly("Nabożeństwo");
        assertThatThrownBy(() -> ogloszenia.zapisz(null, new OgloszenieFormularz(
                "Błąd", "Zły zakres.", LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 1), false
        ))).isInstanceOf(RegulaParafiiException.class);
    }
}
