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

import static org.junit.Assert.*;

import org.apache.wookie.Messages;
import org.apache.wookie.beans.IStartFile;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.jpa.impl.StartFileImpl;
import org.apache.wookie.beans.jpa.impl.WidgetImpl;
import org.apache.wookie.beans.jpa.impl.WidgetInstanceImpl;
import org.apache.wookie.util.opensocial.OpenSocialUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class OpenSocialUtilsTest{
	
	private static IWidgetInstance INSTANCE;
	private static IWidgetInstance INSTANCE_NO_WIDGET;
	private static IWidgetInstance INSTANCE_NO_IDKEY;
	private static IWidgetInstance INSTANCE_NO_USER_ID;
	private static Messages BUNDLE;
	
	@BeforeClass 
	public static void setUp(){
		IWidget widget = new WidgetImpl();
		widget.setGuid("http://getwookie.org/test");
		IStartFile startFile = new StartFileImpl();
        startFile.setUrl("http://getwookie.org/test/index.html");
        startFile.setLang("en");
		widget.getStartFiles().add(startFile);
		
		INSTANCE = new WidgetInstanceImpl();
		INSTANCE.setWidget(widget);
		INSTANCE.setIdKey("xhKEoiff/4ltxSuuBmPjjxBx5hw.eq.");
		INSTANCE.setUserId("scott");

		INSTANCE_NO_WIDGET = new WidgetInstanceImpl();
		INSTANCE_NO_WIDGET.setIdKey("xhKEoiff/4ltxSuuBmPjjxBx5hw.eq.");
		INSTANCE_NO_WIDGET.setUserId("scott");
		
		INSTANCE_NO_IDKEY = new WidgetInstanceImpl();
		INSTANCE_NO_IDKEY.setWidget(widget);
		INSTANCE_NO_IDKEY.setUserId("scott");
		
		INSTANCE_NO_USER_ID = new WidgetInstanceImpl();
        INSTANCE_NO_USER_ID.setWidget(widget);
		INSTANCE_NO_USER_ID.setIdKey("xhKEoiff/4ltxSuuBmPjjxBx5hw.eq.");
	}

	@Test
	public void testCreatePlainToken() {
		try {
			String token = OpenSocialUtils.createPlainToken(INSTANCE, BUNDLE);
			assertNotNull(token);
			assertFalse(token.equals(""));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Couldn't create plaintext token");
		}
	}
	
    @Test(expected=Exception.class)
    public void testCreatePlainTokenNoWidget() throws Exception{
			@SuppressWarnings("unused")
			String token = OpenSocialUtils.createPlainToken(INSTANCE_NO_WIDGET, BUNDLE);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
    }
    
    /**
     * If no id is supplied, the service should create a token using @anon
     * @throws Exception
     */
    @Test()
    public void testCreatePlainTokenNoUserId()
            throws Exception {
        try {
			String token = OpenSocialUtils.createPlainToken(INSTANCE_NO_USER_ID, BUNDLE);
			assertNotNull(token);
			assertTrue(token.contains("anon"));
        } catch (Exception expected) {
        	fail("should've created a token using @anon");
        }
    }
    
    @Test(expected=Exception.class)
    public void testCreatePlainTokenNoId()
            throws Exception {
			@SuppressWarnings("unused")
			String token = OpenSocialUtils.createPlainToken(INSTANCE_NO_IDKEY, BUNDLE);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
    }

	@Test
	public void testCreateEncryptedToken() {
		try {
			String token = OpenSocialUtils.createEncryptedToken(INSTANCE,"UNSECURED_TOKEN_KEY", BUNDLE);
			assertNotNull(token);
			assertFalse(token.equals(""));
		} catch (Exception e) {
			fail("Couldn't create encrypted token");
		}
	}
	
    @Test(expected=Exception.class)
    public void testCreateEncryptedTokenNoWidget() throws Exception{
			@SuppressWarnings("unused")
			String token = OpenSocialUtils.createEncryptedToken(INSTANCE_NO_WIDGET,"UNSECURED_TOKEN_KEY", BUNDLE);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
    }
    
    @Test()
    public void testCreateEncryptedTokenNoUserId()
            throws Exception {
        try {
			String token = OpenSocialUtils.createEncryptedToken(INSTANCE_NO_USER_ID,"UNSECURED_TOKEN_KEY", BUNDLE);
            assertNotNull(token);
        } catch (Exception expected) {
            fail("should've created a token using @anon");
        }
    }
    
    @Test(expected=Exception.class)
    public void testCreateEncryptedTokenNoId()
            throws Exception {
			@SuppressWarnings("unused")
			String token = OpenSocialUtils.createEncryptedToken(INSTANCE_NO_IDKEY,"UNSECURED_TOKEN_KEY", BUNDLE);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
    }

}
