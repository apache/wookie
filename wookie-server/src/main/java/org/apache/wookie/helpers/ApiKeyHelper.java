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

import org.apache.wookie.server.security.ApiKey;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * View helper for API keys
 */
public class ApiKeyHelper {
  
  static Logger logger = Logger.getLogger(ApiKeyHelper.class.getName());

  /**
   * Create an XML representation of a set of API keys
   * @param keys the keys to serialize
   * @return a String containing the XML serialization of the API keys
   */
  public static String toXml(ApiKey[] keys){
    Document document = new Document();
    Element keysElement = new Element("keys");

    for(ApiKey key: keys){
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
  
  /**
   * Create a JSON representation of a set of API keys
   * @param keys the keys to serialize
   * @return a JSON String of the keys
   */
  public static String toJson(ApiKey[] keys){
    JSONArray json = new JSONArray();
    for(ApiKey key: keys){
      JSONObject jsonKey = new JSONObject();
      try {
        jsonKey.put("id", key.getId());
        jsonKey.put("value", key.getValue());
        jsonKey.put("email", key.getEmail());
      } catch (JSONException e) {
        logger.error("Problem rendering json for ApiKey object", e);
      }
      json.put(jsonKey);
    }
    return json.toString();
  }
}
