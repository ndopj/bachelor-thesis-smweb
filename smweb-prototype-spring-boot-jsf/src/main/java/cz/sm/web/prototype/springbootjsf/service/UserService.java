package cz.sm.web.prototype.springbootjsf.service;

import cz.sm.web.prototype.springbootjsf.model.Identity;

public interface UserService {
    void save(String username, String password, String role);
    Identity findByUserName(String userName);
}
