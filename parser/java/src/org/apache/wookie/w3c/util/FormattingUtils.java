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
 * The methods in this class can be used to generate i18n strings using
 * CSS bidi properties for use in display. This involves inserting HTML 
 * <span> tags containing CSS styling properties for text direction
 * 
 */
public class FormattingUtils {
	
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
		return getFormatted(widget.getVersion());
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
		String mode = "embed";
		if (dir == null) dir = "ltr";
		// Reformat embedded SPAN tags
		value = reformatSpan(value);
		// Apply DIR to the string
		if (dir.equals("lro")) {dir = "ltr"; mode="bidi-override";};
		if (dir.equals("rlo")) {dir = "rtl"; mode="bidi-override";};
		return "<span style=\"unicode-bidi:"+mode+"; direction:"+dir+"\">"+value+"</span>";
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
