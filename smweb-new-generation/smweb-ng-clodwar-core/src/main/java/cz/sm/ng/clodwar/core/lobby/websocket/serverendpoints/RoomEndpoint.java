package cz.sm.ng.clodwar.core.lobby.websocket.serverendpoints;

import cz.sm.ng.clodwar.core.lobby.managers.RoomManager;
import cz.sm.ng.clodwar.core.lobby.model.Room;
import cz.sm.ng.clodwar.core.lobby.websocket.messages.WebSocketMessage;
import cz.sm.ng.clodwar.core.lobby.websocket.messages.WebSocketStringDataMessage;
import cz.sm.ng.core.identity.models.Identity;
import io.gsonfire.GsonFireBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Room WebSocket controller simulating separate
 * endpoint. It uses SessionSubscribeEvent to check
 * if subscription is meant to connect to this endpoint.
 *
 * @author Norbert Dopjera
 */
@Controller
public class RoomEndpoint extends BaseEndpoint
{
    private static final String NEW = "new";
    private static final String LIST = "list";
    private static final String CONNECTED_TOTAL = "connected-total";
    private static final String PLAYERS_LEFT = "players-left";
    private static final String KICKED = "kicked";

    @Autowired private RoomManager roomManager;
    private static final List<Client> clients = Collections.synchronizedList(new ArrayList<>());

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event)
    {
        String simpDestination = getDestinationFromRelevantEvent(event, "/user/clodwar/ws/room/");
        if (simpDestination == null)
            return;

        var user = getUserFromEvent(event);
        Identity identity = getIdentity(user);
        Set<Room> rooms = roomManager.getRoomsBySide(identity.getSide());
        GsonFireBuilder gson = new GsonFireBuilder().enableExposeMethodResult();
        String json = gson.createGsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create().toJson(rooms);

        messagingTemplate.convertAndSendToUser(
                user.getName(),
                simpDestination.substring(simpDestination.indexOf('/', 1)),
                new WebSocketStringDataMessage(LIST, json));

        String roomId = simpDestination.substring(simpDestination.lastIndexOf('/') + 1);
        clients.add(new BaseEndpoint.Client(identity.getId(), identity.getLogin(), roomId));
    }

    //@SendTo("/user/clodwar/ws/room/")
    public void notifyLobbyAboutNewRoom(Room newRoom)
    {
        GsonFireBuilder gson = new GsonFireBuilder().enableExposeMethodResult();
        String jsonNew = gson.createGsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create().toJson(newRoom);
        messagingTemplate.convertAndSend("/user/clodwar/ws/room/",
                                         new WebSocketStringDataMessage(NEW, jsonNew));
    }

    //@SendTo("/user/clodwar/ws/room/")
    public void notifyLobbyAboutRoomTotalConnections(Room changedRoom)
    {
        messagingTemplate.convertAndSend("/user/clodwar/ws/room",
                new WebSocketMessage(CONNECTED_TOTAL, new Object()
                {
                    private final int id = changedRoom.getId();
                    private final int connectedTotal = changedRoom.getConnectedTotal();
                    public int getId() { return id; }
                    public int getConnectedTotal() { return connectedTotal; }
                }));
    }

    public void notifyUserInRoomPlayersLeft(Identity identity, Room room)
    {
        messagingTemplate.convertAndSendToUser(
                identity.getLogin(),
                "/clodwar/ws/room/" + room.getId(),
                new WebSocketStringDataMessage(PLAYERS_LEFT, "{}"));
    }

    public void sendKickedToUser(Identity identity, Room room) {
        messagingTemplate.convertAndSendToUser(
                identity.getLogin(),
                "/clodwar/ws/room/" + room.getId(),
                new WebSocketStringDataMessage(KICKED, "{}")
        );
    }

    public void sendMessageToRooms(String action, String data, String... roomIds) {
        Arrays.stream(roomIds).forEach(roomId -> {
            messagingTemplate.convertAndSend("/user/clodwar/ws/room/" + roomId,
                    new WebSocketStringDataMessage(action, data));
        });
    }

    @EventListener
    public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event)
    {
        onClientLeave(event);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event)
    {
        onClientLeave(event);
    }

    private void onClientLeave(AbstractSubProtocolEvent event)
    {
        var user = getUserFromEvent(event);
        Identity ident = getIdentity(user);
        clients.removeIf(client -> client.getId() == ident.getId());
    }
}

