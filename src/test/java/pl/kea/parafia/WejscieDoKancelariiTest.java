package pl.kea.parafia;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.hamcrest.Matchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import pl.kea.parafia.bezpieczenstwo.Rola;
import pl.kea.parafia.bezpieczenstwo.Uzytkownik;
import pl.kea.parafia.bezpieczenstwo.UzytkownikRepository;

@SpringBootTest
@AutoConfigureMockMvc
class WejscieDoKancelariiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UzytkownikRepository uzytkownicy;

    @Autowired
    private PasswordEncoder koder;

    @BeforeEach
    void kontoTestowe() {
        if (uzytkownicy.findByEmailIgnoreCase("test@poznan.parafia.pl").isEmpty()) {
            uzytkownicy.save(new Uzytkownik(
                    "test@poznan.parafia.pl",
                    koder.encode("tajne-haslo"),
                    "Test",
                    "Kancelarii",
                    Rola.SEKRETARIAT,
                    true
            ));
        }
    }

    @Test
    void niezalogowanyTrafiaNaLogowanie() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void stronaLogowaniaPokazujeNazweParafii() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Parafia Ewangelicko-Augsburska Zmiłowania Bożego w Poznaniu")))
                .andExpect(content().string(Matchers.containsString("Diecezja Pomorsko-Wielkopolska")));
    }

    @Test
    void poprawneHasloWpuszczaNaPulpit() throws Exception {
        mvc.perform(post("/login")
                        .param("username", "test@poznan.parafia.pl")
                        .param("password", "tajne-haslo")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
