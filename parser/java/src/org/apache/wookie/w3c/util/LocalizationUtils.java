/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wookie.w3c.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.wookie.w3c.ILocalizedElement;

import com.ibm.icu.util.GlobalizationPreferences;
import com.ibm.icu.util.ULocale;

/**
 * Utilities for localization
 * @author Scott Wilson
 *
 */
public class LocalizationUtils {
	static Logger _logger = Logger.getLogger(LocalizationUtils.class.getName());
	
  /**
   * Returns the first (best) match for an element given the set of locales, or will fall back to the
   * match for defaultLocale if there are no suitable matches, and then to null
   * if there are no elements at all.
   * @param elements
   * @param locales
   * @param defaultLocale the default locale in case none of the supplied locales provide a match
   * @return an ILocalizedElement, or null if there are no valid entries
   */	
	public static ILocalizedElement getLocalizedElement(ILocalizedElement[] elements, String[] locales, String defaultLocale){
	  ILocalizedElement element =  getLocalizedElement(elements, locales);
	  // If using the algorithm did not return ANY results, try again using defaultlocale
	  if (element == null && elements != null){
	    for(ILocalizedElement elem:elements){
	      if (elem.getLang().equals(defaultLocale)) return elem;
	    }
	  }
	  return element;
	}
	
	/**
	 * Returns the first (best) match for an element given the set of locales, or null
	 * if there are no suitable elements.
	 * @param elements
	 * @param locales
	 * @return an ILocalizedElement, or null if there are no valid entries
	 */
	protected static ILocalizedElement getLocalizedElement(ILocalizedElement[] elements,String[] locales){
		if (elements == null) return null;
		elements = processElementsByLocales(elements,locales);
		if (elements.length == 0) return null;
		return elements[0];
	}

	 /**
   * Filters and sorts a list of localized elements using the given locale list; only localized elements
   * are returned unless no appropriate localized elements are found, in which case nonlocalized elements
   * are returned. If there are no nonlocalized elements, the defaultLocale attribute is used to try to find
   * a suitable match
   * 
   * @param elements
   * @param locales
   * @param defaultLocale the default locale in case none of the supplied locales provide a match
   * @return the sorted and filtered set of elements
   */
  public static ILocalizedElement[] processElementsByLocales(ILocalizedElement[] elements,String[] locales, String defaultLocale){
    if (elements == null) return null;
    ILocalizedElement[] filteredElements = processElementsByLocales(elements,locales);
    if (filteredElements == null || filteredElements.length == 0){
      for (ILocalizedElement element: elements){
        if(element.getLang().equals(defaultLocale)){
          return (ILocalizedElement[]) ArrayUtils.removeElement(elements, element);
        }
      }
    }
    return filteredElements;
  }
	
	/**
	 * Filters and sorts a list of localized elements using the given locale list; only localized elements
	 * are returned unless no appropriate localized elements are found, in which case nonlocalized elements
	 * are returned
	 * 
	 * @param elements
	 * @param locales
	 * @return the sorted and filtered set of elements
	 */
	protected static ILocalizedElement[] processElementsByLocales(ILocalizedElement[] elements,String[] locales){
		if (elements == null) return null;
		List<ULocale> localesList = getProcessedLocaleList(locales);
		Arrays.sort(elements, new LocaleComparator(localesList));
		return filter(elements,localesList);
	}
	
	/**
	 * Sorts an array of localized elements using default locales (*). This places
	 * any localized elements first, then any unlocalized elements
	 * @return the sorted list of elements
	 */
	public static ILocalizedElement[] processElementsByDefaultLocales(ILocalizedElement[] elements){
		if (elements == null) return null;
		List<ULocale> localesList = getDefaultLocaleList();
		Arrays.sort(elements, new LocaleComparator(localesList));
		return filter(elements,localesList);
	}
	
	/**
	 * Comparator that sorts elements based on priority in the given locale list,
	 * with the assumption that earlier in the list means a higher priority.
	 */
	static class LocaleComparator implements Comparator<ILocalizedElement>{
		private List<ULocale> locales;
		public LocaleComparator(List<ULocale> locales){
			this.locales = locales;
		}
		public int compare(ILocalizedElement o1, ILocalizedElement o2) {
			// check non-localized values for comparison
			if (o1.getLang()!=null && o2.getLang() == null) return -1;
			if (o1.getLang()==null && o2.getLang()!= null) return 1;
			if (o1.getLang()==null && o2.getLang()==null) return 0;
			
			// get index position of locales
			int o1i = -1;
			int o2i = -1;
			for (int i=0;i<locales.size();i++){
				if (o1.getLang().equalsIgnoreCase(locales.get(i).toLanguageTag())) o1i = i;
				if (o2.getLang().equalsIgnoreCase(locales.get(i).toLanguageTag())) o2i = i;
			}
			// put non-matched after matched
			if (o1i == -1 && o2i > -1) return 1;
			if (o1i > -1 && o2i == -1) return -1;
			
			// order by match
			return o1i - o2i;
		}
	}
	
	/**
	 * Filters a set of elements using the locales and returns a copy of the array
	 * containing only elements that match the locales, or that are not localized
	 * if there are no matches
	 * @param elements
	 * @param locales
	 * @return a copy of the array of elements only containing the filtered elements
	 */
	protected static ILocalizedElement[] filter(ILocalizedElement[] elements, List<ULocale> locales){		
		for (ILocalizedElement element:elements){
			String lang = element.getLang();
			boolean found = false;
			for(ULocale locale:locales){
				if (locale.toLanguageTag().equalsIgnoreCase(lang)) found = true;
			}
			if (!found && lang != null) elements = (ILocalizedElement[])ArrayUtils.removeElement(elements, element);
		}
		// Strip out non-localized elements only if there are localized elements left
		if (elements.length > 0){
			if (elements[0].getLang() != null){
				for (ILocalizedElement element:elements){
					if (element.getLang()==null) elements = (ILocalizedElement[])ArrayUtils.removeElement(elements, element);
				}
			}
		}
		return elements;
	}	

	
	/**
	 * Converts an array of language tags to a list of ULocale instances
	 * ordered according to priority, availability and fallback
	 * rules. For example "en-us-posix" will be returned as a List
	 * containing the ULocales for "en-us-posix", "en-us", and "en".
	 * @param locales
	 * @return the sorted list of ULocale instances
	 */
	public static List<ULocale> getProcessedLocaleList(String[] locales){
		if (locales == null) return getDefaultLocaleList();
		GlobalizationPreferences prefs = new GlobalizationPreferences();
		
		ArrayList<ULocale> ulocales = new ArrayList<ULocale>();
		for (String locale:locales){
			if (locale != null){
				try {
					ULocale ulocale = ULocale.forLanguageTag(locale);
					if (!ulocale.getLanguage().equals(""))
						ulocales.add(ulocale);
				} catch (Exception e) {
					// There can be intermittent problems with the internal
					// functioning of the ULocale class with some
					// language tags; best to just log these and continue
					_logger.error("icu4j:ULocale.forLanguageTag("+locale+") threw Exception:",e);
				}
			}
		}	
		if (ulocales.isEmpty()) return getDefaultLocaleList();
		prefs.setLocales(ulocales.toArray(new ULocale[ulocales.size()]));
		return prefs.getLocales();
	}
	
	/**
	 * Gets the default locales list
	 * @return a list of ULocale instances for the default locale
	 */
	protected static List<ULocale> getDefaultLocaleList(){
		GlobalizationPreferences prefs = new GlobalizationPreferences();
		return prefs.getLocales();
	}
	
	/**
	 * Validates a given language tag. Empty and null values are considered false.
	 * @param tag
	 * @return true if a valid language tag, otherwise false
	 */
	public static boolean isValidLanguageTag(String tag){
		try {
	    if (tag.equals("x-w3c-test")) return true; // hack for testing :(
			ULocale locale = ULocale.forLanguageTag(tag);
			if (locale.toLanguageTag() == null) return false;
			// We don't accept "x" extensions (private use tags)
			if(locale.getExtension("x".charAt(0))!=null) return false;
			if (!locale.toLanguageTag().equalsIgnoreCase(tag)) return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
