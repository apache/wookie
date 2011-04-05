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

import org.apache.wookie.w3c.IAuthorEntity;
import org.apache.wookie.w3c.IDescriptionEntity;
import org.apache.wookie.w3c.ILicenseEntity;
import org.apache.wookie.w3c.INameEntity;
import org.apache.wookie.w3c.W3CWidget;

/**
 * i18n formatting utilities
 * 
 * <p>The methods in this class can be used to obtain strings
 * that have been processed for i18n content to be presented 
 * in different ways, for example using CSS styling or unicode
 * control characters</p>
 * 
 * <p>The <em>getFormattedXYZ</em> methods generate i18n strings using
 * CSS bidi properties for use in display. This involves inserting HTML 
 * &lt;span&gt; tags containing CSS styling properties for text direction</p>
 * 
 * <p>The <em>getEncodedXYZ</em> methods generate unicode Strings
 * using unicode control characters, and remove any embedded &lt;span&gt; tags</p>
 * 
 */
public class FormattingUtils {
	
	public final static String LTR = "\u202a";
	public final static String RTL = "\u202b";
	public final static String LRO = "\u202d";
	public final static String RLO = "\u202e";
	public final static String END = "\u202c";
	
	/**
	 * Returns the CSS formatted i18n string for the widget name
	 * @param name the Widget's Name entity
	 * @return a CSS-formatted i18n string
	 */
	public static String getFormattedWidgetName(INameEntity name){
		return getFormatted(name.getDir(), name.getName());
	}
	/**
	 * Returns the CSS formatted i18n string for the widget short name
	 * @param name the Widget's Name entity
	 * @return a CSS-formatted i18n string
	 */
	public static String getFormattedWidgetShortName(INameEntity name){
		return getFormatted(name.getDir(), name.getShort());
	}
	/**
	 * Returns the CSS formatted i18n string for the widget version
	 * @param widget the Widget
	 * @return a CSS-formatted i18n string
	 */
	public static String getFormattedWidgetVersion(W3CWidget widget){
		return getFormatted(widget.getDir(), widget.getVersion());
	}
	/**
	 * Returns the CSS formatted i18n string for the widget description
	 * @param description the Widget's description entity
	 * @return a CSS-formatted i18n string
	 */
	public static String getFormattedWidgetDescription(IDescriptionEntity description){
		return getFormatted(description.getDir(), description.getDescription());
	}
	/**
	 * Returns the CSS formatted i18n string for the widget author's name
	 * @param author the Widget's author entity
	 * @return a CSS-formatted i18n string
	 */
	public static String getFormattedWidgetAuthor(IAuthorEntity author){
		return getFormatted(author.getDir(), author.getAuthorName());
	}
	/**
	 * Returns the CSS formatted i18n string for the widget license
	 * @param license the Widget's License entity
	 * @return a CSS-formatted i18n string
	 */
	public static String getFormattedWidgetLicense(ILicenseEntity license){
		return getFormatted(license.getDir(), license.getLicenseText());
	}

	/**
	 * Generates a CSS i18n string from a raw string. Only use this
	 * method where the only possible directional information is inline
	 * and you want to ensure that embedded "dir" attributes are correctly
	 * formatted
	 * 
	 * @param value
	 * @return a CSS i18n string
	 */
	protected static String getFormatted(String value){
		// Reformat embedded SPAN tags
		value = reformatSpan(value);
		return value;
	}
	
	/**
	 * Generates a CSS i18n string using a given direction and value
	 * @param dir the text direction
	 * @param value the value to modify
	 * @return a CSS i18n string
	 */
	protected static String getFormatted(String dir, String value){
		
		// If the string has no embedded spans with dir attributes, and no set dir, just return the string
		if ((dir == null || dir.equals("ltr") )&& !value.contains("dir=")) return value;
		
		String mode = "embed";
		if (dir == null) dir = "ltr";
		// Reformat embedded SPAN tags
		value = reformatSpan(value);
		// Apply DIR to the string
		if (dir.equals("lro")) {dir = "ltr"; mode="bidi-override";};
		if (dir.equals("rlo")) {dir = "rtl"; mode="bidi-override";};
		return "<span style=\"unicode-bidi:"+mode+"; direction:"+dir+"\">"+value+"</span>";
	}
	
	public static String getEncoded(String dir, String value){		
		// Encode any embedded SPAN tags into unicode control characters
		String checkSpans = encodeSpan(value);
		// If no changes, and no dir property, return original string unmodified
		if (checkSpans.equals(value) && (dir == null  || dir.equals("ltr"))) return value;
		value = checkSpans;
		// Prepend direction control character
		if (dir == null) dir = "ltr";
		if (dir.equals("ltr")) dir = LTR;
		if (dir.equals("lro")) dir = LRO;
		if (dir.equals("rlo")) dir = RLO;
		if (dir.equals("rtl")) dir = RTL;
		// Append marker
		return dir+value+END;
	}
	
	/**
	 * Replace any embedded <span> tags with
	 * control characters
	 * @param value
	 * @return
	 */
	private static String encodeSpan(String value){
		value = value.replace("<span dir=\"ltr\">", LTR);
		value = value.replace("<span dir=\"rtl\">", RTL);
		value = value.replace("<span dir=\"lro\">", LRO);
		value = value.replace("<span dir=\"rlo\">", RLO);
		value = value.replace("</span>", END);
		return value;
	}
	
	/**
	 * Reformats any embedded <span dir="xyz"> tags to use
	 * CSS BIDI properties
	 * @param value
	 * @return a String with corrected BIDI properties
	 */
	private static String reformatSpan(String value){

		String mode="embed";
		if (value.contains("lro")){
			value = value.replace("lro", "ltr");	
			mode = "bidi-override";
		}
		if (value.contains("rlo")){
			value = value.replace("rlo", "rtl");	
			mode = "bidi-override";
		}
		
		value = value.replace("span dir=\"", "span style=\"unicode-bidi:"+mode+"; direction:");

		return value;
	}

}
