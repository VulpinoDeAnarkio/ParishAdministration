package pl.kea.parafia.sluzba;

import jakarta.validation.constraints.NotNull;

public record DyzurFormularz(@NotNull Long nabozenstwoId, @NotNull Long osobaId, @NotNull RolaSluzby rola) {
}
