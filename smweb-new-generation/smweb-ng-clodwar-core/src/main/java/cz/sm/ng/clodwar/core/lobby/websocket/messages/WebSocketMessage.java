package cz.sm.ng.clodwar.core.lobby.websocket.messages;

/**
 * Simple POJO class used to store WebSocket message data.
 * This object will be serialized and deserialized
 * based on JSON format by automatically
 * implemented Spring Boot converters.
 *
 * @author Norbert Dopjera
 */
public class WebSocketMessage
{
    private final String action;
    private final Object data;

    public WebSocketMessage(String action, Object data)
    {
        this.action = action;
        this.data = data;
    }

    public String getAction() { return action; }
    public Object getData() { return data; }

} // WebSocketMessage
