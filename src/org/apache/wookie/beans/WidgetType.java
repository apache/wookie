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
 * WidgetType - what type of service a particular widget can be
 * 
 * @author Paul Sharples
 * @version $Id: WidgetType.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 */
public class WidgetType extends AbstractKeyBean<WidgetType> {

	private static final long serialVersionUID = 1L;
	
	private Widget widget;
	private String widgetContext;

	public String getWidgetContext() {
		return widgetContext;
	}

	public void setWidgetContext(String widgetContext) {
		this.widgetContext = widgetContext;
	}

	public Widget getWidget() {
		return widget;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}


	/// Active record methods
	public static WidgetType findById(String id){
		return (WidgetType) findById(WidgetType.class, id);
	}
	
	public static WidgetType[] findByValue(String key, Object value){
		return (WidgetType[]) findByValue(WidgetType.class, key, value);
	}
	
	@SuppressWarnings("unchecked")
	public static WidgetType[] findByValues(Map map){
		return (WidgetType[]) findByValues(WidgetType.class, map);		
	}
	
	
}
