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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.wookie.tests.AbstractWookieTest;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.junit.AfterClass;

/**
 * Constants used in functional tests. Change these values to test servers
 * at other locations.
 *
 */
public abstract class AbstractControllerTest extends AbstractWookieTest{
    protected static final int TEST_SERVER_PORT = 8080;
    protected static final String TEST_SERVER_HOST = "localhost";	
    protected static final String TEST_SERVER_ORIGIN = "http://" + TEST_SERVER_HOST + ":" + TEST_SERVER_PORT;
    protected static final String TEST_SERVER_LOCATION = TEST_SERVER_ORIGIN+"/wookie/";

    protected static final String TEST_POLICIES_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"policies";
    protected static final String TEST_INSTANCES_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"widgetinstances";
    protected static final String TEST_PROPERTIES_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"properties";
    protected static final String TEST_PARTICIPANTS_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"participants";
    protected static final String TEST_WIDGETS_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"widgets";
    protected static final String TEST_SERVICES_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"services";
    protected static final String TEST_VALIDATOR_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"validate";

    protected static final String API_KEY_VALID = "TEST";
    protected static final String API_KEY_INVALID = "rubbish";
    protected static final String WIDGET_ID_VALID = "http://www.getwookie.org/widgets/weather";
    protected static final String WIDGET_ID_INVALID = "http://www.getwookie.org/widgets/nosuchwidget";
    protected static final String WIDGET_ID_LOCALIZED = "http://www.getwookie.org/widgets/localetest";

    protected static Collection<String> importedWidgetList = new ArrayList<String>();
    
    protected String TEMPUPLOADFOLDER = System.getProperty("java.io.tmpdir");

	/**
	 * Set credentials for accessing Wookie admin functions
	 * @param client
	 */
    protected static void setAuthenticationCredentials(HttpClient client){
        Credentials defaultcreds = new UsernamePasswordCredentials("java", "java");
        client.getState().setCredentials(new AuthScope(TEST_SERVER_HOST, TEST_SERVER_PORT, AuthScope.ANY_REALM), defaultcreds);
    }

    protected static boolean shouldValidate = false;

    public static void initValidatorValue() throws ConfigurationException{
        File file = new File("src/widgetserver.properties");
        assertTrue(file.exists());
        Configuration properties = new PropertiesConfiguration("src/widgetserver.properties");
        shouldValidate = properties.getBoolean("widget.enable.validator");
    }  

	
  @AfterClass
  public static void tearDown() throws HttpException, IOException{
    for (String id : importedWidgetList){
      removeWidget(id);
    }
    importedWidgetList.clear();
  }
  
  private static void removeWidget(String identifier) throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    DeleteMethod delete = new DeleteMethod(TEST_WIDGETS_SERVICE_URL_VALID + "/" + identifier);
    client.executeMethod(delete);
    delete.releaseConnection();
  }
  
  protected static String storeImportedPackageId(String response){
    if(response != null){
      String result = getId(response);
      importedWidgetList.add(result);
      return result;
    }
    return null;
  }
  
  private static String getId(String response) {
    String id = null;
    SAXBuilder builder = new SAXBuilder();
    Reader in = new StringReader(response);
    Document doc;
    try {
      doc = builder.build(in);      
      Element widget = doc.getRootElement();
      Namespace ns  = widget.getNamespace(); 
      id = widget.getAttributeValue("id");
      if(id==null){
        id = widget.getAttributeValue("id", ns);
      }  
    } catch (Exception e) {
      e.printStackTrace();      
    }
    return id;
  }
	
}
