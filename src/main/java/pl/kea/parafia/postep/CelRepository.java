package pl.kea.parafia.postep;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CelRepository extends JpaRepository<CelParafii, Long> {
    List<CelParafii> findByAktywnyIsTrueOrderByRokDescNazwaAsc();
}
