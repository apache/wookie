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
package org.apache.wookie.util;

import org.apache.wookie.beans.IDescription;
import org.apache.wookie.beans.ILicense;
import org.apache.wookie.beans.IName;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.w3c.util.FormattingUtils;

/**
 * i18n formatting utilities
 * 
 * The methods in this class can be used to generate i18n strings using
 * CSS bidi properties for use in display. This involves inserting HTML 
 * <span> tags containing CSS styling properties for text direction
 * 
 */
public class WidgetFormattingUtils extends FormattingUtils{
	
	/**
	 * Returns the CSS formatted i18n string for the widget name
	 * @param name the Widget's Name 
	 * @return a CSS-formatted i18n string
	 */
	public static String getFormattedWidgetName(IName name){
		return getFormatted(name.getDir(), name.getName());
	}
	/**
	 * Returns the CSS formatted i18n string for the widget short name
	 * @param name the Widget's Name 
	 * @return a CSS-formatted i18n string
	 */
	public static String getFormattedWidgetShortName(IName name){
		return getFormatted(name.getDir(), name.getShortName());
	}
	/**
	 * Returns the CSS formatted i18n string for the widget version
	 * @param widget the Widget
	 * @return a CSS-formatted i18n string
	 */
	public static String getFormattedWidgetVersion(IWidget widget){
		return getFormatted(widget.getDir(), widget.getVersion());
	}
	/**
	 * Returns the CSS formatted i18n string for the widget description
	 * @param description the Widget's description 
	 * @return a CSS-formatted i18n string
	 */
	public static String getFormattedWidgetDescription(IDescription description){
		return getFormatted(description.getDir(), description.getDescription());
	}
	/**
	 * Returns the CSS formatted i18n string for the widget license
	 * @param license the Widget's License 
	 * @return a CSS-formatted i18n string
	 */
	public static String getFormattedWidgetLicense(ILicense license){
		return getFormatted(license.getDir(), license.getLicenseText());
	}


}
