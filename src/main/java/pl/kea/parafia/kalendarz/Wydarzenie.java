package pl.kea.parafia.kalendarz;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "wydarzenie")
public class Wydarzenie extends EncjaAudytowana {

    @Column(nullable = false, length = 200)
    private String tytul;

    @Column(length = 2000)
    private String opis;

    @Column(nullable = false)
    private LocalDateTime poczatek;

    @Column(nullable = false)
    private LocalDateTime koniec;

    @Column(length = 200)
    private String miejsce;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id")
    private Sala sala;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private RodzajWydarzenia rodzaj;

    @Column(name = "widoczne_publicznie", nullable = false)
    private boolean widocznePublicznie;

    @Column(length = 160)
    private String organizator;

    protected Wydarzenie() {
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public LocalDateTime getPoczatek() {
        return poczatek;
    }

    public void setPoczatek(LocalDateTime poczatek) {
        this.poczatek = poczatek;
    }

    public LocalDateTime getKoniec() {
        return koniec;
    }

    public void setKoniec(LocalDateTime koniec) {
        this.koniec = koniec;
    }

    public String getMiejsce() {
        return miejsce;
    }

    public void setMiejsce(String miejsce) {
        this.miejsce = miejsce;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public RodzajWydarzenia getRodzaj() {
        return rodzaj;
    }

    public void setRodzaj(RodzajWydarzenia rodzaj) {
        this.rodzaj = rodzaj;
    }

    public boolean isWidocznePublicznie() {
        return widocznePublicznie;
    }

    public void setWidocznePublicznie(boolean widocznePublicznie) {
        this.widocznePublicznie = widocznePublicznie;
    }

    public String getOrganizator() {
        return organizator;
    }

    public void setOrganizator(String organizator) {
        this.organizator = organizator;
    }
}
