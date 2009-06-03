package org.tencompetence.widgetservice;

import java.io.IOException;
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
import org.tencompetence.widgetservice.manager.impl.WidgetKeyManager;
import org.tencompetence.widgetservice.server.LocaleHandler;

/**
 * WidgetWebMenuServlet
 *
 * @author Paul Sharples
 * @version $Id: WidgetWebMenuServlet.java,v 1.6 2009-06-03 15:46:30 scottwilson Exp $
 */
public class WidgetWebMenuServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;

	// our list of allowed operations
	private enum Operation {
		LISTWIDGETS, INDEX, INSTANTIATE, REQUESTAPIKEY,
	}

	// Get the logger
	static Logger _logger = Logger.getLogger(WidgetWebMenuServlet.class.getName());

	// jsp page handles
	private static final String fMainPage = "/webmenu/index.jsp"; //$NON-NLS-1$
	private static final String fListWidgetsPage = "/webmenu/listall.jsp"; //$NON-NLS-1$
	private static final String fInstantiateWidgetsPage = "/webmenu/instantiate.jsp"; //$NON-NLS-1$
	private static final String fRequestApiKeyPage = "/webmenu/requestapikey.jsp"; //$NON-NLS-1$

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetAdminManager manager = (IWidgetAdminManager)session.getAttribute(WidgetAdminManager.class.getName());
		if(manager == null){
			manager = new WidgetAdminManager(localizedMessages);
			session.setAttribute(WidgetAdminManager.class.getName(), manager);
		}
		Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties"); //$NON-NLS-1$

		session.setAttribute("error_value", null); //$NON-NLS-1$
		session.setAttribute("message_value", null); //$NON-NLS-1$
		session.setAttribute("widget_defaults", null); //$NON-NLS-1$
		session.setAttribute("widgets", null); //$NON-NLS-1$
		session.setAttribute("version", properties.getString("widget.version")); //$NON-NLS-1$ //$NON-NLS-2$
		String task = request.getParameter("operation"); //$NON-NLS-1$
		Operation op=null;
		// sanity check...
		if (task != null) {
			// if this is called with a string not contained within the enumerated list an exception is thrown
			try {op = Operation.valueOf(task);}
			catch (IllegalArgumentException e) {
				op=null;
				session.setAttribute("error_value", localizedMessages.getString("WidgetWebMenuServlet.0")); //$NON-NLS-1$ //$NON-NLS-2$				
			}
		}
		if(op!=null){
			switch (op) {
				case INDEX: {
					doForward(request, response, fMainPage);
					break;
				}
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
				case REQUESTAPIKEY:{
					requestApiKeyOperation(request,properties,manager,session);
					doForward(request, response, fMainPage);
					break;
				}
				default: {
					session.setAttribute("error_value", localizedMessages.getString("WidgetWebMenuServlet.2"));//$NON-NLS-1$ //$NON-NLS-2$
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
		WidgetDefault[] def = WidgetDefault.findAll();
		session.setAttribute("defaults", def); //$NON-NLS-1$
	}

	private void listOperation(HttpSession session, IWidgetAdminManager manager){
		Hashtable<String, Widget> widgetsHash = new Hashtable<String, Widget>();

		for(Widget widget:Widget.findAll()){
			widgetsHash.put(widget.getGuid(), widget);
		}

		//for(WidgetDefault defaultWidget : manager.getAllDefaultWidgets()){
		//	widgetsHash.put(defaultWidget.getWidgetContext(), manager.getWidget(defaultWidget.getWidgetId()));
		//}
		session.setAttribute("widgetsHash", widgetsHash); //$NON-NLS-1$
	}

	private void requestApiKeyOperation(HttpServletRequest request, Configuration properties, IWidgetAdminManager manager, HttpSession session){
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		session.setAttribute("message_value", null); //$NON-NLS-1$
		try {
			String email = request.getParameter("email"); //$NON-NLS-1$
			if (email == null) {
				session.setAttribute("message_value", localizedMessages.getString("WidgetWebMenuServlet.1")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else {
				if (email.trim().equals("")){ //$NON-NLS-1$
					session.setAttribute("message_value", localizedMessages.getString("WidgetWebMenuServlet.1"));					 //$NON-NLS-1$ //$NON-NLS-2$
				}
				else {
					// Otherwise, good to go
					WidgetKeyManager.createKey(request, email, localizedMessages);
					session.setAttribute("message_value", localizedMessages.getString("WidgetWebMenuServlet.2")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		catch (Exception ex) {
			session.setAttribute("message_value", localizedMessages.getString("WidgetWebMenuServlet.4")); //$NON-NLS-1$ //$NON-NLS-2$
			_logger.error(localizedMessages.getString("WidgetWebMenuServlet.4"), ex); //$NON-NLS-1$
		}

	}

	/**
	 * Forward to correct jsp page
	 */
	private void doForward(HttpServletRequest request, HttpServletResponse response, String jsp) throws ServletException, IOException{
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jsp);
		dispatcher.forward(request, response);
	}
}
