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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import org.apache.wookie.connector.framework.User;
import org.apache.wookie.connector.framework.Widget;
import org.apache.wookie.connector.framework.WidgetInstance;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.junit.BeforeClass;
import org.junit.Test;

public class WookieConnectorService {

  private static TestWookieConnectorService service;

  @BeforeClass
  public static void setup() throws Exception {
    service = TestWookieConnectorService.getInstance();
  }
  
  @Test
  public void getCurrentUser() {
    assertNotNull("Current user is null", service.getCurrentUser());
  }
  
  @Test
  public void getUser() {
    assertNotNull("Test user is null", service.getUser("testuser"));
  }
  
  @Test
  public void getAvailableWidgets() throws WookieConnectorException {
    HashMap<String, Widget> widgets = service.getAvailableWidgets();
    assertTrue("Not retrieved enough widgets", 10 < widgets.size());
    assertNotNull("Widget value is null", widgets.values().toArray()[0]);
  }
  
  @Test
  public void getOrCreateInstance() throws WookieConnectorException, IOException {
    HashMap<String, Widget> widgets = service.getAvailableWidgets();
    WidgetInstance instance = service.getOrCreateInstance((Widget)widgets.values().toArray()[0]);
    assertNotNull("Retrieved widget instance is null", instance);
  }
  
  @Test
  public void addParticipant() throws WookieConnectorException, IOException {
	HashMap<String, Widget> widgets = service.getAvailableWidgets();
	WidgetInstance instance = service.getOrCreateInstance((Widget)widgets.values().toArray()[0]);
    assertNotNull("Retrieved widget instance is null", instance);
    
	User user = new User("test1","test user 1");
    service.addParticipant(instance, user);
    User[] users = service.getUsers(instance);
    assertTrue("Wrong number of users returned",users.length==2);
    assertTrue("Wrong user returned", users[0].getLoginName().equals("testuser"));
    assertTrue("Wrong user returned", users[1].getLoginName().equals("test1"));
  }
  
  @Test
  public void addParticipantWithThumbnailUrl() throws WookieConnectorException, IOException{
    HashMap<String, Widget> widgets = service.getAvailableWidgets();
    WidgetInstance instance = service.getOrCreateInstance((Widget)widgets.values().toArray()[0]);
    assertNotNull("Retrieved widget instance is null", instance);    
    User user = new User("thumbnailtestuser","thumbnail test user","http://bar.com/icon.png");
    service.addParticipant(instance, user);
    User[] users = service.getUsers(instance);
    user = users[users.length-1];
    assertTrue("Incorrect thumbnail", user.getThumbnailUrl().equals("http://bar.com/icon.png"));
  }
}
