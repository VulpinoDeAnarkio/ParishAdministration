package pl.kea.parafia.finanse;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record OperacjaFormularz(
        @NotNull Long funduszId,
        @NotNull RodzajOperacji rodzaj,
        @NotNull @DecimalMin(value = "0.01") @Digits(integer = 10, fraction = 2) BigDecimal kwota,
        @NotNull LocalDate dataOperacji,
        @Size(max = 500) String opis,
        @Size(max = 160) String kontrahent,
        Long osobaId
) {
}
