package pl.kea.parafia.postep;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UczestnikRocznikaRepository extends JpaRepository<UczestnikRocznika, Long> {
    List<UczestnikRocznika> findByRocznikIdOrderByOsobaNazwiskoAsc(Long rocznikId);
    Optional<UczestnikRocznika> findByRocznikIdAndOsobaId(Long rocznikId, Long osobaId);
    boolean existsByOsobaId(Long osobaId);
}
