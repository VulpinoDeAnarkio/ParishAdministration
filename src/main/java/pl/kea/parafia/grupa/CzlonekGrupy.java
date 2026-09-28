package pl.kea.parafia.grupa;

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
@Table(name = "czlonek_grupy")
public class CzlonekGrupy extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupa_id", nullable = false)
    private Grupa grupa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_id", nullable = false)
    private Osoba osoba;

    @Column(length = 80)
    private String funkcja;

    private LocalDate odDnia;
    private LocalDate doDnia;

    protected CzlonekGrupy() {
    }

    public boolean jestAktywny() {
        return doDnia == null || doDnia.isAfter(LocalDate.now());
    }

    public Grupa getGrupa() {
        return grupa;
    }

    public void setGrupa(Grupa grupa) {
        this.grupa = grupa;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public String getFunkcja() {
        return funkcja;
    }

    public void setFunkcja(String funkcja) {
        this.funkcja = funkcja;
    }

    public LocalDate getOdDnia() {
        return odDnia;
    }

    public void setOdDnia(LocalDate odDnia) {
        this.odDnia = odDnia;
    }

    public LocalDate getDoDnia() {
        return doDnia;
    }

    public void setDoDnia(LocalDate doDnia) {
        this.doDnia = doDnia;
    }
}
