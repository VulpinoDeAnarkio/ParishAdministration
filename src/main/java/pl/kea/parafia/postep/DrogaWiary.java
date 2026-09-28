package pl.kea.parafia.postep;

import java.util.ArrayList;
import java.util.List;

public final class DrogaWiary {

    private DrogaWiary() {
    }

    public static List<EtapDrogi> etapy(DaneDrogi dane) {
        List<EtapDrogi> wynik = new ArrayList<>();
        if (dane.ochrzczony()) {
            wynik.add(EtapDrogi.CHRZEST);
        }
        if (dane.wNauce() || dane.konfirmowany()) {
            wynik.add(EtapDrogi.NAUKA);
        }
        if (dane.konfirmowany()) {
            wynik.add(EtapDrogi.KONFIRMACJA);
        }
        if (dane.czlonek()) {
            wynik.add(EtapDrogi.CZLONKOSTWO);
        }
        if (dane.wieczerza()) {
            wynik.add(EtapDrogi.WIECZERZA);
        }
        if (dane.sluzba()) {
            wynik.add(EtapDrogi.SLUZBA);
        }
        return List.copyOf(wynik);
    }
}
