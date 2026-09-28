package pl.kea.parafia.ksiega;

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
@Table(name = "konfirmacja")
public class Konfirmacja extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_id", nullable = false)
    private Osoba osoba;

    @Column(name = "data_konfirmacji", nullable = false)
    private LocalDate dataKonfirmacji;

    @Column(nullable = false, length = 200)
    private String miejsce;

    @Column(nullable = false, length = 160)
    private String duchowny;

    @Column(length = 40)
    private String rocznik;

    @Column(length = 300)
    private String werset;

    @Column(name = "numer_ksiegi", nullable = false, length = 40)
    private String numerKsiegi;

    @Column(length = 1000)
    private String uwagi;

    protected Konfirmacja() {
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public LocalDate getDataKonfirmacji() {
        return dataKonfirmacji;
    }

    public void setDataKonfirmacji(LocalDate dataKonfirmacji) {
        this.dataKonfirmacji = dataKonfirmacji;
    }

    public String getMiejsce() {
        return miejsce;
    }

    public void setMiejsce(String miejsce) {
        this.miejsce = miejsce;
    }

    public String getDuchowny() {
        return duchowny;
    }

    public void setDuchowny(String duchowny) {
        this.duchowny = duchowny;
    }

    public String getRocznik() {
        return rocznik;
    }

    public void setRocznik(String rocznik) {
        this.rocznik = rocznik;
    }

    public String getWerset() {
        return werset;
    }

    public void setWerset(String werset) {
        this.werset = werset;
    }

    public String getNumerKsiegi() {
        return numerKsiegi;
    }

    public void setNumerKsiegi(String numerKsiegi) {
        this.numerKsiegi = numerKsiegi;
    }

    public String getUwagi() {
        return uwagi;
    }

    public void setUwagi(String uwagi) {
        this.uwagi = uwagi;
    }
}
