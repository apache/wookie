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

import org.apache.wookie.beans.Description;
import org.apache.wookie.beans.License;
import org.apache.wookie.beans.Name;
import org.apache.wookie.beans.PreferenceDefault;
import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetIcon;
import org.apache.wookie.beans.WidgetType;
import org.apache.wookie.w3c.util.LocalizationUtils;

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
	public static String createXMLWidgetsDocument(Widget widget, String localIconPath, String[] locales){
		Widget[] widgets = {widget};
		return createXMLWidgetsDocument(widgets, localIconPath, locales);
	}

	/**
	 * Generate a Widgets representation doc in XML for an array of widgets, e.g. a catalogue
	 * 
	 * @param widgets
	 * @param localIconPath
	 * @return
	 */
	public static String createXMLWidgetsDocument(Widget[] widgets, String localIconPath, String[] locales){
		String document = XMLDECLARATION;
		document += "\n<widgets>\n";
		for (Widget widget:widgets){
			document += toXml(widget, localIconPath, locales);
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
	public static String toXml(Widget widget, String localIconPath, String[] locales){
		String out = getWidgetElement(widget);
		out += getName(widget, locales);
		out += getDescription(widget, locales);
		out += getIcons(widget, locales, localIconPath);
		out += getTags(widget);
		out += getAuthor(widget);
		out += getLicenses(widget, locales);
		out += getPreferences(widget);
		out += "\t</widget>\n";	
		return out;
	}
	
	private static String getWidgetElement(Widget widget){
		if (widget == null) return null;
		String width = "";
		String height = "";
		if (widget.getWidth()!=null)  width = widget.getWidth().toString();
		if (widget.getHeight()!=null) height = widget.getHeight().toString();



		String out = "";

		out += "\t<widget " +
		"id=\""+widget.getId()
		+"\" identifier=\"" + widget.getGuid() 
		+"\" width=\"" + width
		+"\" height=\"" + height
		+ "\" version=\"" + widget.getVersion() 
		+ "\">\n";
		return out;
	}
	
	private static String getAuthor(Widget widget){
		String out = "\t\t<author";
		if (widget.getWidgetAuthorEmail() != null) out+= " email=\""+widget.getWidgetAuthorEmail()+"\"";
		if (widget.getWidgetAuthorHref() != null) out+= " href=\""+widget.getWidgetAuthorHref()+"\"";
		out += ">";
		if (widget.getWidgetAuthor()!=null) out += widget.getWidgetAuthor();
		out += "</author>\n";
		return out;
	}
	
	private static String getLicenses(Widget widget, String[] locales){
		String out = "";
		License[] licenses = License.findByValue("widget", widget);		
		licenses = (License[]) LocalizationUtils.processElementsByLocales(licenses, locales);
		for (License license: licenses){
			out +="\t\t<license ";
			if (license.getLang()!=null) out+=" xml:lang=\""+license.getLang()+"\"";
			if (license.getHref()!=null) out+=" href=\""+license.getHref()+"\"";
			if (license.getDir()!=null) out+=" dir=\""+license.getDir()+"\"";
			out+=">"+license.getText()+"</license>\n";
		}
		return out;
	}
	
	private static String getPreferences(Widget widget){
		String out = "";
		PreferenceDefault[] prefs = PreferenceDefault.findByValue("widget",widget);
		for (PreferenceDefault pref : prefs) {
			out += "\t\t<preference name=\"" + pref.getPreference() + "\"  value=\""+pref.getValue()+"\"  readonly=\"" + (pref.isReadOnly()? "true" : "false") + "\"/>";
		}
		return out;
	}

	private static String getName(Widget widget, String[] locales){
		Name name = (Name) LocalizationUtils.getLocalizedElement(Name.findByValue("widget", widget),locales);
		String shortName = null;
		String longName = null;
		if (name != null) {
			shortName = name.getShortName();
			longName = name.getName();
		}
		String out = "\t\t<title "; 
		if (name != null && name.getDir()!=null) out+=" dir=\""+name.getDir()+"\"";
		if (shortName != null) out +=" short=\""+shortName + "\"";
		out +=">";
		if(longName != null) out += longName; 
		out += "</title>\n";
		return out;
	}

	private static String getDescription(Widget widget, String[] locales){
		Description desc = (Description) LocalizationUtils.getLocalizedElement(Description.findByValue("widget", widget), locales);	
		String out = "\t\t<description";
		if (desc!= null && desc.getDir()!=null) out+=" dir=\""+desc.getDir()+"\"";
		out += ">";
		if (desc != null) out += desc.getContent();
		out += "</description>\n";
		return out;
	}
	
	private static String getTags(Widget widget){
		String out = "";
		WidgetType[] types = WidgetType.findByValue("widget", widget);
		for (WidgetType type:types){
			out +="\t\t<category>"+type.getWidgetContext()+"</category>\n";
		}
		return out;
	}

	private static String getIcons(Widget widget, String[] locales, String localIconPath){
		URL urlWidgetIcon = null;
		String out = "";
		WidgetIcon[] icons;
		if (locales != null && locales.length != 0){
			icons = (WidgetIcon[]) LocalizationUtils.processElementsByLocales(WidgetIcon.findForWidget(widget), locales);
		} else {
			icons = WidgetIcon.findForWidget(widget);
		}
		if (icons!=null){
			for (WidgetIcon icon: icons){
				try {
					// If local...
					if (!icon.getSrc().startsWith("http")) {
						urlWidgetIcon = new URL(localIconPath+icon.getSrc());
					} else {
						urlWidgetIcon = new URL(icon.getSrc());
					}
					out += "\t\t<icon";
					if (icon.getHeight()!=null) out += " height=\""+icon.getHeight()+"\"";
					if (icon.getWidth()!=null) out += " width=\""+icon.getWidth()+"\"";
					if (icon.getLang()!=null) out += " xml:lang=\""+icon.getLang()+"\"";
					out += ">"+urlWidgetIcon.toString() + "</icon>\n";
				} catch (MalformedURLException e) {
					// don't export icon field if its not a valid URL
				}
			}
		}
		return out;
	}

}
