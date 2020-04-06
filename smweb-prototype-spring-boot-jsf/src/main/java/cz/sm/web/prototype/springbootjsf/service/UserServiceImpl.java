package cz.sm.web.prototype.springbootjsf.service;

import cz.sm.web.prototype.springbootjsf.model.*;
import cz.sm.web.prototype.springbootjsf.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private IdentityRepository identityRepository;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(String username, String password, String role) {
        Identity identity = new Identity();
        if (role.equals("ADMIN")) identity = new SystemAdministrator();
        if (role.equals("GENERAL")) identity = new General();
        if (role.equals("PILOT")) identity = new Pilot();

        identity.setLogin(username);
        identity.setPasswdHash(bCryptPasswordEncoder.encode(password));
        identityRepository.save(identity);
    }

    @Override
    public Identity findByUserName(String userName) {
        return identityRepository.findByLogin(userName);
    }
}
