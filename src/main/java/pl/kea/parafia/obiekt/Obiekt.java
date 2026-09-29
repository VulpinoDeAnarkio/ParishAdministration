package pl.kea.parafia.obiekt;

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
@Table(name = "obiekt")
public class Obiekt extends EncjaAudytowana {

    @Column(nullable = false, length = 160)
    private String nazwa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private RodzajObiektu rodzaj;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rodzic_id")
    private Obiekt rodzic;

    private Integer pojemnosc;

    @Column(length = 200)
    private String lokalizacja;

    @Column(length = 1000)
    private String opis;

    @Column(nullable = false)
    private boolean aktywny = true;

    protected Obiekt() {
    }

    public String getNazwaZRodzicem() {
        if (rodzic == null) {
            return nazwa;
        }
        return rodzic.getNazwa() + " · " + nazwa;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public RodzajObiektu getRodzaj() {
        return rodzaj;
    }

    public void setRodzaj(RodzajObiektu rodzaj) {
        this.rodzaj = rodzaj;
    }

    public Obiekt getRodzic() {
        return rodzic;
    }

    public void setRodzic(Obiekt rodzic) {
        this.rodzic = rodzic;
    }

    public Integer getPojemnosc() {
        return pojemnosc;
    }

    public void setPojemnosc(Integer pojemnosc) {
        this.pojemnosc = pojemnosc;
    }

    public String getLokalizacja() {
        return lokalizacja;
    }

    public void setLokalizacja(String lokalizacja) {
        this.lokalizacja = lokalizacja;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public boolean isAktywny() {
        return aktywny;
    }

    public void setAktywny(boolean aktywny) {
        this.aktywny = aktywny;
    }
}
