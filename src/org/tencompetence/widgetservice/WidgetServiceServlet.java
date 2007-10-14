package org.tencompetence.widgetservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.exceptions.InvalidWidgetCallException;
import org.tencompetence.widgetservice.exceptions.SystemUnavailableException;
import org.tencompetence.widgetservice.exceptions.WidgetTypeNotSupportedException;
import org.tencompetence.widgetservice.manager.WidgetServiceManager;
import org.tencompetence.widgetservice.util.HashGenerator;
import org.tencompetence.widgetservice.util.RandomGUID;

/**
 * Servlet implementation class for Servlet: WidgetService
 *
 */
 public class WidgetServiceServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    
	private static final long serialVersionUID = 308590474406800659L;
		
	static Logger _logger = Logger.getLogger(WidgetServiceServlet.class.getName());
	
	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	private static final String CONTENT_TYPE = "text/xml;charset=\"UTF-8\""; 
	
	private static URL urlWidgetProxyServer = null;
	private static URL urlWidgetAPIServer = null;
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public WidgetServiceServlet() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// set the proxy url.
		if(urlWidgetProxyServer==null){
		urlWidgetProxyServer = new URL(request.getScheme() ,
				request.getServerName() ,
				request.getServerPort() , "/wookie/proxy");
		}
		_logger.debug(urlWidgetProxyServer.toString());
		//set the service url.
		if(urlWidgetAPIServer==null){
			urlWidgetAPIServer = new URL(request.getScheme() ,
					request.getServerName() ,
					request.getServerPort() , "/wookie/dwr/interface/widget.js");
			}
		_logger.debug(urlWidgetAPIServer.toString());
		
		WidgetInstance widgetInstance;
		
		String userId = request.getParameter("userid");
		String runId = request.getParameter("runid");
		String envId = request.getParameter("environmentid");
		String serviceId = request.getParameter("serviceid");
		String serviceType = request.getParameter("servicetype");
		
		try {						
			if(userId==null || runId==null || envId==null || serviceId==null || serviceType==null){
				throw new InvalidWidgetCallException();
			}
		} 
		catch (InvalidWidgetCallException ex) {
			_logger.debug("InvalidWidgetCallException:"+ex.getMessage());				
			response.setContentType(CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			out.println(returnErrorDoc(ex.getMessage()));
		}
				
		WidgetServiceManager wsm = new WidgetServiceManager();	
		widgetInstance = wsm.getwidgetInstance(userId, runId, envId, serviceId, serviceType);
		
		if(widgetInstance!=null){
			// generate a key, url etc
			//doForward(request, response, _okPage);
			formatReturnDoc(request, response, widgetInstance);
		}
		else{
			try {
				// does this type of widget exist?
				Widget widget = wsm.getDefaultWidgetByType(serviceType);
				// generate a nonce
				String nonce = RandomGUID.getUniqueID("nonce-");				

				// now use SHA hash on the nonce				
				String hashKey = HashGenerator.getInstance().encrypt(nonce);	
				
				// get rid of any chars that might upset a url...
				hashKey = hashKey.replaceAll("=", ".eq.");
				hashKey = hashKey.replaceAll("\\?", ".qu.");
				hashKey = hashKey.replaceAll("&", ".am.");
				hashKey = hashKey.replaceAll("\\+", ".pl.");
				
				widgetInstance = wsm.addNewWidgetInstance(userId, runId, envId, serviceId, widget, nonce, hashKey);
				_logger.debug("new widgetinstance added");
				formatReturnDoc(request, response, widgetInstance);
			} 
			catch (WidgetTypeNotSupportedException ex) {
				// TODO - widget not supported	
				// Here we will return a key to a holding page - ie no widget found
				_logger.debug("WidgetTypeNotSupportedException:"+ex.getMessage());				
				response.setContentType(CONTENT_TYPE);
				PrintWriter out = response.getWriter();
				out.println(returnErrorDoc(ex.getMessage()));
			}
			catch (SystemUnavailableException ex) {
				_logger.debug("System Unavailable:"+ex.getMessage());				
				response.setContentType(CONTENT_TYPE);
				PrintWriter out = response.getWriter();				
				out.println(returnErrorDoc(ex.getMessage()));
			}
		}
	}  	
	
	private void formatReturnDoc(HttpServletRequest request, HttpServletResponse response, WidgetInstance widgetInstance) throws IOException{
		URL urlWidget =  new URL(request.getScheme() ,
				request.getServerName() ,
				request.getServerPort() , widgetInstance.getWidget().getUrl());
		
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.println(XMLDECLARATION);			
		out.println("<widgetdata>");					
		out.print("<url>");
		out.print(urlWidget + "?idkey=" + widgetInstance.getIdKey() 
				+ "&amp;url=" + urlWidgetAPIServer.toExternalForm()  
				+ "&amp;proxy=" + urlWidgetProxyServer.toExternalForm() 
		);
		out.println("</url>");
		out.println("<height>"+widgetInstance.getWidget().getHeight()+"</height>");
		out.println("<width>"+widgetInstance.getWidget().getWidth()+"</width>");
		out.println("<maximize>"+widgetInstance.getWidget().isMaximize()+"</maximize>");
		out.println("</widgetdata>");
	}
	
	private String returnErrorDoc(String message){
		StringBuffer envelope = new StringBuffer();	
		envelope.append(XMLDECLARATION);					
		envelope.append("<error>");
		envelope.append(message);
		envelope.append("</error>");
		_logger.debug("Call to getWidget failed:" + message);
		return envelope.toString();
	}	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}   

}