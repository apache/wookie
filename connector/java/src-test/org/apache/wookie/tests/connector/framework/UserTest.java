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
package org.apache.wookie.tests.connector.framework;


import static org.junit.Assert.assertEquals;

import org.apache.wookie.connector.framework.User;
import org.junit.BeforeClass;
import org.junit.Test;


public class UserTest {
  private static final String SCREEN_NAME = "screen name";
  private static final String LOGIN_NAME = "login name";
  static User user;
  
  @BeforeClass
  public static void setUp() {
    user = new User(LOGIN_NAME, SCREEN_NAME);
  }
  
  @Test
  public void testGetLoginName() {
    assertEquals("Login name not correctly set", LOGIN_NAME, user.getLoginName());
  }
 
  @Test
  public void testGetScreenName() {
    assertEquals("Login name not correctly set", SCREEN_NAME, user.getScreenName());
  }
  
  @Test
  public void testSetLoginName() {
    user.setLoginName("foo");
    assertEquals("Login name not correctly set", "foo", user.getLoginName());

    user.setLoginName(LOGIN_NAME);
    assertEquals("Login name not correctly set", LOGIN_NAME, user.getLoginName());
  }
 
  @Test
  public void testSetScreenName() {
    user.setScreenName("foo");
    assertEquals("Login name not correctly set", "foo", user.getScreenName());

    user.setScreenName(SCREEN_NAME);
    assertEquals("Login name not correctly set", SCREEN_NAME, user.getScreenName());
  }
  
  @Test
  public void testSetThumbnailUrl(){
    user.setThumbnailUrl(null);
    assertEquals("Thumbnail URL not correctly set", "", user.getThumbnailUrl());
    
    user.setThumbnailUrl("http://foo.com/test.jpg");
    assertEquals("Thumbnail URL not correctly set", "http://foo.com/test.jpg", user.getThumbnailUrl());
    
    user.setThumbnailUrl(null);
    assertEquals("Thumbnail URL not correctly set", "", user.getThumbnailUrl());
  }
  
}
