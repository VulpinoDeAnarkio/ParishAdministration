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

    public static final String HASLO_DEMO = "Lipowa#2026";

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
            return;
        }
        utworz("admin@lipowa.parafia.pl", "Ewa", "Król", Rola.ADMIN);
        utworz("proboszcz@lipowa.parafia.pl", "Marek", "Lis", Rola.PROBOSZCZ);
        utworz("biuro@lipowa.parafia.pl", "Hanna", "Weber", Rola.SEKRETARIAT);
        utworz("skarbnik@lipowa.parafia.pl", "Tomasz", "Berger", Rola.SKARBNIK);
        utworz("duszpasterz@lipowa.parafia.pl", "Anna", "Maj", Rola.DUSZPASTERZ);
    }

    private void utworz(String email, String imie, String nazwisko, Rola rola) {
        uzytkownicy.save(new Uzytkownik(email, koder.encode(HASLO_DEMO), imie, nazwisko, rola, true));
    }
}
