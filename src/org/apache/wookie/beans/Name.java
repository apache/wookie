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

import org.apache.wookie.w3c.ILocalizedElement;

public class Name extends AbstractLocalizedKeyBean<Name> implements ILocalizedElement {
	
	private static final long serialVersionUID = -1205834267479594166L;
	
	private String shortName;
	private String name;
	private Widget widget;

	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Widget getWidget() {
		return widget;
	}
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	
	/// Active record methods
	public static Name findById(Object id){
		return (Name) findById(Name.class, id);
	}

	public static Name[] findByValue(String key, Object value) {
		return (Name[]) findByValue(Name.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static Name[] findByValues(Map map) {
		return (Name[]) findByValues(Name.class, map);
	}
	
	public static Name[] findAll(){
		return (Name[]) findAll(Name.class);
	}


}
