package cz.sm.ng.core.identity.exceptions;

/**
 * Vyjimka vyhozena pri chybe autorizace - objekt / identita nema pristup
 * ke zdroji / prostredku / funkcionalite.
 * @author Dejvino
 */
public class AuthorizationFailedException extends Exception {

    public AuthorizationFailedException(Throwable cause) {
        super(cause);
    }

    public AuthorizationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationFailedException(String message) {
        super(message);
    }

    public AuthorizationFailedException() {
    }
}

