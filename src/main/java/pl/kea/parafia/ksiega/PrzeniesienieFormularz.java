package pl.kea.parafia.ksiega;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record PrzeniesienieFormularz(
        @NotNull Long osobaId,
        @NotNull @PastOrPresent LocalDate dataCzynnosci,
        @NotNull KierunekPrzeniesienia kierunek,
        @NotBlank @Size(max = 200) String parafia,
        @NotBlank @Size(max = 40) String numerKsiegi,
        @Size(max = 1000) String uwagi
) {
    public static PrzeniesienieFormularz pusty(Long osobaId, String numer) {
        return new PrzeniesienieFormularz(osobaId, LocalDate.now(), KierunekPrzeniesienia.PRZYJECIE, "", numer, "");
    }

    public static PrzeniesienieFormularz z(Przeniesienie wpis) {
        return new PrzeniesienieFormularz(
                wpis.getOsoba().getId(), wpis.getDataCzynnosci(), wpis.getKierunek(), wpis.getParafia(),
                wpis.getNumerKsiegi(), wpis.getUwagi()
        );
    }
}
