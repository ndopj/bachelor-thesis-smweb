package cz.sm.ng.clodwar.core.lobby.rest;

import cz.sm.ng.clodwar.core.lobby.managers.ClientManager;
import cz.sm.ng.clodwar.core.lobby.managers.RoomManager;
import cz.sm.ng.clodwar.core.lobby.model.Client;
import cz.sm.ng.clodwar.core.lobby.model.Room;
import cz.sm.ng.clodwar.core.lobby.rest.messages.RoomDescriptionBody;
import cz.sm.ng.clodwar.core.lobby.websocket.serverendpoints.RoomEndpoint;
import cz.sm.ng.core.identity.models.Identity;
import cz.sm.ng.core.rest.responses.HttpRestResponse;
import cz.sm.ng.security.ISecurityService;
import io.gsonfire.GsonFireBuilder;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST web services controller used to provide
 * services with ClodWar Lobby application rooms.
 *
 * @author Norbert Dopjera
 */
@RestController
@RequestMapping("/api/clodwar/room")
public class RoomService
{
    public static final String UPDATED = "updated";
    private static final String DELETED = "deleted";
    private static final String PLAYER_UPDATED = "player-updated";

    public static final String MISSION_START = "mission-start";
    public static final String MISSION_PAIRING_INFO = "mission-pairing-info";
    public static final String MISSION_QUEUE_INFO = "mission-queue-info";

    @Autowired private ISecurityService securityService;
    @Autowired private RoomManager roomManager;
    @Autowired private ClientManager clientManager;
    @Autowired private RoomEndpoint roomEndpoint;

    /**
     * @return data of room in which currently authenticated
     *         identity is present.
     */
    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpRestResponse getRoomData()
    {
        //RoomManager.getInstance().doRoomCleanUp(); // Norbert Dopjera TODO: not implemented.
        Client client = clientManager.getClientByIdentity(securityService.findLoggedInIdentity());
        if (client == null) {
            return new HttpRestResponse(HttpStatus.OK);
        }

        Room clientRoom = client.getRoom();
        if (clientRoom == null && roomManager.isClientInAnyRoom(this.getIdentity())) {
            client.setRoom(roomManager.getConnectedIdentites().get(this.getIdentity()));
            clientRoom = client.getRoom();
        } else if (clientRoom == null) {
            return new HttpRestResponse(HttpStatus.FORBIDDEN);
        }

        GsonFireBuilder gson = new GsonFireBuilder().enableExposeMethodResult();
        String json = gson.createGsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create().toJson(clientRoom);

        if (clientRoom.getOwner().getId() == client.getIdentity().getId()) {
            JSONObject object = new JSONObject(json);
            object.put("password", clientRoom.getPassword());
            json = object.toString();
        }
        return new HttpRestResponse(HttpStatus.OK, json);
    }

    /**
     * Creates new room with parameters sent in REST HTTP
     * request body. If authenticated identity already has
     * room, new room will not be created.
     *
     * @param roomDescription instance into which JSON message
     *                        will be deserialized.
     * @return HTTP request code identifying status.
     */
    @RequestMapping(path = "", method = RequestMethod.POST)
    public HttpRestResponse createNewRoom(@RequestBody RoomDescriptionBody roomDescription)
    {
        Room newRoom = roomManager.createRoom(roomDescription.getName(),
                                              roomDescription.getDescription(),
                                              roomDescription.getPassword(),
                                              roomDescription.getCapacity(),
                                              this.getIdentity());
        roomEndpoint.notifyLobbyAboutNewRoom(newRoom);
        return new HttpRestResponse(HttpStatus.OK);
    }

    /**
     * Allows identity to authorize into room if this identity
     * not currently present in other room and passwords match
     * if password is required
     *
     * @param data Map into which REST HTTP JSON message will be deserialized.
     * @param roomId identifier of room to authorize
     * @return HTTP request code identifying status.
     */
    @RequestMapping(path = "/{room-id}/authorize", method = RequestMethod.POST)
    public HttpRestResponse authorizeToRoom(@PathVariable("room-id") final int roomId,
                                            @RequestBody Map<String, String> data)
    {
        LoggerFactory.getLogger(this.getClass()).info("Sent password: " + data.get("password"));
        Room room = roomManager.getRoomById(roomId);
        Identity identity = this.getIdentity();
        if (room == null) {
            return new HttpRestResponse(HttpStatus.NOT_FOUND);
        }

        if (room.getCapacity() == room.getConnectedTotal() || room.hasMission()
                || roomManager.isClientInAnyRoom(identity)) {
            return new HttpRestResponse(HttpStatus.FORBIDDEN);
        }

        String password = data.getOrDefault("password", "");
        if ((room.getPassword() != null && !"".equals(room.getPassword()) && !room.getPassword().equals(password))
                || room.getRoomSide() != identity.getSide()) {
            return new HttpRestResponse(422, "Unprocessable Entity", null);
        }

        roomManager.addClientToRoom(identity, room);
        roomEndpoint.notifyLobbyAboutRoomTotalConnections(room);
        return new HttpRestResponse(HttpStatus.OK);
    }

    /**
     * Logs out currently authenticated identity from room where it
     * is present. If identity is last owner of the room then the room
     * will be destroyed. If identity is second last it will became owner
     * of the room.
     *
     * @return HTTP request code identifying status.
     */
    @RequestMapping(path = "", method = RequestMethod.DELETE)
    public HttpRestResponse logOutFromRoom()
    {
        return this.logOutFromRoom(this.getIdentity());
    }

    /**
     * @see RoomService::logOutFromRoom()
     * @return HTTP request code identifying status.
     */
    public HttpRestResponse logOutFromRoom(Identity ident)
    {
        Client client = clientManager.getClientByIdentity(ident);
        if (client == null || client.getRoom() == null) {
            return new HttpRestResponse(422, "Unprocessable Entity", null);
        }

        Room clientRoom = client.getRoom();
        if (clientRoom.getRoomSide() != ident.getSide()
                && !clientRoom.getConnectedClients().contains(client)) {
            return new HttpRestResponse(422, "Unprocessable Entity", null);
        }

        boolean ownerChanged = willOwnerChange(clientRoom, ident);
        GsonFireBuilder gson = new GsonFireBuilder().enableExposeMethodResult();
        String jsonToDelete = gson.createGsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create().toJson(clientRoom);

        if (roomManager.removeClientFromRoom(ident, clientRoom) || ownerChanged) {
            roomEndpoint.sendMessageToRooms((ownerChanged) ? UPDATED : DELETED,
                    jsonToDelete, "", Integer.toString(clientRoom.getId()));
        }

        if (clientRoom.getConnectedTotal() > 0) {
            //this.sendConnectedTotalChangedInfo(clientRoom);
        }
        return new HttpRestResponse(HttpStatus.OK);
    }

    private Identity getIdentity()
    {
        return securityService.findLoggedInIdentity();
    }

    private boolean willOwnerChange(Room room, Identity lastOwner)
    {
        if (room.getOwner().getId() == lastOwner.getId() && room.getConnectedTotal() > 1) {
            for (var clientInRoom : room.getConnectedClients()) {
                if (clientInRoom.getIdentity().getId() != lastOwner.getId()) {
                    room.setOwner(clientInRoom.getIdentity());
                    roomManager.reloadRoomOwner(lastOwner, room);
                    return true;
                }
            }
        }
        return false;
    }

} // RoomService
