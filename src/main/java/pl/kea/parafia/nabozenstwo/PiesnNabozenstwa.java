package pl.kea.parafia.nabozenstwo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "piesn_nabozenstwa")
public class PiesnNabozenstwa extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nabozenstwo_id", nullable = false)
    private Nabozenstwo nabozenstwo;

    @Column(nullable = false)
    private int kolejnosc;

    @Column(name = "numer_spiewnika", length = 20)
    private String numerSpiewnika;

    @Column(nullable = false, length = 200)
    private String tytul;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private MomentPiesni moment;

    protected PiesnNabozenstwa() {
    }

    public Nabozenstwo getNabozenstwo() {
        return nabozenstwo;
    }

    public void setNabozenstwo(Nabozenstwo nabozenstwo) {
        this.nabozenstwo = nabozenstwo;
    }

    public int getKolejnosc() {
        return kolejnosc;
    }

    public void setKolejnosc(int kolejnosc) {
        this.kolejnosc = kolejnosc;
    }

    public String getNumerSpiewnika() {
        return numerSpiewnika;
    }

    public void setNumerSpiewnika(String numerSpiewnika) {
        this.numerSpiewnika = numerSpiewnika;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public MomentPiesni getMoment() {
        return moment;
    }

    public void setMoment(MomentPiesni moment) {
        this.moment = moment;
    }
}
