package pl.kea.parafia.postep;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "lekcja")
public class Lekcja extends EncjaAudytowana {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rocznik_id", nullable = false)
    private Rocznik rocznik;

    @Column(name = "data_lekcji", nullable = false)
    private LocalDate dataLekcji;

    @Column(nullable = false, length = 200)
    private String temat;

    protected Lekcja() {
    }

    public Rocznik getRocznik() {
        return rocznik;
    }

    public void setRocznik(Rocznik rocznik) {
        this.rocznik = rocznik;
    }

    public LocalDate getDataLekcji() {
        return dataLekcji;
    }

    public void setDataLekcji(LocalDate dataLekcji) {
        this.dataLekcji = dataLekcji;
    }

    public String getTemat() {
        return temat;
    }

    public void setTemat(String temat) {
        this.temat = temat;
    }
}
