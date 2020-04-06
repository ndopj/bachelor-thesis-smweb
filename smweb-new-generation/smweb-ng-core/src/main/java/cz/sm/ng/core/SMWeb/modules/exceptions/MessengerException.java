package cz.sm.ng.core.SMWeb.modules.exceptions;


/**
 * Tato vynimka je pouzivana na oznamenie nejakej chyby z ModulesMessenger-a, napr. neuspesny pokus o odoslanie.
 *
 *
 * @author Roman Stoklasa
 */
public class MessengerException extends Exception
{


    public MessengerException(Throwable cause)
    {
        super(cause);
    }


    public MessengerException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public MessengerException(String message)
    {
        super(message);
    }


    public MessengerException()
    {
        super();
    }


}

