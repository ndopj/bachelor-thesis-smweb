package cz.sm.ng.core.SystemProperties;

import cz.sm.ng.core.SystemProperties.exceptions.TypeMismatch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Calendar;

/**
 * Tato entita reprezentuje jednu hodnotu nastaveni. Je specifikovana svojim stringovym klucom,
 * a nesie nejaku hodnotu nejakeho typu.
 *
 *
 * @author Roman Stoklasa
 */
@Entity
@Table
public class SystemProperty implements Serializable
{

//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////


    private static final long serialVersionUID = 1L;

    /**
     * Identifikator (nazov) tohoto nastavenia.
     */
    @Id
    @Column(name="PROPERTYKEY")
    private String key;

    private String stringRawValue = null;
    private Integer integerRawValue = null;
    private Double doubleRawValue = null;


    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar calendarRawValue = null;


//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////



    /**
     * Default bezparametricky (bean) konstruktor -- nova instancia nebude mat vytvorenu ziadnu hodnotu!
     */
    public SystemProperty()
    {
    }

    /**
     * Konstruktor -- vytvori nastavenie, ktore obsahuje stringovu hodnotu.
     *
     * POZOR!! Vytvorenie tohoto nastavenia este neznamena ze bude ulozene aj do DB! To sa musi ulozit
     * pomocou SystemConfig triedy!
     *
     * @param key
     * @param stringValue
     */
    public SystemProperty(String key, String stringValue)
    {
        this.key = key;
        this.stringRawValue = stringValue;
    }


    /**
     * Konstruktor -- vytvori nastavenie, ktore obsahuje integer hodnotu.
     *
     * POZOR!! Vytvorenie tohoto nastavenia este neznamena ze bude ulozene aj do DB! To sa musi ulozit
     * pomocou SystemConfig triedy!
     *
     * @param key
     * @param intValue
     */
    public SystemProperty(String key, int intValue)
    {
        this.key = key;
        this.integerRawValue = intValue;
    }


    /**
     * Konstruktor -- vytvori nastavenie, ktore obsahuje double hodnotu.
     *
     * POZOR!! Vytvorenie tohoto nastavenia este neznamena ze bude ulozene aj do DB! To sa musi ulozit
     * pomocou SystemConfig triedy!
     *
     * @param key
     * @param doubleValue
     */
    public SystemProperty(String key, double doubleValue)
    {
        this.key = key;
        this.doubleRawValue = doubleValue;
    }


    /**
     * Konstruktor -- vytvori nastavenie, ktore obsahuje date-time (Calendar) hodnotu.
     *
     * POZOR!! Vytvorenie tohoto nastavenia este neznamena ze bude ulozene aj do DB! To sa musi ulozit
     * pomocou SystemConfig triedy!
     *
     * @param key
     * @param calendarValue
     */
    public SystemProperty(String key, Calendar calendarValue)
    {
        this.key = key;
        this.calendarRawValue = calendarValue;
    }



    /**
     * Metoda, ktora vrati hodnotu prveho typu, ktora je ne-NULLova. Ak su vsetky NULL-ove, tak vrati NULL.
     *
     * @return
     */
    public Object getValue()
    {
        if (this.stringRawValue != null) {
            return this.stringRawValue;
        }

        if (this.integerRawValue != null) {
            return this.integerRawValue;
        }

        if (this.doubleRawValue != null) {
            return this.doubleRawValue;
        }

        if (this.calendarRawValue != null) {
            return this.calendarRawValue;
        }

        // -- vsetky hodnoty su NULL-ove? Vratim null...
        return null;
    }


// ======================================================================================

    /**
     * Vrati typ hodnoty tohoto nastavenia, alebo NULL, ak su vsetky hodnote null-ove.
     * @return
     */
    public Class getType()
    {
        Object value = this.getValue();
        return (value != null ? value.getClass() : null);
    }

// ======================================================================================


    /**
     * Vrati hodnotu, ktora je typu String, alebo NULL, ak ziadnu hodnotu neobsahuje.
     * Pokial obsahuje hodnotu ineho typu nez String, vyhadzuje vynimku TypeMismatch.
     *
     * @return
     * @throws TypeMismatch
     */
    public String getStringValue() throws TypeMismatch
    {
        Object value = this.getValue();

        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return (String)value;
        } else {
            throw new TypeMismatch("Incorrect type requested - property " + this.key + " contains value of type: " + (this.getType() != null ? this.getType().toString() : "NULL") );
        }

    }

// ======================================================================================

    /**
     * Vrati hodnotu, ktora je typu int.
     *
     * Pokial neobsahuje hodnotu typu int, vyhadzuje vynimku TypeMismatch.
     *
     * @return
     * @throws TypeMismatch
     */
    public int getIntValue() throws TypeMismatch
    {
        Object value = this.getValue();
        if (value instanceof Integer) {
            return ((Integer)value).intValue();
        } else {
            throw new TypeMismatch("Incorrect type requested - property " + this.key + " contains value of type: " + (this.getType() != null ? this.getType().toString() : "NULL") );
        }

    }

// ======================================================================================


    /**
     * Vrati hodnotu, ktora je typu double.
     *
     * Pokial neobsahuje hodnotu typu double, vyhadzuje vynimku TypeMismatch.
     *
     * @return
     * @throws TypeMismatch
     */
    public double getDoubleValue() throws TypeMismatch
    {
        Object value = this.getValue();
        if (value instanceof Double) {
            return ((Double)value).doubleValue();
        } else {
            throw new TypeMismatch("Incorrect type requested - property " + this.key + " contains value of type: " + (this.getType() != null ? this.getType().toString() : "NULL") );
        }

    }

// ======================================================================================


    /**
     * Vrati hodnotu, ktora je typu Calendar, alebo NULL, ak ziadnu hodnotu neobsahuje.
     * Pokial obsahuje hodnotu ineho typu nez Calendar, vyhadzuje vynimku TypeMismatch.
     *
     * @return
     * @throws TypeMismatch
     */
    public Calendar getCalendarValue() throws TypeMismatch
    {
        Object value = this.getValue();

        if (value == null) {
            return null;
        }

        if (value instanceof Calendar) {
            return (Calendar)value;
        } else {
            throw new TypeMismatch("Incorrect type requested - property " + this.key + " contains value of type: " + (this.getType() != null ? this.getType().toString() : "NULL") );
        }

    }

// ======================================================================================


    /**
     * Nastavy vsetky hodnoty na NULL (hodnoty vsetkych typov).
     */
    private void clearAllValues()
    {
        this.stringRawValue = null;
        this.integerRawValue = null;
        this.doubleRawValue = null;
        this.calendarRawValue = null;
    }


// ======================================================================================

    /**
     * Nastavi novu hodnotu tejto instancie  na stringovu hodnotu value.
     *
     * Hodnotu nepotrebuje klonovat, pretoze String je "immutable".
     *
     * @param value
     */
    public void setStringValue(String value)
    {
        this.clearAllValues();
        this.stringRawValue = value;
    }

// ======================================================================================


    /**
     * Nastavi novu hodnotu tejto instancie na intovu hodnotu value
     *
     * @param value
     */
    public void setIntValue(int value)
    {
        this.clearAllValues();
        this.integerRawValue = new Integer(value);
    }

// ======================================================================================


    /**
     * Nastavi novu hodnotu tejto instancie na intovu hodnotu value
     * @param value
     */
    public void setDoubleValue(double value)
    {
        this.clearAllValues();
        this.doubleRawValue = new Double(value);
    }

// ======================================================================================


    /**
     * Nastavi novu hodnotu tejto instancie na Calendar hodnotu 'value'
     * @param value
     */
    public void setCalendarValue(Calendar value)
    {
        this.clearAllValues();
        this.calendarRawValue = (Calendar)value.clone();
    }

// ======================================================================================




    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (key != null ? key.hashCode() : 0);
        hash += (this.getValue() != null ? this.getValue().hashCode() : 0);

        return hash;
    }


// ======================================================================================


    /**
     * Porovna zhodu dvoch instancii.
     *
     * Dve instancie su povazovane za rovnake, ak maju rovnaky nazov (key) a obsahuju rovnake hodnoty (vzhladom na equals).
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof SystemProperty)) {
            return false;
        }
        SystemProperty other = (SystemProperty) object;
        if ((this.key == null && other.key != null) || (this.key != null && !this.key.equals(other.key))) {
            return false;
        }
        // <--> takze kluce sa zhoduju - zhoduju sa aj hodnoty?
        if ((this.getValue() == null && other.getValue() != null) || (this.getValue() != null && !this.getValue().equals(other.getValue()))) {
            return false;
        }


        return true;
    }


// ======================================================================================


    @Override
    public String toString()
    {
        return "SystemProperty[" + key + "=" + (this.getValue() != null ? this.getValue().toString() : "NULL") + "]";
    }


// ======================================================================================


    public Calendar getCalendarRawValue()
    {
        return calendarRawValue;
    }


    public void setCalendarRawValue(Calendar calendarRawValue)
    {
        this.calendarRawValue = calendarRawValue;
    }


// ======================================================================================


    public String getStringRawValue()
    {
        return stringRawValue;
    }


    public void setStringRawValue(String stringRawValue)
    {
        this.stringRawValue = stringRawValue;
    }

// ======================================================================================




    public Double getDoubleRawValue()
    {
        return doubleRawValue;
    }


    public void setDoubleRawValue(Double doubleValue)
    {
        this.doubleRawValue = doubleValue;
    }

// ======================================================================================


    public Integer getIntegerRawValue()
    {
        return integerRawValue;
    }


    public void setIntegerRawValue(Integer intValue)
    {
        this.integerRawValue = intValue;
    }

// ======================================================================================


    public String getKey()
    {
        return key;
    }


    public void setKey(String key)
    {
        this.key = key;
    }

// ======================================================================================





}

