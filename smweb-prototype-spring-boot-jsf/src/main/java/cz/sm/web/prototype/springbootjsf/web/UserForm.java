package cz.sm.web.prototype.springbootjsf.web;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Component
@SessionScope
public class UserForm implements Serializable {

    @NotEmpty
    private String userName;

    @NotEmpty
    @Size(min = 4, message = "Password must have at least 4 characters")
    private String password;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
