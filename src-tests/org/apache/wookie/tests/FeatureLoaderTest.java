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
import static org.junit.Assert.fail;
import org.junit.Test;

import org.apache.wookie.beans.ServerFeature;
import org.apache.wookie.feature.FeatureLoader;


public class FeatureLoaderTest {
	
	@Test(expected=Exception.class)
	public void nullTest() throws Exception{
		@SuppressWarnings("unused")
		ServerFeature sf = FeatureLoader.createFeature(null, null);
        // Uh-oh! No exception was thrown so we 
        // better make this test fail!
        fail("should've thrown an exception!");
	}
	
	@Test(expected=Exception.class)
	public void nullTest2() throws Exception{
		@SuppressWarnings("unused")
		ServerFeature sf = FeatureLoader.createFeature(null, "fail");
        // Uh-oh! No exception was thrown so we 
        // better make this test fail!
        fail("should've thrown an exception!");
	}
	
	@Test(expected=Exception.class)
	public void nullTest3() throws Exception{
		@SuppressWarnings("unused")
		ServerFeature sf = FeatureLoader.createFeature("fail:", null);
        // Uh-oh! No exception was thrown so we 
        // better make this test fail!
        fail("should've thrown an exception!");
	}
	
	@Test(expected=Exception.class)
	public void badClass() throws Exception{
		@SuppressWarnings("unused")
		ServerFeature sf = FeatureLoader.createFeature("fail:", "org.apache.wookie.NoSuchClass");
        // Uh-oh! No exception was thrown so we 
        // better make this test fail!
        fail("should've thrown an exception!");
	}
	
	@Test(expected=Exception.class)
	public void badClass2() throws Exception{
		@SuppressWarnings("unused")
		ServerFeature sf = FeatureLoader.createFeature("fail:", "");
        // Uh-oh! No exception was thrown so we 
        // better make this test fail!
        fail("should've thrown an exception!");
	}
	
	@Test(expected=Exception.class)
	public void badClass3() throws Exception{
		@SuppressWarnings("unused")
		ServerFeature sf = FeatureLoader.createFeature("fail:", "org.apache.wookie.beans.Name");
        // Uh-oh! No exception was thrown so we 
        // better make this test fail!
        fail("should've thrown an exception!");
	}
	
	@Test(expected=Exception.class)
	public void badName() throws Exception{
		@SuppressWarnings("unused")
		ServerFeature sf = FeatureLoader.createFeature("FAIL", "org.apache.wookie.feature.conformance.Test");
        // Uh-oh! No exception was thrown so we 
        // better make this test fail!
        fail("should've thrown an exception!");
	}
	
	@Test(expected=Exception.class)
	public void badName2() throws Exception{
		@SuppressWarnings("unused")
		ServerFeature sf = FeatureLoader.createFeature("", "org.apache.wookie.feature.conformance.Test");
        // Uh-oh! No exception was thrown so we 
        // better make this test fail!
        fail("should've thrown an exception!");
	}	
	
	@Test(expected=Exception.class)
	public void mismatch() throws Exception{
		@SuppressWarnings("unused")
		ServerFeature sf = FeatureLoader.createFeature("fail:", "org.apache.wookie.feature.conformance.Test");
        // Uh-oh! No exception was thrown so we 
        // better make this test fail!
        fail("should've thrown an exception!");
	}

	@Test
	public void valid(){
		try {
			ServerFeature sf = FeatureLoader.createFeature("feature:a9bb79c1", "org.apache.wookie.feature.conformance.Test");
			assertEquals(sf.getFeatureName(), "feature:a9bb79c1");
			assertEquals(sf.getClassName(), "org.apache.wookie.feature.conformance.Test");
		} catch (Exception e) {
			fail("Couldn't create valid feature");
		}
	}

}
