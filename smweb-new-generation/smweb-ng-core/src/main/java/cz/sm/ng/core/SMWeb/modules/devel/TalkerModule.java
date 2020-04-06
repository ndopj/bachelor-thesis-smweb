package cz.sm.ng.core.SMWeb.modules.devel;

import cz.sm.ng.core.SMWeb.modules.AbstractModule;
import cz.sm.ng.core.SMWeb.modules.exceptions.MessengerException;
import cz.sm.ng.core.SMWeb.modules.messages.ModulesMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple module implementation, which only generates some messages periodically.
 *
 * Simple line-testing module.
 * Sends a numbered message every X seconds.
 *
 * @author Dejvino
 */
@Service
public class TalkerModule extends AbstractModule
{

//////////////////////////////////////////////////////////////////////////////////
// ============= [     C L A S S E S      ] ======================================
//////////////////////////////////////////////////////////////////////////////////

    // internal worker class
    private class Worker implements Runnable
    {

        //////////////////////////////////////////////////////////////////////////////////
        // ============= [  A T T R I B U T E S   ] ======================================
        //////////////////////////////////////////////////////////////////////////////////

        private AtomicBoolean enabled = new AtomicBoolean(false);
        private int num = 1;

        //////////////////////////////////////////////////////////////////////////////////
        // ========== [  M E T H O D S   ] ===============================================
        //////////////////////////////////////////////////////////////////////////////////

        public void stop() {
            enabled.set(false);
        }

        @Override
        public void run()
        {
            // enable module's worker
            enabled.set(true);
            Logger.getLogger("ModuleTalker").info("Talker's Worker starting");
            // perform work loop
            while (enabled.get()) {
                try {
                    Thread.sleep(1000);
                    try {
                        sendMessage(new ModulesMessage("Talker (" + num++ + ") " + new Date().toString()));
                    } catch (MessengerException ex) {
                        Logger.getLogger(TalkerModule.class.getName()).log(Level.SEVERE, "Nepodarilo sa odoslat spravu '" + num + "'. Preskakujem ju.", ex);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(TalkerModule.class.getName()).log(Level.INFO, null, ex);
                }
            }
            Logger.getLogger("TalkerModule").info("Talker's Worker finished");
        }
        // ======================================================================================

    }

//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////

    /**
     * Instancia vlakna, ktore ma za ulohu pravidelne zasobovat JMS novymi spravami..
     */
    private Worker worker = null;

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
        try {
            this.sendMessage(new ModulesMessage("Bagr"));
        } catch (MessengerException ex) {
            Logger.getLogger(TalkerModule.class.getName()).log(
                    Level.SEVERE, "Nepodarilo sa odoslat spravu 'Bagr'! Nieco je zle!:)", ex);
        }
        worker = new Worker();
        Thread workerThread = new Thread(worker);
        workerThread.setName("Module " + getName() + "'s worker");
        workerThread.setDaemon(true);
        workerThread.start();
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
        // empty
    }


// ======================================================================================

    /**
     *
     * @return
     */
    @Override
    protected void deinit()
    {
        // disable worker thread
        if (worker != null) {
            worker.stop();
            worker = null;
        }
    }


// ======================================================================================


    /**
     *
     * @return
     */
    @Override
    public String getName()
    {
        return "TalkerModule";
    }


// ======================================================================================


}

