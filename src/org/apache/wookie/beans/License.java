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

public class License extends AbstractLocalizedKeyBean<License> {

	private static final long serialVersionUID = 5896306127658105242L;
	
	private String text;
	private String href;
	private Widget widget;
	
	public License(){}
	
	public License(String text, String href, String lang, String dir, Widget widget){
		setText(text);
		setHref(href);
		setLang(lang);
		setDir(dir);
		setWidget(widget);
	}

	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public Widget getWidget() {
		return widget;
	}
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	
	/// Active record methods
	public static License findById(Object id){
		return (License) findById(License.class, id);
	}

	public static License[] findByValue(String key, Object value) {
		return (License[]) findByValue(License.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static License[] findByValues(Map map) {
		return (License[]) findByValues(License.class, map);
	}
	
	public static License[] findAll(){
		return (License[]) findAll(License.class);
	}

}
