package pl.kea.parafia.finanse;

public enum KierunekOperacji {
    WPLYW("Wpływ"),
    WYDATEK("Wydatek");

    private final String etykieta;

    KierunekOperacji(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
