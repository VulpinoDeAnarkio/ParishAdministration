package pl.kea.parafia.kalendarz;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findByAktywnaIsTrueOrderByNazwaAsc();
}
