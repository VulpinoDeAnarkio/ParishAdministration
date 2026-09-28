package pl.kea.parafia.bezpieczenstwo;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class BiezacyUzytkownik {

    public Optional<Uzytkownik> znajdz() {
        Authentication uwierzytelnienie = SecurityContextHolder.getContext().getAuthentication();
        if (uwierzytelnienie != null && uwierzytelnienie.getPrincipal() instanceof KontoUzytkownika konto) {
            return Optional.of(konto.getUzytkownik());
        }
        return Optional.empty();
    }

    public boolean maRole(String... role) {
        Authentication uwierzytelnienie = SecurityContextHolder.getContext().getAuthentication();
        if (uwierzytelnienie == null) {
            return false;
        }
        for (String rola : role) {
            if (uwierzytelnienie.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + rola))) {
                return true;
            }
        }
        return false;
    }
}
