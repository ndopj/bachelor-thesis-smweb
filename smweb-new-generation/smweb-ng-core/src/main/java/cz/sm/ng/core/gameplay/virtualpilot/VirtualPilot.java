package cz.sm.ng.core.gameplay.virtualpilot;

import com.google.gson.annotations.Expose;
import cz.sm.ng.core.LifeCycles.Pilot.PilotLifecycle;
import cz.sm.ng.core.clodwar.missiongenerator.ClodWarPlaneClass;
import cz.sm.ng.core.gameplay.virtualpilot.repositories.IVirtualPilotJpaController;
import cz.sm.ng.core.identity.models.Identity;
import cz.sm.ng.core.identity.models.Pilot;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Tato trieda reprezentuje jedneho virtualneho pilota - virtualnu osobu, ktora
 * sada do lietadla, je premiestnovana po mape (v lietadle & pozemnym
 * transportom), moze byt zranena alebo zabita a podobne.
 *
 * Kazdy VirtualnyPilot ma v danej chvili svojho "majitela" - identity Hraca
 * (resp. teoreticky moze byt aj bez asociacie s majitelom, ale to je okrajovy
 * pripad (napr. ked sa vytvoria virt. piloti "do zasoby").
 *
 * @author Roman Stoklasa
 */
@Entity
public class VirtualPilot implements Serializable
{

//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////
    /**
     * Unikatne ID-cko tejto instancie entity v DB.
     */
    @Id
    @GeneratedValue
    @Expose
    private int id;

    /**
     * Identita, ktora vlastni tohoto virt. pilota
     */
    @ManyToOne(optional = true)
    private Identity owner;

    /**
     * Meno virtualneho pilota.
     *
     * Nema ziadny hlbsi vyznam nez aby poskytoval lahku human-readable
     * identifikaciu. Pozor!! Nemusi to byt jednoznacny identifikator!!
     */
    @Expose
    private String name;


    /**
     * Priznak, ci je tento virtualny pilot povazovany za mrtveho.
     * Sluzi na rychle filtrovanie dostupnych a potencialnych virt. pilotov.
     */
    private boolean killed;

    /**
     * Triedy lietadiel pre ClodWar ktore moze pilot pilotovat.
     * Jeden pilot moze vediet ovladat viacero tried lietadiel.
     */
    @ElementCollection(fetch= FetchType.EAGER, targetClass = ClodWarPlaneClass.class)
    @CollectionTable(name = "virtualPilot_planeClass_association")
    @Enumerated(EnumType.STRING)
    private Set<ClodWarPlaneClass> aircraftClasses = new HashSet<>();;


    /**
     * Asociovana zakladna, na ktoru tentp pilot "patri" (je jeho domovskou zakladnou).
     * Samozrejme, priradena zakladna sa moze v case menit.
     */ /*
    @ManyToOne(fetch=FetchType.LAZY, cascade= CascadeType.MERGE, optional=true)
    private Homebase associatedHomebase; */


    /**
     * Zivotny cyklus virtualniho pilota
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private PilotLifecycle lifeCycle;



//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////
    /**
     * Zakladny bezparametricky Bean- konstruktor.
     */
    public VirtualPilot() {
    }

// ======================================================================================
    /**
     * Konstruktor so specifikovanou identitou vlastnika.
     *
     * Meno virtualneho plota sa vytvori ako IL2-nick identty s pridanym
     * "poradovym cislom" (cislo urcene na zaklade poctu virt.pilotov, ktore v
     * DB pre daneho vlastnika uz existuju).
     *
     * @param owner
     */
    public VirtualPilot(Identity owner, @Autowired IVirtualPilotJpaController vpJpaController)
    {
        this.owner = owner;

        String ownerNick;
        if (owner instanceof Pilot) {
            ownerNick = ((Pilot) owner).getIl2NickName();
        } else if (owner != null) {
            ownerNick = owner.getLogin();
        } else {
            ownerNick = "(unassigned)";
        }

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        long count = 0;
        if (owner != null) {
            count = vpJpaController.countByOwner(owner);
        }

        this.name = ownerNick + " VirtPilot #" + (count + 1);
    }

// ======================================================================================
    /**
     * Konstruktor, ktory vytvori virtualneho pilota pre zadaneho vlastnika so
     * zadanym menom.
     *
     * @param owner
     * @param name
     */
    public VirtualPilot(Identity owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    // ======================================================================================
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // ======================================================================================
    public Identity getOwner() {
        return owner;
    }

    public void setOwner(Identity owner) {
        this.owner = owner;
    }

    // ======================================================================================
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
// ======================================================================================

    public Set<ClodWarPlaneClass> getAircraftClasses() {
        return aircraftClasses;
    }

    public void setAircraftClasses(Set<ClodWarPlaneClass> aircraftClasses) {
        this.aircraftClasses = aircraftClasses;
    }

// ======================================================================================
    /**
     * Vrati zivotny cyklu tejto identity.
     *
     * @return
     */
    public PilotLifecycle getPilotLifecycle()
    {
        return this.lifeCycle;
    }

    public void setPilotLifeCycle(PilotLifecycle lifeCycle)
    {
        this.lifeCycle = lifeCycle;
    }

// ======================================================================================
    /**
     * Proxy metoda na ziskanie priamo nazvu aktualneho stavu zivotneho cyklu, v
     * ktorom sa tato identita nachadza.
     *
     * @return
     */
    public String getCurrentPilotStateName() {
        return this.getPilotLifecycle().getCurrentState().getName();
    }


// ======================================================================================


    public boolean isKilled()
    {
        return killed;
    }

    public void setKilled(boolean killed)
    {
        this.killed = killed;
    }


// ======================================================================================

/*
    public Homebase getAssociatedHomebase()
    {
        return associatedHomebase;
    }

    public void setAssociatedHomebase(Homebase associatedHomebase)
    {
        this.associatedHomebase = associatedHomebase;
    }*/


// ======================================================================================

}

