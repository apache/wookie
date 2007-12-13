package org.tencompetence.widgetservice.manager;

import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.SharedData;
import org.tencompetence.widgetservice.beans.WidgetInstance;

public interface IWidgetAPIManager {

	/**
	 * Check that a request is valid by getting the hashed key and seeing if it exists in the DB
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public WidgetInstance checkUserKey(String key);

	/**
	 * Returns all sharedData records related to this instance
	 * 
	 * @param instance - a widget instance
	 * @return - shared data for a this instance
	 * @throws Exception 
	 */
	public SharedData[] getSharedDataForInstance(WidgetInstance instance);

	/**
	 * Add or update...
	 * @param widgetInstance
	 * @param name
	 * @param value
	 * @throws Exception 
	 */
	public void updateSharedDataEntry(WidgetInstance widgetInstance,
			String name, String value, boolean append);

	/**
	 * Add a new shared data entry to the DB
	 * @param instance
	 * @param name
	 * @param value
	 * @throws Exception
	 */
	public void addNewSharedDataEntry(WidgetInstance instance, String name,
			String value) throws Exception;

	/**
	 * Returns all preference records found which match a given WidgetInstance
	 * @param id
	 * @return - an array of preferences
	 * @throws Exception 
	 */
	public Preference[] getPreferenceForInstance(WidgetInstance id)
			throws Exception;

	/**
	 * Add or update...
	 * @param widgetInstance
	 * @param name
	 * @param value
	 * @throws Exception 
	 */
	public void updatePreference(WidgetInstance widgetInstance, String name,
			String value) throws Exception;

	/**
	 * Add a new preference for a widget instance
	 * @param widgetInstance
	 * @param name
	 * @param value
	 * @throws Exception
	 */
	public void addNewPreference(WidgetInstance widgetInstance, String name,
			String value) throws Exception;

}