package cz.sm.ng.clodwar.core.lobby.websocket.messages;

/**
 * Simple POJO class used to store message data from
 * user. This object will be serialized and deserialized
 * based on JSON format by automatically
 * implemented Spring Boot converters.
 *
 * @author Norbert Dopjera
 */
public class FromUserChatMessage
{
    private String message;

    public FromUserChatMessage() {}
    public FromUserChatMessage(String message) { this.message= message; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

} // FromUserChatMessage
