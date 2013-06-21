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
 *  limitations under the License.
 */

package org.apache.wookie.beans;

import java.util.Collection;

import org.apache.wookie.helpers.WidgetRuntimeHelper;
import org.apache.wookie.util.WidgetFormattingUtils;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.apache.wookie.w3c.*;

/**
 * IWidget - a simple bean to model a widgets attributes.
 * 
 * @author Paul Sharples
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IWidget extends W3CWidget, IBean
{
  
  /**
   * Set the the default locale
   * @param locale the locale to set as default
   */
  void setDefaultLocale(String locale);
  
	/**
	 * Get the path to the .wgt file used to install the widget
	 * @return
	 */
	String getPackagePath();

	void setPackagePath(String path);
	
	/**
	 * Set the widget update location
	 * @param location the location to set
	 */
	void setUpdateLocation(String location);
    
    /**
     * Set widget height.
     * 
     * @param height widget height
     */
    void setHeight(Integer height);
    
    /**
     * Set widget width.
     * 
     * @param width widget width
     */
    void setWidth(Integer width);
    
    /**
     * Set widget GUID.
     * 
     * @param guid widget GUID
     */
    void setIdentifier(String guid);
    
    /**
     * Set the widget author
     * @param author the author to set
     */
    void setAuthor(IAuthor author);
    
    /**
     * Set widget version number.
     * 
     * @param version widget version
     */
    void setVersion(String version);
    
    /**
     * Set collection of features for this widget.
     * 
     * @param features features collection
     */
    void setFeatures(Collection<IFeature> features);
    
    /**
     * Set collection of widget icons for this widget.
     * 
     * @param widgetIcons widget icons collection
     */
    void setIcons(Collection<IIcon> widgetIcons);
    
    /**
     * Set collection of licenses for this widget.
     * 
     * @param licenses licenses collection
     */
    void setLicenses(Collection<ILicense> licenses);
    
    /**
     * Set collection of names for this widget.
     * 
     * @param names names collection
     */
    void setNames(Collection<IName> names);

    /**
     * Set collection of descriptions for this widget.
     * 
     * @param descriptions descriptions collection
     */
    void setDescriptions(Collection<IDescription> descriptions);
    
    /**
     * Set collection of start files for this widget.
     * 
     * @param startFiles start files collection
     */
    void setContentList(Collection<IContent> startFiles);
    
    /**
     * Set collection of preference defaults for this widget.
     * 
     * @param preferenceDefaults preference defaults collection
     */
    void setPreferences(Collection<org.apache.wookie.w3c.IPreference> preferenceDefaults);
    
    /**
     * Shared implementation utilities.
     */
    public static class Utilities
    {
        
        /**
         * Get widget title for locale.
         * 
         * Note that the title is automatically formatted
         * to use CSS BIDI properties. To get the "raw" name
         * you should call getNames().
         * 
         * @param widget widget
         * @param locale locale
         * @return widget title
         */
        public static String getWidgetTitle(IWidget widget, String locale)
        {
        	IName[] names = widget.getNames().toArray(new IName[widget.getNames().size()]);
            IName name = (IName)LocalizationUtils.getLocalizedElement(names, new String[]{locale}, widget.getDefaultLocale());
            return ((name != null) ? WidgetFormattingUtils.getFormattedWidgetName(name) : IW3CXMLConfiguration.UNKNOWN);
        }
        
        /**
         * Get widget description for locale.
         *       
         * Note that the description is automatically formatted
         * to use CSS BIDI properties. To get the "raw" description
         * you should call getDescriptions().
         * 
         * @param widget widget
         * @param locale locale
         * @return widget description or null
         */
        public static String getWidgetDescription(IWidget widget, String locale)
        {
        	IDescription[] descriptions = widget.getDescriptions().toArray(new IDescription[widget.getDescriptions().size()]);
            IDescription description = (IDescription)LocalizationUtils.getLocalizedElement(descriptions, new String[]{locale}, widget.getDefaultLocale());
            return ((description != null) ? WidgetFormattingUtils.getFormattedWidgetDescription(description) : null);
        }

        /**
         * Get widget short name for locale.
         *
         * Note that the short name is automatically formatted
         * to use CSS BIDI properties. To get the "raw" name
         * you should call getNames().
         * 
         * @param widget widget
         * @param locale locale
         * @return widget short name
         */
        public static String getWidgetShortName(IWidget widget, String locale)
        {
        	IName[] names = widget.getNames().toArray(new IName[widget.getNames().size()]);
            IName name = (IName)LocalizationUtils.getLocalizedElement(names, new String[]{locale}, widget.getDefaultLocale());
            return ((name != null) ? WidgetFormattingUtils.getFormattedWidgetShortName(name) : IW3CXMLConfiguration.UNKNOWN);
        }

        /**
         * Get widget start file url for locale.
         * 
         * @param widget widget
         * @param locale locale
         * @return widget start file url
         */
        public static String getUrl(IWidget widget, String locale)
        {
        	IContent[] startFiles = widget.getContentList().toArray(new IContent[widget.getContentList().size()]);
        	IContent startFile = (IContent)LocalizationUtils.getLocalizedElement(startFiles, new String[]{locale}, widget.getDefaultLocale());
            return ((startFile != null) ? startFile.getSrc() : null);
        }

        
        /**
         * Get widget icon location for locale.
         * 
         * @param widget widget
         * @param locale locale
         * @return widget icon location
         */
        public static String getWidgetIconLocation(IWidget widget, String locale)
        {
        	IIcon[] icons = widget.getIcons().toArray(new IIcon[widget.getIcons().size()]);
            IIcon icon = (IIcon)LocalizationUtils.getLocalizedElement(icons, new String[]{locale}, widget.getDefaultLocale());
            return ((icon != null) ? icon.getSrc() : WidgetRuntimeHelper.DEFAULT_ICON_PATH);
        }
    }
}
