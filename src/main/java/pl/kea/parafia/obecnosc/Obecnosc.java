package pl.kea.parafia.obecnosc;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(
        name = "obecnosc",
        uniqueConstraints = @UniqueConstraint(name = "uq_obecnosc", columnNames = {"rodzaj", "cel_id", "osoba_id"})
)
public class Obecnosc extends EncjaAudytowana {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RodzajObecnosci rodzaj;

    @Column(name = "cel_id", nullable = false)
    private Long celId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_id", nullable = false)
    private Osoba osoba;

    @Column(nullable = false)
    private boolean obecny;

    @Column(length = 300)
    private String uwagi;

    protected Obecnosc() {
    }

    public RodzajObecnosci getRodzaj() {
        return rodzaj;
    }

    public void setRodzaj(RodzajObecnosci rodzaj) {
        this.rodzaj = rodzaj;
    }

    public Long getCelId() {
        return celId;
    }

    public void setCelId(Long celId) {
        this.celId = celId;
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

    public String getUwagi() {
        return uwagi;
    }

    public void setUwagi(String uwagi) {
        this.uwagi = uwagi;
    }
}
