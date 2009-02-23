package org.tencompetence.widgetservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetDefault;
import org.tencompetence.widgetservice.manager.IWidgetAdminManager;
import org.tencompetence.widgetservice.manager.impl.WidgetAdminManager;

public class WidgetWebMenuServlet extends HttpServlet implements Servlet {
	
	// our list of allowed operations
	private enum Operation {		
		LISTWIDGETS, INSTANTIATE
	}	
	 	 	
	// Get the logger
	static Logger _logger = Logger.getLogger(WidgetWebMenuServlet.class.getName());
	
	// jsp page handles
	private static final String fMainPage = "/webmenu/index.jsp";
	private static final String fListWidgetsPage = "/webmenu/listall.jsp";
	private static final String fInstantiateWidgetsPage = "/webmenu/instantiate.jsp";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IWidgetAdminManager manager = new WidgetAdminManager();
		Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties");
		HttpSession session = request.getSession(true);
		session.setAttribute("error_value", null);
		session.setAttribute("message_value", null);
		session.setAttribute("widget_defaults", null);
		session.setAttribute("widgets", null);
		session.setAttribute("version", properties.getString("widget.version"));
		String task = request.getParameter("operation");
		Operation op=null;
		// sanity check...		
		if (task != null) {
			// if this is called with a string not contained within the enumerated list an exception is thrown
			try {op = Operation.valueOf(task);} 
			catch (IllegalArgumentException e) {
				op=null;
				session.setAttribute("error_value", "No such operation allowed");
			}
		}	
		if(op!=null){
			switch (op) {
				case LISTWIDGETS: {
					listOperation(session, manager);	
					doForward(request, response, fListWidgetsPage);						
					break;
				}
				case INSTANTIATE: {
					instantiateOperation(session, manager);	
					doForward(request, response, fInstantiateWidgetsPage);						
					break;
				}
				default: {
					session.setAttribute("error_value", "No operation could be ascertained");// need to i18n this
					doForward(request, response, fMainPage);
				}
			}						
		} 
		else {
			doForward(request, response, fMainPage);
		}
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
	
	private void instantiateOperation(HttpSession session, IWidgetAdminManager manager){
		WidgetDefault[] def = manager.getAllDefaultWidgets();
		session.setAttribute("defaults", def);
	}
	
	private void listOperation(HttpSession session, IWidgetAdminManager manager){
		Hashtable<String, Widget> widgetsHash = new Hashtable<String, Widget>();		
		for(WidgetDefault defaultWidget : manager.getAllDefaultWidgets()){
			widgetsHash.put(defaultWidget.getWidgetContext(), manager.getWidget(defaultWidget.getWidgetId()));
		}	
		session.setAttribute("widgetsHash", widgetsHash);
	}

	/**
	 * Forward to correct jsp page
	 */
	private void doForward(HttpServletRequest request, HttpServletResponse response, String jsp) throws ServletException, IOException{
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jsp);
		dispatcher.forward(request, response);
	}
}
