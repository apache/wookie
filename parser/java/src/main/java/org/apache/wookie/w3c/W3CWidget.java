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

package org.apache.wookie.w3c;

import java.util.List;

/**
 * A W3C Widget object
 * 
 * This represents the information model for a W3C widget that has been
 * unpacked using the W3CWidgetFactory.
 */
public interface W3CWidget extends ILocalized, IDirectional {

  /**
   * Get the default locale
   * @return a locale string, or Null if no default locale is specified
   */
  public String getDefaultLocale();
  
	/**
	 * Get the list of access request entities for the widget
	 */
	public List<IAccess> getAccessList();
	/**
	 * Get the list of start pages for the widget
	 */
	public List<IContent> getContentList();
	/**
	 * Get the list of icons for the widget
	 */
	public List<IIcon> getIcons();
	/**
	 * Get the widget identifier (IRI)
	 */
	public String getIdentifier();
	/**
	 * Get the list of Licenses for the widget
	 */
	public List<ILicense> getLicenses();
	/**
	 * Get the list of Names for the widget
	 */
	public List<IName> getNames();
	/**
	 * Get the list of Descriptions for the widget
	 */
	public List<IDescription> getDescriptions();
	/**
	 * Get the widget height as an Integer. Note this may be Null.
	 */
	public Integer getHeight();
	/**
	 * Get the widget width as an Integer. Note this may be Null.
	 */
	public Integer getWidth();
	/**
	 * Get the Author information for the widget
	 */
	public IAuthor getAuthor();
	/**
	 * Get the list of Preferences defined for the widget
	 */
	public List<IPreference> getPreferences();
	/**
	 * Get a list of Features requested by the widget. Note that only valid
	 * features are included (e.g. ones supported by the platform, or that are defined by the widget as optional)
	 */
	public List<IFeature> getFeatures();
	/**
	 * Get the version of the widget
	 */
	public String getVersion();
	
	/**
	 * Gets the widget viewmodes attribute. A keyword list attribute that denotes the author's preferred view mode, 
	 * followed by the next most preferred view mode and so forth
	 */
	public String getViewModes();
	
	/**
	 * A convenience method typically used for generating debug messages during import. This method will never return null, however the name may be an empty String.
	 * 
	 * This is equivalent to:
	 * 
	 * <code>
	 * 	  List<IName> names = widget.getNames();
	 * 	  IName name = (IName)LocalizationUtils.getLocalizedElement(names.toArray(new IName[fNamesList.size()]), new String[]{"en"});
	 * </code>
	 * 
	 * @param locale
	 * @return the name of the widget to be used in the given locale
	 */
	public String getLocalName(String locale);
	
	/**
	 * @return the update description document URL for the widget, or null if no valid update URL has been set
	 */
	public String getUpdateLocation();
	
}
