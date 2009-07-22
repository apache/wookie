package org.apache.wookie.tests;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class TestClient {
	private static String aUrl = "http://www.reload.ac.uk";

	public TestClient() {
	}

	public String get(String url) throws Exception {
		System.out.println("GET from " + url); //$NON-NLS-1$
		return fetchData(new GetMethod(url));
	}

	public String post(String uri, String xmlData) throws Exception {
		// Prepare HTTP post
		PostMethod post = new PostMethod(uri);
		System.out.println("POST to " + uri); //$NON-NLS-1$
		System.out.println(xmlData);

		return sendXmlData(xmlData, post, null);
	}

	private String fetchData(HttpMethodBase method) throws Exception {
		try {
			// Create an instance of HttpClient.
			HttpClient client = new HttpClient();

			// set the clients proxy values if needed
			// ConnectionsPrefsManager.setProxySettings(client);

			// Provide custom retry handler is necessary
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(1, false));

			// Tell the method to automatically handle authentication.
			method.setDoAuthentication(true);

			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode == HttpStatus.SC_OK) {
				// for now we are only expecting Strings
				return method.getResponseBodyAsString();

			} else if (statusCode == HttpStatus.SC_UNAUTHORIZED)
				throw new Exception("Passed credentials are not correct"); //$NON-NLS-1$						
			else if (statusCode == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED)
				throw new Exception(
						"Proxy authentication required. This can be set in the preferences."); //$NON-NLS-1$									
			else
				throw new Exception(
						"Method failed: " + method.getStatusLine() + ' ' + method.getURI() + ' ' + method.getStatusText()); //$NON-NLS-1$
		} catch (HttpException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
	}

	private String sendXmlData(String xmlData, EntityEnclosingMethod method,
			Part[] parts) throws Exception {
		try {
			// Create an instance of HttpClient.
			HttpClient client = new HttpClient();
			// Tell the method to automatically handle authentication.
			method.setDoAuthentication(true);

			if (parts != null) {
				method.setRequestEntity(new MultipartRequestEntity(parts,
						method.getParams()));
			} else {

				// Request content will be retrieved directly
				// from the input stream
				// Per default, the request content needs to be buffered
				// in order to determine its length.
				// Request body buffering can be avoided when
				// content length is explicitly specified
				method.setRequestEntity(new StringRequestEntity(xmlData,
						"text/xml", "UTF8")); //$NON-NLS-1$  //$NON-NLS-2$

				// Specify content type and encoding
				// If content encoding is not explicitly specified
				// ISO-8859-1 is assumed
				method.setRequestHeader(
						"Content-type", "text/xml; charset=UTF8"); //$NON-NLS-1$  //$NON-NLS-2$
				// post.setRequestHeader("Content-type", "text/xml;
				// charset=ISO-8859-1");

				// Provide custom retry handler is necessary
				method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
						new DefaultHttpMethodRetryHandler(3, false));
			}
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode == HttpStatus.SC_OK
					|| statusCode == HttpStatus.SC_CREATED) {
				// Read the response body.
				byte[] responseBody = method.getResponseBody();

				// Deal with the response.
				// Use caution: ensure correct character encoding and is not
				// binary data
				return new String(responseBody);

			} else if (statusCode == HttpStatus.SC_UNAUTHORIZED)
				throw new Exception("Passed credentials are not correct"); //$NON-NLS-1$
			else if (statusCode == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED)
				throw new Exception(
						"Proxy authentication required. This can be set in the preferences."); //$NON-NLS-1$									
			else if (statusCode == HttpStatus.SC_CONFLICT)
				throw new Exception("Resource already exists on server"); //$NON-NLS-1$
			else
				throw new Exception(
						"Method failed: " + method.getStatusLine() + ' ' + method.getURI()); //$NON-NLS-1$
		} catch (HttpException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
	}

	public static void main(String[] args) {
		TestClient testClient = new TestClient();
		try {
			System.out.println(testClient.get(aUrl));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}