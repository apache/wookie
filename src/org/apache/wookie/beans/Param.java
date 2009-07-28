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
/**
 * A Param found in the config.xml 
 * @author Paul Sharples
 * @version $Id: Param.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $ 
 */
public class Param extends AbstractKeyBean<Param> {

	private static final long serialVersionUID = 1L;

	private Feature parentFeature;
	private String parameterName;
	private String parameterValue;	
	
	
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	public Feature getParentFeature() {
		return parentFeature;
	}
	public void setParentFeature(Feature parentFeature) {
		this.parentFeature = parentFeature;
	}
	
	public static Param[] findByValue(String key, Object value) {
		return (Param[]) findByValue(Param.class, key, value);
	}
}
