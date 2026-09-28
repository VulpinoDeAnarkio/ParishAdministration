package pl.kea.parafia.ksiega;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrzeniesienieRepository extends JpaRepository<Przeniesienie, Long> {
    List<Przeniesienie> findByOsobaIdOrderByDataCzynnosciAsc(Long osobaId);
    List<Przeniesienie> findTop20ByOrderByDataCzynnosciDesc();
    boolean existsByNumerKsiegi(String numer);
    boolean existsByNumerKsiegiAndIdNot(String numer, Long id);
}
