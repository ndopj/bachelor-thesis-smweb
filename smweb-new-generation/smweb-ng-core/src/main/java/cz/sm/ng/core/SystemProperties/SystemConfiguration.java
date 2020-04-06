package cz.sm.ng.core.SystemProperties;

import cz.sm.ng.core.SystemProperties.exceptions.NotFoundException;
import cz.sm.ng.core.SystemProperties.exceptions.TypeMismatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Optional;

/**
 * This service provides functions which
 * simplifies work over system configuration entities.
 * As all spring services this service is thread safe
 * and thus can be simply autowired
 *
 * @author Norbert Dopjera
 */
@Service
public class SystemConfiguration {

    @Autowired private ISystemConfigJpaController systemConfigJpaController;

    /**
     * Custom function wrapper that behaves exactly like Function
     * interface instead it allows to throw TypeMismatch exception.
     * This interface can be used to store lambda that throws relevant
     * exception.
     *
     * @param <T> Parameter Type
     * @param <R> Return value type
     */
    @FunctionalInterface
    private interface CheckedFunction<T, R> {
        R apply(T t) throws TypeMismatch;
    }

    /**
     * Internal function using generics to generalize implementation
     * of get{Type}Value functions provided by this class. It finds
     * system property by given key and extracts certain value from
     * it by using provided function.
     *
     * @param key system property identifier
     * @param valueGetter function that will be used to extract value from found system property
     * @param <Type> type of value to be extracted.
     * @return system property value of provided Type
     * @throws NotFoundException when system property for given key doesnt exists
     * @throws TypeMismatch when extracting value from system property
     */
    private <Type> Type getValueForKey(
            String key, @NotNull CheckedFunction<SystemProperty, Type> valueGetter)
            throws NotFoundException, TypeMismatch
    {
        return valueGetter.apply(systemConfigJpaController.findById(key)
                .orElseThrow(NotFoundException::new));
    }

    /**
     * Internal function using generics to generalize implementation
     * of get{Type}Value functions with default values, provided by this class.
     * It finds system property by given key and extracts certain value from
     * it by using provided function. If there is no system property stored
     * under provided key, new system property is created with default value.
     *
     * @param key system property identifier
     * @param valueGetter function that will be used to extract value from found system property
     * @param defaultProperty property to be stored when required key doesnt exists
     * @param defaultValue value used by default property argument.
     * @param <Type> type of value to be extracted.
     * @return system property value or newly stored system property value of provided Type
     */
    private <Type> Type getValueForKey(
            String key, @NotNull CheckedFunction<SystemProperty, Type> valueGetter,
            SystemProperty defaultProperty, Type defaultValue)
    {
        Optional<SystemProperty> foundValue = systemConfigJpaController.findById(key);
        if (!foundValue.isPresent()) {
            systemConfigJpaController.save(defaultProperty);
            return defaultValue;
        }

        try {
            return valueGetter.apply(foundValue.get());
        } catch (TypeMismatch exception) {
            return defaultValue;
        }
    }

    /**
     * Gets system property String value by given key.
     *
     * @param key identifier of required value
     * @return found String value
     * @throws NotFoundException when provided key cannot be found
     * @throws TypeMismatch when value for provided key is else then String
     */
    public String getStringValue(String key) throws NotFoundException, TypeMismatch
    {
        return getValueForKey(key, systemProperty -> systemProperty.getStringValue());
    }

    /**
     * Gets system property String value by given key. And stores
     * provided default value if there is not value under provided key.
     *
     * @param key identifier of required value
     * @param defaultValue default value to use when key is missing
     * @return String with found value or default value.
     */
    public String getStringValue(String key, String defaultValue)
    {
        return getValueForKey(key, SystemProperty::getStringValue,
                new SystemProperty(key, defaultValue), defaultValue);
    }

    /**
     * Gets system property int value by given key.
     *
     * @param key identifier of required value
     * @return found int value
     * @throws NotFoundException when provided key cannot be found
     * @throws TypeMismatch when value for provided key is else then int
     */
    public int getIntValue(String key) throws NotFoundException, TypeMismatch
    {
        return getValueForKey(key, SystemProperty::getIntValue);
    }

    /**
     * Gets system property int value by given key. And stores
     * provided default value if there is not value under provided key.
     *
     * @param key identifier of required value
     * @param defaultValue default value to use when key is missing
     * @return int of found value or default value.
     */
    public int getIntValue(String key, int defaultValue)
    {
        return getValueForKey(key, SystemProperty::getIntValue,
                new SystemProperty(key, defaultValue), defaultValue);
    }

    /**
     * Gets system property double value by given key.
     *
     * @param key identifier of required value
     * @return found double value
     * @throws NotFoundException when provided key cannot be found
     * @throws TypeMismatch when value for provided key is else then double
     */
    public double getDoubleValue(String key) throws NotFoundException, TypeMismatch
    {
        return getValueForKey(key, SystemProperty::getDoubleRawValue);
    }

    /**
     * Gets system property double value by given key. And stores
     * provided default value if there is not value under provided key.
     *
     * @param key identifier of required value
     * @param defaultValue default value to use when key is missing
     * @return double of found value or default value.
     */
    public double getDoubleValue(String key, double defaultValue)
    {
        return getValueForKey(key, SystemProperty::getDoubleValue,
                new SystemProperty(key, defaultValue), defaultValue);
    }

    /**
     * Gets system property Calendar value by given key.
     *
     * @param key identifier of required value
     * @return found Calendar value
     * @throws NotFoundException when provided key cannot be found
     * @throws TypeMismatch when value for provided key is else then Calendar value
     */
    public Calendar getCalendarValue(String key) throws NotFoundException, TypeMismatch
    {
        return getValueForKey(key, SystemProperty::getCalendarValue);
    }

    /**
     * Gets system property Calendar value by given key. And stores
     * provided default value if there is not value under provided key.
     *
     * @param key identifier of required value
     * @param defaultValue default Calendar value to use when key is missing
     * @return found Calendar value or default value.
     */
    public Calendar getCalendarValue(String key, Calendar defaultValue)
    {
        return getValueForKey(key, SystemProperty::getCalendarValue,
                new SystemProperty(key, defaultValue), defaultValue);
    }

} // SystemConfiguration.class
