package cz.sm.web.prototype.javaeejsf.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="IDENT_PILOT")
public class Pilot extends Identity implements Serializable {

    public static final String GET_PILOT_BY_NICK = "nick";
    private static final long serialVersionUID = 1L;

    private boolean starred;
    private boolean fighter;
    private boolean bomber;
    private boolean recon;

    public boolean isBomber() { return bomber; }
    public void setBomber(boolean bomber){ this.bomber = bomber; }

    public boolean isFighter() { return fighter; }
    public void setFighter(boolean fighter) { this.fighter = fighter; }

    public boolean isRecon() { return recon; }
    public void setRecon(boolean recon) { this.recon = recon; }

    public boolean isStarred() { return starred; }
    public void setStarred(boolean starred) { this.starred = starred; }

    @Override
    public String toString()
    {
        return super.toString() + "(bomber=" + bomber + ", fighter=" + fighter + ", recon=" + recon + ")";
    }
}

