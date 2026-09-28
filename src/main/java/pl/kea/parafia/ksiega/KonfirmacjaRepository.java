package pl.kea.parafia.ksiega;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KonfirmacjaRepository extends JpaRepository<Konfirmacja, Long> {
    Optional<Konfirmacja> findByOsobaId(Long osobaId);
    List<Konfirmacja> findTop20ByOrderByDataKonfirmacjiDesc();
    boolean existsByNumerKsiegi(String numer);
    boolean existsByNumerKsiegiAndIdNot(String numer, Long id);
}
