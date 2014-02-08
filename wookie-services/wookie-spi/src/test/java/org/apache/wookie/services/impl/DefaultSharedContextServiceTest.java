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
package org.apache.wookie.services.impl;

import static org.junit.Assert.*;

import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.services.SharedContextService;
import org.junit.Test;

public class DefaultSharedContextServiceTest {
	
	private static final String API_KEY = "TEST";
	private static final String WIDGET_ID = "http://TEST_WIDGET";
	private static final String CONTEXT_ID = "TEST_CONTEXT";
	
	
	@Test
	public void addAndRemoveParticipant(){
		SharedContextService.Factory.getInstance().addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob", "Bob", "http://test/img");
		
		IParticipant result = SharedContextService.Factory.getInstance().getParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob");
		
		assertNotNull(result);
		assertEquals("bob", result.getParticipantId());
		
		SharedContextService.Factory.getInstance().removeParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob");
		result = SharedContextService.Factory.getInstance().getParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob");
		
		assertNull(result);

	}
	
	@Test
	public void addDuplicateParticipant(){
		boolean result = SharedContextService.Factory.getInstance().addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob", "Bob", "http://test/img");		
		assertTrue(result);
		result = SharedContextService.Factory.getInstance().addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob", "Bob", "http://test/img");		
		assertFalse(result);
		SharedContextService.Factory.getInstance().removeParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob");
	}


}
