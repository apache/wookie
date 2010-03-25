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

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

/**
 * Constants used in functional tests. Change these values to test servers
 * at other locations.
 *
 */
public abstract class AbstractControllerTest{
	protected static final int TEST_SERVER_PORT = 8080;
	protected static final String TEST_SERVER_HOST = "localhost";	
	protected static final String TEST_SERVER_ORIGIN = "http://"+TEST_SERVER_HOST+":"+TEST_SERVER_PORT;
	protected static final String TEST_SERVER_LOCATION = TEST_SERVER_ORIGIN+"/wookie/";
	
	protected static final String TEST_INSTANCES_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"widgetinstances";
	protected static final String TEST_PROPERTIES_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"properties";
	protected static final String TEST_PARTICIPANTS_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"participants";
	protected static final String TEST_WIDGETS_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"widgets";
	protected static final String TEST_SERVICES_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"services";
		
	protected static final String API_KEY_VALID = "TEST";
	protected static final String API_KEY_INVALID = "rubbish";
	protected static final String WIDGET_ID_VALID = "http://www.getwookie.org/widgets/weather";
	protected static final String WIDGET_ID_INVALID = "http://www.getwookie.org/widgets/nosuchwidget";

	/**
	 * Set credentials for accessing Wookie admin functions
	 * @param client
	 */
	protected static void setAuthenticationCredentials(HttpClient client){
		Credentials defaultcreds = new UsernamePasswordCredentials("java", "java");
		client.getState().setCredentials(new AuthScope(TEST_SERVER_HOST, TEST_SERVER_PORT, AuthScope.ANY_REALM), defaultcreds);
	}
	
}
