/*
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
package org.apache.wookie.server.security;

import java.util.LinkedList;

/**
 * A simple cache for nonces so we can guard against
 * replay attacks reusing nonces.
 */
public class NonceCache {
	
	/**
	 * The singleton instance
	 */
	private static NonceCache instance;
	
	/**
	 * The cache, a simple linked list
	 */
	private LinkedList<String> cache;
	
	/**
	 * The number of nonces to hold in the cache
	 */
	private static final int CACHE_SIZE = 100;
	
	/**
	 * Private constructor for singleton
	 */
	private NonceCache(){
		cache = new LinkedList<String>();
	}
	
	/**
	 * Get single instance
	 * @return the NonceCache
	 */
	public static NonceCache getInstance(){
		if (instance == null) instance = new NonceCache();
		return instance;
	}
	
	/**
	 * Checks whether a nonce is valid - whether it
	 * has been recently used.
	 * @param nonce
	 * @return true if the nonce is valid, false if it is recycled
	 */
	public boolean isValid(String nonce){
		
		//
		// Has it been used?
		//
		if (cache.contains(nonce)) return false;
		
		//
		// If not, add it to the end of the cache
		//
		cache.addLast(nonce);
		
		//
		// If the cache has grown to its max size, pop
		// off the first nonce
		//
		if (cache.size() > CACHE_SIZE) cache.removeFirst();
		return true;
	}
	
	

}
