package pl.kea.parafia.grupa;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CzlonekFormularz(
        @NotNull Long osobaId,
        @Size(max = 80) String funkcja,
        LocalDate odDnia
) {
}
