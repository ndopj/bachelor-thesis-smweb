package cz.sm.ng.clodwar.core.lobby.model.ClodMission;

import com.google.gson.annotations.Expose;
import cz.sm.ng.core.gameplay.weapons.WeaponConfigurationManager;
import cz.sm.ng.core.gameplay.weapons.models.WeaponConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Tato trida zastupuje letadlo pro hrace ClodMissionPlayer
 *
 * @author Jaroslav Rehorka <xrehorka@fi.muni.cz>
 */
public class ClodMissionPlane
{
    @Expose private final String type;
    @Expose private final String task;
    @Expose private final String homebaseName;
    @Expose private int fuel;
    @Expose private String weaponSet;
    @Expose private final List<String> weaponSets;

    private List<WeaponConfiguration> weaponConfigs;
    private WeaponConfiguration weaponConfig;

    @Autowired WeaponConfigurationManager weaponConfigurationManager;

    public ClodMissionPlane(String type, String task, String homebase, String equipment) {
        this.type = type;
        this.task = task;
        this.homebaseName = homebase;
        this.fuel = 100;
        this.weaponSets = ínitializeWeaponSets();
    }

    public WeaponConfiguration getWeaponConfig() {
        return weaponConfig;
    }

    public String getType() {
        return type;
    }

    public String getTask() {
        return task;
    }

    public String getHomebaseName() {
        return homebaseName;
    }

    public String getWeaponSet() {
        return weaponSet;
    }

    public List<String> getWeaponSets() {
        return weaponSets;
    }

    public int getFuel() {
        return fuel;
    }

    public void setWeaponSet(String weaponSet) {
        this.weaponSet = weaponSet;
        this.weaponConfig = weaponConfigs.stream().filter(c -> c.getName().equals(weaponSet)).findFirst().get();
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    private List<String> ínitializeWeaponSets() {
        List<String> result = new ArrayList<>();
        weaponConfigs = weaponConfigurationManager.getWeaponConfigurationsForPlaneType(type);

        if (!weaponConfigs.isEmpty()) {
            this.weaponConfig = weaponConfigs.get(0);
            this.weaponSet = this.weaponConfig.getName();
        }

        for (WeaponConfiguration wc : weaponConfigs) {
            result.add(wc.getName());
        }
        return result;
    }
}

