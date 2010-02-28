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
package org.apache.wookie.connector.framework;

/**
 * An implementation of the WookieConnectorService for use by Wookie itself.
 * 
 */
public class WookieConnectorService extends AbstractWookieConnectorService {
  User currentUser;
  
  public WookieConnectorService(String url, String apiKey, String sharedDataKey) throws WookieConnectorException {
    setConnection(new WookieServerConnection(url, apiKey, sharedDataKey));
  }
  
  public User getCurrentUser() {
    if (currentUser == null) {
      currentUser = getTestUser();
    }
    return currentUser;
  }

  private User getTestUser() {
    return new User("testuser", "Test User");
  }
  
  public User getUser(String login) {
    if (login.toLowerCase().equals("testuser")) {
      return getCurrentUser();
    }
    return null;
  }
  

}
