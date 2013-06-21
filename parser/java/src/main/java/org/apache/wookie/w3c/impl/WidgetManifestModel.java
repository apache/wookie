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

package org.apache.wookie.w3c.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wookie.w3c.updates.UpdateDescription;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.apache.wookie.w3c.IAccess;
import org.apache.wookie.w3c.IAuthor;
import org.apache.wookie.w3c.IContent;
import org.apache.wookie.w3c.IDescription;
import org.apache.wookie.w3c.IFeature;
import org.apache.wookie.w3c.IIcon;
import org.apache.wookie.w3c.ILicense;
import org.apache.wookie.w3c.ILocalized;
import org.apache.wookie.w3c.IName;
import org.apache.wookie.w3c.IPreference;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.util.IRIValidator;
import org.apache.wookie.w3c.util.NumberUtils;
import org.apache.wookie.w3c.util.RandomGUID;
import org.apache.wookie.w3c.util.UnicodeUtils;
import org.apache.wookie.w3c.util.WidgetPackageUtils;
import org.apache.wookie.w3c.xml.IContentEntity;
import org.apache.wookie.w3c.xml.IElement;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
/**
 * Processes a config.xml document to create a model
 * for a widget, including all sub-objects
 * @author Paul Sharples
 */
public class WidgetManifestModel extends AbstractLocalizedEntity implements W3CWidget, IElement {
	
	static Logger fLogger = Logger.getLogger(WidgetManifestModel.class.getName());
	
	private String defaultLocale;
	private String defaultIdentifier;
	private String fIdentifier;
	private String fVersion;
	private Integer fHeight;
	private Integer fWidth;
	private String fViewModes;
	private String[] features;
	private List<IName> fNamesList;
	private List<IDescription> fDescriptionsList;
	private IAuthor fAuthor;
	private List<ILicense> fLicensesList;
	private List<IIcon> fIconsList;
	private List<IAccess> fAccessList;
	private List<IContent> fContentList;
	private List<IFeature> fFeaturesList;
	private List<IPreference> fPreferencesList;
	private String fUpdate;
	
	private String[] supportedEncodings;
	
	private ZipFile zip;
	
	/**
	 * Constructs a new WidgetManifestModel using an XML manifest supplied as a String.
	 * @param xmlText the XML manifest file
	 * @throws JDOMException
	 * @throws IOException
	 * @throws BadManifestException
	 */
	public WidgetManifestModel (String xmlText, String[] locales, String[] features, String[] encodings, ZipFile zip, String defaultIdentifier) throws JDOMException, IOException, BadManifestException {		
		super();		
		this.zip = zip;
		this.features = features;
		this.supportedEncodings = encodings;
		fNamesList = new ArrayList<IName>();
		fDescriptionsList = new ArrayList<IDescription>();
		fLicensesList = new ArrayList<ILicense>();
		fIconsList = new ArrayList<IIcon>();
		fContentList = new ArrayList<IContent>();
		fAccessList = new ArrayList<IAccess>();
		fFeaturesList = new ArrayList<IFeature>();
		fPreferencesList = new ArrayList<IPreference>();
		this.defaultIdentifier = defaultIdentifier;
		SAXBuilder builder = new SAXBuilder();
		Element root;
		try {
			root = builder.build(new StringReader(xmlText)).getRootElement();
		} catch (Exception e) {
			throw new BadManifestException("Config.xml is not well-formed XML");
		}				
		fromXML(root,locales);	
		
		// Add default icons
		for (String iconpath:WidgetPackageUtils.getDefaults(zip, locales,IW3CXMLConfiguration.DEFAULT_ICON_FILES)){
			if (iconpath != null) {
				// don't add it if its a duplicate
				boolean exists = false;
				for (IIcon icon: fIconsList){
					if (icon.getSrc().equals(iconpath)) exists = true;
				}
				if (!exists){
					IconEntity i = new IconEntity();
					i.setLang(WidgetPackageUtils.languageTagForPath(iconpath));
					i.setSrc(iconpath);
					fIconsList.add(i);	
				}
			}
		}
		
		// Add default start files
		for (String startpath:WidgetPackageUtils.getDefaults(zip, locales,IW3CXMLConfiguration.START_FILES)){
			if (startpath != null) {
				// don't add it if its a duplicate
				boolean exists = false;
				for (IContent content: fContentList){
					if (content.getSrc().equals(startpath)) exists = true;
				}
				if (!exists){
					ContentEntity c = new ContentEntity();
					c.setLang(WidgetPackageUtils.languageTagForPath(startpath));
					c.setSrc(startpath);
					c.setCharSet(IW3CXMLConfiguration.DEFAULT_CHARSET);
					// Set the default content type
					if (startpath.endsWith(".htm") || startpath.endsWith(".html")) c.setType("text/html");
					if (startpath.endsWith(".xht") || startpath.endsWith(".xhtml")) c.setType("application/xhtml+xml");
					if (startpath.endsWith(".svg")) c.setType("image/svg+xml");
					fContentList.add(c);	
				}
			}
		}
	}
	
	public String getDefaultLocale(){
	  return defaultLocale;
	}

	public String getViewModes() {
		return fViewModes;
	}
	
	public String getVersion() {
		return fVersion;
	}
	
	public List<IPreference> getPreferences(){
		return fPreferencesList;
	}
	
	public List<IFeature> getFeatures(){
		return fFeaturesList;
	}
	
	public List<IAccess> getAccessList(){
		return fAccessList;
	}
	
	public IAuthor getAuthor(){
		return fAuthor;
	}

	public List<IContent> getContentList() {
		return fContentList;
	}
	
	public List<IDescription> getDescriptions(){
		return fDescriptionsList;
	}
	
	public List<IName> getNames() {
		return fNamesList;
	}
	
	public List<IIcon> getIcons() {
		return fIconsList;
	}

	public List<ILicense> getLicenses() {
		return fLicensesList;
	}

	public String getIdentifier() {
		return fIdentifier;
	}

	public Integer getHeight() {
		return fHeight;
	}

	public Integer getWidth() {
		return fWidth;
	}
	
	public String getUpdateLocation(){
		return fUpdate;
	}
	
	public void fromXML(Element element){
		fLogger.warn("WidgetManifestModel.fromXML() called with no locales");
		try {
			fromXML(element, new String[]{"en"});
		} catch (BadManifestException e) {
			fLogger.error("WidgetManifestModel.fromXML() called with no locales and Bad Manifest",e);
		}
	}

	public String getLocalName(String locale){
		NameEntity name = (NameEntity)LocalizationUtils.getLocalizedElement(fNamesList.toArray(new NameEntity[fNamesList.size()]), new String[]{locale}, defaultLocale);
		if (name != null) return name.getName();
		return IW3CXMLConfiguration.UNKNOWN;
	}

	public void updateIconPaths(String path){
		for(IIcon icon : fIconsList){
			if(!icon.getSrc().startsWith("http:")) icon.setSrc(path + icon.getSrc());
		}
	}

	@SuppressWarnings("deprecation")
	public void fromXML(Element element, String[] locales) throws BadManifestException {						
		// check the namespace uri 
		if(!element.getNamespace().getURI().equals(IW3CXMLConfiguration.MANIFEST_NAMESPACE)){			
			throw new BadManifestException("'"+element.getNamespace().getURI() 
					+ "' is a bad namespace. (Should be '" + IW3CXMLConfiguration.MANIFEST_NAMESPACE +"')");
		}				
		// IDENTIFIER IS OPTIONAL
		fIdentifier = element.getAttributeValue(IW3CXMLConfiguration.ID_ATTRIBUTE);
		if(fIdentifier == null){
			// try the old one
			fIdentifier = element.getAttributeValue(IW3CXMLConfiguration.UID_ATTRIBUTE);
		}
		// Normalize spaces
		if(fIdentifier != null) fIdentifier = UnicodeUtils.normalizeSpaces(fIdentifier);
		// Not a valid IRI?
		if (!IRIValidator.isValidIRI(fIdentifier)){
			fIdentifier = null;
		}
		if(fIdentifier == null){
			//is there a default?
			if (defaultIdentifier != null){
				fIdentifier = defaultIdentifier;
			} else {
				//give up & generate one
				RandomGUID r = new RandomGUID();
				fIdentifier = "http://incubator.apache.org/wookie/generated/" + r.toString();
			}
		}
		
		// DEFAULTLOCALE IS OPTIONAL
		defaultLocale = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.DEFAULTLOCALE_ATTRIBUTE));
		locales = addDefaultLocale(locales, defaultLocale);
		
		// VERSION IS OPTIONAL		
		fVersion = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.VERSION_ATTRIBUTE));
		
		// HEIGHT IS OPTIONAL	
		String height  = element.getAttributeValue(IW3CXMLConfiguration.HEIGHT_ATTRIBUTE);
		if(height != null){
			try { 
				fHeight = NumberUtils.processNonNegativeInteger(height); 
			} catch (NumberFormatException e) { 
				// Not a valid number - pass through without setting 
			} 
		}

		// WIDTH IS OPTIONAL		
		String width  = element.getAttributeValue(IW3CXMLConfiguration.WIDTH_ATTRIBUTE);
		if(width != null){
			try { 
				fWidth = NumberUtils.processNonNegativeInteger(width); 
			} catch (NumberFormatException e) { 
				// Not a valid number - pass through without setting 
			} 
		}

		// VIEWMODES IS OPTIONAL	
		fViewModes = element.getAttributeValue(IW3CXMLConfiguration.MODE_ATTRIBUTE);
		if(fViewModes == null){
			fViewModes = IW3CXMLConfiguration.DEFAULT_VIEWMODE;
		} else {
			fViewModes = UnicodeUtils.normalizeSpaces(fViewModes);
			String modes = "";
			// remove any unsupported modes
			for (String mode:fViewModes.split(" ")){
				if (Arrays.asList(IW3CXMLConfiguration.VIEWMODES).contains(mode)) modes = modes + mode +" ";
			}
			fViewModes = modes.trim();
		}
		// DIR and XML:LANG ARE OPTIONAL
		super.fromXML(element);
		

		
		// parse the children
		boolean foundContent = false;
		for(Object o : element.getChildren()) {
			Element child = (Element)o;
			String tag = child.getName();			

			// NAME IS OPTIONAL - get the name elements (multiple based on xml:lang)
			if(tag.equals(IW3CXMLConfiguration.NAME_ELEMENT)) {				
				NameEntity aName = new NameEntity();
				aName.fromXML(child);				
				// add it to our list only if its not a repetition of an
				// existing name for the locale
				if (isFirstLocalizedEntity(fNamesList,aName)) fNamesList.add(aName);
			}
			
			// DESCRIPTION IS OPTIONAL multiple on xml:lang
			if(tag.equals(IW3CXMLConfiguration.DESCRIPTION_ELEMENT)) {				
				DescriptionEntity aDescription = new DescriptionEntity();
				aDescription.fromXML(child);
				// add it to our list only if its not a repetition of an
				// existing description for the locale and the language tag is valid
				if (isFirstLocalizedEntity(fDescriptionsList,aDescription) && aDescription.isValid()) fDescriptionsList.add(aDescription);
			}
			
			// AUTHOR IS OPTIONAL - can only be one, ignore subsequent repetitions
			if(tag.equals(IW3CXMLConfiguration.AUTHOR_ELEMENT) && fAuthor == null) {
				fAuthor = new AuthorEntity();
				((IElement) fAuthor).fromXML(child);
			}	
			
			// UDPATE DESCRIPTION IS OPTONAL - can only be one, ignore subsequent repetitions
			if(tag.equals(IW3CXMLConfiguration.UPDATE_ELEMENT) && fUpdate == null && child.getNamespace().getURI().equals(IW3CXMLConfiguration.MANIFEST_NAMESPACE)) {
				UpdateDescription update = new UpdateDescription();
				update.fromXML(child);
				// It must have a valid HREF attribute, or it is ignored
				if (update.getHref() != null) fUpdate = update.getHref();
			}	
		
			// LICENSE IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.LICENSE_ELEMENT)) {				
				LicenseEntity aLicense = new LicenseEntity();
				aLicense.fromXML(child);
				// add it to our list only if its not a repetition of an
				// existing entry for the locale and the language tag is valid
				if (isFirstLocalizedEntity(fLicensesList,aLicense) && aLicense.isValid()) fLicensesList.add(aLicense);
			}
			
			// ICON IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.ICON_ELEMENT)) {						
				IconEntity anIcon = new IconEntity();
				anIcon.fromXML(child,locales,zip);
				if (anIcon.getSrc()!=null) fIconsList.add(anIcon);
			}
			
			// ACCESS IS OPTIONAL  can be many 
			if(tag.equals(IW3CXMLConfiguration.ACCESS_ELEMENT)) {											
				IAccess access = new AccessEntity();
				((IElement) access).fromXML(child);
				if (access.getOrigin()!=null){
					if (access.getOrigin().equals("*")) {
						fAccessList.add(0, access);
					} else {
						fAccessList.add(access);
					}
				}
			}
			
			// CONTENT IS OPTIONAL - can be 0 or 1
			if(tag.equals(IW3CXMLConfiguration.CONTENT_ELEMENT)) {	
				if (!foundContent){
					foundContent = true;
					IContentEntity aContent = new ContentEntity();	
					aContent.fromXML(child,locales,supportedEncodings,zip);
					if (aContent.getSrc()!=null) fContentList.add(aContent);
				}
			}
			
			// FEATURE IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.FEATURE_ELEMENT)) {
				FeatureEntity feature = new FeatureEntity(this.features);
				feature.fromXML(child);
				if (feature.getName()!=null) fFeaturesList.add(feature);
			}
			
			// PREFERENCE IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.PREFERENCE_ELEMENT)) {
				PreferenceEntity preference = new PreferenceEntity();
				preference.fromXML(child);
				// Skip preferences without names
				if (preference.getName() != null){
					// Skip preferences already defined
					boolean found = false;
					for (IPreference pref:getPreferences()){
						if (pref.getName().equals(preference.getName())) found = true;
					}
					if (!found) fPreferencesList.add(preference);
				}
			}
			
		}
	}

	/**
	 * Checks to see if the given list already contains an ILocalizedEntity
	 * with a language that matches that of the given entity. If it does, then the
	 * method returns false.
	 * @param list a list of ILocalizedEntity instances
	 * @param ent an ILocalizedEntity
	 * @return true if the list contains an entity with matching language
	 */
	@SuppressWarnings("unchecked")
	private boolean isFirstLocalizedEntity(List list, ILocalized ent){
		boolean first = true;
		for (ILocalized entity: (ILocalized[])list.toArray(new ILocalized[list.size()]))
			if (StringUtils.equals(entity.getLang(), ent.getLang())) first = false;
		return first;
	}
	
	/**
	 * Adds the defaultLocale to the locales array, provided it isn't null
	 * and doesn't duplicate an existing locale
	 * @param locales
	 * @param defaultLocale
	 * @return the updated locale array
	 */
	private String[] addDefaultLocale(String[] locales, String defaultLocale){
	  if (defaultLocale == null) return locales;
	  // If there is no locales list, create a new one with the defaultLocale in it
	  if (locales == null) return new String[]{defaultLocale};
	  // If it already exists, return the existing locales array
	  for (String locale:locales){
	    if (locale.equals(defaultLocale)) return locales;
	  }
	  // Create a copy of the locales array and add the defaultlocale to the end
	  String[] newLocales = new String[locales.length+1];
	  System.arraycopy(locales, 0, newLocales, 0, locales.length);
	  newLocales[newLocales.length-1] = defaultLocale;
	  return newLocales;
	}

	public Element toXml() {
		Element widgetElem = new Element(IW3CXMLConfiguration.WIDGET_ELEMENT,IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		widgetElem.setAttribute(IW3CXMLConfiguration.ID_ATTRIBUTE, getIdentifier());
		if (getVersion() != null && getVersion().length() > 0) widgetElem.setAttribute(IW3CXMLConfiguration.VERSION_ATTRIBUTE, getVersion());
		if (getHeight() != null && getHeight() > 0) widgetElem.setAttribute(IW3CXMLConfiguration.HEIGHT_ATTRIBUTE,String.valueOf(getHeight()));
		if (getWidth() != null && getWidth() > 0) widgetElem.setAttribute(IW3CXMLConfiguration.WIDTH_ATTRIBUTE,String.valueOf(getWidth()));
		if (getViewModes() != null) widgetElem.setAttribute(IW3CXMLConfiguration.MODE_ATTRIBUTE, getViewModes());
		
		
		// Name
		for (IName name: getNames()){
			Element nameElem = ((IElement) name).toXml();
			widgetElem.addContent(nameElem);
		}
		// Description
		for (IDescription description: getDescriptions()){
			widgetElem.addContent(((IElement) description).toXml());
		}
		// Author
		if (getAuthor() != null) widgetElem.addContent(((IElement) getAuthor()).toXml());
		
		// Update
		if (getUpdateLocation()!= null){
			widgetElem.addContent(new UpdateDescription(getUpdateLocation()).toXML());
		}
		
		
		// Licenses
		for (ILicense license: getLicenses()){
			widgetElem.addContent(((IElement) license).toXml());
		}
		
		// Icons
		for (IIcon icon:getIcons()){
			Element iconElem = ((IElement) icon).toXml();
			widgetElem.addContent(iconElem);				
		}
		
		// Access 
		for (IAccess access: getAccessList()){
			Element accessElem = ((IElement) access).toXml();
			widgetElem.addContent(accessElem);
		}
		
		// Content
		for (IContent content: getContentList()){
			Element contentElem = ((IElement) content).toXml();
			widgetElem.addContent(contentElem);			
		}
		
		// Features
		for (IFeature feature: getFeatures()){
			widgetElem.addContent(((IElement) feature).toXml());
		}
			
		// Preferences
		for (IPreference preference: getPreferences()){
			widgetElem.addContent(((IElement) preference).toXml());
		}
		
		return widgetElem;
	}
}
