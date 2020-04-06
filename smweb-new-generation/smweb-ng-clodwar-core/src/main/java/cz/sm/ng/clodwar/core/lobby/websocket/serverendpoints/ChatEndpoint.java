package cz.sm.ng.clodwar.core.lobby.websocket.serverendpoints;

import cz.sm.ng.clodwar.core.lobby.websocket.messages.FromUserChatMessage;
import cz.sm.ng.clodwar.core.lobby.websocket.messages.ToUserChatMessage;
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

/**
 * Chat WebSocket controller simulating separate
 * endpoint. It uses SessionSubscribeEvent to check
 * if subscription is meant to connect to this endpoint.
 *
 * @author Norbert Dopjera
 */
@Controller
public class ChatEndpoint extends BaseEndpoint
{
    private static final List<Client> clients = Collections.synchronizedList(new ArrayList<>());

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event)
    {
        String simpDestination = getDestinationFromRelevantEvent(event, "/user/clodwar/ws/chat/");
        if (simpDestination == null)
            return;

        var user = getUserFromEvent(event);
        Identity identity = getIdentity(user);
        String roomId = simpDestination.substring(simpDestination.lastIndexOf('/') + 1);
        clients.add(new BaseEndpoint.Client(identity.getId(), identity.getLogin(), roomId));
    }

    @MessageMapping("/clodwar/ws/chat/{roomId}")
    @SendTo("/user/clodwar/ws/chat/{roomId}")
    public WebSocketMessage roomChat(FromUserChatMessage message, SimpMessageHeaderAccessor headerAccessor)
    {
        String senderName = headerAccessor.getUser().getName(); // users in chat are always logged in
        return new WebSocketMessage(null, new ToUserChatMessage(senderName, message.getMessage()));
    }

    @MessageMapping("/clodwar/ws/chat/")
    @SendTo("/user/clodwar/ws/chat/")
    public WebSocketMessage globalChat(FromUserChatMessage message, SimpMessageHeaderAccessor headerAccessor)
    {
        return roomChat(message, headerAccessor);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionUnsubscribeEvent event)
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

} // ChatEndpoint
