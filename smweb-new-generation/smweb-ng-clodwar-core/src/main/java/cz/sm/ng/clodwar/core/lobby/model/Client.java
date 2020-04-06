package cz.sm.ng.clodwar.core.lobby.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.Expose;
import cz.sm.ng.core.identity.models.Identity;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Trida reprezentujici hrace pripojeneho na ClodWar websocket - drzi si jeho
 * identitu a vsechny otevrene sessions
 *
 * @author Jaroslav Rehorka <xrehorka@fi.muni.cz>
 */
@JsonIgnoreProperties({"openedSessions", "room"})
public class Client
{
    @Expose
    private final Identity identity;
    private final Map<String, Session> openedSessions = new HashMap<>();
    private Room room;

    public Client(Identity identity) {
        this.identity = identity;
    }

    public Session getSessionByEndpoint(String endpoint) {
        return this.openedSessions.get(endpoint);
    }

    public Session addSession(String endpointClassName, Session session) {
        return this.openedSessions.put(endpointClassName, session);
    }

    public Session removeSession(Session session) {
        if (session.isOpen()) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE,
                        "Disconnected by server"));
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (Map.Entry<String, Session> e : this.openedSessions.entrySet()) {
            if (e.getValue() == session) {
                return this.openedSessions.remove(e.getKey());
            }
            String key = e.getKey();
            Object value = e.getValue();
        }

        return null;
    }

    public Identity getIdentity() {
        return this.identity;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void disconnectFromRoom() {
        this.room = null;
    }

    /**
     * Vraci vsechny sessions daneho klienta
     *
     * @return
     */
    public Set<Session> getOpenedSessions() {
        return new HashSet<>(this.openedSessions.values());
    }

    /**
     * Uzavre vsechny websocket pripojeni danemho klienta
     *
     */
    public void closeAllSessions() {
        Set<Session> sessions = new HashSet<>(openedSessions.values());
        Iterator<Session> iter = sessions.iterator();

        while (iter.hasNext()) {
            Session ses = iter.next();

            if (ses.isOpen()) {
                try {
                    Session sesObj = ses;
                    iter.remove();
                    sesObj.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE,
                            "Disconnected by server"));
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        this.openedSessions.clear();
    }
}

