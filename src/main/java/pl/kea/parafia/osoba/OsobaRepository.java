package pl.kea.parafia.osoba;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OsobaRepository extends JpaRepository<Osoba, Long> {

    @EntityGraph(attributePaths = "gospodarstwo")
    @Query(
            value = """
                    select o from Osoba o
                    where (:q is null or (
                        lower(o.imie) like lower(concat('%', :q, '%'))
                        or lower(o.nazwisko) like lower(concat('%', :q, '%'))
                        or lower(coalesce(o.email, '')) like lower(concat('%', :q, '%'))
                        or lower(concat(o.imie, ' ', o.nazwisko)) like lower(concat('%', :q, '%'))))
                      and (:status is null or o.status = :status)
                    """,
            countQuery = """
                    select count(o) from Osoba o
                    where (:q is null or (
                        lower(o.imie) like lower(concat('%', :q, '%'))
                        or lower(o.nazwisko) like lower(concat('%', :q, '%'))
                        or lower(coalesce(o.email, '')) like lower(concat('%', :q, '%'))
                        or lower(concat(o.imie, ' ', o.nazwisko)) like lower(concat('%', :q, '%'))))
                      and (:status is null or o.status = :status)
                    """
    )
    Page<Osoba> szukaj(@Param("q") String q, @Param("status") StatusCzlonkostwa status, Pageable pageable);

    List<Osoba> findByGospodarstwoIdOrderByNazwiskoAscImieAsc(Long gospodarstwoId);

    List<Osoba> findAllByOrderByNazwiskoAscImieAsc();

    List<Osoba> findByStatusInOrderByNazwiskoAscImieAsc(List<StatusCzlonkostwa> statusy);

    long countByStatus(StatusCzlonkostwa status);

    @Query("select o.gospodarstwo.id, count(o) from Osoba o where o.gospodarstwo is not null group by o.gospodarstwo.id")
    List<Object[]> liczbyWGospodarstwach();
}
