package pl.kea.parafia.osoba;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "gospodarstwo")
public class Gospodarstwo extends EncjaAudytowana {

    @Column(nullable = false, length = 160)
    private String nazwa;

    private String ulica;
    private String kodPocztowy;
    private String miejscowosc;
    private String telefon;

    @Column(length = 1000)
    private String uwagi;

    protected Gospodarstwo() {
    }

    public Gospodarstwo(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getKodPocztowy() {
        return kodPocztowy;
    }

    public void setKodPocztowy(String kodPocztowy) {
        this.kodPocztowy = kodPocztowy;
    }

    public String getMiejscowosc() {
        return miejscowosc;
    }

    public void setMiejscowosc(String miejscowosc) {
        this.miejscowosc = miejscowosc;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getUwagi() {
        return uwagi;
    }

    public void setUwagi(String uwagi) {
        this.uwagi = uwagi;
    }

    public String getAdresJednaLinia() {
        StringBuilder adres = new StringBuilder();
        if (ulica != null) {
            adres.append(ulica);
        }
        if (kodPocztowy != null || miejscowosc != null) {
            if (!adres.isEmpty()) {
                adres.append(", ");
            }
            if (kodPocztowy != null) {
                adres.append(kodPocztowy).append(' ');
            }
            if (miejscowosc != null) {
                adres.append(miejscowosc);
            }
        }
        return adres.toString().trim();
    }
}
