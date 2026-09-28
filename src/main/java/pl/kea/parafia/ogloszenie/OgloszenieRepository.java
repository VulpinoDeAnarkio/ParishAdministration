package pl.kea.parafia.ogloszenie;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OgloszenieRepository extends JpaRepository<Ogloszenie, Long> {
    List<Ogloszenie> findAllByOrderByObowiazujeOdDesc();

    List<Ogloszenie> findByWidocznePublicznieIsTrueOrderByObowiazujeOdDesc();
}
