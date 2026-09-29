package pl.kea.parafia.obiekt;

public enum RodzajObiektu {
    SALKA("Salka"),
    HOTEL("Hotel"),
    POKOJ("Pokój gościnny"),
    SALA("Sala"),
    INNY("Inny obiekt");

    private final String etykieta;

    RodzajObiektu(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
