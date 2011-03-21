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
package org.apache.wookie.helpers;

import org.apache.wookie.beans.IAccessRequest;

/**
 * Helper for rendering Widget Access Request Policies (WARP)
 */
public class AccessRequestHelper {
	
	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/**
	 * Creates an XML return document 
	 * @param accessRequests
	 * @return
	 */
	public static String createXMLAccessRequestDocument(IAccessRequest[] accessRequests){
		String document = XMLDECLARATION;
		document += "\n<policies>\n";
		if (accessRequests != null){
			for (IAccessRequest ar:accessRequests){
				document += toXml(ar);
			}
		}
		document += "</policies>\n";
		return document;
	}
	
	public static String createJSON(IAccessRequest[] accessRequests){
		String json = "{ \"policies\":[";
		if (accessRequests != null){
			for (IAccessRequest ar:accessRequests){
				json += toJSON(ar);
			}
		}
		// remove last comma
		json = json.substring(0, json.length()-1);
		json += "]}";
		
		return json;
	}
	
	/**
	 * Creates a HTML table with controls
	 * @param accessRequests
	 * @return
	 */
	public static String createAccessRequestHTMLTable(IAccessRequest[] accessRequests){		
		String document = "<table width=\"500\" class=\"ui-widget ui-widget-content\" align=\"center\">\n";
		document+= "<tr class=\"ui-widget-header\"><td colspan=\"5\">Policies</td></tr>  ";
		for (IAccessRequest ar:accessRequests){
			document += toHtml(ar);
		}
		document += "</table>\n";
		return document;
	}
	
	private static String toXml(IAccessRequest ar){
		String xml = "\t<policy ";
		xml += "id=\""+ar.getId()+"\" ";
		xml += "widget=\""+ar.getWidget().getId()+"\" ";
		xml += "widget_title=\""+WidgetHelper.getEncodedWidgetTitle(ar.getWidget(), null)+"\" ";
		xml += "origin=\""+ar.getOrigin()+"\" ";
		xml += "subdomains=\""+ar.isSubdomains()+"\" ";
		if (ar.isGranted()) {
			xml+= "granted=\"true\"";
		} else {
			xml+= "granted=\"false\"";			
		}
		xml += "/>\n";
		return xml;
	}
	
	private static String toJSON(IAccessRequest ar){
		String json = "{";
			json += " \"id\":\"" + ar.getId() + "\""; 
			json += ",\"widget\":\"" + ar.getWidget().getId() + "\""; 
			json += ",\"widget_title\":\"" + WidgetHelper.getEncodedWidgetTitle(ar.getWidget(), null) + "\""; 
			json += ",\"origin\":\"" + ar.getOrigin() + "\""; 	
			json += ",\"subdomains\":\"" + ar.isSubdomains() + "\""; 	
			if (ar.isGranted()) {
				json+= ",\"granted\": \"true\"";
			} else {
				json+= ",\"granted\": \"false\"";			
			}
		json += "},";
		return json;
	}
	
	public static String toHtml(IAccessRequest ar){
		String html = "";
		if (ar.isGranted()){
			html += "<tr style=\"background-color:#9FC\">";
		}else{	
			html += "<tr>";
		}
		html += "<td>"+ar.getWidget().getWidgetTitle("en")+"</td>";
		html += "<td>"+ar.getOrigin();
		if (ar.isSubdomains())
			html += " (and sub-domains)";	
		html += "</td>";
		if (ar.isGranted()){
			html+="<td><em>granted</em></td>";
			html+="<td><input type=\"button\" name=\"Submit\" value=\"revoke\" onClick=\"revoke("+ar.getId()+")\" class=\"ui-button ui-state-default ui-corner-all\"></td>";
		}else{	
			html+="<td><em>not granted</em></td>";
			html+="<td><input type=\"button\" name=\"Submit\" value=\"grant\" onClick=\"grant("+ar.getId()+")\" class=\"ui-button ui-state-default ui-corner-all\"></td>";
		}
		html += "</tr>\n";
		return html;		
	}
}
