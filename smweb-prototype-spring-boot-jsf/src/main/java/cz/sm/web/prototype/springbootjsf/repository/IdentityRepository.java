package cz.sm.web.prototype.springbootjsf.repository;

import cz.sm.web.prototype.springbootjsf.model.Identity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityRepository extends JpaRepository<Identity, Long> {
    Identity findByLogin(String login);
}
