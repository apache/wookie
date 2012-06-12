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
package org.apache.wookie.helpers;

import org.apache.wookie.beans.IWidgetInstance;


/**
 * Service facade for managing SharedDataKeys in a consistent fashion.
 * 
 * SharedDataKeys have an external form used by the host application (e.g. "tab23").
 * 
 * However we also have an internal form which avoids potential collisions, and which is unique
 * to the combination of the host application (API key), external shared data key, and the widget URI
 */
public class SharedDataHelper {
  
  /**
   * Get the external form of the shared data key; i.e. the key provided by the client and 
   * unique only within the scope of the client API key
   * @param instance the widget instance
   * @return the external shared data key for the widget instance
   * FIXME Not implemented yet. We need somewhere to safely store external shared data keys
   */
    public static String getExternalSharedDataKey(IWidgetInstance instance){
      return null;
    }
	
  /**
   * Get the internal form of the shared data key
   * @param instance the widget instance
   * @return the internal shared data key for the widget instance
   */
	public static String getInternalSharedDataKey(IWidgetInstance instance){
	  return instance.getSharedDataKey();
	}
	
	/**
	 * Create an internal shared data key given a widget instance and external shared data key
	 * @param instance the widget instance
	 * @param externalSharedDataKey the shared data key provided by the client plugin
	 * @return the internal shared data key for the instance
	 */
	public static String getInternalSharedDataKey(IWidgetInstance instance, String externalSharedDataKey){
		String key = externalSharedDataKey + ":" + instance.getApiKey() + ":" + instance.getWidget().getIdentifier();
		return String.valueOf(key.hashCode());
	}
	
	/**
	 * Create an internal shared data key given an API key, widget URI and external shared data key
	 * @param apiKey the API key used to request widget instances
	 * @param widgetUri the Widget's external URI-based identifier
	 * @param externalSharedDataKey the shared data key provided by the client plugin
	 * @return the internal shared data key
	 */
	 public static String getInternalSharedDataKey(String apiKey, String widgetUri, String externalSharedDataKey){
	    String key = externalSharedDataKey + ":" + apiKey + ":" + widgetUri;
	    return String.valueOf(key.hashCode());
	  }

}
