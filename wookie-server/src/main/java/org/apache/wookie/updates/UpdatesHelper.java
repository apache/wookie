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
package org.apache.wookie.updates;

import java.util.List;

import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * View Helper for updates
 */
public class UpdatesHelper {

	/**
	 * Creates an XML serialization of a List of updates, including information about
	 * the widget as well as the update description document associated with it
	 * @param updates a List of UpdateInformation objects to render
	 * @return a String containing the XML serialization of the updates
	 */
	public static String createXML(
			List<UpdateInformation> updates) {
		Document document = new Document();
		// The returned XML uses the Widgets namespace
		Element root = new Element("updates",IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		for (UpdateInformation info: updates){
			// We use the toXml method on the UpdateInformation class to serialize each
			root.addContent(info.toXml());
		}
		document.setRootElement(root);
		XMLOutputter outputter = new XMLOutputter();
		return outputter.outputString(document);
	}

}
