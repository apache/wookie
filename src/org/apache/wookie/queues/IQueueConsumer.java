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

import org.apache.wookie.queues.beans.IQueuedBean;

/**
 * 
 * @author Paul Sharples
 *
 */
public interface IQueueConsumer {

	/**
	 * set the name of the queue
	 * @param string
	 */
	void setName(String string);

	/**
	 * run as daemon
	 * @param b
	 */
	void setDaemon(boolean b);

	/**
	 * start it
	 */
	void start();

	/**
	 * use interrupt to stop the thread
	 */
	void interrupt();
	
	abstract void process(IQueuedBean bean);
}
