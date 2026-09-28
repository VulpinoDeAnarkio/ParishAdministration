package pl.kea.parafia.parafia;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfilFormularz(
        @NotBlank @Size(max = 200) String nazwa,
        @NotBlank @Size(max = 160) String diecezja,
        @Size(max = 160) String ulica,
        @Size(max = 12) String kodPocztowy,
        @Size(max = 120) String miejscowosc,
        @Size(max = 40) String telefon,
        @Email @Size(max = 180) String email,
        @Size(max = 200) String www,
        @Size(max = 160) String proboszcz,
        @Size(max = 160) String kurator,
        @Size(max = 160) String poraNabozenstwa,
        @Size(max = 2000) String opis
) {
    public static ProfilFormularz z(ProfilParafii profil) {
        return new ProfilFormularz(
                profil.getNazwa(),
                profil.getDiecezja(),
                profil.getUlica(),
                profil.getKodPocztowy(),
                profil.getMiejscowosc(),
                profil.getTelefon(),
                profil.getEmail(),
                profil.getWww(),
                profil.getProboszcz(),
                profil.getKurator(),
                profil.getPoraNabozenstwa(),
                profil.getOpis()
        );
    }
}
