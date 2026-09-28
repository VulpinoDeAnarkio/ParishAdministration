package pl.kea.parafia.nabozenstwo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import pl.kea.parafia.kalendarz.Wydarzenie;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "nabozenstwo")
public class Nabozenstwo extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wydarzenie_id", nullable = false, unique = true)
    private Wydarzenie wydarzenie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private RodzajNabozenstwa rodzaj;

    @Column(name = "niedziela_roku", length = 160)
    private String niedzielaRoku;

    @Column(length = 160)
    private String kaznodzieja;

    @Column(name = "czytanie_starego", length = 200)
    private String czytanieStarego;

    @Column(length = 200)
    private String psalm;

    @Column(name = "czytanie_apostolskie", length = 200)
    private String czytanieApostolskie;

    @Column(length = 200)
    private String ewangelia;

    @Column(name = "tytul_kazania", length = 200)
    private String tytulKazania;

    @Column(name = "werset_kazania", length = 200)
    private String wersetKazania;

    @Column(name = "wieczerza_panska", nullable = false)
    private boolean wieczerzaPanska = true;

    @Column(name = "uwagi_liturgiczne", length = 2000)
    private String uwagiLiturgiczne;

    @OneToMany(mappedBy = "nabozenstwo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("kolejnosc")
    private List<PiesnNabozenstwa> piesni = new ArrayList<>();

    protected Nabozenstwo() {
    }

    public Wydarzenie getWydarzenie() {
        return wydarzenie;
    }

    public void setWydarzenie(Wydarzenie wydarzenie) {
        this.wydarzenie = wydarzenie;
    }

    public RodzajNabozenstwa getRodzaj() {
        return rodzaj;
    }

    public void setRodzaj(RodzajNabozenstwa rodzaj) {
        this.rodzaj = rodzaj;
    }

    public String getNiedzielaRoku() {
        return niedzielaRoku;
    }

    public void setNiedzielaRoku(String niedzielaRoku) {
        this.niedzielaRoku = niedzielaRoku;
    }

    public String getKaznodzieja() {
        return kaznodzieja;
    }

    public void setKaznodzieja(String kaznodzieja) {
        this.kaznodzieja = kaznodzieja;
    }

    public String getCzytanieStarego() {
        return czytanieStarego;
    }

    public void setCzytanieStarego(String czytanieStarego) {
        this.czytanieStarego = czytanieStarego;
    }

    public String getPsalm() {
        return psalm;
    }

    public void setPsalm(String psalm) {
        this.psalm = psalm;
    }

    public String getCzytanieApostolskie() {
        return czytanieApostolskie;
    }

    public void setCzytanieApostolskie(String czytanieApostolskie) {
        this.czytanieApostolskie = czytanieApostolskie;
    }

    public String getEwangelia() {
        return ewangelia;
    }

    public void setEwangelia(String ewangelia) {
        this.ewangelia = ewangelia;
    }

    public String getTytulKazania() {
        return tytulKazania;
    }

    public void setTytulKazania(String tytulKazania) {
        this.tytulKazania = tytulKazania;
    }

    public String getWersetKazania() {
        return wersetKazania;
    }

    public void setWersetKazania(String wersetKazania) {
        this.wersetKazania = wersetKazania;
    }

    public boolean isWieczerzaPanska() {
        return wieczerzaPanska;
    }

    public void setWieczerzaPanska(boolean wieczerzaPanska) {
        this.wieczerzaPanska = wieczerzaPanska;
    }

    public String getUwagiLiturgiczne() {
        return uwagiLiturgiczne;
    }

    public void setUwagiLiturgiczne(String uwagiLiturgiczne) {
        this.uwagiLiturgiczne = uwagiLiturgiczne;
    }

    public List<PiesnNabozenstwa> getPiesni() {
        return piesni;
    }
}
