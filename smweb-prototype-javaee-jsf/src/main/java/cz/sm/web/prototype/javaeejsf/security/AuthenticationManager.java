package cz.sm.web.prototype.javaeejsf.security;

import cz.sm.web.prototype.javaeejsf.model.General;
import cz.sm.web.prototype.javaeejsf.model.Identity;
import cz.sm.web.prototype.javaeejsf.model.Pilot;
import cz.sm.web.prototype.javaeejsf.model.SystemAdministrator;
import cz.sm.web.prototype.javaeejsf.repository.IdentityRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Authentication manager providing implementation of credentials
 * validation specified by new JavaEE 8 security specification. This
 * class will be internally used by security context to authenticate
 * given credentials.
 *
 * @author Norbert Dopjera
 */
@ApplicationScoped
public class AuthenticationManager implements IdentityStore {

    @Inject IdentityRepository identityRepository;

    /**
     * Validates if given credentials are already present inside
     * identities database with same encoded password. This method
     * also ensures that security context will be aware of identity
     * roles if validation is successful.
     *
     * @param credential credential to be validated
     * @return Data used by security context to determine result
     *         of validation and identity roles
     */
    @Override
    public CredentialValidationResult validate(Credential credential) {
        UsernamePasswordCredential login = (UsernamePasswordCredential) credential;
        Identity matchingUser = identityRepository.findByLogin(login.getCaller());

        if (matchingUser == null) throw new IllegalArgumentException();
        try {
            if (!matchingUser.getPasswdHash().equals(AuthenticationUtils.encodeSHA256(login.getPasswordAsString())))
                return CredentialValidationResult.NOT_VALIDATED_RESULT;
        } catch (Exception e) { return CredentialValidationResult.NOT_VALIDATED_RESULT; }

        Set<String> grantedAuthorities = new HashSet<>();
        if (matchingUser instanceof SystemAdministrator) grantedAuthorities.addAll(Arrays.asList("ADMIN", "GENERAL", "PILOT"));
        if (matchingUser instanceof General) grantedAuthorities.addAll(Arrays.asList("GENERAL"));
        if (matchingUser instanceof Pilot) grantedAuthorities.addAll(Arrays.asList("PILOT"));
        return new CredentialValidationResult(login.getCaller(), grantedAuthorities);
    }

    /**
     * Since there can be more identity stores active at
     * same time, priority for each needs to be provided.
     *
     * @return priority number
     */
    @Override
    public int priority() { return 10; }

    @Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(ValidationType.VALIDATE, ValidationType.PROVIDE_GROUPS);
    }
}
