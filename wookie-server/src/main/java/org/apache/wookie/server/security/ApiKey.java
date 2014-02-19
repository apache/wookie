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

/**
 * Representation of an API key, consisting of an id, a key value, and a contact email address
 */
public class ApiKey {
  
  private String value;
  private String secret;
  
  public ApiKey(){
  }
  
  public ApiKey(String key, String secret){
    setValue(key);
    setSecret(secret);
  }

  /**
   * Get the actual Key value
   * @return
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the value 
   * @param value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Get contact email associated with this key
   * @return
   */
  public String getSecret() {
    return secret;
  }

  /**
   * Set the contact email address
   * @param email
   */
  public void setSecret(String secret) {
    this.secret = secret;
  }

}
