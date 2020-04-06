package cz.sm.ng.core.SMWeb.modules;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * ModuleIface
 * Interface of a module
 *
 * @author Dejvino
 */
@JsonSerialize(as = AbstractModule.class)
public interface ModuleIface extends Runnable {

    /**
     * Starts the module
     */
    public void start();

    void stop();

    /**
     * Checks whether the module is active and running
     * @return Is the module running?
     */
    public boolean isActive();

    /**
     * Returns the module name
     * @return Module name
     */
    public String getName();
}

