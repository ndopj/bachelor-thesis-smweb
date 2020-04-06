package cz.sm.ng.security;

import cz.sm.ng.core.identity.models.Identity;

/**
 * Interface for custom implemented
 * security service.
 *
 * @author Norbert Dopjera
 */
public interface ISecurityService
{
    String findLoggedInUsername();
    boolean authenticate(String userName, String password);
    Identity findLoggedInIdentity();

} // ISecurityService
