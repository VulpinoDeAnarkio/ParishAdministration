package pl.kea.parafia.osoba;

public enum Plec {
    KOBIETA("Kobieta"),
    MEZCZYZNA("Mężczyzna"),
    INNA("Inna");

    private final String etykieta;

    Plec(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
