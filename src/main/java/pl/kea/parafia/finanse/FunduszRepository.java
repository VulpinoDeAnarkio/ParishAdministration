package pl.kea.parafia.finanse;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunduszRepository extends JpaRepository<Fundusz, Long> {
    List<Fundusz> findAllByOrderByNazwaAsc();
}
