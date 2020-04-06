package cz.sm.web.prototype.javaeejsf.repository;

import cz.sm.web.prototype.javaeejsf.configuration.HibernateConfig;
import cz.sm.web.prototype.javaeejsf.model.Identity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.enterprise.inject.Model;
import javax.persistence.NoResultException;
import java.util.List;

/**
 * This component is JavaEE controller providing thread safe access to database specified
 * by Hibernate configuration. No instances of this class should be created.
 * This class is singleton and its intended to be accessed through @Inject annotation.
 *
 * @author Norbert Dopjera
 */
@Model
public class IdentityRepository {

    public void save(Identity identity) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(identity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Identity findByLogin(String login) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createNamedQuery("findByLogin", Identity.class)
                          .setParameter("login", login)
                          .getSingleResult();
        } catch (NoResultException ignored) { return null; }
    }

    public List<Identity> findAllIdentities() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Identity ", Identity.class).list();
        }
    }
}
