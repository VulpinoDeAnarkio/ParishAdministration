package pl.kea.parafia.ksiega;

public enum RodzajSlubu {
    SLUB("Ślub"),
    BLOGOSLAWIENSTWO("Błogosławieństwo małżeństwa");

    private final String etykieta;

    RodzajSlubu(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
