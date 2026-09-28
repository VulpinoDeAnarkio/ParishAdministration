package pl.kea.parafia.kalendarz;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "sala")
public class Sala extends EncjaAudytowana {

    @Column(nullable = false, length = 160)
    private String nazwa;

    private Integer pojemnosc;

    @Column(length = 500)
    private String opis;

    @Column(nullable = false)
    private boolean aktywna = true;

    protected Sala() {
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Integer getPojemnosc() {
        return pojemnosc;
    }

    public void setPojemnosc(Integer pojemnosc) {
        this.pojemnosc = pojemnosc;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public boolean isAktywna() {
        return aktywna;
    }

    public void setAktywna(boolean aktywna) {
        this.aktywna = aktywna;
    }
}
