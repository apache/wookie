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
import java.util.Collection;
import java.util.List;

import org.apache.wookie.beans.IPreference;
import org.apache.wookie.services.PreferencesService;
import org.apache.wookie.services.impl.DefaultPreferenceImpl;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * An implementation of a PreferencesService using Redis as the backend
 */
public class RedisPreferencesService implements PreferencesService {
	
	/**
	 * The thread pool for jedis
	 */
	private JedisPool pool;
	
	public RedisPreferencesService(){
		pool = new JedisPool(new JedisPoolConfig(), "localhost");
	}

	private IPreference getPreferenceObject(String apiKey, String widgetId, String contextId, String viewerId, String name){
		//
		// Get a Jedis from the pool
		//
		Jedis jedis = pool.getResource();
		
		//
		// Get the context token
		//
		String token = getToken(apiKey, widgetId, contextId, viewerId);
		
		//
		// Get the jedis key for this pref object
		//
		String key = getKey(token, name);
		
		//
		// Get the value from redis
		//
		String result = jedis.get(key);
		
		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);
		
		//
		// Convert the value back into an IPreference and return it
		//
		return rehydrate(result);		
	}
	
	@Override
	public void setPreference(String apiKey, String widgetId, String contextId, String viewerId, IPreference preference) {
		
		//
		// Check if the preference is null - if so, do nothing
		// TODO throw an exception, or log a warning
		//
		if (preference == null) return;
		
		//
		// Get a Jedis from the pool
		//
		Jedis jedis = pool.getResource();
		
		//
		// Get the context token
		//
		String token = getToken(apiKey, widgetId, contextId, viewerId);
		
		//
		// Get the redis key to use for this pref
		//
		String key = getKey(token, preference.getName());
		
		//
		// If there is no existing tuple, add the key to the list of keys for this token
		//
		if (!jedis.exists(key)){
			jedis.lpush(token, key);			
		}

		//
		// Convert the IPreference into JSON for storage
		//
		String preferenceJson = dehydrate(preference);
		
		//
		// Store the preference
		//
		jedis.set(key, preferenceJson);
		
		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);
	}
	
	//////
	
	@Override
	public String getPreference(String apiKey, String widgetId, String contextId, String viewerId, String name) {
		IPreference preference = getPreferenceObject(apiKey, widgetId, contextId, viewerId, name);
		if (preference == null) return null;
		return preference.getValue();
	}

	@Override
	public void setPreference(String apiKey, String widgetId, String contextId, String viewerId,  String name, String value) {
		DefaultPreferenceImpl pref = new DefaultPreferenceImpl(name, value, false);
		setPreference(apiKey, widgetId, contextId, viewerId, pref);
	}

	@Override
	public void setPreference(String apiKey, String widgetId, String contextId, String viewerId, String name, String value,
			boolean readOnly) {
		DefaultPreferenceImpl pref = new DefaultPreferenceImpl(name, value, readOnly);
		setPreference(apiKey, widgetId, contextId, viewerId, pref);
	}

	//////


	@Override
	public Collection<IPreference> getPreferences(String apiKey, String widgetId, String contextId, String viewerId) {
		
		//
		// Get the context token
		//
		String token = getToken(apiKey, widgetId, contextId, viewerId);
		
		//
		// Get a Jedis from the pool
		//
		Jedis jedis = pool.getResource();
		
		//
		// Get the list of keys for the token
		//
		List<String> keys = jedis.lrange(token, 0, 999);
		ArrayList<IPreference> preferences = new ArrayList<IPreference>();
		for (String key: keys){
			
			//
			// Get the IPreference for each key on the list and 
			// add it to the arraylist
			//
			String json = jedis.get(key);
			IPreference pref = rehydrate(json);
			preferences.add(pref);
		}
		
		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);
			
		//
		// Return the arraylist
		//
		return preferences;
	}

	@Override
	public void setPreferences(String apiKey, String widgetId, String contextId, String viewerId, Collection<IPreference> preferences) {
		
		//
		// Clear the token
		//
		removePreferences(apiKey, widgetId, contextId, viewerId);
		
		//
		// Get a Jedis from the pool
		//
		Jedis jedis = pool.getResource();
		
		//
		// Add the preferences to the list
		//
		if (preferences != null){
			for (IPreference pref: preferences){
				setPreference(apiKey, widgetId, contextId, viewerId, pref);
			}
		}
		
		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);
	}

	@Override
	public void removePreferences(String apiKey, String widgetId, String contextId, String viewerId) {
		//
		// Get a Jedis from the pool
		//
		Jedis jedis = pool.getResource();
		
		//
		// Get the context token
		//
		String token = getToken(apiKey, widgetId, contextId, viewerId);
		
		//
		// Remove everything linked from the preferences list
		//
		List<String> keys = jedis.lrange(token, 0, 999);
		for (String key: keys){
			jedis.del(key);
		}
		
		//
		// Remove the existing preference list
		//
		jedis.del(token);
		
		//
		// Release Jedis back to the pool
		//
		pool.returnResource(jedis);
	}
	
	////// Utilities
	
	/**
	 * Convert a JSON string into an IPreference instance
	 */
	private IPreference rehydrate(String input){
		if (input == null) return null;
		JSONObject json = new JSONObject(input);
		DefaultPreferenceImpl pref = new DefaultPreferenceImpl(json.getString("name"), json.getString("value"), json.getBoolean("readOnly"));
		return pref;
	}
	
	/**
	 * Convert an IPreference instance into a JSON String
	 */
	private String dehydrate(IPreference pref){
		JSONObject json = new JSONObject();
		json.put("name", pref.getName());
		json.put("value", pref.getValue());
		json.put("readOnly", pref.isReadOnly());
		return json.toString();
	}
	
	/**
	 * Gets the redis key to use for a combination of token and pref name
	 * @param token the token
	 * @param name the preference name
	 * @return the redis key to use for getting and setting the pref
	 */
	private String getKey(String token, String name){
		
		return "pref::" + token + "::" + name;
	}
	
	private String getToken(String apiKey, String widgetId, String contextId, String viewerId){
		return apiKey+"-"+contextId+"-"+widgetId+"-"+viewerId;
	}

}
