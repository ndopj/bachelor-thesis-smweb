package cz.sm.ng.core.SMWeb.modules.core.exceptions;

/**
 * Vynimka pouzivana CoreModule-om, aby oznamil, ze prijaty typ eventu nevie ako spracovat.
 *
 * Moze nastat situacia, ze v priebehu casu vzniknu nove typy eventov, tak aby existovala nejaka
 * notifikacia, ze je treba spracovanie noveho typu vynimky implementovat existuje tato vynimka.
 *
 * @author Roman Stoklasa
 */
public class UnsupportedEventTypeException extends Exception
{


    public UnsupportedEventTypeException(Throwable cause)
    {
        super(cause);
    }


    public UnsupportedEventTypeException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public UnsupportedEventTypeException(String message)
    {
        super(message);
    }


    public UnsupportedEventTypeException()
    {
    }

}

