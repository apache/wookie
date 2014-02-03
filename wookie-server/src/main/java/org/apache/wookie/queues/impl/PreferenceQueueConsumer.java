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
package org.apache.wookie.queues.impl;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.controller.PropertiesController;
import org.apache.wookie.queues.beans.IQueuedBean;
/**
 * Implementation of the Preference Queue consumer
 * 
 * @author Paul Sharples
 *
 */
public class PreferenceQueueConsumer extends AbstractQueueConsumer {		

	public static Logger logger = Logger.getLogger(PreferenceQueueConsumer.class);
	
	public PreferenceQueueConsumer(BlockingQueue<IQueuedBean> theQueue, String qIdentifier){ 
		super(theQueue, qIdentifier);
		this.setName(PreferenceQueueConsumer.class.getName() + ":" + qIdentifier);		
		//logger.info("("+queueIdentifer+")NEW PreferenceQueueConsumer starting");
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.impl.AbstractQueueConsumer#process(org.apache.wookie.queues.beans.IQueuedBean)
	 */
   public void process(IQueuedBean bean) {
    	//logger.info("("+queueIdentifer+")CONSUME START PreferenceQueueConsumer" + bean.getKey()+ "' TO '" + bean.getValue()+"'");  
    	try {   
    		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    		persistenceManager.begin();
    		IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(bean.getId_key());
    		if (widgetInstance != null){
    			PropertiesController.updatePreference(widgetInstance, bean.getKey(), bean.getValue());
    			persistenceManager.commit();
    		}
    	} 
    	catch (Exception ex) {    		
    		logger.error("("+queueIdentifer+ " to " +bean.getValue() + ")(Error setting preference: "+ ex, ex);
    	}
    	finally{
    		PersistenceManagerFactory.closePersistenceManager();
    	}
    	//logger.info("("+queueIdentifer+")CONSUME END PreferenceQueueConsumer" + bean.getKey()+ "' TO '" + bean.getValue()+"'");  
    }
  }

