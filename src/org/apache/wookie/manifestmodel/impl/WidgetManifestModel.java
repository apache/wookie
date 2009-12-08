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

package org.apache.wookie.manifestmodel.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wookie.beans.ServerFeature;
import org.apache.wookie.exceptions.BadManifestException;
import org.apache.wookie.manifestmodel.IAccessEntity;
import org.apache.wookie.manifestmodel.IAuthorEntity;
import org.apache.wookie.manifestmodel.IContentEntity;
import org.apache.wookie.manifestmodel.IDescriptionEntity;
import org.apache.wookie.manifestmodel.IFeatureEntity;
import org.apache.wookie.manifestmodel.IIconEntity;
import org.apache.wookie.manifestmodel.ILicenseEntity;
import org.apache.wookie.manifestmodel.ILocalizedEntity;
import org.apache.wookie.manifestmodel.IManifestModel;
import org.apache.wookie.manifestmodel.INameEntity;
import org.apache.wookie.manifestmodel.IParamEntity;
import org.apache.wookie.manifestmodel.IPreferenceEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.apache.wookie.util.IRIValidator;
import org.apache.wookie.util.NumberUtils;
import org.apache.wookie.util.RandomGUID;
import org.apache.wookie.util.UnicodeUtils;
import org.apache.wookie.util.WidgetPackageUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
/**
 * @author Paul Sharples
 * @version $Id: WidgetManifestModel.java,v 1.3 2009-09-02 18:37:31 scottwilson Exp $
 */
public class WidgetManifestModel implements IManifestModel {
	
	static Logger fLogger = Logger.getLogger(WidgetManifestModel.class.getName());
	
	private String fIdentifier;
	private String fVersion;
	private Integer fHeight;
	private Integer fWidth;
	private String fViewModes;
	private String fLang;
	private List<INameEntity> fNamesList;
	private List<IDescriptionEntity> fDescriptionsList;
	private IAuthorEntity fAuthor;
	private List<ILicenseEntity> fLicensesList;
	private List<IIconEntity> fIconsList;
	private List<IAccessEntity> fAccessList;
	private IContentEntity fContent;
	private List<IFeatureEntity> fFeaturesList;
	private List<IPreferenceEntity> fPreferencesList;
	
	private ZipFile zip;

	public WidgetManifestModel() {
		super();
		fNamesList = new ArrayList<INameEntity>();
		fDescriptionsList = new ArrayList<IDescriptionEntity>();
		fLicensesList = new ArrayList<ILicenseEntity>();
		fIconsList = new ArrayList<IIconEntity>();
		fAccessList = new ArrayList<IAccessEntity>();
		fFeaturesList = new ArrayList<IFeatureEntity>();
		fPreferencesList = new ArrayList<IPreferenceEntity>();
	}
	
	/**
	 * Constructs a new WidgetManifestModel using an XML manifest supplied as a String.
	 * @param xmlText the XML manifest file
	 * @throws JDOMException
	 * @throws IOException
	 * @throws BadManifestException
	 */
	public WidgetManifestModel (String xmlText, String[] locales, ZipFile zip) throws JDOMException, IOException, BadManifestException {		
		super();		
		this.zip = zip;
		fNamesList = new ArrayList<INameEntity>();
		fDescriptionsList = new ArrayList<IDescriptionEntity>();
		fLicensesList = new ArrayList<ILicenseEntity>();
		fIconsList = new ArrayList<IIconEntity>();
		fAccessList = new ArrayList<IAccessEntity>();
		fFeaturesList = new ArrayList<IFeatureEntity>();
		fPreferencesList = new ArrayList<IPreferenceEntity>();
		SAXBuilder builder = new SAXBuilder();
		Element root = builder.build(new StringReader(xmlText)).getRootElement();				
		fromXML(root,locales);	
		
		// Add default icons
		for (String iconpath:WidgetPackageUtils.locateAllDefaultIcons(zip, locales)){
			if (iconpath != null) {
				// don't add it if its a duplicate
				boolean exists = false;
				for (IIconEntity icon: fIconsList){
					if (icon.getSrc().equals(iconpath)) exists = true;
				}
				if (!exists) fIconsList.add(new IconEntity(iconpath,null,null));	
			}
		}
		//Uncomment this when performing conformance testing
		//outputFeatureList();
	}
	
	/**
	 * Used to check output during conformance testing
	 */
	private void outputFeatureList(){
		if (fFeaturesList.size()==0) return;
		String out = "";
		out+=("id:"+this.fIdentifier+":"+this.getLocalName("en"));
		for (IFeatureEntity feature: fFeaturesList){
			String params = "";
			for (IParamEntity param:feature.getParams()){
				params+="["+param.getName()+":"+param.getValue()+"]";
			}
			out+=("feature:"+feature.getName()+"required="+feature.isRequired()+"{"+params+"}");
		}
		System.out.println(out);
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
	
	public String getFirstIconPath(){
		if(fIconsList.size() > 0){
			return fIconsList.get(0).getSrc();
		}
		else {
			return IW3CXMLConfiguration.DEFAULT_ICON_PATH;
		}
	}
	
	public String getAuthor(){
		if (fAuthor == null) return null;
		return fAuthor.getAuthorName();
	}
	
	public String getAuthorEmail() {
		if (fAuthor == null) return null;
		return fAuthor.getEmail();
	}

	public String getAuthorHref() {
		if (fAuthor == null) return null;
		return fAuthor.getHref();
	}

	public void updateIconPaths(String path){
		for(IIconEntity icon : fIconsList){
			if(!icon.getSrc().startsWith("http:")){
				icon.setSrc(path + icon.getSrc());
			}
		}
	}
	
	public List<IDescriptionEntity> getDescriptions(){
		return fDescriptionsList;
	}
	
	public List<INameEntity> getNames() {
		return fNamesList;
	}
	
	public String getLocalName(String locale){
		INameEntity name = getLocalNameEntity(locale);
		if (name != null) return name.getName();
		return IW3CXMLConfiguration.UNKNOWN;
	}
	
	public String getLocalShortName(String locale){
		INameEntity name = getLocalNameEntity(locale);
		if (name != null) return name.getShort();
		return IW3CXMLConfiguration.UNKNOWN;		
	}
	
	private INameEntity getLocalNameEntity(String locale){
		INameEntity nonLocalized = null;
		for (INameEntity name:fNamesList.toArray(new INameEntity[fNamesList.size()])){
			if (name.getLanguage().equals(locale)) return name;
			if (name.getLanguage().equals("")) nonLocalized = name;
		}
		return nonLocalized;
	}
	
	public String getLocalDescription(String locale){
		String nonlocalizedvalue = "";
		for (IDescriptionEntity desc:fDescriptionsList.toArray(new IDescriptionEntity[fDescriptionsList.size()])){
			if (desc.getLanguage().equals(locale)) return desc.getDescription();
			if (desc.getLanguage().equals("")) nonlocalizedvalue = desc.getDescription();
		}
		if (nonlocalizedvalue == null) return IW3CXMLConfiguration.UNKNOWN;
		return nonlocalizedvalue;	
	}
	
	public List<IIconEntity> getIconsList() {
		return fIconsList;
	}

	public List<ILicenseEntity> getLicensesList() {
		return fLicensesList;
	}

	
	public void setIconsList(List<IIconEntity> iconsList) {
		fIconsList = iconsList;
	}
	
	public String getIdentifier() {
		return fIdentifier;
	}
	
	public IContentEntity getContent() {
		return fContent;
	}

	public void setContent(IContentEntity content) {
		fContent = content;
	}

	public Integer getHeight() {
		return fHeight;
	}

	public void setHeight(int height) {
		fHeight = height;
	}
	
	public Integer getWidth() {
		return fWidth;
	}

	public void setWidth(int width) {
		fWidth = width;
	}

	public boolean hasContentEntity(){
		if(fContent == null){
			return false;
		}
		return true;
	}
	
	public String getXMLTagName() {
		return IW3CXMLConfiguration.WIDGET_ELEMENT;
	}
	
	public void fromXML(Element element) throws BadManifestException{
		fLogger.warn("WidgetManifestModel.fromXML() called with no locales");
		fromXML(element, new String[]{"en"});
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
			fIdentifier = "generated-uid-" + r.toString();
		}
		// VERSION IS OPTIONAL		
		fVersion = element.getAttributeValue(IW3CXMLConfiguration.VERSION_ATTRIBUTE);
		if(fVersion == null){
			// give up 
			fVersion = IW3CXMLConfiguration.DEFAULT_WIDGET_VERSION;
		} else {
			fVersion = UnicodeUtils.normalizeSpaces(fVersion);
		}
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
		}
		// xml:lang optional
		fLang = element.getAttributeValue(IW3CXMLConfiguration.LANG_ATTRIBUTE, Namespace.XML_NAMESPACE);
		if(fLang == null){
			fLang = IW3CXMLConfiguration.DEFAULT_LANG;
		}

		
		// parse the children
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
				// existing description for the locale
				if (isFirstLocalizedEntity(fDescriptionsList,aDescription)) fDescriptionsList.add(aDescription);
			}
			
			// AUTHOR IS OPTIONAL - can only be one, ignore subsequent repetitions
			if(tag.equals(IW3CXMLConfiguration.AUTHOR_ELEMENT) && fAuthor == null) {
				fAuthor = new AuthorEntity();
				fAuthor.fromXML(child);
			}		
		
			// LICENSE IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.LICENSE_ELEMENT)) {				
				ILicenseEntity aLicense = new LicenseEntity();
				aLicense.fromXML(child);
				// add it to our list only if its not a repetition of an
				// existing entry for the locale
				if (isFirstLocalizedEntity(fLicensesList,aLicense)) fLicensesList.add(aLicense);
			}
			
			// ICON IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.ICON_ELEMENT)) {						
				IIconEntity anIcon = new IconEntity();
				anIcon.fromXML(child,locales,zip);
				if (anIcon.getSrc()!=null) fIconsList.add(anIcon);
			}
			
			// ACCESS IS OPTIONAL  can be many 
			// (not sure if this has been removed from the spec?)
			if(tag.equals(IW3CXMLConfiguration.ACCESS_ELEMENT)) {											
				IAccessEntity access = new AccessEntity();
				access.fromXML(child);
				fAccessList.add(access);
			}
			
			// CONTENT IS OPTIONAL - can only be 0 or 1
			// Only the first valid CONTENT element should be considered, further instances MUST be ignored
			if(tag.equals(IW3CXMLConfiguration.CONTENT_ELEMENT)) {	
				if (fContent == null){
					fContent = new ContentEntity();						
					fContent.fromXML(child,zip);
				}
			}
			
			// FEATURE IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.FEATURE_ELEMENT)) {
				IFeatureEntity feature = new FeatureEntity();
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
			if (StringUtils.equals(entity.getLanguage(), ent.getLanguage())) first = false;
		return first;
	}
}
