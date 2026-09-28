package pl.kea.parafia.finanse;

public enum RodzajOperacji {
    SKLADKA(KierunekOperacji.WPLYW, "Składka członkowska"),
    TACA(KierunekOperacji.WPLYW, "Taca"),
    DAROWIZNA(KierunekOperacji.WPLYW, "Darowizna"),
    OFIARA(KierunekOperacji.WPLYW, "Ofiara celowa"),
    INNY_WPLYW(KierunekOperacji.WPLYW, "Inny wpływ"),
    MEDIA(KierunekOperacji.WYDATEK, "Media i utrzymanie"),
    WYNAGRODZENIE(KierunekOperacji.WYDATEK, "Wynagrodzenie"),
    REMONT(KierunekOperacji.WYDATEK, "Remont"),
    DIAKONIA(KierunekOperacji.WYDATEK, "Diakonia"),
    INNY_WYDATEK(KierunekOperacji.WYDATEK, "Inny wydatek");

    private final KierunekOperacji kierunek;
    private final String etykieta;

    RodzajOperacji(KierunekOperacji kierunek, String etykieta) {
        this.kierunek = kierunek;
        this.etykieta = etykieta;
    }

    public KierunekOperacji getKierunek() {
        return kierunek;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
