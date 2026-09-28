package pl.kea.parafia.wspolne;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ObslugaWyjatkow {

    @ExceptionHandler(NieZnalezionoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String nieZnaleziono(NieZnalezionoException wyjatek, HttpServletRequest zadanie) {
        zadanie.setAttribute("komunikat", wyjatek.getMessage());
        return "error/404";
    }

    @ExceptionHandler(RegulaParafiiException.class)
    public String regula(RegulaParafiiException wyjatek, HttpServletRequest zadanie, RedirectAttributes atrybuty) {
        atrybuty.addFlashAttribute("blad", wyjatek.getMessage());
        return "redirect:" + bezpiecznaSciezka(zadanie.getHeader("Referer"));
    }

    private String bezpiecznaSciezka(String referer) {
        if (referer == null || referer.isBlank()) {
            return "/";
        }
        try {
            URI uri = URI.create(referer);
            String sciezka = uri.getPath();
            if (sciezka == null || !sciezka.startsWith("/") || sciezka.startsWith("//")) {
                return "/";
            }
            return uri.getRawQuery() == null ? sciezka : sciezka + "?" + uri.getRawQuery();
        } catch (IllegalArgumentException ex) {
            return "/";
        }
    }
}
