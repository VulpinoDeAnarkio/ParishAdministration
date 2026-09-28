package pl.kea.parafia.kalendarz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SalaFormularz(
        @NotBlank @Size(max = 160) String nazwa,
        Integer pojemnosc,
        @Size(max = 500) String opis,
        Boolean aktywna
) {
}
