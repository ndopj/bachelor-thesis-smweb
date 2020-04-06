package cz.sm.ng.core.SMWeb.modules;

import cz.sm.ng.core.SMWeb.modules.exceptions.MessengerException;
import cz.sm.ng.core.SMWeb.modules.messages.ModulesMessage;

/**
 *  ModulesMessenger
 * Interface for a inter-module messenger class.
 * It is used to send (and receive) messages among the server modules.
 * This interface abstracts from the actual technology used to deliver the messages
 * so it can be easily changed.
 *
 * A message sent using this interface should be delivered to every listener
 * which is using the same interface throughout the whole system, so there
 * should be only one active implementation if the system want's to satisfy
 * this requirement.
 *
 * @author Dejvino
 */
public interface ModulesMessenger
{


//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Sends a message to other listening modules
     *
     * @param msg Message to send
     * @throws MessengerException - vynimka nesie informaciu o zlyhanom pokuse o odoslanie spravy.
     */
    public void send(ModulesMessage msg) throws MessengerException;


    /**
     * Receives a message or returns after timeout with null
     *
     * @param timeout Maximal timeout before the method returns null if there was no message
     * @return Received message or null
     */
    public ModulesMessage receive(int timeout);


    /**
     * Closes connections and frees resources
     */
    public void close();

}

