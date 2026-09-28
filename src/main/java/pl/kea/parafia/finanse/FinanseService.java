package pl.kea.parafia.finanse;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kea.parafia.osoba.Osoba;
import pl.kea.parafia.osoba.OsobaRepository;
import pl.kea.parafia.wspolne.NieZnalezionoException;
import pl.kea.parafia.wspolne.RegulaParafiiException;

@Service
public class FinanseService {

    private final FunduszRepository fundusze;
    private final OperacjaRepository operacje;
    private final OsobaRepository osoby;

    public FinanseService(FunduszRepository fundusze, OperacjaRepository operacje, OsobaRepository osoby) {
        this.fundusze = fundusze;
        this.operacje = operacje;
        this.osoby = osoby;
    }

    @Transactional(readOnly = true)
    public List<SaldoFunduszu> salda() {
        return fundusze.findAllByOrderByNazwaAsc().stream()
                .map(fundusz -> new SaldoFunduszu(fundusz, saldo(fundusz.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public Fundusz znajdzFundusz(Long id) {
        return fundusze.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiego funduszu."));
    }

    @Transactional(readOnly = true)
    public List<Operacja> operacje(Long funduszId) {
        return operacje.findByFunduszIdOrderByDataOperacjiDescIdDesc(funduszId);
    }

    @Transactional(readOnly = true)
    public List<Operacja> ostatnie() {
        return operacje.findTop12ByOrderByDataOperacjiDescIdDesc();
    }

    @Transactional(readOnly = true)
    public BigDecimal saldo(Long funduszId) {
        return operacje(funduszId).stream()
                .map(operacja -> operacja.getKierunek() == KierunekOperacji.WPLYW ? operacja.getKwota() : operacja.getKwota().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumaWplywow() {
        return operacje.findAll().stream()
                .filter(operacja -> operacja.getKierunek() == KierunekOperacji.WPLYW)
                .map(Operacja::getKwota)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Fundusz zapiszFundusz(Long id, FunduszFormularz formularz) {
        Fundusz fundusz = id == null ? new Fundusz() : znajdzFundusz(id);
        fundusz.setNazwa(formularz.nazwa());
        fundusz.setOpis(formularz.opis());
        fundusz.setAktywny(!Boolean.FALSE.equals(formularz.aktywny()));
        return fundusze.save(fundusz);
    }

    @Transactional
    public Operacja zapiszOperacje(OperacjaFormularz formularz) {
        Fundusz fundusz = znajdzFundusz(formularz.funduszId());
        if (!fundusz.isAktywny()) {
            throw new RegulaParafiiException("Fundusz jest zamknięty. Nie zapisujemy na nim nowych operacji.");
        }
        if (formularz.rodzaj() == RodzajOperacji.SKLADKA && formularz.osobaId() == null) {
            throw new RegulaParafiiException("Składka członkowska musi być przypisana do osoby.");
        }
        Operacja operacja = new Operacja();
        operacja.setFundusz(fundusz);
        operacja.setRodzaj(formularz.rodzaj());
        operacja.setKierunek(formularz.rodzaj().getKierunek());
        operacja.setKwota(formularz.kwota());
        operacja.setDataOperacji(formularz.dataOperacji());
        operacja.setOpis(formularz.opis());
        operacja.setKontrahent(formularz.kontrahent());
        operacja.setOsoba(formularz.osobaId() == null ? null : osoba(formularz.osobaId()));
        return operacje.save(operacja);
    }

    @Transactional
    public void usunOperacje(Long id) {
        Operacja operacja = operacje.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej operacji."));
        operacje.delete(operacja);
    }

    private Osoba osoba(Long id) {
        return osoby.findById(id).orElseThrow(() -> new NieZnalezionoException("Nie ma takiej osoby."));
    }
}
