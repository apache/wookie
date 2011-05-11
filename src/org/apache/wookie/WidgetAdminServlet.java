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
import org.apache.wookie.beans.IApiKey;
import org.apache.wookie.beans.IWhitelist;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetDefault;
import org.apache.wookie.beans.IWidgetService;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.controller.WidgetServicesController;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.feature.Features;
import org.apache.wookie.helpers.WidgetFactory;
import org.apache.wookie.helpers.WidgetKeyManager;
import org.apache.wookie.manager.IWidgetAdminManager;
import org.apache.wookie.manager.impl.WidgetAdminManager;
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
		ADDNEWSERVICE, ADDNEWWHITELISTENTRY, LISTSERVICES, LISTSERVICESFORADDITION, 
		LISTWIDGETS, REMOVESERVICE, REMOVESINGLEWIDGETTYPE, REMOVEWHITELISTENTRY, REMOVEWIDGET,  
		REVISETYPES, SETDEFAULTWIDGET, SETWIDGETTYPES, UPLOADWIDGET, VIEWWHITELIST, REGISTERGADGET,
		LISTAPIKEYS, REVOKEAPIKEY
	}	

	// Get the logger
	static Logger _logger = Logger.getLogger(WidgetAdminServlet.class.getName());	

	// jsp page handles
	private static final String fAddNewServicesPage = "/admin/addnewservice.jsp"; //$NON-NLS-1$
	private static final String faddToWhiteListPage = "/admin/addtowhitelist.jsp"; //$NON-NLS-1$
	private static final String fListServicesPage = "/admin/listservices.jsp"; //$NON-NLS-1$
	private static final String fListWidgetsForDeletePage = "/admin/listallfordelete.jsp";	 //$NON-NLS-1$
	private static final String fListWidgetsPage = "/admin/listall.jsp"; //$NON-NLS-1$
	private static final String fMainPage = "/admin/index.jsp"; //$NON-NLS-1$
	private static final String fRemoveServicesPage = "/admin/removeservice.jsp"; //$NON-NLS-1$
	private static final String fremoveWhiteListPage = "/admin/removewhitelist.jsp";	 //$NON-NLS-1$
	private static final String fUpLoadResultsPage = "/admin/uploadresults.jsp"; //$NON-NLS-1$
	private static final String fViewWhiteListPage = "/admin/viewwhitelist.jsp"; //$NON-NLS-1$
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
	 * Add a new service type to the DB
	 * @param request
	 * @param manager
	 */
	private void addNewService(HttpServletRequest request) {
		String serviceName = request.getParameter("newservice"); //$NON-NLS-1$
		Messages localizedMessages = LocaleHandler.localizeMessages(request);	
		try {
			WidgetServicesController.create(serviceName);
			request.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.0")); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (ResourceDuplicationException e) {
			request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.1")); //$NON-NLS-1$ //$NON-NLS-2$ 
		} catch (InvalidParametersException e) {
			request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.1")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}
	}

	/**
	 *  Adds a new entry to the whitelist DB
	 */
	private void addWhiteListEntry(HttpServletRequest request) {
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		String uri = request.getParameter("newuri"); //$NON-NLS-1$
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWhitelist list = persistenceManager.newInstance(IWhitelist.class);
		list.setfUrl(uri);
		if(persistenceManager.save(list)){
			request.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.2")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}
		else{
			request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.3")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}
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

		IWidgetAdminManager manager = (IWidgetAdminManager)session.getAttribute(WidgetAdminManager.class.getName());
		if(manager == null){
			manager = new WidgetAdminManager(localizedMessages);
			session.setAttribute(WidgetAdminManager.class.getName(), manager);
		}
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
			case ADDNEWWHITELISTENTRY: {
				addWhiteListEntry(request);					
				listWhiteListOperation(request);
				doForward(request, response, faddToWhiteListPage);
				break;
			}
			case VIEWWHITELIST: {
				listWhiteListOperation(request);
				if(request.getParameter("param").equalsIgnoreCase("list")){											 //$NON-NLS-1$ //$NON-NLS-2$
					doForward(request, response, fViewWhiteListPage);
				}
				else if(request.getParameter("param").equalsIgnoreCase("add")){					 //$NON-NLS-1$ //$NON-NLS-2$
					doForward(request, response, faddToWhiteListPage);
				}
				else if(request.getParameter("param").equalsIgnoreCase("remove")){					 //$NON-NLS-1$ //$NON-NLS-2$
					doForward(request, response, fremoveWhiteListPage);						
				}
				break;
			}
			case REMOVEWIDGET: {
				removeWidget(request, properties, manager);										
				listOperation(request, false);					
				doForward(request, response, fListWidgetsForDeletePage);					
				break;
			}
			case REVISETYPES: {
				reviseTypes(request, manager);					
				doForward(request, response, fUpLoadResultsPage);
				break;
			}
			case ADDNEWSERVICE: {
				addNewService(request);	
				retrieveServices(request);
				doForward(request, response, fAddNewServicesPage);
				break;
			}
			case UPLOADWIDGET: {
				uploadOperation(request, properties, manager);						
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
			case LISTSERVICES: {
				retrieveServices(request);
				if(request.getParameter("param").equalsIgnoreCase("list")){											 //$NON-NLS-1$ //$NON-NLS-2$
					doForward(request, response, fListServicesPage);
				}
				else if(request.getParameter("param").equalsIgnoreCase("add")){					 //$NON-NLS-1$ //$NON-NLS-2$
					doForward(request, response, fAddNewServicesPage);
				}
				else if(request.getParameter("param").equalsIgnoreCase("remove")){					 //$NON-NLS-1$ //$NON-NLS-2$
					doForward(request, response, fRemoveServicesPage);
				}
				break;
			}
			case SETWIDGETTYPES: {
				updateWidgetTypes(request, manager);
				listOperation(request, true);											
				doForward(request, response, fListWidgetsPage);
				break;
			}				
			case REMOVESERVICE:{
				removeServiceOperation(request);
				doForward(request, response, fRemoveServicesPage);
				break;
			}
			case REMOVEWHITELISTENTRY: {
				removeWhiteListEntry(request);										
				listWhiteListOperation(request);
				doForward(request, response, fremoveWhiteListPage);
				break;
			}
			case REMOVESINGLEWIDGETTYPE:{
				removeSingleWidgetTypeOperation(request, manager);
				listOperation(request, true);
				doForward(request, response, fListWidgetsPage);
				break;
			}
			case SETDEFAULTWIDGET:{
				setDefaultWidgetOperation(request, manager);
				doForward(request, response, fListWidgetsPage);
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

	private void listOperation(HttpServletRequest request, boolean getDefaults){
		retrieveWidgets(request);	
		if(getDefaults){
			Hashtable<String, Object> defaultHash = new Hashtable<String, Object>();
	        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
			IWidgetDefault[] wds = persistenceManager.findAll(IWidgetDefault.class);
			if (wds != null){
				for(IWidgetDefault defaultWidget : wds){
					defaultHash.put(defaultWidget.getWidgetContext(), defaultWidget.getWidget().getId());				
				}	
			}
			request.setAttribute("widget_defaults", defaultHash); //$NON-NLS-1$
		}
	}

	private void listWhiteListOperation(HttpServletRequest request) {
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		request.setAttribute("whitelist", persistenceManager.findAll(IWhitelist.class)); //$NON-NLS-1$
	}  	

	private void removeServiceOperation(HttpServletRequest request) {
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		String serviceId = request.getParameter("serviceId"); //$NON-NLS-1$
		try {
			WidgetServicesController.remove(serviceId);
			request.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.6")); 				 //$NON-NLS-1$ //$NON-NLS-2$ 
		} catch (ResourceNotFoundException e) {
			request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.7")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}
		retrieveServices(request);
	}

	private void removeSingleWidgetTypeOperation(HttpServletRequest request, IWidgetAdminManager manager) {
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		String widgetId = request.getParameter("widgetId"); //$NON-NLS-1$
		String widgetType = request.getParameter("widgetType");	 //$NON-NLS-1$
		if(manager.removeSingleWidgetType(widgetId, widgetType)){
			request.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.8")); 				 //$NON-NLS-1$ //$NON-NLS-2$ 
		}
		else{
			request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.9")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}	
		request.setAttribute("widgets", null);						 //$NON-NLS-1$
	}

	private void removeWhiteListEntry(HttpServletRequest request) {
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		String entryId = request.getParameter("entryId"); //$NON-NLS-1$
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWhitelist entry = persistenceManager.findById(IWhitelist.class, entryId);
		if(persistenceManager.delete(entry)){
			request.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.10")); 				 //$NON-NLS-1$ //$NON-NLS-2$ 
		}
		else{
			request.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.11")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}				
	}

	private void removeWidget(HttpServletRequest request, Configuration properties, IWidgetAdminManager manager) {
		final String WIDGETFOLDER = getServletContext().getRealPath(properties.getString("widget.widgetfolder"));//$NON-NLS-1$
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		String widgetId = request.getParameter("widgetId"); //$NON-NLS-1$
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidget widget = persistenceManager.findById(IWidget.class, widgetId);
		String guid = widget.getGuid();
		if(WidgetFactory.destroy(widgetId)){
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

	private void retrieveServices(HttpServletRequest request){						
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		request.setAttribute("services", persistenceManager.findAll(IWidgetService.class));						 //$NON-NLS-1$
	}

	private void retrieveWidgets(HttpServletRequest request){
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		request.setAttribute("widgets", persistenceManager.findAll(IWidget.class)); //$NON-NLS-1$
	}



	private void reviseTypes(HttpServletRequest request, IWidgetAdminManager manager) {
		retrieveServices(request);
		request.setAttribute("hasValidated", Boolean.valueOf(true)); //$NON-NLS-1$
		request.setAttribute("closeWindow", Boolean.valueOf(false)); //$NON-NLS-1$
		String dbkey = request.getParameter("dbkey"); //$NON-NLS-1$
		request.setAttribute("dbkey", Integer.parseInt(dbkey)); //$NON-NLS-1$
	}

	private void setDefaultWidgetOperation(HttpServletRequest request, IWidgetAdminManager manager){
		String widgetId = request.getParameter("widgetId"); //$NON-NLS-1$
		String widgetType = request.getParameter("widgetType");		 //$NON-NLS-1$
		manager.setDefaultWidget(widgetId, widgetType);
		listOperation(request, true);
	}

	private void updateWidgetTypes(HttpServletRequest request, IWidgetAdminManager manager) throws IOException{
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		boolean canMax = false;

		String maximize = request.getParameter("max"); //$NON-NLS-1$
		if(maximize!=null){
			canMax = Boolean.valueOf(maximize);
		}
		String dbKey = request.getParameter("dbkey"); //$NON-NLS-1$
		String[] widgetTypes = request.getParameterValues("widgetTypes"); //$NON-NLS-1$
		manager.setWidgetTypesForWidget(dbKey, widgetTypes, canMax);
		request.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.15")); 		 //$NON-NLS-1$ //$NON-NLS-2$ 
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

	private void listAPIKeysOperation(HttpServletRequest request){
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		request.setAttribute("keys", persistenceManager.findAll(IApiKey.class));
	}

	private void revokeAPIKeyOperation(HttpServletRequest request){
		String value = request.getParameter("key");
		if (WidgetKeyManager.revokeKey(value)){
			request.setAttribute("message_value", "Key revoked");
		} else {
			request.setAttribute("error_value", "Key could not be revoked");
		}
	}

	private void uploadOperation(HttpServletRequest request, Configuration properties, IWidgetAdminManager manager) {

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
					IWidget widget = WidgetFactory.addNewWidget(widgetModel, null, zipFile, false);
					Object dbkey = widget.getId();
					// widget added
					request.setAttribute("message_value", "'"+ widgetModel.getLocalName("en") +"' - " + localizedMessages.getString("WidgetAdminServlet.19")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$					
					retrieveServices(request);
					request.setAttribute("hasValidated", Boolean.valueOf(true));																	 //$NON-NLS-1$
					request.setAttribute("dbkey", dbkey); //$NON-NLS-1$
				}	
				else{
					// UPDATE
					// Update the widget metadata and configuration details
					WidgetFactory.update(widgetModel, persistenceManager.findWidgetByGuid(widgetModel.getIdentifier()), false);
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
