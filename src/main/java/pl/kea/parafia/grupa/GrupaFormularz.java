package pl.kea.parafia.grupa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GrupaFormularz(
        @NotBlank @Size(max = 160) String nazwa,
        @NotNull RodzajGrupy rodzaj,
        @Size(max = 2000) String opis,
        @Size(max = 160) String termin,
        @Size(max = 160) String miejsce,
        Long liderId,
        Boolean aktywna
) {
    public static GrupaFormularz pusty() {
        return new GrupaFormularz("", RodzajGrupy.INNA, "", "", "", null, true);
    }

    public static GrupaFormularz z(Grupa grupa) {
        return new GrupaFormularz(
                grupa.getNazwa(), grupa.getRodzaj(), grupa.getOpis(), grupa.getTermin(), grupa.getMiejsce(),
                grupa.getLider() == null ? null : grupa.getLider().getId(), grupa.isAktywna()
        );
    }
}
