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
package org.apache.wookie.queues;

import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.wookie.queues.beans.IQueuedBean;
import org.apache.wookie.queues.beans.impl.QueuedBean;
import org.apache.wookie.queues.impl.PreferenceQueueHandler;
import org.apache.wookie.queues.impl.SharedDataQueueHandler;
/**
 * QueueManager (One instance per wookie) is designed to queue requests to update
 * preferences and shareddata records so that there are no concurrent
 * modifications made to the SAME record at the SAME time in the database, this can happen
 * particularly with shared data requests (imagine 100s of clients trying to update a chat log at the same time)
 * Therefore two hashmaps (one for preferences, the other for shareddata) keep a list of queues for
 * accessing a particular preference/shareddata. In the case of preferences, a queue is made unique 
 * by assigning it a queue name of instancekey and preference name concatenated together. In the
 * case of shared data, the shareddata key and shareddata name are concatenated together. 
 * 
 * The Hashtables in each case contains a list of QueueHandlers. A QueueHandler is responsible for its own queue,
 * a consumer thread (which processes each request and commits it to the database) and a watcher task which will destroy the queue
 * resources when needed.  The queues themselves are short lived and are designed to be destroyed after a short period of inactivity.
 * 
 * @author Paul Sharples
 * @version $Id$ 
 *
 */
public class QueueManager {

	public static Logger logger = Logger.getLogger(QueueManager.class);
	// list of preference queues
	private static HashMap<String, PreferenceQueueHandler> preferenceQueueList = new HashMap<String, PreferenceQueueHandler>();
	// list of shared data queues	
	private static HashMap<String, SharedDataQueueHandler> sharedDataQueueList = new HashMap<String, SharedDataQueueHandler>();	
	protected static QueueManager instance = null;
	
	/**
	 * Non public constructor  
	 */
	private QueueManager(){};
	
	/**
	 * Should only ever be one queuemanager
	 * @return the instance
	 */
	public static synchronized QueueManager getInstance(){
		if (instance == null){
			//logger.info("QUEUE MANAGER CONSTRUCTOR***********SHOULD ONLY EVER BE CALLED ONCE************");
			instance = new QueueManager();			
		}				
		return instance;
	}
		
	/**
	 * Add a request for setting a preference to the correct queue
	 * @param id_key
	 * @param key
	 * @param value
	 */
	public static synchronized void queueSetPreferenceRequest(String id_key, String key, String value){
		IQueuedBean preferenceBean = new QueuedBean(id_key, key, value, false);
		String queueKey = id_key + "-" + key;
		PreferenceQueueHandler queueHandler;
		if(preferenceQueueList.containsKey(queueKey)){
			queueHandler = (PreferenceQueueHandler) preferenceQueueList.get(queueKey);			
		}
		else{
			queueHandler = new PreferenceQueueHandler(queueKey);
			preferenceQueueList.put(queueKey, queueHandler);		
		}
		queueHandler.addBeanToQueue(preferenceBean);
	}
	
	/**
	 * Add a request for setting a named shareddata value to the correct queue
	 * @param id_key
	 * @param sharedDataKey
	 * @param key
	 * @param value
	 * @param append
	 */
	public static synchronized void queueSetSharedDataRequest(String id_key, String sharedDataKey, String key, String value, boolean append){	
		IQueuedBean sharedDataBean = new QueuedBean(id_key, key, value, append);
		String queueKey = sharedDataKey + "-" + key; // use shareddatakey here instead of instancekey
		SharedDataQueueHandler queueHandler;
		if(sharedDataQueueList.containsKey(queueKey)){
			queueHandler = (SharedDataQueueHandler) sharedDataQueueList.get(queueKey);			
		}
		else{
			queueHandler = new SharedDataQueueHandler(queueKey);
			sharedDataQueueList.put(queueKey, queueHandler);		
		}
		queueHandler.addBeanToQueue(sharedDataBean);			
	}
	
	/**
	 * Remove the shared data queue from the manager
	 * @param key
	 */
	public static synchronized void removeSharedDataQueueFromManager(String key){
		if(sharedDataQueueList.containsKey(key)){
			sharedDataQueueList.remove(key);
		}
	}

	/**
	 * Remove the preference queue from the manager
	 * @param key
	 */

	public static synchronized void removePreferenceQueueFromManager(String key){
		if(preferenceQueueList.containsKey(key)){
			preferenceQueueList.remove(key);
		}
	}
}
