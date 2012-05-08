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


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
			url = new URL(conn.getURL() + "/properties?"+putString);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("PUT");
			urlConn.connect();
	
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

  public WidgetInstance getOrCreateInstance(String guid) throws IOException,
      WookieConnectorException {
    URL url;
    WidgetInstance instance;
    try {
      StringBuilder postdata = new StringBuilder("api_key=");
      postdata.append(URLEncoder.encode(getConnection().getApiKey(), "UTF-8"));
      postdata.append("&shareddatakey=");
      postdata.append(URLEncoder.encode(getConnection().getSharedDataKey(), "UTF-8"));
      postdata.append("&userid=");
      postdata.append(URLEncoder.encode(getCurrentUser().getLoginName(),"UTF-8"));
      postdata.append("&widgetid=");
      postdata.append(URLEncoder.encode(guid, "UTF-8"));

      logger.debug("Making Wookie REST query using: " + postdata);

      url = new URL(conn.getURL() + "/widgetinstances?" + postdata);
      HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
      urlConn.setRequestMethod("POST");
      urlConn.setDoOutput(true);
      urlConn.setDoInput(true);
      InputStream is = urlConn.getInputStream();

      //
      // From v 0.9.2 onwards, we get 201 for created, 200 for already existing
      //
      if (urlConn.getResponseCode() != 200 && urlConn.getResponseCode() != 201) {
        throw new IOException(urlConn.getResponseMessage());
      }
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder docb = dbf.newDocumentBuilder();
      Document parsedDoc = docb.parse(is);
      instance = parseInstance(guid, parsedDoc);
      instances.put(instance);

      addParticipant(instance, getCurrentUser());

    } catch (MalformedURLException e) {
      throw new RuntimeException("URL for supplied Wookie Server is malformed",
          e);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException("Unable to configure XML parser", e);
    } catch (SAXException e) {
      throw new RuntimeException("Problem parsing XML from Wookie Server", e);
    }

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
	 * Removes a user as a participant from a particular widget instance
	 * @param instance
	 * @param user
	 * @throws WookieConnectorException
	 */
	public void removeParticipantFromWidget ( WidgetInstance instance, User user ) throws WookieConnectorException {
		StringBuilder queryString = new StringBuilder(createInstanceParams(instance));
		try {
			queryString.append("&participant_id=");
			queryString.append(URLEncoder.encode(user.getLoginName(), "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
				throw new WookieConnectorException ( "UTF-8 must be supported", e);
		}
		URL url = null;
		try {
			url = new URL(conn.getURL() + "/participants?"+queryString);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("DELETE");
			urlConn.connect();
		}
		catch (MalformedURLException e) {
			throw new WookieConnectorException( "Participants rest URL is incorrect: " + url, e);
		}
		catch (IOException e) {
			StringBuilder sb = new StringBuilder( "Problem adding a participant. ");
			sb.append("URL: ");
			sb.append(url);
			sb.append(" data: ");
			sb.append(queryString);
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
	      
	      Widget widget = createWidgetFromElement ( widgetEl );

	      widgets.put(widget.getIdentifier(), widget);
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
	    throw new WookieConnectorException( "Unable to parse the response from Wookie", e);
	  }
	  return widgets;
	}
	
	
	/**
	 * This function is supplied for non browser based clients enabling them to upload a widget file to the wookie server.
	 * 
	 * @param widgetFile - a java file pointer to the widget file on the local system
	 * @param adminUsername - the admin user as defined within the configuration of the wookie server
	 * @param adminPassword - the admin password as defined within the configuration of the wookie server
	 * @return - a widget describing the meta-data of the widget uploaded.
	 * @throws WookieConnectorException
	 */
	public Widget postWidget(File widgetFile, String adminUsername, String adminPassword ) throws WookieConnectorException {
		HttpURLConnection connection = null;
		DataOutputStream dos = null;
		String exsistingFileName = widgetFile.getAbsolutePath();
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		Widget widget = null;

		try {

			FileInputStream fileInputStream = new FileInputStream(new File(exsistingFileName));
			URL url = new URL(conn.getURL() + "/widgets");
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			
			addBasicAuthToConnection ( connection, adminUsername, adminPassword );

			dos = new DataOutputStream(connection.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"upload\";"
					+ " filename=\"" + exsistingFileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			// create a buffer of maximum size

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...

			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams

			fileInputStream.close();
			dos.flush();
			dos.close();

		} catch (MalformedURLException ex) {
			throw new WookieConnectorException ( "Bad url to wookie host: ", ex);
		}

		catch (IOException ioe) {
			throw new WookieConnectorException ( "From Server: ", ioe );
		}

		// ------------------ read the SERVER RESPONSE

		try {
			if ( connection.getResponseCode() != 201 ) {
				throw new IOException ( "Widget file was not uploaded successfully." );
			}
			Document doc = parseInputStreamAsDocument(connection.getInputStream());
			Element root = doc.getDocumentElement();
			//NodeList widgetList = root.getElementsByTagName("widget");
			//Element widgetEl = (Element) widgetList.item(0);
			widget = createWidgetFromElement ( root );

		} catch (IOException ioex) {
			throw new WookieConnectorException("From (ServerResponse): ", ioex);

		} catch (ParserConfigurationException e) {
			throw new WookieConnectorException ( "XML Parser configuration failed: ", e );
		} catch (SAXException e) {
			throw new WookieConnectorException ( "XML Parser error: ", e);
		}
		return widget;

	}
	
	
	public Widget updateWidget ( File widgetFile, String widgetIdentifier, String adminUsername, String adminPassword ) throws WookieConnectorException {
		HttpURLConnection connection = null;
		DataOutputStream dos = null;
		String exsistingFileName = widgetFile.getAbsolutePath();
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		Widget widget = null;

		try {

			FileInputStream fileInputStream = new FileInputStream(new File(exsistingFileName));
			URL url = new URL(conn.getURL() + "/widgets/"+widgetIdentifier);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			
			addBasicAuthToConnection ( connection, adminUsername, adminPassword );

			dos = new DataOutputStream(connection.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"upload\";"
					+ " filename=\"" + exsistingFileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			// create a buffer of maximum size

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...

			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams

			fileInputStream.close();
			dos.flush();
			dos.close();
			if ( connection.getResponseCode() != 200 ) {
				throw new IOException ( "Widget file was not updated successfully." );
			}
			Document doc = parseInputStreamAsDocument(connection.getInputStream());
			Element root = doc.getDocumentElement();
			//NodeList widgetList = root.getElementsByTagName("widget");
			//Element widgetEl = (Element) widgetList.item(0);
			widget = createWidgetFromElement ( root );

		}
		catch (MalformedURLException ex) {
			throw new WookieConnectorException ( "Bad url to wookie host: ", ex);
		}
		catch (IOException ioe) {
			throw new WookieConnectorException ( "From Server: ", ioe );
		}
		catch (ParserConfigurationException e) {
			throw new WookieConnectorException ( "XML Parser configuration failed: ", e );
		}
		catch (SAXException e) {
			throw new WookieConnectorException ( "XML Parser error: ", e);
		}
		return widget;
	}
	
	
	
	public void deleteWidget ( String identifier, String adminUsername, String adminPassword ) throws IOException, WookieConnectorException
	{
		URL url = null;
		try {
			url = new URL ( conn.getURL() + "/widgets/"+identifier);
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			addBasicAuthToConnection ( urlConn, adminUsername, adminPassword );
			urlConn.setRequestMethod("DELETE");
			urlConn.connect();
			if ( urlConn.getResponseCode() > 201 ) {
				throw new IOException ( urlConn.getResponseMessage());
			}
		}
		catch ( MalformedURLException e ) {
			throw new WookieConnectorException ( "Delete Widget rest URL is incorect: " + url, e );
		}
	}
	
	/**
	 * Gets a single widget xml description for the given widget identifier
	 * @param identifier
	 * @return
	 * @throws WookieConnectorException
	 * @throws IOException
	 */
	public Widget getWidget ( String identifier ) throws WookieConnectorException, IOException {
		URL url = null;
		Widget widget = null;
		try {
			url = new URL ( conn.getURL() + "/widgets/"+identifier );
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Accept", "xml");
			if ( connection.getResponseCode() == 404 ) {
				// could not find the widget just return null;
				return null;
			}
			else if( connection.getResponseCode() != 200 ) {
				throw new IOException( connection.getResponseMessage());
			}
			Document doc = parseInputStreamAsDocument ( connection.getInputStream());
			Element root = doc.getDocumentElement();
			//NodeList widgetList = root.getElementsByTagName("widget");
			//Element widgetEl = (Element) widgetList.item(0);
			widget = createWidgetFromElement ( root );
		}
		catch ( MalformedURLException e ) {
			throw new WookieConnectorException ( "Get Widget rest URL is incorect: " + url, e );
		} catch (ParserConfigurationException e) {
		    throw new WookieConnectorException("Unable to create XML parser", e);
		} catch (SAXException e) {
		    throw new WookieConnectorException( "Unable to parse the response from Wookie", e);
		}
		return widget;
	}
	
	
	/**
	 * This gets the data of a widget file for the given widget id
	 * @param identifier - the unique id of the widget
	 * @param saveFile - a file or file path to save the file data into.  At the moment the REST api for wookie doesn't return the widget file name
	 * so this must be supplied by the caller here.
	 * @throws WookieConnectorException
	 * @throws IOException
	 */
	public void getWidgetFile ( String identifier, File saveFile ) throws WookieConnectorException, IOException {
		URL url = null;
		
		try {
			url = new URL (conn.getURL() + "/widgets/"+identifier );
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Accept", "application/widget");
			connection.connect();
			
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			
			if ( !saveFile.exists()) {
				saveFile.createNewFile();
			}
			OutputStream out = new BufferedOutputStream ( new FileOutputStream ( saveFile ) );
			byte[] buf = new byte[256];
			int readSize = 0;
			while ((readSize = in.read(buf)) >= 0 ) {
				out.write(buf, 0, readSize );
			}
			out.flush();
			out.close();
		}
		catch ( FileNotFoundException fnf ) {
			throw new WookieConnectorException ( "The file "+saveFile.getAbsolutePath()+" could not be created.", fnf );
		}
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
	
	
	// -----------------------------------------------------------------------------------
	// admin functions requiring basic authentication
	

	/**
	 * Gets a list of all the api keys registered in wookie
	 * @param adminUsername
	 * @param adminPassword
	 * @return
	 * @throws WookieConnectorException
	 * @throws IOException
	 */
	public List<ApiKey> getAPIKeys(String adminUsername, String adminPassword) throws WookieConnectorException, IOException {
		
		URL url;
		ArrayList<ApiKey> keys =  new ArrayList<ApiKey>();
		try {
			url = new URL ( conn.getURL() + "/keys" );
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			addBasicAuthToConnection (httpConnection, adminUsername, adminPassword );
			InputStream is = httpConnection.getInputStream();
			if (httpConnection.getResponseCode() > 200) {
				throw new IOException(httpConnection.getResponseMessage());
			}
			Document doc = parseInputStreamAsDocument ( is );
			Element rootElement = doc.getDocumentElement();
			NodeList keyNodes = rootElement.getElementsByTagName("key");
			for ( int i = 0; i < keyNodes.getLength(); i++ ) {
				Element keyElement = (Element) keyNodes.item(i);
				ApiKey key = new ApiKey ( keyElement.getAttribute("id"),
						keyElement.getAttribute("value"),
						keyElement.getAttribute("email"));
				keys.add(key);
			}
		}
		catch ( MalformedURLException e ) {
			throw new WookieConnectorException ( "Bad url: ", e);
		} 
		catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new WookieConnectorException ( "Problem parsing data returned by Wookie: ", e);
		} 
		catch (SAXException e) {
			e.printStackTrace();
			throw new WookieConnectorException ( "Problem parsing data returned by Wookie: ", e);
		}
		return keys;
	}
	
	
	/**
	 * Creates a new api key
	 * @param newKey
	 * @param adminUsername
	 * @param adminPassword
	 * @throws WookieConnectorException
	 */
	public void createApiKey ( ApiKey newKey, String adminUsername, String adminPassword ) throws WookieConnectorException {
		String paramString = "";
		try{
			paramString += "apikey=";
			paramString += URLEncoder.encode(newKey.getKey(), "UTF-8");
			paramString += "&email=";
			paramString += URLEncoder.encode(newKey.getEmail(), "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new WookieConnectorException ( "Must support UTF-8", e );
		}
		URL url = null;
		try {
			url = new URL ( conn.getURL() + "/keys" );
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			addBasicAuthToConnection(urlConn, adminUsername, adminPassword);
			urlConn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
			wr.write(paramString);
			wr.flush();
			if (urlConn.getResponseCode() > 201) {
				throw new IOException(urlConn.getResponseMessage());
			}
		}
		catch (MalformedURLException e) {
			throw new WookieConnectorException( "ApiKeys rest URL is incorrect: " + url, e);
		}
		catch (IOException e) {
			StringBuilder sb = new StringBuilder( "Problem adding an apikey. ");
			sb.append("URL: ");
			sb.append(url);
			sb.append(" data: ");
			sb.append(paramString);
			throw new WookieConnectorException(sb.toString(), e);
		}
	}
	
	
	/**
	 * Deletes a specified key
	 * @param key
	 * @throws IOException
	 * @throws WookieConnectorException
	 */
	public void removeApiKey ( ApiKey key, String adminUsername, String adminPassword ) throws IOException, WookieConnectorException {
		URL url = null;
		try {
			url = new URL ( conn.getURL() + "/keys/"+key.getId());
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			addBasicAuthToConnection ( urlConn, adminUsername, adminPassword );
			urlConn.setRequestMethod("DELETE");
			urlConn.connect();
			if ( urlConn.getResponseCode() > 201 ) {
				throw new IOException ( urlConn.getResponseMessage());
			}
		}
		catch ( MalformedURLException e ) {
			throw new WookieConnectorException ( "ApiKeys rest URL is incorect: " + url, e );
		}
	}
	
	
	
	/**
	 * Returns a full list of policies
	 * @param adminUsername
	 * @param adminPassword
	 * @param scope - use to define the scope of the search for policies, if empty or null all are returned
	 * @return
	 * @throws IOException
	 * @throws WookieConnectorException
	 */
	public List<Policy> getPolicies( String adminUsername, String adminPassword, String scope ) throws IOException, WookieConnectorException {
		URL url;
		ArrayList<Policy> policies = new ArrayList<Policy>();
		try {
			String urlString = conn.getURL() + "/policies";
			if ( scope != null && scope != "" ) {
				urlString += "/" + URLEncoder.encode(scope, "UTF-8" );
			}
			url = new URL ( urlString );

			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			addBasicAuthToConnection ( urlConn, adminUsername, adminPassword );
			urlConn.setRequestProperty("Accept", "text/xml");
			if (urlConn.getResponseCode() > 200) {
				throw new IOException(urlConn.getResponseMessage());
			}
			Document doc = parseInputStreamAsDocument ( urlConn.getInputStream() );
			Element rootElement = doc.getDocumentElement();
			NodeList policyNodes = rootElement.getElementsByTagName("policy");
			for ( int i = 0; i < policyNodes.getLength(); i++ ) {
				Element keyElement = (Element) policyNodes.item(i);
				Policy policy = new Policy ( keyElement.getAttribute("scope"),
						keyElement.getAttribute("origin"),
						keyElement.getAttribute("directive"));
				policies.add(policy);
			}
		}
		catch ( MalformedURLException e ) {
			throw new WookieConnectorException ( "Bad url: ", e);
		} 
		catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new WookieConnectorException ( "Problem parsing data returned by Wookie: ", e);
		} 
		catch (SAXException e) {
			e.printStackTrace();
			throw new WookieConnectorException ( "Problem parsing data returned by Wookie: ", e);
		}

		return policies;
	}
	
	
	/**
	 * Create a new policy
	 * @param newPolicy
	 * @param adminUsername
	 * @param adminPassword
	 * @throws WookieConnectorException
	 */
	public void createPolicy ( Policy newPolicy, String adminUsername, String adminPassword ) throws WookieConnectorException {
		URL url = null;
		try {
			url = new URL ( conn.getURL() + "/policies" );
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			addBasicAuthToConnection ( urlConn, adminUsername, adminPassword );
			urlConn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
			wr.write(newPolicy.toString());
			wr.flush();
			if (urlConn.getResponseCode() > 201) {
				throw new IOException(urlConn.getResponseMessage());
			}
		}
		catch (MalformedURLException e) {
			throw new WookieConnectorException( "Policies rest URL is incorrect: " + url, e);
		}
		catch (IOException e) {
			StringBuilder sb = new StringBuilder( "Problem adding a policy. ");
			sb.append("URL: ");
			sb.append(url);
			sb.append(" data: ");
			sb.append(newPolicy.toString());
			throw new WookieConnectorException(sb.toString(), e);
		}

	}
	
	/**
	 * Deletes a policy
	 * @param policy
	 * @param adminUsername
	 * @param adminPassword
	 * @throws WookieConnectorException
	 * @throws IOException
	 */
	public void deletePolicy ( Policy policy, String adminUsername, String adminPassword ) throws WookieConnectorException, IOException {
		URL url = null;
		try {
			url = new URL ( conn.getURL() + "/policies/"+URLEncoder.encode(policy.toString(), "UTF-8"));
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			addBasicAuthToConnection ( urlConn, adminUsername, adminPassword );
			urlConn.setRequestMethod("DELETE");
			urlConn.connect();
			if ( urlConn.getResponseCode() > 201 ) {
				throw new IOException ( urlConn.getResponseMessage());
			}
		}
		catch ( MalformedURLException e ) {
			throw new WookieConnectorException ( "Properties rest URL is incorect: " + url, e );
		} 
		catch (UnsupportedEncodingException e) {
			throw new WookieConnectorException ( "Must support UTF-8 encoding", e );
		}
	}
	
	
	
	// -----------------------------------------------------------------------------------
	// private functions
	
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
	
	private Widget createWidgetFromElement ( Element e ) throws MalformedURLException {
		String id = e.getAttribute("id");

		//
		// If there is an "identifier" attribute, this is a 0.9.2 or older
		// server
		//
		if (e.hasAttribute("identifier")) {
			id = e.getAttribute("identifier");
		}

		String width = e.getAttribute("width");
		String height = e.getAttribute("height");
		String version = e.getAttribute("version");

		String name = getNodeTextContent(e, "name");

		//
		// In 0.9.2 and earlier, the widget has a Title rather
		// than a Name
		//
		if (e.getElementsByTagName("title").getLength() > 0) {
			name = getNodeTextContent(e, "title");
		}

		String description = getNodeTextContent(e, "description");
		String license = getNodeTextContent(e, "license");
		String author = getNodeTextContent(e, "author");
		Element iconEl = (Element) e.getElementsByTagName("icon")
				.item(0);
		URL iconURL;
		if (iconEl != null) {
			if (iconEl.hasAttribute("src")) {

				//
				// From 0.10.0 onwards, icon info is in the "src" attribute
				//
				iconURL = new URL(iconEl.getAttribute("src"));
			} else {

				//
				// For 0.9.2, there is no src attribute
				//
				iconURL = new URL(iconEl.getTextContent());
			}

		} else {
			iconURL = new URL("http://www.oss-watch.ac.uk/images/logo2.gif");
		}

		return new Widget(id, name, description, iconURL, width, height,
				version, author, license);
	}

	
	
	private Document getURLDoc(URL url) throws IOException, ParserConfigurationException, SAXException {
		InputStream is = getURLInputStream ( url );
		return parseInputStreamAsDocument ( is );
	}
	
	
	
	private InputStream getURLInputStream ( URL url ) throws IOException {
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		InputStream is = httpConnection.getInputStream();
		if (httpConnection.getResponseCode() > 200) {
			throw new IOException(httpConnection.getResponseMessage());
		}
		return is;
	}
	
	
	
	private Document parseInputStreamAsDocument ( InputStream in ) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docb = dbf.newDocumentBuilder();
		Document parsedDoc = docb.parse(in);
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
	private WidgetInstance parseInstance(String widgetId, Document xml) {
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
	
	
	
	
	private void addBasicAuthToConnection ( HttpURLConnection urlConnection, String username, String password ){
		String authString = username + ":" + password;
		String encodedAuth = encodeBase64String ( authString );
		urlConnection.setRequestProperty("Authorization", "Basic " + encodedAuth );
	}
	
	
	
	
	private String encodeBase64String ( String input ) {
		String charMap = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		String encodedString = "";

		byte[] inputBytes;
		try {
			inputBytes = input.getBytes("UTF-8");
		}
		catch (Exception ignore ) {
			inputBytes = input.getBytes();
		}
		// pad out so we don't get index out of bounds on input when we index at 3 bytes each time
		if ( inputBytes.length % 3 != 0 ) {
			byte[] paddedInput = new byte[inputBytes.length + (inputBytes.length % 3)];
			System.arraycopy(inputBytes, 0, paddedInput, 0, inputBytes.length);
			inputBytes = paddedInput;
		}
		int encodedStringBoundaryIndex = 0;

		for (int i = 0; i < inputBytes.length; i += 3 ) {
			int packed = ((inputBytes[i] & 0xff) << 16) + ((inputBytes[i+1] & 0xff) << 8) + (inputBytes[i+2] & 0xff);
			encodedString = encodedString + charMap.charAt((packed >> 18) & 0x3f) + 
											charMap.charAt((packed >> 12) & 0x3f) + 
											charMap.charAt((packed >> 6) & 0x3f) + 
											charMap.charAt(packed & 0x3f);
			encodedStringBoundaryIndex += 4;
			if ( encodedStringBoundaryIndex == 76 ) {
				// we should do a carriage return and line feed after 76 bytes for some reason
				encodedString += "\r\n";
				encodedStringBoundaryIndex = 0;
			}
		}
		return encodedString;
	}
}
