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

import java.util.HashMap;

import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;

/**
 * Service facade for managing SharedDataKeys in a consistent fashion.
 * 
 * SharedDataKeys have an external form used by the host application (e.g. "tab23").
 * 
 * However we also have an internal form which avoids potential collisions, and which is unique
 * to the combination of the host application, external shared data key, and the widget URI
 */
public class SharedDataHelper {
	
	public static String getInternalSharedDataKey(IWidgetInstance instance){
		String key = instance.getSharedDataKey() + ":" + instance.getApiKey() + ":" + instance.getWidget().getGuid();
		return String.valueOf(key.hashCode());
	}
	
	public static String getInternalSharedDataKey(IWidgetInstance instance, String sharedDataKey){
		String key = sharedDataKey + ":" + instance.getApiKey() + ":" + instance.getWidget().getGuid();
		return String.valueOf(key.hashCode());
	}
	
	public static ISharedData[] findSharedData(IWidgetInstance instance){
		String sharedDataKey = SharedDataHelper.getInternalSharedDataKey(instance);
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        return (ISharedData[]) persistenceManager.findByValue(ISharedData.class, "sharedDataKey", sharedDataKey);
	}
	
	public static ISharedData findSharedData(IWidgetInstance instance, String key){
		String sharedDataKey = SharedDataHelper.getInternalSharedDataKey(instance);
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("sharedDataKey", sharedDataKey);
        params.put("dkey", key);
        ISharedData[] results = (ISharedData[]) persistenceManager.findByValues(ISharedData.class, params);
        if (results.length != 0) return results[0];
        return null;
	}	

}
