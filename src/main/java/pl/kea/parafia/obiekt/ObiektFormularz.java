package pl.kea.parafia.obiekt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ObiektFormularz(
        @NotBlank @Size(max = 160) String nazwa,
        @NotNull RodzajObiektu rodzaj,
        Long rodzicId,
        @Positive Integer pojemnosc,
        @Size(max = 200) String lokalizacja,
        @Size(max = 1000) String opis,
        Boolean aktywny
) {
    public static ObiektFormularz pusty() {
        return new ObiektFormularz("", RodzajObiektu.SALKA, null, null, "", "", true);
    }

    public static ObiektFormularz z(Obiekt obiekt) {
        return new ObiektFormularz(
                obiekt.getNazwa(),
                obiekt.getRodzaj(),
                obiekt.getRodzic() == null ? null : obiekt.getRodzic().getId(),
                obiekt.getPojemnosc(),
                obiekt.getLokalizacja(),
                obiekt.getOpis(),
                obiekt.isAktywny()
        );
    }
}
