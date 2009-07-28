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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * A Feature found in the config.xml 
 * @author Paul Sharples
 * @version $Id: Feature.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $ 
 */
public class Feature extends AbstractKeyBean<Feature>{

	private static final long serialVersionUID = 1L;
	
	private String featureName;	
	private boolean required;
	// parent widget
	private Widget widget;
	// set of parameters
	@SuppressWarnings("unchecked")
	private Set<Param> parameters = new HashSet();
	
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public Widget getWidget() {
		return widget;
	}
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	public Set<Param> getParameters() {
		return parameters;
	}
	public void setParameters(Set<Param> parameters) {
		this.parameters = parameters;
	}
	
	public static Feature[] findByValue(String key, Object value) {
		return (Feature[]) findByValue(Feature.class, key, value);
	}
	
	public static Feature findNamedFeatureForWidget(Widget widget, String name){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("widget", widget);
		map.put("featureName", name);
		Feature[] features = (Feature[]) findByValues(Feature.class, map);
		if (features == null||features.length!=1) return null;
		return features[0];
	}


}
