package pl.kea.parafia.grupa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CzlonekGrupyRepository extends JpaRepository<CzlonekGrupy, Long> {
    List<CzlonekGrupy> findByGrupaIdOrderByOsobaNazwiskoAsc(Long grupaId);
    List<CzlonekGrupy> findByOsobaIdOrderByGrupaNazwaAsc(Long osobaId);
    Optional<CzlonekGrupy> findByGrupaIdAndOsobaId(Long grupaId, Long osobaId);
    boolean existsByOsobaIdAndDoDniaIsNull(Long osobaId);
}
