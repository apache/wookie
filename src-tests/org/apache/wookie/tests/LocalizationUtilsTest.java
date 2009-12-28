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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.apache.wookie.beans.ILocalizedElement;
import org.apache.wookie.beans.License;
import org.apache.wookie.util.LocalizationUtils;
import org.junit.Test;

public class LocalizationUtilsTest {
	
	public static final String[] LANG_LIST_1 = {null, "en", "fr", "nl"};
	public static final String[] LANG_LIST_2 = {"en", "za", "nl", null};
	public static final String[] LANG_LIST_3 = {"en", null, "nl", null};
	public static final String[] LANG_LIST_4 = {"en"};
	public static final String[] LANG_LIST_5 = {"en","fr"};
	public static final String[] LANG_LIST_6 = {"en","nl"};
	public static final String[] LANG_LIST_7 = {null, "za", "fr", "nl"};
	public static final String[] LANG_LIST_8 = {"en-us-POSIX"};
	public static final String[] LANG_LIST_9 = {null, "en", "en-gb", "en-US-POSIX"};
	public static final String[] LANG_LIST_10 = {"de"};
	public static final String[] LANG_LIST_11 = {"bu",null};
	public static final String[] LANG_LIST_INV_1 = {"x-gb-a-a"};
	public static final String[] LANG_LIST_INV_2 = {"x-gb-a-a","12345567889"};
	public static final String[] LANG_LIST_INV_3 = {"x-gb-a-a","i-argserg45ggadfgdfsg-4t534","fr"};	
	
	public ILocalizedElement[] elements(String[] langs){
		ArrayList<ILocalizedElement> licenses = new ArrayList<ILocalizedElement>();
		for (String lang:langs){
			License license = new License();
			license.setLang(lang);
			licenses.add(license);
		}
		return licenses.toArray(new License[licenses.size()]);
	}
	
	@Test
	public void nullTest1(){
		ILocalizedElement[] elements = elements(LANG_LIST_1);
		elements = LocalizationUtils.processElementsByLocales(elements,null);
		assertEquals("en", elements[0].getLang());
		assertEquals(1, elements.length);	
	}
	
	@Test
	public void nullTest2(){
		ILocalizedElement[] elements = elements(LANG_LIST_1);
		elements = LocalizationUtils.processElementsByLocales(elements,new String[]{null});
		assertEquals("en", elements[0].getLang());
		assertEquals(1, elements.length);	
	}

	@Test
	public void nullTest3(){
		ILocalizedElement[] elements = elements(LANG_LIST_1);
		elements = LocalizationUtils.processElementsByLocales(elements,new String[]{null,null,""});
		assertEquals("en", elements[0].getLang());
		assertEquals(1, elements.length);	
	}
	
	@Test
	public void nullTest4(){
		ILocalizedElement[] elements = elements(LANG_LIST_11);
		elements = LocalizationUtils.processElementsByLocales(elements,null);
		assertEquals(null, elements[0].getLang());
		assertEquals(1, elements.length);	
	}
	
	@Test
	public void nullTest5(){
		ILocalizedElement[] elements = elements(LANG_LIST_11);
		ILocalizedElement element = LocalizationUtils.getLocalizedElement(elements,null);
		assertEquals(null, element.getLang());
	}
	
	@Test
	public void nullTest6(){
		try {
			ILocalizedElement[] elements = null;
			ILocalizedElement element = LocalizationUtils.getLocalizedElement(elements,null);
			assertNull(element);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void nullTest7(){
		try {
			ILocalizedElement[] elements = null;
			elements = LocalizationUtils.processElementsByDefaultLocales(elements);
			assertNull(elements);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void nullTest8(){
		try {
			ILocalizedElement[] elements = null;
			elements = LocalizationUtils.processElementsByLocales(elements,null);
			assertNull(elements);
		} catch (Exception e) {
			fail();
		}
	}
	
	// Invalid locales are skipped - if this creates an empty list, then use defaults
	@Test
	public void invalid(){
		ILocalizedElement[] elements = elements(LANG_LIST_1);
		elements = LocalizationUtils.processElementsByLocales(elements,LANG_LIST_INV_1);
		assertEquals("en", elements[0].getLang());
		assertEquals(1, elements.length);	
	}
	@Test
	public void invalid2(){
		ILocalizedElement[] elements = elements(LANG_LIST_1);
		elements = LocalizationUtils.processElementsByLocales(elements,LANG_LIST_INV_2);
		assertEquals("en", elements[0].getLang());
		assertEquals(1, elements.length);	
	}
	@Test
	public void invalid3(){
		ILocalizedElement[] elements = elements(LANG_LIST_1);
		elements = LocalizationUtils.processElementsByLocales(elements,LANG_LIST_INV_3);
		assertEquals("fr", elements[0].getLang());
		assertEquals(1, elements.length);	
	}
	
	@Test
	public void defaultOrder1(){	
		ILocalizedElement[] elements = elements(LANG_LIST_1);
		elements = LocalizationUtils.processElementsByDefaultLocales(elements);
		assertEquals("en", elements[0].getLang());
		assertEquals(1, elements.length);
	}
	@Test
	public void defaultOrder2(){	
		ILocalizedElement[] elements = elements(LANG_LIST_2);
		elements = LocalizationUtils.processElementsByDefaultLocales(elements);
		assertEquals("en", elements[0].getLang());
		assertEquals(1, elements.length);
	}
	@Test
	public void defaultOrder3(){	
		ILocalizedElement[] elements = elements(LANG_LIST_3);
		elements = LocalizationUtils.processElementsByDefaultLocales(elements);
		assertEquals("en", elements[0].getLang());
		assertEquals(1, elements.length);
	}
	
	@Test
	public void specifiedOrder1(){
		ILocalizedElement[] elements = elements(LANG_LIST_1);
		elements = LocalizationUtils.processElementsByLocales(elements, LANG_LIST_4);
		assertEquals("en", elements[0].getLang());
		assertEquals(1, elements.length);
	}
	@Test
	public void specifiedOrder2(){	
		ILocalizedElement[] elements = elements(LANG_LIST_2);
		elements = LocalizationUtils.processElementsByLocales(elements, LANG_LIST_4);
		assertEquals("en", elements[0].getLang());
		assertEquals(1, elements.length);
	}
	@Test
	public void specifiedOrder3(){	
		ILocalizedElement[] elements = elements(LANG_LIST_3);
		elements = LocalizationUtils.processElementsByLocales(elements, LANG_LIST_4);
		assertEquals("en", elements[0].getLang());
		assertEquals(1, elements.length);
	}
	
	@Test
	public void noMatch(){
		ILocalizedElement[] elements = elements(LANG_LIST_7);
		elements = LocalizationUtils.processElementsByLocales(elements, LANG_LIST_4);
		assertEquals(null, elements[0].getLang());
		assertEquals(1, elements.length);
	}
	
	@Test
	public void noMatch3(){
		ILocalizedElement[] elements = elements(LANG_LIST_11);
		ILocalizedElement element = LocalizationUtils.getLocalizedElement(elements, LANG_LIST_4);
		assertEquals(null,element.getLang());
	}
	
	@Test
	public void noMatch2(){
		ILocalizedElement[] elements = elements(LANG_LIST_1);
		elements = LocalizationUtils.processElementsByLocales(elements, LANG_LIST_10);
		assertEquals(null, elements[0].getLang());
		assertEquals(1, elements.length);
	}
	
	@Test
	public void noMatch1(){
		ILocalizedElement[] elements = elements(LANG_LIST_2);
		elements = LocalizationUtils.processElementsByLocales(elements, LANG_LIST_4);
		assertEquals(1, elements.length);
	}
	
	
	@Test
	public void specifiedOrderM4(){	
		ILocalizedElement[] elements = elements(LANG_LIST_3);
		elements = LocalizationUtils.processElementsByLocales(elements, LANG_LIST_6);
		assertEquals("en", elements[0].getLang());
		assertEquals("nl", elements[1].getLang());
		assertEquals(2, elements.length);
	}
	@Test
	public void specifiedOrderM5(){	
		ILocalizedElement[] elements = elements(LANG_LIST_3);
		elements = LocalizationUtils.processElementsByLocales(elements, LANG_LIST_5);
		assertEquals("en", elements[0].getLang());
		assertEquals(1, elements.length);
	}	
	@Test
	public void specifiedOrderM6(){	
		ILocalizedElement[] elements = elements(LANG_LIST_1);
		elements = LocalizationUtils.processElementsByLocales(elements, LANG_LIST_6);
		assertEquals("en", elements[0].getLang());
		assertEquals("nl", elements[1].getLang());
		assertEquals(2, elements.length);
	}	
	@Test
	public void specifiedOrderM7(){	
		ILocalizedElement[] elements = elements(LANG_LIST_9);
		elements = LocalizationUtils.processElementsByLocales(elements, LANG_LIST_8);
		assertEquals("en-US-POSIX", elements[0].getLang());
		assertEquals("en", elements[1].getLang());
		assertEquals(2, elements.length);
	}
	@Test
	public void classCast(){	
		ILocalizedElement[] elements = elements(LANG_LIST_9);
		elements = LocalizationUtils.processElementsByLocales(elements, LANG_LIST_8);
		assertEquals("en-US-POSIX", elements[0].getLang());
		assertEquals("en", elements[1].getLang());
		assertTrue(elements[0] instanceof License);
	}	
	
	@Test
	public void simple(){
		String[] langs = {"en","fr","za"};
		for (String lang:langs){
		assertTrue(LocalizationUtils.isValidLanguageTag(lang));
		}
		String[] langs2 = {"waytoolong", "l33t", "26", null, "", "     "};
		for (String lang:langs2){
		assertFalse(LocalizationUtils.isValidLanguageTag(lang));
		}
	}
	@Test
	public void region(){
		String[] langs = {"en-gb","en-us", "ch-229"};
		for (String lang:langs){
		assertTrue(LocalizationUtils.isValidLanguageTag(lang));
		}
		String[] langs2 = {"en-123456789", "i-klingon"};
		for (String lang:langs2){
		assertFalse(LocalizationUtils.isValidLanguageTag(lang));
		}
	}
	@Test
	public void script(){
		String[] langs = {"za-hans", "de-Latn"};
		for (String lang:langs){
		assertTrue(LocalizationUtils.isValidLanguageTag(lang));
		}
		String[] langs2 = {"za-hansa-hans", "de-Latin-x", "en-gb-12345a678-a2cdefgh"};
		for (String lang:langs2){
		assertFalse(LocalizationUtils.isValidLanguageTag(lang));
		}
	}
	@Test
	public void variants(){
		String[] langs = {"de-Latf-DE","de-Latn-DE-1996", "de-Deva-DE"};
		for (String lang:langs){
		assertTrue(LocalizationUtils.isValidLanguageTag(lang));
		}
		String[] langs2 = {"x-x-test", "de-Latn-DE-9999999999"};
		for (String lang:langs2){
		assertFalse(LocalizationUtils.isValidLanguageTag(lang));
		}
	}
	@Test
	public void extensions(){
		String[] langs = {"de-Latf-DE-a-11","de-Latn-DE-c-ab", "de-Deva-DE-n-231", "en-gb-a-manc"};
		for (String lang:langs){
		assertTrue(LocalizationUtils.isValidLanguageTag(lang));
		}
		String[] langs2 = {"en-gb-a-a", "de-Latn-DE-x-x", "de-Latn-DE-a-123456789"};
		for (String lang:langs2){
		assertFalse(LocalizationUtils.isValidLanguageTag(lang));
		}
	}
}
