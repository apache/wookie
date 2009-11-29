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

import java.util.HashMap;
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
import java.util.Map;

/**
 * An object representing an icon for a widget
 * @author Scott Wilson
 *
 */
public class WidgetIcon extends AbstractKeyBean<WidgetIcon> {
	
	private static final long serialVersionUID = 9080382124923145538L;
	
	private String src;
	private Integer height;
	private Integer width;
	private Widget widget;
	
	public Widget getWidget() {
		return widget;
	}
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public WidgetIcon(String src, Integer height, Integer width, Widget widget){
		setSrc(src);
		setHeight(height);
		setWidth(width);
		setWidget(widget);
	}
	public WidgetIcon(){};
	
	/// Active record methods
	public static WidgetIcon findById(Object id){
		return (WidgetIcon) findById(WidgetIcon.class, id);
	}

	public static WidgetIcon[] findByValue(String key, Object value) {
		return (WidgetIcon[]) findByValue(WidgetIcon.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static WidgetIcon[] findByValues(Map map) {
		return (WidgetIcon[]) findByValues(WidgetIcon.class, map);
	}
	
	public static WidgetIcon[] findAll(){
		return (WidgetIcon[]) findAll(WidgetIcon.class);
	}
	/// Special queries
	public static WidgetIcon[] findForWidget(Widget widget){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("widget", widget);
		return WidgetIcon.findByValues(map);
	}
	

}
