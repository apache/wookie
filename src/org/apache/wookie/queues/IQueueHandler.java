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

import java.util.concurrent.BlockingQueue;

import org.apache.wookie.queues.beans.IQueuedBean;
/**
 * 
 * @author Paul Sharples
 *
 */
public interface IQueueHandler {
	/**
	 * Get the identifier of the queue
	 * @return
	 */
	String getQueueIdentifer();
	
	/**
	 * return the current count of requests added to queue
	 * @return
	 */
	int getThreadTerminationCount();
	
	/**
	 * update the current count of requests added to queue
	 * @param num
	 */
	void setThreadTerminationCount(int num);
	
	/**
	 * return the queue
	 * @return
	 */
	BlockingQueue<IQueuedBean> getQueue();
	
	/**
	 * cleanup
	 */
	void destroy();
	
	/**
	 * return the consumer thread
	 */
	IQueueConsumer getConsumer();
}
