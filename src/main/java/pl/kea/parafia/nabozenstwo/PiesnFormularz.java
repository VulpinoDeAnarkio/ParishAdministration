package pl.kea.parafia.nabozenstwo;

import jakarta.validation.constraints.Size;

public record PiesnFormularz(
        @Size(max = 20) String numerSpiewnika,
        @Size(max = 200) String tytul,
        MomentPiesni moment
) {
    public static PiesnFormularz pusta(MomentPiesni moment) {
        return new PiesnFormularz("", "", moment);
    }
}
