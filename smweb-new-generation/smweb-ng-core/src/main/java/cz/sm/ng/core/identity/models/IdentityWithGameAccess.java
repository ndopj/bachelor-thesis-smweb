package cz.sm.ng.core.identity.models;

import cz.sm.ng.core.LifeCycles.Identity.IdentityLifecycle;
import cz.sm.ng.core.LifeCycles.IState;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Tato trieda reprezentuje identity, ktore mozu mat pristup do hry (IL-2).
 *
 * Tato trieda je pod IdentityWithTS3Access, pretoze napr. general moze mat pristup na TS, ale nepredpoklada
 * sa, ze by mal tiez pristup do hry. Zaroven, kazda identita, co ma pristup do hry ma urcite moznost
 * pristupu aj na TS.
 *
 * @author Roman Stoklasa
 */
@Entity
@Table(name = "IDENT_WITH_GAME_ACCESS")
public class IdentityWithGameAccess extends IdentityWithTS3Access implements Serializable
{


//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////

    /**
     * Nick na hernim serveru
     */
    @Column(nullable = false, unique = true)
    private String il2NickName;


    /**
     * Zivotny cyklus identity
     */
    @OneToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    private IdentityLifecycle lifeCycle;



//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Vrati nick identity na hernom IL-2 serveri.
     *
     * @return
     */
    public String getIl2NickName()
    {
        return il2NickName;
    }

    public void setIl2NickName(String il2NickName)
    {
        this.il2NickName = il2NickName;
    }


// ======================================================================================


    /**
     * Vrati zivotny cyklu tejto identity.
     * @return
     */
    public IdentityLifecycle getLifecycle()
    {
        return this.lifeCycle;
    }


    public void setLifeCycle(IdentityLifecycle lifeCycle)
    {
        this.lifeCycle = lifeCycle;
    }


// ======================================================================================


    /**
     * Proxy metoda na ziskanie priamo nazvu aktualneho stavu zivotneho cyklu, v ktorom sa
     * tato identita nachadza.
     *
     * @return
     */
    public String getCurrentStateName()
    {
        IdentityLifecycle lifecycle = this.getLifecycle();
        if (lifecycle == null) {
            return "unknown (LifeCycle not found)";
        }

        IState currentState = lifecycle.getCurrentState();
        return (currentState == null) ? "unknown (current state not found)" : currentState.getName();
    }

// ======================================================================================


    @Override
    public String toString()
    {
        return this.getClass().getName() + "[" + this.getId() + "]{" + this.getLogin() + " (" + il2NickName + ")}";
    }




}

