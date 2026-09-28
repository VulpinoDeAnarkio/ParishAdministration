package pl.kea.parafia.duszpasterstwo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WizytaRepository extends JpaRepository<Wizyta, Long> {
    List<Wizyta> findBySprawaIdOrderByDataWizytyDesc(Long sprawaId);

    List<Wizyta> findTop8ByOrderByDataWizytyDesc();

    long countByDataWizytyGreaterThanEqual(java.time.LocalDate od);
}
