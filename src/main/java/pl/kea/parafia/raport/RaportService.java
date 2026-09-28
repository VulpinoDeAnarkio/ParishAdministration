package pl.kea.parafia.raport;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.finanse.Operacja;
import pl.kea.parafia.finanse.OperacjaRepository;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaRepository;

@Service
public class RaportService {

    private final OsobaRepository osoby;
    private final OperacjaRepository operacje;

    public RaportService(OsobaRepository osoby, OperacjaRepository operacje) {
        this.osoby = osoby;
        this.operacje = operacje;
    }

    @Transactional(readOnly = true)
    public String osobyCsv() {
        StringBuilder csv = naglowek("imię;nazwisko;status;e-mail;telefon");
        for (Osoba osoba : osoby.findAllByOrderByNazwiskoAscImieAsc()) {
            csv.append(pole(osoba.getImie())).append(';')
                    .append(pole(osoba.getNazwisko())).append(';')
                    .append(pole(osoba.getStatus().getEtykieta())).append(';')
                    .append(pole(osoba.getEmail())).append(';')
                    .append(pole(osoba.getTelefon())).append('\n');
        }
        return csv.toString();
    }

    @Transactional(readOnly = true)
    public String finanseCsv() {
        StringBuilder csv = naglowek("data;fundusz;rodzaj;kierunek;kwota;opis");
        List<Operacja> lista = operacje.findAll();
        lista.sort((a, b) -> b.getDataOperacji().compareTo(a.getDataOperacji()));
        for (Operacja operacja : lista) {
            csv.append(operacja.getDataOperacji()).append(';')
                    .append(pole(operacja.getFundusz().getNazwa())).append(';')
                    .append(pole(operacja.getRodzaj().getEtykieta())).append(';')
                    .append(pole(operacja.getKierunek().getEtykieta())).append(';')
                    .append(operacja.getKwota().toPlainString()).append(';')
                    .append(pole(operacja.getOpis())).append('\n');
        }
        return csv.toString();
    }

    private StringBuilder naglowek(String kolumny) {
        return new StringBuilder("\uFEFF").append(kolumny).append('\n');
    }

    private String pole(String wartosc) {
        if (wartosc == null) {
            return "";
        }
        String oczyszczone = wartosc.replace("\"", "\"\"");
        if (oczyszczone.contains(";") || oczyszczone.contains("\"") || oczyszczone.contains("\n")) {
            return "\"" + oczyszczone + "\"";
        }
        return oczyszczone;
    }
}
