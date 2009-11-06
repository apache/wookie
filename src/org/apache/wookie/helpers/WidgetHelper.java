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

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.wookie.beans.PreferenceDefault;
import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetType;

/**
 * A helper for Widgets
 * @author scott wilson
 *
 */
public class WidgetHelper {
	
	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	/**
	 * Generate a Widgets representation doc in XML for a single widget
	 * @param widget
	 * @param localIconPath
	 * @return
	 */
	public static String createXMLWidgetsDocument(Widget widget, String localIconPath){
		Widget[] widgets = {widget};
		return createXMLWidgetsDocument(widgets, localIconPath);
	}
	
	/**
	 * Generate a Widgets representation doc in XML for an array of widgets, e.g. a catalogue
	 * 
	 * @param widgets
	 * @param localIconPath
	 * @return
	 */
	public static String createXMLWidgetsDocument(Widget[] widgets, String localIconPath){
		String document = XMLDECLARATION;
		document += "\n<widgets>\n";
		for (Widget widget:widgets){
			document += toXml(widget, localIconPath);
		}
		document += "</widgets>\n";
		return document;
	}
	
	/**
	 * Returns an XML representation of the given widget.
	 * 
	 * @param widget the widget to represent
	 * @param localIconPath the local path to prefix any local icons, typically the server URL
	 * @return the XML representation of the widget
	 */
	public static String toXml(Widget widget, String localIconPath){
		if (widget == null) return null;
		String out = "";
		URL urlWidgetIcon = null;
		out += "\t<widget id=\""+widget.getId()+"\" identifier=\"" + widget.getGuid() + "\" version=\""
				+ widget.getVersion() + "\">\n";
		out += "\t\t<title short=\""+widget.getWidgetShortName()+"\">" + widget.getWidgetTitle() + "</title>\n";
		out += "\t\t<description>" + widget.getWidgetDescription()
				+ "</description>\n";
		String iconPath = widget.getWidgetIconLocation();
		
		try {
			// If local...
			if (!iconPath.startsWith("http")) {
				urlWidgetIcon = new URL(localIconPath+iconPath);
			} else {
				urlWidgetIcon = new URL(iconPath);
			}
			out += "\t\t<icon>" + urlWidgetIcon.toString() + "</icon>\n";
		} catch (MalformedURLException e) {
			// don't export icon field if its not a valid URL
		}
		
		// Do tags/services/categories
		WidgetType[] types = WidgetType.findByValue("widget", widget);
		for (WidgetType type:types){
			out +="\t\t<category>"+type.getWidgetContext()+"</category>\n";
		}

		// Do preference defaults
		PreferenceDefault[] prefs = PreferenceDefault.findByValue("widget",
				widget);
		for (PreferenceDefault pref : prefs) {
			out += "\t\t<preference name=\"" + pref.getPreference() + "\"/>";
		}
		out += "\t</widget>\n";

		return out;
	}

}
