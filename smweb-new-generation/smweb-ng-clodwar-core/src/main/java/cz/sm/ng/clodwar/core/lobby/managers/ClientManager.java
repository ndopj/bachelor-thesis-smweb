package cz.sm.ng.clodwar.core.lobby.managers;

import cz.sm.ng.clodwar.core.lobby.model.Client;
import cz.sm.ng.core.SideEnum;
import cz.sm.ng.core.identity.models.Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager pripojenych klientu, singleton
 *
 * @author Jaroslav Rehorka <xrehorka@fi.muni.cz>
 */
@Service
public class ClientManager
{
    @Autowired private RoomManager roomManager;

    private final Map<Identity, Client> clientsByIdentity = new ConcurrentHashMap<>();
    private final Map<String, Client> clientsByLogin = new ConcurrentHashMap<>();
    private final Map<SideEnum, Set<Client>> clientsBySide = new EnumMap<>(SideEnum.class);

    private ClientManager() {
        for (SideEnum side : SideEnum.values()) {
            this.clientsBySide.put(side, new HashSet<>());
        }
    }

    public Client getClientByIdentity(Identity identity) {
        return (identity != null)
                ? this.clientsByIdentity.getOrDefault(identity, null)
                : null;
    }

    /**
     * Vytvori a ulozi noveho klienta, pokud jiz byl klient vytvoren novy se
     * nevytvari ale vrati se puvodni
     *
     * @param identity
     * @return
     */
    public synchronized Client createClient(Identity identity) {
        if (identity == null) {
            return null;
        }

        Client newClient;
        if (this.clientsByIdentity.containsKey(identity)) {
            newClient = this.clientsByIdentity.get(identity);
        } else {
            newClient = new Client(identity);

            this.clientsByIdentity.put(identity, newClient);
            this.clientsByLogin.put(identity.getLogin(), newClient);
            this.clientsBySide.get(identity.getSide()).add(newClient);
        }

        return newClient;
    }

    /**
     * Odstrani existujiciho clienta
     *
     * @param identity
     */
    public void removeClient(Identity identity) {
        Client client = this.clientsByIdentity.get(identity);

        if (client == null) {
            return;
        }

        this.clientsByIdentity.remove(client.getIdentity());
        this.clientsByLogin.remove(client.getIdentity().getLogin());
        this.clientsBySide.get(client.getIdentity().getSide()).remove(client);
    }

    /**
     * Odpoji klienta
     *
     * @param client
     */
    public void disconnectClient(Client client) {
        client.closeAllSessions();
        this.removeClient(client.getIdentity());
    }

    /**
     * Ulozi otevrenou session, pokud klient neexistuje vytvori jej
     *
     * @param clientIdentity
     * @param session
     * @param endpoint
     */
    public synchronized void addClientSession(Identity clientIdentity, Session session, String endpoint) {
        Client client = this.clientsByIdentity.get(clientIdentity);

        if (client == null) {
            client = this.createClient(clientIdentity);
        }

        client.addSession(endpoint, session);
    }

    /**
     * Odebere a uzavre session uzivatele, pokud jiz zadna dalsi neexistuje a
     * klient neni zaroven v zadne mistnosti pak ji smaze
     *
     * @param clientIdentity
     * @param session
     */
    public synchronized void removeClientSession(Identity clientIdentity, Session session) {
        Client client = this.clientsByIdentity.get(clientIdentity);

        if (client == null) {
            return;
        }

        client.removeSession(session);

        if (client.getOpenedSessions().isEmpty() && !roomManager.isClientInAnyRoom(clientIdentity)) {
            this.removeClient(clientIdentity);
        }
    }
}

