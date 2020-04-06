package cz.sm.ng.core.LifeCycles;

/**
 * Typy eventu tykajicich se zivotnich syklu.
 *
 * @author Jaroslav Dufek
 */
public class LifecycleEventType {

    // TYPY POUZIVANE LIFECYCLY PRO OHLASOVANI UDALOSTI KTERE SI POSILAJI PRIMO MEZI SEBOU
    public static final String INNER_REFLY_DISCONNECT = "INNER_REFLY_DISCONNECT";
    public static final String INNER_PILOT_SPAWNED_TO_PLANE = "INNER_PILOT_SPAWNED_TO_PLANE";	// <-- pouziva sa k oznaceniu udalosti, ze Virtualny Pilot bol posadeny do lietadla (PlaneInstance)
    public static final String INNER_PILOT_BAILED = "INNER_PILOT_BAILED";
    public static final String INNER_PILOT_IN_FLIGHT = "INNER_PILOT_IN_FLIGHT";
    public static final String INNER_PILOT_LANDED = "INNER_PILOT_LANDED";
    //nastava, ked je pilot zachraneny: zmena stavov "Captured" -> "SomewhereInBarracks"
    public static final String INNER_RESCUE = "INNER_RESCUE";



    // EVENTY GENEROVANE PRECHODEM DO NOVEHO STAVU V ZIVOTNICH CYKLECH
    public static final String LIFECYCLE_IDENTITY_IN_GAME = "LIFECYCLE_IDENTITY_IN_GAME";
    public static final String LIFECYCLE_IDENTITY_IN_GAMEMENU = "LIFECYCLE_IDENTITY_IN_GAMEMENU";
    public static final String LIFECYCLE_IDENTITY_OFFLINE = "LIFECYCLE_IDENTITY_OFFLINE";

    public static final String LIFECYCLE_PILOT_CAPTURED = "LIFECYCLE_PILOT_CAPTURED";
    public static final String LIFECYCLE_PILOT_IN_CHUTE = "LIFECYCLE_PILOT_IN_CHUTE";
    public static final String LIFECYCLE_PILOT_IN_PLANE = "LIFECYCLE_PILOT_IN_PLANE";
    public static final String LIFECYCLE_PILOT_KILLED = "LIFECYCLE_PILOT_KILLED";
    public static final String LIFECYCLE_PILOT_SOMEWHERE_IN_BARRACKS = "LIFECYCLE_PILOT_SOMEWHERE_IN_BARRACKS";
    public static final String LIFECYCLE_PILOT_SUCCESFULLY_BAILED = "LIFECYCLE_PILOT_SUCCESFULLY_BAILED";

    public static final String LIFECYCLE_PLANE_IN_HANGAR = "LIFECYCLE_PLANE_IN_HANGAR";
    public static final String LIFECYCLE_PLANE_ON_GROUND = "LIFECYCLE_PLANE_ON_GROUND";
    public static final String LIFECYCLE_PLANE_IN_FLIGHT = "LIFECYCLE_PLANE_IN_FLIGHT";
    public static final String LIFECYCLE_PLANE_LANDED_IN_FIELD = "LIFECYCLE_PLANE_LANDED_IN_FIELD";
    public static final String LIFECYCLE_PLANE_DESTROYED = "LIFECYCLE_PLANE_DESTROYED";
    public static final String LIFECYCLE_PLANE_ON_GROUND_AFTER_BAILOUT = "LIFECYCLE_PLANE_ON_GROUND_AFTER_BAILOUT";
}

