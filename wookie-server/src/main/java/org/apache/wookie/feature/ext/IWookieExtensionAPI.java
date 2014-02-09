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

package org.apache.wookie.feature.ext;

/**
 * Wookie-specific runtime API extensions, including "private" support methods
 */
public interface IWookieExtensionAPI { 
  
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
   * show a widget instance based on the id_key
   * 
   * Note this is not a part of the W3C standard interface, but a Wookie-specific extension
   * 
   * @param id_key
   * @return
   */
  public String show(String id_key);
  
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

  
}
