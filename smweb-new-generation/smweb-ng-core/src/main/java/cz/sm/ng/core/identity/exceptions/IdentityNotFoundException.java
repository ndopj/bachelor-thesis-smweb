package cz.sm.ng.core.identity.exceptions;

/**
 * Tato vynimka sluzi k oznameniu, ze pozadovana identita sa nenasla.
 *
 * @author Romi
 */
public class IdentityNotFoundException extends Exception
{


    public IdentityNotFoundException(Throwable cause)
    {
        super(cause);
    }


    public IdentityNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public IdentityNotFoundException(String message)
    {
        super(message);
    }


    public IdentityNotFoundException()
    {
    }


}

