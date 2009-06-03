package org.tencompetence.widgetservice.manager;

import org.apache.commons.configuration.Configuration;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.exceptions.WidgetTypeNotSupportedException;

/**
 * Interface for service functions
 * 
 * @author Paul Sharples
 * @version $Id: IWidgetServiceManager.java,v 1.11 2009-06-03 22:06:51 scottwilson Exp $
 *
 */
public interface IWidgetServiceManager extends IWidgetAPIManager {

	/**
	 * Get the default widget for the type specified by by the parameter
	 * @param typeToSearch
	 * @return a Widget or NULL meaning it has not been set
	 */
	Widget getDefaultWidgetByType(String typeToSearch)
			throws WidgetTypeNotSupportedException;
	
	/**
	 * Get the specified widget
	 * @param id
	 * @return a Widget or NULL meaning the id is invalid
	 * @throws WidgetTypeNotSupportedException
	 */
	Widget getWidgetById(String id)
			throws WidgetTypeNotSupportedException;

	/**
	 * Check if a widgetinstance exists in the DB
	 * @param userId - userId to check
	 * @param runId - runId to check
	 * @param envId - environmentId to check
	 * @param serviceId - serviceId to check
	 * @param serviceContext - the widget context
	 * @return
	 */
	boolean widgetInstanceExists(String api_key, String userId, String sharedDataKey, String serviceContext);

	/**
	 * Get a widgetinstance from the DB
	 * @param userId - userId to check
	 * @param runId - runId to check
	 * @param envId - environmentId to check
	 * @param serviceId - serviceId to check
	 * @param serviceContext - the widget context
	 * @return
	 */
	WidgetInstance getWidgetInstance(String api_key, String userId, String sharedDataKey, String serviceContext);

	/**
	 * Get a widgetinstance from the DB
	 * @param userId - userId to check
	 * @param runId - runId to check
	 * @param envId - environmentId to check
	 * @param serviceId - serviceId to check
	 * @param widgetId - the widget identifier
	 * @return
	 */
	WidgetInstance getWidgetInstanceById(String api_key, String userId, String sharedDataKey, String widgetId);
	
	/**
	 * Get a widget instance from the DB; returns null if none exists
	 * @param id_key
	 * @return - the widget instance, or null if no matching instance exists
	 */
	WidgetInstance getWidgetInstance(String id_key);
	
	/**
	 * Add a new widget instance
	 * @param userId - user id for the new instance
	 * @param sharedDataKey - shared data key for the new instance
	 * @param widget - widget instantiated
	 * @param nonce - nonce for id generation
	 * @param idKey - id key of instance
	 * @param properties - servlet context properties to generate info
	 * @return the widget instance created
	 */
	WidgetInstance addNewWidgetInstance(String api_key, String userId, String sharedDataKey, Widget widget, String nonce,
			String idKey, Configuration properties);
	
	/**
	 * 
	 * @param api_key
	 * @param widget_instance_id
	 * @param participant_id
	 * @param participant_display_name
	 * @param participant_thumbnail_url
	 * @return
	 */
	boolean addParticipantToWidgetInstance(WidgetInstance instance, String participant_id, String participant_display_name, String participant_thumbnail_url);
	
	/**
	 * 
	 * @param api_key
	 * @param widget_instance_id
	 * @param participant_id
	 * @param participant_display_name
	 * @param participant_thumbnail_url
	 * @return
	 */
	boolean removeParticipantFromWidgetInstance(WidgetInstance instance, String participant_id);

}