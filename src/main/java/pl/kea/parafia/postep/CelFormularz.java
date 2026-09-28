package pl.kea.parafia.postep;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CelFormularz(
        @NotBlank @Size(max = 200) String nazwa,
        @Size(max = 1000) String opis,
        @NotNull ZrodloCelu zrodlo,
        @NotNull @DecimalMin("0.01") @Digits(integer = 10, fraction = 2) BigDecimal wartoscDocelowa,
        @Digits(integer = 10, fraction = 2) BigDecimal wartoscReczna,
        @NotNull Integer rok,
        Boolean aktywny
) {
}
