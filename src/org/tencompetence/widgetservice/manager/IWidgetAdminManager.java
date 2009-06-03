/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.widgetservice.manager;

import java.util.List;

import org.tencompetence.widgetservice.beans.PreferenceDefault;

/**
 * Interface for admin functions
 * 
 * @author Paul Sharples
 * @version $Id: IWidgetAdminManager.java,v 1.9 2009-06-03 10:06:17 scottwilson Exp $
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
	void addNewWidget(String widgetIconLocation, String url, java.util.Hashtable<String, String> widgetData, List<PreferenceDefault> prefs);

	/**
	 * Add a new widget to the system
	 * @param widgetName - the name of this widget
	 * @param url - the url which it resides
	 * @param height - the height at which it is supposed to be displayed
	 * @param width - the width at which it is supposed to be displayed
	 * @param widgetTypes - a string array containing the types this widget can perform as
	 * @return - the new key created for this widget
	 */
	int addNewWidget(String widgetIconLocation, String url, java.util.Hashtable<String,String> widgetData, List<PreferenceDefault> prefs, String[] widgetTypes);

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