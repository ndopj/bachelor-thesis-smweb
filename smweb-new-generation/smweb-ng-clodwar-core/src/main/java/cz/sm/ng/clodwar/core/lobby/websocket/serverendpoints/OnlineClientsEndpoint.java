package cz.sm.ng.clodwar.core.lobby.websocket.serverendpoints;

import cz.sm.ng.clodwar.core.lobby.websocket.messages.WebSocketMessage;
import cz.sm.ng.core.identity.models.Identity;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Online client WebSocket controller simulating separate
 * endpoint. It uses SessionSubscribeEvent to check
 * if subscription is meant to connect to this endpoint.
 *
 * @author Norbert Dopjera
 */
@Controller
public class OnlineClientsEndpoint extends BaseEndpoint
{
    private static final String LIST = "list";
    private static final String LOGGED_IN = "logged-in";
    private static final String LOGGED_OUT = "logged-out";

    private static final List<BaseEndpoint.Client> clients = Collections.synchronizedList(new ArrayList<>());

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event)
    {
        String simpDestination = getDestinationFromRelevantEvent(event, "/user/clodwar/ws/online/");
        if (simpDestination == null)
            return;

        var user = getUserFromEvent(event);
        Identity identity = getIdentity(user);
        messagingTemplate.convertAndSend(
                simpDestination,
                new WebSocketMessage(LOGGED_IN, new BaseEndpoint.Client(identity.getId(), identity.getLogin()))
        );

        String roomId = simpDestination.substring(simpDestination.lastIndexOf('/') + 1);
        clients.add(new BaseEndpoint.Client(identity.getId(), identity.getLogin(), roomId));
        messagingTemplate.convertAndSendToUser(
                user.getName(),
                simpDestination.substring(simpDestination.indexOf('/', 1)),
                new WebSocketMessage(LIST, clients.stream()
                        .filter(client -> client.getRoomId().equals(roomId))
                        .collect(Collectors.toList())));
    }

    @MessageMapping("/hello/{roomID}")
    @SendTo("/user/clodwar/ws/online/{roomID}")
    public void greeting(Object received, SimpMessageHeaderAccessor headerAccessor) throws Exception
    {
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
        clients.removeIf(client -> {
            if (client.getId() == ident.getId()) {
                messagingTemplate.convertAndSend(
                        "/user/clodwar/ws/online/" + client.getRoomId(),
                        new WebSocketMessage(LOGGED_OUT, new BaseEndpoint.Client(ident.getId(), ident.getLogin())));
                return true;
            }
            return false;
        });
    }

} // OnlineClientsEndpoint
