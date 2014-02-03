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

package org.apache.wookie.util.gadgets;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.apache.wookie.w3c.W3CWidget;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utilities for working with a Shindig Gadget Server, particularly for parsing gadget metadata
 * 
 * @author scott
 *
 */
public class GadgetUtils {

	static Logger _logger = Logger.getLogger(GadgetUtils.class.getName());

	/**
	 * Relative URL of the Shindig  service
	 */
	public static final String SHINDIG_SERVICE = "/";

	/**
	 * Relative URL of the Shindig metadata service
	 */
	public static final String METADATA_SERVICE = "/gadgets/metadata";

	/**
	 * The default Container ID
	 */
	private static final String CONTAINER_ID = "default";

	/**
	 * Create a new W3CWidget from the request supplied
	 * @param request the registration request, with a url parameter for the desired widget
	 * @return the widget
	 * @throws MalformedURLException
	 */
	public static W3CWidget createWidget(HttpServletRequest request) throws Exception{
		String svc = GadgetUtils.METADATA_SERVICE;
		String shindig = GadgetUtils.SHINDIG_SERVICE;
		try {
			Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties");
			if (properties.getString("widget.metadata.url")!=null) svc = properties.getString("widget.metadata.url");
			if (properties.getString("widget.shindig.url")!=null) shindig = properties.getString("widget.shindig.url");
		} catch (Exception e) {
			// Problem with the servlet context; we'll just let it go for now and use the defaults
			_logger.warn("problem configuring the gadget metadata service from widgetserver.properties - using defaults");
		}
		URL gadgetService = new URL(request.getScheme() ,
				request.getServerName() ,
				request.getServerPort() , svc);
		return createWidget(gadgetService.toString(), request.getParameter("url"), shindig);
	}

	/**
	 * Create a new widget based on the gadget url supplied, and the metadata service url supplied
	 * @param service the metadata service
	 * @param url the gadget url
	 * @return the W3CWidget instance
	 * @throws Exception
	 */
	private static W3CWidget createWidget(String service, String url, String shindig) throws Exception{
		return getWidget(getMetadata(service,url), shindig);
	}

	/**
	 * Returns metadata from a metadata service for the requested gadget url.
	 * If the request successfully completes, but no valid gadget is found, then
	 * no exception is thrown - the error information will instead be returned in the JSON string.
	 * @param service
	 * @param url
	 * @return Gadget metadata as a JSON-formatted String; this may include either gadgets or errors depending on the outcome of the request
	 * @throws Exception if the service does not exist, the service responds with a HTTP error, or the widget URL is malformed
	 */
	public static String getMetadata(String service,String url) throws Exception{

		try {
			new URL(url);
		} catch (MalformedURLException m) {
			throw new Exception("Gadget URL was malformed");
		}

		try {
			String request = "{\"context\":{\"country\":\"US\",\"language\":\"en\",\"view\":\"home\",\"container\":\""+CONTAINER_ID+"\"},\"gadgets\":[{\"url\":\""+url+"\",\"moduleId\":1}]}";
			StringRequestEntity req = null;
			req = new StringRequestEntity(request, "text/x-json","UTF-8");
			PostMethod post = new PostMethod(service);
			post.setRequestEntity(req);
			post.setRequestHeader("Content-Length", String.valueOf(req.getContentLength()));
			return executeMethod(post);
		} catch (Exception e) {
			throw new Exception("There was a problem connecting to the Shindig metadata service");
		}

	}

	/**
	 * Call a remote service
	 * @param method the method to invoke
	 * @return the response from the remote service
	 * @throws Exception
	 */
	private static String executeMethod(HttpMethod method) throws Exception {
		// Execute the method.
		try {		
			HttpClient client = new HttpClient();

			// Add user language to http request in order to notify server of user's language
			Locale locale = Locale.getDefault();

			method.setRequestHeader("Accept-Language", locale.getLanguage()); //$NON-NLS-1$ 
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {		
				// for now we are only expecting Strings					
				return method.getResponseBodyAsString();
			}																	
			else {
				throw new Exception("Method failed: " + method.getStatusLine() + ' ' + method.getURI() + ' ' + method.getStatusText() + method.getResponseBodyAsString()); //$NON-NLS-1$
			}
		} 
		catch (IOException e) {
			throw new Exception("ERROR_CONNECT", e);
		} 
		finally {
			// Release the connection.
			method.releaseConnection();
		}
	}

	/**
	 * Marshall JSON metadata into a new W3CWidget instance
	 * @param metadata JSON-formatted String containing Gadget metadata
	 * @param shindig the URL of the shindig service
	 * @return a W3CWidget instance
	 * @throws Exception if metadata cannot create a valid widget
	 */
	public static W3CWidget getWidget(String metadata, String shindig) throws Exception{
		JSONObject gadget = null;
		JSONObject response = new JSONObject(metadata);
		JSONArray gadgets = response.getJSONArray("gadgets");
		if (gadgets.length()==0) return null;
		gadget = gadgets.getJSONObject(0);
		return new GadgetAdapter(gadget, shindig);
	}

}
