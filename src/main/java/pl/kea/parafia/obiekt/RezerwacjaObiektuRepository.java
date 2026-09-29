package pl.kea.parafia.obiekt;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RezerwacjaObiektuRepository extends JpaRepository<RezerwacjaObiektu, Long> {

    @Query("""
            select r from RezerwacjaObiektu r
            join fetch r.obiekt o
            left join fetch o.rodzic
            where r.obiekt.id = :obiektId
            order by r.poczatek desc
            """)
    List<RezerwacjaObiektu> obiektu(@Param("obiektId") Long obiektId);

    @Query("""
            select r from RezerwacjaObiektu r
            join fetch r.obiekt o
            left join fetch o.rodzic
            where r.status = pl.kea.parafia.obiekt.StatusRezerwacji.POTWIERDZONA
              and r.koniec >= :od
            order by r.poczatek
            """)
    List<RezerwacjaObiektu> nadchodzace(@Param("od") LocalDateTime od);

    @Query("""
            select r from RezerwacjaObiektu r
            where r.obiekt.id in :ids
              and r.status = pl.kea.parafia.obiekt.StatusRezerwacji.POTWIERDZONA
              and (:pomin is null or r.id <> :pomin)
              and r.poczatek < :koniec
              and r.koniec > :poczatek
            """)
    List<RezerwacjaObiektu> kolizje(
            @Param("ids") Collection<Long> ids,
            @Param("pomin") Long pomin,
            @Param("poczatek") LocalDateTime poczatek,
            @Param("koniec") LocalDateTime koniec
    );
}
