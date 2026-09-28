package pl.kea.parafia.duszpasterstwo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SprawaFormularz(
        @NotNull Long osobaId,
        @NotBlank @Size(max = 200) String temat,
        @Size(max = 4000) String opis,
        @NotNull StatusSprawy status
) {
}
