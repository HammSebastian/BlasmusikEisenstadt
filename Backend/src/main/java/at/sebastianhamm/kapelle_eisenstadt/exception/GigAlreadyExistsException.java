package at.sebastianhamm.kapelle_eisenstadt.exception;

public class GigAlreadyExistsException extends RuntimeException {
    public GigAlreadyExistsException(String message) {
        super(message);
    }
}
