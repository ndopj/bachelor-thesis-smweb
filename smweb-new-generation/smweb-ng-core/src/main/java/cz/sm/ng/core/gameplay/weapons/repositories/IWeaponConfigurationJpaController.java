package cz.sm.ng.core.gameplay.weapons.repositories;

import cz.sm.ng.core.gameplay.weapons.models.WeaponConfiguration;
import cz.sm.ng.core.gameplay.weapons.models.WeaponConfigurationPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWeaponConfigurationJpaController extends JpaRepository<WeaponConfiguration, WeaponConfigurationPK>
{
    List<WeaponConfiguration> findAllByWeaponConfigPKPlaneTypeIdent(String planeTypeIdent);
}
