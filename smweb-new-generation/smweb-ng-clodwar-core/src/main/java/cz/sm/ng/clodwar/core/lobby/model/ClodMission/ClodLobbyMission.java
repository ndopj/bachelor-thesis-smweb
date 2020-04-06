package cz.sm.ng.clodwar.core.lobby.model.ClodMission;

import com.google.gson.annotations.Expose;
import cz.sm.ng.clodwar.core.lobby.rest.RoomService;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Tato trida reprezentuje misi, ktera je navazana na konktretni mistnost
 *
 * @author Jaroslav Rehorka <xrehorka@fi.muni.cz>
 */
public class ClodLobbyMission
{
    @Expose
    private String missionBriefing;
    @Expose
    private final Map<Integer, ClodMissionPlayer> playersByIdentID = new HashMap<>();
    @Expose
    private String status;
    @Expose
    private Integer queuePosition;
    @Expose
    private Date enqueueTime;
    @Expose
    private Date startTime;

    /*private ClodWarMissionMetadata metadata;*/


    public ClodLobbyMission() {

    }

    public String getMissionBriefing() {
        return missionBriefing;
    }

    public void setMissionBriefing(String missionBriefing) {
        this.missionBriefing = missionBriefing;
    }
/*
    public ClodWarMissionMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ClodWarMissionMetadata metadata) {
        this.metadata = metadata;
    }*/

    public Map<Integer, ClodMissionPlayer> getPlayersByIdentID() {
        return playersByIdentID;
    }

    @ExposeMethodResult("isQueued")
    public boolean getIsQueued() {
        return enqueueTime != null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals(RoomService.MISSION_START)) {
            this.startTime = new Date();
        }
        this.status = status;
    }

    public Integer getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(Integer queuePosition) {
        this.queuePosition = queuePosition;
    }

    public Date getEnqueueTime() {
        return enqueueTime;
    }

    public void setEnqueueTime(Date enqueueTime) {
        this.enqueueTime = enqueueTime;
    }

    public Date getStartTime() {
        return startTime;
    }
}

