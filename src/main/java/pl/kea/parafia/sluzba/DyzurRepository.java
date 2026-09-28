package pl.kea.parafia.sluzba;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DyzurRepository extends JpaRepository<Dyzur, Long> {
    List<Dyzur> findByNabozenstwoIdOrderByRolaAsc(Long nabozenstwoId);

    Optional<Dyzur> findByNabozenstwoIdAndOsobaIdAndRola(Long nabozenstwoId, Long osobaId, RolaSluzby rola);

    boolean existsByOsobaId(Long osobaId);
}
