package org.tencompetence.widgetservice.manager;

import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.exceptions.WidgetTypeNotSupportedException;

public interface IWidgetServiceManager extends IWidgetAPIManager {

	/**
	 * Get the default widget for the type specified by by the parameter
	 * @param typeToSearch
	 * @return a Widget or NULL meaning it has not been set
	 */
	Widget getDefaultWidgetByType(String typeToSearch)
			throws WidgetTypeNotSupportedException;

	WidgetInstance[] getwidgetInstancesForWidget(Widget widget);

	void deletePreferenceInstancesForWidgetInstance(WidgetInstance instance);

	void deleteSharedDataInstancesForWidgetInstance(WidgetInstance instance);

	/**
	 * Check if a widgetinstance exists in the DB
	 * @param userId - userId to check
	 * @param runId - runId to check
	 * @param envId - environmentId to check
	 * @param serviceId - serviceId to check
	 * @param serviceContext - the widget context
	 * @return
	 */
	boolean widgetInstanceExists(String userId, String runId, String envId,
			String serviceId, String serviceContext);

	/**
	 * Get a widgetinstance from the DB
	 * @param userId - userId to check
	 * @param runId - runId to check
	 * @param envId - environmentId to check
	 * @param serviceId - serviceId to check
	 * @param serviceContext - the widget context
	 * @return
	 */
	WidgetInstance getwidgetInstance(String userId, String runId, String envId,
			String serviceId, String serviceContext);

	/**
	 * Method to add a new instance to the widget instances table
	 * @param type
	 * @throws WidgetTypeNotSupportedException 
	 */
	WidgetInstance addNewWidgetInstance(String userId, String runId,
			String envId, String serId, Widget widget, String nonce,
			String idKey);

}