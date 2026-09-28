package pl.kea.parafia.kalendarz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record WydarzenieFormularz(
        @NotBlank @Size(max = 200) String tytul,
        @Size(max = 2000) String opis,
        @NotNull LocalDateTime poczatek,
        @NotNull LocalDateTime koniec,
        @Size(max = 200) String miejsce,
        Long salaId,
        @NotNull RodzajWydarzenia rodzaj,
        Boolean widocznePublicznie,
        @Size(max = 160) String organizator
) {
    public static WydarzenieFormularz pusty() {
        LocalDateTime start = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).plusHours(1);
        return new WydarzenieFormularz("", "", start, start.plusHours(1), "", null, RodzajWydarzenia.SPOTKANIE, false, "");
    }

    public static WydarzenieFormularz z(Wydarzenie wydarzenie) {
        return new WydarzenieFormularz(
                wydarzenie.getTytul(), wydarzenie.getOpis(), wydarzenie.getPoczatek(), wydarzenie.getKoniec(),
                wydarzenie.getMiejsce(), wydarzenie.getSala() == null ? null : wydarzenie.getSala().getId(),
                wydarzenie.getRodzaj(), wydarzenie.isWidocznePublicznie(), wydarzenie.getOrganizator()
        );
    }
}
