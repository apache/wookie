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
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.wookie.queues.IQueueConsumer;
import org.apache.wookie.queues.IQueueHandler;
import org.apache.wookie.queues.beans.IQueuedBean;
import org.apache.wookie.queues.util.QueueWatcherTask;
/**
 * This class mananges the both the queue and the consumer thread which reads from the queue
 * and processes the requests to the database/backend
 * 
 * This class is designed to be extended for either a preference scenario or shareddata scanario
 * and contains their common functionality
 * 
 * @author Paul Sharples
 *
 */
public abstract class AbstractQueueHandler implements IQueueHandler {	
	// the queue
	private BlockingQueue<IQueuedBean> queue = new LinkedBlockingQueue<IQueuedBean>();
	// the queue consumer
	protected IQueueConsumer queueConsumer;
	// the thread that watches the queue
	protected QueueWatcherTask threadWatcher;
	// the queue identifier
	protected String queueIdentifer;
	// a count used to flag when to cancel/stop the queue
	protected int threadTerminationCount = 1;
	
	public AbstractQueueHandler(String queueId) {
		super();
		queueIdentifer = queueId;
		threadWatcher = new QueueWatcherTask(this);	
	}
	
	/**
	 * setup - initialise the queue and start the consumer thread
	 */
	public void initConsumerQueue(IQueueConsumer qConsumer){
		queueConsumer = qConsumer;
		queueConsumer.setName("consumer@"+queueIdentifer);
		queueConsumer.setDaemon(true);
		queueConsumer.start();		
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.IQueueHandler#getQueueIdentifer()
	 */
	public String getQueueIdentifer() {
		return queueIdentifer;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.IQueueHandler#getThreadTerminationCount()
	 */
	public int getThreadTerminationCount() {
		return threadTerminationCount;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.IQueueHandler#setThreadTerminationCount(int)
	 */
	public void setThreadTerminationCount(int threadTerminationCount) {
		this.threadTerminationCount = threadTerminationCount;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.IQueueHandler#getQueue()
	 */
	public BlockingQueue<IQueuedBean> getQueue() {
		return queue;
	}
	
	/*
	 * 
	 */
	public void addBeanToQueue(IQueuedBean bean){
		queue.add(bean);
		threadTerminationCount++;		
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.IQueueHandler#destroy()
	 */
	public void destroy(){
		queue = null;
		threadWatcher = null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.queues.IQueueHandler#getConsumer()
	 */
	public IQueueConsumer getConsumer(){
		return queueConsumer;
	}

}
