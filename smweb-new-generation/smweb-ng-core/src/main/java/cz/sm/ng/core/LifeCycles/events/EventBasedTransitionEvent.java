package cz.sm.ng.core.LifeCycles.events;

import cz.sm.ng.core.LifeCycles.ITransitionEvent;
import cz.sm.ng.core.events.Event;

import java.util.Date;
import java.util.Map;

/**
 * Tato trieda implementuje prechod, ktory je zalozeny na "SM evente".
 *
 * "SM event" oznacuje udalosti ci uz z eventlogu, alebo udalost distribuovanu
 * cez JMS Topic. Obecne to je trieda cz.sm.events.Event.
 *
 * @author Roman Stoklasa
 */
public class EventBasedTransitionEvent implements ITransitionEvent<String, Event>
{

//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////



    /**
     * SM-event, ktory nesie data tohoto eventu
     */
    private Event event;


    /**
     * Atribut, ktory moze uchovavat dodatocne typy eventu -- typy nad ramec typov eventlog eventov.
     * Napr. novo definovany stav 'PILOT_ASSOCIATED_WITH_PLANE', ktory nepochadza z eventlogu, ale
     * je generovany zivotnym cyklom.
     */
    private String additionalEventType = null;


    /**
     * Atribut, ktoreho ulohou je uchovavat dodatocne parametre prechodu (nad ramec alebo namiesto
     * parametrov v instancii Event-u).
     */
    private Map<String, Object> additionalParams = null;


    private Date eventTime = new Date();


//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Konstruktor - vytvori ITransitionEvent pre zadany SM event
     * @param event
     */
    public EventBasedTransitionEvent(Event event)
    {
        this.event = event;
    }


// ======================================================================================

    /**
     * Konstruktor - vytvori ITransitionEvent pre zadany SM event
     *
     * @param additionalEventType
     */
    public EventBasedTransitionEvent(String additionalEventType)
    {
        this.event = null;
        this.additionalEventType = additionalEventType;
    }


// ======================================================================================

    /**
     * Konstruktor - vytvori ITransitionEvent pre zadany SM event
     *
     * @param additionalEventType
     * @param additionalParams
     */
    public EventBasedTransitionEvent(String additionalEventType, Map<String, Object> additionalParams)
    {
        this(additionalEventType);
        this.additionalParams = additionalParams;
    }


// ======================================================================================



    /**
     * Staticka factory metoda.
     *
     * @param event
     * @return
     */
    public static EventBasedTransitionEvent forEvent(Event event)
    {
        return new EventBasedTransitionEvent(event);
    }



// ======================================================================================


    /**
     * Vraty typ 'backing' event-u, nebo additional typ, pokud zadny backing event neexistuje.
     *
     * @return
     */
    @Override
    public String getType()
    {
        if (this.event != null) {
            return this.event.getEventType();
        } else {
            return this.getAdditionalEventType();
        }

    }


// ======================================================================================


    /**
     * Vrati SM event, z ktoreho je mozne vytiahnut jeho atributy.
     *
     * @return
     */
    @Override
    public Event getData()
    {
        return this.event;
    }

// ======================================================================================


    @Override
    public String toString()
    {
        if (event != null) {
            return "SMEventTransitionEvent{" + this.event.toString() + '}';
        } else {
            return "InnerTransitionEvent{" + this.getAdditionalEventType() + '}';
        }
    }

// ======================================================================================




    /**
     * @return the additionalEventType
     */
    public String getAdditionalEventType()
    {
        return additionalEventType;
    }

    /**
     * @param additionalEventType the additionalEventType to set
     */
    public void setAdditionalEventType(String additionalEventType) {
        this.additionalEventType = additionalEventType;
    }


// ======================================================================================

    /**
     * Vrati mapu doplnkovych parametrov.
     * @return
     */
    public Map<String, Object> getAdditionalParams()
    {
        return additionalParams;
    }

// ======================================================================================


    public Date getEventTime()
    {
        if (this.getData() != null) {
            return this.getData().getEventTime();
        } else {
            return this.eventTime;
        }
    }

    public void setEventTime(Date eventTime)
    {
        this.eventTime = eventTime;
    }




}

