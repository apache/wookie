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

import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.proxy.Policy;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * View Helper class for creating representations of Policy objects
 */
public class PoliciesHelper {
  
  /**
   * Creates an XML representation of an array of policies
   * @param policies the array of policies to represent
   * @return the XML representation as a String
   */
  public static String toXml(Policy[] policies){
    Document document = new Document();
    Element policiesElement = new Element("policies");
    for(Policy policy:policies){
      Element policyElement = new Element("policy");
      policyElement.setAttribute("scope", policy.getScope());
      policyElement.setAttribute("origin",policy.getOrigin());
      policyElement.setAttribute("directive", policy.getDirective());
      policiesElement.addContent(policyElement);
    }
    document.setRootElement(policiesElement);
    XMLOutputter outputter = new XMLOutputter();
    return outputter.outputString(document);
  }
  
  /**
   * Creates a JSON representaion of an array of policies
   * @param policies the array of policies to represent
   * @return the JSON representation as a String
   */
  public static String toJson(Policy[] policies){
    JSONObject object = null;
    try {
      object = new JSONObject();
      JSONArray policiesJson = new JSONArray();
      for(Policy policy: policies){
        JSONObject policyJson = new JSONObject();
        policyJson.put("scope", policy.getScope());
        if (!policy.getScope().equals("*")){
          IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
          IWidget widget = persistenceManager.findWidgetByGuid(policy.getScope());
          if (widget != null){
            policyJson.put("widget-title", WidgetAdvertHelper.getEncodedWidgetTitle(widget, null));
          }
        }
        policyJson.put("origin", policy.getOrigin());
        policyJson.put("directive", policy.getDirective());
        policiesJson.put(policyJson);
      }
      object.put("policies",policiesJson);
    } catch (JSONException e) {
      // 
      e.printStackTrace();
    }
    return object.toString(); 
  }

  /**
   * Createa a HTML representation of an array of policies
   * @param policies
   * @return the HTML representation as a String
   */
  public static String toHtml(Policy[] policies){
    String document = "<table width=\"500\" class=\"ui-widget ui-widget-content\" align=\"center\">\n";
    document+= "<tr class=\"ui-widget-header\"><td colspan=\"5\">Policies</td></tr>  ";
    document+= "<tr><th>Scope</th><th>Origin</th><th>Directive</th></tr>  ";
    for (Policy policy:policies){
      if (policy.getDirective().equals("ALLOW")){
        document += "<tr class=\"policy-line\" style=\"color:white;background-color:green\">";
      }else{  
        document += "<tr class=\"policy-line\" style=\"color:white;background-color:red\">";
      }
      document += "<td class=\"policy-scope\">"+policy.getScope()+"</td>";
      document += "<td class=\"policy-origin\">"+policy.getOrigin()+"</td>";
      document += "<td class=\"policy-directive\">"+policy.getDirective()+"</td>";
      document += "</tr>\n";
    }
    document += "</table>\n";
    return document;
  }
}
