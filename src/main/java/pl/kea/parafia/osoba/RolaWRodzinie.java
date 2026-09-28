package pl.kea.parafia.osoba;

public enum RolaWRodzinie {
    GLOWA("Osoba prowadząca dom"),
    MALZONEK("Małżonek"),
    DZIECKO("Dziecko"),
    RODZIC("Rodzic"),
    INNA("Inna");

    private final String etykieta;

    RolaWRodzinie(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
