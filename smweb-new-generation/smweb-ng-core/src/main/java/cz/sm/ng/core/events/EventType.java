package cz.sm.ng.core.events;


/**
 * Ciselnik vsetkych moznych typov eventov.
 *
 * Vsetky konstanty su textove! Porovnavat pomocou equals!!
 *
 * @author Roman Stoklasa
 */
public class EventType
{
    public static final String ERROR_EVENT = "ERROR_EVENT";
    public static final String NULL_EVENT = "NULL_EVENT";
    public static final String END_OF_INPUT = "END_OF_INPUT";
    public static final String RADAR_ECHO = "RADAR_ECHO";
    public static final String RADAR_MARK = "RADAR_MARK";

    public static final String MISSION_PLAYING = "MISSION_PLAYING";
    public static final String MISSION_BEGIN = "MISSION_BEGIN";
    public static final String MISSION_END = "MISSION_END";

    public static final String PLAYER_CONNECTED = "PLAYER_CONNECTED";
    public static final String PLAYER_DISCONNECTED = "PLAYER_DISCONNECTED";
    public static final String PLAYER_SELECTED_ARMY = "PLAYER_SELECTED_ARMY";
    public static final String PILOT_SPAWNED = "PILOT_SPAWNED";
    public static final String PILOT_REFLY = "PILOT_REFLY";

    public static final String PILOT_IN_FLIGHT = "PILOT_IN_FLIGHT";
    public static final String PILOT_SEAT_CHANGED = "PILOT_SEAT_CHANGED";
    public static final String AI_PILOT_SEAT_CHANGED = "AI_PILOT_SEAT_CHANGE";
    public static final String PILOT_DEAD = "PILOT_DEAD";
    public static final String PILOT_LANDED = "PILOT_LANDED";
    public static final String PILOT_WOUNDED = "PILOT_WOUNDED";
    public static final String PILOT_HEAVILY_WOUNDED = "PILOT_HEAVILY_WOUNDED";
    public static final String PILOT_BAILED = "PILOT_BAILED";
    public static final String PILOT_BAILED_SUC = "PILOT_BAILED_SUC";
    public static final String PILOT_CHUTE_DESTROYED_BY_PLAYER = "PILOT_CHUTE_DESTROYED_BY_PLAYER";
    public static final String PILOT_CHUTE_DESTROYED_BY_STATIC = "PILOT_CHUTE_DESTROYED_BY_STATIC";
    public static final String PILOT_KILLED = "PILOT_KILLED";
    public static final String PILOT_KILLED_BY_PLAYER = "PILOT_KILLED_BY_PLAYER";
    public static final String PILOT_KILLED_BY_STATIC = "PILOT_KILLED_BY_STATIC";
    public static final String PILOT_CAPTURED = "PILOT_CAPTURED";
    public static final String PILOT_KILLED_IN_CHUTE_BY_PLAYER = "PILOT_KILLED_IN_CHUTE_BY_PLAYER";
    public static final String PILOT_KILLED_IN_CHUTE_BY_STATIC = "PILOT_KILLED_IN_CHUTE_BY_STATIC";

    public static final String PLANE_DAMAGED_ON_GROUND = "PLANE_DAMAGED_ON_GROUND";
    public static final String PLANE_DAMAGED_BY_PLAYER = "PLANE_DAMAGED_BY_PLAYER";
    public static final String PLANE_DAMAGED_BY_LANDSCAPE = "PLANE_DAMAGED_BY_LANDSCAPE";
    public static final String PLANE_DAMAGED_BY_STATIC = "PLANE_DAMAGED_BY_STATIC";
    public static final String PLANE_SHOT_DOWN_BY_PLAYER = "PLANE_SHOT_DOWN_BY_PLAYER";
    public static final String PLANE_SHOT_DOWN_BY_LANDSCAPE = "PLANE_SHOT_DOWN_BY_LANDSCAPE";
    public static final String PLANE_SHOT_DOWN_BY_STATIC = "PLANE_SHOT_DOWN_BY_STATIC";
    public static final String PLANE_CRASHED = "PLANE_CRASHED";
    public static final String PLANE_SMOKE = "PLANE_SMOKE";
    public static final String PLANE_LIGHTS = "PLANE_LIGHTS";

    // TYTO EVENTY JESTE NEJSOU NIJAK ZPRACOVAVANE
    public static final String PLAYER_DESTROYED_OBJECT = "PLAYER_DESTROYED_OBJECT";		// povodny tvar (pred 2.2.2018): "PILOT_DESTROYED_STATIC"
    public static final String OBJECT_DESTROYED_OBJECT = "OBJECT_DESTROYED_OBJECT";		// povodny tvar (pred 2.2.2018): "STATIC_DESTROYED_STATIC"

    // CLOD eventy

    public static final String ACTOR_CREATED = "ACTOR_CREATED";
    public static final String ACTOR_DAMAGED = "ACTOR_DAMAGED";
    public static final String ACTOR_DESTROYED = "ACTOR_DESTROYED";
    public static final String ACTOR_DEAD = "ACTOR_DEAD";
    public static final String TASK_COMPLETED = "TASK_COMPLETED";
    public static final String PLANE_LIMB_DAMAGED = "PLANE_LIMB_DAMAGED";
    public static final String PLANE_LIMB_CUT = "PLANE_LIMB_CUT";
    public static final String PLANE_DAMAGED = "PLANE_DAMAGED";
    public static final String PLANE_AUTOPILOT = "PLANE_AUTOPILOT";
    public static final String BOMB_EXPLODED = "BOMB_EXPLODED";
    public static final String BUILDING_DESTROYED = "BUILDING_DESTROYED";
    public static final String TRIGGER = "TRIGGER";

    //CLOD mission eventy
    public static final String RESCUE_FAILED = "RESCUE_FAILED";
    public static final String RESCUE_SUCCESS = "RESCUE_SUCCESS";
    public static final String LANDED_IN_FIELD = "LANDED_IN_FIELD";
    public static final String IN_HOMEBASE = "IN_HOMEBASE";

}

