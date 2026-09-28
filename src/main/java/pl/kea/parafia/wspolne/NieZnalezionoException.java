package pl.kea.parafia.wspolne;

public class NieZnalezionoException extends RuntimeException {

    public NieZnalezionoException(String komunikat) {
        super(komunikat);
    }
}
