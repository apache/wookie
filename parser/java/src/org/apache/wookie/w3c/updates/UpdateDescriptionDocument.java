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
package org.apache.wookie.w3c.updates;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.impl.AbstractLocalizedEntity;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

/**
 * An update description document (UDD) as defined at http://www.w3.org/TR/widgets-updates
 */
public class UpdateDescriptionDocument{
	
	private URL updateSource;
	private String versionTag;
	private ArrayList<Details> details;
	private URL baseUrl;
	
	/**
	 * Get the details of the update, typically a short description of any new features.
	 * @param locale the preferred locale for the details
	 * @return the update details
	 */
	public String getDetails(String locale) {
		Details localDetails = (Details) LocalizationUtils.getLocalizedElement(details.toArray(new Details[details.size()]), new String[]{locale});
		return localDetails.text;
	}
	
	/**
	 * Get the URL for the updated widget
	 * @return the URL
	 */
	public URL getUpdateSource() {
		return updateSource;
	}
	
	/**
	 * Get the version tag for the update
	 * @return the version tag
	 */
	public String getVersionTag() {
		return versionTag;
	}

	/**
	 * Manually construct a document. Only used for testing.
	 * @param details
	 * @param updateSource
	 * @param versionTag
	 */
	public UpdateDescriptionDocument(String details, URL updateSource, String versionTag){
		this.details = new ArrayList<Details>();
		Details d = new Details();
		d.text = details;
		this.details.add(d);
		this.updateSource = updateSource;
		this.versionTag = versionTag;
	}
	
	/**
	 * Load a UDD from a URL
	 * @param href the URL to load the UDD from
	 * @throws InvalidUDDException if the UDD cannot be found, or is not valid
	 */
	public UpdateDescriptionDocument(String href) throws InvalidUDDException{
		try {
			baseUrl = new URL(href);
			
			HttpClient client = new HttpClient();
			GetMethod method = new GetMethod(baseUrl.toString());
			method.setFollowRedirects(true);
			client.executeMethod(method);
			String type = method.getResponseHeader("Content-Type").getValue();
			int code = method.getStatusCode();
			
			if (code != 200) throw new InvalidUDDException("Bad HTTP response from update site: "+code);
			if (!type.equals("application/xml")) throw new InvalidUDDException("Bad content type in response from update site: "+type);
			
			Document doc;
			doc = new SAXBuilder().build(method.getResponseBodyAsStream());
			
			fromXML(doc);
			
		} catch (Exception e) {
			// If it is a specific UDDException, throw it, otherwise raise a generic UDD exception
			if (e instanceof InvalidUDDException) throw (InvalidUDDException)e;
			throw new InvalidUDDException("the document is not a valid UDD");
		}
	}
	
	/**
	 * Parse a UDD from XML
	 * @param document the XML document to parse
	 * @throws InvalidUDDException if the document is not a valid UDD
	 */
	public void fromXML(Document document) throws InvalidUDDException{
		if (document == null) throw new InvalidUDDException("No document found");
		Element root;
		try {
			root = document.getRootElement();
		} catch (Exception e1) {
			throw new InvalidUDDException("Root element must be <update-info>");
		}
		if (!root.getName().equals("update-info")) throw new InvalidUDDException("Root element must be <update-info>");
		if (root.getNamespace() != Namespace.getNamespace(IW3CXMLConfiguration.MANIFEST_NAMESPACE)) throw new InvalidUDDException("Wrong namespace for Update Description Document");
		if (root.getAttribute("version") == null) throw new InvalidUDDException("no version attribute");
		if (root.getAttribute("src") == null) throw new InvalidUDDException("no src attribute");
		versionTag = root.getAttributeValue("version");
		
		// Determine the update source URL.
		try {
			updateSource = new URL(root.getAttributeValue("src"));
		} catch (MalformedURLException e) {
			// If the URL is relative, try to make it absolute by using the URL where the UDD is obtained from as the base
			try {
				updateSource = new URL(baseUrl, root.getAttributeValue("src"));
			} catch (MalformedURLException e1) {
				throw new InvalidUDDException("src attribute is not a valid URL");
			}
		}
		List<?> detailsElements = root.getChildren("details", Namespace.getNamespace(IW3CXMLConfiguration.MANIFEST_NAMESPACE));
		this.details = new ArrayList<Details>();
		for (Object o: detailsElements){
			Details detailsItem = new Details();
			detailsItem.fromXML((Element) o);
			this.details.add(detailsItem);
		}
	}
	
	/**
	 * Inner class used to represent Details as a localized entity
	 */
	static class Details extends AbstractLocalizedEntity{
		public String text;
		
		public Details(){
		}

		@Override
		public void fromXML(Element element) {
			super.fromXML(element);
			this.text = getLocalizedTextContent(element);
		}

		public Element toXml() {
			Element element = new Element("details",IW3CXMLConfiguration.MANIFEST_NAMESPACE);
			element.setText(text);
			element = setLocalisationAttributes(element);
			return element;
		}
	}
}
