package pl.kea.parafia.obiekt;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ObiektRepository extends JpaRepository<Obiekt, Long> {

    @Query("select o from Obiekt o left join fetch o.rodzic order by o.rodzaj, o.nazwa")
    List<Obiekt> wszystkieZRodzicem();

    List<Obiekt> findByRodzicIdOrderByNazwaAsc(Long rodzicId);

    Optional<Obiekt> findFirstByNazwa(String nazwa);
}
