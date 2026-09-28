package pl.kea.parafia.postep;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "rocznik_konfirmacyjny")
public class Rocznik extends EncjaAudytowana {

    @Column(nullable = false, length = 160)
    private String nazwa;

    @Column(nullable = false)
    private int rok;

    @Column(name = "data_konfirmacji")
    private LocalDate dataKonfirmacji;

    protected Rocznik() {
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getRok() {
        return rok;
    }

    public void setRok(int rok) {
        this.rok = rok;
    }

    public LocalDate getDataKonfirmacji() {
        return dataKonfirmacji;
    }

    public void setDataKonfirmacji(LocalDate dataKonfirmacji) {
        this.dataKonfirmacji = dataKonfirmacji;
    }
}
