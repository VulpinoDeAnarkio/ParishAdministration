package pl.kea.parafia.osoba;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GospodarstwoFormularz(
        @NotBlank @Size(max = 160) String nazwa,
        @Size(max = 160) String ulica,
        @Size(max = 12) String kodPocztowy,
        @Size(max = 120) String miejscowosc,
        @Size(max = 40) String telefon,
        @Size(max = 1000) String uwagi
) {
    public static GospodarstwoFormularz pusty() {
        return new GospodarstwoFormularz("", "", "", "", "", "");
    }

    public static GospodarstwoFormularz z(Gospodarstwo gospodarstwo) {
        return new GospodarstwoFormularz(
                gospodarstwo.getNazwa(),
                gospodarstwo.getUlica(),
                gospodarstwo.getKodPocztowy(),
                gospodarstwo.getMiejscowosc(),
                gospodarstwo.getTelefon(),
                gospodarstwo.getUwagi()
        );
    }
}
