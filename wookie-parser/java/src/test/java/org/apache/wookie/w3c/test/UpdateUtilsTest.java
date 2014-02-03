/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.apache.wookie.w3c.test;

import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.exceptions.InvalidContentTypeException;
import org.apache.wookie.w3c.updates.UpdateUtils;
import org.apache.wookie.w3c.W3CWidget;
import org.junit.Test;

/**
 * Additional tests for UpdateUtils
 */
public class UpdateUtilsTest {
  
  
  //
  // Test that a request to get an update over HTTP fails when HTTPSOnly = true
  //
  @Test
  public void testHttpsOnly() throws InvalidContentTypeException, BadWidgetZipFileException, BadManifestException, IOException, Exception{
      W3CWidget updatedWidget = UpdateUtils.getUpdate(new W3CWidgetFactory(), "test", "http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", "1.0", true);
      assertNull(updatedWidget);
  }
}

