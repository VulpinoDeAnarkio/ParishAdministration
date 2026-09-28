package pl.kea.parafia.postep;

public enum EtapDrogi {
    CHRZEST("Chrzest"),
    NAUKA("Nauka konfirmacyjna"),
    KONFIRMACJA("Konfirmacja"),
    CZLONKOSTWO("Członkostwo"),
    WIECZERZA("Wieczerza Pańska"),
    SLUZBA("Służba w zborze");

    private final String etykieta;

    EtapDrogi(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
