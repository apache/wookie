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

import org.apache.wookie.manifestmodel.IManifestModel;

/**
 * Interface for admin functions
 * 
 * @author Paul Sharples
 * @version $Id: IWidgetAdminManager.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $
 *
 */
public interface IWidgetAdminManager {

	/**
	 * Adds a new widget
	 * @param model the model of the widget to add
	 * @param widgetTypes the types to allocate the widget to
	 * @return true if successfully added, otherwise false
	 */
	int addNewWidget(IManifestModel model, String[] widgetTypes);
	
	/**
	 * Adds a new widget
	 * @param model the model of the widget to add
	 * @return true if successfully added, otherwise false
	 */
	int addNewWidget(IManifestModel model);
	
	/**
	 * Adds a new widget
	 * @param model the model of the widget to add
	 * @param grantAccessRequests whether to automatically grant access requests for the widget
	 * @return true if successfully added, otherwise false
	 */
	int addNewWidget(IManifestModel model, boolean grantAccessRequests);

	/**
	 * delete any default types that belong to a given widgetid
	 */
	void deleteWidgetDefaultById(int widgetKey);

	/**
	 * from a given widgetKey & service type, remove the default widget entry
	 * @param widgetKey
	 * @param serviceType
	 */
	void deleteWidgetDefaultByIdAndServiceType(int widgetKey,
			String serviceType);

	/**
	 * Check to see if a service type is currently listed for a particular widget
	 * @param dbkey
	 * @param serviceType
	 * @return
	 */
	boolean doesServiceExistForWidget(int dbkey,
			String serviceType);

	/**
	 * Find if this widget is maximizable, from given key
	 * @param dbKey
	 * @return
	 */
	boolean isWidgetMaximized(int dbKey);

	/**
	 * 
	 * @param widgetId
	 * @param widgetType
	 */
	boolean removeSingleWidgetType(int widgetId, String widgetType);

	/**
	 * Remove a widget and any references in the DB - i.e. WidgetInstances 
	 * @param widgetId
	 * @return
	 */
	boolean removeWidgetAndReferences(int widgetId);

	/**
	 * Sets a given widget to be the default for a given context - i.e. chat or discussion etc...
	 * @param key - the key of the widget to set as defult
	 * @param widgetType - the type of widget
	 */
	void setDefaultWidget(int key, String widgetType);

	/**
	 * FRom a given widget db key, allow to add multiple types
	 * @param dbKey
	 * @param widgetTypes
	 * @param maximize
	 */
	void setWidgetTypesForWidget(int dbKey,
			String[] widgetTypes, boolean maximize);


}