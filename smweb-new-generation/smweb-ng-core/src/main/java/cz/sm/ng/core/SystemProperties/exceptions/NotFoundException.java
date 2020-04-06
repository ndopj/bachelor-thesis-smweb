package cz.sm.ng.core.SystemProperties.exceptions;

/**
 * Vynimka pouzivana na oznamenie chyby, ze dane nastavenie sa nenaslo.
 *
 *
 * @author Roman Stoklasa
 */
public class NotFoundException extends Exception
{


    public NotFoundException(Throwable cause)
    {
        super(cause);
    }


    public NotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public NotFoundException(String message)
    {
        super(message);
    }


    public NotFoundException()
    {
    }



}

