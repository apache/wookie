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

import org.apache.wookie.util.IRIValidator;
import org.junit.Test;


public class IRIValidatorTest {
	
	@Test
	public void url(){
		assertTrue(IRIValidator.isValidIRI("http://incubator.apache.org"));
	}
	@Test
	public void nulltest(){
		assertFalse(IRIValidator.isValidIRI(null));
	}
	@Test
	public void empty(){
		assertFalse(IRIValidator.isValidIRI(""));
	}
	@Test
	public void schemeOnly(){
		assertTrue(IRIValidator.isValidIRI("pass:"));
	}
	@Test
	public void pathonly(){
		assertFalse(IRIValidator.isValidIRI("FAIL"));
	}
	@Test
	public void utf8(){
		assertTrue(IRIValidator.isValidIRI("http://אב.גד.הו/זח/טי/כל?מן=סע;פץ=קר#שת"));
	}
}
