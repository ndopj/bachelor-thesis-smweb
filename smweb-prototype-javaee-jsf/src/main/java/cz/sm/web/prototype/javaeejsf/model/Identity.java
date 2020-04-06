package cz.sm.web.prototype.javaeejsf.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(name = "findByLogin", query = "SELECT u FROM Identity u WHERE u.login = :login"),
        @NamedQuery(name = "findAllIdentities", query = "SELECT u FROM Identity u")
})
@Table(name = "IDENT")
@Inheritance(strategy = InheritanceType.JOINED)
public class Identity implements Serializable {

    public static final String GET_IDENTITY_BY_LOGIN = "login";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private int id;

    @Column(nullable = false, unique = true)
    @Expose
    private String login;

    @Column(nullable = false)
    private String passwdHash;

    @Expose
    private String email;

    public String getLogin() { return this.login; }
    public void setLogin(String login) { this.login = login; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPasswdHash() { return passwdHash; }
    public void setPasswdHash(String passwdHash) { this.passwdHash = passwdHash; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email;}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Identity ident = (Identity) obj;
        return (this.getId() == ident.getId());
    }

    @Override
    public int hashCode() { return this.id * 123; }

    @Override
    public String toString() { return this.getClass().getName() + "[" + this.getId() + "] " + this.getLogin(); }

}
