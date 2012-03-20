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
package org.apache.wookie.connector.framework;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpException;
//import org.apache.commons.httpclient.methods.DeleteMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class AbstractWookieConnectorService implements IWookieConnectorService {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractWookieConnectorService.class);
	
	WookieServerConnection conn;
	
	WidgetInstances instances = new WidgetInstances();

	public AbstractWookieConnectorService(String url, String apiKey, String sharedDataKey) throws WookieConnectorException {
		WookieServerConnection thisConnection = new WookieServerConnection (url, apiKey, sharedDataKey);
		setConnection(thisConnection);
	}
	
	
	

	/**
	 * Creates a WookieConnectorService that has not yet been initialised to
	 * connect to a specific server.
	 */
	protected AbstractWookieConnectorService() {
		super();
	}
	

	public void setConnection(WookieServerConnection newConn) {
		logger.debug("Setting wookie connection to: " + newConn);
		this.conn = newConn;
	}
	

	public WookieServerConnection getConnection() {
		return this.conn;
	}

	/**
	 * Get or create an instance of a widget.
	 * 
	 * @param widget
	 * @return the ID of the widget instance
	 * @throws IOException
	 * @throws SimalRepositoryException
	 * 
	 */
	public WidgetInstance getOrCreateInstance(Widget widget) throws IOException, WookieConnectorException {
		return getOrCreateInstance(widget.identifier);
	}

	public void setPropertyForInstance(WidgetInstance instance,
										boolean is_public, String fName,
										String fValue) throws WookieConnectorException, IOException {
		String queryString;
		try {
			queryString = createInstanceParams(instance);
			//queryString = "id_key=";
			//queryString += URLEncoder.encode(instance.getIdKey(), "UTF-8");
			//queryString += "&api_key=";
			//queryString += (URLEncoder.encode(getConnection().getApiKey(), "UTF-8"));
			queryString += "&is_public=";
			if ( is_public ) {
				queryString += "true";
			}
			else {
				queryString += "false";
			}
			queryString += ("&propertyname=");
			queryString += (URLEncoder.encode(fName, "UTF-8"));
			queryString += ("&propertyvalue=");
			queryString += (URLEncoder.encode(fValue, "UTF-8"));
			logger.debug(queryString);
		}
		catch (UnsupportedEncodingException e) {
			throw new WookieConnectorException("Must support UTF-8 encoding", e);
		}
		URL url = null;
		try {
			url = new URL(conn.getURL() + "/properties");
			
			//url = new URL(conn.getURL() + "/WidgetServiceServlet" + queryString);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setDoOutput(true);
			
			OutputStreamWriter out = new OutputStreamWriter(urlConn.getOutputStream());
			out.write(queryString);
			out.close();		
			if (urlConn.getResponseCode() > 201) {
				throw new IOException(urlConn.getResponseMessage());
			}
		}
		catch (MalformedURLException e) {
			throw new RuntimeException( "URL for supplied Wookie Server is malformed", e);
		}
	}
	
	
	public String getPropertyForInstance ( WidgetInstance instance, String propertyName ) throws WookieConnectorException, IOException {
		String queryString = createInstanceParams(instance);
		try {
			queryString += "&propertyname=";
			queryString += URLEncoder.encode(propertyName, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new WookieConnectorException ("Must support UTF-8 encoding", e);
		}
		URL url = null;
		try {
			url = new URL(conn.getURL() + "/properties?" + queryString);
			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
			InputStream s = urlConn.getInputStream();
			String property = convertISToString(s);
			if ( urlConn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
				// should mean the property isn't there so just return null
				// TODO - do we need to throw an exception here?
				return null;
			}
			return property;
		}
		catch (FileNotFoundException e) {
			// catch file not found exception generated if resource is not found and return null
			return null;
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("URL for supplied Wookie Server is malformed", e);
		} 
	}
	
	
	public void updatePropertyForInstance (WidgetInstance instance, boolean is_public, String propertyName, String data ) throws WookieConnectorException, IOException {
		String putString;
		try {
			
			putString = createInstanceParams(instance);
			putString += "&is_public=";
			if ( is_public ) {
				putString += "true";
			}
			else {
				putString += "false";
			}
			putString += "&propertyname=";
			putString += URLEncoder.encode(propertyName, "UTF-8");
			putString += ("&propertyvalue=");
			putString += (URLEncoder.encode(data, "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			throw new WookieConnectorException("Must support UTF-8 encoding", e);
		}
		URL url = null;
		try {
			//url = new URL(conn.getURL() + "/properties?api_key="+URLEncoder.encode(conn.getApiKey(), "UTF-8"));
			url = new URL(conn.getURL() + "/properties?"+putString);
			//url = new URL(conn.getURL() + "/WidgetServiceServlet" + queryString);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("PUT");
			urlConn.connect();
			//urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			//urlConn.setDoOutput(true);
			
			//OutputStreamWriter out = new OutputStreamWriter(urlConn.getOutputStream());
			//out.write(putString);
			//out.close();		
			if (urlConn.getResponseCode() > 201) {
				throw new IOException(urlConn.getResponseMessage());
			}
			
		}
		catch (MalformedURLException e) {
			throw new WookieConnectorException("URL for supplied Wookie Server is malformed", e);
		}
	}
	
	
	
	public void deletePropertyForInstance ( WidgetInstance instance, boolean is_public, String propertyName ) throws WookieConnectorException, IOException {
		String deleteString = createInstanceParams(instance);
		try {
			deleteString += "&is_public=";
			if ( is_public ) {
				deleteString += "true";
			}
			else {
				deleteString += "false";
			}
			deleteString += "&propertyname=";
			deleteString += URLEncoder.encode(propertyName, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new WookieConnectorException("Must support UTF-8 encoding", e);
		}
		try {
			URL url = new URL(conn.getURL()+"/properties?"+deleteString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
			connection.connect();
			int code = connection.getResponseCode();
			if (code > 202 ) {
				throw new IOException (connection.getResponseMessage());
			}
		}
		catch ( MalformedURLException e ) {
			throw new WookieConnectorException ("URL for Wookie Server is malfomed", e);
		}
	}

	public WidgetInstance getOrCreateInstance(String guid) throws IOException, WookieConnectorException {
		URL url;
		WidgetInstance instance;
		try {
			StringBuilder postdata = new StringBuilder("requestid=getwidget&api_key=");
			postdata.append(URLEncoder.encode(getConnection().getApiKey(), "UTF-8"));
			postdata.append("&shareddatakey=");
			postdata.append(URLEncoder.encode(getConnection().getSharedDataKey(), "UTF-8"));
			postdata.append("&userid=");
			postdata.append(URLEncoder.encode(getCurrentUser().getLoginName(), "UTF-8"));
			postdata.append("&widgetid=");
			postdata.append(URLEncoder.encode(guid, "UTF-8"));

			logger.debug("Making Wookie REST query using: " + postdata);
            
			url = new URL(conn.getURL() + "/WidgetServiceServlet?"+postdata);
			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			InputStream is = urlConn.getInputStream();

			if (urlConn.getResponseCode() > 200) {
				throw new IOException(urlConn.getResponseMessage());
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docb = dbf.newDocumentBuilder();
			Document parsedDoc = docb.parse(is);
			instance = parseInstance(guid, parsedDoc);
			instances.put(instance);


			addParticipant(instance, getCurrentUser());
			
		}
		catch (MalformedURLException e) {
			throw new RuntimeException(
					"URL for supplied Wookie Server is malformed", e);
		}
		catch (ParserConfigurationException e) {
			throw new RuntimeException("Unable to configure XML parser", e);
		}
		catch (SAXException e) {
			throw new RuntimeException(
					"Problem parsing XML from Wookie Server", e);
		}

		return instance;
	}

	/**
	 * Parse an XML document returned from the Wookie server that describes a
	 * widget instance.
	 * 
	 * @param widgetId
	 *            the identifier of the widget this document represents
	 * @param xml
	 *            description of the instance as returned by the widget server
	 *            when the widget was instantiated.
	 * 
	 * @return the identifier for this instance
	 */
	public WidgetInstance parseInstance(String widgetId, Document xml) {
		Element rootEl = xml.getDocumentElement();
		String url = getNodeTextContent(rootEl, "url");
		String title = getNodeTextContent(rootEl, "title");
		String height = getNodeTextContent(rootEl, "height");
		String width = getNodeTextContent(rootEl, "width");
		String idKey = getNodeTextContent(rootEl, "identifier");
		WidgetInstance instance = new WidgetInstance(url, widgetId, title, height, width, idKey);
		logger.debug(instance.toString());
		return instance;
	}

	/**
	 * @refactor At time of writing the REST API for adding a participant is
	 *           broken so we are using the non-REST approach. The code for REST
	 *           API is commented out and should be used in the future.
	 */
	public void addParticipant(WidgetInstance widget, User user)
			throws WookieConnectorException {
		StringBuilder postdata;
		try {
			postdata = new StringBuilder("api_key=");
			postdata.append(URLEncoder.encode(getConnection().getApiKey(), "UTF-8"));
			postdata.append("&shareddatakey=");
			postdata.append(URLEncoder.encode(getConnection().getSharedDataKey(), "UTF-8"));
			postdata.append("&userid=");
			postdata.append(URLEncoder.encode(getCurrentUser().getLoginName(), "UTF-8"));
			postdata.append("&widgetid=");
			postdata.append(URLEncoder.encode(widget.getId(), "UTF-8"));
			postdata.append("&participant_id=");
			postdata.append(URLEncoder.encode(user.getLoginName(), "UTF-8"));
			postdata.append("&participant_display_name=");
			postdata.append(URLEncoder.encode(user.getScreenName(), "UTF-8"));
			postdata.append("&participant_thumbnail_url=");
			postdata.append(URLEncoder.encode(user.getThumbnailUrl(), "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			throw new WookieConnectorException("Must support UTF-8 encoding", e);
		}

		URL url = null;
		try {
			url = new URL(conn.getURL() + "/participants");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(postdata.toString());
			wr.flush();
			if (conn.getResponseCode() > 201) {
				throw new IOException(conn.getResponseMessage());
			}
		}
		catch (MalformedURLException e) {
			throw new WookieConnectorException( "Participants rest URL is incorrect: " + url, e);
		}
		catch (IOException e) {
			StringBuilder sb = new StringBuilder( "Problem adding a participant. ");
			sb.append("URL: ");
			sb.append(url);
			sb.append(" data: ");
			sb.append(postdata);
			throw new WookieConnectorException(sb.toString(), e);
		}
	}
	

	/**
	 * Get a set of all the available widgets in the server. If there is an
	 * error communicating with the server return an empty set, or the set
	 * received so far in order to allow the application to proceed. The
	 * application should display an appropriate message in this case.
	 * 
	 * @return
	 * @throws WookieConnectorException
	 */
	public HashMap<String, Widget> getAvailableWidgets() throws WookieConnectorException {

		HashMap<String, Widget> widgets = new HashMap<String, Widget>();

		try {
			URL url = new URL(conn.getURL() + "/widgets?all=true");

			Document widgetsDoc = getURLDoc(url);

			Element root = widgetsDoc.getDocumentElement();
			NodeList widgetList = root.getElementsByTagName("widget");
			for (int idx = 0; idx < widgetList.getLength(); idx = idx + 1) {
				Element widgetEl = (Element) widgetList.item(idx);
				String id = widgetEl.getAttribute("id");
				String width = widgetEl.getAttribute("width");
				String height = widgetEl.getAttribute("height");
				String version = widgetEl.getAttribute("version");
				if (widgets.containsKey(id)) {
					break;
				}
				String name = getNodeTextContent(widgetEl, "name" );
				String description = getNodeTextContent(widgetEl, "description" );
				String license = getNodeTextContent(widgetEl, "license" );
				String author = getNodeTextContent(widgetEl, "author" );
				Element iconEl = (Element) widgetEl.getElementsByTagName("icon").item(0);
				URL iconURL;
				if (iconEl != null) {
					iconURL = new URL(iconEl.getAttribute("src"));
				}
				else {
					iconURL = new URL("http://www.oss-watch.ac.uk/images/logo2.gif");
				}

				Widget widget = new Widget(id, name, 
										description, iconURL,
										width, height,
										version, author,
										license);
				
				widgets.put(id, widget);
			}
		}
		catch (ParserConfigurationException e) {
			throw new WookieConnectorException("Unable to create XML parser", e);
		}
		catch (MalformedURLException e) {
			throw new WookieConnectorException("URL for Wookie is malformed", e);
		}
		catch (IOException e) {
			// return an empty set, or the set received so far in order to allow
			// the application to proceed. The application should display an
			// appropriate message in this case.
			return widgets;
		}
		catch (SAXException e) {
			throw new WookieConnectorException(
					"Unable to parse the response from Wookie", e);
		}
		return widgets;
	}
	
	
	

	public WidgetInstances getInstances() {
		return instances;
	}

	/**
	 * Get the array of users for a widget instance
	 * 
	 * @param instance
	 * @return an array of users
	 * @throws WookieConnectorException
	 */
	public User[] getUsers(WidgetInstance instance) throws WookieConnectorException {
		String queryString;
		try {
			queryString = new String("?api_key=");
			queryString += (URLEncoder.encode(getConnection().getApiKey(), "UTF-8"));
			queryString += ("&shareddatakey=");
			queryString += (URLEncoder.encode(getConnection().getSharedDataKey(), "UTF-8"));
			queryString += ("&userid=");
			queryString += (URLEncoder.encode(getCurrentUser().getLoginName(), "UTF-8"));
			queryString += ("&widgetid=");
			queryString += (URLEncoder.encode(instance.getId(), "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			throw new WookieConnectorException("Must support UTF-8 encoding", e);
		}

		URL url = null;
		try {
			url = new URL(conn.getURL() + "/participants" + queryString);
			Document usersDoc = getURLDoc(url);
			Element root = usersDoc.getDocumentElement();
			NodeList participantsList = root.getElementsByTagName("participant");
			if (participantsList == null || participantsList.getLength() == 0) {
				return new User[0];
			}
			User[] users = new User[participantsList.getLength()];
			for (int idx = 0; idx < participantsList.getLength(); idx = idx + 1) {
				Element participantEl = (Element) participantsList.item(idx);
				String id = participantEl.getAttribute("id");
				String name = participantEl.getAttribute("display_name");
				String thumbnail = participantEl.getAttribute("thumbnail_url");
				User user = new User(id, name, thumbnail);
				users[idx] = user;
			}
			return users;
		}
		catch (MalformedURLException e) {
			throw new WookieConnectorException(
					"Participants rest URL is incorrect: " + url, e);
		}
		catch (IOException e) {
			StringBuilder sb = new StringBuilder("Problem getting participants. ");
			sb.append("URL: ");
			sb.append(url);
			sb.append(" data: ");
			sb.append(queryString);
			throw new WookieConnectorException(sb.toString(), e);
		}
		catch (ParserConfigurationException e) {
			throw new WookieConnectorException("Problem parsing data: " + url, e);
		}
		catch (SAXException e) {
			throw new WookieConnectorException("Problem parsing data: " + url, e);
		}
	}
	
	
	
	
	private String getNodeTextContent(Element e, String subElementName ) {
		NodeList nl = e.getElementsByTagName(subElementName);
		if ( nl.getLength() > 0 ) {
			Node n = nl.item(0);
			if ( n != null ) {
				return n.getTextContent();
			}
		}
		return "";
	}

	
	
	
	/**
	 * Gets the input stream and parses it to a document
	 * 
	 * @param url
	 * @return Document
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private Document getURLDoc(URL url) throws IOException, ParserConfigurationException, SAXException {
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		InputStream is = httpConnection.getInputStream();
		if (httpConnection.getResponseCode() > 200) {
			throw new IOException(httpConnection.getResponseMessage());
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docb = dbf.newDocumentBuilder();
		Document parsedDoc = docb.parse(is);
		return parsedDoc;
	}
	
	private String createInstanceParams ( WidgetInstance instance ) throws WookieConnectorException {
		String queryString;
		try {
			queryString = new String("api_key=");
			queryString += (URLEncoder.encode(getConnection().getApiKey(), "UTF-8"));
			queryString += ("&shareddatakey=");
			queryString += (URLEncoder.encode(getConnection().getSharedDataKey(), "UTF-8"));
			queryString += ("&userid=");
			queryString += (URLEncoder.encode(getCurrentUser().getLoginName(), "UTF-8"));
			queryString += ("&widgetid=");
			queryString += (URLEncoder.encode(instance.getId(), "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			throw new WookieConnectorException("Must support UTF-8 encoding", e);
		}
		return queryString;
	}
	
	private String convertISToString (InputStream is ) throws IOException {
		StringWriter writer = new StringWriter();
		
		char buff[] = new char[1024];
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		int n;
		while ((n = reader.read(buff)) != -1 ) {
			writer.write(buff,0,n);
		}
		writer.close();
		return writer.toString();
		
	}
}
