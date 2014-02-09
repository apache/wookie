/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.apache.wookie.beans;

import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.services.SharedContextService;

/**
 * Represents the shared context for a set of widget instances
 * and the data objects that share that context (participants and data).
 * 
 * Instances of this class are (currently) a "virtual" object rather than persisted, 
 * and are used to tie together the common objects of the shared context at
 * a higher level of abstraction than specific data queries. 
 * 
 * In future this class could be used to reference other types of data associated 
 * with a shared context, for example to handle host/owner (see WOOKIE-66) or
 * could allow us to have a storage model for shared data that is completely
 * separate from the persistence model for the rest of Wookie, for example a
 * tuple store or distributed keystore
 */
public class SharedContext {
	
	private String apiKey;
	private String widgetId;
	private String contextId;
	private String viewerId;
	
	public SharedContext(AuthToken authToken){
		this.apiKey = authToken.getApiKey();
		this.widgetId = authToken.getWidgetId();
		this.contextId = authToken.getContextId();
		this.viewerId = authToken.getViewerId();
		
	}

	/**
	 * get the shared data belonging to this shared context
	 * @return an array of SharedData instances, or null if no shared data exists
	 */
	public ISharedData[] getSharedData(){
		//
		// Obtain a persistence manager and return the results of executing a query of SharedData objects matching the sharedDataKey
		//
		return SharedContextService.Factory.getInstance().getSharedData(apiKey, widgetId, contextId);
	}

	/**
	 * Find a specific shared data object for a given Widget Instance and object key
	 * @param instance the widget instance
	 * @param key the key of the shared data object, i.e. the tuple key not the shared data key
	 * @return a SharedData object, or null if no matches are found
	 */
	public ISharedData getSharedData(String key){
		return SharedContextService.Factory.getInstance().getSharedData(apiKey, widgetId, contextId, key);
	}

	/**
	 * Remove a single Shared Data Object
	 * @param name the name (key) of the data object
	 * @return true if a shared data object was located and deleted, false if no match was found
	 */
	public boolean removeSharedData(String key){
		return SharedContextService.Factory.getInstance().removeSharedData(apiKey, widgetId, contextId, key);
	}

	/**
	 * Update a single Shared Data object
	 * @param name the name (key) of the data object
	 * @param value the value to set, or null to clear the entry
	 * @param append set to true to append the value to the current value
	 * @return true if the value was updated, false if a new object was created
	 */
	public boolean updateSharedData(String key, String value, boolean append){
		return SharedContextService.Factory.getInstance().updateSharedData(apiKey, widgetId, contextId, key, value, append);
	}

	/**
	 * get the participants belonging to this shared context
	 * @return an arry of Participant instances, or null if there are no participants
	 */
	public IParticipant[] getParticipants(){
		return SharedContextService.Factory.getInstance().getParticipants(apiKey, widgetId, contextId);
	}

	/**
	 * Add a participant to a shared context
	 * @param participantId the id property of the participant to add
	 * @param participantDisplayName the display name property of the participant to add
	 * @param participantThumbnailUrl the thumbnail url property of the participant to add
	 * @return true if the participant was successfully added, otherwise false
	 */
	public boolean addParticipant(String participantId, String participantDisplayName, String participantThumbnailUrl) {
		return SharedContextService.Factory.getInstance().addParticipant(apiKey, widgetId, contextId, participantId, participantDisplayName, participantThumbnailUrl);
	}

	/**
	 * Add a participant to a shared context
	 * @param participantId the id property of the participant to add
	 * @param participantDisplayName the display name property of the participant to add
	 * @param participantThumbnailUrl the thumbnail url property of the participant to add
	 * @return true if the participant was successfully added, otherwise false
	 */
	public boolean addParticipant(String participantId, String participantDisplayName, String participantThumbnailUrl, String role) {
		return SharedContextService.Factory.getInstance().addParticipant(apiKey, widgetId, contextId, participantId, participantDisplayName, participantThumbnailUrl, role);
	}

	/**
	 * Remove a participant from the shared context
	 * @param participant
	 */
	public void removeParticipant(IParticipant participant){
		SharedContextService.Factory.getInstance().removeParticipant(apiKey, widgetId, contextId, participant);
	}

	/**
	 * Removes a participant from a widget instance
	 * @param participantId the id property of the participant
	 * @return true if the participant is successfully removed, otherwise false
	 */
	public boolean removeParticipant(String participantId) {
		return SharedContextService.Factory.getInstance().removeParticipant(apiKey, widgetId, contextId, participantId);
	}

	/**
	 * Get a participant by ID from this shared context
	 * @param participantId
	 * @return the participant, or null if there is no participant with the given id in the context
	 */
	public IParticipant getParticipant(String participantId){
		return SharedContextService.Factory.getInstance().getParticipant(apiKey, widgetId, contextId, participantId);    
	}

	/**
	 * Get the participant associated with the widget instance as the viewer
	 * @param widgetInstance
	 * @return the IParticipant representing the viewer, or null if no match is found
	 */
	@Deprecated
	public IParticipant getViewer(IWidgetInstance widgetInstance){
		return SharedContextService.Factory.getInstance().getParticipant(widgetInstance.getApiKey(), widgetInstance.getWidget().getIdentifier(), widgetInstance.getSharedDataKey(), widgetInstance.getUserId());    
	}

	/**
	 * Get the participant associated with the widget instance as the viewer
	 * @param widgetInstance
	 * @return the IParticipant representing the viewer, or null if no match is found
	 */
	public IParticipant getViewer(){
		return SharedContextService.Factory.getInstance().getParticipant(apiKey, widgetId, contextId, viewerId);    
	}

	/**
	 * Get the participant designated as the host of the shared context. Note that
	 * if there are multiple hosts, only the first is returned.
	 * @return a participant designated the host, or null if no participant is host
	 */
	public IParticipant getHost(){
		return SharedContextService.Factory.getInstance().getHost(apiKey, widgetId, contextId);    
	}

	public IParticipant[] getHosts(){
		return SharedContextService.Factory.getInstance().getHosts(apiKey, widgetId, contextId);    
	}
}
