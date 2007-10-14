package org.tencompetence.widgetservice.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Some useful General Utilities
 *
 * @author Phillip Beauvoir
 * @author Paul Sharples
 * @version $Id: GeneralUtils.java,v 1.1 2007-10-14 10:58:40 ps3com Exp $
 */
public final class GeneralUtils  {
	
	private GeneralUtils() {}
	
	public static final int MACINTOSH = 0;
	public static final int WINDOWS_XP = 1;
	public static final int WINDOWS_98 = 2;
	public static final int WINDOWS_NT = 3;
	public static final int WINDOWS_2000 = 4;
    public static final int WINDOWS_2003 = 5;
    public static final int WINDOWS_XX = 6;
	public static final int UNIX = 99;
	
	
	/**
	 * Determine the OS we are running on.
	 * @return The OS we are running on
	 */
	public static int getOS() {
		String osName = System.getProperty("os.name");
		osName = osName.toLowerCase();
		if(osName.equalsIgnoreCase("windows xp")) return WINDOWS_XP;
		if(osName.equalsIgnoreCase("windows 2000")) return WINDOWS_2000;
		if(osName.equalsIgnoreCase("windows nt")) return WINDOWS_NT;
        if(osName.equalsIgnoreCase("windows 98")) return WINDOWS_98;
        if(osName.equalsIgnoreCase("windows 2003")) return WINDOWS_2003;
		if(osName.startsWith("windows")) return WINDOWS_XX;
		if(osName.startsWith("mac os")) return MACINTOSH;
		else return UNIX;
	}
	
    /**
     * @param href The href to parse
     * @return true if href is an external URL
     */
    public static boolean isExternalURL(String href) {
    	if(href != null) {
    		href = href.toLowerCase();
    		return 	href.startsWith("http") ||
					href.startsWith("www") ||
					href.startsWith("ftp:"
				);}
    	return false;
    }


    /**
	 * Get the version of Java we are on.
	 * @return The version
	 */
	public static String getJavaVersion() {
		return System.getProperty("java.version");
	}
	
	/**
	 * Parse a date to a short date string.
	 * @param date The date to parse
	 * @return A String representation of the date such as "25-12-2002"
	 */
	public static String getShortDate(Date date) {
		if(date == null) return "";
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		return df.format(date);
	}
	
	/**
	 * Get the time as it is now.
	 * @return The Time Now
	 */
	public static Date getNow() {
		Calendar c = Calendar.getInstance();
		return c.getTime();
	}

	/**
	 * Returns a class from a jar file that exists in the given folder
	 * @param className The name of the class required
	 * @param jarFile The file of the jar
	 * @return The class or null if could not be created
	 */
	public static Class getClassFromJar(String className, File jarFile) {
		try {
			URL url = jarFile.toURL();
			ClassLoader loader = new URLClassLoader(new URL[] { url });
			return loader.loadClass(className);
		}
		catch(Exception ex) {
			System.out.println("Could not create class: "  + className + " " + ex);
			return null;
		}
	}
	
	/**
	 * Returns a class New Instance from a jar file that exists in the given jar
	 * @param className The name of the class required
	 * @param jarFile The file of the jar
	 * @return An instance of the class or null if it could not be created
	 */
	public static Object getClassInstanceFromJar(String className, File jarFile) {
		Object o = null;
		
		try {
			Class c = getClassFromJar(className, jarFile);
			if(c != null) o = c.newInstance();
		}
		catch(Exception ex) {
			System.out.println("Could not create class instance: "  + className + " " + ex);
			return null;
		}
		return o;
	}
	
}

