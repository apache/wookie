/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.apache.wookie.helpers;

import org.apache.wookie.beans.IApiKey;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * View helper for API keys
 */
public class ApiKeyHelper {

  /**
   * Create an XML representation of a set of API keys
   * @param keys the keys to serialize
   * @return a String containing the XML serialization of the API keys
   */
  public static String createXML(IApiKey[] keys){
    Document document = new Document();
    Element keysElement = new Element("keys");

    for(IApiKey key: keys){
      Element keyElement = new Element("key");
      keyElement.setAttribute("id", String.valueOf(key.getId()));
      keyElement.setAttribute("value", key.getValue());
      keyElement.setAttribute("email", key.getEmail());
      keysElement.addContent(keyElement);
    }
    document.setRootElement(keysElement);
    XMLOutputter outputter = new XMLOutputter();
    return outputter.outputString(document);
  }
}
