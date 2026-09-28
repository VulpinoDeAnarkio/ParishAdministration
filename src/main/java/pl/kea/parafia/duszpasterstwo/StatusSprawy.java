package pl.kea.parafia.duszpasterstwo;

public enum StatusSprawy {
    OTWARTA("Otwarta"),
    W_TOKU("W toku"),
    ZAMKNIETA("Zamknięta");

    private final String etykieta;

    StatusSprawy(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
