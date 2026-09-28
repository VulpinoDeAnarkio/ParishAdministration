package pl.kea.parafia.grupa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupaRepository extends JpaRepository<Grupa, Long> {
    List<Grupa> findAllByOrderByNazwaAsc();
}
