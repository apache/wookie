/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.apache.wookie.manifestmodel.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wookie.exceptions.BadManifestException;
import org.apache.wookie.manifestmodel.IAccessEntity;
import org.apache.wookie.manifestmodel.IAuthorEntity;
import org.apache.wookie.manifestmodel.IContentEntity;
import org.apache.wookie.manifestmodel.IDescriptionEntity;
import org.apache.wookie.manifestmodel.IFeatureEntity;
import org.apache.wookie.manifestmodel.IIconEntity;
import org.apache.wookie.manifestmodel.ILicenseEntity;
import org.apache.wookie.manifestmodel.IManifestModel;
import org.apache.wookie.manifestmodel.INameEntity;
import org.apache.wookie.manifestmodel.IPreferenceEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.apache.wookie.util.RandomGUID;
import org.jdom.Element;
import org.jdom.Namespace;
/**
 * @author Paul Sharples
 * @version $Id: WidgetManifestModel.java,v 1.1 2009-07-22 09:31:30 scottwilson Exp $
 */
public class WidgetManifestModel implements IManifestModel {
	
	static Logger fLogger = Logger.getLogger(WidgetManifestModel.class.getName());
	
	private String fIdentifier;
	private String fVersion;
	private int fHeight;
	private int fWidth;
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
		return fAuthor.getAuthorName();
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
	
	public String getFirstName(){
		if(fNamesList.size() > 0){
			return fNamesList.get(0).getName();
		}
		else {
			return IW3CXMLConfiguration.UNKNOWN;
		}
	}
	
	public String getFirstDescription(){
		if(fDescriptionsList.size() > 0){
			return fDescriptionsList.get(0).getDescription();
		}
		else {
			return IW3CXMLConfiguration.UNKNOWN;
		}
	}
	
	public List<IIconEntity> getIconsList() {
		return fIconsList;
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

	public int getHeight() {
		return fHeight;
	}

	public void setHeight(int height) {
		fHeight = height;
	}
	
	public int getWidth() {
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
	
	public String getTagName() {
		return IW3CXMLConfiguration.WIDGET_ELEMENT;
	}

	@SuppressWarnings("deprecation")
	public void fromJDOM(Element element) throws BadManifestException {						
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
		if(fIdentifier == null){
			//give up & generate one
			fIdentifier = RandomGUID.getUniqueID("generated-uid-");
		}
		// VERSION IS OPTIONAL		
		fVersion = element.getAttributeValue(IW3CXMLConfiguration.VERSION_ATTRIBUTE);
		if(fVersion == null){
			// give up 
			fVersion = IW3CXMLConfiguration.DEFAULT_WIDGET_VERSION;
		}
		// HEIGHT IS OPTIONAL		
		String height  = element.getAttributeValue(IW3CXMLConfiguration.HEIGHT_ATTRIBUTE);
		if(height != null){
			fHeight  = Integer.valueOf(height);
		}
		else{ 
			// give up
			fHeight = IW3CXMLConfiguration.DEFAULT_HEIGHT_LARGE;
		}
		// WIDTH IS OPTIONAL		
		String width  = element.getAttributeValue(IW3CXMLConfiguration.WIDTH_ATTRIBUTE);
		if(width != null){
			fWidth = Integer.valueOf(width);
		}
		else{
			// give up
			fWidth = IW3CXMLConfiguration.DEFAULT_WIDTH_LARGE;
		}
		// VIEWMODES IS OPTIONAL	
		fViewModes = element.getAttributeValue(IW3CXMLConfiguration.MODE_ATTRIBUTE);
		if(fViewModes == null){
			fViewModes = IW3CXMLConfiguration.DEFAULT_VIEWMODE;
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
				aName.fromJDOM(child);				
				// add it to our list
				fNamesList.add(aName);
			}
			
			// DESCRIPTION IS OPTIONAL multiple on xml:lang
			if(tag.equals(IW3CXMLConfiguration.DESCRIPTION_ELEMENT)) {				
				IDescriptionEntity aDescription = new DescriptionEntity();
				aDescription.fromJDOM(child);
				// add it to our list
				fDescriptionsList.add(aDescription);
			}
			
			// AUTHOR IS OPTIONAL - can only be one
			if(tag.equals(IW3CXMLConfiguration.AUTHOR_ELEMENT)) {
				fAuthor = new AuthorEntity();
				fAuthor.fromJDOM(child);
			}		
		
			// LICENSE IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.LICENSE_ELEMENT)) {				
				ILicenseEntity aLicense = new LicenseEntity();
				aLicense.fromJDOM(child);
				fLicensesList.add(aLicense);
			}
			
			// ICON IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.ICON_ELEMENT)) {						
				IIconEntity anIcon = new IconEntity();
				anIcon.fromJDOM(child);
				fIconsList.add(anIcon);
			}
			
			// ACCESS IS OPTIONAL  can be many 
			// (not sure if this has been removed from the spec?)
			if(tag.equals(IW3CXMLConfiguration.ACCESS_ELEMENT)) {											
				IAccessEntity access = new AccessEntity();
				access.fromJDOM(child);
				fAccessList.add(access);
			}
			
			// CONTENT IS OPTIONAL - can only be 0 or 1
			if(tag.equals(IW3CXMLConfiguration.CONTENT_ELEMENT)) {							
				fContent = new ContentEntity();						
				fContent.fromJDOM(child);
			}
			
			// FEATURE IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.FEATURE_ELEMENT)) {
				IFeatureEntity feature = new FeatureEntity();
				feature.fromJDOM(child);
				fFeaturesList.add(feature);
			}
			
			// PREFERENCE IS OPTIONAL - can be many
			if(tag.equals(IW3CXMLConfiguration.PREFERENCE_ELEMENT)) {
				IPreferenceEntity preference = new PreferenceEntity();
				preference.fromJDOM(child);
				fPreferencesList.add(preference);
			}
			
		}
	}


}
