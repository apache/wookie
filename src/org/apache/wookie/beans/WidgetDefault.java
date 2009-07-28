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

import java.io.Serializable;
import java.util.Map;

/**
 * A default widget entity
 * 
 * @author Paul Sharples
 * @version $Id: WidgetDefault.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 */
public class WidgetDefault extends ActiveRecord<WidgetDefault> implements Serializable {

	private static final long serialVersionUID = -8585379777119902714L;
	
	private String widgetContext;
	private Integer widgetId;

	public WidgetDefault() {
	}

	public Integer getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(Integer widgetId) {
		this.widgetId = widgetId;
	}

	public String getWidgetContext() {
		return widgetContext;
	}

	public void setWidgetContext(String widgetContext) {
		this.widgetContext = widgetContext;
	}
	/// Active record methods
	public static WidgetDefault findById(String id){
		return (WidgetDefault) findById(WidgetDefault.class, id);
	}
	public static WidgetDefault[] findAll(){
		return (WidgetDefault[]) findAll(WidgetDefault.class);		
	}
	public static WidgetDefault[] findByValue(String key, Object value){
		return (WidgetDefault[]) findByValue(WidgetDefault.class, key, value);		
	}
	@SuppressWarnings("unchecked")
	public static WidgetDefault[] findByValues(Map map){
		return (WidgetDefault[]) findByValues(WidgetDefault.class,map);			
	}

}
