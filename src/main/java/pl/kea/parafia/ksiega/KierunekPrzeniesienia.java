package pl.kea.parafia.ksiega;

public enum KierunekPrzeniesienia {
    PRZYJECIE("Przyjęcie do parafii"),
    PRZENIESIENIE("Przeniesienie do innej parafii"),
    WYSTAPIENIE("Wystąpienie z parafii");

    private final String etykieta;

    KierunekPrzeniesienia(String etykieta) {
        this.etykieta = etykieta;
    }

    public String getEtykieta() {
        return etykieta;
    }
}
