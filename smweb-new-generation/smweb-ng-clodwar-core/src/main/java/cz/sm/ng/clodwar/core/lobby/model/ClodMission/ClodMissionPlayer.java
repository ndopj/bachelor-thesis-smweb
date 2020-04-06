package cz.sm.ng.clodwar.core.lobby.model.ClodMission;

import com.google.gson.annotations.Expose;
import cz.sm.ng.core.gameplay.virtualpilot.VirtualPilot;
import cz.sm.ng.core.identity.models.Identity;

/**
 * Tato trida zastupuje hrace ClodMission
 *
 * @author Jaroslav Rehorka <xrehorka@fi.muni.cz>
 */
public class ClodMissionPlayer
{
    @Expose
    private final Identity identity;
    @Expose
    private final VirtualPilot virtualPilot;
    @Expose
    private final ClodMissionPlane plane;
    @Expose
    private boolean isReady;

    public ClodMissionPlayer(Identity identity, VirtualPilot virtualPilot, ClodMissionPlane plane) {
        this.identity = identity;
        this.virtualPilot = virtualPilot;
        this.plane = plane;
    }

    public ClodMissionPlane getPlane() {
        return plane;
    }

    public Identity getIdentity() {
        return identity;
    }

    public VirtualPilot getVirtualPilot() {
        return virtualPilot;
    }

    public boolean getIsReady() {
        return isReady;
    }

    public void setIsReady(boolean isReady) {
        this.isReady = isReady;
    }
}

