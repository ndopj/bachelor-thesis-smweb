package cz.sm.ng.web.SMWeb.ManagedBeans.admin;

import cz.sm.ng.core.SystemProperties.ISystemConfigJpaController;
import cz.sm.ng.core.SystemProperties.SystemProperty;
import cz.sm.ng.core.SystemProperties.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * This component represents view scoped JSF managed
 * bean responsible for SMWeb system configurations. It should
 * be accessed only from JSF pages to maintain data consistency.
 *
 * @author Norbert Dopjera
 */
@Component(value = "SystemConfigurationBean")
@ViewScoped
public class SystemConfigurationBean implements Serializable
{
    private String key = null;
    private String stringValue = null;
    private Integer integerValue = null;
    private Double doubleValue = null;
    private Calendar calendarValue = null;
    @Autowired ISystemConfigJpaController systemConfigJpaController;

    /**
     * This method is called to save new system configuration.
     * Data to be saved are filled into private attributes by
     * JSF page where this bean is used as form backing.
     */
    public void saveForm()
    {
        SystemProperty systemProperty = new SystemProperty();
        systemProperty.setKey(this.key);

        if (this.calendarValue != null) {
            systemProperty.setCalendarValue(this.calendarValue);
        }
        if (this.doubleValue != null) {
            systemProperty.setDoubleValue(this.doubleValue);
        }
        if (this.integerValue != null) {
            systemProperty.setIntValue(this.integerValue);
        }
        if (this.stringValue != null && this.stringValue.length() > 0) {
            systemProperty.setStringValue(this.stringValue);
        }
        systemConfigJpaController.save(systemProperty);
    }

    /**
     * Gets all currently stored SMWeb system configurations.
     * This method is intended to be used in JSF page to fill
     * data table with system configurations.
     *
     * @return List containing all system configurations.
     */
    public List<SystemProperty> getItems()
    {
        return systemConfigJpaController.findAll();
    }

    /**
     * This method is called when JSF page edit button for certain
     * system configuration is pressed. It will prepare text fields
     * for editing that one system configuration. This system configuration
     * is identified by key stored in JSF context. This key will be stored
     * in JSF context when it asks for all system configurations. That
     * usually happens when table with all system configurations is rendered
     * as response for client accessing modules page at admin section
     *
     * @throws NotFoundException when no system configuration is found under key
     */
    public void prepareEdit() throws NotFoundException
    {
        String parameterKey = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("key");
        SystemProperty systemProperty = systemConfigJpaController
                .findById(parameterKey)
                .orElseThrow(() -> new NotFoundException("Setting with id " + parameterKey + " was not found"));

        this.key = systemProperty.getKey();
        this.stringValue = systemProperty.getStringRawValue();
        this.integerValue = systemProperty.getIntegerRawValue();
        this.doubleValue = systemProperty.getDoubleRawValue();
        this.calendarValue = systemProperty.getCalendarRawValue();
    }

    /**
     * This method is called when JSF page delete button for certain
     * system configuration is pressed. That system configuration is
     * identified by key stored in JSF context. This key will be stored
     * in JSF context when it asks for all system configurations. That
     * usually happens when table with all system configurations is rendered
     * as response for client accessing modules page at admin section
     *
     * @throws NotFoundException when no system configuration is found under key
     */
    public void delete() throws NotFoundException
    {
        String parameterKey = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("key");
        SystemProperty systemProperty = systemConfigJpaController
                .findById(parameterKey)
                .orElseThrow(() -> new NotFoundException("Setting with id " + parameterKey + " was not found"));
        systemConfigJpaController.deleteById(parameterKey);
    }

    public Calendar getCalendarValue()
    {
        return calendarValue;
    }

    public void setCalendarValue(Calendar calendarValue)
    {
        this.calendarValue = calendarValue;
    }

    public Double getDoubleValue()
    {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue)
    {
        this.doubleValue = doubleValue;
    }

    public Integer getIntValue()
    {
        return integerValue;
    }

    public void setIntValue(Integer intValue)
    {
        this.integerValue = intValue;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getStringValue()
    {
        return stringValue;
    }

    public void setStringValue(String stringValue)
    {
        this.stringValue = stringValue;
    }

} // SystemConfigurationBean

