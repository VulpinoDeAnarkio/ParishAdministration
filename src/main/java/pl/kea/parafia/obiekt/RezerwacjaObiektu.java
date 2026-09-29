package pl.kea.parafia.obiekt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "rezerwacja_obiektu")
public class RezerwacjaObiektu extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "obiekt_id", nullable = false)
    private Obiekt obiekt;

    @Column(nullable = false)
    private LocalDateTime poczatek;

    @Column(nullable = false)
    private LocalDateTime koniec;

    @Column(nullable = false, length = 160)
    private String rezerwujacy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "osoba_id")
    private Osoba osoba;

    @Column(length = 200)
    private String cel;

    @Column(name = "liczba_osob")
    private Integer liczbaOsob;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusRezerwacji status;

    @Column(length = 1000)
    private String uwagi;

    protected RezerwacjaObiektu() {
    }

    public Obiekt getObiekt() {
        return obiekt;
    }

    public void setObiekt(Obiekt obiekt) {
        this.obiekt = obiekt;
    }

    public LocalDateTime getPoczatek() {
        return poczatek;
    }

    public void setPoczatek(LocalDateTime poczatek) {
        this.poczatek = poczatek;
    }

    public LocalDateTime getKoniec() {
        return koniec;
    }

    public void setKoniec(LocalDateTime koniec) {
        this.koniec = koniec;
    }

    public String getRezerwujacy() {
        return rezerwujacy;
    }

    public void setRezerwujacy(String rezerwujacy) {
        this.rezerwujacy = rezerwujacy;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    public Integer getLiczbaOsob() {
        return liczbaOsob;
    }

    public void setLiczbaOsob(Integer liczbaOsob) {
        this.liczbaOsob = liczbaOsob;
    }

    public StatusRezerwacji getStatus() {
        return status;
    }

    public void setStatus(StatusRezerwacji status) {
        this.status = status;
    }

    public String getUwagi() {
        return uwagi;
    }

    public void setUwagi(String uwagi) {
        this.uwagi = uwagi;
    }
}
