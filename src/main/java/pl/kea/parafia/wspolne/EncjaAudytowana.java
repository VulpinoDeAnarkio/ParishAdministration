package pl.kea.parafia.wspolne;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class EncjaAudytowana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime utworzono;

    private LocalDateTime zmodyfikowano;

    @PrePersist
    void przedZapisem() {
        LocalDateTime teraz = LocalDateTime.now();
        if (utworzono == null) {
            utworzono = teraz;
        }
        zmodyfikowano = teraz;
    }

    @PreUpdate
    void przedZmiana() {
        zmodyfikowano = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getUtworzono() {
        return utworzono;
    }

    public LocalDateTime getZmodyfikowano() {
        return zmodyfikowano;
    }
}
