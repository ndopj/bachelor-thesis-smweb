package cz.sm.ng.core.LifeCycles.Identity;

import cz.sm.ng.core.LifeCycles.LifecycleEventType;
import cz.sm.ng.core.LifeCycles.Pilot.PilotState;
import cz.sm.ng.core.LifeCycles.IState;
import cz.sm.ng.core.LifeCycles.ITransitionEvent;
import cz.sm.ng.core.LifeCycles.events.EventBasedTransitionEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roman Stoklasa
 */
@Entity
@Table(name = "lifecycle_identity_states")
public abstract class IdentityState implements IState, Serializable
{


//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * ID-cko daneho stavu-u, potrebne kvoli ulozeniu do DB.
     * Ak ma hodnotu NULL, znamena to, ze tento stav este nema obraz v DB.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id = null;


    /**
     * Instancia nadriadeneho zivotneho cyklu.
     */
    @ManyToOne
    private IdentityLifecycle parentStateMachine;


    /**
     * 'Efektivny' cas vytvorenie tohoto stavu.
     *
     * Efektivny znamena, ze to nemusi byt nutne systemovy cas, kedy bola instancia vytvorena, ale moze to byt napr.
     * cas kedy vznikol event, ktory tento novy stav sposobil.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=true)
    Date effectiveDateOfCreate;



//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Zakladny bezparametricky (bean) konstruktor.
     */
    protected IdentityState()
    {
    }


// ======================================================================================


    /**
     * Zakladny konstruktor - poznamena si referenciu na instanciu objektu nadradeneho zivotneho cyklu.
     * @param parentStateMachine
     * @param effectiveDateOfCreate
     */
    public IdentityState(IdentityLifecycle parentStateMachine, Date effectiveDateOfCreate)
    {
        this.parentStateMachine = parentStateMachine;
        this.effectiveDateOfCreate = effectiveDateOfCreate;
    }

// ======================================================================================



    /**
     * Vrati databazove ID-cko tohoto stavu (tejto entity).
     * @return
     */
    public Integer getId()
    {
        return id;
    }


    public void setId(Integer id)
    {
        this.id = id;
    }


// ======================================================================================


    /**
     * Vrati instanciu nadriadeneho zivotneho cyklu.
     * @return
     */
    public IdentityLifecycle getParentStateMachine()
    {
        return parentStateMachine;
    }


// ======================================================================================


    /**
     * Ziska efektivny cas vzniku tohoto stavu.
     * @return
     */
    public Date getEffectiveDateOfCreate()
    {
        return effectiveDateOfCreate;
    }


    public void setEffectiveDateOfCreate(Date effectiveDateOfCreate)
    {
        this.effectiveDateOfCreate = effectiveDateOfCreate;
    }


// ======================================================================================


    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "[" + this.getId() + "]{" + this.parentStateMachine.getIdentity() + '}';
    }


// ======================================================================================


    @Override
    public IState performTransition(ITransitionEvent event)
    {
        if (! (event instanceof EventBasedTransitionEvent)) {
            throw new IllegalArgumentException("envet must be of type SMEventTransitonEvent. Found: " + event.getClass().getCanonicalName());
        }
        return this.performTransition((EventBasedTransitionEvent)event);
    }


// ======================================================================================



    protected abstract IState performTransition(EventBasedTransitionEvent event);


// ======================================================================================


    /**
     * Vrati nazov tohoto stavu.
     *
     * @return
     */
    @Override
    public String getName()
    {
        return this.getClass().getSimpleName();
    }

// ======================================================================================


    /**
     * Vrati nazev event typu pro stav.
     *
     * Vraci NULL pokud nerozpozna stav.
     *
     * @return
     */
    public String getStateEventTypeName()
    {
        String name = getName();

        if(name.equals("InGame")) {
            return LifecycleEventType.LIFECYCLE_IDENTITY_IN_GAME;
        } else if(name.equals("InGameMenuDF")) {
            return LifecycleEventType.LIFECYCLE_IDENTITY_IN_GAMEMENU;
        } else if(name.equals("OfflineDF")) {
            return LifecycleEventType.LIFECYCLE_IDENTITY_OFFLINE;
        } else {
            Logger.getLogger(PilotState.class.getName()).log(Level.WARNING, "IdentityState trida nedokazala zjistit EventTypeName");
            return "UNKNOWN_IDENTITY_STATE";
        }

    }


// ======================================================================================

    /**
     * Defaultna prazdna implementacia metoda, ktora nerobi nic (pretoze nepotrebuje).
     * Stav, ktory chce tuto metodu vyuzit, nech ju Override-ne.
     */
    @Override
    public void activate()
    {
        // -- standartne stav nemusi nic robit, a nemusi to implementovat, ak nema zaujem.
        // Ak by stav mal co na praci, tak tuto metodu jednoducho pretazi.
    }

// ======================================================================================



}

