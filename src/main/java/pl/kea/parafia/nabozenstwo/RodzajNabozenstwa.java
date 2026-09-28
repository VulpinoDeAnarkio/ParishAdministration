package pl.kea.parafia.nabozenstwo;

public enum RodzajNabozenstwa {
    GLOWNE("Nabożeństwo główne"),
    WIECZORNE("Nabożeństwo wieczorne"),
    TYGODNIOWE("Nabożeństwo tygodniowe"),
    SWIATECZNE("Nabożeństwo świąteczne"),
    SLUBNE("Nabożeństwo ślubne"),
    POGRZEBOWE("Nabożeństwo pogrzebowe"),
    SZKOLNE("Nabożeństwo szkolne");

    private final String etykieta;

    RodzajNabozenstwa(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
