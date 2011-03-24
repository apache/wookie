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
import org.apache.wookie.queues.IQueueConsumer;
import org.apache.wookie.queues.beans.IQueuedBean;
/**
 * 
 * This class is designed to be extended for either a preference scenario or shareddata scanario
 * and contains their common functionality
 * 
 * @author Paul Sharples
 *
 */
public abstract class AbstractQueueConsumer extends Thread implements IQueueConsumer {
	
	public static Logger logger = Logger.getLogger(PreferenceQueueConsumer.class);
	// the queue
	protected BlockingQueue<IQueuedBean> queue;
	// the queue identifier
	protected String queueIdentifer;

	public AbstractQueueConsumer(BlockingQueue<IQueuedBean> queue, String queueIdentifer) {
		super();
		this.queue = queue;
		this.queueIdentifer = queueIdentifer;		
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			while (true) {
				IQueuedBean bean = (IQueuedBean)queue.take();
				process(bean);
			}						
		} 
		catch (InterruptedException ex) {		
			//logger.info("("+queueIdentifer+")END QueueConsumer stopping");
			Thread.currentThread().interrupt();			
			return;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.IQueueConsumer#process(org.apache.wookie.queues.beans.IQueuedBean)
	 */
	public abstract void process(IQueuedBean bean);

}
