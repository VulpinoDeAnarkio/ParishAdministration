package pl.kea.parafia.bezpieczenstwo;

public enum Rola {
    ADMIN("Administrator"),
    PROBOSZCZ("Proboszcz"),
    SEKRETARIAT("Sekretariat"),
    SKARBNIK("Skarbnik"),
    DUSZPASTERZ("Duszpasterz");

    private final String etykieta;

    Rola(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
