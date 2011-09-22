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

import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetService;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;

public class WidgetServiceHelper {
	
	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	/**
	 * Generate a Widget Service representation doc in XML for a single service
	 * @param service
	 * @param localIconPath
	 * @return an XML representation of the service as a String
	 */
	public static String createXMLWidgetServiceDocument(IWidgetService service, String localIconPath, String[] locales){
		return XMLDECLARATION + WidgetServiceHelper.toXml(service, localIconPath, locales);
	}
	
	/**
	 * Generate a Widget Service representation doc in XML for an array of services
	 * @param services
	 * @param localIconPath
	 * @return an XML representation of the set of services as a String
	 */
	public static String createXMLWidgetServicesDocument(IWidgetService[] services, String localIconPath, boolean defaults, String[] locales){
		String out = XMLDECLARATION;
		out+="<services>\n";
		for (IWidgetService service: services){
			out += WidgetServiceHelper.toXml(service,localIconPath,defaults, locales);
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
	private static String toXml(IWidgetService service, String localIconPath, String[] locales){
		return toXml(service, localIconPath,false, locales);
	}
	
	/**
	 * Represent in XML a single WidgetService and all Widgets in that category
	 * @param service
	 * @param localIconPath
	 * @return
	 */
	private static String toXml(IWidgetService service, String localIconPath, boolean defaults, String[] locales){
		String out = "\n<service name=\""+service.getServiceName()+"\">\n";
		IWidget[] widgets;
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		if (defaults){
			 widgets = new IWidget[]{persistenceManager.findWidgetDefaultByType(service.getServiceName())};
		} else {
			widgets = persistenceManager.findWidgetsByType(service.getServiceName());
		}
		for (IWidget widget:widgets) out += WidgetHelper.toXml(widget, localIconPath, locales);
		out +="</service>\n";
		return out;
	}

}
