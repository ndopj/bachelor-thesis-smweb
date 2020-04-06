package cz.sm.ng.web.SMWeb.ManagedBeans.admin;

import cz.sm.ng.core.SMWeb.modules.ModuleIface;
import cz.sm.ng.core.SMWeb.modules.ModulesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This component represents request scoped JSF managed
 * bean responsible for managing SMWeb modules. It should
 * be accessed only from JSF pages to maintain data consistency.
 *
 * @author Dejvino, Norbert Dopjera
 */
@Component(value = "ModulesBean")
@RequestScope
public class ModulesBean
{
    private final ModulesManager modulesManager;

    /** Creates a new instance of ModulesBean */
    public ModulesBean(@Autowired ModulesManager modulesManager)
    {
        this.modulesManager = modulesManager;
    }

    /** Custom comparator for map entries used by this class */
    public class ModulesEntryComparator implements Comparator<Map.Entry<String, ModuleIface>>
    {
        @Override
        public int compare(Map.Entry<String, ModuleIface> o1, Map.Entry<String, ModuleIface> o2)
        {
            if (o1 == o2) {
                return 0;
            }
            if (o1 == null) {
                return -1;
            }
            return o1.getValue().getName().compareTo(o2.getValue().getName());
        }
    }

    /**
     * Starts all modules that are present in SMWeb, i.e
     * starts all modules managed by ModulesManager service.
     * This method is indented to be called from JSF page.
     */
    public void start()
    {
        modulesManager.startAll();
    }

    /**
     * Stops all modules that are present in SMWeb, i.e
     * stops all modules managed by ModulesManager service.
     * This method is indented to be called from JSF page.
     */
    public void stop()
    {
        modulesManager.stopAll();
    }

    /**
     * This method is serving only as hook for AJAX used on
     * JSF pages. Calling this method indicates that JSF page
     * should be refreshed, which can be captured by multiple
     * AJAX listeners.
     */
    public void refresh()
    {
    }

    /**
     * Returns status for all modules that are present in
     * SMWeb. Status for each module indicates if that module
     * is currently active, i.e it is running in some thread.
     *
     * @return concatenated statues for all modules.
     */
    public String getStatus()
    {
        Map<String, Boolean> map = modulesManager.getModulesStatusMap();
        String ret = "";
        for (Map.Entry<String, Boolean> e : map.entrySet()) {
            ret += e.getKey() + " - " + e.getValue() + "\n";
        }
        return ret;
    }

    /**
     * TODO: need to implement SMWeb missions first
     * @return TODO
     */
    public String getMissionStatus()
    {
        String result = "unknown";
        try {
            /*boolean isPlaying = MissionStatusManager.isMissionPlaying();*/
            result = Boolean.toString(true /* isPlaying */);
        } catch (Exception ex) {
            Logger.getLogger(ModulesBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;

    }

    /**
     * Will get list of all modules that are present in
     * SMWeb. This method is indented to be used by JSF
     * page to fill data-table with all modules. Otherwise
     * this method is unusable as keys for each module are
     * randomly generated.
     *
     * @return
     */
    public List<Map.Entry<String, ModuleIface>> listAllModules()
    {
        List<Map.Entry<String, ModuleIface>> modulesList = new ArrayList<Map.Entry<String, ModuleIface>>(
                modulesManager.listAllModules().entrySet()
        );
        Collections.sort(modulesList, new ModulesEntryComparator());
        return modulesList;
    }

    /**
     * Starts module which is identified by key stored in JSF context.
     * This key will be stored in JSF context when it asks for all modules.
     * That usually happens when table with all modules is rendered as
     * response for client accessing modules page at admin section.
     */
    public void startModule()
    {
        String moduleKey = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("key");
        modulesManager.startModule(moduleKey);
        this.refresh();
    }

    /**
     * Stops module which is identified by key stored in JSF context.
     * This key will be stored in JSF context when it asks for all modules.
     * That usually happens when table with all modules is rendered as
     * response for client accessing modules page at admin section.
     */
    public String stopModule()
    {
        String moduleKey = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("key");
        modulesManager.stopModule(moduleKey);
        this.refresh();
        return null;
    }

} // ModulesBean

