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
package org.apache.wookie.queues.util;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.wookie.queues.IQueueHandler;
/**
 * Watches a given queue and will stop the consumer thread when needed and then cleanup
 * 
 * @author Paul Sharples
 *
 */
public class QueueWatcherTask extends TimerTask {

	// parent object - so we can clean up when finished
	private IQueueHandler parent;
	// local timer
	private Timer timer;
	
	/**
	 * start the timer (5 second interval)
	 */
	public void init(){
		 timer = new Timer();		 
		 timer.scheduleAtFixedRate(this, 5000L, 5000L);
	}
	
	public QueueWatcherTask(IQueueHandler parentHandler) {
		parent = parentHandler;
		init();
	}

	/**
	 * This method looks at the parent queue and sees if it is empty. if it is then it will wait a further 
	 * 5 seconds by setting the threadterminationcount to zero and then checking again after 5 seconds
	 * This is just to make sure the queue has been empty for at least 5 seconds before destroying it
	 * and doesn't catch it the split second it happens to be empty
	 */
	@Override
	public void run() {	
		//System.out.println("(" + parent.getQueueIdentifer() + ") tcount="+parent.getThreadTerminationCount()+" queue size="+parent.getQueue().size());
		 if(parent.getThreadTerminationCount() < 1 && parent.getQueue().isEmpty()){
			 timer.cancel();
			 parent.destroy();
			 this.cancel();
		 }
		 else{
			 parent.setThreadTerminationCount(0);
		 }
	}

}
