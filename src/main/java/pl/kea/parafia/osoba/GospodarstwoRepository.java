package pl.kea.parafia.osoba;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GospodarstwoRepository extends JpaRepository<Gospodarstwo, Long> {

    List<Gospodarstwo> findAllByOrderByNazwaAsc();
}
