package pl.kea.parafia.postep;

import java.math.BigDecimal;

public record WynikCelu(CelParafii cel, BigDecimal aktualna, int procent) {
}
