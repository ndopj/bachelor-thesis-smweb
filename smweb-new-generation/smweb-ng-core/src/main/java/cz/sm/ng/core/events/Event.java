package cz.sm.ng.core.events;

import cz.sm.ng.core.Position2D;
import cz.sm.ng.core.Position3D;

import java.io.Serializable;

/**
 * Trieda reprezentujuca event prijaty z herneho servera
 */
public class Event implements Serializable
{


//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////



    /**
     * index eventu - cislo, ake mu priraduje EventAgent
     */
    private long index;

    /**
     * Identifikator typu eventu - je ukladany ako textovy retazec.
     * Stringove konstanty su dostupne v triede EventType.
     */
    private String eventType;


    /**
     * Cas eventu, kedy sa odohral
     */
    private java.util.Date eventTime;

    /**
     * identifikator hlavneho aktera eventu. Typicky id pilota, o ktorom ta zprava je
     */
    private String actorIdent;

    /**
     * typ hracovho lietadla
     */
    private String planeTypeId;

    /**
     * id protivnika
     */
    private String enemyIdent;

    /**
     * typicky typ lietadla protivnika, alebo typ nalozene vyzbroje pri PILOT_SPAWNED evente.
     */
    private String weaponTypeId;

    /**
     * X-ova pozicia merana v metroch od referencneho bodu mapy
     */
    private double x;

    /**
     * Y-ova pozicia merana v metroch od referencneho bodu mapy
     */
    private double y;

    /**
     * vyska merana v metroch.
     */
    private double alt;

    /**
     * typicky cislo sedadla, alebo index strany v RADAR_MARK evente
     */
    private int number;

    /**
     * priorita spravy - default je 10
     */
    private int priority;

    /**
     * orientacne cislo riadku, z ktoreho dany event vznikol
     */
    private int eventlogSourceLine;

    /**
     * komentar - volny stringovy atribut. Pouzivany napr. v eventoch RADAR_MARK
     * (indetifikator typu objektu) alebo v PILOT_SPAWNED (k mnozstvu paliva)
     */
    private String comment;


    private short missionNumber;

//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Defaultny bezparametricky konstruktor ('bean-constructor').
     */
    public Event()
    {
    }


// ======================================================================================


    /**
     * Konstruktor, ktory vyplni aspon typ eventu.
     * @param eventType
     */
    public Event(String eventType)
    {
        this.eventType = eventType;
    }

// ======================================================================================

    /**
     * Konstruktor, ktory vyplni typ eventu a identifikator aktera..
     *
     * @param eventType
     */
    public Event(String eventType, String actorIdent)
    {
        this.eventType = eventType;
        this.actorIdent = actorIdent;
    }

// ======================================================================================

    /**
     * Vrati index eventu - cislo, ake mu priraduje EventAgent
     *
     * @return
     */
    public long getIndex()
    {
        return this.index;
    }


    public void setIndex(long index)
    {
        this.index = index;
    }


// ======================================================================================


    /**
     * Vrati Cas eventu, kedy sa odohral
     * @return
     */
    public java.util.Date getEventTime()
    {
        return this.eventTime;
    }


    public void setEventTime(java.util.Date eventTime)
    {
        this.eventTime = eventTime;
    }



// ======================================================================================


    /**
     * Vrati identifikator hlavneho aktera eventu. Typicky id pilota, o ktorom ta zprava je
     *
     * @return
     */
    public String getActorIdent()
    {
        return this.actorIdent;
    }


    public void setActorIdent(String ident)
    {
        this.actorIdent = ident;
    }


// ======================================================================================


    /**
     * Vrati typ hracovho lietadla
     * @return
     */
    public String getPlaneTypeId()
    {
        return this.planeTypeId;
    }


    public void setPlaneTypeId(String planeTypeId)
    {
        this.planeTypeId = planeTypeId;
    }


// ======================================================================================


    /**
     * Vrati id protivnika
     * @return
     */
    public String getEnemyIdent()
    {
        return this.enemyIdent;
    }


    public void setEnemyIdent(String enemyIdent)
    {
        this.enemyIdent = enemyIdent;
    }


// ======================================================================================


    /**
     * Vrati typicky typ lietadla protivnika, alebo typ nalozene vyzbroje pri PILOT_SPAWNED evente.
     * @return
     */
    public String getWeaponTypeId()
    {
        return this.weaponTypeId;
    }


    public void setWeaponTypeId(String weaponTypeId)
    {
        this.weaponTypeId = weaponTypeId;
    }


// ======================================================================================


    /**
     * Vrati  X-ovu poziciu meranu v metroch od referencneho bodu mapy
     * @return
     */
    public double getX()
    {
        return this.x;
    }


    public void setX(double x)
    {
        this.x = x;
    }


// ======================================================================================


    /**
     * Vrati Y-ovu poziciu meranu v metroch od referencneho bodu mapy
     * @return
     */
    public double getY()
    {
        return this.y;
    }


    public void setY(double y)
    {
        this.y = y;
    }


// ======================================================================================


    /**
     * Vrati vysku merana v metroch.
     * @return
     */
    public double getAlt()
    {
        return this.alt;
    }


    public void setAlt(double alt)
    {
        this.alt = alt;
    }


// ======================================================================================

    /**
     * Tovarnickova metoda na ziskanie instancie Position2D z atributov X a Y.
     * @return
     */
    public Position2D getPosition2D()
    {
        return new Position2D(this.x, this.y);
    }


// ======================================================================================

    /**
     * Tovarnickova metoda na ziskanie instancie position3D z atributov X, Y, a Alt.
     * @return
     */
    public Position3D getPosition3D()
    {
        return new Position3D(this.x, this.y, this.alt);
    }


// ======================================================================================




    /**
     * Vrati typicky cislo sedadla, alebo index strany v RADAR_MARK evente
     * @return
     */
    public int getNumber()
    {
        return this.number;
    }


    public void setNumber(int number)
    {
        this.number = number;
    }


// ======================================================================================


    /**
     * Vrati prioritu spravy - default je 10
     * @return
     */
    public int getPriority()
    {
        return this.priority;
    }


    public void setPriority(int priority)
    {
        this.priority = priority;
    }


// ======================================================================================


    /**
     * Vrati orientacne cislo riadku, z ktoreho dany event vznikol
     * @return
     */
    public int getEventlogSourceLine()
    {
        return this.eventlogSourceLine;
    }


    public void setEventlogSourceLine(int eventlogSourceLine)
    {
        this.eventlogSourceLine = eventlogSourceLine;
    }


// ======================================================================================


    /**
     * Vrati  komentar - volny stringovy atribut. Pouzivany napr. v eventoch RADAR_MARK
     * (indetifikator typu objektu) alebo v PILOT_SPAWNED (k mnozstvu paliva)
     *
     * @return
     */
    public String getComment()
    {
        return this.comment;
    }


    public void setComment(String comment)
    {
        this.comment = comment;
    }


// ======================================================================================


    /**
     * Vrati  Identifikator typu eventu - je ukladany ako textovy retazec.
     * Stringove konstanty su dostupne v triede EventType.
     *
     * @return
     */
    public String getEventType()
    {
        return this.eventType;
    }


    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }

    // =====================================================================================

    public short getMissionNumber() {
        return missionNumber;
    }

    public void setMissionNumber(short missionNumber) {
        this.missionNumber = missionNumber;
    }


// ======================================================================================


    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Event other = (Event) obj;
        if (this.index != other.index) {
            return false;
        }
        if (this.eventTime != other.eventTime && (this.eventTime == null || !this.eventTime.equals(other.eventTime))) {
            return false;
        }
        if ((this.actorIdent == null) ? (other.actorIdent != null) : !this.actorIdent.equals(other.actorIdent)) {
            return false;
        }
        if ((this.planeTypeId == null) ? (other.planeTypeId != null) : !this.planeTypeId.equals(other.planeTypeId)) {
            return false;
        }
        if ((this.enemyIdent == null) ? (other.enemyIdent != null) : !this.enemyIdent.equals(other.enemyIdent)) {
            return false;
        }
        if ((this.weaponTypeId == null) ? (other.weaponTypeId != null) : !this.weaponTypeId.equals(other.weaponTypeId)) {
            return false;
        }
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.alt != other.alt) {
            return false;
        }
        if (this.number != other.number) {
            return false;
        }
        if (this.priority != other.priority) {
            return false;
        }
        if (this.eventlogSourceLine != other.eventlogSourceLine) {
            return false;
        }
        if ((this.comment == null) ? (other.comment != null) : !this.comment.equals(other.comment)) {
            return false;
        }
        if ((this.eventType == null) ? (other.eventType != null) : !this.eventType.equals(other.eventType)) {
            return false;
        }
        return true;
    }


// ======================================================================================


    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 67 * hash + Long.toString(this.index).hashCode();
        hash = 67 * hash + (this.eventTime != null ? this.eventTime.hashCode() : 0);
        hash = 67 * hash + (this.actorIdent != null ? this.actorIdent.hashCode() : 0);
        hash = 67 * hash + (this.planeTypeId != null ? this.planeTypeId.hashCode() : 0);
        hash = 67 * hash + (this.enemyIdent != null ? this.enemyIdent.hashCode() : 0);
        hash = 67 * hash + (this.weaponTypeId != null ? this.weaponTypeId.hashCode() : 0);
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.alt) ^ (Double.doubleToLongBits(this.alt) >>> 32));
        hash = 67 * hash + this.number;
        hash = 67 * hash + this.priority;
        hash = 67 * hash + this.eventlogSourceLine;
        hash = 67 * hash + (this.comment != null ? this.comment.hashCode() : 0);
        hash = 67 * hash + (this.eventType != null ? this.eventType.hashCode() : 0);
        return hash;
    }


    @Override
    public String toString() {
        return "[" + this.index + "] '" + this.eventType + "' " + this.actorIdent + " (" + this.eventTime + ")";
    }
}

