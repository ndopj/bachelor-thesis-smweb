package cz.sm.ng.security;

import cz.sm.ng.core.libs.utils.HexStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Custom password encoder using SHA-1 crypt.
 * Also providing matching of raw password with its
 * encoded form. This class should be kept as private as
 * possible since knowledge of used crypt can lead to security
 * leaks.
 *
 * @author Norbert Dopjera
 */
public class SHA1CryptPasswordEncoder implements PasswordEncoder
{
    @Override
    public String encode(CharSequence charSequence)
    {
        return HexStringUtils.getSha1String(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String encodedPassword)
    {
        return encode(charSequence).equals(encodedPassword);
    }

}

