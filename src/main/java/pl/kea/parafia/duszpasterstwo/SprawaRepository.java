package pl.kea.parafia.duszpasterstwo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprawaRepository extends JpaRepository<SprawaDuszpasterska, Long> {
    List<SprawaDuszpasterska> findAllByOrderByZmodyfikowanoDesc();

    List<SprawaDuszpasterska> findByOsobaIdOrderByZmodyfikowanoDesc(Long osobaId);
}
