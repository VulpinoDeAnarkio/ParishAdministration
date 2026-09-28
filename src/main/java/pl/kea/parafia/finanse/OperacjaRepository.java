package pl.kea.parafia.finanse;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperacjaRepository extends JpaRepository<Operacja, Long> {
    List<Operacja> findByFunduszIdOrderByDataOperacjiDescIdDesc(Long funduszId);

    List<Operacja> findTop12ByOrderByDataOperacjiDescIdDesc();
}
