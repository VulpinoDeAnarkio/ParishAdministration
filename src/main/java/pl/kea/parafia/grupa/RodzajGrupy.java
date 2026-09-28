package pl.kea.parafia.grupa;

public enum RodzajGrupy {
    CHOR("Chór"),
    SZKOLKA("Szkółka niedzielna"),
    MLODZIEZ("Młodzież"),
    DIAKONIA("Diakonia"),
    RADA("Rada parafialna"),
    BIBLIOJNA("Grupa biblijna"),
    SENIORZY("Seniorzy"),
    KOBIET("Koło pań"),
    INNA("Inna");

    private final String etykieta;

    RodzajGrupy(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
