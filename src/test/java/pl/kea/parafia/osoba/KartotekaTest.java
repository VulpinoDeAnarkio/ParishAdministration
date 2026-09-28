package pl.kea.parafia.osoba;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class KartotekaTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "SKARBNIK")
    void skarbnikMozeCzytacAleNieDopisuje() throws Exception {
        mvc.perform(get("/osoby")).andExpect(status().isOk());
        mvc.perform(post("/osoby")
                        .param("imie", "Ewa")
                        .param("nazwisko", "Nowak")
                        .param("plec", "KOBIETA")
                        .param("status", "CZLONEK")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SEKRETARIAT")
    void sekretariatZakladaDomIOsobe() throws Exception {
        mvc.perform(post("/gospodarstwa")
                        .param("nazwa", "Dom Nowaków")
                        .param("miejscowosc", "Lipowa")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/gospodarstwa/*"));

        mvc.perform(post("/osoby")
                        .param("imie", "Adam")
                        .param("nazwisko", "Nowak")
                        .param("plec", "MEZCZYZNA")
                        .param("status", "CZLONEK")
                        .param("uczestniczyWWieczerzy", "true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/osoby/*"));

        mvc.perform(get("/osoby").param("q", "Nowak").param("status", "CZLONEK"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Adam")));
    }
}
