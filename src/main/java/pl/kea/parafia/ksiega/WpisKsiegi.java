package pl.kea.parafia.ksiega;

import java.time.LocalDate;

public record WpisKsiegi(LocalDate data, String rodzaj, String opis, String edycja) {
}
