package pl.kea.parafia.obecnosc;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ObecnoscRepository extends JpaRepository<Obecnosc, Long> {
    List<Obecnosc> findByRodzajAndCelId(RodzajObecnosci rodzaj, Long celId);

    Optional<Obecnosc> findByRodzajAndCelIdAndOsobaId(RodzajObecnosci rodzaj, Long celId, Long osobaId);

    @Query("""
            select o.celId, count(o) from Obecnosc o
            where o.rodzaj = pl.kea.parafia.obecnosc.RodzajObecnosci.NABOZENSTWO and o.obecny = true
            group by o.celId
            """)
    List<Object[]> obecniNaNabozenstwach();
}
