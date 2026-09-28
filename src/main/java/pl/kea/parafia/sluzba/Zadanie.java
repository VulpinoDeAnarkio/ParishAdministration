package pl.kea.parafia.sluzba;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import pl.kea.parafia.kalendarz.Wydarzenie;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "zadanie")
public class Zadanie extends EncjaAudytowana {

    @Column(nullable = false, length = 200)
    private String tytul;

    @Column(length = 2000)
    private String opis;

    private LocalDate termin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusZadania status = StatusZadania.DO_ZROBIENIA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "osoba_id")
    private Osoba osoba;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wydarzenie_id")
    private Wydarzenie wydarzenie;

    protected Zadanie() {
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public LocalDate getTermin() {
        return termin;
    }

    public void setTermin(LocalDate termin) {
        this.termin = termin;
    }

    public StatusZadania getStatus() {
        return status;
    }

    public void setStatus(StatusZadania status) {
        this.status = status;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public Wydarzenie getWydarzenie() {
        return wydarzenie;
    }

    public void setWydarzenie(Wydarzenie wydarzenie) {
        this.wydarzenie = wydarzenie;
    }
}
