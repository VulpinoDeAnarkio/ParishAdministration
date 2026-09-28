package pl.kea.parafia.postep;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class DrogaWiaryTest {

    @Test
    void konfirmacjaDomylaNaukeACelNiePrzekraczaStuProcent() {
        List<EtapDrogi> etapy = DrogaWiary.etapy(new DaneDrogi(true, false, true, true, true, false));
        assertThat(etapy).containsExactly(
                EtapDrogi.CHRZEST, EtapDrogi.NAUKA, EtapDrogi.KONFIRMACJA, EtapDrogi.CZLONKOSTWO, EtapDrogi.WIECZERZA
        );
        assertThat(KalkulatorPostepu.procent(new BigDecimal("80"), new BigDecimal("40"))).isEqualTo(100);
        assertThat(KalkulatorPostepu.procent(new BigDecimal("1"), new BigDecimal("2"))).isEqualTo(50);
    }
}
