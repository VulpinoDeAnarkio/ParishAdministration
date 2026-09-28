package pl.kea.parafia.raport;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RaportController {

    private final RaportService raporty;

    public RaportController(RaportService raporty) {
        this.raporty = raporty;
    }

    @GetMapping("/raporty")
    public String indeks() {
        return "raport/indeks";
    }

    @GetMapping("/raporty/osoby")
    public ResponseEntity<String> osoby() {
        return csv("kartoteka.csv", raporty.osobyCsv());
    }

    @GetMapping("/raporty/finanse")
    @PreAuthorize("hasAnyRole('ADMIN','PROBOSZCZ','SKARBNIK')")
    public ResponseEntity<String> finanse() {
        return csv("finanse.csv", raporty.finanseCsv());
    }

    private ResponseEntity<String> csv(String nazwa, String tresc) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nazwa + "\"")
                .contentType(new MediaType("text", "csv", java.nio.charset.StandardCharsets.UTF_8))
                .body(tresc);
    }
}
