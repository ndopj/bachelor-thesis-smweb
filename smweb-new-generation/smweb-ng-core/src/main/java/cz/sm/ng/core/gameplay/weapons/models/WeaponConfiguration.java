package cz.sm.ng.core.gameplay.weapons.models;

import com.google.gson.annotations.Expose;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Tato trieda reprezentuje konfiguraciu vyzbroje lietadla pre dany typ lietadla.
 * Objekty tejto triedy su abstrakciou mnozin obsahujucich rozne typy municie (weaponType)
 * a nim odpovedajuce pocty a poradie municie v danej konfiguracii vyzbroje.
 * (napriklad 6x .50 cal Browning M2 machine gun + 250kg bomb + 20x Tiny Tim rocket)
 *
 * @author Veronika Martosova
 */
@Entity
@Table(name = "WEAPON_CONFIG")
public class WeaponConfiguration implements Serializable
{
//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////

    private static final long serialVersionUID = 1L;

    @Expose
    @EmbeddedId
    private WeaponConfigurationPK weaponConfigPK;

    /**
     * Nazov konfiguracie vyzbroje lietadla.
     * Nie je to jednoznacny identifikator, sluzi len na human-readable oznacenie.
     */
    @Expose
    @Column
    private String name;

    /**
     * Zoznam objektov, ktore specifikuju typy municie, ich pocty a poradie v tejto konfiguracii vyzbroje.
     */
    @OneToMany(mappedBy="weaponConfiguration", cascade= CascadeType.REMOVE, fetch= FetchType.EAGER)
    private List<WeaponTypeInConfiguration> weaponTypesInConfig = new ArrayList<>();


//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////

    /**
     * Zakladny bezparametricky konstruktor.
     */
    public WeaponConfiguration()
    {
    }

    /**
     * Konstruktor so specifikovanymi identifikatormi konfiguracie vyzbroje a typu lietadla.
     * Tieto sa stanu primarnym klucom vytvaranej instancie konfiguracie vyzbroje.
     *
     * @param weaponConfigIdent identifikator konfiguracie vyzbroje
     * @param planeTypeIdent identifikator typu lietadla
     */
    public WeaponConfiguration(String weaponConfigIdent, String planeTypeIdent)
    {
        this.weaponConfigPK = new WeaponConfigurationPK();
        this.weaponConfigPK.setWeaponConfigIdent(weaponConfigIdent);
        this.weaponConfigPK.setPlaneTypeIdent(planeTypeIdent);
    }

    public WeaponConfigurationPK getWeaponConfigPK()
    {
        return weaponConfigPK;
    }

    public void setWeaponConfigPK(WeaponConfigurationPK weaponConfigPK)
    {
        this.weaponConfigPK = weaponConfigPK;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<WeaponTypeInConfiguration> getWeaponTypesInConfig()
    {
        return weaponTypesInConfig;
    }

    public void setWeaponTypesInConfig(List<WeaponTypeInConfiguration> weaponTypesInConfig)
    {
        this.weaponTypesInConfig = weaponTypesInConfig;
    }

    /**
     * Dve entity su povazovane za rovnake, ked maju rovnake ID.
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
        final WeaponConfiguration other = (WeaponConfiguration) obj;
        if (!Objects.equals(this.weaponConfigPK, other.weaponConfigPK)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.weaponConfigPK);
        return hash;
    }

    @Override
    public String toString()
    {
        return "WeaponConfiguration{" + "weaponConfigPK=" + this.getWeaponConfigPK().toString() + ", name=" + this.getName() + '}';
    }

}

