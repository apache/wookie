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
package org.apache.wookie.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.services.SharedContextService;

public class DefaultSharedContextService implements SharedContextService {
	
	//
	// Tree structure APIKEY->CONTEXT->WIDGET->SharedContext
	//
	
	DefaultMutableTreeNode root;
	
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
			// Add participants and shared data
			//
			widgetNode.add(new DefaultMutableTreeNode(new SharedContext()));
		}
		contextNode.add(widgetNode);
		return widgetNode;
	}
	
	private SharedContext getSharedContextFromTree(String apiKey, String contextId, String widgetId){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getSharedContext(apiKey, contextId, widgetId).getFirstChild();
		return (SharedContext) node.getUserObject();
	}
	
	public DefaultSharedContextService(){
		root = new DefaultMutableTreeNode();
	}

	@Override
	public ISharedData[] getSharedData(String apiKey, String widgetId,
			String contextId){
		Collection<ISharedData> sharedData = getSharedContextFromTree(apiKey,contextId, widgetId).getSharedData().values();
		return sharedData.toArray(new ISharedData[sharedData.size()]);
	}

	@Override
	public ISharedData getSharedData(String apiKey, String widgetId,
			String contextId, String key) {
		HashMap<String, ISharedData> sharedData = getSharedContextFromTree(apiKey,contextId, widgetId).getSharedData();
		return sharedData.get(key);
	}

	@Override
	public boolean removeSharedData(String apiKey, String widgetId,
			String contextId, String key) {
		HashMap<String, ISharedData> sharedData = getSharedContextFromTree(apiKey,contextId, widgetId).getSharedData();
		if (!sharedData.containsKey(key)) return false;
		sharedData.remove(key);
		return true;
	}

	@Override
	public boolean updateSharedData(String apiKey, String widgetId,
			String contextId, String key, String value, boolean append) {	
		HashMap<String, ISharedData> sharedData = getSharedContextFromTree(apiKey,contextId, widgetId).getSharedData();
		boolean existing = true;
		if (!sharedData.containsKey(key)) existing = false;
		sharedData.put(key, new DefaultSharedDataImpl(key,value));
		return existing;
	}

	@Override
	public IParticipant[] getParticipants(String apiKey, String widgetId,
			String contextId) {
		Collection<IParticipant> participants = getSharedContextFromTree(apiKey,contextId, widgetId).getParticipants().values();
		return participants.toArray(new IParticipant[participants.size()]);
	}

	@Override
	public boolean addParticipant(String apiKey, String widgetId,
			String contextId, String participantId,
			String participantDisplayName, String participantThumbnailUrl) {
		return addParticipant(apiKey, widgetId, contextId, participantId, participantDisplayName, participantThumbnailUrl, "VIEWER");
	}

	@Override
	public boolean addParticipant(String apiKey, String widgetId,
			String contextId, String participantId,
			String participantDisplayName, String participantThumbnailUrl,
			String role) {
		HashMap<String, IParticipant> participants = getSharedContextFromTree(apiKey,contextId, widgetId).getParticipants();
		
		if (participants.containsKey(participantId)) return false;
		participants.put(participantId, new DefaultParticipantImpl(participantId,participantDisplayName, participantThumbnailUrl, role));
		return true;
	}

	@Override
	public void removeParticipant(String apiKey, String widgetId,
			String contextId, IParticipant participant) {
		HashMap<String, IParticipant> participants = getSharedContextFromTree(apiKey,contextId, widgetId).getParticipants();
		participants.remove(participant.getParticipantId());
	}

	@Override
	public boolean removeParticipant(String apiKey, String widgetId,
			String contextId, String participantId) {
		HashMap<String, IParticipant> participants = getSharedContextFromTree(apiKey,contextId, widgetId).getParticipants();
		if (!participants.containsKey(participantId)) return false;
		participants.remove(participantId);
		return true;
	}

	@Override
	public IParticipant getParticipant(String apiKey, String widgetId,
			String contextId, String participantId) {
		HashMap<String, IParticipant> participants = getSharedContextFromTree(apiKey,contextId, widgetId).getParticipants();
		return participants.get(participantId);
	}

	@Override
	public IParticipant getViewer(String apiKey, String widgetId,
			String contextId, String viewerId) {
		HashMap<String, IParticipant> participants = getSharedContextFromTree(apiKey,contextId, widgetId).getParticipants();
		return participants.get(viewerId);
	}

	@Override
	public IParticipant getHost(String apiKey, String widgetId, String contextId) {
		IParticipant[] hosts = getHosts(apiKey, widgetId, contextId);
		if (hosts != null && hosts.length > 0){
			return hosts[0];
		} else {
			return null;
		}
	}

	@Override
	public IParticipant[] getHosts(String apiKey, String widgetId,
			String contextId) {
		HashMap<String, IParticipant> participants = getSharedContextFromTree(apiKey,contextId, widgetId).getParticipants();
		ArrayList<IParticipant> hosts = new ArrayList<IParticipant>();
		for (IParticipant participant: participants.values()){
			if (participant.getRole().equals(IParticipant.HOST_ROLE)){
				hosts.add(participant);
			}
		}
		return hosts.toArray(new IParticipant[hosts.size()]);
	}

}
