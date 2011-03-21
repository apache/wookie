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

import org.apache.wookie.beans.IWhitelist;

public class WhitelistHelper {

	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/**
	 * Creates an XML return document 
	 * @param accessRequests
	 * @return
	 */
	public static String createXMLDocument(IWhitelist[] entries){
		String document = XMLDECLARATION;
		document += "\n<entries>\n";
		if (entries != null){
			for (IWhitelist entry:entries){
				document += toXml(entry);
			}
		}
		document += "</entries>\n";
		return document;
	}
	
	private static String toXml(IWhitelist entry){
		String xml = "\t<entry ";
		xml += "id=\""+entry.getId()+"\" ";
		xml += "url=\""+entry.getfUrl()+"\" ";
		xml += "/>\n";
		return xml;
	}
	
	public static String createJSON(IWhitelist[] entries){
		String json = "{";
		json +="\"entries\": [";
		if (entries != null){
			for (IWhitelist entry:entries){
				json += toJSON(entry);
			}
		}		
		// remove last comma
		json = json.substring(0, json.length()-1);
		json += "]}";
		return json;
	}
	
	public static String toJSON(IWhitelist entry){
		String json = "{\"id\": \""+ entry.getId() +"\"";
		json += ", \"url\":\"" + entry.getfUrl() + "\"},";
		return  json;
	}
	
	public static String createHTML(IWhitelist[] entries){
		String html = "<ul>\n";
		if (entries != null){
			for (IWhitelist entry:entries){
				html += toHTML(entry);				}
		}		
		html += "</ul>\n";
		return html;
	}
	
	public static String toHTML(IWhitelist entry){
		return "\t<li>"+entry.getfUrl()+"</li>\n";
	}
}
