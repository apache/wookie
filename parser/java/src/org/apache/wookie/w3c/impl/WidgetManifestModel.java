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
import org.apache.wookie.w3c.IAccessEntity;
import org.apache.wookie.w3c.IAuthorEntity;
import org.apache.wookie.w3c.IContentEntity;
import org.apache.wookie.w3c.IDescriptionEntity;
import org.apache.wookie.w3c.IFeatureEntity;
import org.apache.wookie.w3c.IIconEntity;
import org.apache.wookie.w3c.ILicenseEntity;
import org.apache.wookie.w3c.ILocalizedEntity;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.INameEntity;
import org.apache.wookie.w3c.IPreferenceEntity;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.util.IRIValidator;
import org.apache.wookie.w3c.util.NumberUtils;
import org.apache.wookie.w3c.util.RandomGUID;
import org.apache.wookie.w3c.util.UnicodeUtils;
import org.apache.wookie.w3c.util.WidgetPackageUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
/**
 * Processes a config.xml document to create a model
 * for a widget, including all sub-objects
 * @author Paul Sharples
 */
public class WidgetManifestModel implements W3CWidget {
	
	static Logger fLogger = Logger.getLogger(WidgetManifestModel.class.getName());
	
	private String fIdentifier;
	private String fVersion;
	private Integer fHeight;
	private Integer fWidth;
	private String fViewModes;
	private String fLang;
	private String[] features;
	private List<INameEntity> fNamesList;
	private List<IDescriptionEntity> fDescriptionsList;
	private IAuthorEntity fAuthor;
	private List<ILicenseEntity> fLicensesList;
	private List<IIconEntity> fIconsList;
	private List<IAccessEntity> fAccessList;
	private List<IContentEntity> fContentList;
	private List<IFeatureEntity> fFeaturesList;
	private List<IPreferenceEntity> fPreferencesList;
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
	public WidgetManifestModel (String xmlText, String[] locales, String[] features, String[] encodings, ZipFile zip) throws JDOMException, IOException, BadManifestException {		
		super();		
		this.zip = zip;
		this.features = features;
		this.supportedEncodings = encodings;
		fNamesList = new ArrayList<INameEntity>();
		fDescriptionsList = new ArrayList<IDescriptionEntity>();
		fLicensesList = new ArrayList<ILicenseEntity>();
		fIconsList = new ArrayList<IIconEntity>();
		fContentList = new ArrayList<IContentEntity>();
		fAccessList = new ArrayList<IAccessEntity>();
		fFeaturesList = new ArrayList<IFeatureEntity>();
		fPreferencesList = new ArrayList<IPreferenceEntity>();
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
				for (IIconEntity icon: fIconsList){
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
				for (IContentEntity content: fContentList){
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

	public String getViewModes() {
		return fViewModes;
	}
	
	public String getVersion() {
		return fVersion;
	}
	
	public List<IPreferenceEntity> getPrefences(){
		return fPreferencesList;
	}
	
	public List<IFeatureEntity> getFeatures(){
		return fFeaturesList;
	}
	
	public List<IAccessEntity> getAccessList(){
		return fAccessList;
	}
	
	public IAuthorEntity getAuthor(){
		return fAuthor;
	}

	public List<IContentEntity> getContentList() {
		return fContentList;
	}
	
	public List<IDescriptionEntity> getDescriptions(){
		return fDescriptionsList;
	}
	
	public List<INameEntity> getNames() {
		return fNamesList;
	}
	
	public List<IIconEntity> getIconsList() {
		return fIconsList;
	}

	public List<ILicenseEntity> getLicensesList() {
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
	
	public String getUpdate(){
		return fUpdate;
	}
	
	public void fromXML(Element element) throws BadManifestException{
		fLogger.warn("WidgetManifestModel.fromXML() called with no locales");
		fromXML(element, new String[]{"en"});
	}

	public String getLocalName(String locale){
		INameEntity name = (INameEntity)LocalizationUtils.getLocalizedElement(fNamesList.toArray(new INameEntity[fNamesList.size()]), new String[]{locale});
		if (name != null) return name.getName();
		return IW3CXMLConfiguration.UNKNOWN;
	}

	public void updateIconPaths(String path){
		for(IIconEntity icon : fIconsList){
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
			//give up & generate one
			RandomGUID r = new RandomGUID();
			fIdentifier = "http://incubator.apache.org/wookie/generated/" + r.toString();
		}
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
		// xml:lang optional
		fLang = element.getAttributeValue(IW3CXMLConfiguration.LANG_ATTRIBUTE, Namespace.XML_NAMESPACE);
		if(fLang == null){
			fLang = IW3CXMLConfiguration.DEFAULT_LANG;
		}

		
		// parse the children
		boolean foundContent = false;
		for(Object o : element.getChildren()) {
			Element child = (Element)o;
			String tag = child.getName();			

			// NAME IS OPTIONAL - get the name elements (multiple based on xml:lang)
			if(tag.equals(IW3CXMLConfiguration.NAME_ELEMENT)) {				
				INameEntity aName = new NameEntity();
				aName.fromXML(child);				
				// add it to our list only if its not a repetition of an
				// existing name for the locale
				if (isFirstLocalizedEntity(fNamesList,aName)) fNamesList.add(aName);
			}
			
			// DESCRIPTION IS OPTIONAL multiple on xml:lang
			if(tag.equals(IW3CXMLConfiguration.DESCRIPTION_ELEMENT)) {				
				IDescriptionEntity aDescription = new DescriptionEntity();
				aDescription.fromXML(child);
				// add it to our list only if its not a repetition of an
				// existing description for the locale and the language tag is valid
				if (isFirstLocalizedEntity(fDescriptionsList,aDescription) && aDescription.isValid()) fDescriptionsList.add(aDescription);
			}
			
			// AUTHOR IS OPTIONAL - can only be one, ignore subsequent repetitions
			if(tag.equals(IW3CXMLConfiguration.AUTHOR_ELEMENT) && fAuthor == null) {
				fAuthor = new AuthorEntity();
				fAuthor.fromXML(child);
			}	
			
			// UDPATE DESCRIPTION IS OPTONAL - can only be one, ignore subsequent repetitions
			if(tag.equals(IW3CXMLConfiguration.UPDATE_ELEMENT) && fUpdate == null) {
				UpdateDescription update = new UpdateDescription();
				update.fromXML(child);
				// It must have a valid HREF attribute, or it is ignored
				if (update.getHref() != null) fUpdate = update.getHref();
			}	
		
			// LICENSE IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.LICENSE_ELEMENT)) {				
				ILicenseEntity aLicense = new LicenseEntity();
				aLicense.fromXML(child);
				// add it to our list only if its not a repetition of an
				// existing entry for the locale and the language tag is valid
				if (isFirstLocalizedEntity(fLicensesList,aLicense) && aLicense.isValid()) fLicensesList.add(aLicense);
			}
			
			// ICON IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.ICON_ELEMENT)) {						
				IIconEntity anIcon = new IconEntity();
				anIcon.fromXML(child,locales,zip);
				if (anIcon.getSrc()!=null) fIconsList.add(anIcon);
			}
			
			// ACCESS IS OPTIONAL  can be many 
			if(tag.equals(IW3CXMLConfiguration.ACCESS_ELEMENT)) {											
				IAccessEntity access = new AccessEntity();
				access.fromXML(child);
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
				IFeatureEntity feature = new FeatureEntity(this.features);
				feature.fromXML(child);
				if (feature.getName()!=null) fFeaturesList.add(feature);
			}
			
			// PREFERENCE IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.PREFERENCE_ELEMENT)) {
				IPreferenceEntity preference = new PreferenceEntity();
				preference.fromXML(child);
				// Skip preferences without names
				if (preference.getName() != null){
					// Skip preferences already defined
					boolean found = false;
					for (IPreferenceEntity pref:getPrefences()){
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
	private boolean isFirstLocalizedEntity(List list, ILocalizedEntity ent){
		boolean first = true;
		for (ILocalizedEntity entity: (ILocalizedEntity[])list.toArray(new ILocalizedEntity[list.size()]))
			if (StringUtils.equals(entity.getLang(), ent.getLang())) first = false;
		return first;
	}

	public Element toXml() {
		Element widgetElem = new Element(IW3CXMLConfiguration.WIDGET_ELEMENT,IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		widgetElem.setAttribute(IW3CXMLConfiguration.ID_ATTRIBUTE, getIdentifier());
		if (getVersion() != null && getVersion().length() > 0) widgetElem.setAttribute(IW3CXMLConfiguration.VERSION_ATTRIBUTE, getVersion());
		if (getHeight() != null && getHeight() > 0) widgetElem.setAttribute(IW3CXMLConfiguration.HEIGHT_ATTRIBUTE,String.valueOf(getHeight()));
		if (getWidth() != null && getWidth() > 0) widgetElem.setAttribute(IW3CXMLConfiguration.WIDTH_ATTRIBUTE,String.valueOf(getWidth()));
		if (getViewModes() != null) widgetElem.setAttribute(IW3CXMLConfiguration.MODE_ATTRIBUTE, getViewModes());
		
		
		// Name
		for (INameEntity name: getNames()){
			Element nameElem = name.toXml();
			widgetElem.addContent(nameElem);
		}
		// Description
		for (IDescriptionEntity description: getDescriptions()){
			widgetElem.addContent(description.toXml());
		}
		// Author
		if (getAuthor() != null) widgetElem.addContent(getAuthor().toXml());
		
		// Update
		if (getUpdate()!= null){
			widgetElem.addContent(new UpdateDescription(getUpdate()).toXML());
		}
		
		
		// Licenses
		for (ILicenseEntity license: getLicensesList()){
			widgetElem.addContent(license.toXml());
		}
		
		// Icons
		for (IIconEntity icon:getIconsList()){
			Element iconElem = icon.toXml();
			widgetElem.addContent(iconElem);				
		}
		
		// Access 
		for (IAccessEntity access: getAccessList()){
			Element accessElem = access.toXml();
			widgetElem.addContent(accessElem);
		}
		
		// Content
		for (IContentEntity content: getContentList()){
			Element contentElem = content.toXml();
			widgetElem.addContent(contentElem);			
		}
		
		// Features
		for (IFeatureEntity feature: getFeatures()){
			widgetElem.addContent(feature.toXml());
		}
			
		// Preferences
		for (IPreferenceEntity preference: getPrefences()){
			widgetElem.addContent(preference.toXml());
		}
		
		return widgetElem;
	}
}
