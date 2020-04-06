package cz.sm.web.prototype.javaeejsf;

import cz.sm.web.prototype.javaeejsf.model.General;
import cz.sm.web.prototype.javaeejsf.model.Identity;
import cz.sm.web.prototype.javaeejsf.model.SystemAdministrator;
import cz.sm.web.prototype.javaeejsf.repository.IdentityRepository;
import cz.sm.web.prototype.javaeejsf.security.AuthenticationUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Simple servlet context listener that is set up to listen for context
 * initialized event. This listener is used to save default registered
 * identities into database each time application is deployed on server.
 *
 * @author Norbert Dopjera
 */
@WebListener
public class WebContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        IdentityRepository identityDao = new IdentityRepository();

        SystemAdministrator admin = new SystemAdministrator();
        admin.setLogin("admin");
        try { admin.setPasswdHash(AuthenticationUtils.encodeSHA256("admin")); }
        catch (UnsupportedEncodingException | NoSuchAlgorithmException ignored) { return; }
        admin.setEmail("admin@admin.com");
        identityDao.save(admin);

        General general = new General();
        general.setLogin("general");
        try { general.setPasswdHash(AuthenticationUtils.encodeSHA256("general")); }
        catch (UnsupportedEncodingException | NoSuchAlgorithmException ignored) { return; }
        general.setEmail("general@general.com");
        identityDao.save(general);

        List<Identity> identities = identityDao.findAllIdentities();
        identities.forEach(s -> System.out.println(s.getLogin()));
    }
}
