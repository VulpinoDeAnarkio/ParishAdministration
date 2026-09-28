package pl.kea.parafia.sluzba;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.kalendarz.KalendarzService;
import pl.kea.parafia.nabozenstwo.NabozenstwoService;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaRepository;
import pl.kea.parafia.osoba.StatusCzlonkostwa;
import pl.kea.parafia.wspolne.NieZnalezionoException;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class SluzbaService {

    private final DyzurRepository dyzury;
    private final ZadanieRepository zadania;
    private final OsobaRepository osoby;
    private final NabozenstwoService nabozenstwa;
    private final KalendarzService kalendarz;

    public SluzbaService(
            DyzurRepository dyzury,
            ZadanieRepository zadania,
            OsobaRepository osoby,
            NabozenstwoService nabozenstwa,
            KalendarzService kalendarz
    ) {
        this.dyzury = dyzury;
        this.zadania = zadania;
        this.osoby = osoby;
        this.nabozenstwa = nabozenstwa;
        this.kalendarz = kalendarz;
    }

    @Transactional(readOnly = true)
    public List<Zadanie> zadania() {
        return zadania.findAllByOrderByStatusAscTerminAsc();
    }

    @Transactional(readOnly = true)
    public List<Dyzur> dyzury(Long nabozenstwoId) {
        return dyzury.findByNabozenstwoIdOrderByRolaAsc(nabozenstwoId);
    }

    @Transactional(readOnly = true)
    public long otwarteZadania() {
        return zadania.countByStatusNot(StatusZadania.ZROBIONE);
    }

    @Transactional(readOnly = true)
    public boolean pelniSluzbe(Long osobaId) {
        return dyzury.existsByOsobaId(osobaId);
    }

    @Transactional
    public Dyzur dodajDyzur(DyzurFormularz formularz) {
        if (dyzury.findByNabozenstwoIdAndOsobaIdAndRola(formularz.nabozenstwoId(), formularz.osobaId(), formularz.rola()).isPresent()) {
            throw new RegulaParafiiException("Ta osoba ma już ten dyżur na tym nabożeństwie.");
        }
        Dyzur dyzur = new Dyzur();
        dyzur.setNabozenstwo(nabozenstwa.znajdz(formularz.nabozenstwoId()));
        dyzur.setOsoba(zywy(formularz.osobaId()));
        dyzur.setRola(formularz.rola());
        return dyzury.save(dyzur);
    }

    @Transactional
    public void usunDyzur(Long id) {
        dyzury.delete(dyzury.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego dyżuru.")));
    }

    @Transactional
    public Zadanie zapiszZadanie(Long id, ZadanieFormularz formularz) {
        Zadanie zadanie = id == null ? new Zadanie() : zadania.findById(id)
                .orElseThrow(() -> new NieZnalezionoException("Nie ma takiego zadania."));
        zadanie.setTytul(formularz.tytul());
        zadanie.setOpis(formularz.opis());
        zadanie.setTermin(formularz.termin());
        zadanie.setStatus(formularz.status());
        zadanie.setOsoba(formularz.osobaId() == null ? null : zywy(formularz.osobaId()));
        zadanie.setWydarzenie(formularz.wydarzenieId() == null ? null : kalendarz.znajdz(formularz.wydarzenieId()));
        return zadania.save(zadanie);
    }

    private Osoba zywy(Long id) {
        Osoba osoba = osoby.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej osoby."));
        if (osoba.getStatus() == StatusCzlonkostwa.ZMARLY) {
            throw new RegulaParafiiException("Osobie zmarłej nie powierzamy służby ani zadania.");
        }
        return osoba;
    }
}
