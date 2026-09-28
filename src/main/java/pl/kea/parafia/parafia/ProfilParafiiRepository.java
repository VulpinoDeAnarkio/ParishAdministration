package pl.kea.parafia.parafia;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilParafiiRepository extends JpaRepository<ProfilParafii, Long> {

    Optional<ProfilParafii> findFirstByOrderByIdAsc();
}
