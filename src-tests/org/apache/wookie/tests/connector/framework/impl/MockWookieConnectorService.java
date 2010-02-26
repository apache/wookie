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
package org.apache.wookie.tests.connector.framework.impl;

import org.apache.wookie.connector.framework.AbstractWookieConnectorService;
import org.apache.wookie.connector.framework.User;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieServerConnection;
/**
 * A mock class for testing purposes.
 * 
 * @FIXME this is not really a mock class it connects, via the network to a live
 * instance of Wookie. This is clearly bad for testing, we need to make this a real
 * Mock class or at least run a local version of Wookie to test against.
 */
public class MockWookieConnectorService extends AbstractWookieConnectorService {

  private static MockWookieConnectorService instance;
  User testUser = new User("test", "test_user");
  
  public MockWookieConnectorService(String url, String apiKey,
      String sharedDataKey) throws WookieConnectorException {
    super(url, apiKey, sharedDataKey);
  }

  public User getCurrentUser() {
    return testUser;
  }

  public User getUser(String login) {
    if (login.equals(testUser.getLoginName())) {
      return testUser;
    }
    return null;
  }
  
  public static MockWookieConnectorService getInstance() throws WookieConnectorException {
    if (instance == null) {
      instance = new MockWookieConnectorService("http://bombax.oucs.ox.ac.uk:8888/wookie", "TEST", "myshareddata");
      
    }
    return instance;
  }

}
