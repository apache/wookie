package org.tencompetence.widgetservice.manager;

import org.tencompetence.widgetservice.beans.Participant;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.SharedData;
import org.tencompetence.widgetservice.beans.WidgetInstance;

/**
 * Interface for api functions
 * 
 * @author Paul Sharples
 * @version $Id: IWidgetAPIManager.java,v 1.5 2009-06-03 20:55:20 scottwilson Exp $
 *
 */
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
	 * Returns all preference records found which match a given WidgetInstance
	 * @param id
	 * @return - an array of preferences, or null if none exist
	 */
	public Preference[] getPreferenceForInstance(WidgetInstance id);

	/**
	 * Return the specified preference record for the given instance 
	 * @param instance
	 * @param key
	 * @return - the preference, or null if not found
	 */
	public Preference getPreferenceForInstance(WidgetInstance instance, String key);
	
	/**
	 * Add or update...
	 * @param widgetInstance
	 * @param name
	 * @param value
	 */
	public void updatePreference(WidgetInstance widgetInstance, String name,
			String value);

	public void lockWidgetInstance(WidgetInstance instance);

	public void unlockWidgetInstance(WidgetInstance instance);

	public String getSharedDataValue(WidgetInstance widgetInstance, String key);

	public boolean isInstanceLocked(WidgetInstance widgetInstance);
	
	public Participant getViewer(WidgetInstance widgetInstance);
	
	public Participant[] getParticipants(WidgetInstance widgetInstance);

}