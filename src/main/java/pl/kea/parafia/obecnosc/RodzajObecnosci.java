package pl.kea.parafia.obecnosc;

public enum RodzajObecnosci {
    NABOZENSTWO("Nabożeństwo"),
    WYDARZENIE("Wydarzenie"),
    GRUPA("Grupa");

    private final String etykieta;

    RodzajObecnosci(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
