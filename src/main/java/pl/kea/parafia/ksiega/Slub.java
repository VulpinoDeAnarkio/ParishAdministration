package pl.kea.parafia.ksiega;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "slub")
public class Slub extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_a_id", nullable = false)
    private Osoba osobaA;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_b_id", nullable = false)
    private Osoba osobaB;

    @Column(name = "data_slubu", nullable = false)
    private LocalDate dataSlubu;

    @Column(nullable = false, length = 200)
    private String miejsce;

    @Column(nullable = false, length = 160)
    private String duchowny;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RodzajSlubu rodzaj;

    @Column(name = "swiadek_1", length = 160)
    private String swiadekPierwszy;

    @Column(name = "swiadek_2", length = 160)
    private String swiadekDrugi;

    @Column(name = "numer_ksiegi", nullable = false, length = 40)
    private String numerKsiegi;

    @Column(length = 1000)
    private String uwagi;

    protected Slub() {
    }

    public Osoba getOsobaA() {
        return osobaA;
    }

    public void setOsobaA(Osoba osobaA) {
        this.osobaA = osobaA;
    }

    public Osoba getOsobaB() {
        return osobaB;
    }

    public void setOsobaB(Osoba osobaB) {
        this.osobaB = osobaB;
    }

    public LocalDate getDataSlubu() {
        return dataSlubu;
    }

    public void setDataSlubu(LocalDate dataSlubu) {
        this.dataSlubu = dataSlubu;
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

    public RodzajSlubu getRodzaj() {
        return rodzaj;
    }

    public void setRodzaj(RodzajSlubu rodzaj) {
        this.rodzaj = rodzaj;
    }

    public String getSwiadekPierwszy() {
        return swiadekPierwszy;
    }

    public void setSwiadekPierwszy(String swiadekPierwszy) {
        this.swiadekPierwszy = swiadekPierwszy;
    }

    public String getSwiadekDrugi() {
        return swiadekDrugi;
    }

    public void setSwiadekDrugi(String swiadekDrugi) {
        this.swiadekDrugi = swiadekDrugi;
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
