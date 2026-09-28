package pl.kea.parafia.duszpasterstwo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "wizyta")
public class Wizyta extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprawa_id")
    private SprawaDuszpasterska sprawa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_id", nullable = false)
    private Osoba osoba;

    @Column(name = "data_wizyty", nullable = false)
    private LocalDate dataWizyty;

    @Column(length = 160)
    private String miejsce;

    @Column(length = 4000)
    private String notatka;

    @Column(name = "nastepny_kontakt")
    private LocalDate nastepnyKontakt;

    protected Wizyta() {
    }

    public SprawaDuszpasterska getSprawa() {
        return sprawa;
    }

    public void setSprawa(SprawaDuszpasterska sprawa) {
        this.sprawa = sprawa;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public LocalDate getDataWizyty() {
        return dataWizyty;
    }

    public void setDataWizyty(LocalDate dataWizyty) {
        this.dataWizyty = dataWizyty;
    }

    public String getMiejsce() {
        return miejsce;
    }

    public void setMiejsce(String miejsce) {
        this.miejsce = miejsce;
    }

    public String getNotatka() {
        return notatka;
    }

    public void setNotatka(String notatka) {
        this.notatka = notatka;
    }

    public LocalDate getNastepnyKontakt() {
        return nastepnyKontakt;
    }

    public void setNastepnyKontakt(LocalDate nastepnyKontakt) {
        this.nastepnyKontakt = nastepnyKontakt;
    }
}
