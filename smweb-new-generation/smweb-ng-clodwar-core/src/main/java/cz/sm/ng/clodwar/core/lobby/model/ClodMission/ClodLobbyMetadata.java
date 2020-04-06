package cz.sm.ng.clodwar.core.lobby.model.ClodMission;

import com.google.gson.annotations.Expose;
import cz.sm.ng.core.Position2D;
import cz.sm.ng.core.SideEnum;
import cz.sm.ng.core.clodwar.missiongenerator.ClodWarPlaneClass;
import cz.sm.ng.core.gameplay.virtualpilot.VirtualPilot;
import cz.sm.ng.core.identity.models.Identity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Tato trida zastupuje metadata potrebne k vytvoreni nove mise
 *
 * @author Jaroslav Rehorka <xrehorka@fi.muni.cz>
 */
public class ClodLobbyMetadata
{
    @Expose private ClodMissionType type;/*
    @Expose private Storage storageFrom;
    @Expose private Storage storageTo;

    @Expose private TargetObjectType bombTargetType;
    @Expose private TargetObjectType transportTargetType;

    @Expose private Sector sectorStart;
    @Expose private Sector sectorTarget;*/

    @Expose private Position2D point;
/*
    @Expose private Homebase homebaseFrom;
    @Expose private Homebase homebaseTo;*/

    @Expose private ClodWarPlaneClass aircraftClass;
    /*@Expose private ComodityGroup comodityGroup;*/

    private final Map<Identity, VirtualPilot> pilots = new HashMap<>();

    @Expose private Date missionRequestTime;
    private int RoomCapacity;
    private SideEnum side;

    public ClodLobbyMetadata() {
    }
/*
    public Storage getStorage(long id) {
        try {
            return StorageManager.getInstance().getStorage(id);
        } catch (StorageNotFoundException ex) {
            Logger.getLogger(ClodLobbyMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }*/

    public ClodWarPlaneClass getAircraftClass() {
        return aircraftClass;
    }

    public void setAircraftClass(ClodWarPlaneClass aircraftClass) {
        this.aircraftClass = aircraftClass;
    }
/*
    public Homebase getHomebase(int id) {
        HomebaseManager hbmanager = new HomebaseManager();

        return hbmanager.getHomebase(id);
    }

    public TargetObjectType getBombTargetType() {
        return bombTargetType;
    }

    public void setBombTargetType(TargetObjectType bombTargetType) {
        this.bombTargetType = bombTargetType;
    }

    public TargetObjectType getTransportTargetType() {
        return transportTargetType;
    }

    public void setTransportTargetType(TargetObjectType transportTargetType) {
        this.transportTargetType = transportTargetType;
    }*/

    public ClodMissionType getType() {
        return type;
    }

    public void setType(ClodMissionType type) {
        this.type = type;
    }
/*
    public Storage getStorageFrom() {
        return storageFrom;
    }

    public void setStorageFrom(long storageFromID) {
        this.storageFrom = this.getStorage(storageFromID);
    }

    public Storage getStorageTo() {
        return storageTo;
    }

    public void setStorageTo(long storageFromID) {
        this.storageTo = this.getStorage(storageFromID);
    }

    public Homebase getHomebaseFrom() {
        return homebaseFrom;
    }

    public void setHomebaseFrom(int homebaseFromID) {
        this.homebaseFrom = getHomebase(homebaseFromID);
    }

    public Homebase getHomebaseTo() {
        return homebaseTo;
    }

    public void setHomebaseTo(int homebaseToID) {
        this.homebaseTo = getHomebase(homebaseToID);
    }*/

    public Map<Identity, VirtualPilot> getPilots() {
        return pilots;
    }

    public Position2D getPoint() {
        return point;
    }

    public void setPoint(Position2D point) {
        this.point = point;
    }

    public Date getMissionRequestTime() {
        return missionRequestTime;
    }

    public void setMissionRequestTime(Date missionRequestTime) {
        this.missionRequestTime = missionRequestTime;
    }
/*
    public void setStorageFrom(Storage storageFrom) {
        this.storageFrom = storageFrom;
    }

    public void setStorageTo(Storage storageTo) {
        this.storageTo = storageTo;
    }

    public Sector getSectorStart() {
        return sectorStart;
    }

    public void setSectorStart(Sector sectorStart) {
        this.sectorStart = sectorStart;
    }

    public Sector getSectorTarget() {
        return sectorTarget;
    }

    public void setSectorTarget(Sector sectorTarget) {
        this.sectorTarget = sectorTarget;
    }*/

    public int getRoomCapacity() {
        return RoomCapacity;
    }

    public void setRoomCapacity(int RoomCapacity) {
        this.RoomCapacity = RoomCapacity;
    }

    public SideEnum getSide() {
        return side;
    }

    public void setSide(SideEnum side) {
        this.side = side;
    }
/*
    public ComodityGroup getComodityGroup() {
        return comodityGroup;
    }

    public void setComodityGroup(ComodityGroup comodityGroup) {
        this.comodityGroup = comodityGroup;
    }*/
}

