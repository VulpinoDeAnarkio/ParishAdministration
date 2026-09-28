package pl.kea.parafia.duszpasterstwo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "sprawa_duszpasterska")
public class SprawaDuszpasterska extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_id", nullable = false)
    private Osoba osoba;

    @Column(nullable = false, length = 200)
    private String temat;

    @Column(length = 4000)
    private String opis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusSprawy status = StatusSprawy.OTWARTA;

    protected SprawaDuszpasterska() {
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public String getTemat() {
        return temat;
    }

    public void setTemat(String temat) {
        this.temat = temat;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public StatusSprawy getStatus() {
        return status;
    }

    public void setStatus(StatusSprawy status) {
        this.status = status;
    }
}
