package pl.kea.parafia.sluzba;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZadanieRepository extends JpaRepository<Zadanie, Long> {
    List<Zadanie> findAllByOrderByStatusAscTerminAsc();

    long countByStatusNot(StatusZadania status);
}
