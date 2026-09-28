package pl.kea.parafia.parafia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pl.kea.parafia.wspolne.EncjaAudytowana;

@Entity
@Table(name = "profil_parafii")
public class ProfilParafii extends EncjaAudytowana {

    @Column(nullable = false, length = 200)
    private String nazwa;

    @Column(nullable = false, length = 160)
    private String diecezja;

    private String ulica;
    private String kodPocztowy;
    private String miejscowosc;
    private String telefon;
    private String email;
    private String www;
    private String proboszcz;
    private String kurator;
    private String poraNabozenstwa;

    @Column(length = 2000)
    private String opis;

    protected ProfilParafii() {
    }

    public static ProfilParafii domyslny() {
        ProfilParafii profil = new ProfilParafii();
        profil.nazwa = "Parafia Ewangelicko-Augsburska Świętej Trójcy w Lipowej";
        profil.diecezja = "Diecezja warszawska";
        profil.miejscowosc = "Lipowa";
        profil.opis = "Parafia fikcyjna na potrzeby kancelarii.";
        return profil;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getDiecezja() {
        return diecezja;
    }

    public void setDiecezja(String diecezja) {
        this.diecezja = diecezja;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getProboszcz() {
        return proboszcz;
    }

    public void setProboszcz(String proboszcz) {
        this.proboszcz = proboszcz;
    }

    public String getKurator() {
        return kurator;
    }

    public void setKurator(String kurator) {
        this.kurator = kurator;
    }

    public String getPoraNabozenstwa() {
        return poraNabozenstwa;
    }

    public void setPoraNabozenstwa(String poraNabozenstwa) {
        this.poraNabozenstwa = poraNabozenstwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getAdresJednaLinia() {
        StringBuilder adres = new StringBuilder();
        if (ulica != null && !ulica.isBlank()) {
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
