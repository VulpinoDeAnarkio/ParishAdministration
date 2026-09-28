package pl.kea.parafia.grupa;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaRepository;
import pl.kea.parafia.wspolne.NieZnalezionoException;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class GrupaService {

    private final GrupaRepository grupy;
    private final CzlonekGrupyRepository czlonkowie;
    private final OsobaRepository osoby;

    public GrupaService(GrupaRepository grupy, CzlonekGrupyRepository czlonkowie, OsobaRepository osoby) {
        this.grupy = grupy;
        this.czlonkowie = czlonkowie;
        this.osoby = osoby;
    }

    @Transactional(readOnly = true)
    public List<Grupa> lista() {
        return grupy.findAllByOrderByNazwaAsc();
    }

    @Transactional(readOnly = true)
    public Grupa znajdz(Long id) {
        return grupy.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej grupy."));
    }

    @Transactional(readOnly = true)
    public List<CzlonekGrupy> czlonkowie(Long grupaId) {
        return czlonkowie.findByGrupaIdOrderByOsobaNazwiskoAsc(grupaId);
    }

    @Transactional(readOnly = true)
    public List<CzlonekGrupy> grupyOsoby(Long osobaId) {
        return czlonkowie.findByOsobaIdOrderByGrupaNazwaAsc(osobaId);
    }

    @Transactional(readOnly = true)
    public boolean sluzy(Long osobaId) {
        return czlonkowie.existsByOsobaIdAndDoDniaIsNull(osobaId);
    }

    @Transactional
    public Grupa zapisz(Long id, GrupaFormularz formularz) {
        Grupa grupa = id == null ? new Grupa() : znajdz(id);
        grupa.setNazwa(formularz.nazwa());
        grupa.setRodzaj(formularz.rodzaj());
        grupa.setOpis(formularz.opis());
        grupa.setTermin(formularz.termin());
        grupa.setMiejsce(formularz.miejsce());
        grupa.setAktywna(!Boolean.FALSE.equals(formularz.aktywna()));
        grupa.setLider(formularz.liderId() == null ? null : osoba(formularz.liderId()));
        return grupy.save(grupa);
    }

    @Transactional
    public void dodajCzlonka(Long grupaId, CzlonekFormularz formularz) {
        Grupa grupa = znajdz(grupaId);
        if (czlonkowie.findByGrupaIdAndOsobaId(grupaId, formularz.osobaId()).isPresent()) {
            throw new RegulaParafiiException("Ta osoba jest już w grupie.");
        }
        CzlonekGrupy czlonek = new CzlonekGrupy();
        czlonek.setGrupa(grupa);
        czlonek.setOsoba(osoba(formularz.osobaId()));
        czlonek.setFunkcja(formularz.funkcja() == null ? "Członek" : formularz.funkcja());
        czlonek.setOdDnia(formularz.odDnia() == null ? LocalDate.now() : formularz.odDnia());
        czlonkowie.save(czlonek);
    }

    @Transactional
    public void zakoncz(Long czlonekId) {
        CzlonekGrupy czlonek = czlonkowie.findById(czlonekId)
                .orElseThrow(() -> new NieZnalezionoException("Nie ma takiego członkostwa."));
        czlonek.setDoDnia(LocalDate.now());
    }

    private Osoba osoba(Long id) {
        return osoby.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej osoby."));
    }
}
