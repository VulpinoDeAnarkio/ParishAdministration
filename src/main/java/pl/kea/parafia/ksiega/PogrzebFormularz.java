package pl.kea.parafia.ksiega;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record PogrzebFormularz(
        @NotNull Long osobaId,
        @NotNull @PastOrPresent LocalDate dataZgonu,
        LocalDate dataPogrzebu,
        @NotBlank @Size(max = 200) String miejsce,
        @Size(max = 200) String cmentarz,
        @NotBlank @Size(max = 160) String duchowny,
        @NotBlank @Size(max = 40) String numerKsiegi,
        @Size(max = 1000) String uwagi
) {
    public static PogrzebFormularz pusty(Long osobaId, String numer) {
        return new PogrzebFormularz(osobaId, LocalDate.now(), LocalDate.now(), "Cmentarz w Poznaniu", "Cmentarz parafialny", "", numer, "");
    }

    public static PogrzebFormularz z(Pogrzeb pogrzeb) {
        return new PogrzebFormularz(
                pogrzeb.getOsoba().getId(), pogrzeb.getDataZgonu(), pogrzeb.getDataPogrzebu(), pogrzeb.getMiejsce(),
                pogrzeb.getCmentarz(), pogrzeb.getDuchowny(), pogrzeb.getNumerKsiegi(), pogrzeb.getUwagi()
        );
    }
}
