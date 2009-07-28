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

package org.apache.wookie.beans;

import java.util.Map;
 /**
 * A type of service - a service provided in the system
 * 
 * @author Paul Sharples
 * @version $Id: WidgetService.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 */
public class WidgetService extends AbstractKeyBean<WidgetService> {
	
	private static final long serialVersionUID = 1L;
	
	private String serviceName;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	/// Active record methods
	public static WidgetService findById(Object id){
		return (WidgetService) findById(WidgetService.class, id);
	}
	
	public static WidgetService[] findByValue(String key, Object value) {
		return (WidgetService[]) findByValue(WidgetService.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static WidgetService[] findByValues(Map map) {
		return (WidgetService[]) findByValues(WidgetService.class, map);
	}
	
	public static WidgetService[] findAll(){
		return (WidgetService[]) findAll(WidgetService.class);
	}
}
