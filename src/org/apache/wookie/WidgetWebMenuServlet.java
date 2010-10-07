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

package org.apache.wookie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.connector.framework.AbstractWookieConnectorService;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieConnectorService;
import org.apache.wookie.controller.WidgetInstancesController;
import org.apache.wookie.helpers.WidgetInstanceFactory;
import org.apache.wookie.helpers.WidgetKeyManager;
import org.apache.wookie.manager.IWidgetAdminManager;
import org.apache.wookie.manager.impl.WidgetAdminManager;
import org.apache.wookie.server.LocaleHandler;

/**
 * WidgetWebMenuServlet
 *
 * @author Paul Sharples
 * @version $Id: WidgetWebMenuServlet.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $
 */
public class WidgetWebMenuServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;

	// our list of allowed operations
	private enum Operation {
		LISTWIDGETS, INDEX, INSTANTIATE, REQUESTAPIKEY, DEMO_WIDGET
	}

	// Get the logger
	static Logger _logger = Logger.getLogger(WidgetWebMenuServlet.class.getName());

	// jsp page handles
	private static final String fMainPage = "/webmenu/index.jsp"; //$NON-NLS-1$
	private static final String fListWidgetsPage = "/webmenu/listall.jsp"; //$NON-NLS-1$
	private static final String fDemoWidgetPage = "/webmenu/demoWidget.jsp"; //$NON-NLS-1$
	private static final String fInstantiateWidgetsPage = "/webmenu/instantiate.jsp"; //$NON-NLS-1$
	private static final String fRequestApiKeyPage = "/webmenu/requestapikey.jsp"; //$NON-NLS-1$

  private WookieConnectorService connectorService;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetAdminManager manager = (IWidgetAdminManager)session.getAttribute(WidgetAdminManager.class.getName());
		if(manager == null){
			manager = new WidgetAdminManager(localizedMessages);
			session.setAttribute(WidgetAdminManager.class.getName(), manager);
		}
		Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties"); //$NON-NLS-1$

		request.setAttribute("version", properties.getString("widget.version")); //$NON-NLS-1$ //$NON-NLS-2$
		String task = request.getParameter("operation"); //$NON-NLS-1$
		Operation op=null;
		// sanity check...
		if (task != null) {
			// if this is called with a string not contained within the enumerated list an exception is thrown
			try {op = Operation.valueOf(task);}
			catch (IllegalArgumentException e) {
				op=null;
				request.setAttribute("error_value", localizedMessages.getString("WidgetWebMenuServlet.0")); //$NON-NLS-1$ //$NON-NLS-2$				
			}
		}
		if(op!=null){
			switch (op) {
				case INDEX: {
					doForward(request, response, fMainPage);
					break;
				}
				case LISTWIDGETS: {
					listOperation(request, session, manager);
					doForward(request, response, fListWidgetsPage);
					break;
				}
				case DEMO_WIDGET:{
          String idKey = request.getParameter("idkey");
          try {
            IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
            String guid = persistenceManager.findWidgetInstanceByIdKey(idKey).getWidget().getGuid();
            AbstractWookieConnectorService conn = getConnectorService(request);
            conn.setCurrentUser("testuser");
            org.apache.wookie.connector.framework.WidgetInstance instanceOne = conn.getOrCreateInstance(guid);
            conn.setPropertyForInstance(instanceOne, "setpersonalproperty", "moderator", "true");
            conn.setCurrentUser("testuser2");
            org.apache.wookie.connector.framework.WidgetInstance instanceTwo = conn.getOrCreateInstance(guid);            
            request.setAttribute("firstWidgetURL", instanceOne.getUrl());
            request.setAttribute("secondWidgetURL", instanceTwo.getUrl());
            request.setAttribute("widgetHeight", instanceOne.getHeight());
            request.setAttribute("widgetWidth", instanceOne.getWidth());
            request.setAttribute("proxy", WidgetInstancesController.checkProxy(request));
            doForward(request, response, fDemoWidgetPage);
          } catch (WookieConnectorException e) {
        	  e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          }
					break;
				}
				case INSTANTIATE: {
					instantiateOperation(request, manager);
					doForward(request, response, fInstantiateWidgetsPage);
					break;
				}
				case REQUESTAPIKEY:{
					requestApiKeyOperation(request,properties,manager);
					doForward(request, response, fMainPage);
					break;
				}
				default: {
					request.setAttribute("error_value", localizedMessages.getString("WidgetWebMenuServlet.2"));//$NON-NLS-1$ //$NON-NLS-2$
					doForward(request, response, fMainPage);
				}
			}
		}
		else {
			doForward(request, response, fMainPage);
		}
	}

	/**
	 * Get a connector service for the first test user.
	 * 
	 * @param request
	 * @return
	 * @throws WookieConnectorException
	 */
	private AbstractWookieConnectorService getConnectorService(HttpServletRequest request) throws WookieConnectorException {
	  if (connectorService == null) {
      StringBuilder sbUrl = new StringBuilder(request.getScheme());
      sbUrl.append("://");
      sbUrl.append(request.getServerName());
      sbUrl.append(":");
      sbUrl.append(request.getServerPort());
      sbUrl.append(request.getContextPath());
      connectorService = new WookieConnectorService(sbUrl.toString(), "TEST", "myshareddata");
	  }
	  return connectorService;
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

	private void instantiateOperation(HttpServletRequest request, IWidgetAdminManager manager){
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidget[] widgets = persistenceManager.findAll(IWidget.class);
		request.setAttribute("widgets", widgets); //$NON-NLS-1$
	}
	/**
	 * Creates a table, looks for widget definitions with a string (the name of 
	 * the widget) and an instance of the widget which references the actual widget 
	 * object and puts the resulting set of instances in the widgetInstances attribute
	 * of the request. This can then be accessed by listall.jsp 
	 * 
	 * @param request
	 * @param session
	 * @param manager
	 */
	private void listOperation(HttpServletRequest request, HttpSession session, IWidgetAdminManager manager){
		ArrayList<IWidgetInstance> widgetInstances = new ArrayList<IWidgetInstance>();

        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidget[] widgets = persistenceManager.findAll(IWidget.class);
		for(IWidget widget : widgets){
			// Create an instance of the widget so that we can display it as the demo widget
			IWidgetInstance instance = null;
			String apiKey = "TEST"; //$NON-NLS-1$
			String userId = "testuser"; //$NON-NLS-1$
			String sharedDataKey = "myshareddata"; //$NON-NLS-1$
			String widgetId = widget.getGuid();
			instance = WidgetInstanceFactory.getWidgetFactory(session, LocaleHandler.localizeMessages(request)).newInstance(apiKey, userId, sharedDataKey, null, widgetId, null);
			if (instance != null) {
				widgetInstances.add(instance);
			}
		}
		
		// Sort the widget instances by name
		Collections.sort(widgetInstances, new Comparator<Object>(){
			public int compare(Object o1, Object o2) {
				String w1 = ((IWidgetInstance)o1).getWidget().getWidgetTitle(null);
				String w2 = ((IWidgetInstance)o2).getWidget().getWidgetTitle(null);
				return w1.compareTo(w2);
			}
			
		});
		
		// Store the array of instances in the request attribute
		request.setAttribute("widgetInstances", widgetInstances.toArray( new IWidgetInstance[widgetInstances.size()])); //$NON-NLS-1$
	}

	private void requestApiKeyOperation(HttpServletRequest request, Configuration properties, IWidgetAdminManager manager){
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		try {
			String email = request.getParameter("email"); //$NON-NLS-1$
			if (email == null) {
				request.setAttribute("message_value", localizedMessages.getString("WidgetWebMenuServlet.1")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else {
				if (email.trim().equals("")){ //$NON-NLS-1$
					request.setAttribute("message_value", localizedMessages.getString("WidgetWebMenuServlet.1"));					 //$NON-NLS-1$ //$NON-NLS-2$
				}
				else {
					// Otherwise, good to go
					WidgetKeyManager.createKey(request, email, localizedMessages);
					request.setAttribute("message_value", localizedMessages.getString("WidgetWebMenuServlet.3")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		catch (Exception ex) {
			request.setAttribute("error_value", localizedMessages.getString("WidgetWebMenuServlet.4")); //$NON-NLS-1$ //$NON-NLS-2$
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
