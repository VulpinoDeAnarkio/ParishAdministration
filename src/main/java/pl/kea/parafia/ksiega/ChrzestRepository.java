package pl.kea.parafia.ksiega;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChrzestRepository extends JpaRepository<Chrzest, Long> {
    Optional<Chrzest> findByOsobaId(Long osobaId);
    List<Chrzest> findTop20ByOrderByDataChrztuDesc();
    boolean existsByNumerKsiegiAndIdNot(String numer, Long id);
    boolean existsByNumerKsiegi(String numer);
}
