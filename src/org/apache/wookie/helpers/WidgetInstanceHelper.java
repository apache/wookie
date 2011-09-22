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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.w3c.IW3CXMLConfiguration;

/**
 * A helper to create representations of Widget Instance resources
 */
public class WidgetInstanceHelper {
	
	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/**
	 * Generate a Widget Instance representation doc in XML for a single instance of the given widget
	 * @param instance the widget instance
	 * @param url the URL of the widget instance
	 * @param locale the locale of the widget instance
	 * @return an XML representation of the Widget Instance as a String
	 */
	public static String createXMLWidgetInstanceDocument(IWidgetInstance instance, String url, String locale){
		String xml = XMLDECLARATION;
		IWidget widget = instance.getWidget();
		
		// Return a default width and height where the original value is either not provided
		// or of an invalid range (<0)
		String width = String.valueOf(IW3CXMLConfiguration.DEFAULT_WIDTH_LARGE);
		String height = String.valueOf(IW3CXMLConfiguration.DEFAULT_HEIGHT_LARGE);
		if (widget.getWidth()!=null && widget.getWidth()>0) width = widget.getWidth().toString();
		if (widget.getHeight()!=null && widget.getHeight()>0) height = widget.getHeight().toString();
				
		xml += "<widgetdata>"; //$NON-NLS-1$
		xml += "\t<url>"+url+"</url>"; //$NON-NLS-1$ //$NON-NLS-2$
		xml += "\t<identifier>"+instance.getIdKey()+"</identifier>\n"; //$NON-NLS-1$ //$NON-NLS-2$
		xml += "\t<title>"+StringEscapeUtils.escapeXml(widget.getWidgetTitle(locale))+"</title>\n"; //$NON-NLS-1$ //$NON-NLS-2$
		xml += "\t<height>"+height+"</height>\n"; //$NON-NLS-1$ //$NON-NLS-2$
		xml += "\t<width>"+width+"</width>\n"; //$NON-NLS-1$ //$NON-NLS-2$
		xml += "</widgetdata>"; //$NON-NLS-1$
		
		return xml;
	}
	
}
