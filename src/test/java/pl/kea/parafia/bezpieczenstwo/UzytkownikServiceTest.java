package pl.kea.parafia.bezpieczenstwo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@SpringBootTest
@AutoConfigureMockMvc
class UzytkownikServiceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UzytkownikService usluga;

    @Autowired
    private UzytkownikRepository repozytorium;

    @Autowired
    private PasswordEncoder koder;

    @Test
    @WithMockUser(roles = "SEKRETARIAT")
    void sekretariatNieWidziKont() throws Exception {
        mvc.perform(get("/uzytkownicy"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void administratorDodajePracownika() throws Exception {
        mvc.perform(post("/uzytkownicy")
                        .param("imie", "Jan")
                        .param("nazwisko", "Organista")
                        .param("email", "organista@lipowa.parafia.pl")
                        .param("haslo", "organy-123")
                        .param("rola", "SEKRETARIAT")
                        .param("aktywny", "true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/uzytkownicy"));

        Uzytkownik zapisany = repozytorium.findByEmailIgnoreCase("organista@lipowa.parafia.pl").orElseThrow();
        assertThat(koder.matches("organy-123", zapisany.getHaslo())).isTrue();
        assertThat(zapisany.getRola()).isEqualTo(Rola.SEKRETARIAT);
    }

    @Test
    @Transactional
    void nieMoznaWylaczycOstatniegoAdministratora() {
        Uzytkownik admin = repozytorium.save(new Uzytkownik(
                "jedyny-admin@lipowa.parafia.pl",
                koder.encode("haslo-admina"),
                "Jedyny",
                "Admin",
                Rola.ADMIN,
                true
        ));
        repozytorium.findAll().stream()
                .filter(u -> u.getRola() == Rola.ADMIN && !u.getId().equals(admin.getId()))
                .forEach(u -> {
                    u.setAktywny(false);
                    repozytorium.save(u);
                });

        assertThatThrownBy(() -> usluga.aktualizuj(admin.getId(), new UzytkownikFormularz(
                "Jedyny", "Admin", admin.getEmail(), "", Rola.SEKRETARIAT, true
        ))).isInstanceOf(RegulaParafiiException.class);
    }
}
