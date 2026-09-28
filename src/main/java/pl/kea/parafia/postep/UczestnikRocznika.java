package pl.kea.parafia.postep;

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
        name = "uczestnik_rocznika",
        uniqueConstraints = @UniqueConstraint(name = "uq_uczestnik_rocznika", columnNames = {"rocznik_id", "osoba_id"})
)
public class UczestnikRocznika extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rocznik_id", nullable = false)
    private Rocznik rocznik;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_id", nullable = false)
    private Osoba osoba;

    protected UczestnikRocznika() {
    }

    public Rocznik getRocznik() {
        return rocznik;
    }

    public void setRocznik(Rocznik rocznik) {
        this.rocznik = rocznik;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }
}
