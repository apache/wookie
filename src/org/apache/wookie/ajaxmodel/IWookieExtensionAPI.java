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

package org.apache.wookie.ajaxmodel;

/**
 * Wookie-specific runtime API extensions, including "private" support methods
 */
public interface IWookieExtensionAPI {
  
  
  /**
   * Appends a string to the string contained in the shared data value in the DB, obtained
   * from the given "key" 
   * 
   * Note that this method is retained only as a workaround for some issues in the Wave Gadget API implementation
   * and is likely to be deprecated in the future.
   * 
   * @param id_key - the unique instance id key for a widget instance
   * @param key - key for the value to change
   * @param key - the value to change to
   * @return - a string value marking status or an error message
   */
  public String appendSharedDataForKey(String id_key, String key, String value);  
  
  /**
   * Call to hide a widget instance based on the instance_key
   * 
   * Note this is not a part of the W3C standard interface, but a Wookie-specific extension
   * 
   * @param id_key
   * @return
   */
  public String hide(String id_key);
  
  /**
   * Open a url in a window. DEPRECATED.
   * @param url
   * @return
   */
  @Deprecated
  public String openURL(String url);
  
  /**
   * Returns a string preference value from the DB, obtained
   * from the given "key" . DEPRECATED: Use widget.preferences
   * @param id_key - the unique instance id key for a widget instance
   * @param key - key for the value to retrieve
   * @return - a string value found in the DB or an error message
   */
   @Deprecated
  public String preferenceForKey(String id_key, String key);
  
  /**
   * Sets a string preference value in the DB, obtained
   * from the given "key" 
   * @param id_key - the unique instance id key for a widget instance
   * @param key - key for the value to change
   * @param key - the value to change to
   * @return - a string value marking status or an error message
   */
  public String setPreferenceForKey(String id_key, String key, String value);
    
  /**
   * Sets a string shared data value in the DB, obtained
   * from the given "key". DEPRECATED: Use wave.getState().submitValue()
   * @param id_key - the unique instance id key for a widget instance
   * @param key - key for the value to change
   * @param key - the value to change to
   * @return - a string value marking status or an error message
   */
  @Deprecated
  public String setSharedDataForKey(String id_key, String key, String value);
  
  /**
   * Returns a string value from the DB, obtained
   * from the given "key". This is a shared data value
   * between widgets using the same data. DEPRECATED: Use wave.getState().get()
   * @param id_key - the unique instance id key for a widget instance
   * @param key - key for the value to retrieve
   * @return - a string value found in the DB or an error message
   */
   @Deprecated
  public String sharedDataForKey(String id_key, String key);  
  
  /**
   * show a widget instance based on the id_key
   * 
   * Note this is not a part of the W3C standard interface, but a Wookie-specific extension
   * 
   * @param id_key
   * @return
   */
  public String show(String id_key);
  
}
