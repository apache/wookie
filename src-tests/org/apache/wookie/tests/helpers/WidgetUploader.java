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
package org.apache.wookie.tests.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * Helper class for uploading widgets and gathering any errors generated
 */
public class WidgetUploader {
	
	public static final String SERVICE_URL = "http://localhost:8080/wookie/widgets";

	/**
	 * Upload a widget from a file at a given URL
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String uploadWidget(String url) throws IOException{
		HttpClient httpclient = new HttpClient();
		GetMethod get = new GetMethod(url);
		int status = httpclient.executeMethod(get);
		if (status != 200){
			fail("problem with download:"+url);
		}
		File file = File.createTempFile("w3c", ".wgt");
		FileUtils.writeByteArrayToFile(file, IOUtils.toByteArray(get.getResponseBodyAsStream()));
		get.releaseConnection();
		return uploadWidget(file);		
	}
	
	/**
	 * Upload a widget
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String uploadWidget(File file) throws IOException{
		HttpClient httpclient = new HttpClient();
		httpclient.getState().setCredentials(
				 new AuthScope("localhost", 8080, "wookie"),
				 new UsernamePasswordCredentials("java", "java")
				 );
		PostMethod post = new PostMethod(SERVICE_URL);
		Part[] parts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(parts, post
				.getParams()));
		String response = IOUtils.toString(post.getResponseBodyAsStream());
		post.releaseConnection();
		return response;
	}
	
	/**
	 * Get the current set of installed widgets
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Element[] getWidgets(){
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod("http://localhost:8080/wookie/widgets?all=true");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = IOUtils.toString(get.getResponseBodyAsStream());
	        
	        get.releaseConnection();
	        
			SAXBuilder builder = new SAXBuilder();
			Reader in = new StringReader(response);
			try {
				Document doc = builder.build(in);
				List widgets = doc.getRootElement().getChildren("widget");
				if (widgets != null){
					return (Element[])widgets.toArray(new Element[widgets.size()]);
				}
			} catch (Exception e) {
				//e.printStackTrace();
				fail("Bad XML returned by server");
				return null;
			} 
	    }
	    catch (Exception e) {
	    	//e.printStackTrace();
	    	fail("get failed");
	    }
		return null;		
	}

	/**
	 * Get a specific widget by its URI
	 * @param uid
	 * @return
	 */
	public static Element getWidget(String uri){
		Element[] widgets = getWidgets();
		if (widgets == null) return  null;
		for (Element widget:widgets){
			if (widget.getAttributeValue("identifier").equals(uri)) return widget;
		}
		return null;
	}
	
	/**
	 * Get the last installed widget
	 * @return
	 */
	public static Element getLastWidget(){
		Element[] widgets = getWidgets();
		if (widgets == null) return  null;
		return widgets[widgets.length-1];
	}

}
