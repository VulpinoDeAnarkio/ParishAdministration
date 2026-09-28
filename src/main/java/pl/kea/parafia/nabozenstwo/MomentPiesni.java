package pl.kea.parafia.nabozenstwo;

public enum MomentPiesni {
    WEJSCIE("Wejście"),
    PRZED_EWANGELIA("Przed Ewangelią"),
    PO_KAZANIU("Po kazaniu"),
    WIECZERZA("Wieczerza Pańska"),
    ZAKONCZENIE("Zakończenie"),
    INNY("Inny moment");

    private final String etykieta;

    MomentPiesni(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
