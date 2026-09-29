package pl.kea.parafia.obiekt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record RezerwacjaFormularz(
        @NotNull Long obiektId,
        @NotNull LocalDateTime poczatek,
        @NotNull LocalDateTime koniec,
        @NotBlank @Size(max = 160) String rezerwujacy,
        Long osobaId,
        @Size(max = 200) String cel,
        @Positive Integer liczbaOsob,
        @Size(max = 1000) String uwagi
) {
    public static RezerwacjaFormularz pusty(Long obiektId) {
        LocalDateTime poczatek = LocalDateTime.now().plusDays(1).withHour(15).withMinute(0).withSecond(0).withNano(0);
        return new RezerwacjaFormularz(obiektId, poczatek, poczatek.plusHours(3), "", null, "", null, "");
    }

    public static RezerwacjaFormularz z(RezerwacjaObiektu rezerwacja) {
        return new RezerwacjaFormularz(
                rezerwacja.getObiekt().getId(),
                rezerwacja.getPoczatek(),
                rezerwacja.getKoniec(),
                rezerwacja.getRezerwujacy(),
                rezerwacja.getOsoba() == null ? null : rezerwacja.getOsoba().getId(),
                rezerwacja.getCel(),
                rezerwacja.getLiczbaOsob(),
                rezerwacja.getUwagi()
        );
    }
}
