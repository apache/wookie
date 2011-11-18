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

import java.net.URISyntaxException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.wookie.proxy.Policies;
import org.apache.wookie.proxy.PolicyFormatException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests Policies
 */
public class PoliciesTest {
  
  @BeforeClass
  public static void setUp() throws ConfigurationException, PolicyFormatException{
    //
    // Clear existing
    //
    Policies.getInstance().clear();
    assertEquals(0, Policies.getInstance().getPolicies().length);

    //
    // Add a couple of other policies to test with
    //
    Policies.getInstance().addPolicy("http://incubator.apache.org/wookie/widgets/test2 http://allowed.com DENY");
    Policies.getInstance().addPolicy("http://incubator.apache.org/wookie/widgets/test http://*.apache.org ALLOW");
    Policies.getInstance().addPolicy("http://incubator.apache.org/wookie/widgets/test2 http://test.com:9000 ALLOW");
    Policies.getInstance().addPolicy("http://incubator.apache.org/wookie/widgets/test2 http://test2.com DENY");
    Policies.getInstance().addPolicy("http://incubator.apache.org/wookie/widgets/test2 http://*.test3.com DENY");
    Policies.getInstance().addPolicy("* http://allowed.com ALLOW");
    Policies.getInstance().addPolicy("* http://notallowed.com DENY");
    Policies.getInstance().addPolicy("http://www.getwookie.org/widgets/weather http://newsrss.bbc.co.uk:80 ALLOW");
    
    assertEquals(8, Policies.getInstance().getPolicies().length);
  }

  @Test
  public void validUrl() throws URISyntaxException, ConfigurationException{
    assertTrue(Policies.getInstance().validate("http://abc.apache.org", "http://incubator.apache.org/wookie/widgets/test"));
  }
  
  @Test
  public void invalidUrl() throws URISyntaxException, ConfigurationException{
    assertFalse(Policies.getInstance().validate("http://abc.invalid.org", "http://incubator.apache.org/wookie/widgets/test"));
  }
  
  @Test
  public void invalidPort() throws URISyntaxException, ConfigurationException{
    assertFalse(Policies.getInstance().validate("http://test.com:9001", "http://incubator.apache.org/wookie/widgets/test2"));
  }
  
  @Test
  public void validPort() throws URISyntaxException, ConfigurationException{
    assertTrue(Policies.getInstance().validate("http://test.com:9000", "http://incubator.apache.org/wookie/widgets/test2"));
  }
  
  @Test
  public void globals() throws URISyntaxException, ConfigurationException{
    assertTrue(Policies.getInstance().validate("http://allowed.com", "http://incubator.apache.org/wookie/widgets/test"));
  }
  
  @Test
  public void globals2() throws URISyntaxException, ConfigurationException{
    assertFalse(Policies.getInstance().validate("http://notallowed.com", "http://incubator.apache.org/wookie/widgets/test"));
  }
  
  @Test
  public void override() throws URISyntaxException, ConfigurationException{
    assertFalse(Policies.getInstance().validate("http://allowed.com", "http://incubator.apache.org/wookie/widgets/test2"));
  }
  
  @Test
  public void validSubdomain() throws URISyntaxException, ConfigurationException{
    assertTrue(Policies.getInstance().validate("http://zzz.apache.org", "http://incubator.apache.org/wookie/widgets/test"));
  }
  
  @Test
  public void invalidSubdomain() throws URISyntaxException, ConfigurationException{
    assertFalse(Policies.getInstance().validate("http://zzz.allowed.com", "http://incubator.apache.org/wookie/widgets/test"));
  }

  
  @Test
  public void invalidSubdomain2() throws URISyntaxException, ConfigurationException{
    assertFalse(Policies.getInstance().validate("http://abc.test3.com", "http://incubator.apache.org/wookie/widgets/test2"));
  }
  
  @Test(expected = URISyntaxException.class)
  public void badURI() throws URISyntaxException, ConfigurationException{
      Policies.getInstance().validate("zzzzz", "http://incubator.apache.org/wookie/widgets/test");
  }
  @Test(expected = URISyntaxException.class)
  public void badURI2() throws URISyntaxException, ConfigurationException{
      Policies.getInstance().validate("test:", "http://incubator.apache.org/wookie/widgets/test");
  }
  @Test(expected = URISyntaxException.class)
  public void badURI3() throws URISyntaxException, ConfigurationException{
      Policies.getInstance().validate("not:really:valid", "http://incubator.apache.org/wookie/widgets/test");
  }
  @Test(expected = URISyntaxException.class)
  public void badURI4() throws URISyntaxException, ConfigurationException{
      Policies.getInstance().validate("/test", "http://incubator.apache.org/wookie/widgets/test");
  }
  
  @Test
  public void addAndDeletePolicy() throws ConfigurationException, URISyntaxException, PolicyFormatException{
    //
    // Add a new policy and test
    //
    Policies.getInstance().addPolicy("* http://temp.org ALLOW");
    assertTrue(Policies.getInstance().validate("http://temp.org", "http://incubator.apache.org/wookie/widgets/test"));
    //
    // Delete the policy and test again
    //
    Policies.getInstance().removePolicy("* http://temp.org ALLOW");
    assertFalse(Policies.getInstance().validate("http://temp.org", "http://incubator.apache.org/wookie/widgets/test"));      
  }
  
  @Test
  public void addWildcardPolicy() throws ConfigurationException, URISyntaxException, PolicyFormatException{
    Policies.getInstance().addPolicy("http://incubator.apache.org/wookie/widgets/test3 * ALLOW");
    assertTrue(Policies.getInstance().validate("http://xxx.yyy.zzz", "http://incubator.apache.org/wookie/widgets/test3"));
    
  }
  
  @Test
  public void clearPolicies() throws URISyntaxException, ConfigurationException, PolicyFormatException{
    assertTrue(Policies.getInstance().validate("http://zzz.apache.org", "http://incubator.apache.org/wookie/widgets/test"));
    Policies.getInstance().clearPolicies("http://incubator.apache.org/wookie/widgets/test");
    assertFalse(Policies.getInstance().validate("http://zzz.apache.org", "http://incubator.apache.org/wookie/widgets/test"));
    Policies.getInstance().addPolicy("http://incubator.apache.org/wookie/widgets/test http://*.apache.org ALLOW");
  }
  
  @Test
  public void weather() throws ConfigurationException, URISyntaxException{
    assertTrue(Policies.getInstance().validate("http://newsrss.bbc.co.uk/weather/forecast/9/Next3DaysRSS.xml", "http://www.getwookie.org/widgets/weather"));
  }
  
  @Test
  public void duplicates() throws ConfigurationException, PolicyFormatException{
    Policies.getInstance().addPolicy("http://temp.org http://temp.org ALLOW");
    Policies.getInstance().addPolicy("http://temp.org http://temp.org ALLOW");
    assertEquals(1, Policies.getInstance().getPolicies("http://temp.org").length);
    Policies.getInstance().clearPolicies("http://temp.org");
  }
 
}
