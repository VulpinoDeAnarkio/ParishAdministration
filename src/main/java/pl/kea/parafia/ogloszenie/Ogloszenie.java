package pl.kea.parafia.ogloszenie;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "ogloszenie")
public class Ogloszenie extends EncjaAudytowana {

    @Column(nullable = false, length = 200)
    private String tytul;

    @Column(nullable = false, length = 4000)
    private String tresc;

    @Column(name = "obowiazuje_od", nullable = false)
    private LocalDate obowiazujeOd;

    @Column(name = "obowiazuje_do")
    private LocalDate obowiazujeDo;

    @Column(name = "widoczne_publicznie", nullable = false)
    private boolean widocznePublicznie;

    protected Ogloszenie() {
    }

    public boolean jestAktualne(LocalDate dzien) {
        if (obowiazujeOd.isAfter(dzien)) {
            return false;
        }
        return obowiazujeDo == null || !obowiazujeDo.isBefore(dzien);
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getTresc() {
        return tresc;
    }

    public void setTresc(String tresc) {
        this.tresc = tresc;
    }

    public LocalDate getObowiazujeOd() {
        return obowiazujeOd;
    }

    public void setObowiazujeOd(LocalDate obowiazujeOd) {
        this.obowiazujeOd = obowiazujeOd;
    }

    public LocalDate getObowiazujeDo() {
        return obowiazujeDo;
    }

    public void setObowiazujeDo(LocalDate obowiazujeDo) {
        this.obowiazujeDo = obowiazujeDo;
    }

    public boolean isWidocznePublicznie() {
        return widocznePublicznie;
    }

    public void setWidocznePublicznie(boolean widocznePublicznie) {
        this.widocznePublicznie = widocznePublicznie;
    }
}
