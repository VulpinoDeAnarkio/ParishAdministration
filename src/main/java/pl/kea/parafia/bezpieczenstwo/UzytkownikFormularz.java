package pl.kea.parafia.bezpieczenstwo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UzytkownikFormularz(
        @NotBlank @Size(max = 80) String imie,
        @NotBlank @Size(max = 80) String nazwisko,
        @NotBlank @Email @Size(max = 180) String email,
        @Size(max = 72) String haslo,
        @NotNull Rola rola,
        boolean aktywny
) {
    public static UzytkownikFormularz pusty() {
        return new UzytkownikFormularz("", "", "", "", Rola.SEKRETARIAT, true);
    }

    public static UzytkownikFormularz z(Uzytkownik uzytkownik) {
        return new UzytkownikFormularz(
                uzytkownik.getImie(),
                uzytkownik.getNazwisko(),
                uzytkownik.getEmail(),
                "",
                uzytkownik.getRola(),
                uzytkownik.isAktywny()
        );
    }
}
