package cz.sm.ng.web.SMWeb.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Tato trieda implementuje converter medzi String-om a triedou java.util.Calendar (aby sa dala
 * trieda Calendar pouzivat vo formularoch JSF stranok).
 *
 *
 * @author Roman Stoklasa
 */
@FacesConverter(value = "CalendarConverter")
public class CalendarConverter extends DateTimeConverter
{

//////////////////////////////////////////////////////////////////////////////////
// ========== [  M E T H O D S   ] ===============================================
//////////////////////////////////////////////////////////////////////////////////

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value == null || "".equals(value) ) {
            return null;
        }

        Date date;
        try {
            date = this.parseDbDateString(value);
        } catch (ParseException ex) {
            throw new ConverterException("Nie je mozne skonvertovat string '" + value + "' na Calendar!", ex);
        }
        Calendar retValue = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        retValue.setTime(date);

        // calculate calendar fields
        retValue.getTime();

        return retValue;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value instanceof Calendar) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.format( ((Calendar)value).getTime() );
        } else {
            return super.getAsString(context, component, value);
        }
    }


// ======================================================================================

    /**
     * Pomocna metoda na parsovanie stringu
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    private Date parseDbDateString(String dateString) throws ParseException
    {
        // create gmt time zone
        TimeZone gmtZone = TimeZone.getTimeZone("UTC");
        // create db date format (cut millis)
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(gmtZone);
        return dateFormat.parse(dateString);
    }


// ======================================================================================
}

