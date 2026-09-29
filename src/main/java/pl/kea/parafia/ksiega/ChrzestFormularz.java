package pl.kea.parafia.ksiega;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ChrzestFormularz(
        @NotNull Long osobaId,
        @NotNull @PastOrPresent LocalDate dataChrztu,
        @NotBlank @Size(max = 200) String miejsce,
        @NotBlank @Size(max = 160) String duchowny,
        @Size(max = 160) String rodzicPierwszy,
        @Size(max = 160) String rodzicDrugi,
        @Size(max = 160) String chrzestnyPierwszy,
        @Size(max = 160) String chrzestnyDrugi,
        @NotBlank @Size(max = 40) String numerKsiegi,
        @Size(max = 1000) String uwagi
) {
    public static ChrzestFormularz pusty(Long osobaId, String numer) {
        return new ChrzestFormularz(osobaId, LocalDate.now(), "Kościół Zmiłowania Bożego w Poznaniu", "", "", "", "", "", numer, "");
    }

    public static ChrzestFormularz z(Chrzest chrzest) {
        return new ChrzestFormularz(
                chrzest.getOsoba().getId(),
                chrzest.getDataChrztu(),
                chrzest.getMiejsce(),
                chrzest.getDuchowny(),
                chrzest.getRodzicPierwszy(),
                chrzest.getRodzicDrugi(),
                chrzest.getChrzestnyPierwszy(),
                chrzest.getChrzestnyDrugi(),
                chrzest.getNumerKsiegi(),
                chrzest.getUwagi()
        );
    }
}
