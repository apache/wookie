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
	 * Create a new Widget from the request supplied
	 * @param request the registration request, with a url parameter for the desired widget
	 * @return the widget
	 * @throws MalformedURLException
	 */
	public static Widget createWidget(HttpServletRequest request) throws MalformedURLException{
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
	private static Widget createWidget(String service, String url){
		Widget widget = null;
		try {
			widget = getWidget(getMetadata(service,url));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return widget;
	}
	
	/**
	 * Return metadata from a metadata service for the requested gadget url
	 * @param service
	 * @param url
	 * @return
	 */
	public static String getMetadata(String service,String url){
		String request = new String();
		request = "{\"context\":{\"country\":\"US\",\"language\":\"en\",\"view\":\"default\",\"container\":\"default\"},\"gadgets\":[{\"url\":\""+url+"\",\"moduleId\":1}]}";

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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
     * @param metadata
     * @return
     * @throws Exception
     */
    private static Widget getWidget(String metadata) throws Exception{
    	JSONObject gadget = null;
    	Widget widget = new Widget();
    	JSONObject response = new JSONObject(metadata);
    	JSONArray gadgets = response.getJSONArray("gadgets");
    	for (int idx=0;idx < gadgets.length(); idx++){
    		gadget = gadgets.getJSONObject(idx);
    		widget.setGuid(gadget.getString("url"));
    		widget.setUrl(gadget.getString("iframeUrl"));
    		widget.setHeight(gadget.getInt("height"));
    		// Default from gadget spec
    		if (widget.getHeight() == 0) widget.setHeight(200);
    		widget.setWidth(gadget.getInt("width"));
    		// Default from gadget spec
    		if (widget.getWidth() == 0) widget.setWidth(320);
    		widget.setWidgetAuthor(gadget.getString("author"));
    		widget.setWidgetIconLocation(gadget.getString("thumbnail"));
    		widget.setMaximize(false);
    		widget.setWidgetTitle(gadget.getString("title"));
    		widget.setWidgetDescription("Google Gadget");
    	}
    	return widget;
    }
    

}
