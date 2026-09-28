package pl.kea.parafia.finanse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FunduszFormularz(
        @NotBlank @Size(max = 160) String nazwa,
        @Size(max = 500) String opis,
        Boolean aktywny
) {
}
