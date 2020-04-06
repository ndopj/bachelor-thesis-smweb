package cz.sm.ng.clodwar.core.lobby.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import cz.sm.ng.clodwar.core.lobby.managers.ClientManager;
import cz.sm.ng.clodwar.core.lobby.model.ClodMission.ClodLobbyMetadata;
import cz.sm.ng.clodwar.core.lobby.model.ClodMission.ClodLobbyMission;
import cz.sm.ng.core.SideEnum;
import cz.sm.ng.core.identity.models.Identity;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Trida reprezentujici herni mistnosti
 *
 * @author Jaroslav Rehorka <xrehorka@fi.muni.cz>
 */
@JsonIgnoreProperties({"connectedClients"})
public class Room
{
    @Expose private final int id;
    @Expose private String name;
    @Expose private String description;

    @Expose private int capacity;
    @Expose private Identity owner;
    @Expose private final ClodLobbyMetadata missionMeta;
    @Expose private ClodLobbyMission mission;
    @Expose private Date created;

    private String password;
    private final Set<Client> connectedClients = new LinkedHashSet<>();

    public Room(int id, String name, String description, String password,
                int capacity, Identity owner, ClientManager clientManager) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.password = password;
        this.capacity = capacity;
        this.owner = owner;
        this.connectedClients.add(clientManager.createClient(owner));
        this.created = new Date();
        this.missionMeta = new ClodLobbyMetadata();
        this.missionMeta.setRoomCapacity(this.capacity);
        this.missionMeta.setSide(this.getRoomSide());
    }

    public SideEnum getRoomSide() {
        return this.owner.getSide();
    }

    @ExposeMethodResult("connectedTotal")
    public int getConnectedTotal() {
        return this.connectedClients.size();
    }

    @ExposeMethodResult("isLocked")
    public boolean isLocked() {
        return this.password != null && !"".equals(this.password) || hasMission();
    }

    public Set<Client> getConnectedClients() {
        return connectedClients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public Identity getOwner() {
        return owner;
    }

    public void setOwner(Identity newOwner) {
        this.owner = newOwner;
    }

    @ExposeMethodResult("hasMission")
    public boolean hasMission() {
        return this.mission != null && this.missionMeta.getMissionRequestTime() != null;
    }

    public ClodLobbyMetadata getMissionMeta() {
        return missionMeta;
    }

    public ClodLobbyMission getMission() {
        return mission;
    }

    @ExposeMethodResult("missionStartTime")
    public Date getMissionStart() {
        if (!hasMission()) {
            return null;
        }
        return this.mission.getStartTime();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        return this.id == other.id;
    }

    public void setMission(ClodLobbyMission mission) {
        this.mission = mission;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}

