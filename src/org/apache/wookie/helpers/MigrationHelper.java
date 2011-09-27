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

package org.apache.wookie.helpers;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.controller.WidgetInstancesController;

/**
 * Migration Helper
 * 
 * This class provides helper methods for migrating from earlier versions of Wookie.
 * 
 * Note that this class and/or its methods may be deprecated in future releases.
 */
public class MigrationHelper {
  
  static Logger _logger = Logger.getLogger(MigrationHelper.class.getName());

  /**
   * Finds a widget instance using supplied parameters, using legacy shared data key generation methods. If an instance
   * is found, the instance and any related resources are updated to use the current generation methods.
   * @param apiKey
   * @param userId
   * @param sharedDataKey the shared data key, in its original external form
   * @param widgetId
   * @param serviceType
   * @return the widget instance, or null if there is no match
   * @throws UnsupportedEncodingException
   */
  public static IWidgetInstance findLegacyWidgetInstance(String apiKey, String userId, String sharedDataKey, String widgetId, String serviceType) throws UnsupportedEncodingException{
    //
    // Get shared data key using legacy method
    //
    String legacySharedDataKey = getLegacySharedDataKey(sharedDataKey);

    //
    // Find widget instance
    //
    IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(apiKey, userId, legacySharedDataKey, widgetId, serviceType);

    //
    // Match found, so migrate the instance as well as any sibling instances that use the same shared data key, and any shared data instances
    //
    if (instance != null){  

      //
      // Get shared data key using current method
      //
      String internalSharedDataKey = SharedDataHelper.getInternalSharedDataKey(apiKey, widgetId, sharedDataKey);

      //
      // migrate instances and shared data using the legacy key to use new shared data hashcodes
      //
      migrateSharedDataKeys(legacySharedDataKey, internalSharedDataKey);
    }
    return instance;
  }

  /**
   * Get the legacy version of the shared data key, from 0.9.0
   * 
   * @param externalSharedDataKey
   * @return the legacy shared data key as a String
   */
  private static String getLegacySharedDataKey(String externalSharedDataKey){
    String key = "null:"+externalSharedDataKey;
    return String.valueOf(key.hashCode());
  }

  /**
   * Migrate objects based on shared data keys
   * 
   * @param oldKey the old legacy shared data key
   * @param newKey the new key to use as replacement
   */
  private static void migrateSharedDataKeys(String oldKey, String newKey){

    //
    // Locate instances using the old key and migrate to the new key
    //
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    IWidgetInstance[] widgetInstances = persistenceManager.findByValue(IWidgetInstance.class, "sharedDataKey", oldKey);
    for (IWidgetInstance widgetInstance:widgetInstances){
      widgetInstance.setSharedDataKey(newKey);
      persistenceManager.save(widgetInstance);
    }

    //
    // locate shared data objects using the old key and migrate to the new key
    //
    ISharedData[] sharedDataItems = persistenceManager.findByValue(ISharedData.class, "sharedDataKey", oldKey);
    for (ISharedData sharedData: sharedDataItems){
      sharedData.setSharedDataKey(newKey);
      persistenceManager.save(sharedData);
    }
    
    _logger.debug("upgraded widget instances and shared data from 0.9.0. Old key:"+oldKey+" . New key:"+newKey+". "+widgetInstances.length+" widget instances  and "+sharedDataItems.length+" shared data objects updated.");
  }

}
