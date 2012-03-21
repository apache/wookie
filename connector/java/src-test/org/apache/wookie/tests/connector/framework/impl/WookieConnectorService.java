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

import static org.junit.Assert.assertEquals;
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
  public void getWidgetMetadata() throws WookieConnectorException{
    Widget widget = service.getAvailableWidgets().get("http://notsupported");
    assertEquals("Unsupported widget widget", widget.getName());
    assertEquals("This widget is a placeholder for when no corresponding widget is found for a given type", widget.getDescription());
    assertEquals("500", widget.getWidth());
    assertEquals("358", widget.getHeight());
    assertEquals("Paul Sharples", widget.getAuthor());
    assertEquals("http://notsupported", widget.getIdentifier());
  }

  @Test
  public void getWidgetMetadata2() throws WookieConnectorException{
    Widget widget = service.getAvailableWidgets().get("http://wookie.apache.org/widgets/simplechat");
    assertEquals("SimpleChat", widget.getName());
    assertEquals("Stripped down chat widget with minimal styling", widget.getDescription());
    assertEquals("255", widget.getWidth());
    assertEquals("383", widget.getHeight());
    assertEquals("Apache Wookie (Incubating) Team", widget.getAuthor());
    assertEquals("http://localhost:8080/wookie/wservices/wookie.apache.org/widgets/simplechat/icon.png", widget.getIcon().toString());
    assertEquals("http://wookie.apache.org/widgets/simplechat", widget.getIdentifier());
    assertEquals("Licensed under the Apache 2.0 License (see http://www.apache.org/licenses/LICENSE-2.0). Smileys created by macpoupou and licensed under Creative Commons Attribution License 3.0. See http://ismileys.free.fr/smileys/ for more information.", widget.getLicense());
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
    assertTrue("Wrong number of users returned",users.length>2);
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
  
  @Test
  public void properties() throws WookieConnectorException, IOException{
	    HashMap<String, Widget> widgets = service.getAvailableWidgets();
	    WidgetInstance instance = service.getOrCreateInstance((Widget)widgets.values().toArray()[0]);
	    assertNotNull("Retrieved widget instance is null", instance);
	    service.setPropertyForInstance(instance, true, "test_property2", "test data");
	    String data = service.getPropertyForInstance(instance, "test_property2");
	    assertNotNull ( "Data from property is null", data );
	    service.updatePropertyForInstance(instance, true, "test_property2", "new test data");
	    data = service.getPropertyForInstance(instance, "test_property2");
	    assertTrue ("Property data did not update", data.equals("new test data"));
	    service.deletePropertyForInstance(instance, true, "test_property2");
	    data = service.getPropertyForInstance(instance, "test_property2");
	    assertTrue("The property was not deleted", (data == null));
  }
  
  
 
  
}
