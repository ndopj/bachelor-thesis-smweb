package cz.sm.ng.core.identity.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author Roman Stoklasa
 */
@Entity
@Table(name = "IDENT_WITH_TS3")
public class IdentityWithTS3Access extends Identity implements Serializable
{


//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Nick na TS3 serveri
     */
    @Column(nullable = true, unique = true)
    protected String ts3NickName;


//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Vrati nickname, ktory dana identita pouziva na TS3 serveri.
     * @return
     */
    public String getTs3NickName()
    {
        return ts3NickName;
    }


    /**
     * Nastavi nickname, ktory dana identita pouziva na TS3 serveri.
     * @return
     */
    public void setTs3NickName(String ts3NickName)
    {
        this.ts3NickName = ts3NickName;
    }

// ======================================================================================

}

