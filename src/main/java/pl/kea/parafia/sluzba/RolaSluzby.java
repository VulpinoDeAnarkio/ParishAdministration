package pl.kea.parafia.sluzba;

public enum RolaSluzby {
    ORGANISTA("Organista"),
    LEKTOR("Lektor"),
    KOSCIELNY("Kościelny"),
    SZAFARZ("Szafarz Wieczerzy"),
    PROWADZACY("Prowadzący liturgię");

    private final String etykieta;

    RolaSluzby(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
