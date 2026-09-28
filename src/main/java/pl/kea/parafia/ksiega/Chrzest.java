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
@Table(name = "chrzest")
public class Chrzest extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_id", nullable = false)
    private Osoba osoba;

    @Column(name = "data_chrztu", nullable = false)
    private LocalDate dataChrztu;

    @Column(nullable = false, length = 200)
    private String miejsce;

    @Column(nullable = false, length = 160)
    private String duchowny;

    @Column(name = "rodzic_1", length = 160)
    private String rodzicPierwszy;

    @Column(name = "rodzic_2", length = 160)
    private String rodzicDrugi;

    @Column(name = "chrzestny_1", length = 160)
    private String chrzestnyPierwszy;

    @Column(name = "chrzestny_2", length = 160)
    private String chrzestnyDrugi;

    @Column(name = "numer_ksiegi", nullable = false, length = 40)
    private String numerKsiegi;

    @Column(length = 1000)
    private String uwagi;

    protected Chrzest() {
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public LocalDate getDataChrztu() {
        return dataChrztu;
    }

    public void setDataChrztu(LocalDate dataChrztu) {
        this.dataChrztu = dataChrztu;
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

    public String getRodzicPierwszy() {
        return rodzicPierwszy;
    }

    public void setRodzicPierwszy(String rodzicPierwszy) {
        this.rodzicPierwszy = rodzicPierwszy;
    }

    public String getRodzicDrugi() {
        return rodzicDrugi;
    }

    public void setRodzicDrugi(String rodzicDrugi) {
        this.rodzicDrugi = rodzicDrugi;
    }

    public String getChrzestnyPierwszy() {
        return chrzestnyPierwszy;
    }

    public void setChrzestnyPierwszy(String chrzestnyPierwszy) {
        this.chrzestnyPierwszy = chrzestnyPierwszy;
    }

    public String getChrzestnyDrugi() {
        return chrzestnyDrugi;
    }

    public void setChrzestnyDrugi(String chrzestnyDrugi) {
        this.chrzestnyDrugi = chrzestnyDrugi;
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
