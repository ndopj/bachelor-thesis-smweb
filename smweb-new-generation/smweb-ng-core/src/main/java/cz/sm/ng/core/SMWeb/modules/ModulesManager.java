package cz.sm.ng.core.SMWeb.modules;

import cz.sm.ng.core.SMWeb.modules.devel.TalkerModule;
import cz.sm.ng.core.SMWeb.modules.exceptions.MessengerException;
import cz.sm.ng.core.SMWeb.modules.loggers.MessageLoggerModule;
import cz.sm.ng.core.SMWeb.modules.messages.ModulesMessage;
import cz.sm.ng.core.libs.utils.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *  ModulesManager
 * Modules controlling singleton
 *
 * This class maintains a list of server modules, it is able to start / stop them,
 * return their status.
 *
 * It is responsible for creating instances of ModulesMessenger used for
 * inter-module communication, as specified in the interface.
 *
 * @author Dejvino
 * @author Roman Stoklasa
 */
@Service
public class ModulesManager
{

//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////

    private Map<String, ModuleIface> modules = null;
    private TalkerModule talkerModule;
    private MessageLoggerModule messageLoggerModule;

//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////

    /**
     * Privatny konstruktor singleton instancie
     */
    private ModulesManager(@Autowired TalkerModule talkerModule,
                           @Autowired MessageLoggerModule messageLoggerModule)
    {
        // prepare modules
        this.modules = new HashMap<String, ModuleIface>();
        this.talkerModule = talkerModule;
        this.messageLoggerModule = messageLoggerModule;
        this.init();
    }

// ======================================================================================

    /**
     * Ensures that all active modules are terminated
     * before application shutdown.
     */
    @PreDestroy
    public void whenApplicationTerminates() {
        LoggerFactory.getLogger(this.getClass()).info("Shutting down modules manager");
        this.stopAll();
        try {
            Thread.sleep(3000); // Wait till all threads finish
        } catch (InterruptedException ignored) {}
    }

// ======================================================================================

    /**
     * Inits modules manager singleton
     */
    public synchronized void init()
    {
        // -- ak inicializujem ModulesManager znovu, a nahodou uz mam predosle nejake moduly, tak ich zastavim a zahodim.
        if (!this.modules.isEmpty()) {
            this.stopAll();
            this.modules.clear();
        }

        /** @todo: load modules from config */

        ModuleIface module; // create modules
        // logger -- loguje vsetky JMS spravy - je urceny za ucelom odladovania
        this.addModule(messageLoggerModule);

        // talker
        this.addModule(talkerModule); /*

        // CoreModule -- hlavny modul so zivotnym cyklom... alebo tiez nepotrebny modul, ak bude zivotny cyklus implementovany v inom module
        module = new CoreModule();
        this.addModule(module);

        // -- ObjectsPositionTrackerModule -- trackovanie objektov cez DeviceLink
        module = new ObjectPositionTrackerModule();
        this.addModule(module);

        // -- RadarMarkPositionsToDbSaver -- eventlogove hlasky RADAR_MARK pouziva k aktualizacii pozicii v DB.
        module = new RadarMarkPositionsToDbSaver();
        //modules.add(module);


        // -- replay logger - logovanie priebeznych eventov za ucelom toho, aby sa pozdejsie dal spravit replay
        module = new ReplayLoggerModule();
        this.addModule(module);


        module = RadarModule.getInstance();
        this.addModule(module);

        //modul pre komunikaciu medzi radarmi a frontendom pomocou Web Socketov
        module = new RadarCommunicationModule();
        this.addModule(module);

        // -- Radio-Navigacie: ------------------
        module = new GeeNavigationModule();
        this.addModule(module);

        module = new XGeraetNavigationModule();
        this.addModule(module);

        module = new OboeNavigationModule();
        this.addModule(module);

        module = new KnickebeinNavigationModule();
        this.addModule(module);

        // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


        // -- moduly pro casovou organizaci hry: ------------------
        // modul pro casovou organizaci hry
        module = TimeModule.getInstance();
        this.addModule(module);

        // module ktery si zapisuje pozici pohyblivych objektu
        module = ObjectHistoryModule.getInstance();
        this.addModule(module);

        // modul pro kontrolu letadel
        module = new PilotsWatcherModule();
        //this.addModule(module);

        // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        module = new SturmRadioModule();
        this.addModule(module);

        // -- modul pre console connector
        this.addModule(ConsoleConnectorModule.getInstance());


//		module = new ClodWarCommunicationServerModule();
//		this.addModule(module);

        module = new ClodWarDistributionGeneratorModule();
        this.addModule(module);

        // pomocny modul, ktory treba dopracovat
        module = new ClodWarDistributionModule();
        this.addModule(module);

        // -- console log modul
        module = new ConsoleLoggerModule();
        this.addModule(module);

        // -- playerRegistration modul
        module = PlayerRegistrationCheckModule.getInstance();
        this.addModule(module);

        // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        //-- statistics
        module = new StatisticsModule();
        this.addModule(module);

        // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        module = new SupplyModule();
        this.addModule(module);

        // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        module = SectorActivityModule.getInstance();
        this.addModule(module);

        // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        // - - - - - - - - LOGGING - - - - - - - - - - - - - - - -

        // -- EventLoggerModule (len Eventlog spravy)
        module = new EventLoggerModule();
        this.addModule(module);

        // -- EventLoggerModule (ALL)
        //module = new EventLoggerModule("all");
        //this.addModule(module);

        // -- EventLoggerModule (vsetko okrem ObjectPositions, ktorych je zaplava)
        module = new EventLoggerModule("all-without-ObjectPositions",
                Arrays.asList(ConsoleConnectorMessage.class.getName(), ConsoleResponseMessage.class.getName(),
                        EventMessage.class.getName(), LifecycleMessage.class.getName(),
                        TS3SoundMessage.class.getName() ));
        this.addModule(module);
        // ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^

        //moduly pre ClodWar generator misii
        module = new ClodWarCoreModule();
        this.addModule(module);

        module = new ClodWarRescueMissionModule();
        this.addModule(module);

        module = new ClodWarReconMissionModule();
        this.addModule(module);

        module = new ClodWarAirTransportMissionModule();
        this.addModule(module);

        module = new ClodWarSectorStateWatcherModule();
        this.addModule(module);

        module = new ClodWarTransportMissionModule();
        this.addModule(module); */
    }

// ======================================================================================

    /**
     * Prida modul do interneho dictionary s tym, ze mu vygeneruje novy nahodny kluc
     * @param module
     */
    private synchronized void addModule(ModuleIface module)
    {
        String randomKey = StringUtils.generateRandomString(8);
        while (this.modules.containsKey(randomKey)) {
            randomKey = StringUtils.generateRandomString(8);
        }
        this.modules.put(randomKey, module);
    }

// ======================================================================================

    /**
     * Starts all modules
     */
    public synchronized void startAll()
    {
        // startAll all modules
        for (ModuleIface module : this.modules.values()) {
            module.start();
        }
    }

// ======================================================================================

    /**
     * Tells all modules to terminate
     */
    public synchronized void stopAll()
    {
        // skip stopped singleton
        if (modules == null) {
            return;
        }

        // stopAll all modules
        for (ModuleIface module : this.modules.values()) {
            LoggerFactory.getLogger(this.getClass()).info("Shutting down module '" + module.getName() + "'");
            module.stop();
        }
    }

// ======================================================================================

    /**
     * Nastartuje modul pre zadany kluc (ak existuje).
     *
     * @param moduleKey
     */
    public synchronized void startModule(String moduleKey)
    {
        ModuleIface module = this.modules.getOrDefault(moduleKey, null);
        if (module != null) {
            module.start();
        }
    }

// ======================================================================================

    /**
     * Zastavi modul s danym klucom (ak existuje).
     * @param moduleKey
     */
    public synchronized void stopModule(String moduleKey)
    {
        ModuleIface module = this.modules.getOrDefault(moduleKey, null);
        if (module != null) {
            module.stop();
        }
    }

// ======================================================================================

    /**
     * Vrati unmodifyable map -- slovnik vsetkych modulov a ich identifikatorov.
     * @return
     */
    public Map<String, ModuleIface> listAllModules()
    {
        return Collections.unmodifiableMap(this.modules);
    }

// ======================================================================================

    /**
     * Returns a map containing (module_name, is_active) pairs
     * @return
     */
    public synchronized Map<String, Boolean> getModulesStatusMap()
    {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        if (this.modules != null) {
            for (ModuleIface module : this.modules.values()) {
                map.put(module.getName(), module.isActive());
            }
        }
        return map;
    }

// ======================================================================================

}

