package pl.kea.parafia.pulpit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WejscieController {

    @GetMapping("/login")
    public String logowanie() {
        return "logowanie";
    }

    @GetMapping("/")
    public String pulpit() {
        return "pulpit/index";
    }

    @GetMapping("/brak-dostepu")
    public String brakDostepu() {
        return "error/403";
    }
}
