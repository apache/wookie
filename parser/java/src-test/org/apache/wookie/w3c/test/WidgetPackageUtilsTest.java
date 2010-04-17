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
package org.apache.wookie.w3c.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.wookie.w3c.util.WidgetPackageUtils;
import org.junit.Test;

/**
 * 
 * Tests for generic utility methods not covered already in conformance tests etc
 *
 */
public class WidgetPackageUtilsTest {
	
	@Test
	public void testConvertPath(){
		String path = WidgetPackageUtils.convertPathToPlatform("/x/");
		assertEquals("/x", path);
	}
	
	@Test
	public void testInvalidLocalePaths(){
		String[] path;
		try {
			path = WidgetPackageUtils.locateFilePaths("locales/", new String[]{"bogus!"}, null);
			assertNull(path);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testInvalidLocalePaths2(){
		String[] path;
		try {
			path = WidgetPackageUtils.locateFilePaths("locales/izabogustag", new String[]{"bogus!"}, null);
			assertNull(path);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testInvalidLocalePaths3(){
		String[] path;
		try {
			path = WidgetPackageUtils.locateFilePaths("/locales/izabogustag", new String[]{"bogus!"}, null);
			assertNull(path);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void getLanguageTagForPath(){
		String locale = WidgetPackageUtils.languageTagForPath("locales/en");
		assertEquals("en", locale);
	}
	
	@Test
	public void getLanguageTagForPath2(){
		String locale = WidgetPackageUtils.languageTagForPath("locales/");
		assertNull(locale);
	}
}
