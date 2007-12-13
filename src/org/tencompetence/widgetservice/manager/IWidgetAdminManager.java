package org.tencompetence.widgetservice.manager;

import org.tencompetence.widgetservice.beans.Whitelist;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetDefault;
import org.tencompetence.widgetservice.beans.WidgetService;
import org.tencompetence.widgetservice.exceptions.WidgetTypeNotSupportedException;

public interface IWidgetAdminManager {

	public abstract boolean addNewService(String serviceName);

	/**
	 * Add a new widget to the system - without the widget type
	 * @param widgetName - the name of this widget
	 * @param url - the url which it resides
	 * @param height - the height at which it is supposed to be displayed
	 * @param width - the width at which it is supposed to be displayed
	 */
	public abstract void addNewWidget(String widgetName, String url,
			String guid, int height, int width);

	/**
	 * Add a new widget to the system
	 * @param widgetName - the name of this widget
	 * @param url - the url which it resides
	 * @param height - the height at which it is supposed to be displayed
	 * @param width - the width at which it is supposed to be displayed
	 * @param widgetTypes - a string array containing the types this widget can perform as
	 * @return - the new key created for this widget
	 */
	@SuppressWarnings("unchecked")
	public abstract int addNewWidget(String widgetName, String url,
			String guid, int height, int width, String[] widgetTypes);

	public abstract boolean addWhiteListEntry(String uri);

	/**
	 * delete any default types that belong to a given widgetid
	 */
	public abstract void deleteWidgetDefaultById(int widgetKey);

	public abstract void deleteWidgetDefaultByIdAndServiceType(int widgetKey,
			String serviceType);

	/**
	 * delete any default types that belong to a given service name
	 */
	public abstract void deleteWidgetDefaultByServiceName(String serviceName);

	/**
	 * Check to see if a service type is currently listed for a particular widget
	 * @param dbkey
	 * @param serviceType
	 * @return
	 */
	public abstract boolean doesServiceExistForWidget(int dbkey,
			String serviceType);

	public abstract boolean doesWidgetAlreadyExistInSystem(String guid);

	/**
	 * Get all of the default widgets from the DefaultWidgets table
	 * @return - an array of default widgets
	 */
	public abstract WidgetDefault[] getAllDefaultWidgets();

	/**
	 * 
	 * @return
	 */
	public abstract WidgetService[] getAllServices();

	/**
	 * Get all widgets from widget table
	 * 
	 * @return - an array of widgets
	 */
	public abstract Widget[] getAllWidgets();

	public abstract Whitelist[] getWhiteList();

	public abstract Widget getWidget(int dbKey);

	/**
	 * Get all widgets of a particular type - e.g. - chat
	 * @param typeToSearch - a widget type
	 * @return - returns an array of widgets
	 */
	public abstract Widget[] getWidgetsByType(String typeToSearch)
			throws WidgetTypeNotSupportedException;

	public abstract WidgetService getWidgetService(int dbKey);

	public abstract boolean isWidgetMaximized(int dbKey);

	/**
	 * Prints the details of all widgets
	 * @param magr - a widgetAdminManager instance
	 */
	@SuppressWarnings("unchecked")
	public abstract void printOutAllWidgets(IWidgetAdminManager magr);

	public abstract boolean removeServiceAndReferences(int serviceId);

	public abstract void removeSingleWidgetType(int widgetId, String widgetType);

	public abstract boolean removeWhiteListEntry(int entryId);

	public abstract boolean removeWidgetAndReferences(int widgetId);

	/**
	 * Sets a given widget to be the default for a given context - i.e. chat or discussion etc...
	 * @param key - the key of the widget to set as defult
	 * @param widgetType - the type of widget
	 */
	public abstract void setDefaultWidget(int key, String widgetType);

	@SuppressWarnings("unchecked")
	public abstract void setWidgetTypesForWidget(int dbKey,
			String[] widgetTypes, boolean maximize);

}