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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wookie.beans.IDescription;
import org.apache.wookie.beans.ILicense;
import org.apache.wookie.beans.IName;
import org.apache.wookie.beans.IPreferenceDefault;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetIcon;
import org.apache.wookie.beans.IWidgetType;
import org.apache.wookie.util.WidgetFormattingUtils;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
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
	 * @return an XML representation of the widget as a String
	 */
	public static String createXMLWidgetsDocument(IWidget widget, String localIconPath, String[] locales){
		IWidget[] widgets = {widget};
		return createXMLWidgetsDocument(widgets, localIconPath, locales);
	}

	/**
	 * Generate a Widgets representation doc in XML for an array of widgets, e.g. a catalogue
	 * 
	 * @param widgets
	 * @param localIconPath
	 * @return an XML representation of the set of widgets as a String
	 */
	public static String createXMLWidgetsDocument(IWidget[] widgets, String localIconPath, String[] locales){
		String document = XMLDECLARATION;
		document += "\n<widgets>\n";
		for (IWidget widget:widgets){
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
	public static String toXml(IWidget widget, String localIconPath, String[] locales){
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
	
	private static String getWidgetElement(IWidget widget){
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
		+ "\" version=\"" + StringEscapeUtils.escapeXml(widget.getVersion())
		+ "\">\n";
		return out;
	}
	
	private static String getAuthor(IWidget widget){

	  if (widget.getAuthor() != null){
	    String out = "\t\t<author";
	    if (widget.getAuthor().getEmail() != null) out+= " email=\""+widget.getAuthor().getEmail()+"\"";
	    if (widget.getAuthor().getHref() != null) out+= " href=\""+widget.getAuthor().getHref()+"\"";
	    out += ">";
	    if (widget.getAuthor().getAuthor()!=null) out += StringEscapeUtils.escapeXml(widget.getAuthor().getAuthor());	    
	    out += "</author>\n";
	    return out;
	  } else {
	    return "";
	  }
	}
	
	private static String getLicenses(IWidget widget, String[] locales){
		String out = "";
		ILicense[] licenses = widget.getLicenses().toArray(new ILicense[widget.getLicenses().size()]);
		licenses = (ILicense[])LocalizationUtils.processElementsByLocales(licenses, locales, widget.getDefaultLocale());
		for (ILicense license: licenses){
			out +="\t\t<license ";
			if (license.getLang()!=null) out+=" xml:lang=\""+license.getLang()+"\"";
			if (license.getHref()!=null) out+=" href=\""+license.getHref()+"\"";
			if (license.getDir()!=null) out+=" dir=\""+license.getDir()+"\"";
			out+=">"+StringEscapeUtils.escapeXml(license.getText())+"</license>\n";
		}
		return out;
	}
	
	private static String getPreferences(IWidget widget){
		String out = "";
		for (IPreferenceDefault pref : widget.getPreferenceDefaults()) {
			out += "\t\t<preference name=\"" + pref.getPreference() + "\"  value=\""+pref.getValue()+"\"  readonly=\"" + (pref.isReadOnly()? "true" : "false") + "\"/>";
		}
		return out;
	}

	private static String getName(IWidget widget, String[] locales){
    	IName[] names = widget.getNames().toArray(new IName[widget.getNames().size()]);
		IName name = (IName)LocalizationUtils.getLocalizedElement(names,locales, widget.getDefaultLocale());
		String shortName = null;
		String longName = null;
		if (name != null) {
			shortName = name.getShortName();
			longName = name.getName();
		}
		String out = "\t\t<title "; 
		if (name != null && name.getDir()!=null) out+=" dir=\""+name.getDir()+"\"";
		if (shortName != null) out +=" short=\""+StringEscapeUtils.escapeXml(shortName) + "\"";
		out +=">";
		if(longName != null) out += StringEscapeUtils.escapeXml(longName); 
		out += "</title>\n";
		return out;
	}

	private static String getDescription(IWidget widget, String[] locales){
    	IDescription[] descriptions = widget.getDescriptions().toArray(new IDescription[widget.getDescriptions().size()]);
		IDescription desc = (IDescription)LocalizationUtils.getLocalizedElement(descriptions, locales, widget.getDefaultLocale());	
		String out = "\t\t<description";
		if (desc!= null && desc.getDir()!=null) out+=" dir=\""+desc.getDir()+"\"";
		out += ">";
		if (desc != null) out += StringEscapeUtils.escapeXml(desc.getContent());
		out += "</description>\n";
		return out;
	}
	
	private static String getTags(IWidget widget){
		String out = "";
		for (IWidgetType type:widget.getWidgetTypes()){
			out +="\t\t<category>"+type.getWidgetContext()+"</category>\n";
		}
		return out;
	}

	private static String getIcons(IWidget widget, String[] locales, String localIconPath){
		URL urlWidgetIcon = null;
		String out = "";
		IWidgetIcon[] icons;
		if (locales != null && locales.length != 0){
			icons = (IWidgetIcon[])LocalizationUtils.processElementsByLocales(widget.getWidgetIcons().toArray(new IWidgetIcon[widget.getWidgetIcons().size()]), locales, widget.getDefaultLocale());
		} else {
			icons = widget.getWidgetIcons().toArray(new IWidgetIcon[widget.getWidgetIcons().size()]);
		}
		if (icons!=null){
			for (IWidgetIcon icon: icons){
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
	
	/**
	 * Get the widget title in unicode for the specified locale. Note that this
	 * uses unicode control characters rather than <span dir..> tags
	 * @param widget the widget to get the title for
	 * @param locale the required locale, or null to use the default system locale
	 * @return a unicode string
	 */
	public static String getEncodedWidgetTitle(IWidget widget, String locale){
    	IName[] names = widget.getNames().toArray(new IName[widget.getNames().size()]);
        IName name = (IName)LocalizationUtils.getLocalizedElement(names, new String[]{locale}, widget.getDefaultLocale());
        return ((name != null) ? WidgetFormattingUtils.getEncoded(name.getDir(), name.getName()) : IW3CXMLConfiguration.UNKNOWN);   		
	}

}
