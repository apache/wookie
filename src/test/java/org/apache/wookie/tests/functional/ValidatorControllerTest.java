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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ValidatorControllerTest extends AbstractControllerTest {

  @BeforeClass 
  public static void setup() throws ConfigurationException{
      initValidatorValue();
  }
    
  @AfterClass
  public static void tearDown() throws HttpException, IOException{}

  @Test
  public void validateOkWidget() throws HttpException, IOException{
    try {
        HttpClient client = new HttpClient();
        
        PostMethod post = new PostMethod(TEST_VALIDATOR_SERVICE_URL_VALID);
        
        //
        // Use upload test widget
        //
        File file = new File("src-tests/testdata/upload-test-2.wgt");
        assertTrue(file.exists());
        
        //
        // Add test wgt file to POST
        //
        Part[] parts = { new FilePart(file.getName(), file) };
        post.setRequestEntity(new MultipartRequestEntity(parts, post
            .getParams()));

        client.executeMethod(post);   
        int code = post.getStatusCode();
        if(shouldValidate){
            assertEquals(200, code);
        }else{
            assertEquals(503, code);
        }
        //System.out.println(post.getResponseBodyAsString());
        post.releaseConnection();
        // ensure resources have been removed
        File tempUploadFolder = new File(TEMPUPLOADFOLDER, "uploadtest_2");
        assertFalse(tempUploadFolder.exists());
        File uploadedWgt = new File(TEMPUPLOADFOLDER, "upload-test-2.wgt");
        assertFalse(uploadedWgt.exists());
    } catch (ConnectException e) {
        // TODO Auto-generated catch block
    }
  }
  
  @Test
  public void validateWidgetMissingStartPage() throws HttpException, IOException{
    try {
        HttpClient client = new HttpClient();
        
        PostMethod post = new PostMethod(TEST_VALIDATOR_SERVICE_URL_VALID);
        
        File file = new File("src-tests/testdata/missing-start-page.wgt");
        assertTrue(file.exists());
        
        //
        // Add test wgt file to POST
        //
        Part[] parts = { new FilePart(file.getName(), file) };
        post.setRequestEntity(new MultipartRequestEntity(parts, post
            .getParams()));
        
        client.executeMethod(post);   
        int code = post.getStatusCode();
        // should be 400 - bad wgt package
        if(shouldValidate){
            assertEquals(400, code);
        }else{
            assertEquals(503, code);
        }
        //System.out.println(post.getResponseBodyAsString());
        post.releaseConnection();
        // ensure resources have been removed
        File tempUploadFolder = new File(TEMPUPLOADFOLDER, "invalid-widget-no-start-page");
        assertFalse(tempUploadFolder.exists());
        File uploadedWgt = new File(TEMPUPLOADFOLDER, "missing-start-page.wgt");
        assertFalse(uploadedWgt.exists());
    } catch (Exception e) {
        // TODO Auto-generated catch block
    }
  }
  
  @Test
  public void validateWidgetMissingConfigDotXml() throws HttpException, IOException{
    try {
        HttpClient client = new HttpClient();
        
        PostMethod post = new PostMethod(TEST_VALIDATOR_SERVICE_URL_VALID);
        
        File file = new File("src-tests/testdata/missing-config.wgt");
        assertTrue(file.exists());
        
        //
        // Add test wgt file to POST
        //
        Part[] parts = { new FilePart(file.getName(), file) };
        post.setRequestEntity(new MultipartRequestEntity(parts, post
            .getParams()));
        
        client.executeMethod(post);   
        int code = post.getStatusCode();
        // should be 400 - bad wgt package
        if(shouldValidate){
            assertEquals(400, code);
        }else{
            assertEquals(503, code);
        }
        //System.out.println(post.getResponseBodyAsString());
        post.releaseConnection();
        // ensure resources have been removed
        // no unpacked folder should have been created this time
        // remove original wgt upload
        File uploadedWgt = new File(TEMPUPLOADFOLDER, "missing-config.wgt");
        assertFalse(uploadedWgt.exists());
    } catch (ConnectException e) {
        // TODO Auto-generated catch block
    }
  }
  
  @Test
  public void validateWidgetInvalidXmlMarkup() throws HttpException, IOException{
    try {
        HttpClient client = new HttpClient();
        
        PostMethod post = new PostMethod(TEST_VALIDATOR_SERVICE_URL_VALID);
        
        File file = new File("src-tests/testdata/invalid-xml.wgt");
        assertTrue(file.exists());
        
        //
        // Add test wgt file to POST
        //
        Part[] parts = { new FilePart(file.getName(), file) };
        post.setRequestEntity(new MultipartRequestEntity(parts, post
            .getParams()));
        
        client.executeMethod(post);   
        int code = post.getStatusCode();
        // should be 400 - bad wgt package
        if(shouldValidate){
            assertEquals(400, code);
        }else{
            assertEquals(503, code);
        }
        //System.out.println(post.getResponseBodyAsString());
        post.releaseConnection();
        // ensure resources have been removed
        // no unpacked folder should have been created this time
        // remove original wgt upload
        File uploadedWgt = new File(TEMPUPLOADFOLDER, "invalid-xml.wgt");
        assertFalse(uploadedWgt.exists());
    } catch (ConnectException e) {
        // TODO Auto-generated catch block
    }
  }
}