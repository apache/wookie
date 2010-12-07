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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wookie.util.WidgetFormattingUtils;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.util.LocalizationUtils;

/**
 * IWidget - a simple bean to model a widgets attributes.
 * 
 * @author Paul Sharples
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IWidget extends IBean
{
    /**
     * Get widget height.
     * 
     * @return widget height
     */
    Integer getHeight();
    
    /**
     * Set widget height.
     * 
     * @param height widget height
     */
    void setHeight(Integer height);

    /**
     * Get widget width.
     * 
     * @return widget width
     */
    Integer getWidth();
    
    /**
     * Set widget width.
     * 
     * @param width widget width
     */
    void setWidth(Integer width);

    /**
     * Get collection of widget types for this widget.
     * 
     * @return widget types collection
     */
    Collection<IWidgetType> getWidgetTypes();
    
    /**
     * Set collection of widget types for this widget.
     * 
     * @param widgetTypes widget types collection
     */
    void setWidgetTypes(Collection<IWidgetType> widgetTypes);

    /**
     * Get widget GUID.
     * 
     * @return widget GUID
     */
    String getGuid();
    
    /**
     * Set widget GUID.
     * 
     * @param guid widget GUID
     */
    void setGuid(String guid);

    /**
     * Get widget author.
     * 
     * @return widget author
     */
    String getWidgetAuthor();
    
    /**
     * Set widget author.
     * 
     * @param widgetAuthor widget author
     */
    void setWidgetAuthor(String widgetAuthor);

    /**
     * Get widget author email address.
     * 
     * @return widget author email
     */
    String getWidgetAuthorEmail();
    
    /**
     * Set widget author email address.
     * 
     * @param widgetAuthorEmail widget author email
     */
    void setWidgetAuthorEmail(String widgetAuthorEmail);

    /**
     * Get widget author web page href URL.
     * 
     * @return widget author href
     */
    String getWidgetAuthorHref();
    
    /**
     * Set widget author web page href URL.
     * 
     * @param widgetAuthorHref widget author href
     */
    void setWidgetAuthorHref(String widgetAuthorHref);

    /**
     * Get widget version number.
     * 
     * @return widget version
     */
    String getVersion();
    
    /**
     * Set widget version number.
     * 
     * @param version widget version
     */
    void setVersion(String version);
    
    /**
     * Get collection of features for this widget.
     * 
     * @return features collection
     */
    Collection<IFeature> getFeatures();
    
    /**
     * Set collection of features for this widget.
     * 
     * @param features features collection
     */
    void setFeatures(Collection<IFeature> features);

    /**
     * Get collection of widget icons for this widget.
     * 
     * @return widget icons collection
     */
    Collection<IWidgetIcon> getWidgetIcons();
    
    /**
     * Set collection of widget icons for this widget.
     * 
     * @param widgetIcons widget icons collection
     */
    void setWidgetIcons(Collection<IWidgetIcon> widgetIcons);

    /**
     * Get collection of licenses for this widget.
     * 
     * @return licenses collection
     */
    Collection<ILicense> getLicenses();
    
    /**
     * Set collection of licenses for this widget.
     * 
     * @param licenses licenses collection
     */
    void setLicenses(Collection<ILicense> licenses);

    /**
     * Get collection of names for this widget.
     * 
     * @return names collection
     */
    Collection<IName> getNames();
    
    /**
     * Set collection of names for this widget.
     * 
     * @param names names collection
     */
    void setNames(Collection<IName> names);

    /**
     * Get collection of descriptions for this widget.
     * 
     * @return descriptions collection
     */
    Collection<IDescription> getDescriptions();
    
    /**
     * Set collection of descriptions for this widget.
     * 
     * @param descriptions descriptions collection
     */
    void setDescriptions(Collection<IDescription> descriptions);

    /**
     * Get collection of start files for this widget.
     * 
     * @return start files collection
     */
    Collection<IStartFile> getStartFiles();
    
    /**
     * Set collection of start files for this widget.
     * 
     * @param startFiles start files collection
     */
    void setStartFiles(Collection<IStartFile> startFiles);

    /**
     * Get collection of preference defaults for this widget.
     * 
     * @return preference defaults collection
     */
    Collection<IPreferenceDefault> getPreferenceDefaults();
    
    /**
     * Set collection of preference defaults for this widget.
     * 
     * @param preferenceDefaults preference defaults collection
     */
    void setPreferenceDefaults(Collection<IPreferenceDefault> preferenceDefaults);

    /**
     * Get collection of shared data for this widget.
     * 
     * @return shared data collection
     */
    Collection<ISharedData> getSharedData();
    
    /**
     * Set collection of shared data for this widget.
     * 
     * @param sharedData shared data collection
     */
    void setSharedData(Collection<ISharedData> sharedData);
    
    /**
     * Get default widget title, (deprecated in favor of locale specifying APIs).
     * 
     * @return widget title
     */
    @Deprecated
    String getWidgetTitle();
    
    /**
     * Get widget title for locale.
     * 
     * @return widget title
     */
    String getWidgetTitle(String locale);
    
    /**
     * Get default widget description, (deprecated in favor of locale specifying APIs).
     * 
     * @return widget description
     */
    @Deprecated
    String getWidgetDescription();

    /**
     * Get default widget short name, (deprecated in favor of locale specifying APIs).
     * 
     * @return widget short name
     */
    @Deprecated
    String getWidgetShortName();
    
    /**
     * Get shared data with specified key for widget.
     * 
     * @param sharedDataKey shared data key
     * @return shared data
     */
    ISharedData [] getSharedData(String sharedDataKey);
    
    /**
     * Get shared data with specified key and name for widget.
     * 
     * @param sharedDataKey shared data key
     * @param name shared data name
     * @return shared data
     */
    ISharedData getSharedData(String sharedDataKey, String name);
    
    /**
     * Get default start file url, (deprecated in favor of locale specifying APIs).
     * 
     * @return start file url
     */
    @Deprecated
    String getUrl();
    
    /**
     * Get default widget icon location, (deprecated in favor of locale specifying APIs).
     * 
     * @return widget icon location
     */
    @Deprecated
    String getWidgetIconLocation();
    
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
            IName name = (IName)LocalizationUtils.getLocalizedElement(names, new String[]{locale});
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
            IDescription description = (IDescription)LocalizationUtils.getLocalizedElement(descriptions, new String[]{locale});
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
            IName name = (IName)LocalizationUtils.getLocalizedElement(names, new String[]{locale});
            return ((name != null) ? WidgetFormattingUtils.getFormattedWidgetShortName(name) : IW3CXMLConfiguration.UNKNOWN);
        }
        
        /**
         * Get shared data with specified key for widget.
         * 
         * @param widget widget
         * @param sharedDataKey shared data key
         * @return shared data array
         */
        public static ISharedData [] getSharedData(IWidget widget, String sharedDataKey)
        {
            List<ISharedData> sharedDataList = new ArrayList<ISharedData>();
            for (ISharedData sharedData : widget.getSharedData())
            {
                if (sharedData.getSharedDataKey().equals(sharedDataKey))
                {
                    sharedDataList.add(sharedData);
                }
            }
            return sharedDataList.toArray(new ISharedData[sharedDataList.size()]);
        }

        /**
         * Get shared data with specified key and name for widget.
         * 
         * @param widget widget
         * @param sharedDataKey shared data key
         * @param name shared data name
         * @return shared data or null
         */
        public static ISharedData getSharedData(IWidget widget, String sharedDataKey, String name)
        {
            for (ISharedData sharedData : widget.getSharedData())
            {
                if (sharedData.getSharedDataKey().equals(sharedDataKey) && sharedData.getDkey().equals(name))
                {
                    return sharedData;
                }
            }
            return null;
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
        	IStartFile[] startFiles = widget.getStartFiles().toArray(new IStartFile[widget.getStartFiles().size()]);
            IStartFile startFile = (IStartFile)LocalizationUtils.getLocalizedElement(startFiles, new String[]{locale});
            return ((startFile != null) ? startFile.getUrl() : null);
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
        	IWidgetIcon[] icons = widget.getWidgetIcons().toArray(new IWidgetIcon[widget.getWidgetIcons().size()]);
            IWidgetIcon icon = (IWidgetIcon)LocalizationUtils.getLocalizedElement(icons, new String[]{locale});
            return ((icon != null) ? icon.getSrc() : IW3CXMLConfiguration.DEFAULT_ICON_PATH);
        }
    }
}
