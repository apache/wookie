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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Implements the core API capabilities
 */
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
		
		SignedApiRequest request = SignedApiRequest.POST(conn.getURL()+"/properties", conn.getApiKey(), conn.getSecret());
		createInstanceParams(request, instance.id);
		
		String json = "{";

		if ( is_public ) {
			json += "\"shareddata\":[{\"name\":\""+fName+"\", \"value\":\""+fValue+"\"}]";
		}
		else {
			json += "\"preferences\":[{\"name\":\""+fName+"\", \"value\":\""+fValue+"\"}]";
		}
		json += "}";
		
		request.setRequestEntity(json);
		request.execute();

		if (request.getStatusCode() > 201) {
			throw new IOException("Error POSTing to /properties");
		}
	}


	public String getPropertyForInstance ( WidgetInstance instance, String propertyName ) throws WookieConnectorException, IOException {
		
		SignedApiRequest request = SignedApiRequest.GET(conn.getURL()+"/properties", conn.getApiKey(), conn.getSecret());
		createInstanceParams(request, instance.id);
		
		request.addParameter("propertyname", propertyName);
		
		request.execute();
		
		if (request.getStatusCode() == 200){
			return request.getResponseBodyAsString();
		}
		if (request.getStatusCode() == 404){
			return null;
		}
		throw new RuntimeException("Problem GETting property from /properties");
	}


	public void updatePropertyForInstance (WidgetInstance instance, boolean is_public, String name, String value ) throws WookieConnectorException, IOException {
		
		SignedApiRequest request = SignedApiRequest.PUT(conn.getURL()+"/properties", conn.getApiKey(), conn.getSecret());
		createInstanceParams(request, instance.id);
		
		String json = "{";

		if ( is_public ) {
			json += "\"shareddata\":[{\"name\":\""+name+"\", \"value\":\""+value+"\"}]";
		}
		else {
			json += "\"preferences\":[{\"name\":\""+name+"\", \"value\":\""+value+"\"}]";
		}
		json += "}";
		
		request.setRequestEntity(json);

		request.execute();

		if (request.getStatusCode() != 200) throw new RuntimeException("Problem PUTting to /properties");
	}



	public void deletePropertyForInstance ( WidgetInstance instance, boolean is_public, String propertyName ) throws WookieConnectorException, IOException {
		
		SignedApiRequest request = SignedApiRequest.DELETE(conn.getURL()+"/properties", conn.getApiKey(), conn.getSecret());
		createInstanceParams(request, instance.id);
		
		if ( is_public ) {
			request.addParameter("is_public", "true");
		}
		else {
			request.addParameter("is_public", "false");
		}
		request.addParameter("propertyname", propertyName);		
		
		request.execute();
		
		if (request.getStatusCode() != 200) throw new RuntimeException("Problem DELETEing from /properties");
	}

	public WidgetInstance getOrCreateInstance(String guid) throws IOException,
	WookieConnectorException {
		
		SignedApiRequest request = SignedApiRequest.POST(conn.getURL()+"/widgetinstances", conn.getApiKey(), conn.getSecret());
		createInstanceParams(request, guid);

		//
		// If the user has a preferred locale, add as a parameter to the request
		//
		if (getCurrentUser().getLocale()!= null){
			request.addParameter("locale", getCurrentUser().getLocale());
		}

		request.execute();

		if (request.getStatusCode() != 200 && request.getStatusCode() != 201) {
			throw new IOException("Problem POSTing to /widgetinstances "+request.getStatusCode());
		}
		
		WidgetInstance instance;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docb = dbf.newDocumentBuilder();
			Document parsedDoc = docb.parse(request.getResponseBodyAsStream());
			instance = parseInstance(guid, parsedDoc);
			instances.put(instance);
			addParticipant(instance, getCurrentUser());
			return instance;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Problem POSTing to /widgetinstances");
		}
	}


	/**
	 */
	public void addParticipant(WidgetInstance widget, User user)
	throws WookieConnectorException {

		SignedApiRequest request = SignedApiRequest.POST(conn.getURL()+"/participants", conn.getApiKey(), conn.getSecret());
		createInstanceParams(request, widget.getId());
		
		request.addParameter("participant_id", user.getLoginName());
		request.addParameter("participant_display_name", user.getScreenName());	
		
		String thumbnail = "";
		if (user.getThumbnailUrl() != null && user.getThumbnailUrl().trim().length() > 0){
			thumbnail = user.getThumbnailUrl();
		}
		request.addParameter("participant_thumbnail_url", thumbnail);

		String role = "";
		if (user.getRole() != null && user.getRole().trim().length() > 0){
			role = user.getRole();
		}
		request.addParameter("participant_role", role);

		try {
			request.execute();
		} catch (IOException e) {
			throw new WookieConnectorException("Problem POSTing to /participants", e);
		}

		if (request.getStatusCode() > 201){
			throw new RuntimeException("Problem POSTing to /participants: "+request.getStatusCode());
		}
	}



	/**
	 * Removes a user as a participant from a particular widget instance
	 * @param instance
	 * @param user
	 * @throws WookieConnectorException
	 */
	public void removeParticipantFromWidget ( WidgetInstance instance, User user ) throws WookieConnectorException {

		SignedApiRequest request = SignedApiRequest.DELETE(conn.getURL()+"/participants", conn.getApiKey(), conn.getSecret());
		createInstanceParams(request, instance.getId());
		
		request.addParameter("participant_id", user.getLoginName());
		
		try {
			request.execute();
		} catch (IOException e) {
			throw new WookieConnectorException("Problem POSTing to /participants", e);

		}	
		if (request.getStatusCode() != 200){
			throw new RuntimeException("Problem DELETEing from /participants");
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
			SignedApiRequest request = SignedApiRequest.GET(conn.getURL()+"/widgets", conn.getApiKey(), conn.getSecret());
			request.execute();
			Document widgetsDoc = parseInputStreamAsDocument(request.getResponseBodyAsStream());

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

	public String normalizeFileName(URL urlPath){
		String filename;
		String[] parts = urlPath.getFile().split("/");
		if(parts[parts.length-1].length() < 1){
			filename = "unknown.wgt";
		}else{
			filename = parts[parts.length-1];
		}
		if(filename.indexOf('.') == -1){
			filename = filename + ".wgt"; 
		}
		else{
			if(!filename.endsWith(".wgt")){
				String[] split = filename.split("\\.");
				filename = split[0] + ".wgt";
			}
		}
		return filename;
	}

	public Widget postWidget(String widgetStrUrl ) throws WookieConnectorException {
		Widget publishedWidget = null;
		try {
			URL widgetUrl = new URL(widgetStrUrl);
			String tempUploadFolder = System.getProperty("java.io.tmpdir");
			String filename = normalizeFileName(widgetUrl);
			File tempWgtFile = new File(tempUploadFolder, filename);
			FileUtils.copyURLToFile(widgetUrl, tempWgtFile, 10000, 10000); // 10 second timeouts
			publishedWidget = postWidget(tempWgtFile);
			// cleanup temp file
			if(tempWgtFile.exists()){
				tempWgtFile.delete();
			}
		} catch (MalformedURLException e) {
			throw new WookieConnectorException("Malformed url error.", e);
		} catch (IOException e) {
			throw new WookieConnectorException("I/O error. Problem downloading widget from given URL", e);
		}
		return publishedWidget;
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
	public Widget postWidget(File widgetFile) throws WookieConnectorException {
		
		SignedApiRequest request = SignedApiRequest.POST(conn.getURL()+"/widgets", conn.getApiKey(), conn.getSecret());
		request.setFile(new File(widgetFile.getAbsolutePath()));
		
		try {
			request.execute();
		} catch (IOException e) {
			throw new WookieConnectorException("Problem POSTing to /widgets",e);
		}
		
		if (request.getStatusCode() != 201 && request.getStatusCode() != 200){
			throw new WookieConnectorException ( "Widget file was not uploaded successfully.", null);
		}
		
		try {
			Document doc = parseInputStreamAsDocument(request.getResponseBodyAsStream());
			Element root = doc.getDocumentElement();
			//NodeList widgetList = root.getElementsByTagName("widget");
			//Element widgetEl = (Element) widgetList.item(0);
			Widget widget = createWidgetFromElement ( root );
			return widget;
		} catch (Exception e) {
			throw new WookieConnectorException ( "Widget file was not uploaded successfully.", e);
		}

	}


	public Widget updateWidget ( File widgetFile, String widgetIdentifier) throws WookieConnectorException {
		
		SignedApiRequest request = SignedApiRequest.PUT(conn.getURL()+"/widgets/"+widgetIdentifier, conn.getApiKey(), conn.getSecret());
		request.setFile(new File(widgetFile.getAbsolutePath()));
		
		try {
			request.execute();
		} catch (IOException e) {
			throw new WookieConnectorException("Problem PUTting to /widgets",e);
		}
		
		if (request.getStatusCode() != 200){
			throw new WookieConnectorException ( "Widget file was not uploaded successfully.", null);
		}
		
		try {
			Document doc = parseInputStreamAsDocument(request.getResponseBodyAsStream());
			Element root = doc.getDocumentElement();
			//NodeList widgetList = root.getElementsByTagName("widget");
			//Element widgetEl = (Element) widgetList.item(0);
			Widget widget = createWidgetFromElement ( root );
			return widget;
		} catch (Exception e) {
			throw new WookieConnectorException ( "Widget file was not uploaded successfully.", e);
		}
	}



	public void deleteWidget ( String identifier ) throws IOException, WookieConnectorException
	{
		SignedApiRequest request = SignedApiRequest.DELETE(conn.getURL()+"/widgets/"+identifier, conn.getApiKey(), conn.getSecret());
		request.execute();
		
		if (request.getStatusCode() != 200){
			throw new IOException("Problem DELETEing from /widgets "+request.getStatusCode());
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
		SignedApiRequest request = SignedApiRequest.GET(conn.getURL()+"/widgets/"+identifier, conn.getApiKey(), conn.getSecret());
		request.execute();
		if (request.getStatusCode() == 404){
			return null;
		}
		Document doc;
		try {
			doc = parseInputStreamAsDocument ( request.getResponseBodyAsStream());
		} catch (Exception e) {
			throw new WookieConnectorException("Problem GETting /widget", e);
		} 
		Element root = doc.getDocumentElement();
		Widget widget = createWidgetFromElement ( root );
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
		SignedApiRequest request = SignedApiRequest.GET(conn.getURL()+"/widgets/"+identifier, conn.getApiKey(), conn.getSecret());
		request.setAccepts("application/widget");
		request.execute();

		BufferedInputStream in = new BufferedInputStream(request.getResponseBodyAsStream());

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
	
		try {	
			
			SignedApiRequest request = SignedApiRequest.GET(conn.getURL()+"/participants", conn.getApiKey(), conn.getSecret());
			createInstanceParams(request, instance.id);
			request.execute();
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docb = dbf.newDocumentBuilder();
			Document usersDoc = docb.parse(request.getResponseBodyAsStream());
			
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
				String role = participantEl.getAttribute("role");
				User user = new User(id, name, thumbnail, role);
				users[idx] = user;
			}
			return users;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new WookieConnectorException(
					"Problem GETting participants", e);
		}
	}


	/**
	 * Gets a list of all the api keys registered in wookie
	 * @param adminUsername
	 * @param adminPassword
	 * @return
	 * @throws WookieConnectorException
	 * @throws IOException
	 */
	public List<ApiKey> getAPIKeys() throws WookieConnectorException, IOException {

		SignedApiRequest request = SignedApiRequest.GET(conn.getURL()+"/keys", conn.getApiKey(), conn.getSecret());
		request.execute();

		ArrayList<ApiKey> keys =  new ArrayList<ApiKey>();
		try {
			Document doc = parseInputStreamAsDocument ( request.getResponseBodyAsStream() );
			Element rootElement = doc.getDocumentElement();
			NodeList keyNodes = rootElement.getElementsByTagName("key");
			for ( int i = 0; i < keyNodes.getLength(); i++ ) {
				Element keyElement = (Element) keyNodes.item(i);
				ApiKey key = new ApiKey (
						keyElement.getTextContent(),
						null);
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
	public void createApiKey ( ApiKey newKey) throws WookieConnectorException {
		
		SignedApiRequest request = SignedApiRequest.POST(conn.getURL()+"/keys", conn.getApiKey(), conn.getSecret());
		request.addParameter("apikey", newKey.getKey());
		request.addParameter("email", newKey.getSecret());
		try {
			request.execute();
		} catch (IOException e) {
			throw new WookieConnectorException("Problem adding an apikey.", e);
		}
		if (request.getStatusCode() != 201){
			throw new WookieConnectorException("Problem adding an apikey. ", new IOException("Error:"+request.getStatusCode()));
		}
	}


	/**
	 * Deletes a specified key
	 * @param key
	 * @throws IOException
	 * @throws WookieConnectorException
	 */
	public void removeApiKey ( ApiKey key) throws IOException, WookieConnectorException {
		SignedApiRequest request = SignedApiRequest.DELETE(conn.getURL()+"/keys/"+key.getKey(), conn.getApiKey(), conn.getSecret());
		request.execute();
			if ( request.getStatusCode() != 200 ) {
				throw new IOException ("Problem DELETEing from /keys");
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
	public List<Policy> getPolicies( String scope ) throws IOException, WookieConnectorException {
		
		String url = conn.getURL()+ "/policies";
		if (scope != null){
			url += "/" + scope + "";
			System.out.println(url);
		}
		
		SignedApiRequest request = SignedApiRequest.GET(url, conn.getApiKey(), conn.getSecret());
		request.setAccepts("text/xml");
		request.execute();
				
		ArrayList<Policy> policies = new ArrayList<Policy>();
		
		if (request.getResponseBodyAsStream() == null){
			return policies;
		}
		try {
			Document doc = parseInputStreamAsDocument ( request.getResponseBodyAsStream() );
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
	public void createPolicy ( Policy newPolicy ) throws WookieConnectorException {
		
		SignedApiRequest request = SignedApiRequest.POST(conn.getURL()+"/policies", conn.getApiKey(), conn.getSecret());
		request.setRequestEntity(newPolicy.toString());
		try {
			request.execute();
		} catch (IOException e1) {
			throw new WookieConnectorException("Problem POSTing to /policies", e1);
		}
		
		if (request.getStatusCode() != 201){
		//	throw new WookieConnectorException("Problem POSTing to /policies", new IOException("Error:"+request.getStatusCode()));
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
	public void deletePolicy ( Policy policy ) throws WookieConnectorException, IOException {
		SignedApiRequest request = SignedApiRequest.DELETE(conn.getURL()+"/policies/"+policy.toString(), conn.getApiKey(), conn.getSecret());
		request.execute();
		if (request.getStatusCode() != 200){
			throw new WookieConnectorException("Problem DELETEing from /policies", new IOException("Error:"+request.getStatusCode()));
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

	private Document parseInputStreamAsDocument ( InputStream in ) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docb = dbf.newDocumentBuilder();
		Document parsedDoc = docb.parse(in);
		return parsedDoc;
	}



	private void createInstanceParams ( SignedApiRequest request, String widgetId ) throws WookieConnectorException {
		request.addParameter("api_key", conn.getApiKey());
		request.addParameter("shareddatakey", conn.getSharedDataKey());
		request.addParameter("userid",getCurrentUser().getLoginName());
		request.addParameter("widgetid", widgetId);
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
}
