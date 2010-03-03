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
package org.apache.wookie.w3c.util;

import org.jdom.Element;
import org.jdom.Text;

/**
 * General XML utilities
 * @author scott
 *
 */
public class XmlUtils {
	
	/**
	 * Returns the text content of an element, recursively adding
	 * any text nodes found in its child nodes
	 * @param element
	 * @return a string
	 */
	public static String getTextContent(Element element){
		String content = "";
		for (Object node:element.getContent()){
			if (node instanceof Element){
				content += getTextContent((Element)node);
			}
			if (node instanceof Text){
				content += ((Text)node).getText();
			}
		}
		return content;
	}

}
