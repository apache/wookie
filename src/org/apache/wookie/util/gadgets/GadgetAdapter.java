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
package org.apache.wookie.util.gadgets;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.wookie.w3c.IAccessEntity;
import org.apache.wookie.w3c.IAuthorEntity;
import org.apache.wookie.w3c.IContentEntity;
import org.apache.wookie.w3c.IDescriptionEntity;
import org.apache.wookie.w3c.IFeatureEntity;
import org.apache.wookie.w3c.IIconEntity;
import org.apache.wookie.w3c.ILicenseEntity;
import org.apache.wookie.w3c.INameEntity;
import org.apache.wookie.w3c.IPreferenceEntity;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.impl.AuthorEntity;
import org.apache.wookie.w3c.impl.ContentEntity;
import org.apache.wookie.w3c.impl.DescriptionEntity;
import org.apache.wookie.w3c.impl.IconEntity;
import org.apache.wookie.w3c.impl.NameEntity;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.jdom.Element;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An adapter class used for building a W3C Widget model using Gadget metadata
 */
public class GadgetAdapter implements W3CWidget {
	
	private String fIdentifier;
	private String fVersion;
	private Integer fHeight;
	private Integer fWidth;
	private ArrayList<INameEntity> fNamesList;
	private ArrayList<IDescriptionEntity> fDescriptionsList;
	private IAuthorEntity fAuthor;
	private ArrayList<IIconEntity> fIconsList;
	private ArrayList<IContentEntity> fContentList;
	
	/**
	 * The default Gadget icon
	 */
	private static final String DEFAULT_ICON = "/wookie/shared/images/defaultwidget.png";

	public GadgetAdapter(JSONObject gadget, String shindig) throws Exception {
		if (gadget.has("errors")) throw new Exception("Invalid gadget - Shindig error");
		if (!gadget.has("url")) throw new Exception("Invalid gadget - URL missing");
		if (gadget.getString("url") == null || gadget.getString("url").equals("")) throw new Exception("Invalid gadget - Invalid URL");
		try {
			@SuppressWarnings("unused")
			URL url = new URL(gadget.getString("url"));
		} catch (Exception e) {
			throw new Exception("Invalid gadget - invalid URL");
		}
		
		// Start file
		// We should be able to use the "iframeUrl" property here, but
		// it isn't very reliable at generating a usable value, so we construct
		// a very basic URL instead
		// FIXME we need to use real locales in these URLs
		this.fContentList = new ArrayList<IContentEntity>();
		String url = (shindig+"/gadgets/ifr?url="+gadget.getString("url")+"&amp;lang=en&amp;country=UK&amp;view=home");
		ContentEntity content = new ContentEntity(url, "UTF-8",IW3CXMLConfiguration.DEFAULT_MEDIA_TYPE);
		fContentList.add(content);
		
		// Identifier
		this.fIdentifier = gadget.getString("url");
		
		// Height and Width
		fHeight = 200;
		fWidth = 320;
		if (gadget.has("height")) if (gadget.getInt("height") != 0) fHeight = gadget.getInt("height");
		if (gadget.has("width")) if (gadget.getInt("width") != 0) fWidth = gadget.getInt("width");

		// Author
		String authorName = getValue(gadget, "author", "Unknown Author");
		AuthorEntity author = new AuthorEntity(authorName, null, null);
		fAuthor = author;

		// Name
		this.fNamesList = new ArrayList<INameEntity>();
		String title = getValue(gadget, "title", "Untitled Gadget");
		// Override the title with directory title if present (this is intended for gallery use)
		title = getValue(gadget, "directory_title", title);
		NameEntity name = new NameEntity();
		name.setName(title);
		this.fNamesList.add(name);

		// Description
		this.fDescriptionsList = new ArrayList<IDescriptionEntity>();
		String description = getValue(gadget, "description", "Google Gadget");
		DescriptionEntity desc = new DescriptionEntity();
		desc.setDescription(description);
		this.fDescriptionsList.add(desc);
		
		// Icons
		this.fIconsList = new ArrayList<IIconEntity>();
		String icon = getValue(gadget,"thumbnail", DEFAULT_ICON);	
		IconEntity iconEntity = new IconEntity(icon,null,null);
		this.fIconsList.add(iconEntity);
	}
	
	private static String getValue(JSONObject gadget, String property, String defaultValue) throws JSONException{
		if (gadget.has(property)){
			if (gadget.getString(property)!=null){
				if (!(gadget.getString(property).trim()).equals("")){
					return gadget.getString(property);
				}
			}
		}
		return defaultValue;
	}

	public String getViewModes() {
		return null;
	}
	
	public String getVersion() {
		return fVersion;
	}
	
	public List<IPreferenceEntity> getPrefences(){
		return new ArrayList<IPreferenceEntity>();

	}
	
	public List<IFeatureEntity> getFeatures(){
		return new ArrayList<IFeatureEntity>();

	}
	
	public List<IAccessEntity> getAccessList(){
		return new ArrayList<IAccessEntity>();

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
		return new ArrayList<ILicenseEntity>();
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

	public void fromXML(Element element) throws BadManifestException {
	}

	public String getXMLTagName() {
		return null;
	}

	public String getLocalName(String locale) {
		INameEntity name = (INameEntity)LocalizationUtils.getLocalizedElement(fNamesList.toArray(new INameEntity[fNamesList.size()]), new String[]{locale});
		if (name != null) return name.getName();
		return IW3CXMLConfiguration.UNKNOWN;
	}

	public String getUpdate() {
		return null;
	}
	
	

}
