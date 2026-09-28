package pl.kea.parafia.parafia;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfilParafiiService {

    private final ProfilParafiiRepository repozytorium;

    public ProfilParafiiService(ProfilParafiiRepository repozytorium) {
        this.repozytorium = repozytorium;
    }

    @Transactional(readOnly = true)
    public ProfilParafii pobierz() {
        return repozytorium.findFirstByOrderByIdAsc().orElseGet(ProfilParafii::domyslny);
    }

    @Transactional
    public ProfilParafii zapisz(ProfilFormularz formularz) {
        ProfilParafii profil = repozytorium.findFirstByOrderByIdAsc().orElseGet(ProfilParafii::domyslny);
        profil.setNazwa(formularz.nazwa());
        profil.setDiecezja(formularz.diecezja());
        profil.setUlica(formularz.ulica());
        profil.setKodPocztowy(formularz.kodPocztowy());
        profil.setMiejscowosc(formularz.miejscowosc());
        profil.setTelefon(formularz.telefon());
        profil.setEmail(formularz.email());
        profil.setWww(formularz.www());
        profil.setProboszcz(formularz.proboszcz());
        profil.setKurator(formularz.kurator());
        profil.setPoraNabozenstwa(formularz.poraNabozenstwa());
        profil.setOpis(formularz.opis());
        return repozytorium.save(profil);
    }
}
