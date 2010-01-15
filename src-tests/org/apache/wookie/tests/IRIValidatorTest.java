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
