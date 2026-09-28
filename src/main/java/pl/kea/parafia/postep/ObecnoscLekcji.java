package pl.kea.parafia.postep;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(
        name = "obecnosc_lekcji",
        uniqueConstraints = @UniqueConstraint(name = "uq_obecnosc_lekcji", columnNames = {"lekcja_id", "osoba_id"})
)
public class ObecnoscLekcji extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lekcja_id", nullable = false)
    private Lekcja lekcja;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_id", nullable = false)
    private Osoba osoba;

    @Column(nullable = false)
    private boolean obecny;

    protected ObecnoscLekcji() {
    }

    public Lekcja getLekcja() {
        return lekcja;
    }

    public void setLekcja(Lekcja lekcja) {
        this.lekcja = lekcja;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public boolean isObecny() {
        return obecny;
    }

    public void setObecny(boolean obecny) {
        this.obecny = obecny;
    }
}
