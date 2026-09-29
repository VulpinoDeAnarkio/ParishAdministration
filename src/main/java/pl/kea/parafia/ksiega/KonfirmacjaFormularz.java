package pl.kea.parafia.ksiega;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record KonfirmacjaFormularz(
        @NotNull Long osobaId,
        @NotNull @PastOrPresent LocalDate dataKonfirmacji,
        @NotBlank @Size(max = 200) String miejsce,
        @NotBlank @Size(max = 160) String duchowny,
        @Size(max = 40) String rocznik,
        @Size(max = 300) String werset,
        @NotBlank @Size(max = 40) String numerKsiegi,
        @Size(max = 1000) String uwagi
) {
    public static KonfirmacjaFormularz pusty(Long osobaId, String numer) {
        return new KonfirmacjaFormularz(osobaId, LocalDate.now(), "Kościół Zmiłowania Bożego w Poznaniu", "", "", "", numer, "");
    }

    public static KonfirmacjaFormularz z(Konfirmacja wpis) {
        return new KonfirmacjaFormularz(
                wpis.getOsoba().getId(), wpis.getDataKonfirmacji(), wpis.getMiejsce(), wpis.getDuchowny(),
                wpis.getRocznik(), wpis.getWerset(), wpis.getNumerKsiegi(), wpis.getUwagi()
        );
    }
}
