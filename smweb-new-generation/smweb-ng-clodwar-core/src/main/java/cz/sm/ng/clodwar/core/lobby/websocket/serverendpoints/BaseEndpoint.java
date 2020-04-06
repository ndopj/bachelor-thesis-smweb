package cz.sm.ng.clodwar.core.lobby.websocket.serverendpoints;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cz.sm.ng.core.identity.models.Identity;
import cz.sm.ng.core.identity.repositories.IIdentityJpaController;
import cz.sm.ng.security.ISecurityService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

import javax.persistence.criteria.From;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Optional;

/**
 * Base class for each WebSocket endpoint providing
 * operations used by all WebSocket endpoints. This
 * class is intended to be marked abstract.
 *
 * @author Norbert Dopjera
 */
public abstract class BaseEndpoint
{
    @Autowired IIdentityJpaController identityJpaController;
    @Autowired SimpMessageSendingOperations messagingTemplate;

    protected Identity getIdentity(@NotNull Principal user)
    {
        return identityJpaController
                .findByLogin(user.getName())
                .orElseThrow(() -> new IllegalStateException("Unknow user with name: " + user.getName()));
    }

    public static String getDestinationFromRelevantEvent(AbstractSubProtocolEvent event, String destinationPrefix)
    {
        GenericMessage message = (GenericMessage) event.getMessage(); // safe cast
        String simpDestination = Optional.ofNullable((String) message
                .getHeaders().get("simpDestination")) // can be null
                .orElse("");
        return (simpDestination.startsWith(destinationPrefix))
                ? simpDestination
                : null;
    }

    public static @NotNull Principal getUserFromEvent(AbstractSubProtocolEvent event)
    {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        return Optional.ofNullable(headerAccessor.getUser())
                .orElseThrow(() -> new IllegalCallerException("Unsecured session accesing websocket"));
    }

    @JsonIgnoreProperties("roomId")
    protected class Client
    {
        private final int id;
        private final String login;
        private final String roomId;

        Client(int id, String login, String roomId)
        {
            this.id = id;
            this.login = login;
            this.roomId = roomId;
        }

        Client(int id, String login) {
            this(id, login, "");
        }

        public int getId() { return id; }
        public String getLogin() { return login; }
        public String getRoomId() { return roomId; }
    } // Client

} // BaseEndpoint
