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
package org.apache.wookie.tests.server.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.wookie.tests.functional.AbstractControllerTest;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * Turning on the validate service in wookie means anyone can upload artifacts
 * (although they should be uploaded to the java.temp folder), so these tests check
 * that resources are deleted after the validation has occurred.
 */
public class MaliciousContentUploadTest extends AbstractControllerTest {
    
    @AfterClass
    public static void tearDown() throws HttpException, IOException{}

    @Test
    public void testNoExecutableInUploadFolder() throws HttpException, IOException{
      try {
        HttpClient client = new HttpClient();
          
          PostMethod post = new PostMethod(TEST_VALIDATOR_SERVICE_URL_VALID);
          
          //
          // Use upload test widget
          //
          File file = new File("src-tests/testdata/security-tests/binary.exe");
          assertTrue(file.exists());
          
          //
          // Add test binary file to POST
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
          // ensure no binary.exe in temp folder
          File uploadedWgt = new File(TEMPUPLOADFOLDER, "binary.exe");
          assertFalse(uploadedWgt.exists());
    } catch (ConnectException e) {
        // can't connect to server
    }
    }
    
    @Test
    public void testNoJspInUploadFolder() throws HttpException, IOException{
      try {
        HttpClient client = new HttpClient();
          
          PostMethod post = new PostMethod(TEST_VALIDATOR_SERVICE_URL_VALID);
          
          //
          // Use upload test widget
          //
          File file = new File("src-tests/testdata/security-tests/jsp.jsp");
          assertTrue(file.exists());
          
          //
          // Add test binary file to POST
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
          // ensure no jsp.jsp in temp folder
          File uploadedWgt = new File(TEMPUPLOADFOLDER, "jsp.jsp");
          assertFalse(uploadedWgt.exists());
    } catch (ConnectException e) {
        // TODO Auto-generated catch block
    }
    }
    
    @Test
    public void testNoBatchFileInUploadFolder() throws HttpException, IOException{
      try {
        HttpClient client = new HttpClient();
          
          PostMethod post = new PostMethod(TEST_VALIDATOR_SERVICE_URL_VALID);
          
          //
          // Use upload test widget
          //
          File file = new File("src-tests/testdata/security-tests/script.bat");
          assertTrue(file.exists());
          
          //
          // Add test binary file to POST
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
          // ensure no script.bat in temp folder
          File uploadedWgt = new File(TEMPUPLOADFOLDER, "script.bat");
          assertFalse(uploadedWgt.exists());
    } catch (ConnectException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    }
    
    @Test
    public void testNoScriptFileInUploadFolder() throws HttpException, IOException{
      try {
        HttpClient client = new HttpClient();
          
          PostMethod post = new PostMethod(TEST_VALIDATOR_SERVICE_URL_VALID);
          
          //
          // Use upload test widget
          //
          File file = new File("src-tests/testdata/security-tests/script.sh");
          assertTrue(file.exists());
          
          //
          // Add test binary file to POST
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
          // ensure no script.sh in temp folder
          File uploadedWgt = new File(TEMPUPLOADFOLDER, "script.sh");
          assertFalse(uploadedWgt.exists());
    } catch (ConnectException e) {
        // TODO Auto-generated catch block
    }
    }

    @Test
    public void testUploadMaliciousWidgetNoManifest() throws HttpException, IOException{
      try {
        HttpClient client = new HttpClient();
          
          PostMethod post = new PostMethod(TEST_VALIDATOR_SERVICE_URL_VALID);
          
          //
          // Use upload test widget
          //
          File file = new File("src-tests/testdata/security-tests/widget-with-malicious-code-no-manifest.wgt");
          assertTrue(file.exists());
          
          //
          // Add test binary file to POST
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
          // ensure resources are removed
          File uploadedWgt = new File(TEMPUPLOADFOLDER, "widget-with-malicious-code-no-manifest.wgt");
          assertFalse(uploadedWgt.exists());
    } catch (ConnectException e) {
        // TODO Auto-generated catch block
    }
    }
    
    @Test
    public void testUploadMaliciousWidgetWithManifest() throws HttpException, IOException{
      try {
        HttpClient client = new HttpClient();
          
          PostMethod post = new PostMethod(TEST_VALIDATOR_SERVICE_URL_VALID);
          
          //
          // Use upload test widget
          //
          File file = new File("src-tests/testdata/security-tests/widget-with-malicious-code-with-manifest.wgt");
          assertTrue(file.exists());
          
          //
          // Add test binary file to POST
          //
          Part[] parts = { new FilePart(file.getName(), file) };
          post.setRequestEntity(new MultipartRequestEntity(parts, post
              .getParams()));

          client.executeMethod(post);   
          int code = post.getStatusCode();
          // should be 200
          if(shouldValidate){
              assertEquals(200, code);
          }else{
              assertEquals(503, code);
          }
          //System.out.println(post.getResponseBodyAsString());
          post.releaseConnection();
          // ensure resources are removed
          File tempUploadFolder = new File(TEMPUPLOADFOLDER, "pretend-malicious-package");
          assertFalse(tempUploadFolder.exists());
          File uploadedWgt = new File(TEMPUPLOADFOLDER, "widget-with-malicious-code-with-manifest.wgt");
          assertFalse(uploadedWgt.exists());
    } catch (ConnectException e) {
        // TODO Auto-generated catch block
    }
    }
}
