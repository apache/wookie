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
package org.apache.wookie.auth;

import java.util.LinkedList;

/**
 * 
 * A cache of already-used single use tokens.
 * Single use tokens have a short lifespan, so the cache
 * doesn't have to be huge.
 *
 */
public class ExpiredSingleUseTokenCache {
	
	/**
	 * The singleton instance
	 */
	private static ExpiredSingleUseTokenCache instance;
	
	/**
	 * The cache, a simple linked list
	 */
	private LinkedList<String> cache;
	
	/**
	 * Private constructor
	 */
	private ExpiredSingleUseTokenCache(){
		cache = new LinkedList<String>();
	}
	
	/**
	 * The number of tokens to hold in the cache
	 */
	private static final int CACHE_SIZE = 500;
	
	/**
	 * Get the single instance
	 * @return the instance
	 */
	public static ExpiredSingleUseTokenCache getInstance(){
		if (instance == null){
			instance = new ExpiredSingleUseTokenCache();
		}
		return instance;
	}
	
	/**
	 * Add a used token to the cache
	 * @param token
	 */
	public void addToken(String token){
		//
		// add it to the end of the cache
		//
		cache.addLast(token);
		
		//
		// If the cache has grown to its max size, pop
		// off the first nonce
		//
		if (cache.size() > CACHE_SIZE) cache.removeFirst();
	}
	
	/**
	 * Checks whether the token has been used.
	 * @param token the token to test
	 * @return true if the token is still valid, false if it has been used already
	 */
	public boolean isValid(String token){
		if (cache.contains(token)) return false;
		return true;
	}

}
