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
package org.apache.wookie.services;

import static org.junit.Assert.*;

import org.junit.Test;

public abstract class AbstractWorkflowServiceTest {
	
	protected static WorkflowService svc;
	
	@Test
	public void lockSession(){
		svc.lock("test-api", "test-widget", "test-context");
		assertTrue(svc.isLocked("test-api", "test-widget", "test-context"));
		assertFalse(svc.isLocked("test-api", "test-widget", "test-other-context"));
		
		svc.unlock("test-api", "test-widget", "test-context");
		assertFalse(svc.isLocked("test-api", "test-widget", "test-context"));
		assertFalse(svc.isLocked("test-api", "test-widget", "test-other-context"));
	}


}
