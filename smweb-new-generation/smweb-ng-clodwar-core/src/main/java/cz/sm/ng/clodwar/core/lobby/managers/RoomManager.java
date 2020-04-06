package cz.sm.ng.clodwar.core.lobby.managers;

import cz.sm.ng.clodwar.core.lobby.model.Client;
import cz.sm.ng.clodwar.core.lobby.model.Room;
import cz.sm.ng.clodwar.core.lobby.websocket.serverendpoints.RoomEndpoint;
import cz.sm.ng.core.SideEnum;
import cz.sm.ng.core.gameplay.virtualpilot.VirtualPilot;
import cz.sm.ng.core.identity.models.Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manager hernich mistnosti, singleton
 *
 * @author Jaroslav Rehorka <xrehorka@fi.muni.cz>
 */
@Service
public class RoomManager
{
    @Autowired private ClientManager clientManager;
    @Autowired private RoomEndpoint roomEndpoint;

    private final AtomicInteger roomIDIncrement = new AtomicInteger(0);
    private Date lastRoomCleanUp;

    private static final long CLEANUP_PERIOD_MINUTES = 30;
    private static final long CLEANUP_ROOM_THRESHOLD = 180;

    private final Map<SideEnum, Set<Room>> roomsBySide = new EnumMap<>(SideEnum.class);
    private final Map<Identity, Room> roomsByOwner = new HashMap<>();
    private final Map<Integer, Room> roomsById = new HashMap<>();
    private final Map<Identity, Room> identitiesInRooms = new HashMap<>();

    private RoomManager() {
        this.lastRoomCleanUp = new Date();
        for (SideEnum side : SideEnum.values()) {
            this.roomsBySide.put(side, new HashSet<>());
        }
    }

    /**
     * Vrati mistnosti podle strany
     *
     * @param side
     * @return
     */
    public Set<Room> getRoomsBySide(SideEnum side) {
        return roomsBySide.get(side);
    }

    /**
     * Vrati mistnost podle jejiho vlastnika, null pokud zadna mistnost se
     * zadanym vlastnikem neexistuje
     *
     * @param owner
     * @return
     */
    public Room getRoomByOwner(Identity owner) {
        return roomsByOwner.get(owner);
    }

    /**
     * Vrati mistnost podle id, null pokud zadna s takovym id neexistuje
     *
     * @param id
     * @return
     */
    public Room getRoomById(int id) {
        return roomsById.get(id);
    }

    /**
     * Vrati vsechny pripojene klienty a mistnosti ve kterych jsou pripojeni
     *
     * @return
     */
    public Map<Identity, Room> getConnectedIdentites() {
        return this.identitiesInRooms;
    }

    /**
     * Zjisti jestli je identita pripojena v nejake mistnosti
     *
     * @param identity
     * @return
     */
    public boolean isClientInAnyRoom(Identity identity) {
        return this.identitiesInRooms.containsKey(identity);
    }

    /**
     * Prida novou mistnost
     *
     * @param name
     * @param description
     * @param password
     * @param capacity
     * @param creator
     * @return nove vytvorenou mistnost
     */
    public synchronized Room createRoom(String name, String description, String password, int capacity, Identity creator) throws IllegalArgumentException {
        if (this.roomsByOwner.containsKey(creator)) {
            throw new IllegalArgumentException("User already own room");
        }

        Room newRoom = new Room(this.roomIDIncrement.getAndIncrement(), name, description,
                                password, capacity, creator, clientManager);

        this.addClientToRoom(creator, newRoom);

        this.roomsBySide.get(newRoom.getRoomSide()).add(newRoom);
        this.roomsByOwner.put(creator, newRoom);
        this.roomsById.put(newRoom.getId(), newRoom);
        return newRoom;
    }

    /**
     * Odebere mistnost z pameti managera
     *
     * @param room
     */
    public synchronized void removeRoom(Room room) {/*
        if (room.hasMission()) {
            ClodWarMissionMetadata metadata = room.getMission().getMetadata();
            //DualA
            if (metadata.getTemplate().getDualBTemplate() != null) {
                //ak mala misia zverejneny dualBTemplate, odstranim ho
                ClodWarMissionGenerator.getInstance().removeAdvertisedDualBTemplateIfPresent(room.getMission().getMetadata());

                //ak uz existuje vytvorena DualB misia k tejto misiii, vymazem ju
                Room dualBRoom = findRoomWithMissionForTemplate(metadata.getTemplate().getDualBTemplate());
                if (dualBRoom != null) {
                    if (dualBRoom.getMission().getIsQueued()) {
                        //ak je vo fronte, odstranim ju z nej
                        ClodMissionQueueManager.getInstance().dequequeRoom(dualBRoom);
                    }
                    if (dualBRoom.getMissionStart() == null) {
                        //ak este nebola spustena, odpojim z nej hracov
                        for (Client c : dualBRoom.getConnectedClients()) {
                            removeClientFromRoom(c.getIdentity(), dualBRoom);
                            Session kickedSession = c.getSessionByEndpoint(RoomEndpoint.class.getName());
                            new WsMessage("opponents-left", "{}").send(kickedSession);
                        }
                    }
                    dualBRoom.setMission(null);
                }
                //DualB
            } else if (metadata.getTemplate() instanceof ClodWarTemplateB) {
                //ak bola vytvorena pre nejaky DualBTemplate, musim template vratit k zverejnenym
                ClodWarMissionGenerator.getInstance().advertiseDualBTemplate((ClodWarTemplateB) room.getMission().getMetadata().getTemplate());
            }


            //odstrani misiu z fronty, ak je vo fronte
            if (room.getMission().getIsQueued()) {
                ClodMissionQueueManager.getInstance().dequequeRoom(room);
            }
        }*/
        this.roomsBySide.get(room.getRoomSide()).remove(room);
        this.roomsByOwner.remove(room.getOwner());
        this.roomsById.remove(room.getId());
    }

    /**
     * Prida clienta do mistnosti
     *
     * @param client
     * @param room
     * @return
     */
    public synchronized boolean addClientToRoom(Identity client, Room room) {
        Client clientToAdd = clientManager.getClientByIdentity(client);
        if (clientToAdd == null) {
            return false;
        }

        room.getConnectedClients().add(clientToAdd);
        this.identitiesInRooms.put(client, room);
        clientToAdd.setRoom(room);
        return true;
    }

    /**
     * Aktualizuje noveho majitele mistnosti v seznamu mistnosti
     *
     * @param oldOwner
     * @param room
     */
    public synchronized void reloadRoomOwner(Identity oldOwner, Room room) {
        this.roomsByOwner.remove(oldOwner);
        this.roomsByOwner.put(room.getOwner(), room);
    }

    /**
     *
     * @param clientIdentity
     * @param room
     * @return true pokud je smazana mistnost ze ktere je klient odebran
     */
    public synchronized boolean removeClientFromRoom(Identity clientIdentity, Room room) {
        Client clientToRemove = clientManager.getClientByIdentity(clientIdentity);
        room.getConnectedClients().remove(clientToRemove);
        this.identitiesInRooms.remove(clientIdentity);
        room.getMissionMeta().getPilots().remove(clientIdentity);

        //zrusenie rezervacie lietadla
        /* TODO::
        if (room.hasMission()) {
            for (VirtualPilot p : room.getMission().getMetadata().getVirtualPilots()) {
                if (p.getOwner() != null && p.getOwner().getId() == clientIdentity.getId()) {
                    PlaneInstance plane = room.getMission().getMetadata().getPilotsPlane(p);
                    ClodWarMissionGenerator.getInstance().releasePlaneReservation(plane);
                }
            }
        }*/

        clientToRemove.disconnectFromRoom();
        if (room.hasMission() && room.getMission().getStartTime() == null) {
            for (Client c : room.getConnectedClients()) {
                removeClientFromRoom(c.getIdentity(), room);
                roomEndpoint.notifyUserInRoomPlayersLeft(c.getIdentity(), room);
            }
        }

        if (room.getConnectedClients().isEmpty()) {
            this.removeRoom(room);
            return true;
        }
        return false;
    }

    /**
     * Vrati rozdil od aktualniho casu v minutach
     *
     * @param toDiff
     * @return
     */
    private long getMinDiffToCurrentTime(Date toDiff) {
        return (new Date().getTime() - toDiff.getTime()) / 60000;
    }

    /**
     * Vycisti seznam mistnosti - z mistnosti starsi 180 minut odpoji vsechny
     * klienty a nasledne ji smaze. Muze byt volana pouze jednou za 30 minut
     *
     */ /*
    public void doRoomCleanUp() {
        if (this.getMinDiffToCurrentTime(this.lastRoomCleanUp) > CLEANUP_PERIOD_MINUTES) {
            this.lastRoomCleanUp = new Date();
            RoomService roomService = new RoomService();

            Map<Integer, Room> tempRoomMap = new HashMap<>(this.roomsById);
            for (Map.Entry<Integer, Room> entry : tempRoomMap.entrySet()) {
                Room roomToClean = entry.getValue();

                if (this.getMinDiffToCurrentTime(roomToClean.getCreated()) > CLEANUP_ROOM_THRESHOLD) {
                    for (Client client : roomToClean.getConnectedClients()) {
                        roomService.logOutFromRoom(client.getIdentity());
                    }
                }
            }
        }
    } */ /*

    private Room findRoomWithMissionForTemplate(ClodWarTemplateB dualBTemplate) {
        for (Room room : roomsBySide.get(dualBTemplate.getSide())) {
            if (room.hasMission() && dualBTemplate.equals(room.getMission().getMetadata().getTemplate())) {
                return room;
            }
        }
        return null;
    } */
}

