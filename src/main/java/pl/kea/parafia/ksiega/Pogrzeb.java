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
@Table(name = "pogrzeb")
public class Pogrzeb extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_id", nullable = false)
    private Osoba osoba;

    @Column(name = "data_zgonu", nullable = false)
    private LocalDate dataZgonu;

    @Column(name = "data_pogrzebu")
    private LocalDate dataPogrzebu;

    @Column(nullable = false, length = 200)
    private String miejsce;

    @Column(length = 200)
    private String cmentarz;

    @Column(nullable = false, length = 160)
    private String duchowny;

    @Column(name = "numer_ksiegi", nullable = false, length = 40)
    private String numerKsiegi;

    @Column(length = 1000)
    private String uwagi;

    protected Pogrzeb() {
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public LocalDate getDataZgonu() {
        return dataZgonu;
    }

    public void setDataZgonu(LocalDate dataZgonu) {
        this.dataZgonu = dataZgonu;
    }

    public LocalDate getDataPogrzebu() {
        return dataPogrzebu;
    }

    public void setDataPogrzebu(LocalDate dataPogrzebu) {
        this.dataPogrzebu = dataPogrzebu;
    }

    public String getMiejsce() {
        return miejsce;
    }

    public void setMiejsce(String miejsce) {
        this.miejsce = miejsce;
    }

    public String getCmentarz() {
        return cmentarz;
    }

    public void setCmentarz(String cmentarz) {
        this.cmentarz = cmentarz;
    }

    public String getDuchowny() {
        return duchowny;
    }

    public void setDuchowny(String duchowny) {
        this.duchowny = duchowny;
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
