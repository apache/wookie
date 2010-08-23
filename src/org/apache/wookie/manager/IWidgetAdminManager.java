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

package org.apache.wookie.manager;

/**
 * Interface for admin functions
 * 
 * @author Paul Sharples
 * @version $Id: IWidgetAdminManager.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $
 *
 */
public interface IWidgetAdminManager {

	/**
	 * 
	 * @param widgetId
	 * @param widgetType
	 */
	boolean removeSingleWidgetType(String widgetId, String widgetType);

	/**
	 * Sets a given widget to be the default for a given context - i.e. chat or discussion etc...
	 * @param key - the key of the widget to set as defult
	 * @param widgetType - the type of widget
	 */
	void setDefaultWidget(String key, String widgetType);

	/**
	 * FRom a given widget db key, allow to add multiple types
	 * @param dbKey
	 * @param widgetTypes
	 * @param maximize
	 */
	void setWidgetTypesForWidget(String dbKey,
			String[] widgetTypes, boolean maximize);


}