package pl.kea.parafia.bezpieczenstwo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UzytkownikRepository extends JpaRepository<Uzytkownik, Long> {

    Optional<Uzytkownik> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    long countByRolaAndAktywnyIsTrue(Rola rola);
}
