package pl.kea.parafia.osoba;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.wspolne.NieZnalezionoException;

@Service
public class OsobaService {

    private final OsobaRepository osoby;
    private final GospodarstwoRepository gospodarstwa;

    public OsobaService(OsobaRepository osoby, GospodarstwoRepository gospodarstwa) {
        this.osoby = osoby;
        this.gospodarstwa = gospodarstwa;
    }

    @Transactional(readOnly = true)
    public Page<Osoba> szukaj(String q, StatusCzlonkostwa status, Pageable pageable) {
        String zapytanie = q == null || q.isBlank() ? null : q.trim();
        return osoby.szukaj(zapytanie, status, pageable);
    }

    @Transactional(readOnly = true)
    public Osoba znajdz(Long id) {
        return osoby.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej osoby w kartotece."));
    }

    @Transactional(readOnly = true)
    public List<Osoba> wszyscy() {
        return osoby.findAllByOrderByNazwiskoAscImieAsc();
    }

    @Transactional(readOnly = true)
    public List<Osoba> domownicy(Long gospodarstwoId) {
        return osoby.findByGospodarstwoIdOrderByNazwiskoAscImieAsc(gospodarstwoId);
    }

    @Transactional(readOnly = true)
    public long liczbaCzlonkow() {
        return osoby.countByStatus(StatusCzlonkostwa.CZLONEK);
    }

    @Transactional
    public Osoba utworz(OsobaFormularz formularz) {
        Osoba osoba = new Osoba();
        uzupelnij(osoba, formularz);
        return osoby.save(osoba);
    }

    @Transactional
    public Osoba aktualizuj(Long id, OsobaFormularz formularz) {
        Osoba osoba = znajdz(id);
        uzupelnij(osoba, formularz);
        return osoba;
    }

    private void uzupelnij(Osoba osoba, OsobaFormularz formularz) {
        osoba.setImie(formularz.imie());
        osoba.setDrugieImie(formularz.drugieImie());
        osoba.setNazwisko(formularz.nazwisko());
        osoba.setNazwiskoRodowe(formularz.nazwiskoRodowe());
        osoba.setPlec(formularz.plec());
        osoba.setDataUrodzenia(formularz.dataUrodzenia());
        osoba.setMiejsceUrodzenia(formularz.miejsceUrodzenia());
        osoba.setEmail(formularz.email());
        osoba.setTelefon(formularz.telefon());
        osoba.setStatus(formularz.status());
        osoba.setRolaWRodzinie(formularz.rolaWRodzinie());
        osoba.setDataPrzyjecia(formularz.dataPrzyjecia());
        osoba.setDataWystapienia(formularz.dataWystapienia());
        osoba.setZgodaNaKontakt(Boolean.TRUE.equals(formularz.zgodaNaKontakt()));
        osoba.setUczestniczyWWieczerzy(Boolean.TRUE.equals(formularz.uczestniczyWWieczerzy()));
        osoba.setUwagi(formularz.uwagi());
        Gospodarstwo gospodarstwo = formularz.gospodarstwoId() == null
                ? null
                : gospodarstwa.findById(formularz.gospodarstwoId())
                .orElseThrow(() -> new NieZnalezionoException("Nie ma takiego gospodarstwa."));
        osoba.setGospodarstwo(gospodarstwo);
        if (gospodarstwo != null && formularz.ulica() == null) {
            osoba.setUlica(gospodarstwo.getUlica());
            osoba.setKodPocztowy(gospodarstwo.getKodPocztowy());
            osoba.setMiejscowosc(gospodarstwo.getMiejscowosc());
        } else {
            osoba.setUlica(formularz.ulica());
            osoba.setKodPocztowy(formularz.kodPocztowy());
            osoba.setMiejscowosc(formularz.miejscowosc());
        }
    }
}
