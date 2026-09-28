package pl.kea.parafia.duszpasterstwo;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaRepository;
import pl.kea.parafia.osoba.StatusCzlonkostwa;
import pl.kea.parafia.wspolne.NieZnalezionoException;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class DuszpasterstwoService {

    private final SprawaRepository sprawy;
    private final WizytaRepository wizyty;
    private final OsobaRepository osoby;

    public DuszpasterstwoService(SprawaRepository sprawy, WizytaRepository wizyty, OsobaRepository osoby) {
        this.sprawy = sprawy;
        this.wizyty = wizyty;
        this.osoby = osoby;
    }

    @Transactional(readOnly = true)
    public List<SprawaDuszpasterska> lista() {
        return sprawy.findAllByOrderByZmodyfikowanoDesc();
    }

    @Transactional(readOnly = true)
    public List<SprawaDuszpasterska> sprawyOsoby(Long osobaId) {
        return sprawy.findByOsobaIdOrderByZmodyfikowanoDesc(osobaId);
    }

    @Transactional(readOnly = true)
    public SprawaDuszpasterska znajdz(Long id) {
        return sprawy.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej sprawy."));
    }

    @Transactional(readOnly = true)
    public List<Wizyta> wizyty(Long sprawaId) {
        return wizyty.findBySprawaIdOrderByDataWizytyDesc(sprawaId);
    }

    @Transactional(readOnly = true)
    public List<Wizyta> ostatnieWizyty() {
        return wizyty.findTop8ByOrderByDataWizytyDesc();
    }

    @Transactional(readOnly = true)
    public long wizytyWRoku() {
        return wizyty.countByDataWizytyGreaterThanEqual(LocalDate.now().minusYears(1));
    }

    @Transactional
    public SprawaDuszpasterska zapiszSprawe(Long id, SprawaFormularz formularz) {
        Osoba osoba = osoba(formularz.osobaId());
        if (osoba.getStatus() == StatusCzlonkostwa.ZMARLY && id == null) {
            throw new RegulaParafiiException("Nie otwieramy nowej sprawy duszpasterskiej dla osoby zmarłej.");
        }
        SprawaDuszpasterska sprawa = id == null ? new SprawaDuszpasterska() : znajdz(id);
        sprawa.setOsoba(osoba);
        sprawa.setTemat(formularz.temat());
        sprawa.setOpis(formularz.opis());
        sprawa.setStatus(formularz.status());
        return sprawy.save(sprawa);
    }

    @Transactional
    public Wizyta zapiszWizyte(WizytaFormularz formularz) {
        Osoba osoba = osoba(formularz.osobaId());
        SprawaDuszpasterska sprawa = null;
        if (formularz.sprawaId() != null) {
            sprawa = znajdz(formularz.sprawaId());
            if (sprawa.getStatus() == StatusSprawy.ZAMKNIETA) {
                throw new RegulaParafiiException("Sprawa jest zamknięta. Otwórz ją, zanim dopiszesz wizytę.");
            }
            if (!sprawa.getOsoba().getId().equals(osoba.getId())) {
                throw new RegulaParafiiException("Wizyta musi dotyczyć osoby, której sprawa dotyczy.");
            }
        }
        if (formularz.nastepnyKontakt() != null && formularz.nastepnyKontakt().isBefore(formularz.dataWizyty())) {
            throw new RegulaParafiiException("Następny kontakt nie może być wcześniejszy niż wizyta.");
        }
        Wizyta wizyta = new Wizyta();
        wizyta.setSprawa(sprawa);
        wizyta.setOsoba(osoba);
        wizyta.setDataWizyty(formularz.dataWizyty());
        wizyta.setMiejsce(formularz.miejsce());
        wizyta.setNotatka(formularz.notatka());
        wizyta.setNastepnyKontakt(formularz.nastepnyKontakt());
        return wizyty.save(wizyta);
    }

    private Osoba osoba(Long id) {
        return osoby.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej osoby."));
    }
}
