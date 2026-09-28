package pl.kea.parafia.konfiguracja;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.kea.parafia.bezpieczenstwo.BiezacyUzytkownik;
import pl.kea.parafia.parafia.ProfilParafii;
import pl.kea.parafia.parafia.ProfilParafiiService;

@ControllerAdvice
public class ModelGlobalny {

    private final ProfilParafiiService profile;
    private final BiezacyUzytkownik biezacy;
    private final boolean trybDemo;

    public ModelGlobalny(
            ProfilParafiiService profile,
            BiezacyUzytkownik biezacy,
            @Value("${parafia.demo:false}") boolean trybDemo
    ) {
        this.profile = profile;
        this.biezacy = biezacy;
        this.trybDemo = trybDemo;
    }

    @ModelAttribute("trybDemo")
    public boolean trybDemo() {
        return trybDemo;
    }

    @ModelAttribute("profil")
    public ProfilParafii profil() {
        return profile.pobierz();
    }

    @ModelAttribute("konto")
    public Object konto() {
        return biezacy.znajdz().orElse(null);
    }

    @ModelAttribute("mozeFinanse")
    public boolean mozeFinanse() {
        return biezacy.maRole("ADMIN", "PROBOSZCZ", "SKARBNIK");
    }

    @ModelAttribute("mozeDuszpasterstwo")
    public boolean mozeDuszpasterstwo() {
        return biezacy.maRole("ADMIN", "PROBOSZCZ", "DUSZPASTERZ");
    }

    @ModelAttribute("mozeEdytowacKartoteke")
    public boolean mozeEdytowacKartoteke() {
        return biezacy.maRole("ADMIN", "PROBOSZCZ", "SEKRETARIAT", "DUSZPASTERZ");
    }

    @ModelAttribute("jestAdminem")
    public boolean jestAdminem() {
        return biezacy.maRole("ADMIN");
    }

    @ModelAttribute("mozeProfil")
    public boolean mozeProfil() {
        return biezacy.maRole("ADMIN", "PROBOSZCZ");
    }
}
