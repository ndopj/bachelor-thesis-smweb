package cz.sm.ng.clodwar.core.lobby.websocket.messages;

/**
 * Simple POJO class used to store message data to
 * user. This object will be serialized and deserialized
 * based on JSON format by automatically
 * implemented Spring Boot converters.
 *
 * @author Norbert Dopjera
 */
public class ToUserChatMessage extends FromUserChatMessage
{
    private final String sender;

    public ToUserChatMessage(String sender, String message)
    {
        super(message);
        this.sender = sender;
    }

    public String getSender() { return sender; }

} // ToUserChatMessage
