package pl.kea.parafia.ksiega;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PogrzebRepository extends JpaRepository<Pogrzeb, Long> {
    Optional<Pogrzeb> findByOsobaId(Long osobaId);
    List<Pogrzeb> findTop20ByOrderByDataZgonuDesc();
    boolean existsByNumerKsiegi(String numer);
    boolean existsByNumerKsiegiAndIdNot(String numer, Long id);
}
