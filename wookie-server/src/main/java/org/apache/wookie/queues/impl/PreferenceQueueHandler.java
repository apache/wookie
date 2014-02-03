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

import org.apache.log4j.Logger;
import org.apache.wookie.queues.QueueManager;
/**
 * This class handles preference requests to be sent to the database. 
 * 
 * @author Paul Sharples
 *
 */
public class PreferenceQueueHandler extends AbstractQueueHandler {
	
	public static Logger logger = Logger.getLogger(PreferenceQueueHandler.class);
		
	public PreferenceQueueHandler(String queueId) {
		super(queueId);		
		//logger.info("("+queueIdentifer+" )PreferenceQueueHandler* SHOULD ONLY EVER BE CALLED ONCE PER INSTANCE:");		
		initConsumerQueue(new PreferenceQueueConsumer(getQueue(), queueIdentifer));		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.impl.AbstractQueueHandler#destroy()
	 */
	public void destroy(){
		super.destroy();
		QueueManager.removePreferenceQueueFromManager(queueIdentifer);						
		queueConsumer.interrupt();
		//logger.info("("+queueIdentifer+" )PreferenceQueueHandler DESTROY thread");				
	}		
	
}
