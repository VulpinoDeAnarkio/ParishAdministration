package pl.kea.parafia.finanse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "operacja")
public class Operacja extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fundusz_id", nullable = false)
    private Fundusz fundusz;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private RodzajOperacji rodzaj;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private KierunekOperacji kierunek;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal kwota;

    @Column(name = "data_operacji", nullable = false)
    private LocalDate dataOperacji;

    @Column(length = 500)
    private String opis;

    @Column(length = 160)
    private String kontrahent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "osoba_id")
    private Osoba osoba;

    protected Operacja() {
    }

    public Fundusz getFundusz() {
        return fundusz;
    }

    public void setFundusz(Fundusz fundusz) {
        this.fundusz = fundusz;
    }

    public RodzajOperacji getRodzaj() {
        return rodzaj;
    }

    public void setRodzaj(RodzajOperacji rodzaj) {
        this.rodzaj = rodzaj;
    }

    public KierunekOperacji getKierunek() {
        return kierunek;
    }

    public void setKierunek(KierunekOperacji kierunek) {
        this.kierunek = kierunek;
    }

    public BigDecimal getKwota() {
        return kwota;
    }

    public void setKwota(BigDecimal kwota) {
        this.kwota = kwota;
    }

    public LocalDate getDataOperacji() {
        return dataOperacji;
    }

    public void setDataOperacji(LocalDate dataOperacji) {
        this.dataOperacji = dataOperacji;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getKontrahent() {
        return kontrahent;
    }

    public void setKontrahent(String kontrahent) {
        this.kontrahent = kontrahent;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }
}
