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

package org.apache.wookie.feature;

/**
 * Interface supported by features
 */
public interface IFeature {
	
	/**
	 * The name (IRI) of the feature
	 * @return
	 */
	public String getName();
	/**
	 * An array of Strings representing the path to each script required by the feature
	 * @return
	 */
	public String[] scripts();
	/**
	 * An array of Strings representing the path to each stylesheet required by the feature
	 * @return
	 */
	public String[] stylesheets();
	
	/**
	 * @return true if this feature should be flattened (injected) on export, or remain in the config.xml as a <feature> element
	 */
	public boolean flattenOnExport();
	
	/**
	 * @return the path to the folder containing the feature and its resources
	 */
	public String getFolder();

}
