package pl.kea.parafia.sluzba;

public enum StatusZadania {
    DO_ZROBIENIA("Do zrobienia"),
    W_TOKU("W toku"),
    ZROBIONE("Zrobione");

    private final String etykieta;

    StatusZadania(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
