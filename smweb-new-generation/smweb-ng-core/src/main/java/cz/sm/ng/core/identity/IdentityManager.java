package cz.sm.ng.core.identity;

import cz.sm.ng.core.LifeCycles.Identity.IdentityLifecycle;
import cz.sm.ng.core.SideEnum;
import cz.sm.ng.core.exceptions.PreexistingEntityException;
import cz.sm.ng.core.identity.exceptions.IdentityNotFoundException;
import cz.sm.ng.core.identity.exceptions.InvalidCredentialException;
import cz.sm.ng.core.identity.models.Identity;
import cz.sm.ng.core.identity.models.IdentityWithGameAccess;
import cz.sm.ng.core.identity.models.IdentityWithTS3Access;
import cz.sm.ng.core.identity.models.Pilot;
import cz.sm.ng.core.identity.repositories.IIdentityJpaController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IdentityManager
{


//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////


    @Autowired
    private IIdentityJpaController controller;


//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////



    /**
     * Konstruktor managera.
     *
     * 22.11.2014 zmeneny na PRIVATE, aby si z neho nemohol ktokolvek ziskavat instanciu. Miesto nej sa ma pouzivat
     * singleton globalna instancia.
     *
     * Pozn.: 12.3.2010 - parameter strany je zrejme pre tohoto managera nepotrebny, pretoze on
     * zo svojej podstaty musi pracovat nad kompletnym zoznamom identit, nie len nad identitami jednej
     * strany.
     *
     *
     */
    private IdentityManager()
    {
    }

// ======================================================================================


    /**
     * Ziska z DB instanciu identity podla ID-cka a vrati ju ako objekt
     * co najspecifickejsieho typu (cize napr. General, Pilot, ..., Identity).
     * @param id
     * @return
     */
    public Identity getIdentity(int id) throws IdentityNotFoundException
    {
        return this.controller
                .findById(id)
                .orElseThrow(() -> new IdentityNotFoundException("Identity not found for id = '" + id + "'!" ));
    }

// ======================================================================================

    /**
     * Ziska z DB instanciu identity podla loginu a vrati ju ako objekt
     * co najspecifickejsieho typu (cize napr. General, Pilot, ..., Identity).
     *
     * @param id
     * @return
     */
    public Identity getIdentity(String login) throws IdentityNotFoundException
    {
        return this.controller
                .findByLogin(login)
                .orElseThrow(() -> new IdentityNotFoundException("Identity not found for login = '" + login + "'!" ));
    }

// ======================================================================================


    /**
     * Ziska z DB instanciu Pilot-a podla jeho nickname (= loginu).
     *
     * ZMENA SPECIFIKACIE!!!
     *
     * NEBUDE podporovat konfiguracnu volbu "autoCreateNewPilots",
     * ktora by rozhodovala o vysledku metody, ked bude zadany
     * zatial neexistujuci nick. Pokial by bola volba povolena,
     * bol by novy Pilot automaticky zaregistrovany (heslo = nick)
     * a nova instancia by bola vratena.
     * -- !! ALE !! --
     * S automatickou registraciou pilotov je PROBLEM, ze nie je zname, k akej strane ho priradit!!
     * Preto je moznost autovytvarania pilotov zrusena!
     *
     *
     * @param il2NickName
     * @return
     */
    public IdentityWithGameAccess getIdentityByIl2NickName(String il2NickName) throws IdentityNotFoundException
    {
        //Pilot fooIdentity = new Pilot();
        //fooIdentity.setLogin("falkon");
        //fooIdentity.setIl2NickName("7./1stCL_Falkon");

        //return fooIdentity;
        return this.controller
                .findByIl2NickName(il2NickName)
                .orElseThrow(() -> new IdentityNotFoundException("Pilot with nick = '" + il2NickName + "' not found!"));
    }

// ======================================================================================


    /**
     * Ziska identitu hrace pomoci TS3 nicknamu.
     *
     * @param ts3NickName
     * @return
     * @throws IdentityNotFoundException
     */
    public IdentityWithTS3Access getIdentityByTS3NickName(String ts3NickName) throws IdentityNotFoundException
    {
        return this.controller
                .findByTs3NickName(ts3NickName)
                .orElseThrow(() -> new IdentityNotFoundException("Pilot with TS3 nick = '" + ts3NickName + "' not found!"));
    }

// ======================================================================================

    /**
     * Vytvori novu identitu typu Pilot a vyplni jej zakladne atributy
     * (login, heslo, nick). Vrati vytvorenu instanciu Pilot-a
     * Pokud je login nebo nick jiz zaregistrovany, metoda selze vyhozenim
     * vyjimky PreexistingEntityException.
     *
     * @param login
     * @param plainPasswd	Heslo v otvorenej podobe
     * @param nick
     * @return
     * @throws PreexistingEntityException
     */
    public synchronized Pilot createPilot(String login, String plainPasswd, String nick, SideEnum side)
            throws PreexistingEntityException
    {

        if (this.controller.findByLogin(login).isPresent()) {
            throw new PreexistingEntityException("Login " + login + " already registered");
        }
        if (this.controller.findByIl2NickName(nick).isPresent()) {
            throw new PreexistingEntityException("Nick " + nick + " already registered");
        }
        Pilot pilot = new Pilot();
        pilot.setLogin(login);
        pilot.setPlainPasswd(plainPasswd);
        pilot.setIl2NickName(nick);
        pilot.setSide(side);
        this.controller.save(pilot);
        return pilot;
    }

// ======================================================================================


    /**
     * Ulozi identitu do DB.
     *
     * @param identity
     */
    public void saveIdentity(Identity identity)
    {
            this.controller.save(identity);
    }

// ======================================================================================

    /**
     * Ulozi zadanu triedu do DB (vytvori novu databazovu instanciu zivotnho cyklu).
     *
     * Je to len proxy-metoda do JPA controllera.
     *
     * @param lifecycle
     */ /*
    public synchronized void createIdentityLifeCycle(IdentityLifecycle lifecycle)
    {
        this.getController().createIdentityLifeCycle(lifecycle);
    } */


// ======================================================================================


    /**
     * Ulozi zadany zivotny cyklus identity do DB.
     *
     * @param lifecycle
     */ /*
    public void saveIdentityLifecycle(IdentityLifecycle lifecycle)
    {
        try {
            this.controller.saveLifecycle(lifecycle);
        } catch (Exception  e) {
            System.out.println(e);
        }
    } */

// ======================================================================================



    /**
     * Autentizuje identitu pouzitim loginu a hesla.
     *
     * Pokial udaje login a plainPasswd su spravne a odpovedaju nejakej identite, tak je tato
     * identita ziskana a vratena. Pokial su udaje nespravne, je vyhodena vynimka popisujuca dovod.
     *
     * @param login
     * @param plainPasswd
     * @return
     * @throws IdentityNotFoundException ak sa identita pre dany login nenajde
     * @throws InvalidCredentialException ak dvojica plainPasswd nie je platne heslo pre identity s loginom login.
     */ /*
    public synchronized Identity authenticate(String login, String plainPasswd) throws IdentityNotFoundException, InvalidCredentialException
    {
        if (login == null) {
            throw new IllegalArgumentException("login can not be null");
        }
        if (plainPasswd == null) {
            throw new IllegalArgumentException("plainPasswd can not be null");
        }


        Identity identity = this.getIdentity(login);
        String passwdHashForTest = HexStringUtils.getSha1String(plainPasswd);

        if ( !passwdHashForTest.equals(identity.getPasswdHash()) ) {
            throw new InvalidCredentialException("Invalid credentials");
        } else {
            return identity;
        }

    } */

// ======================================================================================


    /**
     * Vrati zoznam vsetkych zaregistrovanych identit.
     * @return
     */
    public List<Identity> listAllIdentities()
    {
        return this.controller.findAll();
    }



// ======================================================================================


    public List<Identity> listAllIdentities(SideEnum side)
    {
        throw new UnsupportedOperationException();
        //return this.getController().findIdentityEntities();
    }





}

