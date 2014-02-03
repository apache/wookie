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

package org.apache.wookie.server.security;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;

/**
 * Class for managing API Keys used to gain access to widget instances
 *
 */
public class ApiKeys {
  
  static Logger logger = Logger.getLogger(ApiKeys.class.getName());  
  
  /*
   * Lock on the configuration; commons-config isn't thread safe
   * so we must use an explicit lock to avoid concurrency issues
   */
  private final Lock lock = new ReentrantLock();
  
  private static ApiKeys instance;
  
  private HashMap<String, ApiKey> keys;
  private  PropertiesConfiguration properties;
  private  FileChangedReloadingStrategy reloader = new FileChangedReloadingStrategy();
  
  /**
   * Private constructor
   */
  private ApiKeys(){
    try {
      keys = new HashMap<String, ApiKey>();
      properties = new PropertiesConfiguration("keys");
      properties.setReloadingStrategy(reloader);
      properties.setAutoSave(false);
      load();
    } catch (ConfigurationException e) {
      logger.error("Problem initializing the API Keys from configuration", e);
    }
  }
  
  /**
   * Get the ApiKeys instance
   * @return the singleton ApiKeys instance
   */
  public static ApiKeys getInstance() {
    if (instance == null) instance = new ApiKeys();
    return instance;
  }
  
  /**
   * Refresh the API keys, checking
   * if the file needs to be reloaded
   */
  private void refresh(){
    lock.lock();
    try{
      if (reloader.reloadingRequired()){
        keys.clear();
        load();
        reloader.reloadingPerformed();
      }
    } finally {
      lock.unlock();
    }
  }
  
  /**
   * Load the API keys from the key
   * configuration file
   */
  private void load(){
    lock.lock();
    try {
      properties.clear();
      properties.load();
      keys.clear();
      @SuppressWarnings("rawtypes")
      Iterator keys = properties.getKeys();
      while(keys.hasNext()){
        String key = (String)keys.next();
        String email = properties.getString(key);
        ApiKey apiKey = new ApiKey(key, email);
        this.keys.put(apiKey.getValue(), apiKey);
      }
    } catch (ConfigurationException e) {
      logger.error("Error loading API Keys from the keys file", e);
    } finally {
      lock.unlock();
    }
  }
  
  /**
   * Checks if a key exists
   * @param key
   * @return true if the key exists, otherwise false
   */
  public boolean validate(String key){
    refresh();
    if (keys.containsKey(key)){
      return true;
    }
    return false;
  }
  
  /**
   * Get an array of all the currently installed keys
   * @return an array of ApiKey objects
   */
  public ApiKey[] getKeys(){
    refresh();
    return keys.values().toArray(new ApiKey[keys.size()]);
  }
  
  /**
   * Internal method for adding a new key and saving the
   * configuration properties file.
   * @param key
   * @param email
   * @return true if the key was successfully added, otherwise false
   * @throws ConfigurationException
   */
  private boolean addKeyToCollection(String key, String email) throws ConfigurationException{
    ApiKey apiKey = new ApiKey(key, email);
    if (keys.containsKey(apiKey.getValue())){
      logger.debug("Duplicate key submitted for "+email);
      return false;
    } else {
      // Add
      keys.put(apiKey.getValue(), apiKey);
      logger.debug("Key added for "+apiKey.getEmail());
      return true;
    }
  }
  
  /**
   * Add and save a new API key
   * @param key
   * @param email
   * @throws ResourceDuplicationException if the key already exists
   */
  public void addKey(String key, String email) throws ResourceDuplicationException{
    try {
      if (addKeyToCollection(key, email)){
        lock.lock();
        try {
          properties.clearProperty(key);
          properties.addProperty(key, email);
          properties.save();
        } finally {
          lock.unlock();
        }
      } else {
        throw new ResourceDuplicationException();
      }
    } catch (ConfigurationException e) {
      logger.error("Problem with keys properties configuration", e);
    }
  }
  
  /**
   * Remove a key
   * @param key
   * @throws ResourceNotFoundException 
   */
  public void removeKey(String key) throws ResourceNotFoundException{
    if (keys.containsKey(key)){
      keys.remove(key);
      lock.lock();
      try{
        properties.clearProperty(key);
        properties.save();
      } catch (ConfigurationException e) {
        logger.error("Problem with keys properties configuration", e);
      } finally {
        lock.unlock();
      }
    } else {
      throw new ResourceNotFoundException();
    }
  }
  
  /**
   * Clear all keys
   */
  public void clear(){
    keys.clear();
    lock.lock();
    try{
      properties.clear();
    } finally {
      lock.unlock();
    }
  }

}
