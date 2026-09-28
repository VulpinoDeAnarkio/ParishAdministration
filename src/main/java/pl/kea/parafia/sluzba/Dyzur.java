package pl.kea.parafia.sluzba;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import pl.kea.parafia.nabozenstwo.Nabozenstwo;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(
        name = "dyzur",
        uniqueConstraints = @UniqueConstraint(name = "uq_dyzur", columnNames = {"nabozenstwo_id", "osoba_id", "rola"})
)
public class Dyzur extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nabozenstwo_id", nullable = false)
    private Nabozenstwo nabozenstwo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_id", nullable = false)
    private Osoba osoba;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private RolaSluzby rola;

    protected Dyzur() {
    }

    public Nabozenstwo getNabozenstwo() {
        return nabozenstwo;
    }

    public void setNabozenstwo(Nabozenstwo nabozenstwo) {
        this.nabozenstwo = nabozenstwo;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public RolaSluzby getRola() {
        return rola;
    }

    public void setRola(RolaSluzby rola) {
        this.rola = rola;
    }
}
