package pl.kea.parafia.sluzba;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ZadanieFormularz(
        @NotBlank @Size(max = 200) String tytul,
        @Size(max = 2000) String opis,
        LocalDate termin,
        @NotNull StatusZadania status,
        Long osobaId,
        Long wydarzenieId
) {
}
