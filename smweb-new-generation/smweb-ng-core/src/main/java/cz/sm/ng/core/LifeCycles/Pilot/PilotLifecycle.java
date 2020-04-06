package cz.sm.ng.core.LifeCycles.Pilot;

import cz.sm.ng.core.LifeCycles.AbstractLifeCycle;
import cz.sm.ng.core.LifeCycles.events.EventBasedTransitionEvent;
import cz.sm.ng.core.LifeCycles.exceptions.IllegalTransitionException;
import cz.sm.ng.core.events.EventType;
import cz.sm.ng.core.identity.exceptions.IdentityNotFoundException;
import cz.sm.ng.core.identity.models.Identity;
import cz.sm.ng.core.identity.models.IdentityWithGameAccess;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstraktna trieda, ktora je predkom vsetkych roznych moznych implementacii zivotneho cyklu pilota.
 *
 * Tato trieda v sebe neobsahuje ziadnu logiku o dostupnych stavoch a prechodoch -- to vsetko dodava
 * az rozsirujuca (implementujuca) trieda.
 *
 * @author Jaroslav Dufek
 */
@Entity
@Table(name = "lifecycle_pilot")
public abstract class PilotLifecycle extends AbstractLifeCycle<PilotState, EventBasedTransitionEvent> implements Serializable
{



//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////

    /**
     * Pilot, kteremu patri tento zivotni cyklus.
     */
    @Id
    private int ownerPilotId;

    /**
     * Identita, ktorej patri tento zivotny cyklus.
     */
    private int ownerIdentityId;


    /**
     * Vycet eventov, ktore su platne pre tuto PilotLifecycle, hoci obsahuju ineho
     * hlavneho aktera nez je "nas" pilot (alebo neobsahuju aktera vobec.
     */
    private static final Set<String> allowedEventsWithDifferentActor = new HashSet<String>(Arrays.asList(new String[]{EventType.MISSION_END, EventType.LANDED_IN_FIELD, EventType.RESCUE_SUCCESS, EventType.AI_PILOT_SEAT_CHANGED, EventType.PILOT_DEAD, EventType.ACTOR_DEAD, EventType.PILOT_BAILED_SUC}));







//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Zakladny bezparametricky bean konstruktor.
     */
    protected PilotLifecycle()
    {
    }



    /**
     * Konstruktor -- poznamena si instanciu Identity, pre ktoru bude tato instancia zivotneho cyklu sluzit.
     * @param pilot
     *//*
    public PilotLifecycle(VirtualPilot pilot)
    {
        this.ownerPilotId = pilot.getId();
        this.ownerIdentityId = pilot.getOwner().getId();
    }*/


// ======================================================================================


    /**
     * Vrati instanciu identity, pre ktoru je tento zivotny cyklus asociovany.
     *
     * Je to proxy metoda na IdentityManager.getIdentity().
     *
     * @return
     *//*
    public IdentityWithGameAccess getIdentity()
    {
        try {
            Identity identity = IdentityManager.getGlobalStaticInstance().getIdentity(this.ownerIdentityId);
            if (identity instanceof IdentityWithGameAccess) {
                return (IdentityWithGameAccess)identity;
            } else {
                throw new IllegalStateException("Identia pre LifeCycle s identityId = " + this.ownerIdentityId + " sa sice nasla, ale nie je potomkom IdentityWithGameAccess!");
            }

        } catch (IdentityNotFoundException ex) {
            throw new IllegalStateException("Ziada sa identia pre LifeCycle s identityId = " + this.ownerIdentityId + ", ktora sa ale v DB nenasla!", ex);
        }

    }*/

// ======================================================================================


    public int getOwnerIdentityId()
    {
        return ownerIdentityId;
    }


    public void setOwnerIdentityId(int ownerIdentityId)
    {
        this.ownerIdentityId = ownerIdentityId;
    }

// ======================================================================================



    /**
     * Skontroluje, ci sa prijaty event naozaj tyka tejto identity, a ak ano, preposle ziadost
     * o spracovanie nadradenej triede.
     *
     *
     * @param event
     * @throws IllegalTransitionException
     *//*
    @Override
    public void applyEvent(EventBasedTransitionEvent event) throws IllegalTransitionException
    {
        IdentityWithGameAccess identity = this.getIdentity();
        String myIdentityIdent = identity.getIl2NickName();

        if ( ! allowedEventsWithDifferentActor.contains(event.getType()) ) {
            // -- vykonaj kontrolu, ci je prijaty event naozaj platy pre tento lifecycle
            if(event.getData() != null) { // pokud je pritomen backing event, je to event ze hry, takze zkontrolovat prijemce
                String actorIdent = event.getData().getActorIdent();
                if ( actorIdent == null || !actorIdent.equals(myIdentityIdent) ) {
                    throw new IllegalTransitionException("Illegal event sent to IdentityLifecycle pilot with identity [" + this.getIdentity() + "]. Event: " + event.toString());
                }
            }
        }

        String stateNameBeforeApply = this.getCurrentState().getName();

        super.applyEvent(event);

        VirtualPilotManager.getInstance().savePilotLifecycle(this);

        if (! (stateNameBeforeApply.equals(this.getCurrentState().getName()))) {
            LifecycleMessage lifecycleMessage = new LifecycleMessage(this.getIdentity().getLogin(), this.getCurrentState().getStateEventTypeName());
            try {
                CoreModule.getModulesMessanger().send(lifecycleMessage);
            } catch (MessengerException ex) {
                Logger.getLogger(PilotLifeCycleManager.class.getName()).log(Level.WARNING, "Nepodarilo se odeslat zpravu o zmene stavu lifecyclu pilota u nicku {0} do JMS.", new Object[] { this.getIdentity().getIl2NickName() });
            }
        }
    }*/


// ======================================================================================


    @Override
    public String toString()
    {
        return "PilotLifecycle{identity=" + ownerIdentityId + ", currentState=" + this.getCurrentState() + ", historySize=" + this.getHistoryOfStates().size() + '}';
    }


// ======================================================================================


    /**
     * @return the ownerPilotId
     */
    public int getOwnerPilotId() {
        return ownerPilotId;
    }

    /**
     * @param ownerPilotId the ownerPilotId to set
     */
    public void setOwnerPilotId(int ownerPilotId) {
        this.ownerPilotId = ownerPilotId;
    }


// ======================================================================================


    /**
     * Proxy metoda na ziskanie instancie VirtualPilot, ktory vlastni tento zivotny cyklus,
     * alebo NULL ak sa ziadna instancia nenajde!
     *
     * @return
     *//*
    public VirtualPilot getOwnerVirtualPilot()
    {
        try {
            return VirtualPilotManager.getInstance().getVirtualPilot(this.ownerPilotId);
        } catch (VirtualPilotNotFoundException ex) {
            return null;
        }
    }*/


// ======================================================================================





}

