package pl.kea.parafia.bezpieczenstwo;

import java.util.Locale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.wspolne.NieZnalezionoException;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class UzytkownikService {

    private final UzytkownikRepository repozytorium;
    private final PasswordEncoder koder;
    private final BiezacyUzytkownik biezacy;

    public UzytkownikService(UzytkownikRepository repozytorium, PasswordEncoder koder, BiezacyUzytkownik biezacy) {
        this.repozytorium = repozytorium;
        this.koder = koder;
        this.biezacy = biezacy;
    }

    @Transactional(readOnly = true)
    public Page<Uzytkownik> lista(Pageable strona) {
        return repozytorium.findAll(strona);
    }

    @Transactional(readOnly = true)
    public Uzytkownik znajdz(Long id) {
        return repozytorium.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego konta."));
    }

    @Transactional
    public Uzytkownik utworz(UzytkownikFormularz formularz) {
        wymagajHasla(formularz.haslo());
        String email = formularz.email().toLowerCase(Locale.ROOT);
        if (repozytorium.existsByEmailIgnoreCase(email)) {
            throw new RegulaParafiiException("Konto z tym adresem już istnieje.");
        }
        return repozytorium.save(new Uzytkownik(
                email,
                koder.encode(formularz.haslo()),
                formularz.imie(),
                formularz.nazwisko(),
                formularz.rola(),
                formularz.aktywny()
        ));
    }

    @Transactional
    public Uzytkownik aktualizuj(Long id, UzytkownikFormularz formularz) {
        Uzytkownik uzytkownik = znajdz(id);
        String email = formularz.email().toLowerCase(Locale.ROOT);
        repozytorium.findByEmailIgnoreCase(email)
                .filter(inny -> !inny.getId().equals(id))
                .ifPresent(inny -> {
                    throw new RegulaParafiiException("Konto z tym adresem już istnieje.");
                });
        chronOstatniegoAdministratora(uzytkownik, formularz);
        uzytkownik.setEmail(email);
        uzytkownik.setImie(formularz.imie());
        uzytkownik.setNazwisko(formularz.nazwisko());
        uzytkownik.setRola(formularz.rola());
        uzytkownik.setAktywny(formularz.aktywny());
        if (formularz.haslo() != null && !formularz.haslo().isBlank()) {
            wymagajHasla(formularz.haslo());
            uzytkownik.setHaslo(koder.encode(formularz.haslo()));
        }
        return uzytkownik;
    }

    private void chronOstatniegoAdministratora(Uzytkownik uzytkownik, UzytkownikFormularz formularz) {
        boolean bylAktywnymAdminem = uzytkownik.getRola() == Rola.ADMIN && uzytkownik.isAktywny();
        boolean zostajeAktywnymAdminem = formularz.rola() == Rola.ADMIN && formularz.aktywny();
        if (bylAktywnymAdminem && !zostajeAktywnymAdminem && repozytorium.countByRolaAndAktywnyIsTrue(Rola.ADMIN) <= 1) {
            throw new RegulaParafiiException("W kancelarii musi zostać przynajmniej jeden aktywny administrator.");
        }
        biezacy.znajdz().ifPresent(ja -> {
            if (ja.getId().equals(uzytkownik.getId()) && !formularz.aktywny()) {
                throw new RegulaParafiiException("Nie wyłączysz własnego konta.");
            }
        });
    }

    private void wymagajHasla(String haslo) {
        if (haslo == null || haslo.length() < 8) {
            throw new RegulaParafiiException("Hasło musi mieć co najmniej 8 znaków.");
        }
    }
}
