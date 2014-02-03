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
import org.apache.log4j.Logger;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A helper to create representations of Widget Instance resources
 */
public class WidgetInstanceHelper {
	
  static Logger logger = Logger.getLogger(WidgetInstanceHelper.class.getName());
  
	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/**
	 * Generate a Widget Instance representation doc in XML for a single instance of the given widget
	 * @param instance the widget instance
	 * @param url the URL of the widget instance
	 * @param locale the locale of the widget instance
	 * @return an XML representation of the Widget Instance as a String
	 */
	public static String createXMLWidgetInstanceDocument(IWidgetInstance instance, String url, String locale, boolean useDefaultSizes){
		String xml = XMLDECLARATION;
		IWidget widget = instance.getWidget();
		

		
		String width = null;
		String height = null;
		
		// Return a default width and height where the original value is either not provided
		// or of an invalid range (<0)
		if (useDefaultSizes){
			width = String.valueOf(IW3CXMLConfiguration.DEFAULT_WIDTH_LARGE);
			height = String.valueOf(IW3CXMLConfiguration.DEFAULT_HEIGHT_LARGE);
		}
		if (widget.getWidth()!=null && widget.getWidth()>0) width = widget.getWidth().toString();
		if (widget.getHeight()!=null && widget.getHeight()>0) height = widget.getHeight().toString();
				
		xml += "<widgetdata>"; //$NON-NLS-1$
		xml += "\t<url>"+url+"</url>"; //$NON-NLS-1$ //$NON-NLS-2$
		xml += "\t<identifier>"+instance.getIdKey()+"</identifier>\n"; //$NON-NLS-1$ //$NON-NLS-2$
		xml += "\t<title>"+StringEscapeUtils.escapeXml(widget.getLocalName(locale))+"</title>\n"; //$NON-NLS-1$ //$NON-NLS-2$
		if (height != null) xml += "\t<height>"+height+"</height>\n"; //$NON-NLS-1$ //$NON-NLS-2$
		if (width != null) xml += "\t<width>"+width+"</width>\n"; //$NON-NLS-1$ //$NON-NLS-2$
		xml += "</widgetdata>"; //$NON-NLS-1$
		
		return xml;
	}
	
  public static String toJson(IWidgetInstance instance, String url, String locale, boolean useDefaultSizes) {
    IWidget widget = instance.getWidget();
    
    String width = null;
    String height = null;
    
    if (useDefaultSizes){
    	width = String.valueOf(IW3CXMLConfiguration.DEFAULT_WIDTH_LARGE);
    	height = String.valueOf(IW3CXMLConfiguration.DEFAULT_HEIGHT_LARGE);
    }
    if (widget.getWidth() != null && widget.getWidth() > 0)
      width = widget.getWidth().toString();
    if (widget.getHeight() != null && widget.getHeight() > 0)
      height = widget.getHeight().toString();
    JSONObject json = new JSONObject();
    try {
      json.put("url", url);
      json.put("identifier", instance.getIdKey());
      json.put("title", widget.getLocalName(locale));
      if (height != null) json.put("height", height);
      if (width != null) json.put("width", width);
    } catch (JSONException e) {
      logger.error("Problem rendering instance using JSON",e);
    }
    return json.toString();
  }
	
}
