package cz.sm.ng.core.gameplay.weapons;

import cz.sm.ng.core.gameplay.weapons.models.WeaponConfiguration;
import cz.sm.ng.core.gameplay.weapons.models.WeaponConfigurationPK;
import cz.sm.ng.core.gameplay.weapons.repositories.IWeaponConfigurationJpaController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Manager trieda pre pracu s konfiguraciami vyzbroje.
 *
 *
 *
 * @author xstokla2
 */
@Service
public class WeaponConfigurationManager
{
    /**
     * Instancia JPA controllera pre pristup do DB.
     */
    @Autowired private IWeaponConfigurationJpaController jpaController;


    /**
     * Privatny konstruktor singletonu.
     *
     */
    private WeaponConfigurationManager()
    {
    }

    /**
     * Pokusi sa dohladat konfiguraciu vyzbroje podla daneho identifikatora a pre dany typ lietadla.
     * Ak nenajde, vrati NULL!
     *
     * @param configIdent
     * @param planeTypeIdent
     * @return
     */
    public synchronized WeaponConfiguration getWeaponConfiguration(String configIdent, String planeTypeIdent)
    {
        return this.jpaController
                .findById(new WeaponConfigurationPK(configIdent, planeTypeIdent))
                .orElse(null);
    }


    public synchronized List<WeaponConfiguration> getWeaponConfigurationsForPlaneType(String planeTypeIdent)
    {
        return jpaController
                .findAllByWeaponConfigPKPlaneTypeIdent(planeTypeIdent);
    }

}

