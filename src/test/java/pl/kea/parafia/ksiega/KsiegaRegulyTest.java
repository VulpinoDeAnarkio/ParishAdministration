package pl.kea.parafia.ksiega;

import static org.assertj.core.api.Assertions.assertThat;
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
class KsiegaRegulyTest {

    @Autowired
    private KsiegaService ksiegi;

    @Autowired
    private OsobaService osoby;

    @Test
    void konfirmacjaWymagaChrztuAPogrzebZamykaKartoteke() {
        Osoba anna = nowa("Anna", LocalDate.of(2008, 3, 2));

        assertThatThrownBy(() -> ksiegi.zapiszKonfirmacje(null, new KonfirmacjaFormularz(
                anna.getId(), LocalDate.of(2024, 5, 19), "Lipowa", "ks. Marek Lis", "2024", null, "K/2024/9", null
        ))).isInstanceOf(RegulaParafiiException.class);

        ksiegi.zapiszChrzest(null, new ChrzestFormularz(
                anna.getId(), LocalDate.of(2008, 4, 6), "Lipowa", "ks. Marek Lis", "Ewa", "Adam", null, null, "CH/2008/9", null
        ));
        ksiegi.zapiszKonfirmacje(null, new KonfirmacjaFormularz(
                anna.getId(), LocalDate.of(2024, 5, 19), "Lipowa", "ks. Marek Lis", "2024", null, "K/2024/9", null
        ));
        assertThat(osoby.znajdz(anna.getId()).getStatus()).isEqualTo(StatusCzlonkostwa.CZLONEK);

        ksiegi.zapiszPogrzeb(null, new PogrzebFormularz(
                anna.getId(), LocalDate.of(2026, 1, 2), LocalDate.of(2026, 1, 8), "Lipowa", "Cmentarz", "ks. Marek Lis", "P/2026/1", null
        ));
        Osoba poZgonie = osoby.znajdz(anna.getId());
        assertThat(poZgonie.getStatus()).isEqualTo(StatusCzlonkostwa.ZMARLY);
        assertThat(poZgonie.getDataWystapienia()).isEqualTo(LocalDate.of(2026, 1, 2));
    }

    @Test
    void slubOdrzucaTeSamaOsobeINiepelnoletnia() {
        Osoba dorosla = nowa("Ewa", LocalDate.of(1990, 1, 1));
        Osoba dziecko = nowa("Zosia", LocalDate.of(2015, 6, 1));

        assertThatThrownBy(() -> ksiegi.zapiszSlub(null, new SlubFormularz(
                dorosla.getId(), dorosla.getId(), LocalDate.of(2026, 6, 1), "Lipowa", "ks. Lis",
                RodzajSlubu.SLUB, null, null, "S/2026/1", null
        ))).isInstanceOf(RegulaParafiiException.class);

        assertThatThrownBy(() -> ksiegi.zapiszSlub(null, new SlubFormularz(
                dorosla.getId(), dziecko.getId(), LocalDate.of(2026, 6, 1), "Lipowa", "ks. Lis",
                RodzajSlubu.SLUB, null, null, "S/2026/2", null
        ))).isInstanceOf(RegulaParafiiException.class);
    }

    private Osoba nowa(String imie, LocalDate urodzenie) {
        return osoby.utworz(new OsobaFormularz(
                imie, null, "Testowa", null, Plec.KOBIETA, urodzenie, null, null, null,
                null, null, "Lipowa", StatusCzlonkostwa.SYMPATYK, null, null, null, false, false, null, null
        ));
    }
}
