package org.apache.wookie.services.impl;

import org.apache.wookie.services.AbstractWorkflowServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DefaultWorkflowServiceTest extends AbstractWorkflowServiceTest {
	
	@BeforeClass
	public static void setup(){
		svc = new DefaultWorkflowService();
	}
	
	@AfterClass
	public static void tearDown(){
	}

}
