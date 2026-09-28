package pl.kea.parafia.postep;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LekcjaRepository extends JpaRepository<Lekcja, Long> {
    List<Lekcja> findByRocznikIdOrderByDataLekcjiAsc(Long rocznikId);
}
