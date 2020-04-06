package cz.sm.ng.core.gameplay.weapons.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Tato trieda reprezentuje urcitu municiu, jej typ, mnozstvo a poradie
 * v danej konfiguracii vyzbroje lietadla (weaponConfiguration).
 * Ku jednej konfiguracii vyzbroje moze byt asociovanych viacero objektov tejto triedy s rovnakym
 * planeType.
 *
 * Napr. pre konfiguraciu vyzbroje s identifikatorom "9x7.62mmMG+10x250kgbomb" by mohli existovat
 * nasledovne tri instancie tejto triedy s nasledujucimi hodnotami atributov (order, weaponTypeIdent, count):
 *  - 1, 7.62mmMG, 6
 *  - 2, 250kgbomb, 10
 *  - 3, 7.62mmMG, 3
 *
 * @author Veronika Martosova
 */
@Entity
@Table(name = "weapon_type_in_config")
public class WeaponTypeInConfiguration implements Serializable
{
//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////

    private static final long serialVersionUID = 1L;

    /**
     * Unikatne ID tejto instancie entity v DB.
     */
    @Id
    @GeneratedValue
    private int id;

    /**
     * Konfiguracia vyzbroje.
     */
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="weaponConfigIdent", referencedColumnName = "weaponConfigIdent"),
            @JoinColumn(name="planeTypeIdent", referencedColumnName = "planeTypeIdent")
    })
    private WeaponConfiguration weaponConfiguration;

    /**
     * Typ municie.
     */
    @ManyToOne
    @JoinColumn(name = "weaponTypeId", referencedColumnName = "id")
    private WeaponType weaponType;

    /**
     * Mnozstvo municie reprezentovanej tymto objektom.
     *
     */
    @Column(name = "count")
    private int count;

    /**
     * Poradie, v akom ma byt municia reprezentovana tymto objektom v danej konfiguracii vyzbroje pouzita (vystrelena).
     * Je to kladna celociselna hodnota (nikde nie 0).
     */
    @Column(name = "orderInConfig")
    private int order;


//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Zakladny bezparametricky konstruktor.
     */
    public WeaponTypeInConfiguration()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public WeaponConfiguration getWeaponConfiguration()
    {
        return weaponConfiguration;
    }

    public void setWeaponConfiguration(WeaponConfiguration weaponConfiguration)
    {
        this.weaponConfiguration = weaponConfiguration;
    }

    public WeaponType getWeaponType()
    {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType)
    {
        this.weaponType = weaponType;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    /**
     * Dve entity su rovnake, ked maju rovnake ID.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WeaponTypeInConfiguration other = (WeaponTypeInConfiguration) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 97 * hash + this.id;
        return hash;
    }

    @Override
    public String toString()
    {
        return "TypeInConfiguration{" + "id=" + id + ", weaponConfiguration=" + weaponConfiguration.toString() + ", weaponType=" + weaponType.toString() + ", count=" + count + ", order=" + order + '}';
    }

}

