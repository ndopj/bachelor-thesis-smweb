package cz.sm.ng.security;

import cz.sm.ng.core.identity.models.Identity;
import cz.sm.ng.core.identity.repositories.IIdentityJpaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom security service providing authentication
 * and services to find authenticated identity name
 * or object. This service is thread-safe, so various
 * threads of server can simultaneously access
 * authentication functionality.
 *
 * @author Norbert Dopjera
 */
@Service
public class SecurityServiceImpl implements ISecurityService
{
    @Autowired AuthenticationManager authenticationManager;
    @Autowired UserDetailsServiceImpl userDetailsService;
    @Autowired IIdentityJpaController identityJpaController;

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Override
    public String findLoggedInUsername()
    {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object userDetails = authentication.getPrincipal();
        return (userDetails instanceof UserDetails) ? ((UserDetails) userDetails).getUsername() : null;
    }

    @Override
    public Identity findLoggedInIdentity()
    {
        return identityJpaController
                .findByLogin(Optional.ofNullable(findLoggedInUsername()).orElse(""))
                .orElse(null);
    }

    /**
     * This method authenticates identity by given username
     * and raw password. For password validation it uses authentication
     * manager which is configured inside security configuration class.
     *
     * @param userName login of identity to be authenticated
     * @param password raw password of identity to be authenticated
     * @return boolean identifying result of authentication.
     */
    @Override
    public boolean authenticate(String userName, String password)
    {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            if (usernamePasswordAuthenticationToken.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                logger.debug(String.format("Auto login %s successfully!", userName));
                return true;
            }
        } catch (AuthenticationException ignored) { return false; }
        return false;
    }

} // SecurityServiceImpl
