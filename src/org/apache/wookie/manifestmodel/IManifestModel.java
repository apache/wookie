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

package org.apache.wookie.manifestmodel;

import java.util.List;

import org.apache.wookie.exceptions.BadManifestException;
import org.jdom.Element;

/**
 * @author Paul Sharples
 * @version $Id: IManifestModel.java,v 1.3 2009-09-02 18:37:31 scottwilson Exp $
 */
public interface IManifestModel extends IManifestModelBase{

	public IContentEntity getContent();

	public String getIdentifier();

	public List<IIconEntity> getIconsList();
	
	public List<ILicenseEntity> getLicensesList();

	public void setIconsList(List<IIconEntity> iconsList);

	public void updateIconPaths(String path);

	public String getLocalName(String locale);
	
	public String getLocalShortName(String locale);
	
	public List<INameEntity> getNames();

	public String getLocalDescription(String locale);

	public Integer getHeight();

	public Integer getWidth();

	public String getAuthor();

	public String getAuthorEmail();
	
	public String getAuthorHref();
	
	public String getFirstIconPath();

	public List<IPreferenceEntity> getPrefences();

	public List<IFeatureEntity> getFeatures();

	public String getVersion();

	public String getViewModes();

	public List<IDescriptionEntity> getDescriptions();
	
	public void setContent(IContentEntity content);
	
    /**
     * Unmarshall the given XML Element to this Object
     * 
     * @param element The Element to unmarshall
     * @param locales the set of supported locales
     */
	public void fromXML(Element element, String[] locales) throws BadManifestException;
}
