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

package org.apache.wookie.util;

import java.io.IOException;
import java.io.StringReader;
import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.exceptions.BadManifestException;
import org.apache.wookie.manifestmodel.IManifestModel;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.apache.wookie.manifestmodel.impl.WidgetManifestModel;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Manifest Helper class - methods for parsing a w3c widget manifest.
 * 
 * @author Paul Sharples
 * @version $Id: WidgetManifestUtils.java,v 1.1 2009-09-02 18:37:31 scottwilson Exp $ 
 *
 */
public class WidgetManifestUtils implements IW3CXMLConfiguration {

	static Logger _logger = Logger.getLogger(WidgetManifestUtils.class.getName());

	public static IManifestModel dealWithManifest(String xmlText, Messages localizedMessages) throws JDOMException, IOException, BadManifestException {
		SAXBuilder builder = new SAXBuilder();
		Element root = builder.build(new StringReader(xmlText)).getRootElement();				
		IManifestModel manifestModel = new WidgetManifestModel();
		manifestModel.fromXML(root);
		return manifestModel;		
	}
}