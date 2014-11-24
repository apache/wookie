/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
package org.apache.wookie.services;

import java.util.ServiceLoader;

import org.apache.wookie.services.impl.DefaultWorkflowService;

/**
 * The workflow service handles flow control properties related to widgets, such as locking down all
 * widgets in a particular context to prevent modifying their state.
 */
public interface WorkflowService {
	
	/**
	 * Set the status to locked
	 * @param apiKey
	 * @param widgetId
	 * @param contextId
	 */
	public abstract void lock(String apiKey, String widgetId, String contextId);
	
	/**
	 * Set the status to unlocked
	 * @param apiKey
	 * @param widgetId
	 * @param contextId
	 */
	public abstract void unlock(String apiKey, String widgetId, String contextId);
	
	/**
	 * Get the current lock status
	 * @param apiKey
	 * @param widgetId
	 * @param contextId
	 * @return
	 */
	public abstract boolean isLocked(String apiKey, String widgetId, String contextId);
	
	public static class Factory {
		
		private static WorkflowService provider;
		
	    public static WorkflowService getInstance() {
	    	//
	    	// Use the service loader to load an implementation if one is available
	    	//
	    	if (provider == null){
	    		ServiceLoader<WorkflowService> ldr = ServiceLoader.load(WorkflowService.class);
	    		for (WorkflowService service : ldr) {
	    			// We are only expecting one
	    			provider = service;
	    		}
	    	}
	    	//
	    	// If no service provider is found, use the default
	    	//
	    	if (provider == null){
	    		provider = new DefaultWorkflowService();
	    	}
	    	
	    	return provider;
	    }
	}
}
