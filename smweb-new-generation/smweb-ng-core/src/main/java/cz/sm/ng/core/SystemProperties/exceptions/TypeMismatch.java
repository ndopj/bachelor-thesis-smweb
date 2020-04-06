package cz.sm.ng.core.SystemProperties.exceptions;

/**
 * Vynimka, ktora sa pouziva v entitach SystemProperty na oznamenie, ze dane nastavenie obsahuje hodnotu
 * ineho typu, nez bolo pozadovane.
 *
 * @author Roman Stoklasa
 */
public class TypeMismatch extends Exception
{

    public TypeMismatch(Throwable cause)
    {
        super(cause);
    }


    public TypeMismatch(String message, Throwable cause)
    {
        super(message, cause);
    }


    public TypeMismatch(String message)
    {
        super(message);
    }


    public TypeMismatch()
    {
        super();
    }

}

