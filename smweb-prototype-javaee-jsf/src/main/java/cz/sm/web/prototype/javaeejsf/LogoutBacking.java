package cz.sm.web.prototype.javaeejsf;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;

/**
 * Simple JSF Managed Bean which is used by JSF pages to logout
 * currently logged in user. This Bean is request scoped, thus
 * new instance of this Bean will be created for each request
 * that requires it. This class is indented to be used only
 * from JSF pages.
 *
 * @author Norbert Dopjera
 */
@Named
@RequestScoped
public class LogoutBacking {

    public void submit() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        FacesContext.getCurrentInstance().getExternalContext().redirect(contextPath + "/");
    }
}
