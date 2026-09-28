package pl.kea.parafia.osoba;

public enum StatusCzlonkostwa {
    CZLONEK("Członek parafii"),
    DZIECKO_OCHRZCZONE("Dziecko ochrzczone"),
    KATECHUMEN("W nauce konfirmacyjnej"),
    SYMPATYK("Sympatyk"),
    GOSC("Gość"),
    PRZENIESIONY("Przeniesiony"),
    WYSTAPIL("Wystąpił"),
    ZMARLY("Zmarły");

    private final String etykieta;

    StatusCzlonkostwa(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }

    public boolean jestWKartoteceCzynnej() {
        return this == CZLONEK || this == DZIECKO_OCHRZCZONE || this == KATECHUMEN || this == SYMPATYK || this == GOSC;
    }
}
