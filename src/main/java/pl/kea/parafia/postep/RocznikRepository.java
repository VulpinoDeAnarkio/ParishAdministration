package pl.kea.parafia.postep;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RocznikRepository extends JpaRepository<Rocznik, Long> {
    List<Rocznik> findAllByOrderByRokDesc();
    Optional<Rocznik> findFirstByOrderByRokDesc();
}
