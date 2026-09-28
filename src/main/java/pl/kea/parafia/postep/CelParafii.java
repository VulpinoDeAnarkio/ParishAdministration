package pl.kea.parafia.postep;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "cel_parafii")
public class CelParafii extends EncjaAudytowana {

    @Column(nullable = false, length = 200)
    private String nazwa;

    @Column(length = 1000)
    private String opis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private ZrodloCelu zrodlo;

    @Column(name = "wartosc_docelowa", nullable = false, precision = 12, scale = 2)
    private BigDecimal wartoscDocelowa;

    @Column(name = "wartosc_reczna", precision = 12, scale = 2)
    private BigDecimal wartoscReczna;

    @Column(nullable = false)
    private int rok;

    @Column(nullable = false)
    private boolean aktywny = true;

    protected CelParafii() {
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public ZrodloCelu getZrodlo() {
        return zrodlo;
    }

    public void setZrodlo(ZrodloCelu zrodlo) {
        this.zrodlo = zrodlo;
    }

    public BigDecimal getWartoscDocelowa() {
        return wartoscDocelowa;
    }

    public void setWartoscDocelowa(BigDecimal wartoscDocelowa) {
        this.wartoscDocelowa = wartoscDocelowa;
    }

    public BigDecimal getWartoscReczna() {
        return wartoscReczna;
    }

    public void setWartoscReczna(BigDecimal wartoscReczna) {
        this.wartoscReczna = wartoscReczna;
    }

    public int getRok() {
        return rok;
    }

    public void setRok(int rok) {
        this.rok = rok;
    }

    public boolean isAktywny() {
        return aktywny;
    }

    public void setAktywny(boolean aktywny) {
        this.aktywny = aktywny;
    }
}
