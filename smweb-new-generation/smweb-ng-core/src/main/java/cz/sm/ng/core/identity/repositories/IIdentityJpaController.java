package cz.sm.ng.core.identity.repositories;

import cz.sm.ng.core.identity.models.Identity;
import cz.sm.ng.core.identity.models.IdentityWithGameAccess;
import cz.sm.ng.core.identity.models.IdentityWithTS3Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IIdentityJpaController extends JpaRepository<Identity, Integer> {
    Optional<Identity> findByLogin(String login);
    Optional<IdentityWithGameAccess> findByIl2NickName(String nickname);
    Optional<IdentityWithTS3Access> findByTs3NickName(String nickname);
}

