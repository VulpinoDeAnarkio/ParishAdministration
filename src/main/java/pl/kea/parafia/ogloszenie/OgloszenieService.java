package pl.kea.parafia.ogloszenie;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.wspolne.NieZnalezionoException;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class OgloszenieService {

    private final OgloszenieRepository ogloszenia;

    public OgloszenieService(OgloszenieRepository ogloszenia) {
        this.ogloszenia = ogloszenia;
    }

    @Transactional(readOnly = true)
    public List<Ogloszenie> wszystkie() {
        return ogloszenia.findAllByOrderByObowiazujeOdDesc();
    }

    @Transactional(readOnly = true)
    public List<Ogloszenie> publiczne(LocalDate dzien) {
        return ogloszenia.findByWidocznePublicznieIsTrueOrderByObowiazujeOdDesc().stream()
                .filter(ogloszenie -> ogloszenie.jestAktualne(dzien))
                .toList();
    }

    @Transactional(readOnly = true)
    public Ogloszenie znajdz(Long id) {
        return ogloszenia.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego ogłoszenia."));
    }

    @Transactional
    public Ogloszenie zapisz(Long id, OgloszenieFormularz formularz) {
        if (formularz.obowiazujeDo() != null && formularz.obowiazujeDo().isBefore(formularz.obowiazujeOd())) {
            throw new RegulaParafiiException("Data końca ogłoszenia nie może być wcześniejsza niż początek.");
        }
        Ogloszenie ogloszenie = id == null ? new Ogloszenie() : znajdz(id);
        ogloszenie.setTytul(formularz.tytul());
        ogloszenie.setTresc(formularz.tresc());
        ogloszenie.setObowiazujeOd(formularz.obowiazujeOd());
        ogloszenie.setObowiazujeDo(formularz.obowiazujeDo());
        ogloszenie.setWidocznePublicznie(Boolean.TRUE.equals(formularz.widocznePublicznie()));
        return ogloszenia.save(ogloszenie);
    }
}
