/**
 * 
 */
package org.tencompetence.widgetservice.util.gadgets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tencompetence.widgetservice.beans.Widget;
/**
 * Utilities for working with a Shindig Gadget Server, particularly for parsing gadget metadata
 * 
 * @author scott
 *
 */
public class GadgetUtils {

	/**
	 * Relative URL of the Shindig metadata service
	 */
	public static final String METADATA_SERVICE = "/gadgets/metadata";

	/**
	 * The default Container ID
	 */
	private static final String CONTAINER_ID = "wookie";

	/**
	 * The default Gadget icon
	 */
	private static final String DEFAULT_ICON = "/wookie/shared/images/defaultwidget.png";

	/**
	 * Create a new Widget from the request supplied
	 * @param request the registration request, with a url parameter for the desired widget
	 * @return the widget
	 * @throws MalformedURLException
	 */
	public static Widget createWidget(HttpServletRequest request) throws Exception{
		URL gadgetService = new URL(request.getScheme() ,
				request.getServerName() ,
				request.getServerPort() , GadgetUtils.METADATA_SERVICE);
		return createWidget(gadgetService.toString(), request.getParameter("url"));
	}

	/**
	 * Create a new widget based on the gadget url supplied, and the metdata service supplied
	 * @param service the metadata service
	 * @param url the gadget url
	 * @return the Widget instance
	 */
	private static Widget createWidget(String service, String url) throws Exception{
		return getWidget(getMetadata(service,url));
	}

	/**
	 * Return metadata from a metadata service for the requested gadget url
	 * @param service
	 * @param url
	 * @return
	 */
	public static String getMetadata(String service,String url) throws Exception{
		String request = new String();
		request = "{\"context\":{\"country\":\"US\",\"language\":\"en\",\"view\":\"default\",\"container\":\""+CONTAINER_ID+"\"},\"gadgets\":[{\"url\":\""+url+"\",\"moduleId\":1}]}";

		StringRequestEntity req = null;
		try {
			req = new StringRequestEntity(request, "text/x-json","UTF-8");
		} catch (UnsupportedEncodingException e1) {
			//TODO implement
			e1.printStackTrace();
		}

		String response = "";

		PostMethod post = new PostMethod(service);
		try {
			post.setRequestEntity(req);
			post.setRequestHeader("Content-Length", String.valueOf(req.getContentLength()));
			response = executeMethod(post);
		} catch (Exception e) {
			throw new Exception("There was a problem connecting to the Shindig metadata service");
		}
		return response;
	}

	/**
	 * Call a remote service
	 * @param method
	 * @return
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
				//return readFully(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
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
	 * Marshall JSON metadata into a new Widget instance
	 * @param metadata JSON-formatted String containing Gadget metadata
	 * @return a Widget instance
	 * @throws Exception if metadata cannot create a valid widget
	 */
	public static Widget getWidget(String metadata) throws Exception{
		JSONObject gadget = null;
		Widget widget = null;
		JSONObject response = new JSONObject(metadata);
		JSONArray gadgets = response.getJSONArray("gadgets");
		if (gadgets.length() > 0){
			gadget = gadgets.getJSONObject(0);
			if (gadget.has("errors")) throw new Exception("Invalid gadget - Shindig error");
			widget = new Widget();
			// Defaults
			String title = "Untitled Gadget";
			int height = 200;
			int width = 320;
			String author = "Unknown Author";
			String description = "Google Gadget";
			String icon = DEFAULT_ICON;

			if (!gadget.has("url")) throw new Exception("Invalid gadget - URL missing");
			if (gadget.getString("url") == null || gadget.getString("url").equals("")) throw new Exception("Invalid gadget - Invalid URL");
			try {
				URL url = new URL(gadget.getString("url"));
			} catch (Exception e) {
				throw new Exception("Invalid gadget - invalid URL");
			}

			// Use the URL as the GUID
			widget.setGuid(gadget.getString("url"));

			// We should be able to use the "iframeUrl" property here, but
			// it isn't very reliable at generating a usable value, so we construct
			// a very basic URL instead
			widget.setUrl("/gadgets/ifr?url="+gadget.getString("url")+"&amp;lang=en&amp;country=UK");

			if (gadget.has("height")) if (gadget.getInt("height") != 0) height = gadget.getInt("height");
			if (gadget.has("width")) if (gadget.getInt("width") != 0) width = gadget.getInt("width");

			if (gadget.has("author")){
				if (gadget.getString("author")!=null){
					if (!(gadget.getString("author").trim()).equals("")){
						author = gadget.getString("author");
					}
				}
			}

			if (gadget.has("title")){
				if (gadget.getString("title")!=null){
					if (!(gadget.getString("title").trim()).equals("")){
						title = gadget.getString("title");
					}
				}
			}

			// Override the title with directory title if present (this is intended for gallery use)
			if (gadget.has("directory_title")){
				if (gadget.getString("directory_title")!=null){
					if (!(gadget.getString("directory_title").trim()).equals("")){
						title = gadget.getString("directory_title");
					}    			
				}
			}

			if (gadget.has("description")){
				if (gadget.getString("description")!=null){
					if (!(gadget.getString("description").trim()).equals("")){
						title = gadget.getString("description");
					}
				}
			}

			if (gadget.has("thumbnail")){
				if (gadget.getString("thumbnail")!=null){
					if (!(gadget.getString("thumbnail").trim()).equals("")){
						icon = gadget.getString("thumbnail");
					}
				}
			}

			widget.setMaximize(false);
			widget.setHeight(height);
			widget.setWidth(width);
			widget.setWidgetTitle(title);
			widget.setWidgetDescription(description);
			widget.setWidgetIconLocation(icon);
			widget.setWidgetAuthor(author);

		}
		return widget;
	}


}
