package cz.sm.ng.security;

import cz.sm.ng.core.identity.models.General;
import cz.sm.ng.core.identity.models.Identity;
import cz.sm.ng.core.identity.models.Pilot;
import cz.sm.ng.core.identity.models.SystemAdministrator;
import cz.sm.ng.core.identity.repositories.IIdentityJpaController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Custom User details service providing functionality
 * to obtain user details from database with relevant
 * roles. This service is thread-safe since it only reads
 * from database.
 *
 * @author Norbert Dopjera
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

    @Autowired private IIdentityJpaController identityRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName)
    {
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
        SimpleGrantedAuthority general = new SimpleGrantedAuthority("GENERAL");
        SimpleGrantedAuthority pilot = new SimpleGrantedAuthority("PILOT");

        Identity user = identityRepository.findByLogin(userName)
                                          .orElseThrow(() -> new UsernameNotFoundException(userName));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (user instanceof SystemAdministrator) grantedAuthorities.addAll(Arrays.asList(admin, general, pilot));
        if (user instanceof General) grantedAuthorities.addAll(Arrays.asList(general));
        if (user instanceof Pilot) grantedAuthorities.addAll(Arrays.asList(pilot));
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPasswdHash(), grantedAuthorities
        );
    }

} // UserDetailsServiceImpl
