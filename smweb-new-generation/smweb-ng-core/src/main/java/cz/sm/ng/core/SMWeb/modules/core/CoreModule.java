package cz.sm.ng.core.SMWeb.modules.core;

import cz.sm.ng.core.LifeCycles.exceptions.IllegalTransitionException;
import cz.sm.ng.core.SMWeb.modules.AbstractModule;
import cz.sm.ng.core.SMWeb.modules.ModulesMessenger;
import cz.sm.ng.core.SMWeb.modules.core.exceptions.UnsupportedEventTypeException;
import cz.sm.ng.core.SMWeb.modules.messages.EventMessage;
import cz.sm.ng.core.SMWeb.modules.messages.ModulesMessage;
import cz.sm.ng.core.SideEnum;
import cz.sm.ng.core.events.Event;
import cz.sm.ng.core.events.EventType;
import cz.sm.ng.core.identity.IdentityManager;
import cz.sm.ng.core.identity.exceptions.IdentityNotFoundException;
import cz.sm.ng.core.identity.models.IdentityWithGameAccess;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hlavny modul systemu, ktory je zodpovedny za korektnu aktualizaciu zivotnych cyklov vsetkych entit!
 *
 *
 * @todo Doimplementovat vsetky metody! Zatial ponechane defaultne vygenerovane vynimky UnsupportedOperationException
 */
public class CoreModule extends AbstractModule
{


//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////

    @Autowired IdentityManager identityManager;

    /**
     * Instancia loggera
     */
    private static final Logger logger = Logger.getLogger(CoreModule.class.getName());

    private static final Set<String> ignoredEvents = new HashSet<String>(
            Arrays.asList(EventType.RADAR_MARK,        // <-- mozem ignorovat - radar spracovava exteran aplikacia.
                    EventType.RADAR_ECHO,        // <-- stary format radar-echa - mozem ignorovat - radar spracovava exteran aplikacia.

                    EventType.END_OF_INPUT,        // <-- ignoruj (je to interny event EventAgent-a, asi by sa na rozhrani nikdy objavit nemal)
                    EventType.NULL_EVENT,

                    // Nasleduji validni eventy, ktere se zatim nijak nezpracovavaji
                    EventType.PILOT_WOUNDED,
                    EventType.PILOT_HEAVILY_WOUNDED,
                    EventType.PILOT_CHUTE_DESTROYED_BY_PLAYER,
                    EventType.PILOT_CHUTE_DESTROYED_BY_STATIC,
                    EventType.PLANE_DAMAGED_ON_GROUND,
                    EventType.PLANE_DAMAGED_BY_PLAYER,
                    EventType.PLANE_DAMAGED_BY_LANDSCAPE,
                    EventType.PLANE_DAMAGED_BY_STATIC,
                    EventType.PLANE_SMOKE,
                    EventType.PLANE_LIGHTS,

                    EventType.OBJECT_DESTROYED_OBJECT,
                    EventType.PLAYER_DESTROYED_OBJECT,

                    //CLOD eventy, ktore zatial nepotrebujeme
                    EventType.TASK_COMPLETED,
                    EventType.PLANE_LIMB_DAMAGED,
                    EventType.PLANE_DAMAGED,
                    EventType.PLANE_AUTOPILOT,
                    EventType.BOMB_EXPLODED,
                    EventType.BUILDING_DESTROYED,
                    EventType.TRIGGER,

                    //CLOD misie
                    EventType.IN_HOMEBASE,
                    EventType.LANDED_IN_FIELD)
    );




    /**
     * Definicia eventov, ktore sa maju posielat do zivotneho cyklu identity
     * @todo PREROBIT, aby si tento zoznam definoval priamo pouzivany zivotny cyklus Identity (ci uz DF alebo SM varianta)
     */
    private static final Set<String> dispatchableToIdentityLC = new HashSet<String>(
            Arrays.asList(EventType.PLAYER_CONNECTED,
                    EventType.PLAYER_DISCONNECTED,
                    EventType.PILOT_SPAWNED,
                    EventType.PILOT_SEAT_CHANGED,    // <-- pre Clod tuto udalost potrebujeme
                    EventType.PILOT_REFLY,
                    EventType.MISSION_END)
    );


    /**
     * Definicia eventov, ktore sa maju posielat do zivotneho cyklu pilota
     * @todo PREROBIT, aby si tento zoznam definoval priamo pouzivany zivotny cyklus Identity (ci uz DF alebo SM varianta)
     */
    private static final Set<String> dispatchableToPilotLC = new HashSet<String>(
            Arrays.asList(EventType.ACTOR_DEAD,
                    EventType.PILOT_SEAT_CHANGED,
                    EventType.PILOT_WOUNDED,
                    EventType.PILOT_HEAVILY_WOUNDED,
                    EventType.PILOT_BAILED,
                    EventType.PILOT_BAILED_SUC,
                    EventType.PILOT_CHUTE_DESTROYED_BY_PLAYER,
                    EventType.PILOT_CHUTE_DESTROYED_BY_STATIC,
                    EventType.PILOT_KILLED,
                    EventType.PILOT_KILLED_BY_PLAYER,
                    EventType.PILOT_KILLED_BY_STATIC,
                    EventType.PILOT_CAPTURED,
                    EventType.PILOT_KILLED_IN_CHUTE_BY_PLAYER,
                    EventType.PILOT_KILLED_IN_CHUTE_BY_STATIC
                    //EventType.ACTOR_CREATED,
                    //EventType.ACTOR_DESTROYED
            )
    );


    /**
     * Definicia eventov, ktore sa maju posielat do zivotneho cyklu lietadla
     * @todo PREROBIT, aby si tento zoznam definoval priamo pouzivany zivotny cyklus Identity (ci uz DF alebo SM varianta)
     */
    private static final Set<String> dispatchableToPlaneLC = new HashSet<String>(
            Arrays.asList(EventType.PILOT_SEAT_CHANGED,
                    EventType.PILOT_IN_FLIGHT,
                    EventType.PILOT_LANDED,
                    EventType.PILOT_BAILED,
                    EventType.PLANE_DAMAGED_ON_GROUND,
                    EventType.PLANE_DAMAGED_BY_PLAYER,
                    EventType.PLANE_DAMAGED_BY_LANDSCAPE,
                    EventType.PLANE_DAMAGED_BY_STATIC,
                    EventType.PLANE_SHOT_DOWN_BY_PLAYER,
                    EventType.PLANE_SHOT_DOWN_BY_LANDSCAPE,
                    EventType.PLANE_SHOT_DOWN_BY_STATIC,
                    EventType.PLANE_CRASHED,
                    EventType.PLANE_SMOKE,
                    EventType.PLANE_LIGHTS,
                    EventType.PLANE_LIMB_CUT,
                    EventType.ACTOR_DEAD,
                    EventType.ACTOR_DAMAGED,
                    EventType.ACTOR_CREATED,
                    EventType.ACTOR_DESTROYED)
    );
    /**
     * Messanger pro zasilani zprav ze zivotnich cyklu.
     */
    private static ModulesMessenger modulesMessenger;





//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    @Override
    protected void init()
    {
        System.out.println("(II) - CoreModule starting...");
    }


// ======================================================================================


    /**
     *
     * @param msg
     * @return
     */
    @Override
    protected void process(ModulesMessage msg)
    {
        if ( !(msg instanceof EventMessage) ) {
            //System.out.println("CoreModul prijal spravu, ktora nie je typu EventMessage -- ignorujem! Prijaty typ: " + msg.getType() );
        } else {
            Event event = (Event) msg.getPayload();
            try {
                // -- posun Event k spracovaniu:
                this.dispatchEvent(event);
            } catch (UnsupportedEventTypeException ex) {
                // -- zalogujem, ze som narazil na typ eventu, ktory nie je naimplementovany:
                /**
                 * @todo Odkomentovat logovanie neznamych eventov!!! Logovanie vypnute len pre ucely prezentacie...
                 */
                Logger.getLogger(CoreModule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalTransitionException ex) {
                // -- ignore
            } catch (Exception ex) {
                Logger.getLogger(CoreModule.class.getName()).log(Level.SEVERE, "Neodchytena vynimka v tele CoreModule.process()! ", ex);

            }


            //System.out.println("CoreModul prija EVENT - " + event.getEventType() + ", index = " + Long.toString(event.getIndex()));

        }



    }


// ======================================================================================


    /**
     *
     * @return
     */
    @Override
    protected void deinit()
    {
        System.out.println("(II) - CoreModule stopping...");
    }

// ======================================================================================

    /**
     *
     * @return
     */
    @Override
    public String getName()
    {
        return "CoreModule";
    }

// ======================================================================================




//////////////////////////////////////////////////////////////////////////////////
// ========== [  P R I V A T E   M E T H O D S   ] ===============================
//////////////////////////////////////////////////////////////////////////////////




    /**
     * Metoda, ktora ma za ulohu "preposlat" prijaty event niektorej metode k spracovaniu - rozhoduje
     * sa podla typu spravy.
     *
     * Metoda prepusta vyhodene vynimky dielcimi metodami.
     *
     */
    private void dispatchEvent(Event event) throws UnsupportedEventTypeException
    {
        logger.log(Level.INFO, "(II) CoreModule :: dispatch event {0}", event);

        if (ignoredEvents.contains(event.getEventType())) {
            return;		// <-- ak je dany event ignorovany, tak mozem hned skoncit
        }

        // -- priznak, ci tento event bol niekym spracovany.
        boolean eventWasIntercepted = false;


        if ( event.getEventType().equals(EventType.PLAYER_SELECTED_ARMY) ) {
            eventWasIntercepted = true;
            try {
                SideEnum sideFromEvent = SideEnum.fromEnglishSideName(event.getComment());
                // -- kontrola, ci sa hrac pripojil za svoju stranu:
                IdentityWithGameAccess pilotIdentity = identityManager.getIdentityByIl2NickName(event.getActorIdent());

                if (!pilotIdentity.getSide().equals( sideFromEvent ) ) {
                    this.kickPlayer(event.getActorIdent(), "You ( " + event.getActorIdent() + ") have joined incorrect side. "
                            + "You have registered for side: " + pilotIdentity.getSide().toString() );
                }

            } catch (IdentityNotFoundException ex) {
                // -- neznamy hrac -- chcem ho kicknut?!
                /* NORO - int shouldKickUnknownUser = SystemConfig.getIntValue("GamePlay.rules.shouldKickUnknownPlayer", 0);
                if (shouldKickUnknownUser > 0) {
                    // -- inak ho kicknem
                    this.kickPlayer(event.getActorIdent(), "Unknown nickname " + event.getActorIdent() + ". Please register on the website first!" );
                } */
            }
        }

        /* NORO
        if (dispatchableToIdentityLC.contains(event.getEventType())) {
            eventWasIntercepted = true;
            IdentityLifeCycleManager.dispatchEvent(event);
            //return;	// <-- POZOR! Toto znamena, ze po spacovani eventu zivot. cyklom identity sa ten isty event uz nespracovava dalsimi ziv. cyklami!!
        }
        if (dispatchableToPilotLC.contains(event.getEventType())) {
            eventWasIntercepted = true;
            PilotLifeCycleManager.dispatchEvent(event);
            //return; // <-- POZOR! Toto znamena, ze po spacovani eventu zivot. cyklom identity sa ten isty event uz nespracovava dalsimi ziv. cyklami!!
        }
        if (dispatchableToPlaneLC.contains(event.getEventType())) {
            eventWasIntercepted = true;
            PlaneLifeCycleManager.dispatchEvent(event);
            //return; // <-- POZOR! Toto znamena, ze po spacovani eventu zivot. cyklom identity sa ten isty event uz nespracovava dalsimi ziv. cyklami!!
        } */

        // -- tu bude velke rozvetvenie podla typu eventu...

        if (EventType.ERROR_EVENT.equals(event.getEventType())) {
            eventWasIntercepted = true;
            this.processErrorEvent(event);

        } else if (EventType.MISSION_PLAYING.equals(event.getEventType()) ) {
            eventWasIntercepted = true;
            /* NORO this.processMissionPlaying(event); */

        } else if (EventType.MISSION_BEGIN.equals(event.getEventType()) ) {
            eventWasIntercepted = true;
            this.processMissionBegin(event);

        } else if (EventType.MISSION_END.equals(event.getEventType()) ) {
            eventWasIntercepted = true;
            this.processMissionEnd(event);

//		} else if (EventType..equals(event.getEventType()) ) {

//		} else if (EventType..equals(event.getEventType()) ) {

//		} else if (EventType..equals(event.getEventType()) ) {

//		} else if (EventType..equals(event.getEventType()) ) {

//		} else if (EventType..equals(event.getEventType()) ) {

//		} else if (EventType..equals(event.getEventType()) ) {

//		} else if (EventType..equals(event.getEventType()) ) {

//		} else if (EventType..equals(event.getEventType()) ) {

//		} else if (EventType..equals(event.getEventType()) ) {

//		} else if (EventType..equals(event.getEventType()) ) {


        }

        if (!eventWasIntercepted) {
            // -- neznamy (novy?) typ eventu? Zaloguj, ale nevyhadzuj vynimku (nikoho "nadradeneho" nezaujima (maximalne to tak zhodi vlakno tohoto Modulu)
            logger.log(Level.WARNING, "CoreModule zaznamenal NEZNAMY event: {0}", event );

            //throw new UnsupportedEventTypeException("Unsuported EventType '" + event.getEventType() + "' - I don't know, how to process it! ");
            // -- 4.11.2010 - nezname typy Eventov ignorujem
        }


    }

// ======================================================================================


    /**
     * Spracuje chybovy event -- aktualne ho len zaloguje.
     *
     * @param event
     */
    private void processErrorEvent(Event event)
    {
        // Inak nie je co s takymto eventom robit...
        logger.log(Level.WARNING, "Core Module zachytil ErrorEvent! Event: {0}", event);
    }

// ======================================================================================

    /**
     * Spracuje event MISSION_PLAYING  (event generovany po nacitani misie)
     *
     * @param event
     */
    private void processMissionPlaying(Event event)
    { /* NORO
        try {
            // -- poznaci identifikator misie do DB. Tento identifikator sa moze pouzit napr. pre
            //	prepojenie informacii ziskanymi z parsovania daneho mis-fajlu. (pripadne kontrly, ze sa
            //	nacitala spravna misia (ktora bola vygenerovana systemom)
            // -- event obsahuje uzitocne inforamcie v policku Ident
            MissionStatusManager.setMissionLoadedInformation(event.getActorIdent(), event.getEventTime());
        } catch (MissionStatusException ex) {
            Logger.getLogger(CoreModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }

// ======================================================================================


    /**
     * Spracuje event MISSION_BEGIN. Zapina priznak 'isMissionPlaying' na TRUE a poznacuje cas startu.
     *
     * @param event
     */
    private void processMissionBegin(Event event)
    { /* NORO
        try {
            MissionStatusManager.setMissionPlayingStatus(true);
        } catch (MissionStatusException ex) {
            Logger.getLogger(CoreModule.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            MissionStatusManager.setMissionBeginTime(event.getEventTime());
        } catch (MissionStatusException ex) {
            Logger.getLogger(CoreModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }

// ======================================================================================

    /**
     * Spracuje event MISSION_END. Vypina priznak 'isMissionPlaying' + vymazava nastavenie 'missionBeginTime'.
     * @param event
     */
    private void processMissionEnd(Event event)
    { /* NORO
        try {
            MissionStatusManager.setMissionPlayingStatus(false);
        } catch (MissionStatusException ex) {
            Logger.getLogger(CoreModule.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            MissionStatusManager.setMissionBeginTime(null);
        } catch (MissionStatusException ex) {
            Logger.getLogger(CoreModule.class.getName()).log(Level.SEVERE, null, ex);
        }

        // vyuziva funkce, ktera vsem pripojenym hracum odesila umely DISCONNECT
        IdentityLifeCycleManager.disconnectAll();

*/
    }

    /**
     * Staticka lazy-load metoda pro ziskani ModulesMessengera, ktery vyuzivaji zivotni cykly.
     *
     * @return
     */
    public static ModulesMessenger getModulesMessanger()
    { /* NORO
        if (modulesMessenger == null) {
            modulesMessenger = ModulesManager.createMessenger();
        } */

        return modulesMessenger;
    }
// ======================================================================================


    /**
     * Pomocna metoda na kicknutie hraca -- je to len proxy do GameProvide.requestKickPlayer().
     *
     * @param playerNick
     * @param messageToPlayer
     */
    public void kickPlayer(String playerNick, String messageToPlayer)
    {
        // NORO GameProvider.requestKickPlayer(playerNick, messageToPlayer);

//        try {
//			ConsoleChatCommand chatCmd = new ConsoleChatCommand();
//			chatCmd.setMessage(messageToPlayer);
//			chatCmd.setRecipient("TO");
//			chatCmd.setPlayerNick(playerNick);
//
//			ConsoleConnectorMessage msg = new ConsoleConnectorMessage(chatCmd);
//			ModulesManager.sendMessage(msg);
//
//			ConsoleKickCommand cmd = new ConsoleKickCommand();
//			cmd.setPlayerNick(playerNick);
//
//			msg = new ConsoleConnectorMessage(cmd);
//			ModulesManager.sendMessage(msg);
//        } catch (MessengerException ex) {
//			Logger.getLogger(CoreModule.class.getName()).log(Level.SEVERE, null, ex);
//        }


    }



}
