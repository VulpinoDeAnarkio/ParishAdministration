package pl.kea.parafia.ksiega;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record SlubFormularz(
        @NotNull Long osobaAId,
        @NotNull Long osobaBId,
        @NotNull LocalDate dataSlubu,
        @NotBlank @Size(max = 200) String miejsce,
        @NotBlank @Size(max = 160) String duchowny,
        @NotNull RodzajSlubu rodzaj,
        @Size(max = 160) String swiadekPierwszy,
        @Size(max = 160) String swiadekDrugi,
        @NotBlank @Size(max = 40) String numerKsiegi,
        @Size(max = 1000) String uwagi
) {
    public static SlubFormularz pusty(String numer) {
        return new SlubFormularz(null, null, LocalDate.now(), "Kościół Zmiłowania Bożego w Poznaniu", "", RodzajSlubu.SLUB, "", "", numer, "");
    }

    public static SlubFormularz z(Slub slub) {
        return new SlubFormularz(
                slub.getOsobaA().getId(), slub.getOsobaB().getId(), slub.getDataSlubu(), slub.getMiejsce(),
                slub.getDuchowny(), slub.getRodzaj(), slub.getSwiadekPierwszy(), slub.getSwiadekDrugi(),
                slub.getNumerKsiegi(), slub.getUwagi()
        );
    }
}
