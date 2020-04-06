package cz.sm.web.prototype.springbootjsf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired AuthenticationManager authenticationManager;
    @Autowired UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (userDetails instanceof UserDetails) ? ((UserDetails) userDetails).getUsername() : null;
    }

    @Override
    public boolean autoLogin(String userName, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        try { authenticationManager.authenticate(usernamePasswordAuthenticationToken); }
        catch (AuthenticationException exception) { return false; }

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            logger.debug(String.format("Auto login %s successfully!", userName));
            return true;
        }
        return false;
    }
}
