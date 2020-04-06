package cz.sm.web.prototype.springbootjsf.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Identita reprezentujuca normalneho "radoveho" hraca, ktory vystupuje typicky v roli pilota.
 *
 * Atribut login sa pouziva ako nickname na IL-2 hernom serveri.
 */
@Entity
//@NamedNativeQueries({
//	@NamedNativeQuery(name = Pilot.GET_PILOT_BY_NICK, query = "select object(o) from Pilot as o where o.nick = :nick")})
@Table(name="IDENT_PILOT")
public class Pilot extends Identity implements Serializable {

    public static final String GET_PILOT_BY_NICK = "nick";
    private static final long serialVersionUID = 1L;

    /**
     * Nasleduje seznam priznaku, ktore si moze general u daneho pilota poznacit.
     * Typickym priznakom moze byt "hviezdicka", ktoru si u daneho pilota moze
     * general zapnut, aby mu indikovala, ze dany pilot je velitelom.
     * Dalsie priznaky mozu byt napr. 'bomber', 'fighter', 'reconnaissance', ...
     */
    /**
     * Priznak 'hviezdicky'.
     */
    private boolean starred;

    /**
     * Priznak ze dany pilot je stihacom.
     */
    private boolean fighter;

    /**
     * Priznak ze pilot je bombarderom
     */
    private boolean bomber;

    /**
     * Priznak ze pilot je priezkumnikom.
     */
    private boolean recon;


//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////



    public boolean isBomber() { return bomber; }
    public void setBomber(boolean bomber){ this.bomber = bomber; }

// ======================================================================================


    public boolean isFighter() { return fighter; }
    public void setFighter(boolean fighter) { this.fighter = fighter; }

// ======================================================================================

    public boolean isRecon() { return recon; }
    public void setRecon(boolean recon) { this.recon = recon; }

// ======================================================================================

    public boolean isStarred() { return starred; }
    public void setStarred(boolean starred) { this.starred = starred; }

// ======================================================================================

    @Override
    public String toString()
    {
        return super.toString() + "(bomber=" + bomber + ", fighter=" + fighter + ", recon=" + recon + ")";
    }
}

