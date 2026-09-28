package pl.kea.parafia.grupa;

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
@Table(name = "grupa")
public class Grupa extends EncjaAudytowana {

    @Column(nullable = false, length = 160)
    private String nazwa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private RodzajGrupy rodzaj;

    @Column(length = 2000)
    private String opis;

    @Column(length = 160)
    private String termin;

    @Column(length = 160)
    private String miejsce;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lider_id")
    private Osoba lider;

    @Column(nullable = false)
    private boolean aktywna = true;

    protected Grupa() {
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public RodzajGrupy getRodzaj() {
        return rodzaj;
    }

    public void setRodzaj(RodzajGrupy rodzaj) {
        this.rodzaj = rodzaj;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getTermin() {
        return termin;
    }

    public void setTermin(String termin) {
        this.termin = termin;
    }

    public String getMiejsce() {
        return miejsce;
    }

    public void setMiejsce(String miejsce) {
        this.miejsce = miejsce;
    }

    public Osoba getLider() {
        return lider;
    }

    public void setLider(Osoba lider) {
        this.lider = lider;
    }

    public boolean isAktywna() {
        return aktywna;
    }

    public void setAktywna(boolean aktywna) {
        this.aktywna = aktywna;
    }
}
