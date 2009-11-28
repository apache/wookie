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

import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetService;

public class WidgetServiceHelper {
	
	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	/**
	 * Generate a Widget Service representation doc in XML for a single service
	 * @param service
	 * @param localIconPath
	 * @return
	 */
	public static String createXMLWidgetServiceDocument(WidgetService service, String localIconPath){
		return XMLDECLARATION + WidgetServiceHelper.toXml(service, localIconPath);
	}
	
	/**
	 * Generate a Widget Service representation doc in XML for an array of services
	 * @param services
	 * @param localIconPath
	 * @return
	 */
	public static String createXMLWidgetServicesDocument(WidgetService[] services, String localIconPath, boolean defaults){
		String out = XMLDECLARATION;
		out+="<services>\n";
		for (WidgetService service: services){
			out += WidgetServiceHelper.toXml(service,localIconPath,defaults);
		}
		out+="</services>\n";
		return out;
	}
	
	/**
	 * Represent in XML a single WidgetService and all Widgets in that category
	 * @param service
	 * @param localIconPath
	 * @return
	 */
	private static String toXml(WidgetService service, String localIconPath){
		return toXml(service, localIconPath,false);
	}
	
	/**
	 * Represent in XML a single WidgetService and all Widgets in that category
	 * @param service
	 * @param localIconPath
	 * @return
	 */
	private static String toXml(WidgetService service, String localIconPath, boolean defaults){
		String out = "\n<service name=\""+service.getServiceName()+"\">\n";
		Widget[] widgets;
		if (defaults){
			 widgets = new Widget[]{Widget.findDefaultByType(service.getServiceName())};
		} else {
			widgets = Widget.findByType(service.getServiceName());
		}
		for (Widget widget:widgets) out += WidgetHelper.toXml(widget, localIconPath);
		out +="</service>\n";
		return out;
	}

}
