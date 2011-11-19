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

package org.apache.wookie.w3c.test;

import static org.junit.Assert.assertEquals;

import org.apache.wookie.w3c.impl.AccessEntity;
import org.jdom.Element;
import org.junit.Test;

/**
 * Access Tests: tests that the AccessEntity class operates as expected according to the processing rules in the W3C WARP spec
 */
public class AccessTest {
  
  @Test
  public void noOrigin(){
    Element element = new Element("access");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals(null,accessEntity.getOrigin());
  }

  @Test
  public void okOrigin(){
    Element element = new Element("access");
    element.setAttribute("origin","http://www.apache.org");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals("http://www.apache.org:80",accessEntity.getOrigin());
  }
  
  @Test
  public void okOriginHttps(){
    Element element = new Element("access");
    element.setAttribute("origin","https://www.apache.org");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals("https://www.apache.org:443",accessEntity.getOrigin());
  }
  
  @Test
  public void wildcardOrigin(){
    Element element = new Element("access");
    element.setAttribute("origin","*");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals("*",accessEntity.getOrigin());
  }
  
  @Test
  public void badOriginNoScheme(){
    Element element = new Element("access");
    element.setAttribute("origin","www.apache.org");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals(null,accessEntity.getOrigin());
  }
  
  @Test
  public void okOriginWithSubdomains(){
    Element element = new Element("access");
    element.setAttribute("origin","http://apache.org");
    element.setAttribute("subdomains","true");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals("http://apache.org:80",accessEntity.getOrigin());
    assertEquals(true,accessEntity.hasSubDomains());
  }
  
  @Test
  public void okOriginWithBadSubdomains(){
    Element element = new Element("access");
    element.setAttribute("origin","http://apache.org");
    element.setAttribute("subdomains","blah");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals(null,accessEntity.getOrigin());
    assertEquals(false,accessEntity.hasSubDomains());
  }
  
  @Test
  public void badOriginNoHost(){
    Element element = new Element("access");
    element.setAttribute("origin","http://");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals(null,accessEntity.getOrigin());
  }
  
  @Test
  public void badOriginHasUserinfo(){
    Element element = new Element("access");
    element.setAttribute("origin","http://test:test@www.apache.org");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals(null,accessEntity.getOrigin());
  }
  
  @Test
  public void badOriginHasPath(){
    Element element = new Element("access");
    element.setAttribute("origin","http://www.apache.org/incubator");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals(null,accessEntity.getOrigin());
  }
  
  @Test
  public void badOriginHasFragment(){
    Element element = new Element("access");
    element.setAttribute("origin","http://www.apache.org#1");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals(null,accessEntity.getOrigin());
  }
  
  @Test
  public void badOriginHasQuery(){
    Element element = new Element("access");
    element.setAttribute("origin","http://www.apache.org?q=test");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals(null,accessEntity.getOrigin());
  }
  
  @Test
  public void badOriginHasUnsupportedScheme(){
    Element element = new Element("access");
    element.setAttribute("origin","ftp://www.apache.org");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    assertEquals(null,accessEntity.getOrigin());
  }
  
  @Test
  public void xmlOutput(){
    Element element = new Element("access");
    element.setAttribute("origin","http://apache.org");
    element.setAttribute("subdomains","true");
    AccessEntity accessEntity = new AccessEntity();
    accessEntity.fromXML(element);
    Element outputElement = accessEntity.toXml();
    assertEquals("http://apache.org:80",outputElement.getAttributeValue("origin"));
  }
}
