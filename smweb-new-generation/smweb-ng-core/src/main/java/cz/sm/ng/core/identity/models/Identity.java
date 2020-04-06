package cz.sm.ng.core.identity.models;

import com.google.gson.annotations.Expose;
import cz.sm.ng.core.SideEnum;
import cz.sm.ng.core.libs.utils.HexStringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "IDENT")
@Inheritance(strategy = InheritanceType.JOINED)
//@NamedNativeQuery(name = Identity.GET_IDENTITY_BY_LOGIN, query = "select object(o) from Identity as o where o.login = :login")
public class Identity implements Serializable
{

    //////////////////////////////////////////////////////////////////////////////////
// ============= [  C O N S T A N T S  ] =========================================
//////////////////////////////////////////////////////////////////////////////////
    public static final String GET_IDENTITY_BY_LOGIN = "login";

    //////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;

    /**
     * Unikatne ID-cko tejto instancie entity v DB.
     */
    @Id
    @GeneratedValue
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

    /**
     * Cislo strany, ku ktorej identita patri. Moze byt SideEnum.NONE, ak
     * identita nepatri ziadnej strane
     */
    @Column(nullable = false)
    @Expose
    private SideEnum side = SideEnum.NONE;

    @Expose
    private String email;

    //////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////
    public String getLogin()
    {
        return this.login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    // ======================================================================================
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

// ======================================================================================
    /**
     * Vrati hashovanu podobu hesla
     *
     * @return
     */
    public String getPasswdHash()
    {
        return passwdHash;
    }

    /**
     * Nastavi heslo v hashovanej podobe - ako parameter sa ocakava SHA1 hash.
     *
     * @param passwdHash
     */
    public void setPasswdHash(String passwdHash)
    {
        this.passwdHash = passwdHash;
    }

// ======================================================================================
    /**
     * Na vstupe ocakava heslo v otvorenej podobe, do instancie uklada jeho SHA1
     * hash.
     *
     * @param plainPasswd
     */
    public void setPlainPasswd(String plainPasswd)
    {
        if (plainPasswd == null)
        {
            throw new IllegalArgumentException("plainPasswd parameter cannot be null");
        }

        this.passwdHash = HexStringUtils.getSha1String(plainPasswd);
    }

    // ======================================================================================
    public SideEnum getSide()
    {
        return side;
    }

    public void setSide(SideEnum side)
    {
        this.side = side;
    }

    // ======================================================================================
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

// ======================================================================================
    /**
     * Dve identity su povazovane za rovnake, ked maju rovnake ID-cko
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj.getClass() != this.getClass())
        {
            return false;
        }
        Identity ident = (Identity) obj;
        return (this.getId() == ident.getId());
    }

    // ======================================================================================
    @Override
    public int hashCode()
    {
        return this.id * 123;
    }

    // ======================================================================================
    @Override
    public String toString()
    {
        return this.getClass().getName() + "[" + this.getId() + "] " + this.getLogin();
    }

// ======================================================================================
}

