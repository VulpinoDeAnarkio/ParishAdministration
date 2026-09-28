package pl.kea.parafia.kalendarz;

public enum RodzajWydarzenia {
    NABOZENSTWO("Nabożeństwo"),
    SPOTKANIE("Spotkanie"),
    LEKCJA("Lekcja"),
    KONCERT("Koncert"),
    ZEBRANIE("Zebranie"),
    INNE("Inne");

    private final String etykieta;

    RodzajWydarzenia(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
