package cz.sm.web.prototype.springbootjsf.service;

public interface SecurityService {
    String findLoggedInUsername();
    boolean autoLogin(String userName, String password);
}
