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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.wookie.beans.IPreference;
import org.apache.wookie.services.impl.DefaultPreferenceImpl;
import org.junit.After;
import org.junit.Test;

public abstract class AbstractPreferencesServiceTest {
	
	protected static PreferencesService svc;
	
	@After
	public void cleanUp(){
		svc.removePreferences("token");
	}
	
	@Test
	public void getNull(){
		String result = svc.getPreference("token", "test-null");
		assertEquals(null, result);
	}
	
	@Test
	public void setAndGet(){		
		svc.setPreference("token", "test", "test");
		String result = svc.getPreference("token", "test");
		assertEquals("test", result);
	}
	
	@Test
	public void setAndUpdate(){		
		svc.setPreference("token", "test", "test");
		String result = svc.getPreference("token", "test");
		assertEquals("test", result);
		svc.setPreference("token", "test", "test-updated");
		result = svc.getPreference("token", "test");
		assertEquals("test-updated", result);
	}
	
	@Test
	public void setAndGetByString(){	
		svc.setPreference("token", "test", "test-value", true);
		Collection<IPreference> prefs = svc.getPreferences("token");
		assertEquals(1, prefs.size());
		IPreference pref = prefs.iterator().next();
		
		assertEquals("test", pref.getName());
		assertEquals("test-value", pref.getValue());
		assertEquals(true, pref.isReadOnly());
	}
	
	@Test
	public void setAndGetByObject(){	
		IPreference pref = new DefaultPreferenceImpl("test", "test-value", true);
		svc.setPreference("token", pref);
		Collection<IPreference> prefs = svc.getPreferences("token");
		assertEquals(1, prefs.size());
		pref = prefs.iterator().next();
		
		assertEquals("test", pref.getName());
		assertEquals("test-value", pref.getValue());
		assertEquals(true, pref.isReadOnly());
	}
	
	@Test
	public void setAndGetByObjectNull(){	
		svc.setPreference("token", null);
		Collection<IPreference> prefs = svc.getPreferences("token");
		assertEquals(0, prefs.size());
	}
	
	@Test
	public void setCollection(){
		ArrayList<IPreference> preferences = new ArrayList<IPreference>();
		IPreference preference1 = new DefaultPreferenceImpl("test-name1", "test-value1", true);
		IPreference preference2 = new DefaultPreferenceImpl("test-name2", "test-value2", false);
		preferences.add(preference1);
		preferences.add(preference2);
		
		svc.setPreferences("token", preferences);
		
		Collection<IPreference> returned = svc.getPreferences("token");
		assertEquals(2, returned.size());
	}
	
	@Test
	public void resetCollection(){
		ArrayList<IPreference> preferences = new ArrayList<IPreference>();
		IPreference preference1 = new DefaultPreferenceImpl("test-name1", "test-value1", true);
		IPreference preference2 = new DefaultPreferenceImpl("test-name2", "test-value2", false);
		preferences.add(preference1);
		preferences.add(preference2);
		
		svc.setPreferences("token", preferences);
		
		Collection<IPreference> returned = svc.getPreferences("token");
		assertEquals(2, returned.size());
		
		preferences.remove(preference2);
		svc.setPreferences("token", preferences);
		returned = svc.getPreferences("token");
		assertEquals(1, returned.size());
	}
	
	@Test
	public void setCollectionNull(){
		
		svc.setPreferences("token", null);
		
		Collection<IPreference> returned = svc.getPreferences("token");
		assertEquals(0, returned.size());
	}
	
	@Test
	public void resetCollectionNull(){
		ArrayList<IPreference> preferences = new ArrayList<IPreference>();
		IPreference preference1 = new DefaultPreferenceImpl("test-name1", "test-value1", true);
		IPreference preference2 = new DefaultPreferenceImpl("test-name2", "test-value2", false);
		preferences.add(preference1);
		preferences.add(preference2);
		
		svc.setPreferences("token", preferences);
		
		Collection<IPreference> returned = svc.getPreferences("token");
		assertEquals(2, returned.size());
		
		svc.setPreferences("token", null);
		returned = svc.getPreferences("token");
		assertEquals(0, returned.size());
	}
	
	
	@Test
	public void setAndGetObject(){		
		IPreference preference = new DefaultPreferenceImpl("test-name", "test-value", false);
		
		svc.setPreference("token", preference);
		
		String result = svc.getPreference("token", "test-name");
		
		assertEquals("test-value", result);
	}
	
	@Test
	public void setAndGetObjects(){
		
		IPreference preference1 = new DefaultPreferenceImpl("test-name", "test-value", false);
		IPreference preference2 = new DefaultPreferenceImpl("test-name2", "test-value2", false);

		svc.setPreference("token", preference1);
		svc.setPreference("token", preference2);
		
		String result = svc.getPreference("token", "test-name");
		assertEquals("test-value", result);
		
		result = svc.getPreference("token", "test-name2");
		assertEquals("test-value2", result);		
	}
	
	@Test
	public void getNothing(){
						
		String result = svc.getPreference("token", "test-name3");
		
		assertEquals(null, result);
	}
	
	@Test
	public void setAndReset(){

		svc.setPreference("token", "test-reset", "1");
		svc.setPreference("token", "test-reset", "2");
		assertEquals("2", svc.getPreference("token", "test-reset"));
		assertEquals("2", svc.getPreference("token", "test-reset"));
		svc.setPreference("token", "test-reset", "1");
		assertEquals("1", svc.getPreference("token", "test-reset"));
		assertEquals("1", svc.getPreference("token", "test-reset"));
	}


}
