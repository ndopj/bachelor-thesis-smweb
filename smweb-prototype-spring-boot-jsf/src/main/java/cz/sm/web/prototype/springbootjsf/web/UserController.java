package cz.sm.web.prototype.springbootjsf.web;

import cz.sm.web.prototype.springbootjsf.model.*;
import cz.sm.web.prototype.springbootjsf.service.SecurityService;
import cz.sm.web.prototype.springbootjsf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.io.IOException;
import java.io.Serializable;

@Component(value = "userController")
@RequestScope
public class UserController implements Serializable {

    private UserService userService;
    private SecurityService securityService;
    private UserForm userForm;

    public UserController(@Autowired UserService userService,
                          @Autowired SecurityService securityService,
                          @Autowired UserForm userForm) {
        this.userService = userService;
        this.securityService = securityService;
        this.userForm = userForm;
    }

    public void logout() { redirectSession("/logout"); }
    public String getIdentity() { return securityService.findLoggedInUsername(); }

    public void register(String role) {
        String username = userForm.getUserName();
        String password = userForm.getPassword();
        userService.save(username, password, role);
        login();
    }

    public void login() {
        String username = userForm.getUserName();
        String password = userForm.getPassword();

        if (!securityService.autoLogin(username, password)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", null));
            return;
        }

        if (isAllowedEnterSection("admin-section")) redirectSession("/admin/index.jsf");
        else if (isAllowedEnterSection("general-section")) redirectSession("/general/index.jsf");
        else if (isAllowedEnterSection("pilot-section")) redirectSession("/pilot/index.jsf");
    }

    public boolean isAllowedEnterSection(String section) {
        String userName = getIdentity();
        if (userName == null) return false;

        Identity userIdentity = userService.findByUserName(userName);
        if (section.equals("pilot-section")) return rolesContainRole(userIdentity, "PILOT");
        if (section.equals("general-section")) return rolesContainRole(userIdentity, "GENERAL");
        if (section.equals("admin-section")) return rolesContainRole(userIdentity, "ADMIN");
        return false;
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
        if (userService.findByUserName((String) value) != null) {
            FacesMessage message =
                    new FacesMessage(userType + " s loginem - \"" + value + "\" se v databázi již nachází!");
            throw new ValidatorException(message);
        }
    }

    private boolean rolesContainRole(Identity userIdentity, String roleName) {
        if (roleName.equals("PILOT"))
            return (userIdentity instanceof Pilot) || (userIdentity instanceof SystemAdministrator);
        if (roleName.equals("GENERAL"))
            return (userIdentity instanceof General) || (userIdentity instanceof SystemAdministrator);
        if (roleName.equals("ADMIN"))
            return (userIdentity instanceof SystemAdministrator);
        return false;
    }

    private void redirectSession(String fullPath) {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try { ec.redirect(ec.getRequestContextPath() + fullPath); }
        catch (IOException e) { e.printStackTrace(); }
    }
}
