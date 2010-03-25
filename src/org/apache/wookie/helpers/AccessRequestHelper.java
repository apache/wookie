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

import org.apache.wookie.beans.AccessRequest;

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
	public static String createXMLAccessRequestDocument(AccessRequest[] accessRequests){
		String document = XMLDECLARATION;
		document += "\n<policies>\n";
		for (AccessRequest ar:accessRequests){
			document += toXml(ar);
		}
		document += "</policies>\n";
		return document;
	}
	
	/**
	 * Creates a HTML table with controls
	 * @param accessRequests
	 * @return
	 */
	public static String createAccessRequestHTMLTable(AccessRequest[] accessRequests){		
		String document = "<table width=\"500\" class=\"ui-widget ui-widget-content\" align=\"center\">\n";
		document+= "<tr class=\"ui-widget-header\"><td colspan=\"5\">Policies</td></tr>  ";
		for (AccessRequest ar:accessRequests){
			document += toHtml(ar);
		}
		document += "</table>\n";
		return document;
	}
	
	private static String toXml(AccessRequest ar){
		String xml = "\t<policy ";
		xml += "id=\""+ar.getId()+"\" ";
		xml += "widget=\""+ar.getWidget().getId()+"\" ";
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
	
	public static String toHtml(AccessRequest ar){
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
