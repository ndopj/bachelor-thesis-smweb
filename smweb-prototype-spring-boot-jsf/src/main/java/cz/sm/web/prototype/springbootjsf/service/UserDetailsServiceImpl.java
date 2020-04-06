package cz.sm.web.prototype.springbootjsf.service;

import cz.sm.web.prototype.springbootjsf.model.*;
import cz.sm.web.prototype.springbootjsf.repository.IdentityRepository;
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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired private IdentityRepository identityRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) {
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
        SimpleGrantedAuthority general = new SimpleGrantedAuthority("GENERAL");
        SimpleGrantedAuthority pilot = new SimpleGrantedAuthority("PILOT");

        Identity user = identityRepository.findByLogin(userName);
        if (user == null) throw new UsernameNotFoundException(userName);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (user instanceof SystemAdministrator) grantedAuthorities.addAll(Arrays.asList(admin, general, pilot));
        if (user instanceof General) grantedAuthorities.addAll(Arrays.asList(general));
        if (user instanceof Pilot) grantedAuthorities.addAll(Arrays.asList(pilot));
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPasswdHash(), grantedAuthorities);
    }
}
