package cz.sm.ng.web.SMWeb.ManagedBeans;

import cz.sm.ng.core.SideEnum;
import cz.sm.ng.core.identity.models.General;
import cz.sm.ng.core.identity.models.Identity;
import cz.sm.ng.core.identity.models.IdentityWithGameAccess;
import cz.sm.ng.core.identity.models.IdentityWithTS3Access;
import cz.sm.ng.core.identity.models.Pilot;
import cz.sm.ng.core.identity.repositories.IIdentityJpaController;
import cz.sm.ng.security.SecurityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import java.io.IOException;
import java.io.Serializable;

/**
 * This component represents view scoped JSF managed
 * bean responsible for registration of identities.
 * It should be accessed only from JSF pages to maintain
 * data consistency.
 *
 * @author Norbert Dopjera
 */
// Some IDEs may complain that this class should have
// constructor without parameters. Just ignore that.
@Component(value = "RegistrationBean")
@ViewScoped
public class RegistrationBean implements Serializable
{
    private String passwd = "";
    private String passwdCheck = "";
    private Pilot pilot = new Pilot();
    private final IIdentityJpaController identJC;
    private final SecurityServiceImpl securityService;
    private SelectItem[] sideEnumValues;

    /** Creates a new instance of RegistrationBean */
    public RegistrationBean(@Autowired IIdentityJpaController identJC,
                            @Autowired SecurityServiceImpl securityService)
    {
        sideEnumValues = getSideValues();
        this.identJC = identJC;
        this.securityService = securityService;
    }

    /**
     * Gets all possible player side values
     * as array of SelectItem class used in JSF pages.
     * @return SelectItem array containing all side enum values.
     */
    private SelectItem[] getSideValues()
    {
        SelectItem[] items = new SelectItem[SideEnum.values().length];
        int i = 0;
        for (SideEnum g : SideEnum.values()) {
            items[i++] = new SelectItem(g, g.getLabel());
        }
        return items;
    }

    /**
     * Method triggered when clicked registerGeneral button,
     * tries to create new General. General can be registered
     * only from admin section, thus this method will not autologin
     * after registration
     */
    public void registerGeneral()
    {
        General gen = new General();
        gen.setEmail(pilot.getEmail());
        gen.setLogin(pilot.getLogin());
        gen.setPlainPasswd(passwd);
        gen.setSide(pilot.getSide());
        identJC.save(gen);
    }

    /**
     * Register identity with credentials stored in private
     * attributes filled from JSF registration page and then
     * redirects external context(client browser) to specified
     * path.
     *
     * @param path where user will be redirected after successful registration.
     * @return boolean indicating registration success
     */
    private boolean register(String path) {
        if (pilot == null || pilot.getLogin() == null || "".equals(pilot.getLogin())
                || pilot.getIl2NickName() == null || "".equals(pilot.getIl2NickName()) ) {
            return false;
        }

        pilot.setPlainPasswd(passwd);
        identJC.save(pilot);
        System.out.println("Perzistovana entita pilota: " + pilot);
        securityService.authenticate(pilot.getLogin(), passwd);
        redirectSession(path);
        return true;
    }

    /**
     * Method triggered when clicked registerPilot button,
     * tries to create new Pilot. Pilot can be created only
     * from public pages, thus this method will autologin
     * newly created pilot and redirect context.
     *
     * @return String containing status of registration.
     */
    public String registerPilot()
    {
        return (register("/pilot/index.xhtml"))
                ? "pilotRegistrationSuccess"
                : null;
    }

    /**
     * Method triggered when clicked registerPilot button,
     * tries to create new Pilot. Pilot can be created only
     * from public pages, thus this method will autologin
     * newly created pilot and redirect context. This method
     * is intended to be called from clodwar JSF pages.
     */
    public void registerPilotClod() {
        register("/clodwar/index.xhtml");
    }

    /**
     * This method is used from JSF pages to validate that login passed into
     * textfield is not already present in database of identities when registering
     * new identities.
     *
     * @param context JSF context, not used.
     * @param component JSF component, not used.
     * @param value Login that will be validated
     * @throws ValidatorException exception to indicate validation fail for JSF pages.
     */
    public void validateLogin(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        Identity p = identJC.findByLogin((String) value).orElse(null);
        String type = (p instanceof General) ? "General" : "Pilot";
        if (p != null) {
            FacesMessage message =
                    new FacesMessage(type + " s loginem - \"" + value + "\" se v databázi již nachází!");
            throw new ValidatorException(message);
        }
    }

    /**
     * This method is used from JSF pages to validate that IL2 nick passed into
     * textfield is not already present in database of identities when registering
     * new pilot.
     *
     * @param context JSF context, not used.
     * @param component JSF component, not used.
     * @param value IL2 nick that will be validated
     * @throws ValidatorException exception to indicate validation fail for JSF pages.
     */
    public void validateNick(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        IdentityWithGameAccess identityWithGameAccess = identJC.findByIl2NickName((String) value).orElse(null);
        if (identityWithGameAccess != null) {
            FacesMessage message =
                    new FacesMessage("Pilot s nickem - \"" + value + "\" se v databázi již nachází!");
            throw new ValidatorException(message);
        }
    }

    /**
     * This method is used from JSF pages to validate that teamspeak nick passed into
     * textfield is not already present in database of identities when registering
     * new pilot.
     *
     * @param context JSF context, not used.
     * @param component JSF component, not used.
     * @param value TS3 nick that will be validated
     * @throws ValidatorException exception to indicate validation fail for JSF pages.
     */
    public void validateTs3Nick(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        IdentityWithTS3Access identityWithTS3Access = identJC.findByTs3NickName((String) value).orElse(null);
        if (identityWithTS3Access != null) {
            FacesMessage message =
                    new FacesMessage("Pilot s TS3 nickem - \"" + value + "\" se v databázi již nachází!");
            throw new ValidatorException(message);
        }
    }

    /**
     * This method is used from JSF pages to validate that strings inside
     * password and confirm password fields are same.
     *
     * @param context JSF context, autofilled by JSF page
     * @param component JSF component, autofilled by JSF page
     * @param value password inside confirm password JSF page field
     * @throws ValidatorException exception to indicate validation fail for JSF pages.
     */
    public void validatePasswds(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        // Compare values of password in password and confirm password JSF page fields.
        String passwordId = (String) component.getAttributes().get("passwordId");
        UIInput passwordInput = (UIInput) context.getViewRoot().findComponent(passwordId);
        String password = (String) passwordInput.getValue();
        String confirm = (String) value;

        if (!(confirm).equals(password)) {
            FacesMessage message = new FacesMessage("Hesla se neshodují!");
            throw new ValidatorException(message);
        }
    }

    private void redirectSession(String fullPath) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try { ec.redirect(ec.getRequestContextPath() + fullPath); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public Pilot getPilot()
    {
        return pilot;
    }

    public void setPilot(Pilot ident)
    {
        this.pilot = ident;
    }

    public String getPasswd()
    {
        return passwd;
    }

    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }

    public String getPasswdCheck()
    {
        return passwdCheck;
    }

    public void setPasswdCheck(String passwdCheck)
    {
        this.passwdCheck = passwdCheck;
    }

    public SelectItem[] getSideEnumValues()
    {
        return sideEnumValues;
    }

    public void setSideEnumValues(SelectItem[] sideEnumValues)
    {
        this.sideEnumValues = sideEnumValues;
    }

} // RegistrationBean

