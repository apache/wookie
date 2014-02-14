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
package org.apache.wookie.services.redis;

import java.util.ArrayList;
import java.util.List;

import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.services.SharedContextService;
import org.apache.wookie.services.impl.DefaultParticipantImpl;
import org.apache.wookie.services.impl.DefaultSharedDataImpl;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * SharedContext SPI implementation using a Redis nosql database.
 */
public class RedisSharedContextService implements SharedContextService {
	
	/**
	 * The thread pool for jedis
	 */
	private JedisPool pool;
	
	public RedisSharedContextService(){
		pool = new JedisPool(new JedisPoolConfig(), "localhost");
	}
	
	////// Data API

	@Override
	public ISharedData[] getSharedData(String apiKey, String widgetId,
			String contextId) {
		//
		// get the redis context key
		//
		String context = this.getContextKey(apiKey, widgetId, contextId);
		
		//
		// get a jedis connector
		//
		Jedis jedis = pool.getResource();
		
		//
		// Get the context list
		//
		List<String> keys = jedis.lrange(context, 0, 999);
		ArrayList<ISharedData> data = new ArrayList<ISharedData>();
		
		//
		// For each item in the list, if its a data object, get 
		// it and add it to the array
		//
		for (String key: keys){
			
			//
			// Get the ISharedData for each key on the list and 
			// add it to the arraylist
			//
			if (key.startsWith("data")){
			String json = jedis.get(key);
			ISharedData item = rehydrateData(json);
			data.add(item);
			}
		}
		
		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);
		
		//
		// Return the array of ISharedData objects
		//
		return data.toArray(new ISharedData[data.size()]);
	}

	@Override
	public ISharedData getSharedData(String apiKey, String widgetId,
			String contextId, String name) {

		//
		// Get the key
		//
		String key = this.getDataKey(apiKey, widgetId, contextId, name);
		
		//
		// get a jedis connector
		//
		Jedis jedis = pool.getResource();
		
		//
		// get an object from the store and rehydrate it from json
		//
		String json = jedis.get(key);
		ISharedData data = rehydrateData(json);
		
		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);
		
		//
		// Return the object
		//
		return data;
	}

	@Override
	public boolean removeSharedData(String apiKey, String widgetId,
			String contextId, String name) {

		//
		// If there is no key, return false
		//
		if (name == null) return false;
		if (name.trim().length() == 0) return false;

		//
		// Get a Jedis from the pool
		//
		Jedis jedis = pool.getResource();

		//
		// get the Redis key for the participant
		//
		String key = this.getDataKey(apiKey, widgetId, contextId, name);
		
		//
		// if it doesn't exist, return false
		//
		if ( !jedis.exists(key)){
			pool.returnResource(jedis);
			return false;
		}

		//
		// Delete the object
		//
		jedis.del(key);

		//
		// Remove the key from the context list
		//
		String context = this.getContextKey(apiKey, widgetId, contextId);
		jedis.lrem(context, 0, key);

		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);

		//
		// OK
		//
		return true;
	}

	public boolean updateSharedData(String apiKey, String widgetId,
			String contextId, ISharedData data, boolean append) {
		//
		// Check if the data is null
		//
		if (data == null) return false;
		
		//
		// If the value is null, and we're not set to append, this is 
		// actually the same as "remove"
		//
		if (data.getDvalue() == null){
			return this.removeSharedData(apiKey, widgetId, contextId, data.getDkey());
		}
		
		//
		// Get a Jedis from the pool
		//
		Jedis jedis = pool.getResource();
		
		//
		// Get the redis key to use for this 
		//
		String key = getDataKey(apiKey, widgetId, contextId, data.getDkey());
		String context = getContextKey(apiKey, widgetId, contextId);
		
		//
		// If there is no existing tuple, add the key to the list of keys for this token
		//
		ISharedData existing = getSharedData(apiKey, widgetId, contextId, data.getDkey());
		
		if (existing == null){
			jedis.lpush(context, key);			
		} else {
			//
			// if it already exists, and the instruction is to append, prepend the 
			// existing value to the new value to set
			//
			if (append){
				data.setDvalue(existing.getDvalue() + data.getDvalue());
			}
		}

		//
		// Convert the object into JSON for storage
		//
		String json = dehydrateData(data);
		
		//
		// Store the preference
		//
		jedis.set(key, json);
		
		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);
		
		//
		// OK
		//
		return true;
		
	}

		
	@Override
	public boolean updateSharedData(String apiKey, String widgetId,
			String contextId, String name, String value, boolean append) {
		ISharedData data = new DefaultSharedDataImpl(name, value);
		return this.updateSharedData(apiKey, widgetId, contextId, data, append);
	}
	
	
	///// Participants API

	@Override
	public IParticipant[] getParticipants(String apiKey, String widgetId,
			String contextId) {
		//
		// get the redis context key
		//
		String context = this.getContextKey(apiKey, widgetId, contextId);
		
		//
		// get a jedis connector
		//
		Jedis jedis = pool.getResource();
		
		//
		// Get the context list
		//
		List<String> keys = jedis.lrange(context, 0, 999);
		ArrayList<IParticipant> participants = new ArrayList<IParticipant>();
		
		//
		// For each item in the list, if its a IParticipant object, get 
		// it and add it to the array
		//
		for (String key: keys){
			
			//
			// Get the IParticipant for each key on the list and 
			// add it to the arraylist
			//
			if (key.startsWith("participant")){
			String json = jedis.get(key);
			IParticipant item = rehydrateParticipant(json);
			participants.add(item);
			}
		}
		
		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);
		
		//
		// Return the array of ISharedData objects
		//
		return participants.toArray(new IParticipant[participants.size()]);
	}

	@Override
	public boolean addParticipant(String apiKey, String widgetId,
			String contextId, String participantId,
			String participantDisplayName, String participantThumbnailUrl) {
		return addParticipant(apiKey, widgetId, contextId, participantId, participantDisplayName,  participantThumbnailUrl, null);
	}

	@Override
	public boolean addParticipant(String apiKey, String widgetId,
			String contextId, String participantId,
			String participantDisplayName, String participantThumbnailUrl,
			String role) {
		IParticipant participant = new DefaultParticipantImpl(participantId, participantDisplayName,  participantThumbnailUrl, role);
		return addParticipant(apiKey, widgetId, contextId, participant);
	}

	private boolean addParticipant(String apiKey, String widgetId,
			String contextId, IParticipant participant) {
		
		//
		// A participant must have an id at least
		//
		if (participant.getParticipantId() == null || participant.getParticipantId().trim().length() == 0){
			return false;
		}
		
		//
		// Get a Jedis from the pool
		//
		Jedis jedis = pool.getResource();
		
		//
		// Get the redis key to use for this object
		//
		String key = getParticipantKey(apiKey, widgetId, contextId, participant.getParticipantId());
		
		//
		// If there is no existing tuple, add the key to the list of keys for this token
		//
		if (this.getParticipant(apiKey, widgetId, contextId, participant.getParticipantId()) == null){
			jedis.lpush(this.getContextKey(apiKey, widgetId, contextId), key);			
		}

		//
		// Convert the object into JSON for storage
		//
		String json = dehydrateParticipant(participant);
		
		//
		// Store the object
		//
		jedis.set(key, json);
		
		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);
		
		//
		// OK
		//
		return true;
	}

	@Override
	public void removeParticipant(String apiKey, String widgetId,
			String contextId, IParticipant participant) {
		if (participant != null) removeParticipant(apiKey, widgetId, contextId, participant.getParticipantId());
	}

	@Override
	public boolean removeParticipant(String apiKey, String widgetId,
			String contextId, String participantId) {
		
		//
		// Check participant id is valid
		//
		if (participantId == null) return false;
		if (participantId.trim().length() == 0) return false;
		
		//
		// Get a Jedis from the pool
		//
		Jedis jedis = pool.getResource();
		
		//
		// get the Redis key for the participant
		//
		String key = this.getParticipantKey(apiKey, widgetId, contextId, participantId);
				
		//
		// if it doesn't exist, return false
		//
		if (!jedis.exists(key)){
			pool.returnResource(jedis);
			return false;
		}
				
		//
		// Delete the object
		//
		jedis.del(key);
		
		//
		// Remove the key from the context list
		//
		String context = this.getContextKey(apiKey, widgetId, contextId);
		jedis.lrem(context, 0, key);
		
		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);
		
		//
		// OK
		//
		return true;
	}

	@Override
	public IParticipant getParticipant(String apiKey, String widgetId,
			String contextId, String participantId) {
		//
		// Get the key
		//
		String key = this.getParticipantKey(apiKey, widgetId, contextId, participantId);
		
		//
		// get a jedis connector
		//
		Jedis jedis = pool.getResource();
		
		//
		// get an object from the store and rehydrate it from json
		//
		String json = jedis.get(key);
		IParticipant participant = rehydrateParticipant(json);
		
		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);
		
		//
		// Return the object
		//
		return participant;
	}

	@Override
	public IParticipant getViewer(String apiKey, String widgetId,
			String contextId, String viewerId) {
		return this.getParticipant(apiKey, widgetId, contextId, viewerId);
	}

	@Override
	public IParticipant getHost(String apiKey, String widgetId, String contextId) {
		IParticipant[] hosts = getHosts(apiKey, widgetId, contextId);
		if (hosts.length == 0) return null;
		return hosts[0];
	}

	@Override
	public IParticipant[] getHosts(String apiKey, String widgetId,
			String contextId) {
		
		//
		// Create a hosts array
		//
		ArrayList<IParticipant> hosts = new ArrayList<IParticipant>();
		
		//
		// Iterate over participants and add them if they have a host role
		//
		for (IParticipant participant: this.getParticipants(apiKey, widgetId, contextId)){
			if (participant.getRole() != null && participant.getRole().equals(IParticipant.HOST_ROLE)){
				hosts.add(participant);
			}
		}
		
		//
		// Return the array
		//
		return hosts.toArray(new IParticipant[hosts.size()]);
	}
	
	///// Utilities

	private String getContextKey(String apiKey, String widgetId, String contextId){
		return apiKey+"-"+contextId+"-"+widgetId;
	}
	
	private String getDataKey(String apiKey, String widgetId, String contextId, String name){
		return "data::"+getContextKey(apiKey, widgetId, contextId)+"::"+name;
	}
	
	private String getParticipantKey(String apiKey, String widgetId, String contextId, String participantId){
		return "participant::"+getContextKey(apiKey, widgetId, contextId)+"::"+participantId;
	}
	
	private String dehydrateData(ISharedData data){
		JSONObject json = new JSONObject();
		json.put("name", data.getDkey());
		json.put("value", data.getDvalue());
		return json.toString();
	}

	private String dehydrateParticipant(IParticipant participant){
		JSONObject json = new JSONObject();
		json.put("id", participant.getParticipantId());
		json.put("name", participant.getParticipantDisplayName());
		json.put("thumbnail", participant.getParticipantThumbnailUrl());
		json.put("role", participant.getRole());
		return json.toString();
	}

	private ISharedData rehydrateData(String input){
		if (input == null) return null;
		DefaultSharedDataImpl data;
		JSONObject json = new JSONObject(input);
		String value = json.getString("value");
		String name = json.getString("name");
		data = new DefaultSharedDataImpl(name,value);
		return data;
	}

	private IParticipant rehydrateParticipant(String input){
		if (input == null) return null;
		JSONObject json = new JSONObject(input);
		String id = json.getString("id");
		String name = json.has("name") ? json.getString("name"): null;
		String thumbnail = json.has("thumbnail") ? json.getString("thumbnail"): null;
		String role =  json.has("role") ? json.getString("role") : null;
		DefaultParticipantImpl participant = new DefaultParticipantImpl(id, name, thumbnail, role);
		return participant;
	}
}
