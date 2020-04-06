package cz.sm.ng.core.SMWeb.modules.messages;

import java.io.Serializable;

/**
 * ModulesMessage
 * Basic message for inter-module communication containing just it's type and payload.
 * Other (more complex) messages can extend this class and be sent using
 * existing communication infrastructure.
 *
 * @author Dejvino
 */
public class ModulesMessage implements Serializable
{
//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////

    /**
     * Typ spravy
     */
    protected String type;

    /**
     * Payload object. Can contain messageType-specific instances.
     * Have to be instance of Serializable object, in order to have this whole class Serializable
     */
    protected Object payload;

//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////

    /**
     * Konstruktor - vyplni typ spravy.
     *
     * @param type
     */
    public ModulesMessage(String type)
    {
        this(type, null);
    }

// ======================================================================================

    /**
     * Konstruktor - vyplni typ spravy aj payload.
     *
     * @param type
     * @param payload
     */
    public ModulesMessage(String type, Object payload)
    {
        this.type = type;
        this.payload = payload;
    }

// ======================================================================================

    /**
     * Ziska typ spravy
     *
     * @return
     */
    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

// ======================================================================================

    public Object getPayload()
    {
        return this.payload;
    }

    public void setPayload(Object payload)
    {
        this.payload = payload;
    }

// ======================================================================================

}

