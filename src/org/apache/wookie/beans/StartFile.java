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

/**
 * Represents a localized start page for a widget
 * 
 * @author Scott Wilson
 *
 */
public class StartFile extends AbstractKeyBean<StartFile> implements ILocalizedElement {

	private static final long serialVersionUID = -1936521857749076049L;
	
	private String url;
	private String charset;
	private Widget widget;
	private String lang;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public Widget getWidget() {
		return widget;
	}
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	/// Active record methods
	public static StartFile findById(Object id){
		return (StartFile) findById(License.class, id);
	}

	public static StartFile[] findByValue(String key, Object value) {
		return (StartFile[]) findByValue(StartFile.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static StartFile[] findByValues(Map map) {
		return (StartFile[]) findByValues(StartFile.class, map);
	}
	
	public static StartFile[] findAll(){
		return (StartFile[]) findAll(StartFile.class);
	}

}
