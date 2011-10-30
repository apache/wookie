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
import org.apache.wookie.beans.SharedContext;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.queues.beans.IQueuedBean;
/**
 * Implementation of the shareddata Queue consumer
 * 
 * @author Paul Sharples
 *
 */
public class SharedDataQueueConsumer extends AbstractQueueConsumer {		

	public static Logger logger = Logger.getLogger(SharedDataQueueConsumer.class);	

	public SharedDataQueueConsumer(BlockingQueue<IQueuedBean> theQueue, String qIdentifier){ 
		super(theQueue, qIdentifier);				 
		this.setName(SharedDataQueueConsumer.class.getName() + ":" + qIdentifier);		
		//logger.info("("+queueIdentifer+")NEW SharedDataQueueConsumer starting");
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.impl.AbstractQueueConsumer#process(org.apache.wookie.queues.beans.IQueuedBean)
	 */
    public void process(IQueuedBean bean) {    	
    	//logger.info("("+queueIdentifer+")CONSUME START SharedDataQueueConsumer" + bean.getKey()+ "' TO '" + bean.getValue()+"'");  
    	try {   
    		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    		persistenceManager.begin();
    		IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(bean.getId_key());
    		if (widgetInstance != null){
    		  new SharedContext(widgetInstance).updateSharedData(bean.getKey(), bean.getValue(), bean.append());
    			persistenceManager.commit();
    		}
    	} 
    	catch (Exception ex) {    		
    		logger.error("("+queueIdentifer+ " to " +bean.getValue() + ")(Error setting SharedData: "+ ex, ex);
    	}
    	finally{
    		PersistenceManagerFactory.closePersistenceManager();
    	}
    	//logger.info("("+queueIdentifer+")CONSUME END SharedDataQueueConsumer" + bean.getKey()+ "' TO '" + bean.getValue()+"'");      	 
    }
  }