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

	boolean addNewService(String serviceName);

	/**
	 * Add a new widget to the system - without the widget type
	 * @param widgetName - the name of this widget
	 * @param url - the url which it resides
	 * @param height - the height at which it is supposed to be displayed
	 * @param width - the width at which it is supposed to be displayed
	 */
	void addNewWidget(IManifestModel model);

	/**
	 * Add a new widget to the system
	 * @param widgetName - the name of this widget
	 * @param url - the url which it resides
	 * @param height - the height at which it is supposed to be displayed
	 * @param width - the width at which it is supposed to be displayed
	 * @param widgetTypes - a string array containing the types this widget can perform as
	 * @return - the new key created for this widget
	 */
	int addNewWidget(IManifestModel model, String[] widgetTypes);

	/**
	 * Add a new whitelist entry
	 * @param uri
	 * @return
	 */
	boolean addWhiteListEntry(String uri);

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
	 * delete any default types that belong to a given service name
	 */
	void deleteWidgetDefaultByServiceName(String serviceName);

	/**
	 * Check to see if a service type is currently listed for a particular widget
	 * @param dbkey
	 * @param serviceType
	 * @return
	 */
	boolean doesServiceExistForWidget(int dbkey,
			String serviceType);

	/**
	 * using the GIUD found in the config.xml file, ascertain if this widget already exists
	 * @param guid
	 * @return
	 */
	boolean doesWidgetAlreadyExistInSystem(String guid);

	/**
	 * Find if this widget is maximizable, from given key
	 * @param dbKey
	 * @return
	 */
	boolean isWidgetMaximized(int dbKey);

	/**
	 * Prints the details of all widgets
	 * @param magr - a widgetAdminManager instance
	 */
	void printOutAllWidgets(IWidgetAdminManager magr);

	/**
	 * Remove from the system the service & any other places it may reside in the DB
	 *  - i.e in widgetDefault 
	 * @param serviceId
	 * @return
	 */
	boolean removeServiceAndReferences(int serviceId);

	/**
	 * 
	 * @param widgetId
	 * @param widgetType
	 */
	boolean removeSingleWidgetType(int widgetId, String widgetType);

	/**
	 * Remove a whitelist entry from the DB
	 * @param entryId
	 * @return
	 */
	boolean removeWhiteListEntry(int entryId);

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

	/**
	 * Get this widgets GUID
	 * @param parseInt
	 * @return
	 */
	String getWidgetGuid(int parseInt);

}