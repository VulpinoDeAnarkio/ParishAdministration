package pl.kea.parafia.postep;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class KalkulatorPostepu {

    private KalkulatorPostepu() {
    }

    public static int procent(BigDecimal aktualna, BigDecimal docelowa) {
        if (aktualna == null || docelowa == null || docelowa.signum() <= 0) {
            return 0;
        }
        if (aktualna.signum() < 0) {
            return 0;
        }
        int wynik = aktualna.multiply(BigDecimal.valueOf(100))
                .divide(docelowa, 0, RoundingMode.HALF_UP)
                .intValue();
        return Math.min(100, wynik);
    }
}
