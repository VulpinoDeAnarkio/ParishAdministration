package pl.kea.parafia.bezpieczenstwo;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class KontoUzytkownika implements UserDetails {

    private final Uzytkownik uzytkownik;

    public KontoUzytkownika(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
    }

    public Uzytkownik getUzytkownik() {
        return uzytkownik;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + uzytkownik.getRola().name()));
    }

    @Override
    public String getPassword() {
        return uzytkownik.getHaslo();
    }

    @Override
    public String getUsername() {
        return uzytkownik.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return uzytkownik.isAktywny();
    }
}
