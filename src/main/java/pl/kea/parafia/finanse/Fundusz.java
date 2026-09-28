package pl.kea.parafia.finanse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "fundusz")
public class Fundusz extends EncjaAudytowana {

    @Column(nullable = false, length = 160)
    private String nazwa;

    @Column(length = 500)
    private String opis;

    @Column(nullable = false)
    private boolean aktywny = true;

    protected Fundusz() {
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

    public boolean isAktywny() {
        return aktywny;
    }

    public void setAktywny(boolean aktywny) {
        this.aktywny = aktywny;
    }
}
