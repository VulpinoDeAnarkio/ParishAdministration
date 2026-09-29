package pl.kea.parafia.obiekt;

public enum StatusRezerwacji {
    POTWIERDZONA("Potwierdzona"),
    ANULOWANA("Anulowana");

    private final String etykieta;

    StatusRezerwacji(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
