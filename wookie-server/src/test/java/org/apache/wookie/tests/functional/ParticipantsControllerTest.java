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

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.wookie.tests.helpers.Request;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for the Participants REST API
 */
public class ParticipantsControllerTest extends AbstractControllerTest {

	private static String instance_id_key;

	/**
	 * Create a widget instance to test
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		Request post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "participantstest");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(200, code);
		String response = post.getResponseBodyAsString();
		instance_id_key = response.substring(response.indexOf("<identifier>") + 12,
				response.indexOf("</identifier>"));
	}

	@AfterClass
	public static void tearDown() throws HttpException, IOException{
		Request delete = new Request("DELETE", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		delete.addParameter("api_key", API_KEY_VALID);
		delete.addParameter("widgetid", WIDGET_ID_VALID);
		delete.addParameter("userid", "test");
		delete.addParameter("shareddatakey", "participantstest");
		delete.addParameter("participant_id", "80");
		delete.execute(true, false);
		int code = delete.getStatusCode();
		assertEquals(200, code);
	}

	/**
	 * Tests adding and then getting a participant
	 * 
	 * @throws HttpException
	 * @throws IOException
	 */
	@Test
	public void addParticipant() throws HttpException, IOException {

		//
		// Create a new participant
		//
		
		Request post = new Request("POST", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "participantstest");
		post.addParameter("participant_id", "1");
		post.addParameter("participant_display_name", "bob");
		post.addParameter("participant_thumbnail_url", "http://www.test.org");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(201, code);
		

		//
		// Now lets GET it to make sure it was added OK
		//
		Request get = new Request("GET", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("widgetid", WIDGET_ID_VALID);
		get.addParameter("userid", "test");
		get.addParameter("shareddatakey", "participantstest");
		get.execute(true, false);
		code = get.getStatusCode();
		assertEquals(200, code);
		String response = get.getResponseBodyAsString();
		assertTrue(response
				.contains("<participant id=\"1\" display_name=\"bob\" thumbnail_url=\"http://www.test.org\" />"));
	}

	/**
	 * Tests getting a participant using the widget instance ID key
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void getParticipant_usingIdKey() throws HttpException, IOException {
		Request get = new Request("GET", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("idkey", instance_id_key);
		get.execute(true, false);
		int code = get.getStatusCode();
		assertEquals(200, code);
		String response = get.getResponseBodyAsString();
		assertTrue(response
				.contains("<participant id=\"1\" display_name=\"bob\" thumbnail_url=\"http://www.test.org\" />"));
	}

	/**
	 * Tests trying to add a duplicate participant
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void addParticipant_AlreadyExists() throws HttpException, IOException {
		Request post = new Request("POST", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "participantstest");
		post.addParameter("participant_id", "1");
		post.addParameter("participant_display_name", "bob");
		post.addParameter("participant_thumbnail_url", "http://www.test.org");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(200, code);
	}

	/**
	 * Try to add a participant with an invalid API key
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void addParticipant_InvalidAPIkey() throws HttpException, IOException {
		Request post = new Request("POST", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_INVALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "participantstest");
		post.addParameter("participant_id", "1");
		post.addParameter("participant_display_name", "bob");
		post.addParameter("participant_thumbnail_url", "http://www.test.org");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(403, code);
	}

	/**
	 * Try to add a participant with no participant ID
	 * 
	 * @throws HttpException
	 * @throws IOException
	 */
	@Test
	public void addParticipant_InvalidParticipant() throws HttpException,
	IOException {
		Request post = new Request("POST", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "participantstest");
		post.addParameter("participant_id", "");
		post.addParameter("participant_display_name", "bob");
		post.addParameter("participant_thumbnail_url", "http://www.test.org");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(400, code);
	}

	/**
	 * Try to add a participant using a non-existing widget
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void addParticipant_InvalidWidget() throws HttpException, IOException {
		Request post = new Request("POST", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_INVALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "participantstest");
		post.addParameter("participant_id", "1");
		post.addParameter("participant_display_name", "bob");
		post.addParameter("participant_thumbnail_url", "http://www.test.org");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(400, code);
	}

	/**
	 * Delete a participant and check it was deleted
	 * 
	 * @throws HttpException
	 * @throws IOException
	 */
	@Test
	public void deleteParticipant() throws HttpException, IOException {
		//
		// Delete the participant
		//
		Request delete = new Request("DELETE", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		delete.addParameter("api_key", API_KEY_VALID);
		delete.addParameter("widgetid", WIDGET_ID_VALID);
		delete.addParameter("userid", "test");
		delete.addParameter("shareddatakey", "participantstest");
		delete.addParameter("participant_id", "1");
		delete.execute(true, false);
		int code = delete.getStatusCode();
		assertEquals(200, code);

		//
		// Now lets GET it to make sure it was deleted
		//
		Request get = new Request("GET", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("widgetid", WIDGET_ID_VALID);
		get.addParameter("userid", "test");
		get.addParameter("shareddatakey", "participantstest");
		get.execute(true, false);
		code = get.getStatusCode();
		assertEquals(200, code);
		String response = get.getResponseBodyAsString();
		assertFalse(response
				.contains("<participant id=\"1\" display_name=\"bob\" thumbnail_url=\"http://www.test.org\" />"));
	}

	/**
	 * Try to delete a participant with an invalid API key
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void deleteParticipant_InvalidAPIKey() throws HttpException,
	IOException {
		Request delete = new Request("DELETE", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		delete.addParameter("api_key", API_KEY_INVALID);
		delete.addParameter("widgetid", WIDGET_ID_VALID);
		delete.addParameter("userid", "test");
		delete.addParameter("shareddatakey", "participantstest");
		delete.addParameter("participant_id", "99");
		delete.execute(true, false);
		int code = delete.getStatusCode();
		assertEquals(403, code);
	}

	/**
	 * Try to delete a non-existing participant
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void deleteParticipant_InvalidParticipant() throws HttpException,
	IOException {
		Request delete = new Request("DELETE", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		delete.addParameter("api_key", API_KEY_VALID);
		delete.addParameter("widgetid", WIDGET_ID_VALID);
		delete.addParameter("userid", "test");
		delete.addParameter("shareddatakey", "participantstest");
		delete.addParameter("participant_id", "99");
		delete.execute(true, false);
		int code = delete.getStatusCode();
		assertEquals(404, code);
	}

	/**
	 * Try to delete a participant for a non-existing widget instance
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void deleteParticipant_InvalidInstance() throws HttpException,
	IOException {
		Request delete = new Request("DELETE", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		delete.addParameter("api_key", API_KEY_VALID);
		delete.addParameter("widgetid", WIDGET_ID_VALID);
		delete.addParameter("userid", "test");
		delete.addParameter("shareddatakey", "participantstestinvalidkey");
		delete.addParameter("participant_id", "1");
		delete.execute(true, false);
		int code = delete.getStatusCode();
		assertEquals(404, code);
	}

	/**
	 * Tests adding and then getting a participant who is also the Host
	 * 
	 * @throws HttpException
	 * @throws IOException
	 */
	@Test
	public void addHost() throws HttpException, IOException {

		//
		// Create a new participant
		//
		Request post = new Request("POST", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "participantstest");
		post.addParameter("participant_id", "80");
		post.addParameter("participant_display_name", "bob");
		post.addParameter("participant_role", "host");
		post.addParameter("participant_thumbnail_url", "http://www.test.org");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(201, code);

		//
		// Now lets GET it to make sure it was added OK
		//
		Request get = new Request("GET", TEST_PARTICIPANTS_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("widgetid", WIDGET_ID_VALID);
		get.addParameter("userid", "test");
		get.addParameter("shareddatakey", "participantstest");
		get.execute(true, false);
		code = get.getStatusCode();
		assertEquals(200, code);
		String response = get.getResponseBodyAsString();
		assertTrue(response
				.contains("<participant id=\"80\" display_name=\"bob\" thumbnail_url=\"http://www.test.org\" role=\"host\" />"));
	}

}
