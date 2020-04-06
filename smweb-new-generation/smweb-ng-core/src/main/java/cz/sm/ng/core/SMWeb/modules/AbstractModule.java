package cz.sm.ng.core.SMWeb.modules;

import cz.sm.ng.core.SMWeb.modules.exceptions.MessengerException;
import cz.sm.ng.core.SMWeb.modules.messages.ModulesMessage;
import cz.sm.ng.core.SMWeb.modules.websocket.ModulesWebsocket;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class representing module, which communicates via JMS.
 *
 * Default implementation of module interface with abstract methods
 * allowing simpler module development.
 *
 * Extending classes can specify their own init, deinit a message handling code.
 * Separate thread and messenger is obtained and maintained automatically.
 * If the extending class needs it's own 'worker-loop', it should create
 * a separate thread in the init method (it should also stop all the started
 * threads in the deinit method).
 *
 * @author Dejvino
 */
public abstract class AbstractModule implements ModuleIface, Runnable
{


//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////


    /**
     * Default message receiving timeout
     */
    private static final int timeout = 1000;

    /**
     * Module thread processing the messages
     */
    private Thread moduleThread = null;

    /**
     * 'module is enabled' flag
     */
    private AtomicBoolean enabled = new AtomicBoolean(false);

    private static final String JMS_DEST = "JMS.Modules";

    @Autowired protected JmsTemplate jmsTemplate;
    @Autowired protected ModulesWebsocket modulesWebsocket;

//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////

    /**
     * Starts the module if it hasn't been already started
     *
     * !!! POZOR !!! Metoda start() je volana PRED metodou init(), kedze init() je volana uz z "hlavneho tela-metody"
     * run() noveho vlakna, a toto vlakno sa vytvara v AbstractModule::start()!

     */
    @Override
    public void start()
    {
        // skip if module is already inited & started
        if (this.isActive()) {
            return;
        }
        // enable module execution
        enabled.set(true);
        // create a new thread
        moduleThread = new Thread(this);
        moduleThread.setName("Module " + this.getName());
        moduleThread.setDaemon(true);	// <-- vsetky vlakna modulov budu nastavene ako daemon - aby "neblokovali" JVM pred ukoncenim

        // execute the thread
        moduleThread.start();
        modulesWebsocket.sendReload();
        // return to the caller
    }


// ======================================================================================


    /**
     * Flags the module as disabled.
     * It should eventually terminate, but this method does not enforce it.
     */
    @Override
    public void stop()
    {
        if (!isActive()) {
            return;
        }
        // disable current module's execution
        enabled.set(false);
        modulesWebsocket.sendReload();
    }


// ======================================================================================


    /**
     * Checks whether the module's thread is still alive
     * @return Module is running
     */
    @Override
    public boolean isActive()
    {
        return (moduleThread != null && moduleThread.isAlive());
    }


// ======================================================================================


    /**
     * Module thread
     * Implements Runnable interface
     */
    @Override
    public void run()
    {
        // call init method
        Logger.getLogger("Module").log(Level.INFO, "Starting module {0}", getName());
        init();
        // receive and process new messages
        Logger.getLogger("Module").log(Level.INFO, "Module {0} is entering message loop", getName());
        while (enabled.get()) {
            // try to receive a message
            ModulesMessage msg = (ModulesMessage) jmsTemplate.receiveAndConvert(JMS_DEST);
            // if the receiving was successful, process the message
            if (msg != null) {
                process(msg);
            }
        }
        deinit();
        Logger.getLogger("Module").log(Level.INFO, "Module {0} terminated", getName());
        modulesWebsocket.sendReload();
    }

// ======================================================================================

    protected void sendMessage(ModulesMessage message) throws MessengerException {
        try {
            jmsTemplate.convertAndSend(JMS_DEST, message);
        } catch (JmsException exception) {
            throw new MessengerException(exception);
        }
    }

// ======================================================================================


    /**
     * Returns module's logger
     * @return Module's logger
     */
    protected Logger getLogger()
    {
        return Logger.getLogger("Module" + getName());
    }


// ======================================================================================


    /**
     * Method is executed at module start
     */
    protected abstract void init();


// ======================================================================================


    /**
     * Method for message processing
     * Invoked every time a message is received (synchronously).
     * @param msg Received message
     */
    protected abstract void process(ModulesMessage msg);


// ======================================================================================


    /**
     * Method is executed after main loop finishes
     */
    protected abstract void deinit();


// ======================================================================================


    /**
     * Returns module name
     * @return Module name
     */
    @Override
    public abstract String getName();


// ======================================================================================

    public boolean isRunning() {
        return enabled.get() && isActive();
    }

    public boolean isTerminated() {
        return !isActive();
    }

    public boolean isTerminating() {
        return isActive() && !enabled.get();
    }

    public String getActualStateName() {
        if (isTerminated()) {
            return "Stoped";
        }
        return (isRunning()) ? "Running" : "Stopping";
    }
// ======================================================================================

}
