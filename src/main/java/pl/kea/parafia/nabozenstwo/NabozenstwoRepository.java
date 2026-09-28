package pl.kea.parafia.nabozenstwo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NabozenstwoRepository extends JpaRepository<Nabozenstwo, Long> {
    List<Nabozenstwo> findTop30ByOrderByWydarzeniePoczatekDesc();
    Optional<Nabozenstwo> findByWydarzenieId(Long wydarzenieId);
}
