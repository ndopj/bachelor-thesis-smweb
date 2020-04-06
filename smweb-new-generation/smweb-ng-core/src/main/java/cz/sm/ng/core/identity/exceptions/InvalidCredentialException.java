package cz.sm.ng.core.identity.exceptions;

/**
 * Vynimka pouzivana k oznameniu, ze zadane udaje (login // heslo) su neplatne.
 *
 * @author Roman Stoklasa
 */
public class InvalidCredentialException extends Exception
{


    public InvalidCredentialException(Throwable cause)
    {
        super(cause);
    }


    public InvalidCredentialException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public InvalidCredentialException(String message)
    {
        super(message);
    }


    public InvalidCredentialException()
    {
    }


}

