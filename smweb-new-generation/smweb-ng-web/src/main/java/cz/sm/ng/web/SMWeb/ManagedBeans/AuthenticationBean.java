package cz.sm.ng.web.SMWeb.ManagedBeans;

import cz.sm.ng.core.identity.models.General;
import cz.sm.ng.core.identity.models.Identity;
import cz.sm.ng.core.identity.models.Pilot;
import cz.sm.ng.core.identity.models.SystemAdministrator;
import cz.sm.ng.security.ISecurityService;
import cz.sm.ng.security.SecurityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This component represents session scoped JSF managed
 * bean responsible for authentication of identities.
 * It should be accessed only from JSF pages to maintain
 * data consistency.
 *
 * @author Norbert Dopjera
 */
@Component("AuthenticationBean")
@SessionScope
public class AuthenticationBean implements Serializable
{
    public static final String SECTION_GENERAL = "general-section";
    public static final String SECTION_ADMIN = "admin-section";
    public static final String SECTION_PILOT = "pilot-section";

    private String loginForForm = "";
    private String passwordForForm = "";
    private final ISecurityService securityService;

    /** Creates a new instance of AuthenticationBean. */
    public AuthenticationBean(@Autowired SecurityServiceImpl securityService)
    {
        this.securityService = securityService;
    }

    /**
     * Tries to login identity with credentials stored in
     * private attributes which are filled from JSF page.
     * If login is successful also redirects external context
     * to relevant SMWeb section, i.e when pilot is logged in
     * he will be redirected to pilot section.
     */
    public void doLogin()
    {
        this.executeLoginAttemp(this.loginForForm, this.passwordForForm);
        String navigateResult = this.navigateAfterLoginAttemp();
        if (!navigateResult.equals("/")) redirectSession(navigateResult);
    }

    /**
     * Tries to login clodwar identity with credentials stored
     * in private attributes which are filled from clodwar JSF page.
     * If login is successful also redirects external context
     * to clodwar index.xhtml.
     */
    public void doLoginClod()
    {
        this.executeLoginAttemp(this.loginForForm, this.passwordForForm);
        String navigateResult = this.navigateAfterLoginAttemp();
        if (!navigateResult.equals("/")) redirectSession("/clodwar/index.xhtml");
    }

    /**
     * Logs out identity currently stored in security context,
     * i.e client will call this method thought JSF page and
     * he will be logged out and redirected to logout page.
     * This method only serves as proxy for spring security
     * logout url which, when redirected to, will execute logout.
     */
    public void logout()
    {
        loginForForm = "";
        passwordForForm = "";
        redirectSession("/logout");
    }

    /**
     * Logs out clodwar identity currently stored in security
     * context. Same as {@link #logout()} method of this class, expect
     * this one is called from clodwar JSF pages
     */
    public void logoutClod(){
        this.logout();
    }

    /**
     * Gets currently logged in identity, i.e returns
     * identity which is currently stored in spring security
     * context when this method is executed.
     *
     * @return Logged in identity or NULL;
     */
    public Identity getIdentity()
    {
        return securityService.findLoggedInIdentity();
    }

    /**
     * Gets currently logged in username, i.e returns
     * login for identity which is currently stored in spring security
     * context when this method is executed.
     *
     * @return Logged in identity username or NULL.
     */
    public String getIdentityLogin()
    {
        return securityService.findLoggedInUsername();
    }

    /**
     * This method returns path where user will
     * be redirected after successful login. Method
     * resolves this path from user type: ADMIN, GENERAL, PILOT
     *
     * @return path where user should be redirected
     * @throws IllegalStateException when path cannot be determined
     */
    private String navigateAfterLoginAttemp()
    {
        Identity identity = this.getIdentity();

        if (identity == null) {
            return "/";
        }
        if (identity instanceof Pilot) {
            return "/pilot/index.xhtml";
        }
        if (identity instanceof General) {
            return "/general/index.xhtml";
        }
        if (identity instanceof SystemAdministrator) {
            return "/admin/index.xhtml";
        }
        throw new IllegalStateException("Identita " + identity
                + " nie je ziadneho z typov, pre ktory dokazem 'navigovat'!");
    }

    /**
     * Tries to login identity with given credentials. If login for
     * given credentials fails then error message will be inserted
     * into faces context to indicate error on JSF page. Otherwise
     * identity represented by these credentials will be stored in
     * spring security context, i.e session which executed this method
     * will be considered as logged in since this moment.
     *
     * @param login username against which password is authenticated
     * @param password password in non hashed raw form.
     * @return Identity which was logged in or NULL
     */
    private Identity executeLoginAttemp(String login, String password)
    {
        if (!securityService.authenticate(login, password)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Špatné přihlašovací jméno nebo heslo!", null));
            return null;
        }

        Identity identity = getIdentity();
        Logger.getLogger(AuthenticationBean.class.getName())
              .log(Level.INFO, "===== Prihlasil se [{0}]!! [{1}] =====", new Object[]{ identity, new Date() });
        return identity;
    }

    /**
     * Determines if currently logged in identity is allowed
     * to enter given section. This is resolved from identity
     * type: PILOT, GENERAL, ADMIN
     *
     * @param section for which access right will be determined
     * @return boolean indicating result
     */
    public boolean isAllowedEnterSection(String section)
    {
        Identity userIdentity = securityService.findLoggedInIdentity();
        if (section.equals(SECTION_PILOT)) return rolesContainRole(userIdentity, "PILOT");
        if (section.equals(SECTION_GENERAL)) return rolesContainRole(userIdentity, "GENERAL");
        if (section.equals(SECTION_ADMIN)) return rolesContainRole(userIdentity, "ADMIN");
        return false;
    }

    /**
     * Determines if given identity can represent given role
     * One identity can represent multiple roles, i.e identity
     * SystemAdministrator represents role ADMIN and PILOT.
     *
     * @param userIdentity identity for which roles given role is checked
     * @param roleName role to check
     * @return boolean indicating result
     */
    private static boolean rolesContainRole(Identity userIdentity, String roleName)
    {
        if (roleName.equals("PILOT"))
            return (userIdentity instanceof Pilot) || (userIdentity instanceof SystemAdministrator);
        if (roleName.equals("GENERAL"))
            return (userIdentity instanceof General) || (userIdentity instanceof SystemAdministrator);
        if (roleName.equals("ADMIN"))
            return (userIdentity instanceof SystemAdministrator);
        return false;
    }

    public String getLoginForForm() { return this.loginForForm; }

    public void setLoginForForm(String name) { this.loginForForm = name; }

    public String getPasswordForForm() { return passwordForForm; }

    public void setPasswordForForm(String password) { this.passwordForForm = password; }

    private void redirectSession(String fullPath) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try { ec.redirect(ec.getRequestContextPath() + fullPath); }
        catch (IOException e) { e.printStackTrace(); }
    }

} // AuthenticationBean

