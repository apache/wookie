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

package org.apache.wookie.tests.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.wookie.proxy.Policy;
import org.apache.wookie.proxy.PolicyFormatException;
import org.junit.Test;

/**
 * Tests Policy
 */
public class PolicyTest {
  
  
  @Test
  public void createPolicy() throws PolicyFormatException{
    Policy policy = new Policy();
    policy.setScope("*");
    policy.setOrigin("*");
    policy.setDirective("ALLOW");
  }
  
  @Test
  public void createPolicyFromString() throws PolicyFormatException{
    Policy policy = new Policy("* http://localhost ALLOW");
  }
  
  @Test(expected = PolicyFormatException.class)
  public void createPolicyFromInvalidString() throws PolicyFormatException{
    Policy policy = new Policy("http://localhost ALLOW");
  }
  
  @Test(expected = PolicyFormatException.class)
  public void createPolicyFromInvalidString2() throws PolicyFormatException{
    Policy policy = new Policy("* * BANANA");
  }
  
  @Test(expected = PolicyFormatException.class)
  public void createPolicyFromInvalidString3() throws PolicyFormatException{
    Policy policy = new Policy("BANANA * ALLOW");
  }
  
  @Test
  public void policyToString() throws PolicyFormatException{
    Policy policy = new Policy("* http://localhost ALLOW");
    assertEquals("* http://localhost ALLOW",policy.toString());
  }
  
  @Test(expected = PolicyFormatException.class)
  public void createPolicyFromInvalidUriString() throws PolicyFormatException{
    Policy policy = new Policy("* isnotauri! ALLOW");
  }
  
  @Test(expected = PolicyFormatException.class)
  public void createPolicyFromInvalidUriString2() throws PolicyFormatException{
    Policy policy = new Policy("* nohost: ALLOW");
  }

  @Test(expected = PolicyFormatException.class)
  public void createPolicyFromInvalidUriString3() throws PolicyFormatException{
    Policy policy = new Policy("* test:test@http://x.y.z ALLOW");
  }

  @Test(expected = PolicyFormatException.class)
  public void createPolicyFromInvalidUriString4() throws PolicyFormatException{
    Policy policy = new Policy("* http://test@www.apache.org ALLOW");
  }
 
  @Test
  public void checkInvalid() throws URISyntaxException{
    Policy policy = new Policy();
    assertEquals(0,policy.allows(new URI("http://test.apache.org")));  
  }

  @Test
  public void checkInvalid2() throws URISyntaxException, PolicyFormatException{
    Policy policy = new Policy("* http://test.apache.org ALLOW");
    assertEquals(0,policy.allows(new URI("ftp://test.apache.org")));  
  }
  
  @Test
  public void equalsTest() throws PolicyFormatException{
    Policy policy = new Policy("* http://test.apache.org ALLOW");
    Policy policy2 = new Policy("* http://test.apache.org ALLOW");
    assertEquals(policy,policy2);
    assertTrue(policy.equals(policy2));
    
  }

  @Test
  public void unequalTest() throws PolicyFormatException{
    Policy policy = new Policy("* http://test.apache.org ALLOW");
    Object policy2 = new String("* http://test.apache.org ALLOW");
    assertFalse(policy.equals(policy2));
  }
}

