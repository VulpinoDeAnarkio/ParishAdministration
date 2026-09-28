package pl.kea.parafia.postep;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObecnoscLekcjiRepository extends JpaRepository<ObecnoscLekcji, Long> {
    List<ObecnoscLekcji> findByLekcjaId(Long lekcjaId);

    Optional<ObecnoscLekcji> findByLekcjaIdAndOsobaId(Long lekcjaId, Long osobaId);
}
