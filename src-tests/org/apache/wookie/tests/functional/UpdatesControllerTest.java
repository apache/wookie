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
package org.apache.wookie.tests.functional;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.wookie.tests.helpers.WidgetUploader;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for Widget Updates controller.
 * 
 * NOTE:
 * 
 * These are simple test cases for the controller. Actually triggering the
 * updates is tested using the conformance tests in
 * org.apache.wookie.test.conformance.WidgetUpdates
 */
public class UpdatesControllerTest extends AbstractControllerTest {
  
  protected static final String TEST_UPDATES_URL_VALID = TEST_SERVER_LOCATION
      + "updates";

  /**
   * Install widgets for testing
   * 
   * @throws IOException
   */
  @BeforeClass
  public static void setup() throws IOException {
    storeImportedPackageId(
        WidgetUploader
        .uploadWidget("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition11/001/ta-ac-001.wgt")// ac11
    );
    storeImportedPackageId(
        WidgetUploader
        .uploadWidget("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition13/001/ta-ac-001.wgt")// ac13
    );
    storeImportedPackageId(
        WidgetUploader
        .uploadWidget("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/003/ta-pr-003.wgt")// pr203
    );
    storeImportedPackageId(
        WidgetUploader
        .uploadWidget("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/008/ta-pr-008.wgt")// pr208
    );
    storeImportedPackageId(
        WidgetUploader
        .uploadWidget("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/009/ta-pr-009.wgt")// pr209
    );
    storeImportedPackageId(
        WidgetUploader
        .uploadWidget("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/010/ta-pr-010.wgt")// pr210
    );
    storeImportedPackageId(
        WidgetUploader
        .uploadWidget("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/011/ta-pr-011.wgt")// pr211
    );
    storeImportedPackageId(
        WidgetUploader
        .uploadWidget("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/012/ta-pr-012.wgt")// pr212
    );
    storeImportedPackageId(
        WidgetUploader
        .uploadWidget("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/013/ta-pr-013.wgt")// pr213
    );
    storeImportedPackageId(
        WidgetUploader
        .uploadWidget("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/015/ta-pr-015.wgt")// pr215
    );
    storeImportedPackageId(
        WidgetUploader
        .uploadWidget("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/016/ta-pr-016.wgt")// pr216
    );
  }

  /**
   * Tests that a request for updates is refused without admin credentials
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getUpdatesUnauthorized() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    GetMethod get = new GetMethod(TEST_UPDATES_URL_VALID);
    client.executeMethod(get);
    int code = get.getStatusCode();
    assertEquals(401, code);
  }

  /**
   * Tests that a request for updates with admin credentials returns a valid
   * response
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getUpdates() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    GetMethod get = new GetMethod(TEST_UPDATES_URL_VALID);
    setAuthenticationCredentials(client);
    get.setRequestHeader("Content-type", "text/xml");
    client.executeMethod(get);
    int code = get.getStatusCode();
    assertEquals(200, code);
  }

}
