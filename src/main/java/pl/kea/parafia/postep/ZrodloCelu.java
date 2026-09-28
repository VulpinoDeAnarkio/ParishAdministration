package pl.kea.parafia.postep;

public enum ZrodloCelu {
    LICZBA_CZLONKOW("Liczba członków"),
    SREDNIA_OBECNOSC("Średnia obecność na nabożeństwie"),
    SUMA_WPLYWOW("Suma wpływów (zł)"),
    KONFIRMACJA_PROCENT("Konfirmacja rocznika (%)"),
    WIZYTY("Wizyty duszpasterskie w roku"),
    RECZNY("Wartość wpisana ręcznie");

    private final String etykieta;

    ZrodloCelu(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
