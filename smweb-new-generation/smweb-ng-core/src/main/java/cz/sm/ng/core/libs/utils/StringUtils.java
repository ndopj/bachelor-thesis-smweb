package cz.sm.ng.core.libs.utils;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * Staticka trieda pre rozne uzitocne funkcie na pracu s textom.
 *
 * @author Roman Stoklasa
 */
public class StringUtils
{
    /**
     * Vytvori retazec, kde polozky pola (ktore su dostupne pomocou iteratora) su oddelene pomocou
     * stringu <pre>glue</pre>. Polozky kolekcie su na string prevedene pomoocu nativnej toString() metody.
     *
     * @param iterator	Iterator kolekcie, ktorej prvky sa maju vlozit do stringu.
     * @param glue		Oddelovac jednotlivych poloziek.
     * @return
     */
    public static String implode(Iterator<? extends Object> iterator, String glue)
    {
        StringBuilder sb = new StringBuilder();
        if (!iterator.hasNext()) {
            return "";
        }

        if (iterator.hasNext()) {
            Object o = iterator.next();
            sb.append(o.toString());
        }

        while (iterator.hasNext()) {
            sb.append(glue).append(iterator.next().toString());
        }
        return sb.toString();
    }

// ======================================================================================

    /**
     * Spoji hodnoty v poli arr do retazca, a jednotlive hodnoty oddeli pomoco uretazca glue.
     *
     * @param arr		Pole hodnot
     * @param glue		Oddelovac jednotlivych poloziek.
     * @return
     */
    public static String implode(double[] arr, String glue)
    {
        StringBuilder sb = new StringBuilder();
        if (arr.length < 1) {
            return "";
        }

        for (int i = 0; i < arr.length; ++i) {
            if (i > 0) {
                sb.append(glue);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }


// ======================================================================================

    /**
     * Spoji polozky kolekcie list do stringu a  medzi jednotlive polozky vlozi
     * oddelovac delim.
     *
     * @param list
     * @param glue
     * @return
     */
    public static String implode(Collection<? extends Object> list, String glue)
    {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Object item : list) {
            if (i != 0) {
                sb.append(glue);
            }
            sb.append(item.toString());
            i++;
        }
        return sb.toString();
    }

// ======================================================================================

    /**
     * Nacita obsah suboru do string-u.
     *
     * @param filePath the name of the file to open. Not sure if it can accept URLs or just filenames.
     */
    public static String readFileAsString(String filePath) throws java.io.IOException
    {
		/*
		// -- "hlupa" varianta:
		StringBuilder fileContent = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line;

		while ( (line = reader.readLine()) != null ) {
			fileContent.append(line).append("\r\n");
		}

		reader.close();
		return fileContent.toString();
		*/

        // -- "mudra" varianta - vyuzit Apache Commons:
        return org.apache.commons.io.FileUtils.readFileToString(new File(filePath));
    }

// ======================================================================================

    private static final String TEMPLATE_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random randomGenerator = new Random();

// ======================================================================================

    public static String generateRandomString(int length)
    {
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            sb.append( TEMPLATE_CHARS.charAt(randomGenerator.nextInt(TEMPLATE_CHARS.length())) );
        }
        return sb.toString();
    }

// ======================================================================================

    /**
     * Zodpovie, ci je string NULL alebo prazdny
     *
     * @see  http://stackoverflow.com/questions/8476588/java-equivalent-of-c-sharp-string-isnullorempty-and-string-isnullorwhitespace
     *
     * @param s
     * @return
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Zodpovie dotaz, ci je string NULL alebo obsahuej len prazdne znaky.
     *
     * @see http://stackoverflow.com/questions/8476588/java-equivalent-of-c-sharp-string-isnullorempty-and-string-isnullorwhitespace
     *
     * @param s
     * @return
     */
    public static boolean isNullOrWhitespace(String s) {
        return s == null || isWhitespace(s);

    }

    /**
     * Vyhodnoti, ci zadany string obsahuje len biele znaky.
     *
     * @see http://stackoverflow.com/questions/8476588/java-equivalent-of-c-sharp-string-isnullorempty-and-string-isnullorwhitespace
     *
     * @param s
     * @return
     */
    private static boolean isWhitespace(String s) {
        int length = s.length();
        if (length > 0) {
            for (int start = 0, middle = length / 2, end = length - 1; start <= middle; start++, end--) {
                if (s.charAt(start) > ' ' || s.charAt(end) > ' ') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


// ======================================================================================


}

