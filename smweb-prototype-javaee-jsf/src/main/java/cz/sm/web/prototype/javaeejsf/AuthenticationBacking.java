package cz.sm.web.prototype.javaeejsf;

import cz.sm.web.prototype.javaeejsf.model.General;
import cz.sm.web.prototype.javaeejsf.model.Identity;
import cz.sm.web.prototype.javaeejsf.model.Pilot;
import cz.sm.web.prototype.javaeejsf.model.SystemAdministrator;
import cz.sm.web.prototype.javaeejsf.repository.IdentityRepository;
import cz.sm.web.prototype.javaeejsf.security.AuthenticationUtils;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.ws.rs.core.Context;
import java.io.IOException;

import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

/**
 * This component represents JSF Managed Bean responsible
 * for authentication and registration of identities. This
 * Bean is request scoped, thus new instance of this Bean
 * will be created for each request that requires it. This
 * class is indented to be used only from JSF pages.
 *
 * @author Norbert Dopjera
 */
@Named
@RequestScoped
public class AuthenticationBacking {

    @NotEmpty
    @Size(min = 4, message = "Password must have at least 4 characters")
    private String password;

    @NotEmpty
    private String userName;

    @Inject
    @Context
    // Security context is always provided by application server.
    private SecurityContext securityContext;

    @Inject private IdentityRepository identityRepository;
    private FacesContext facesContext = FacesContext.getCurrentInstance();

    /**
     * Registers identity of provided type with credentials
     * stored in private attributes filled from JSF registration
     * page and then auto logs in newly registered identity.
     *
     * @param role
     * @throws IOException
     * @throws ServletException
     */
    public void register(String role) throws IOException, ServletException {
        Identity identity = new Identity();
        if (role.equals("ADMIN")) identity = new SystemAdministrator();
        if (role.equals("GENERAL")) identity = new General();
        if (role.equals("PILOT")) identity = new Pilot();

        identity.setPasswdHash(null);
        identity.setLogin(userName);
        try { identity.setPasswdHash(AuthenticationUtils.encodeSHA256(password)); }
        catch (Exception ignored) {} // if authEncode throws password will remain null and identityRepository save will fail
        identityRepository.save(identity);
        this.login();
    }

    /**
     * Tries to login identity with credentials stored in
     * private attributes which are filled from JSF page.
     * If login is successful also redirects external context
     * to relevant SMWeb section, i.e when pilot is logged in
     * he will be redirected to pilot section.
     */
    public void login() throws IOException, ServletException {
        String userName = this.userName;
        String password = this.password;

        // logout session user if exists. Otherwise currently logging in user wouldn't be able to impersonate session
        getHttpRequestFromFacesContext().logout();

        Credential credential = new UsernamePasswordCredential(userName, new Password(password));
        AuthenticationStatus status = securityContext.authenticate(
                getHttpRequestFromFacesContext(),
                getHttpResponseFromFacesContext(),
                withParams().credential(credential));

        switch (status) {
            case SEND_CONTINUE:
                facesContext.responseComplete();
                break;
            case SEND_FAILURE:
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", null));
                break;
            case SUCCESS:
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Login succeed", null));

                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                String contextRoot = externalContext.getRequestContextPath();
                if (isAllowedEnterSection("admin-section")) externalContext.redirect(contextRoot + "/admin/index.xhtml");
                else if (isAllowedEnterSection("general-section")) externalContext.redirect(contextRoot + "/general/index.xhtml");
                else if (isAllowedEnterSection("pilot-section")) externalContext.redirect(contextRoot + "/pilot/index.xhtml");
                else externalContext.redirect(contextRoot + "/");
                break;
            case NOT_DONE:
        }
    }

    /**
     * @return username of currently logged in identity
     */
    public String getIdentity() {
        return  (securityContext.getCallerPrincipal() != null)
                ? securityContext.getCallerPrincipal().getName()
                : null;
    }

    /**
     * Check whether currently logged in user is allowed to enter
     * provided section.
     *
     * @param section section to be checked
     * @return boolean representing result
     */
    public boolean isAllowedEnterSection(String section) {
        String userName = getIdentity();
        if (userName == null) return false;

        if (section.equals("admin-section")) return securityContext.isCallerInRole("ADMIN");
        if (section.equals("pilot-section")) return securityContext.isCallerInRole("PILOT");
        if (section.equals("general-section")) return securityContext.isCallerInRole("GENERAL");
        return false;
    }

    private HttpServletRequest getHttpRequestFromFacesContext() {
        return (HttpServletRequest) facesContext
                .getExternalContext()
                .getRequest();
    }

    private HttpServletResponse getHttpResponseFromFacesContext() {
        return (HttpServletResponse) facesContext
                .getExternalContext()
                .getResponse();
    }

    public void validateRegisterPilot(FacesContext context, UIComponent componentToValidate,
                                      Object value) throws ValidatorException {
        validateRegister(context, componentToValidate, value, "Pilot");
    }

    public void validateRegisterGeneral(FacesContext context, UIComponent componentToValidate,
                                        Object value) throws ValidatorException {
        validateRegister(context, componentToValidate, value, "General");
    }

    private void validateRegister(FacesContext context,
                                  UIComponent componentToValidate,
                                  Object value, String userType) throws ValidatorException {
        if (identityRepository.findByLogin((String) value) != null) {
            FacesMessage message =
                    new FacesMessage(userType + " s loginem - \"" + value + "\" se v databázi již nachází!");
            throw new ValidatorException(message);
        }
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserName() { return userName; }
    public void setUserName(String email) { this.userName = email; }
}
