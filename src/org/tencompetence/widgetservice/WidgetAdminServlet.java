package org.tencompetence.widgetservice;
/**
 * TODO - Header
 */
import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.beans.WidgetDefault;
import org.tencompetence.widgetservice.manager.WidgetAdminManager;

/**
 * Servlet implementation class for Servlet: WidgetAdminServlet
 * 
 * This servlet handles all requests for Admin tasks
 * 
 * @author Paul Sharples
 * @version $Id: WidgetAdminServlet.java,v 1.1 2007-10-14 10:58:15 ps3com Exp $ 
 *
 */
public class WidgetAdminServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	 	 	
	private static final long serialVersionUID = -3026022301561798524L;	
	// Get the logger
	static Logger _logger = Logger.getLogger(WidgetAdminServlet.class.getName());
	// jsp page handles
	private static final String _mainPage = "/admin/index.jsp";
	private static final String _messagePage = "/admin/message.jsp";
	private static final String _addPage = "/admin/addnew.jsp";
	private static final String _listWidgetsPage = "/admin/listall.jsp";		
	// our list of allowed operations
	private enum Operation {LIST, ADD, SET_DEFAULT, UPDATE, DELETE, RETRIEVE};	
	
    /*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public WidgetAdminServlet() {
		super();
		
		//_manager = new WidgetAdminManager();
		/**
		 * TODO - Need to get the proper url here from the servlet 
		 */		
		//_manager.setUpDefaultData(_manager, "http://localhost:8080/wookie/services/default/");
	}   	
	
	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WidgetAdminManager manager = new WidgetAdminManager();
		Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties");
		HttpSession session = request.getSession(true);
		session.setAttribute("error_value", null);
		session.setAttribute("message_value", null);
		
		String task = request.getParameter("operation");
		Operation op=null;
		// sanity check...		
		if (task != null) {
			// if this is called with a string not contained within the enumerated list an exception is thrown
			try {op = Operation.valueOf(task);} 
			catch (IllegalArgumentException e) {
				op=null;
				session.setAttribute("error_value", "No such operation allowed");// need to i18n this
			}
		}	
		if(op!=null){
			switch (op) {
				case LIST: {
					listOperation(session, manager);						
					doForward(request, response, _listWidgetsPage);
					break;
				}
				case RETRIEVE: {
					retrieveServices(session, properties);
					doForward(request, response, _addPage);						
					break;
				}
				case ADD: {
					addOperation(session, request, manager);
					doForward(request, response, _messagePage);
					break;
				}
				case SET_DEFAULT:
					setDefaultWidgetOperation(session, request, manager);
					doForward(request, response, _listWidgetsPage);
					break;
					// not sure this can be reached?
				default: {
					session.setAttribute("error_value", "No operation could be ascertained");// need to i18n this
					doForward(request, response, _mainPage);
				}
			}						
		} 
		else {
			doForward(request, response, _mainPage);
		}
	}  	
	
	
	private void retrieveServices(HttpSession session, Configuration properties){
		// get em all
		session.setAttribute("services", properties.getStringArray("widget.service"));					
	}
	
	private void listOperation(HttpSession session, WidgetAdminManager manager){
		// list em all
		int count = 0;
		session.setAttribute("widgets", manager.getAllWidgets());				
		Hashtable<String, Integer> defaultHash = new Hashtable<String, Integer>();
		for(WidgetDefault defaultWidget :manager.getAllDefaultWidgets()){
			defaultHash.put(defaultWidget.getWidgetContext(), defaultWidget.getWidgetId());
			count++;
		}		
		session.setAttribute("widget_defaults", defaultHash);
	}
	
	private void addOperation(HttpSession session, HttpServletRequest request, WidgetAdminManager manager) throws IOException{
	    String output = "";
	    String description = request.getParameter("description");
	    String url = request.getParameter("url");
	    String[] widgetTypes = request.getParameterValues("widgetTypes");
	    String height = request.getParameter("height");
	    String width = request.getParameter("width");	    	    
	    output+="<br>description:"+description+"<br>";
	    output+="<br>url:"+url+"<br>";
	    output+="<br>height:"+height+"<br>";
	    output+="<br>width:"+width+"<br>";	  
	    manager.addNewWidget(description, url, Integer.parseInt(height), Integer.parseInt(width), widgetTypes);
		session.setAttribute("message_value", "Widget was successfully added"); 
		session.setAttribute("message_value", output);
	}
	
	private void setDefaultWidgetOperation(HttpSession session, HttpServletRequest request, WidgetAdminManager manager){
		String widgetId = request.getParameter("widgetId");
		String widgetType = request.getParameter("widgetType");		
		manager.setDefaultWidget(Integer.parseInt(widgetId), widgetType);
		listOperation(session, manager);
	}

	
	
	private void doForward(HttpServletRequest request, HttpServletResponse response, String jsp) throws ServletException, IOException{
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jsp);
		dispatcher.forward(request, response);
	}
	
	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}  
	

	
	
}
 