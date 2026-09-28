package pl.kea.parafia.duszpasterstwo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record WizytaFormularz(
        Long sprawaId,
        @NotNull Long osobaId,
        @NotNull LocalDate dataWizyty,
        @Size(max = 160) String miejsce,
        @Size(max = 4000) String notatka,
        LocalDate nastepnyKontakt
) {
}
