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
@Table(name = "przeniesienie")
public class Przeniesienie extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "osoba_id", nullable = false)
    private Osoba osoba;

    @Column(name = "data_czynnosci", nullable = false)
    private LocalDate dataCzynnosci;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private KierunekPrzeniesienia kierunek;

    @Column(nullable = false, length = 200)
    private String parafia;

    @Column(name = "numer_ksiegi", nullable = false, length = 40)
    private String numerKsiegi;

    @Column(length = 1000)
    private String uwagi;

    protected Przeniesienie() {
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public LocalDate getDataCzynnosci() {
        return dataCzynnosci;
    }

    public void setDataCzynnosci(LocalDate dataCzynnosci) {
        this.dataCzynnosci = dataCzynnosci;
    }

    public KierunekPrzeniesienia getKierunek() {
        return kierunek;
    }

    public void setKierunek(KierunekPrzeniesienia kierunek) {
        this.kierunek = kierunek;
    }

    public String getParafia() {
        return parafia;
    }

    public void setParafia(String parafia) {
        this.parafia = parafia;
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
