package cz.sm.ng.core.gameplay.weapons.models;

import cz.sm.ng.core.gameplay.weapons.WeaponCategory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Tato trieda reprezentuje typ municie. Instancie tejto triedy budu niest informacie o jednotlivych
 * typoch municii - nazov, vaha, a pod. Napr. {"name" : "SC 250", "weight" : "250", "category" : "BOMB"}.
 *
 * @author Veronika Martosova
 */
@Entity
@Table(name = "WEAPON_TYPE")
public class WeaponType implements Serializable
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
     * Nazov typu municie. Je jedinecny, sluzi teda aj ako jednoznacny identifikator.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Vaha municie.
     */
    @Column
    private int weight;

    /**
     * Kategoria, do ktorej tento typ municie spada.
     */
    @Column
    private WeaponCategory category;


//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Zakladny bezparametricky konstruktor.
     */
    public WeaponType()
    {
    }

    /**
     * Konstruktor so specifikovanym nazvom typu municie.
     *
     * @param name
     */
    public WeaponType(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    public WeaponCategory getCategory()
    {
        return category;
    }

    public void setCategory(WeaponCategory category)
    {
        this.category = category;
    }

    /**
     * Metoda vrati true, ak tento typ municie je skutocne municiou, false inak.
     *
     * @return true, ak tento typ municie je skutocne municiou, false inak.
     */
    public boolean isWeapon()
    {
        switch(this.category) {
            case BOMB:
            case ROCKET:
            case MUNITION:
            case OTHER:
                return true;
            default:
                return false;
        }
    }

    /**
     * Metoda vrati true, ak je tento typ municie palivom, false inak.
     *
     * @return true, ak je tento typ municie palivom, false inak.
     */
    public boolean isFuel()
    {
        return this.category.equals(WeaponCategory.FUEL);
    }

    /**
     * Dve entity su povazovane za rovnake, ked maju rovnake ID.
     *
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
        final WeaponType other = (WeaponType) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 29 * hash + this.id;
        return hash;
    }

    @Override
    public String toString()
    {
        return "WeaponType{" + "id=" + id + ", name=" + name + ", weight=" + weight + ", category=" + category.toString() + '}';
    }

}

