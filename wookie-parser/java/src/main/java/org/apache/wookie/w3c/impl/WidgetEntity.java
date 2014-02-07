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

import java.util.Collection;
import java.util.List;

import org.apache.wookie.w3c.IAccess;
import org.apache.wookie.w3c.IAuthor;
import org.apache.wookie.w3c.IContent;
import org.apache.wookie.w3c.IDescription;
import org.apache.wookie.w3c.IFeature;
import org.apache.wookie.w3c.IIcon;
import org.apache.wookie.w3c.ILicense;
import org.apache.wookie.w3c.IName;
import org.apache.wookie.w3c.IPreference;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.util.LocalizationUtils;

/**
 * POJO for W3CWidget
 */
public abstract class WidgetEntity extends AbstractLocalizedEntity implements W3CWidget{

	protected String defaultLocale;
	protected String fIdentifier;
	protected String fVersion;
	protected Integer fHeight;
	protected Integer fWidth;
	protected String fViewModes;
	protected List<IName> fNamesList;
	protected List<IDescription> fDescriptionsList;
	protected IAuthor fAuthor;
	protected List<ILicense> fLicensesList;
	protected List<IIcon> fIconsList;
	protected List<IAccess> fAccessList;
	protected List<IContent> fContentList;
	protected List<IFeature> fFeaturesList;
	protected List<IPreference> fPreferencesList;
	protected String fUpdate;

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

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setUpdateLocation(java.lang.String)
	 */
	public void setUpdateLocation(String location) {
		this.fUpdate = location;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setHeight(java.lang.Integer)
	 */
	public void setHeight(Integer height) {
		this.fHeight = height;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setWidth(java.lang.Integer)
	 */
	public void setWidth(Integer width) {
		this.fWidth = width;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setIdentifier(java.lang.String)
	 */
	public void setIdentifier(String identifier) {
		this.fIdentifier = identifier;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setAuthor(org.apache.wookie.w3c.IAuthor)
	 */
	public void setAuthor(IAuthor author) {
		this.fAuthor = author;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setVersion(java.lang.String)
	 */
	public void setVersion(String version) {
		this.fVersion = version;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setFeatures(java.util.Collection)
	 */
	public void setFeatures(Collection<IFeature> features) {
		this.fFeaturesList = (List<IFeature>) features;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setIcons(java.util.Collection)
	 */
	public void setIcons(Collection<IIcon> icons) {
		this.fIconsList = (List<IIcon>) icons;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setLicenses(java.util.Collection)
	 */
	public void setLicenses(Collection<ILicense> licenses) {
		this.fLicensesList = (List<ILicense>) licenses;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setNames(java.util.Collection)
	 */
	public void setNames(Collection<IName> names) {
		this.fNamesList = (List<IName>) names;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setDescriptions(java.util.Collection)
	 */
	public void setDescriptions(Collection<IDescription> descriptions) {
		this.fDescriptionsList = (List<IDescription>) descriptions;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setContentList(java.util.Collection)
	 */
	public void setContentList(Collection<IContent> contents) {
		this.fContentList = (List<IContent>) contents;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setPreferences(java.util.Collection)
	 */
	public void setPreferences(Collection<IPreference> preferenceDefaults) {
		this.fPreferencesList = (List<IPreference>) preferenceDefaults;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#getLocalName(java.lang.String)
	 */
	public String getLocalName(String locale){
		NameEntity name = (NameEntity)LocalizationUtils.getLocalizedElement(fNamesList.toArray(new NameEntity[fNamesList.size()]), new String[]{locale}, defaultLocale);
		if (name != null) return name.getName();
		return IW3CXMLConfiguration.UNKNOWN;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.W3CWidget#setDefaultLocale(java.lang.String)
	 */
	public void setDefaultLocale(String locale) {
		this.defaultLocale = locale;
	}

}
