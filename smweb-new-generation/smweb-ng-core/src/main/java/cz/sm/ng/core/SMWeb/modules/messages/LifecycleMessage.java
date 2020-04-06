package cz.sm.ng.core.SMWeb.modules.messages;

/**
 * Tato trida reprezentuje zpravu tykajici se zmeny se zivotniho cyklu.
 *
 * @author Jaroslav Dufek
 */
public class LifecycleMessage extends ModulesMessage
{
//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////

    /**
     * Typ zpravy.
     */
    public static final String TYPE_IDENT = "LifecycleMessage";

    /**
     * Login uzivatele, ktereho se zmena zivotniho cyklu tyka.
     */
    private final String identityLogin;

    /**
     * Nazev zivotniho cyklu a jeho aktualniho stavu.
     */
    private final String state;

//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////

    /**
     * @param identityLogin login identity, ktere se zmena tyka
     * @param state nazev zivoniho cyklu a aktualniho stavu
     */
    public LifecycleMessage(String identityLogin, String state)
    {
        super(TYPE_IDENT);
        this.identityLogin = identityLogin;
        this.state = state;
    }

    /**
     * @return the identityLogin
     */
    public String getIdentityLogin() {
        return identityLogin;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

}

