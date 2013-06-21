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

package org.apache.wookie.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.wookie.tests.helpers.NanoHTTPD;
import org.apache.wookie.util.NewWidgetBroadcaster;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the NewWidgetBroadcaster
 */
public class NewWidgetBroadcasterTest {

	//
	// A test server
	//
	private static Server server;
	
	//
	// The test server URL
	//
	private static final String broadcastUrl = "http://localhost:9000";
	
	//
	// This is where we hold the ID of the last widget the server received a broadcast event for
	//
	private String lastUploadedWidgetId;

	@BeforeClass
	public static void setup(){
	}

	@Test
	public void testBroadcast() throws IOException {
		
		//
		// Create a custom server
		//
		server = new Server(this, 9000);
		
		//
		// Create  configuration properties for the broadcaster
		//
		Configuration properties = new BaseConfiguration();
		properties.addProperty("widget.import.broadcast", true);
		properties.addProperty("widget.import.broadcast.url", broadcastUrl);
		
		//
		// Create a new broadcast, and check the server received it
		//
		NewWidgetBroadcaster.broadcast(properties, "TEST_BROADCAST");
		assertEquals("TEST_BROADCAST", this.lastUploadedWidgetId);
	
		//
		// Create another new broadcast, and check the server received it
		//
		NewWidgetBroadcaster.broadcast(properties, "TEST_BROADCAST_TWO");
		assertEquals("TEST_BROADCAST_TWO", this.lastUploadedWidgetId);
	}
	
	@AfterClass
	public static void teardown(){
		//
		// Stop the server
		//
		server.stop();
	}

	/**
	 * A subclass of NanoHTTPD for testing
	 */
	static class Server extends NanoHTTPD {
		
		private NewWidgetBroadcasterTest parent;

		/**
		 * Create a server with a reference to the parent class, so we can set the ID of the last
		 * event we received.
		 * 
		 * @param port
		 * @param parent the parent class
		 * @throws IOException
		 */
		public Server(NewWidgetBroadcasterTest parent, int port) throws IOException {
			super(port);
			this.parent = parent;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.apache.wookie.tests.helpers.NanoHTTPD#serve(java.lang.String,
		 * java.lang.String, java.util.Properties, java.util.Properties,
		 * java.util.Properties)
		 */
		@Override
		public Response serve(String uri, String method, Properties header,
				Properties parms, Properties files) {
			Response response = new Response();
			parent.lastUploadedWidgetId = parms.getProperty("widgetId");
			response.status = "200 OK";
			return response;
		}
	}
}
