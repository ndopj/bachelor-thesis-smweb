package cz.sm.ng.core.LifeCycles.exceptions;


/**
 * Tato vynimka oznacuje chybu, kedy je pozadovany neplatny prechod (prechod, ktory nie je definovany).
 *
 * @author Roman Stoklasa
 */
public class IllegalTransitionException extends IllegalStateException
{


    public IllegalTransitionException()
    {
    }


    public IllegalTransitionException(String s)
    {
        super(s);
    }


    public IllegalTransitionException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public IllegalTransitionException(Throwable cause)
    {
        super(cause);
    }

}

