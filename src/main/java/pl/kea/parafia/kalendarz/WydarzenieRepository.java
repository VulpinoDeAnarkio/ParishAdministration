package pl.kea.parafia.kalendarz;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WydarzenieRepository extends JpaRepository<Wydarzenie, Long> {

    @Query("""
            select w from Wydarzenie w
            where w.poczatek < :koniec and w.koniec > :poczatek
            order by w.poczatek
            """)
    List<Wydarzenie> wOkresie(@Param("poczatek") LocalDateTime poczatek, @Param("koniec") LocalDateTime koniec);

    @Query("""
            select w from Wydarzenie w
            where w.sala.id = :salaId
              and (:id is null or w.id <> :id)
              and w.poczatek < :koniec
              and w.koniec > :poczatek
            """)
    List<Wydarzenie> kolizjeSali(
            @Param("salaId") Long salaId,
            @Param("id") Long id,
            @Param("poczatek") LocalDateTime poczatek,
            @Param("koniec") LocalDateTime koniec
    );

    List<Wydarzenie> findTop8ByPoczatekGreaterThanEqualOrderByPoczatekAsc(LocalDateTime od);
}
