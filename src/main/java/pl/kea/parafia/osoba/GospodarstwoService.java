package pl.kea.parafia.osoba;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.wspolne.NieZnalezionoException;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class GospodarstwoService {

    private final GospodarstwoRepository gospodarstwa;
    private final OsobaRepository osoby;

    public GospodarstwoService(GospodarstwoRepository gospodarstwa, OsobaRepository osoby) {
        this.gospodarstwa = gospodarstwa;
        this.osoby = osoby;
    }

    @Transactional(readOnly = true)
    public java.util.List<Gospodarstwo> lista() {
        return gospodarstwa.findAllByOrderByNazwaAsc();
    }

    @Transactional(readOnly = true)
    public Map<Long, Long> liczbyOsob() {
        Map<Long, Long> liczby = new LinkedHashMap<>();
        for (Object[] wiersz : osoby.liczbyWGospodarstwach()) {
            liczby.put((Long) wiersz[0], (Long) wiersz[1]);
        }
        return liczby;
    }

    @Transactional(readOnly = true)
    public Gospodarstwo znajdz(Long id) {
        return gospodarstwa.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego gospodarstwa."));
    }

    @Transactional
    public Gospodarstwo utworz(GospodarstwoFormularz formularz) {
        return gospodarstwa.save(uzupelnij(new Gospodarstwo(formularz.nazwa()), formularz));
    }

    @Transactional
    public Gospodarstwo aktualizuj(Long id, GospodarstwoFormularz formularz) {
        return uzupelnij(znajdz(id), formularz);
    }

    @Transactional
    public void usun(Long id) {
        if (!osoby.findByGospodarstwoIdOrderByNazwiskoAscImieAsc(id).isEmpty()) {
            throw new RegulaParafiiException("Najpierw wypisz osoby z tego gospodarstwa.");
        }
        gospodarstwa.delete(znajdz(id));
    }

    private Gospodarstwo uzupelnij(Gospodarstwo gospodarstwo, GospodarstwoFormularz formularz) {
        gospodarstwo.setNazwa(formularz.nazwa());
        gospodarstwo.setUlica(formularz.ulica());
        gospodarstwo.setKodPocztowy(formularz.kodPocztowy());
        gospodarstwo.setMiejscowosc(formularz.miejscowosc());
        gospodarstwo.setTelefon(formularz.telefon());
        gospodarstwo.setUwagi(formularz.uwagi());
        return gospodarstwo;
    }
}
