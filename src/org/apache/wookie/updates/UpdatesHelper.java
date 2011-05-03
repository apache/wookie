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

	public static String createXML(
			List<UpdateInformation> updates) {
		Document document = new Document();
		Element root = new Element("updates",IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		for (UpdateInformation info: updates){
			root.addContent(info.toXml());
		}
		document.setRootElement(root);
		XMLOutputter outputter = new XMLOutputter();
		return outputter.outputString(document);
	}

}
