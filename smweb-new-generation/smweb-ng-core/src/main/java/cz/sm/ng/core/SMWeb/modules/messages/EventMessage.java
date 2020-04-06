package cz.sm.ng.core.SMWeb.modules.messages;

import cz.sm.ng.core.events.Event;

/**
 * Tato trieda reprezentuje spravu, ktora je nosicom nejakej udalosti z eventlogu.
 * Ako payload nesie instanciu objektu Event, ktory obsahuje udaje ktore boli
 * ziskane z eventlog hlasky.
 *
 * @author Roman Stoklasa
 */
public class EventMessage extends ModulesMessage
{

    public static final String TYPE_IDENT = "EventMessage";



//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////



    /**
     * Konstruktor - nastavy spravny typ spravy a ulozi do payload atributu objekt eventu.
     * @param event Instancia eventu, ktoru ma niest novovytvorena sprava
     * @return
     */
    public EventMessage(Event event)
    {
        //super("EventMessage");
        super(TYPE_IDENT, event);
    }

// ======================================================================================


}

