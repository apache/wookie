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

import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieServerConnection;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WookieServerConnectionTest {
  private static final String TEST_URL = "http://localhost:8080/wookie";
  private static final String TEST_API_KEY = "TEST";
  private static final String TEST_SHARED_DATA_KEY = "myshareddata";
  static WookieServerConnection conn;
  
  @BeforeClass
  public static void setup() throws WookieConnectorException {
    conn = new WookieServerConnection(TEST_URL, TEST_API_KEY, TEST_SHARED_DATA_KEY);  
    assertNotNull("Connection object has not been set up correctly", conn);
  }
  
  @Test
  public void testGetURL() throws WookieConnectorException {
    assertEquals("URL not set correctly", TEST_URL, conn.getURL());
  }
  
  @Test
  public void testSetURL() throws WookieConnectorException {
    conn.setURL("foo");
    assertEquals("URL not set correctly", "foo", conn.getURL());
    conn.setURL(TEST_URL);
    assertEquals("URL not set correctly", TEST_URL, conn.getURL());
  }
}
