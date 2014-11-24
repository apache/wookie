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
package org.apache.wookie.services.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wookie.services.WorkflowService;

/**
 * Default workflow service implementation. Backed by a tree and hashmaps in memory.
 */
public class DefaultWorkflowService implements WorkflowService {


	public DefaultWorkflowService(){
		root = new DefaultMutableTreeNode();
	}
	
	@Override
	public void lock(String apiKey, String widgetId, String contextId) {
		setProperty(apiKey, contextId, widgetId, "locked", true);
	}

	@Override
	public void unlock(String apiKey, String widgetId, String contextId) {
		setProperty(apiKey, contextId, widgetId, "locked", false);
	}

	@Override
	public boolean isLocked(String apiKey, String widgetId, String contextId) {
		Object property = getProperty(apiKey, contextId, widgetId, "locked");
		if (property == null) property = false;
		return (Boolean) property;
	}
	
	
	/////
	
	
	//
	// Tree structure APIKEY->CONTEXT->WIDGET->SharedContext
	//
	
	DefaultMutableTreeNode root;
	
	@SuppressWarnings("rawtypes")
	private DefaultMutableTreeNode getSharedContext(String apiKey, String contextId, String widgetId){
		
		DefaultMutableTreeNode apiKeyNode = null;
		
		Enumeration apiKeys = root.children();
		while(apiKeys.hasMoreElements()){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) apiKeys.nextElement();
			if (node.getUserObject().equals(apiKey)){
				apiKeyNode = node;
			}
		}
		if (apiKeyNode == null){
			apiKeyNode = new DefaultMutableTreeNode(apiKey); 
			root.add(apiKeyNode);
		}
		
		//
		// Context level
		//
		DefaultMutableTreeNode contextNode = null;
		Enumeration contexts = apiKeyNode.children();
		while(contexts.hasMoreElements()){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) contexts.nextElement();
			if (node.getUserObject().equals(contextId)){
				contextNode = node;
			}
		}
		if (contextNode == null){
			contextNode = new DefaultMutableTreeNode(contextId);
			apiKeyNode.add(contextNode);
		}
		
		//
		// Widget level
		//
		DefaultMutableTreeNode widgetNode = null;
		Enumeration widgets = contextNode.children();
		while(widgets.hasMoreElements()){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) widgets.nextElement();
			if (node.getUserObject().equals(widgetId)){
				widgetNode = node;
			} else {
				System.out.println(node.getUserObject());
			}
		}
		if (widgetNode == null){
			widgetNode = new DefaultMutableTreeNode(widgetId);
			contextNode.add(widgetNode);
			
			//
			// Add properties
			//
			Map<String, Object>tuples = new HashMap<String, Object>();	
			widgetNode.add(new DefaultMutableTreeNode(tuples));
		}
		contextNode.add(widgetNode);
		return widgetNode;
	}
	
	private Map<String, Object> getTuplesFromTree(String apiKey, String contextId, String widgetId){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getSharedContext(apiKey, contextId, widgetId).getFirstChild();
		return (Map<String, Object>) node.getUserObject();
	}
	
	private Object getProperty(String apiKey, String contextId, String widgetId, String propertyName){
		Map<String, Object> tuples = getTuplesFromTree(apiKey, widgetId, contextId);
		return tuples.get(propertyName);
	}
	
	private void setProperty(String apiKey, String contextId, String widgetId, String propertyName, Object value){
		Map<String, Object> tuples = getTuplesFromTree(apiKey, widgetId, contextId);
		tuples.put(propertyName, value);
	}

}

