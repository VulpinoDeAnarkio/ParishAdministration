package pl.kea.parafia.nabozenstwo;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.kalendarz.KalendarzService;
import pl.kea.parafia.kalendarz.RodzajWydarzenia;
import pl.kea.parafia.kalendarz.Sala;
import pl.kea.parafia.kalendarz.Wydarzenie;
import pl.kea.parafia.kalendarz.WydarzenieFormularz;
import pl.kea.parafia.wspolne.NieZnalezionoException;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class NabozenstwoService {

    private final NabozenstwoRepository nabozenstwa;
    private final KalendarzService kalendarz;

    public NabozenstwoService(NabozenstwoRepository nabozenstwa, KalendarzService kalendarz) {
        this.nabozenstwa = nabozenstwa;
        this.kalendarz = kalendarz;
    }

    @Transactional(readOnly = true)
    public List<Nabozenstwo> najnowsze() {
        return nabozenstwa.findTop30ByOrderByWydarzeniePoczatekDesc();
    }

    @Transactional(readOnly = true)
    public Nabozenstwo znajdz(Long id) {
        return nabozenstwa.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego nabożeństwa."));
    }

    @Transactional
    public Nabozenstwo zapisz(Long id, NabozenstwoFormularz formularz) {
        if (formularz.minuty() == null || formularz.minuty() < 20 || formularz.minuty() > 240) {
            throw new RegulaParafiiException("Czas nabożeństwa podaj w minutach, od 20 do 240.");
        }
        LocalDateTime poczatek = formularz.data().atTime(formularz.godzina());
        LocalDateTime koniec = poczatek.plusMinutes(formularz.minuty());
        Nabozenstwo nabozenstwo = id == null ? new Nabozenstwo() : znajdz(id);
        Long wydarzenieId = nabozenstwo.getWydarzenie() == null ? null : nabozenstwo.getWydarzenie().getId();
        Wydarzenie wydarzenie = kalendarz.zapisz(wydarzenieId, new WydarzenieFormularz(
                formularz.rodzaj().getEtykieta(),
                opisLiturgii(formularz),
                poczatek,
                koniec,
                null,
                kosciol().getId(),
                RodzajWydarzenia.NABOZENSTWO,
                true,
                formularz.kaznodzieja()
        ));
        nabozenstwo.setWydarzenie(wydarzenie);
        nabozenstwo.setRodzaj(formularz.rodzaj());
        nabozenstwo.setNiedzielaRoku(formularz.niedzielaRoku());
        nabozenstwo.setKaznodzieja(formularz.kaznodzieja());
        nabozenstwo.setCzytanieStarego(formularz.czytanieStarego());
        nabozenstwo.setPsalm(formularz.psalm());
        nabozenstwo.setCzytanieApostolskie(formularz.czytanieApostolskie());
        nabozenstwo.setEwangelia(formularz.ewangelia());
        nabozenstwo.setTytulKazania(formularz.tytulKazania());
        nabozenstwo.setWersetKazania(formularz.wersetKazania());
        nabozenstwo.setWieczerzaPanska(Boolean.TRUE.equals(formularz.wieczerzaPanska()));
        nabozenstwo.setUwagiLiturgiczne(formularz.uwagiLiturgiczne());
        nabozenstwo.getPiesni().clear();
        int kolejnosc = 1;
        if (formularz.piesni() != null) {
            for (PiesnFormularz piesn : formularz.piesni()) {
                if (piesn.tytul() == null || piesn.tytul().isBlank()) {
                    continue;
                }
                PiesnNabozenstwa wpis = new PiesnNabozenstwa();
                wpis.setNabozenstwo(nabozenstwo);
                wpis.setKolejnosc(kolejnosc++);
                wpis.setNumerSpiewnika(piesn.numerSpiewnika());
                wpis.setTytul(piesn.tytul().trim());
                wpis.setMoment(piesn.moment() == null ? MomentPiesni.INNY : piesn.moment());
                nabozenstwo.getPiesni().add(wpis);
            }
        }
        return nabozenstwa.save(nabozenstwo);
    }

    public NabozenstwoFormularz formularz(Nabozenstwo nabozenstwo) {
        Wydarzenie wydarzenie = nabozenstwo.getWydarzenie();
        List<PiesnFormularz> piesniFormularza = new java.util.ArrayList<>(nabozenstwo.getPiesni().stream()
                .map(p -> new PiesnFormularz(p.getNumerSpiewnika(), p.getTytul(), p.getMoment()))
                .toList());
        while (piesniFormularza.size() < 4) {
            piesniFormularza.add(PiesnFormularz.pusta(MomentPiesni.INNY));
        }
        long minuty = java.time.Duration.between(wydarzenie.getPoczatek(), wydarzenie.getKoniec()).toMinutes();
        return new NabozenstwoFormularz(
                wydarzenie.getPoczatek().toLocalDate(),
                wydarzenie.getPoczatek().toLocalTime(),
                (int) minuty,
                nabozenstwo.getRodzaj(),
                nabozenstwo.getNiedzielaRoku(),
                nabozenstwo.getKaznodzieja(),
                nabozenstwo.getCzytanieStarego(),
                nabozenstwo.getPsalm(),
                nabozenstwo.getCzytanieApostolskie(),
                nabozenstwo.getEwangelia(),
                nabozenstwo.getTytulKazania(),
                nabozenstwo.getWersetKazania(),
                nabozenstwo.isWieczerzaPanska(),
                nabozenstwo.getUwagiLiturgiczne(),
                piesniFormularza
        );
    }

    private Sala kosciol() {
        return kalendarz.sale().stream()
                .filter(s -> "Kościół".equals(s.getNazwa()))
                .findFirst()
                .orElseThrow(() -> new NieZnalezionoException("Brak sali Kościół. Dodaj ją w kalendarzu."));
    }

    private String opisLiturgii(NabozenstwoFormularz formularz) {
        StringBuilder opis = new StringBuilder();
        if (formularz.niedzielaRoku() != null && !formularz.niedzielaRoku().isBlank()) {
            opis.append(formularz.niedzielaRoku().trim());
        }
        if (formularz.tytulKazania() != null && !formularz.tytulKazania().isBlank()) {
            if (!opis.isEmpty()) {
                opis.append(" · ");
            }
            opis.append(formularz.tytulKazania().trim());
        }
        return opis.isEmpty() ? null : opis.toString();
    }
}
