package pl.kea.parafia.ksiega;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlubRepository extends JpaRepository<Slub, Long> {
    List<Slub> findByOsobaAIdOrOsobaBId(Long osobaAId, Long osobaBId);
    List<Slub> findTop20ByOrderByDataSlubuDesc();
    boolean existsByNumerKsiegi(String numer);
    boolean existsByNumerKsiegiAndIdNot(String numer, Long id);
}
