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

package org.apache.wookie.tests.server.security;

import static org.junit.Assert.*;

import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.server.security.ApiKey;
import org.apache.wookie.server.security.ApiKeys;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class ApiKeysTest {
	
	private static ApiKey[] keys;
  
  @BeforeClass
  public static void setup() throws ResourceDuplicationException{
	keys = ApiKeys.getInstance().getKeys();
    ApiKeys.getInstance().clear();
    ApiKeys.getInstance().addKey("KEYS_TEST_1", "test@apache.org");
  }
  
  //
  // Restore state from before the tests
  //
  @AfterClass
  public static void teardown() throws ResourceDuplicationException{
	  ApiKeys.getInstance().clear();
	  for (ApiKey key:keys){
		  ApiKeys.getInstance().addKey(key.getValue(), key.getSecret());
	  }
  }
  
  @Test
  public void getInstance(){
    assertNotNull(ApiKeys.getInstance());
  }
  
  @Test
  public void getKeys(){
    assertEquals(1, ApiKeys.getInstance().getKeys().length);
    assertEquals("KEYS_TEST_1", ApiKeys.getInstance().getKeys()[0].getValue());
  }
  
  @Test
  public void addKey() throws ResourceDuplicationException, ResourceNotFoundException{
    ApiKeys.getInstance().addKey("KEYS_TEST_2", "test2@apache.org");
    assertEquals(2, ApiKeys.getInstance().getKeys().length);
    ApiKeys.getInstance().removeKey("KEYS_TEST_2");
  }
  
  @Test
  public void removeKey() throws ResourceNotFoundException, ResourceDuplicationException{
    // Add a key
    ApiKeys.getInstance().addKey("KEYS_TEST_2", "test2@apache.org");
    assertEquals(2, ApiKeys.getInstance().getKeys().length);
    // Remove it
    ApiKeys.getInstance().removeKey("KEYS_TEST_2");
    assertEquals(1, ApiKeys.getInstance().getKeys().length);
    assertEquals("KEYS_TEST_1", ApiKeys.getInstance().getKeys()[0].getValue());
  }

}
