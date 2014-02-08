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
package org.apache.wookie.services;

import java.util.ServiceLoader;

import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.services.impl.DefaultSharedContextService;

public interface SharedContextService {

	/**
	 * get the shared data belonging to the shared context
	 * @return an array of SharedData instances, or null if no shared data exists
	 */
	public abstract ISharedData[] getSharedData(String apiKey, String widgetId, String contextId);

	/**
	 * Find a specific shared data object for a given session and object key
	 * @param instance the widget instance
	 * @param key the key of the shared data object, i.e. the tuple key not the shared data key
	 * @return a SharedData object, or null if no matches are found
	 */
	public abstract ISharedData getSharedData(String apiKey, String widgetId, String contextId, String key);

	/**
	 * Remove a single Shared Data Object
	 * @param name the name (key) of the data object
	 * @return true if a shared data object was located and deleted, false if no match was found
	 */
	public abstract boolean removeSharedData(String apiKey, String widgetId, String contextId, String name);

	/**
	 * Update a single Shared Data object
	 * @param name the name (key) of the data object
	 * @param value the value to set, or null to clear the entry
	 * @param append set to true to append the value to the current value
	 * @return true if the value was updated, false if a new object was created
	 */
	public abstract boolean updateSharedData(String apiKey, String widgetId, String contextId, String name, String value,
			boolean append);

	/**
	 * get the participants belonging to this shared context
	 * @return an arry of Participant instances, or null if there are no participants
	 */
	public abstract IParticipant[] getParticipants(String apiKey, String widgetId, String contextId);

	/**
	 * Add a participant to a shared context
	 * @param participantId the id property of the participant to add
	 * @param participantDisplayName the display name property of the participant to add
	 * @param participantThumbnailUrl the thumbnail url property of the participant to add
	 * @return true if the participant was successfully added, otherwise false
	 */
	public abstract boolean addParticipant(String apiKey, String widgetId, String contextId, String participantId,
			String participantDisplayName, String participantThumbnailUrl);

	/**
	 * Add a participant to a shared context
	 * @param participantId the id property of the participant to add
	 * @param participantDisplayName the display name property of the participant to add
	 * @param participantThumbnailUrl the thumbnail url property of the participant to add
	 * @return true if the participant was successfully added, otherwise false
	 */
	public abstract boolean addParticipant(String apiKey, String widgetId, String contextId, String participantId,
			String participantDisplayName, String participantThumbnailUrl,
			String role);

	/**
	 * Remove a participant from the shared context
	 * @param participant
	 */
	public abstract void removeParticipant(String apiKey, String widgetId, String contextId, IParticipant participant);

	/**
	 * Removes a participant from a widget instance
	 * @param participantId the id property of the participant
	 * @return true if the participant is successfully removed, otherwise false
	 */
	public abstract boolean removeParticipant(String apiKey, String widgetId, String contextId, String participantId);

	/**
	 * Get a participant by ID from this shared context
	 * @param participantId
	 * @return the participant, or null if there is no participant with the given id in the context
	 */
	public abstract IParticipant getParticipant(String apiKey, String widgetId, String contextId, String participantId);

	/**
	 * Get the participant associated with the widget instance as the viewer
	 * @param widgetInstance
	 * @return the IParticipant representing the viewer, or null if no match is found
	 */
	public abstract IParticipant getViewer(String apiKey, String widgetId, String contextId, String viewerId);

	/**
	 * Get the participant designated as the host of the shared context. Note that
	 * if there are multiple hosts, only the first is returned.
	 * @return a participant designated the host, or null if no participant is host
	 */
	public abstract IParticipant getHost(String apiKey, String widgetId, String contextId);

	public abstract IParticipant[] getHosts(String apiKey, String widgetId, String contextId);
	
	public static class Factory {
		
		private static SharedContextService provider;
		
	    public static SharedContextService getInstance() {
	    	//
	    	// Use the service loader to load an implementation if one is available
	    	//
	    	if (provider == null){
	    		ServiceLoader<SharedContextService> ldr = ServiceLoader.load(SharedContextService.class);
	    		for (SharedContextService service : ldr) {
	    			// We are only expecting one
	    			provider = service;
	    		}
	    	}
	    	//
	    	// If no service provider is found, use the default
	    	//
	    	if (provider == null){
	    		provider = new DefaultSharedContextService();
	    	}
	    	
	    	return provider;
	    }
	}

}