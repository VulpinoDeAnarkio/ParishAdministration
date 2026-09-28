package pl.kea.parafia.kalendarz;

import java.time.LocalDate;
import java.util.List;

public record DzienKalendarza(LocalDate data, boolean wMiesiacu, boolean dzisiaj, List<Wydarzenie> wydarzenia) {
}
