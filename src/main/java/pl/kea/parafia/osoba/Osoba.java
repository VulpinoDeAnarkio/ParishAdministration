package pl.kea.parafia.osoba;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.Period;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "osoba")
public class Osoba extends EncjaAudytowana {

    @Column(nullable = false, length = 80)
    private String imie;

    private String drugieImie;

    @Column(nullable = false, length = 80)
    private String nazwisko;

    private String nazwiskoRodowe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Plec plec;

    private LocalDate dataUrodzenia;
    private String miejsceUrodzenia;
    private String email;
    private String telefon;
    private String ulica;
    private String kodPocztowy;
    private String miejscowosc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private StatusCzlonkostwa status;

    @Enumerated(EnumType.STRING)
    @Column(name = "rola_w_rodzinie", length = 30)
    private RolaWRodzinie rolaWRodzinie;

    private LocalDate dataPrzyjecia;
    private LocalDate dataWystapienia;

    @Column(nullable = false)
    private boolean zgodaNaKontakt;

    @Column(name = "uczestniczy_w_wieczerzy", nullable = false)
    private boolean uczestniczyWWieczerzy;

    @Column(length = 2000)
    private String uwagi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gospodarstwo_id")
    private Gospodarstwo gospodarstwo;

    protected Osoba() {
    }

    public String getImieINazwisko() {
        return imie + " " + nazwisko;
    }

    public Integer getWiek() {
        if (dataUrodzenia == null) {
            return null;
        }
        LocalDate koniec = dataWystapienia != null && status == StatusCzlonkostwa.ZMARLY ? dataWystapienia : LocalDate.now();
        return Period.between(dataUrodzenia, koniec).getYears();
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getDrugieImie() {
        return drugieImie;
    }

    public void setDrugieImie(String drugieImie) {
        this.drugieImie = drugieImie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getNazwiskoRodowe() {
        return nazwiskoRodowe;
    }

    public void setNazwiskoRodowe(String nazwiskoRodowe) {
        this.nazwiskoRodowe = nazwiskoRodowe;
    }

    public Plec getPlec() {
        return plec;
    }

    public void setPlec(Plec plec) {
        this.plec = plec;
    }

    public LocalDate getDataUrodzenia() {
        return dataUrodzenia;
    }

    public void setDataUrodzenia(LocalDate dataUrodzenia) {
        this.dataUrodzenia = dataUrodzenia;
    }

    public String getMiejsceUrodzenia() {
        return miejsceUrodzenia;
    }

    public void setMiejsceUrodzenia(String miejsceUrodzenia) {
        this.miejsceUrodzenia = miejsceUrodzenia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getKodPocztowy() {
        return kodPocztowy;
    }

    public void setKodPocztowy(String kodPocztowy) {
        this.kodPocztowy = kodPocztowy;
    }

    public String getMiejscowosc() {
        return miejscowosc;
    }

    public void setMiejscowosc(String miejscowosc) {
        this.miejscowosc = miejscowosc;
    }

    public StatusCzlonkostwa getStatus() {
        return status;
    }

    public void setStatus(StatusCzlonkostwa status) {
        this.status = status;
    }

    public RolaWRodzinie getRolaWRodzinie() {
        return rolaWRodzinie;
    }

    public void setRolaWRodzinie(RolaWRodzinie rolaWRodzinie) {
        this.rolaWRodzinie = rolaWRodzinie;
    }

    public LocalDate getDataPrzyjecia() {
        return dataPrzyjecia;
    }

    public void setDataPrzyjecia(LocalDate dataPrzyjecia) {
        this.dataPrzyjecia = dataPrzyjecia;
    }

    public LocalDate getDataWystapienia() {
        return dataWystapienia;
    }

    public void setDataWystapienia(LocalDate dataWystapienia) {
        this.dataWystapienia = dataWystapienia;
    }

    public boolean isZgodaNaKontakt() {
        return zgodaNaKontakt;
    }

    public void setZgodaNaKontakt(boolean zgodaNaKontakt) {
        this.zgodaNaKontakt = zgodaNaKontakt;
    }

    public boolean isUczestniczyWWieczerzy() {
        return uczestniczyWWieczerzy;
    }

    public void setUczestniczyWWieczerzy(boolean uczestniczyWWieczerzy) {
        this.uczestniczyWWieczerzy = uczestniczyWWieczerzy;
    }

    public String getUwagi() {
        return uwagi;
    }

    public void setUwagi(String uwagi) {
        this.uwagi = uwagi;
    }

    public Gospodarstwo getGospodarstwo() {
        return gospodarstwo;
    }

    public void setGospodarstwo(Gospodarstwo gospodarstwo) {
        this.gospodarstwo = gospodarstwo;
    }
}
