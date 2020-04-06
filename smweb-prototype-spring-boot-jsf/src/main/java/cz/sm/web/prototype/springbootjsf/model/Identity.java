package cz.sm.web.prototype.springbootjsf.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "IDENT")
@Inheritance(strategy = InheritanceType.JOINED)
//@NamedNativeQuery(name = Identity.GET_IDENTITY_BY_LOGIN, query = "select object(o) from Identity as o where o.login = :login")
public class Identity implements Serializable {

    public static final String GET_IDENTITY_BY_LOGIN = "login";
    private static final long serialVersionUID = 1L;

    /**
     * Unikatne ID-cko tejto instancie entity v DB.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private int id;

    /**
     * Login
     */
    @Column(nullable = false, unique = true)
    @Expose
    private String login;

    /**
     * Heslo v SHA1 hashovanej podobe
     */
    @Column(nullable = false)
    private String passwdHash;

    @Expose
    private String email;

    public String getLogin() { return this.login; }
    public void setLogin(String login) { this.login = login; }

    // ======================================================================================
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

// ======================================================================================
    /**
     * Vrati hashovanu podobu hesla
     *
     * @return
     */
    public String getPasswdHash() { return passwdHash; }

    /**
     * Nastavi heslo v hashovanej podobe - ako parameter sa ocakava BCRYPT hash.
     *
     * @param passwdHash
     */
    public void setPasswdHash(String passwdHash) { this.passwdHash = passwdHash; }

    // ======================================================================================
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email;}

// ======================================================================================
    /**
     * Dve identity su povazovane za rovnake, ked maju rovnake ID-cko
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Identity ident = (Identity) obj;
        return (this.getId() == ident.getId());
    }

    // ======================================================================================
    @Override
    public int hashCode() { return this.id * 123; }

    // ======================================================================================
    @Override
    public String toString() { return this.getClass().getName() + "[" + this.getId() + "] " + this.getLogin(); }

// ======================================================================================
}
