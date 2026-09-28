package pl.kea.parafia.ogloszenie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record OgloszenieFormularz(
        @NotBlank @Size(max = 200) String tytul,
        @NotBlank @Size(max = 4000) String tresc,
        @NotNull LocalDate obowiazujeOd,
        LocalDate obowiazujeDo,
        Boolean widocznePublicznie
) {
    public static OgloszenieFormularz puste() {
        return new OgloszenieFormularz("", "", LocalDate.now(), null, true);
    }
}
