package pl.kea.parafia.obiekt;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ObiektWidokTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObiektService obiekty;

    @Test
    @WithMockUser(roles = "SEKRETARIAT")
    void listaPokazujeHotelISalki() throws Exception {
        mvc.perform(get("/obiekty"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Hotel parafialny")))
                .andExpect(content().string(Matchers.containsString("Salka parafialna")))
                .andExpect(content().string(Matchers.containsString("Pokój gościnny")));
    }

    @Test
    @WithMockUser(roles = "SEKRETARIAT")
    void sekretariatRezerwujeSalke() throws Exception {
        Long salka = obiekty.znajdzPoNazwie("Salka katechetyczna").getId();
        mvc.perform(post("/obiekty/" + salka + "/rezerwacje")
                        .param("obiektId", salka.toString())
                        .param("poczatek", "2027-04-08T17:00")
                        .param("koniec", "2027-04-08T19:00")
                        .param("rezerwujacy", "Nauka konfirmacyjna")
                        .param("cel", "Lekcja")
                        .param("liczbaOsob", "8")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/obiekty/" + salka));
    }
}
