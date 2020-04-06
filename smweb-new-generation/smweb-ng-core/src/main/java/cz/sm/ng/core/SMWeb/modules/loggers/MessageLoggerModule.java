package cz.sm.ng.core.SMWeb.modules.loggers;

import cz.sm.ng.core.SMWeb.modules.AbstractModule;
import cz.sm.ng.core.SMWeb.modules.messages.ModulesMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Simple message logging module
 *
 * @author Dejvino
 */
@Service
public class MessageLoggerModule extends AbstractModule
{

//////////////////////////////////////////////////////////////////////////////////
// ============= [  A T T R I B U T E S   ] ======================================
//////////////////////////////////////////////////////////////////////////////////

    /**
     * Pocitadlo, kolko sprav tento modul obdrzal. Sluzi napr. k tomu, aby sa
     * len cas-od-casu vypisala do logu hlaska, ze bolo prijatych X sprav.
     */
    private long messagesCountRecieved = 0;

    /**
     * Priznak, ci sa ma vypisovat detailna informacia o kazdej prijatej sprave alebo nie.
     */
    private boolean printEveryMessageDetails = false;

    /**
     * Urcuje, po kolkych prijatych spravach sa bude vypisovat informacia do logu.
     * Ak je nastavene na 0, tak sa informacie o pocte prijatych sprav nevypisuju vobec.
     */
    private long reportInterval = 1;

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
        this.messagesCountRecieved = 0;
        this.jmsTemplate.setReceiveTimeout(4000);
    }

// ======================================================================================
    /*
    @JmsListener(destination = "mailbox")
    public void receiveMessage(ModulesMessage message) {
        String additionalInfo = "";
        ++this.messagesCountRecieved;
        if (this.reportInterval > 0) {
            if (this.messagesCountRecieved % this.reportInterval == 0) {
                System.out.println(getName() + " recieved: " + this.messagesCountRecieved + " messages (" + new Date() + ")");
            }
        }
    }*/

    /**
     *
     * @param msg
     * @return
     */
    @Override
    protected void process(ModulesMessage msg)
    {
        String additionalInfo = "";
        ++this.messagesCountRecieved;
        if (this.reportInterval > 0) {
            if (this.messagesCountRecieved % this.reportInterval == 0) {
                System.out.println(getName() + " recieved: " + this.messagesCountRecieved + " messages (" + new Date() + ")");
            }
        }
        /*
        if (this.printEveryMessageDetails == true) {
            if ("EventMessage".equals(msg.getType()) ) {
                Event event = (Event)msg.getPayload();
                additionalInfo = "Index: " + Long.toString(event.getIndex()) + " | Type: " + event.getEventType();
                System.out.println(getName() + " received: msgType: " + msg.getType() + " " + additionalInfo);
            }
        }*/
        // -- vsetky spravy nebudem logovat... aby
    }

// ======================================================================================

    /**
     *
     * @return
     */
    @Override
    protected void deinit()
    {
        // empty
    }

// ======================================================================================

    /**
     *
     * @return
     */
    @Override
    public String getName()
    {
        return "LoggerModule";
    }

// ======================================================================================

    /**
     * Vrati pocet sprav, po prijati ktorych sa ma oznamit ich prijatie (aby sa negeneroval log pre
     * kazdu spravu ale len cas-od-casu).
     * @return
     */
    public long getReportInterval()
    {
        return reportInterval;
    }

    public void setReportInterval(long reportInterval)
    {
        this.reportInterval = reportInterval;
    }

// ======================================================================================

    public boolean isPrintEveryMessageDetails()
    {
        return printEveryMessageDetails;
    }

    public void setPrintEveryMessageDetails(boolean printEveryMessageDetails)
    {
        this.printEveryMessageDetails = printEveryMessageDetails;
    }

// ======================================================================================

    public long getMessagesCountRecieved()
    {
        return messagesCountRecieved;
    }

// ======================================================================================
}

