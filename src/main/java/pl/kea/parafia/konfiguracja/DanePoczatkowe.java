package pl.kea.parafia.konfiguracja;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.bezpieczenstwo.Rola;
import pl.kea.parafia.bezpieczenstwo.Uzytkownik;
import pl.kea.parafia.bezpieczenstwo.UzytkownikRepository;

@Component
@ConditionalOnProperty(name = "parafia.demo", havingValue = "true")
public class DanePoczatkowe implements ApplicationRunner {

    public static final String HASLO_DEMO = "poznan#2026";
    private static final String POPRZEDNIE_HASLO_DEMO = "Lipowa#2026";

    private final UzytkownikRepository uzytkownicy;
    private final PasswordEncoder koder;

    public DanePoczatkowe(UzytkownikRepository uzytkownicy, PasswordEncoder koder) {
        this.uzytkownicy = uzytkownicy;
        this.koder = koder;
    }

    @Override
    @Transactional
    public void run(org.springframework.boot.ApplicationArguments args) {
        if (uzytkownicy.count() > 0) {
            przeniesHaslaZLipowej();
            return;
        }
        utworz("admin@poznan.parafia.pl", "Ewa", "Król", Rola.ADMIN);
        utworz("proboszcz@poznan.parafia.pl", "Marek", "Lis", Rola.PROBOSZCZ);
        utworz("biuro@poznan.parafia.pl", "Hanna", "Weber", Rola.SEKRETARIAT);
        utworz("skarbnik@poznan.parafia.pl", "Tomasz", "Berger", Rola.SKARBNIK);
        utworz("duszpasterz@poznan.parafia.pl", "Anna", "Maj", Rola.DUSZPASTERZ);
    }

    private void przeniesHaslaZLipowej() {
        for (Uzytkownik konto : uzytkownicy.findAll()) {
            if (koder.matches(POPRZEDNIE_HASLO_DEMO, konto.getHaslo())) {
                konto.setHaslo(koder.encode(HASLO_DEMO));
                uzytkownicy.save(konto);
            }
        }
    }

    private void utworz(String email, String imie, String nazwisko, Rola rola) {
        uzytkownicy.save(new Uzytkownik(email, koder.encode(HASLO_DEMO), imie, nazwisko, rola, true));
    }
}
