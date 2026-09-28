package pl.kea.parafia.postep;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.ksiega.ChrzestFormularz;
import pl.kea.parafia.ksiega.KsiegaService;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaFormularz;
import pl.kea.parafia.osoba.OsobaService;
import pl.kea.parafia.osoba.Plec;
import pl.kea.parafia.osoba.StatusCzlonkostwa;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@SpringBootTest
@Transactional
class PostepServiceTest {

    @Autowired
    private PostepService postep;

    @Autowired
    private OsobaService osoby;

    @Autowired
    private KsiegaService ksiegi;

    @Test
    void celLiczyCzlonkowANaukaWymagaChrztu() {
        osoby.utworz(new OsobaFormularz(
                "Iga", null, "Horn", null, Plec.KOBIETA, null, null, null, null, null, null, null,
                StatusCzlonkostwa.CZLONEK, null, null, null, false, true, null, null
        ));
        postep.zapiszCel(new CelFormularz("Członkowie", null, ZrodloCelu.LICZBA_CZLONKOW, new BigDecimal("2"), null, 2026, true));
        assertThat(postep.wyniki().getFirst().procent()).isEqualTo(50);

        Osoba dziecko = osoby.utworz(new OsobaFormularz(
                "Ola", null, "Mint", null, Plec.KOBIETA, LocalDate.of(2014, 3, 1), null, null, null, null, null, null,
                StatusCzlonkostwa.GOSC, null, null, null, false, false, null, null
        ));
        Rocznik rocznik = postep.zapiszRocznik("Rocznik próbny", 2026);
        assertThatThrownBy(() -> postep.dodajUczestnika(rocznik.getId(), dziecko.getId()))
                .isInstanceOf(RegulaParafiiException.class);

        ksiegi.zapiszChrzest(null, new ChrzestFormularz(
                dziecko.getId(), LocalDate.of(2014, 6, 1), "Lipowa", "ks. Jan", null, null, null, null, "CH-POSTEP-1", null
        ));
        postep.dodajUczestnika(rocznik.getId(), dziecko.getId());
        assertThat(postep.droga(dziecko.getId())).contains(EtapDrogi.CHRZEST, EtapDrogi.NAUKA);
    }
}
