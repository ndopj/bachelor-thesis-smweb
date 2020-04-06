package cz.sm.ng.clodwar.core.lobby.websocket.messages;

import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * Simple POJO class used to store WebSocket message data.
 * This object will be serialized and deserialized
 * based on JSON format by automatically
 * implemented Spring Boot converters and forces to use
 * String raw data.
 *
 * @author Norbert Dopjera
 */
public class WebSocketStringDataMessage
{
    private final String action;
    @JsonRawValue private final String data;

    public WebSocketStringDataMessage(String action, String data)
    {
        this.action = action;
        this.data = data;
    }

    public String getAction() { return action; }
    public String getData() { return data; }

} // WebSocketStringDataMessage
