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

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.beans.IApiKey;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.feature.Features;
import org.apache.wookie.helpers.WidgetFactory;
import org.apache.wookie.helpers.WidgetKeyManager;
import org.apache.wookie.server.LocaleHandler;
import org.apache.wookie.util.WidgetFileUtils;
import org.apache.wookie.util.WidgetJavascriptSyntaxAnalyzer;
import org.apache.wookie.util.gadgets.GadgetUtils;
import org.apache.wookie.util.html.StartPageProcessor;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.exceptions.InvalidContentTypeException;
import org.apache.wookie.w3c.exceptions.InvalidStartFileException;

/**
 * Servlet implementation class for Servlet: WidgetAdminServlet
 * 
 * This servlet handles all requests for Admin tasks
 * 
 * @author Paul Sharples
 * @version $Id: WidgetAdminServlet.java,v 1.3 2009-09-02 18:37:31 scottwilson Exp $ 
 *
 */
public class WidgetAdminServlet extends HttpServlet implements Servlet {

	// our list of allowed operations
	private enum Operation {
	  // Widget related operations
	  LISTWIDGETS, REMOVEWIDGET, UPLOADWIDGET,
	   // Gadget operations
    REGISTERGADGET,
    // APIKey operations  
    LISTAPIKEYS, REVOKEAPIKEY,
	}	

	// Get the logger
	static Logger _logger = Logger.getLogger(WidgetAdminServlet.class.getName());	

	// jsp page handles
	private static final String fListWidgetsForDeletePage = "/admin/listallfordelete.jsp";	 //$NON-NLS-1$
	private static final String fListWidgetsPage = "/admin/listall.jsp"; //$NON-NLS-1$
	private static final String fMainPage = "/admin/index.jsp"; //$NON-NLS-1$
	private static final String fUpLoadResultsPage = "/admin/uploadresults.jsp"; //$NON-NLS-1$
	private static final String fRegisterGadgetPage = "/admin/registergadget.jsp"; //$NON-NLS-1$
	private static final String fListAPIKeysPage = "/admin/keys.jsp"; //$NON-NLS-1$

	private static final long serialVersionUID = -3026022301561798524L;;	


	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public WidgetAdminServlet() {
		super();
	}   	

	/**
	 * Forward to correct jsp page
	 */
	private void doForward(HttpServletRequest request, HttpServletResponse response, String jsp) throws ServletException, IOException{
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jsp);
		dispatcher.forward(request, response);
	}


	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		HttpSession session = request.getSession(true);						
		Messages localizedMessages = LocaleHandler.localizeMessages(request);	

		Configuration properties = (Configuration) session.getServletContext().getAttribute("properties"); //$NON-NLS-1$

		request.setAttribute("version", properties.getString("widget.version")); //$NON-NLS-1$ //$NON-NLS-2$
		String task = request.getParameter("operation"); //$NON-NLS-1$
		Operation op=null;
		// sanity check...		
		if (task != null) {
			// if this is called with a string not contained within the enumerated list an exception is thrown
			try {op = Operation.valueOf(task);} 
			catch (IllegalArgumentException e) {
				op=null;
				request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.4")); //$NON-NLS-1$ //$NON-NLS-2$				
			}
		}	
		if(op!=null){
			switch (op) {
			case REMOVEWIDGET: {
				removeWidget(request, properties);										
				listOperation(request, false);					
				doForward(request, response, fListWidgetsForDeletePage);					
				break;
			}
			case UPLOADWIDGET: {
				uploadOperation(request, properties);						
				doForward(request, response, fUpLoadResultsPage);
				break;
			}
			case LISTWIDGETS: {
				if(request.getParameter("param").equalsIgnoreCase("remove")){ //$NON-NLS-1$ //$NON-NLS-2$
					listOperation(request, false);					
					doForward(request, response, fListWidgetsForDeletePage);
				}
				else{
					listOperation(request, true);					
					doForward(request, response, fListWidgetsPage);						
				}
				break;
			}
			case REGISTERGADGET:{
				registerOperation(request, properties);
				doForward(request, response,fMainPage);
				break;
			}
			case LISTAPIKEYS:{
				listAPIKeysOperation(request);
				doForward(request, response, fListAPIKeysPage);
				break;
			}
			case REVOKEAPIKEY:{
				revokeAPIKeyOperation(request);
				doForward(request, response, fListAPIKeysPage);
				break;
			}

			default: {
				request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.5"));// need to i18n this //$NON-NLS-1$ //$NON-NLS-2$ 
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

	private void listAPIKeysOperation(HttpServletRequest request){
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		request.setAttribute("keys", persistenceManager.findAll(IApiKey.class));
	}

	private void listOperation(HttpServletRequest request, boolean getDefaults){
		retrieveWidgets(request);	
	}

	private void registerOperation(HttpServletRequest request, Configuration properties){
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		try {
			W3CWidget widgetModel = GadgetUtils.createWidget(request);
	        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
			if(persistenceManager.findWidgetByGuid(widgetModel.getIdentifier()) == null){
				WidgetFactory.addNewWidget(widgetModel);
				request.setAttribute("message_value", widgetModel.getLocalName("en")+": "+localizedMessages.getString("WidgetAdminServlet.16")); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				request.setAttribute("message_value", widgetModel.getLocalName("en")+": "+localizedMessages.getString("WidgetAdminServlet.17")); //$NON-NLS-1$ //$NON-NLS-2$				
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.18")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private void removeWidget(HttpServletRequest request, Configuration properties) {
		final String WIDGETFOLDER = getServletContext().getRealPath(properties.getString("widget.widgetfolder"));//$NON-NLS-1$
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		String widgetId = request.getParameter("widgetId"); //$NON-NLS-1$
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidget widget = persistenceManager.findById(IWidget.class, widgetId);
		String guid = widget.getGuid();
		if(WidgetFactory.destroy(widget)){
			if(WidgetFileUtils.removeWidgetResources(WIDGETFOLDER, guid)){			
				request.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.12"));			 //$NON-NLS-1$ //$NON-NLS-2$ 
			}
			else{
				request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.13")); //$NON-NLS-1$ //$NON-NLS-2$ 
			}
		}
		else{
			request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.14")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}
	}

	private void retrieveWidgets(HttpServletRequest request){
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		request.setAttribute("widgets", persistenceManager.findAll(IWidget.class)); //$NON-NLS-1$
	}

	private void revokeAPIKeyOperation(HttpServletRequest request){
		String value = request.getParameter("key");
		if (WidgetKeyManager.revokeKey(value)){
			request.setAttribute("message_value", "Key revoked");
		} else {
			request.setAttribute("error_value", "Key could not be revoked");
		}
	}

	private void uploadOperation(HttpServletRequest request, Configuration properties) {

		final String WIDGETFOLDER = getServletContext().getRealPath(properties.getString("widget.widgetfolder"));//$NON-NLS-1$
		final String UPLOADFOLDER = getServletContext().getRealPath(properties.getString("widget.useruploadfolder"));//$NON-NLS-1$
		
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		request.setAttribute("hasValidated", Boolean.valueOf(false)); //$NON-NLS-1$
		request.setAttribute("closeWindow", Boolean.valueOf(true)); //$NON-NLS-1$
		File zipFile;
		try {
			zipFile = WidgetFileUtils.upload(UPLOADFOLDER, request);
		} 
		catch (Exception ex) {
			request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.28") + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return;
		}

		try {	
			if(zipFile.exists()){
                IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
				final String[] locales = properties.getStringArray("widget.locales");
				W3CWidgetFactory fac = new W3CWidgetFactory();
				fac.setLocales(locales);
				fac.setLocalPath(getServletContext().getContextPath()+properties.getString("widget.widgetfolder"));
				fac.setOutputDirectory(WIDGETFOLDER);
				fac.setFeatures(Features.getFeatureNames());
				fac.setStartPageProcessor(new StartPageProcessor());
				W3CWidget widgetModel = fac.parse(zipFile);
				WidgetJavascriptSyntaxAnalyzer jsa = new WidgetJavascriptSyntaxAnalyzer(fac.getUnzippedWidgetDirectory());				
	            if(persistenceManager.findWidgetByGuid(widgetModel.getIdentifier()) == null){
					// ADD
					IWidget widget = WidgetFactory.addNewWidget(widgetModel, zipFile, false);
					Object dbkey = widget.getId();
					// widget added
					request.setAttribute("message_value", "'"+ widgetModel.getLocalName("en") +"' - " + localizedMessages.getString("WidgetAdminServlet.19")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$					
					request.setAttribute("hasValidated", Boolean.valueOf(true));																	 //$NON-NLS-1$
					request.setAttribute("dbkey", dbkey); //$NON-NLS-1$
				}	
				else{
					// UPDATE
					// Update the widget metadata and configuration details
					WidgetFactory.update(widgetModel, persistenceManager.findWidgetByGuid(widgetModel.getIdentifier()), false, zipFile);
					request.setAttribute("message_value", "'"+ widgetModel.getLocalName("en") +"' - " + localizedMessages.getString("WidgetAdminServlet.20")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$					
				}
			}
			else{
				// no file found to be uploaded - shouldn't happen
				request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.24")); //$NON-NLS-1$ //$NON-NLS-2$
			}						
		} 		 
		catch (InvalidStartFileException ex){
			_logger.error(ex);
			request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.27") + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$			
		}
		catch (BadManifestException ex) {
			_logger.error(ex);		
			String message = ex.getMessage();
			if (ex.getMessage() == null || ex.getMessage().equals("")) message = localizedMessages.getString("WidgetAdminServlet.28"); //$NON-NLS-1$
			if (ex instanceof InvalidContentTypeException) message = localizedMessages.getString("WidgetAdminServlet.30");//$NON-NLS-1$
			request.setAttribute("error_value", message); //$NON-NLS-1$
		}
		catch (BadWidgetZipFileException ex) {
			_logger.error(ex);	
			String message = ex.getMessage();
			if (ex.getMessage() == null || ex.getMessage().equals("")) message = localizedMessages.getString("WidgetAdminServlet.29"); //$NON-NLS-1$
			request.setAttribute("error_value", message); //$NON-NLS-1$
		}
		catch (Exception ex) {
			_logger.error(ex);			
			request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.25") + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}


	}



}
