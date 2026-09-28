package pl.kea.parafia.bezpieczenstwo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "uzytkownik")
public class Uzytkownik extends EncjaAudytowana {

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(nullable = false, length = 100)
    private String haslo;

    @Column(nullable = false, length = 80)
    private String imie;

    @Column(nullable = false, length = 80)
    private String nazwisko;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private Rola rola;

    @Column(nullable = false)
    private boolean aktywny = true;

    protected Uzytkownik() {
    }

    public Uzytkownik(String email, String haslo, String imie, String nazwisko, Rola rola, boolean aktywny) {
        this.email = email;
        this.haslo = haslo;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.rola = rola;
        this.aktywny = aktywny;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public Rola getRola() {
        return rola;
    }

    public void setRola(Rola rola) {
        this.rola = rola;
    }

    public boolean isAktywny() {
        return aktywny;
    }

    public void setAktywny(boolean aktywny) {
        this.aktywny = aktywny;
    }

    public String getImieINazwisko() {
        return imie + " " + nazwisko;
    }
}
