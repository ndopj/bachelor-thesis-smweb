package cz.sm.ng.clodwar.core.lobby.rest;

import cz.sm.ng.clodwar.core.lobby.managers.ClientManager;
import cz.sm.ng.clodwar.core.lobby.model.Client;
import cz.sm.ng.clodwar.core.lobby.websocket.serverendpoints.RoomEndpoint;
import cz.sm.ng.core.identity.IdentityManager;
import cz.sm.ng.core.identity.exceptions.IdentityNotFoundException;
import cz.sm.ng.core.identity.models.Identity;
import cz.sm.ng.core.rest.responses.HttpRestResponse;
import cz.sm.ng.security.ISecurityService;
import io.gsonfire.GsonFireBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * REST web services controller used to provide
 * services with identities.
 *
 * @author Norbert Dopjera <ndopjera@gmail.com>
 */
@RestController
@RequestMapping("/api/clodwar/user")
public class UserService
{
    @Autowired ISecurityService securityService;
    @Autowired ClientManager clientManager;
    @Autowired IdentityManager identityManager;
    @Autowired RoomEndpoint roomEndpoint;
    @Autowired RoomService roomService;

    /**
     * Returns currently logged in user. Returned object
     * is mapped into json format by springboot internal JSON implementation.
     *
     * @return currently logged in user as json format.
     */
    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpRestResponse getCurrentUser()
    {
        Client client = clientManager.createClient(securityService.findLoggedInIdentity());
        if (client == null) {
            return new HttpRestResponse(HttpStatus.UNAUTHORIZED);
        }

        GsonFireBuilder gson = new GsonFireBuilder().enableExposeMethodResult();
        String json = gson.createGsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create().toJson(client);
        return new HttpRestResponse(HttpStatus.OK, json);
    }

    /**
     * TODO: This is mocked method !!
     */
    @RequestMapping(path = "/virtual-pilots", method = RequestMethod.GET)
    public HttpRestResponse getVirtualPilots()
    {/*
        GameProvider gameStatsProvider = new GameProvider();
        List<VirtualPilot> virtualPilots = gameStatsProvider.listAvailableVirtualPilotsOfIdentity(this.getIdentity());

        GsonFireBuilder gson = new GsonFireBuilder().enableExposeMethodResult();
        String json = gson.createGsonBuilder()
                .excludeFieldsWithoutExposeAnnotation().create().toJson(virtualPilots);

        return HttpResponse.toResponse(Response.Status.OK, json);*/
        return new HttpRestResponse(HttpStatus.OK, "[]");
    }

    /**
     * TODO: This is mocked method !!
     */
    @RequestMapping(path = "/mission-status", method = RequestMethod.GET)
    public HttpRestResponse getMissionStatus()
    {/*
        return HttpResponse.toResponse(Response.Status.OK, "{status: \"ok\"}");*/
        return new HttpRestResponse(HttpStatus.OK, "{}");
    }

    /**
     * Logs out currently authenticated identity.
     * I.E - the identity which is accessing this method.
     *
     * @return HTTPS response code identifying status
     */
    @RequestMapping(path = "", method = RequestMethod.DELETE)
    public HttpRestResponse logOut()
    {
        Client client = clientManager.getClientByIdentity(securityService.findLoggedInIdentity());
        if (client != null) {
            clientManager.disconnectClient(client);
        }

        return new HttpRestResponse(HttpStatus.OK);
    }

    /**
     * Kicks user by his given id from room in which
     * currently authenticated identity is present.
     * This method is only allowed to owner of the room.
     *
     * @param userId identifier of user to kick.
     * @return HTTP response code identifying status.
     */
    @RequestMapping(path = "/{user-id}/kick", method = RequestMethod.POST)
    public HttpRestResponse kickFromRoom(@PathVariable("user-id") final int userId)
    {
        Client owner = clientManager.getClientByIdentity(securityService.findLoggedInIdentity());
        if (owner.getRoom().getOwner().getId() != owner.getIdentity().getId()) {
            return new HttpRestResponse(HttpStatus.FORBIDDEN);
        }

        Identity clientToKickIdent;
        try {
            clientToKickIdent = identityManager.getIdentity(userId);
        } catch (IdentityNotFoundException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            return new HttpRestResponse(HttpStatus.NOT_FOUND);
        }

        Client clientToKick = clientManager.getClientByIdentity(clientToKickIdent);
        if (owner.getRoom().getConnectedClients().stream().noneMatch(
                client -> client.getIdentity() == clientToKick.getIdentity())) {
            return new HttpRestResponse(HttpStatus.NOT_FOUND);
        }

        roomEndpoint.sendKickedToUser(clientToKickIdent, owner.getRoom());
        return roomService.logOutFromRoom(clientToKickIdent);
    }

} // UserService
