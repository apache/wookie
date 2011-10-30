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

import java.util.HashMap;
import java.util.Map;

import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.helpers.SharedDataHelper;

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
  
  private String sharedDataKey;
  
  public SharedContext(String sharedDataKey){
    this.sharedDataKey = sharedDataKey; 
  }
  
  public SharedContext(IWidgetInstance widgetInstance){
    //
    // Use the internal shared data key of the instance
    //
    this.sharedDataKey = SharedDataHelper.getInternalSharedDataKey(widgetInstance);
  }
  
  /**
   * get the shared data belonging to this shared context
   * @return an array of SharedData instances, or null if no shared data exists
   */
  public ISharedData[] getSharedData(){
    //
    // Obtain a persistence manager and return the results of executing a query of SharedData objects matching the sharedDataKey
    //
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    return (ISharedData[]) persistenceManager.findByValue(ISharedData.class, "sharedDataKey", this.sharedDataKey);
  }
  
  /**
   * Find a specific shared data object for a given Widget Instance and object key
   * @param instance the widget instance
   * @param key the key of the shared data object, i.e. the tuple key not the shared data key
   * @return a SharedData object, or null if no matches are found
   */
  public ISharedData getSharedData(String key){
    
    //
    // Obtain a persistence manager and construct a query of SharedData objects matching the sharedDataKey and dkey
    //
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("sharedDataKey", this.sharedDataKey);
    params.put("dkey", key);
    
    //
    // Execute the query and obtain array of results
    // We assert that there are never duplicates.
    //
    ISharedData[] results = (ISharedData[]) persistenceManager.findByValues(ISharedData.class, params);
    assert(results.length <= 1);
    
    //
    // If the result contains a single item, return it, otherwise return null.
    //
    if (results.length != 0) return results[0];
    return null;
  }
  
  /**
   * Remove a single Shared Data Object
   * @param name the name (key) of the data object
   * @return true if a shared data object was located and deleted, false if no match was found
   */
  public boolean removeSharedData(String name){
    //
    // Find a matching item
    //
    ISharedData sharedData = this.getSharedData(name);
    
    //
    // If a match is found, delete it and return true; 
    // otherwise return false
    //
    if (sharedData == null) return false;
    this.removeSharedData(sharedData);
    return true;
  }
  
  /**
   * Removes (deletes) the shared data object
   * @param sharedData
   */
  private void removeSharedData(ISharedData sharedData){
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    persistenceManager.delete(sharedData);
  }
  
  /**
   * Creates a new shared data object in this shared context
   * @param name
   * @param value
   */
  private void addSharedData(String name, String value){
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    ISharedData sharedData = persistenceManager.newInstance(ISharedData.class);
    sharedData.setSharedDataKey(this.sharedDataKey);
    sharedData.setDkey(name);
    sharedData.setDvalue(value);
    persistenceManager.save(sharedData);
  }
  
  /**
   * Update a single Shared Data object
   * @param name the name (key) of the data object
   * @param value the value to set, or null to clear the entry
   * @param append set to true to append the value to the current value
   * @return true if the value was updated, false if a new object was created
   */
  public boolean updateSharedData(String name, String value, boolean append){
    boolean found=false;
    ISharedData sharedData = this.getSharedData(name);
    
    //
    // An existing object is found, so either update or delete
    //
    if (sharedData != null) {
      
      //
      // If the value is set to Null, remove the object
      //
      if(value==null || value.equalsIgnoreCase("null")) {
        this.removeSharedData(sharedData);
      } else {  
        
        //
        // Either append the new value to the old, or overwrite it
        //
        if(append) {
          sharedData.setDvalue(sharedData.getDvalue() + value);
        } else {
          sharedData.setDvalue(value);
        }
      }
      found=true;
    }
    
    //
    // No matching object exists, so create a new object
    //
    if(!found && value != null && !value.equalsIgnoreCase("null")){  
        addSharedData(name, value);
    }
    return found;
  }
  
  /**
   * get the participants belonging to this shared context
   * @return an arry of Participant instances, or null if there are no participants
   */
  public IParticipant[] getParticipants(){
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    return persistenceManager.findByValue(IParticipant.class, "sharedDataKey", this.sharedDataKey);
  }
  
  /**
   * Add a participant to a shared context
   * @param participantId the id property of the participant to add
   * @param participantDisplayName the display name property of the participant to add
   * @param participantThumbnailUrl the thumbnail url property of the participant to add
   * @return true if the participant was successfully added, otherwise false
   */
  public boolean addParticipant(String participantId, String participantDisplayName, String participantThumbnailUrl) {

    //
    // Does participant already exist?
    //
    IParticipant participant = this.getParticipant(participantId);
    if (participant != null) return false;
    
    //
    // Add participant
    //
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    participant = persistenceManager.newInstance(IParticipant.class);
    participant.setParticipantId(participantId);
    participant.setParticipantDisplayName(participantDisplayName);
    participant.setParticipantThumbnailUrl(participantThumbnailUrl);
    participant.setSharedDataKey(this.sharedDataKey);
    persistenceManager.save(participant);
    return true;
  }

  /**
   * Remove a participant from the shared context
   * @param participant
   */
  public void removeParticipant(IParticipant participant){
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    persistenceManager.delete(participant);
  }
  
  /**
   * Removes a participant from a widget instance
   * @param participantId the id property of the participant
   * @return true if the participant is successfully removed, otherwise false
   */
  public boolean removeParticipant(String participantId) {

    //
    // Does participant exist?
    //
    IParticipant participant = this.getParticipant(participantId);
    if (participant != null){
     
      //
      // Remove participant
      //
      removeParticipant(participant);
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * Get a participant by ID from this shared context
   * @param participantId
   * @return the participant, or null if there is no participant with the given id in the context
   */
  public IParticipant getParticipant(String participantId){
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sharedDataKey", this.sharedDataKey);//$NON-NLS-1$
    map.put("participantId", participantId);//$NON-NLS-1$
    IParticipant[] participants = persistenceManager.findByValues(IParticipant.class, map);
    if (participants.length == 1) return participants[0];
    return null;     
  }
  
  /**
   * Get the participant associated with the widget instance as the viewer
   * @param widgetInstance
   * @return the IParticipant representing the viewer, or null if no match is found
   */
  public IParticipant getViewer(IWidgetInstance widgetInstance){
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("sharedDataKey", this.sharedDataKey);//$NON-NLS-1$
    map.put("participantId", widgetInstance.getUserId());//$NON-NLS-1$
    IParticipant [] participants = persistenceManager.findByValues(IParticipant.class, map);
    if(participants != null && participants.length == 1) return participants[0];
    return null;
  }
}
