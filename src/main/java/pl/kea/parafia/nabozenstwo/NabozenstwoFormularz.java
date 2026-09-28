package pl.kea.parafia.nabozenstwo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record NabozenstwoFormularz(
        @NotNull LocalDate data,
        @NotNull LocalTime godzina,
        @NotNull Integer minuty,
        @NotNull RodzajNabozenstwa rodzaj,
        @Size(max = 160) String niedzielaRoku,
        @Size(max = 160) String kaznodzieja,
        @Size(max = 200) String czytanieStarego,
        @Size(max = 200) String psalm,
        @Size(max = 200) String czytanieApostolskie,
        @Size(max = 200) String ewangelia,
        @Size(max = 200) String tytulKazania,
        @Size(max = 200) String wersetKazania,
        Boolean wieczerzaPanska,
        @Size(max = 2000) String uwagiLiturgiczne,
        @Valid List<PiesnFormularz> piesni
) {
    public static NabozenstwoFormularz pusty() {
        return new NabozenstwoFormularz(
                LocalDate.now().plusDays(1), LocalTime.of(10, 0), 75, RodzajNabozenstwa.GLOWNE,
                "", "", "", "", "", "", "", "", true, "",
                List.of(
                        PiesnFormularz.pusta(MomentPiesni.WEJSCIE),
                        PiesnFormularz.pusta(MomentPiesni.PRZED_EWANGELIA),
                        PiesnFormularz.pusta(MomentPiesni.PO_KAZANIU),
                        PiesnFormularz.pusta(MomentPiesni.ZAKONCZENIE)
                )
        );
    }
}
