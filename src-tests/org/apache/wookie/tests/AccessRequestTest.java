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
package org.apache.wookie.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.apache.wookie.beans.IAccessRequest;
import org.apache.wookie.beans.jpa.impl.AccessRequestImpl;
import org.junit.Test;

public class AccessRequestTest {

	@Test
	public void wildcardTest() throws Exception{
		IAccessRequest ar = new AccessRequestImpl();
		ar.setOrigin("*");
		assertTrue(ar.isAllowed(new URI("http://incubator.apache.org")));
	}
	
	@Test
	public void basicTest() throws Exception{
		IAccessRequest ar = new AccessRequestImpl();
		ar.setOrigin("http://incubator.apache.org");
		assertTrue(ar.isAllowed(new URI("http://incubator.apache.org")));
	}
	
	@Test
	public void pathsTest() throws Exception{
		IAccessRequest ar = new AccessRequestImpl();
		ar.setOrigin("http://incubator.apache.org");
		assertTrue(ar.isAllowed(new URI("http://incubator.apache.org/test")));
	}
	
	@Test
	public void schemesTest() throws Exception{
		IAccessRequest ar = new AccessRequestImpl();
		ar.setOrigin("http://incubator.apache.org");
		assertFalse(ar.isAllowed(new URI("https://incubator.apache.org/test")));
	}
	
	@Test
	public void subDomainsTest() throws Exception{
		IAccessRequest ar = new AccessRequestImpl();
		ar.setOrigin("http://apache.org");
		ar.setSubdomains(true);
		assertTrue(ar.isAllowed(new URI("http://incubator.apache.org")));
		assertTrue(ar.isAllowed(new URI("http://www.apache.org")));
		assertFalse(ar.isAllowed(new URI("http://apache.org.com")));
	}
	
	@Test
	public void noSubDomainsTest() throws Exception{
		IAccessRequest ar = new AccessRequestImpl();
		ar.setOrigin("http://apache.org");
		ar.setSubdomains(false);
		assertTrue(ar.isAllowed(new URI("http://apache.org")));
		assertFalse(ar.isAllowed(new URI("http://incubator.apache.org")));
		assertFalse(ar.isAllowed(new URI("http://www.apache.org")));
	}
}
