package cz.sm.ng.core.gameplay.weapons.models;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Tato trieda sluzi ako composite primary key pre entitnu triedu WeaponConfiguration. Primarny kluc sa sklada
 * zo stringoveho identifikatora konfiguracie vyzbroje a z identifikatora typu lietadla.
 *
 * @author Veronika Martosova
 */
@Embeddable
public class WeaponConfigurationPK implements Serializable
{
    /**
     * Stringovy identifikator konfiguracie vyzbroje lietadla, ako ju definuje herny server.
     */
    @Expose
    @Column(name = "weaponConfigIdent")
    private String weaponConfigIdent;

    /**
     * Identifikator typu lietadla, ako ho definuje herny server.
     */
    @Column(name = "planeTypeIdent")
    private String planeTypeIdent;

    public WeaponConfigurationPK()
    {
    }

    public WeaponConfigurationPK(String weaponConfigIdent, String planeTypeIdent)
    {
        this.weaponConfigIdent = weaponConfigIdent;
        this.planeTypeIdent = planeTypeIdent;
    }

    public String getWeaponConfigIdent()
    {
        return weaponConfigIdent;
    }

    public void setWeaponConfigIdent(String weaponConfigIdent)
    {
        this.weaponConfigIdent = weaponConfigIdent;
    }

    public String getPlaneTypeIdent()
    {
        return planeTypeIdent;
    }

    public void setPlaneTypeIdent(String planeTypeIdent)
    {
        this.planeTypeIdent = planeTypeIdent;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WeaponConfigurationPK other = (WeaponConfigurationPK) obj;
        if (!Objects.equals(this.weaponConfigIdent, other.weaponConfigIdent)) {
            return false;
        }
        if (!Objects.equals(this.planeTypeIdent, other.planeTypeIdent)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.weaponConfigIdent);
        hash = 31 * hash + Objects.hashCode(this.planeTypeIdent);
        return hash;
    }

    @Override
    public String toString()
    {
        return "WeaponConfigurationPK{" + "weaponConfigIdent=" + weaponConfigIdent + ", planeTypeIdent=" + planeTypeIdent + '}';
    }

}

