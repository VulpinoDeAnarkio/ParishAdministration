package pl.kea.parafia.osoba;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record OsobaFormularz(
        @NotBlank @Size(max = 80) String imie,
        @Size(max = 80) String drugieImie,
        @NotBlank @Size(max = 80) String nazwisko,
        @Size(max = 80) String nazwiskoRodowe,
        @NotNull Plec plec,
        @PastOrPresent LocalDate dataUrodzenia,
        @Size(max = 160) String miejsceUrodzenia,
        @Email @Size(max = 180) String email,
        @Size(max = 40) String telefon,
        @Size(max = 160) String ulica,
        @Size(max = 12) String kodPocztowy,
        @Size(max = 120) String miejscowosc,
        @NotNull StatusCzlonkostwa status,
        RolaWRodzinie rolaWRodzinie,
        LocalDate dataPrzyjecia,
        LocalDate dataWystapienia,
        Boolean zgodaNaKontakt,
        Boolean uczestniczyWWieczerzy,
        @Size(max = 2000) String uwagi,
        Long gospodarstwoId
) {
    public static OsobaFormularz pusty() {
        return new OsobaFormularz(
                "", null, "", null, Plec.KOBIETA, null, null, null, null, null, null, null,
                StatusCzlonkostwa.SYMPATYK, null, null, null, false, false, null, null
        );
    }

    public static OsobaFormularz z(Osoba osoba) {
        return new OsobaFormularz(
                osoba.getImie(),
                osoba.getDrugieImie(),
                osoba.getNazwisko(),
                osoba.getNazwiskoRodowe(),
                osoba.getPlec(),
                osoba.getDataUrodzenia(),
                osoba.getMiejsceUrodzenia(),
                osoba.getEmail(),
                osoba.getTelefon(),
                osoba.getUlica(),
                osoba.getKodPocztowy(),
                osoba.getMiejscowosc(),
                osoba.getStatus(),
                osoba.getRolaWRodzinie(),
                osoba.getDataPrzyjecia(),
                osoba.getDataWystapienia(),
                osoba.isZgodaNaKontakt(),
                osoba.isUczestniczyWWieczerzy(),
                osoba.getUwagi(),
                osoba.getGospodarstwo() == null ? null : osoba.getGospodarstwo().getId()
        );
    }
}
