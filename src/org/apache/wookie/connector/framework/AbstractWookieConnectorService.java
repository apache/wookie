package org.apache.wookie.connector.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

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

public abstract class AbstractWookieConnectorService implements
    IWookieConnectorService {
  private static final Logger logger = LoggerFactory.getLogger(AbstractWookieConnectorService.class);
  WookieServerConnection conn;
  WidgetInstances instances = new WidgetInstances();
  
  public AbstractWookieConnectorService(String url, String apiKey, String sharedDataKey) throws WookieConnectorException {
    setConnection(new WookieServerConnection(url, apiKey, sharedDataKey));
  }
  
  /**
   * Creates a WookieConnectorService that has not yet been initialised to connect
   * to a specific server.
   */
  protected AbstractWookieConnectorService() {
    super();
  }
  
  public void setConnection(WookieServerConnection newConn) {
    logger.debug("Setting wookie connection to " + conn);
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
  public WidgetInstance getOrCreateInstance(Widget widget) throws IOException,
      WookieConnectorException {
    URL url;
    WidgetInstance instance;
    try {
      StringBuilder postdata = new StringBuilder("api_key=");
      postdata.append(URLEncoder.encode(getConnection().getApiKey(), "UTF-8"));
      postdata.append("&shareddatakey=");
      postdata.append(URLEncoder.encode(getConnection().getSharedDataKey(), "UTF-8"));
      postdata.append("&userid=");
      postdata.append(URLEncoder.encode(getCurrentUser().getLoginName(), "UTF-8"));
      postdata.append("&widgetid=");
      postdata.append(URLEncoder.encode(widget.getIdentifier(), "UTF-8"));
      
      logger.debug("Makeing Wookie REST query using: " + postdata);
      
      url = new URL(conn.getURL() + "/widgetinstances");
      URLConnection conn = url.openConnection();
      conn.setDoOutput(true);
      OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
      wr.write(postdata.toString());
      wr.flush();

      InputStream is = conn.getInputStream();

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      instance = widget.addInstance(db.parse(is));
      
      instances.put(instance);

      wr.close();
      is.close();
      
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
   * @refactor At time of writing the REST API for adding a participant is broken so we are
   * using the non-REST approach. The code for REST API is commented out and should be used
   * in the future.
   */
  public void addParticipant(WidgetInstance widget, User user) throws WookieConnectorException {
    /* 
     * REST API approach - REST API is broken at time of writing
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
    } catch (UnsupportedEncodingException e) {
      throw new WookieConnectorException("Must support UTF-8 encoding", e);
    }
    
    URL url = null;
    try {
      url = new URL(conn.getURL() + "/participants");
      URLConnection conn = url.openConnection();
      conn.setDoOutput(true);
      OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
      wr.write(postdata.toString());
      wr.flush();
  
      InputStream is = conn.getInputStream();
  
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db;
      db = dbf.newDocumentBuilder();
      Document xml = db.parse(is);
    } catch (MalformedURLException e) {
      throw new WookieConnectorException("Participants rest URL is incorrect: " + url, e);
    } catch (IOException e) {
      StringBuilder sb = new StringBuilder("Problem adding a participant. ");
      sb.append("URL: ");
      sb.append(url);
      sb.append(" data: ");
      sb.append(postdata);
      throw new WookieConnectorException(sb.toString(), e);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException("Unable to configure XML parser", e);
    } catch (SAXException e) {
      throw new RuntimeException("Problem parsing XML from Wookie Server", e);
    }
    */
    
    StringBuilder url;
    try {
      url = new StringBuilder(conn.getURL());
      url.append("/WidgetServiceServlet?");
      url.append("requestid=addparticipant");
      url.append("&api_key=");
      url.append(URLEncoder.encode(getConnection().getApiKey(), "UTF-8"));
      url.append("&shareddatakey=");
      url.append(URLEncoder.encode(getConnection().getSharedDataKey(), "UTF-8"));
      url.append("&userid=");
      url.append(URLEncoder.encode(getCurrentUser().getLoginName(), "UTF-8"));
      url.append("&widgetid=");
      url.append(URLEncoder.encode(widget.getId(), "UTF-8"));
      url.append("&participant_id=");
      url.append(URLEncoder.encode(user.getLoginName(), "UTF-8"));
      url.append("&participant_display_name=");
      url.append(URLEncoder.encode(user.getScreenName(), "UTF-8"));
      url.append("&participant_thumbnail_url=");
      url.append(URLEncoder.encode(user.getThumbnailUrl(), "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new WookieConnectorException("Must support UTF-8 encoding", e);
    }
    
    try {
      HttpURLConnection conn = (HttpURLConnection)new URL(url.toString()).openConnection();
      conn.disconnect();
    } catch (MalformedURLException e) {
      throw new WookieConnectorException("Participants rest URL is incorrect: " + url, e);
    } catch (IOException e) {
      StringBuilder sb = new StringBuilder("Problem adding a participant. ");
      sb.append("URL: ");
      sb.append(url);
      throw new WookieConnectorException(sb.toString(), e);
    }
  }
  
  /**
   * Get a set of all the available widgets in the server. If there is an error
   * communicating with the server return an empty set, or the set received so
   * far in order to allow the application to proceed. The application should
   * display an appropriate message in this case.
   * 
   * @return
   * @throws SimalException
   */
  public HashMap<String, Widget> getAvailableWidgets()
      throws WookieConnectorException {
    HashMap<String, Widget> widgets = new HashMap<String, Widget>();
    try {
      InputStream is = new URL(conn.getURL() + "/widgets?all=true").openStream();
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document widgetsDoc = db.parse(is);

      Element root = widgetsDoc.getDocumentElement();
      NodeList widgetList = root.getElementsByTagName("widget");
      for (int idx = 0; idx < widgetList.getLength(); idx = idx + 1) {
        Element widgetEl = (Element) widgetList.item(idx);
        String id = widgetEl.getAttribute("identifier");
        if (widgets.containsKey(id)) {
          break;
        }
        String title = widgetEl.getElementsByTagName("title").item(0).getTextContent();
        String description = widgetEl.getElementsByTagName("description").item(0).getTextContent();
        Node iconEl = widgetEl.getElementsByTagName("icon").item(0);
        URL iconURL;
        if (iconEl != null) {
          iconURL = new URL(iconEl.getTextContent());
        } else {
          iconURL = new URL("http://www.oss-watch.ac.uk/images/logo2.gif");
        }
        Widget widget = new Widget(id, title, description, iconURL);
        widgets.put(id, widget);
      }
    } catch (ParserConfigurationException e) {
      throw new WookieConnectorException("Unable to create XML parser", e);
    } catch (MalformedURLException e) {
      throw new WookieConnectorException("URL for Wookie is malformed", e);
    } catch (IOException e) {
      // return an empty set, or the set received so far in order to allow
      // the application to proceed. The application should display an
      // appropriate message in this case.
      return widgets;
    } catch (SAXException e) {
      throw new WookieConnectorException(
          "Unable to parse the response from Wookie", e);
    }
    return widgets;
  }
  
  public WidgetInstances getInstances() {
    return instances;
  }

  
}
