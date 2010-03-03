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
 * A class to model ALREADY installed features, such as polling for example
 * NOTE: The config.xml file can also define other features, which may 
 * or may not be supported. These types are modelled in the "Feature" class.
 * @author Paul Sharples
 * @version $Id: ServerFeature.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 */
public class ServerFeature extends AbstractKeyBean<ServerFeature> {

	private static final long serialVersionUID = 1L;
	
	private String featureName;
	private String className;

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	
	public static String[] getFeatureNames(){
		ServerFeature[] features = ServerFeature.findAll();
		String[] featureNames = new String[features.length];
		for (int idx=0;idx<features.length;idx++){
			featureNames[idx] = features[idx].getFeatureName();
		}
		return featureNames;
	}
	
	public static ServerFeature findByName(String name){
		ServerFeature[] features = (ServerFeature[]) findByValue(ServerFeature.class, "featureName", name);
		if (features == null || features.length != 1) return null;
		return features[0];
	}
	
	public static ServerFeature[] findAll(){
		 return (ServerFeature[]) findAll(ServerFeature.class);
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

}
