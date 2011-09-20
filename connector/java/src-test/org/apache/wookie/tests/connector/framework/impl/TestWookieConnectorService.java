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

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.wookie.connector.framework.AbstractWookieConnectorService;
import org.apache.wookie.connector.framework.User;
import org.apache.wookie.connector.framework.WookieConnectorException;
/**
 * A Wookie connector for testing purposes. It will first try to connect to
 * http;//localhost:8080/wookie and run tests against that server. If no instance
 * is running locally it will connect to http://bombax.oucs.ox.ac.uk:8888 a test
 * server hosted at the University of Oxford.
 * 
 * Please note tha the bombax server is not guaranteed to be running, therefore
 * always try and run tests against localhost.
 */
public class TestWookieConnectorService extends AbstractWookieConnectorService {

  private static TestWookieConnectorService instance;
  User testUser = new User("testuser", "Test User");
  
  private TestWookieConnectorService(String url, String apiKey,
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

  public void setCurrentUser(User user) {
    this.testUser = user;
  }

  public void setCurrentUser(String userId) {
    if (userId.equals("testuser")) {
      setCurrentUser(new User("testuser", "Test User"));
    } else {
      setCurrentUser((User)null);
    }
  }
  
  public static TestWookieConnectorService getInstance() throws WookieConnectorException, MalformedURLException, IOException {
    if (instance == null) {
      try {
        new URL("http://localhost:8080/wookie").openStream();
        instance = new TestWookieConnectorService("http://localhost:8080/wookie", "TEST", "myshareddata");
      } catch (ConnectException e) {
        // assume localhost is not running so run against bombax
        instance = new TestWookieConnectorService("http://bombax.oucs.ox.ac.uk:8888/wookie", "TEST", "myshareddata");
      }
    }
    return instance;
  }

}
