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
 * A localized description of a widget
 *
 */
public class Description extends AbstractLocalizedKeyBean<Description> {
	
	private static final long serialVersionUID = -1205834267479594166L;
	
	private String content;
	private Widget widget;

	public String getContent() {
		return content;
	}
	public void setContent(String text) {
		this.content = text;
	}
	public Widget getWidget() {
		return widget;
	}
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	
	/// Active record methods
	public static Description findById(Object id){
		return (Description) findById(Description.class, id);
	}

	public static Description[] findByValue(String key, Object value) {
		return (Description[]) findByValue(Description.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static Description[] findByValues(Map map) {
		return (Description[]) findByValues(Description.class, map);
	}
	
	public static Description[] findAll(){
		return (Description[]) findAll(Description.class);
	}


}
