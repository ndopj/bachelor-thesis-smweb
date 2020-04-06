package cz.sm.ng.core.LifeCycles.Identity;

import cz.sm.ng.core.LifeCycles.AbstractLifeCycle;
import cz.sm.ng.core.LifeCycles.events.EventBasedTransitionEvent;
import cz.sm.ng.core.LifeCycles.exceptions.IllegalTransitionException;
import cz.sm.ng.core.SMWeb.modules.core.CoreModule;
import cz.sm.ng.core.SMWeb.modules.exceptions.MessengerException;
import cz.sm.ng.core.SMWeb.modules.messages.LifecycleMessage;
import cz.sm.ng.core.events.EventType;
import cz.sm.ng.core.identity.IdentityManager;
import cz.sm.ng.core.identity.models.Identity;
import cz.sm.ng.core.identity.models.IdentityWithGameAccess;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstraktna trieda, ktora je predkom vsetkych roznych moznych implementacii zivotneho cyklu identity.
 *
 * Tato trieda v sebe neobsahuje ziadnu logiku o dostupnych stavoch a prechodoch -- to vsetko dodava
 * az rozsirujuca (implementujuca) trieda.
 *
 * @author Roman Stoklasa
 */
@Entity
@Table(name = "lifecycle_identity")
public abstract class IdentityLifecycle extends AbstractLifeCycle<IdentityState, EventBasedTransitionEvent> implements Serializable
{



//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////

    @Autowired
    @Transient // do not map this attribute into database.
    private IdentityManager identityManager;

    /**
     * Identita, ktorej patri tento zivotny cyklus.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    @MapsId
    private Identity ownerIdentity;

    /**
     * Vycet eventov, ktore su platne pre tuto IdentityLifecycle, hoci obsahuju ineho
     * hlavneho aktera nez je "nasa" identita (alebo neobsahuju aktera vobec.
     */
    private static final Set<String> allowedEventsWithDifferentActor = new HashSet<String>(
            Arrays.asList(new String[]{
                    EventType.MISSION_END,
            })
    );







//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Zakladny bezparametricky bean konstruktor.
     */
    protected IdentityLifecycle()
    {
    }



    /**
     * Konstruktor -- poznamena si instanciu, pre ktoru bude tato instancia zivotneho cyklu sluzit.
     * @param identity
     */
    public IdentityLifecycle(Identity identity)
    {
        //this.ownersIdentity = identity;
        this.ownerIdentity = identity;
    }


// ======================================================================================


    /**
     * Vrati instanciu identity, pre ktoru je tento zivotny cyklus asociovany.
     *
     * Je to proxy metoda na IdentityManager.getIdentity().
     *
     * @return
     */
    public IdentityWithGameAccess getIdentity()
    {
        if (ownerIdentity instanceof IdentityWithGameAccess) {
            return (IdentityWithGameAccess) ownerIdentity;
        }
        throw new IllegalStateException("Identia pre LifeCycle s identityId = " + this.ownerIdentity.getId()
                                         + " sice existuje, ale nie je potomkom IdentityWithGameAccess!");
    }

// ======================================================================================


    public Identity getOwnerIdentity()
    {
        return ownerIdentity;
    }


    public void setOwnerIdentity(Identity ownerIdentityId)
    {
        this.ownerIdentity = ownerIdentityId;
    }

// ======================================================================================



    /**
     * Skontroluje, ci sa prijaty event naozaj tyka tejto identity, a ak ano, preposle ziadost
     * o spracovanie nadradenej triede.
     *
     *
     * @param event
     * @throws IllegalTransitionException
     */
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
                    throw new IllegalTransitionException("Illegal event sent to IdentityLifecycle for identity [" + this.getIdentity() + "]. Event: " + event.toString());
                }
            }
        }

        String stateNameBeforeApply = this.getCurrentState().getName();

        super.applyEvent(event);
        identity.setLifeCycle(this);
        identityManager.saveIdentity(identity); // update changed lifecycle

        // pokud nastal novy stav odeslat udalost do JMS
        if (! (stateNameBeforeApply.equals(this.getCurrentState().getName()))) {
            LifecycleMessage lifecycleMessage = new LifecycleMessage(this.getIdentity().getLogin(), this.getCurrentState().getStateEventTypeName());
            try {
                CoreModule.getModulesMessanger().send(lifecycleMessage);
            } catch (MessengerException ex) {
                // NORO Logger.getLogger(IdentityLifeCycleManager.class.getName()).log(Level.WARNING, "Nepodarilo se odeslat zpravu o zmene stavu lifecyclu identity u nicku {0} do JMS.", new Object[] { this.getIdentity().getIl2NickName() });
            }
        }
    }


// ======================================================================================


    @Override
    public String toString()
    {
        return "IdentityLifecycle{identity=" + ownerIdentity + ", currentState=" /*+ this.getCurrentState()*/ + ", historySize=" /*+ this.getHistoryOfStates().size()*/ + '}';
    }

    /**
     * Vrati instanci posledniho virtualniho pilota, ktery pouziva/la tato identita.
     *
     * DEPRECATED! Tato metoda sa nema pouzivat, pretoze nedava zmysel, aby obecny
     * @return
     *//*
    @Deprecated
    public abstract VirtualPilot getLastVirtualPilot();*/



}

