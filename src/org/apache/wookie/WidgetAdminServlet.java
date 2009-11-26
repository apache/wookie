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
import org.apache.wookie.beans.ApiKey;
import org.apache.wookie.beans.Whitelist;
import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetDefault;
import org.apache.wookie.beans.WidgetService;
import org.apache.wookie.exceptions.BadManifestException;
import org.apache.wookie.exceptions.BadWidgetZipFileException;
import org.apache.wookie.exceptions.InvalidContentTypeException;
import org.apache.wookie.exceptions.InvalidStartFileException;
import org.apache.wookie.helpers.WidgetKeyManager;
import org.apache.wookie.manager.IWidgetAdminManager;
import org.apache.wookie.manager.impl.WidgetAdminManager;
import org.apache.wookie.manifestmodel.IManifestModel;
import org.apache.wookie.server.LocaleHandler;
import org.apache.wookie.util.WidgetPackageUtils;
import org.apache.wookie.util.gadgets.GadgetUtils;

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
	 * @param session
	 * @param request
	 * @param manager
	 */
	private void addNewService(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager) {
		String serviceName = request.getParameter("newservice"); //$NON-NLS-1$
		Messages localizedMessages = LocaleHandler.localizeMessages(request);	
		if(manager.addNewService(serviceName)){	
			session.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.0")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}
		else{ 
			session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.1")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}
	}

	/**
	 *  Adds a new entry to the whitelist DB
	 */
	private void addWhiteListEntry(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager) {
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		String uri = request.getParameter("newuri"); //$NON-NLS-1$
		if(manager.addWhiteListEntry(uri)){
			session.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.2")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}
		else{
			session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.3")); //$NON-NLS-1$ //$NON-NLS-2$ 
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
				session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.4")); //$NON-NLS-1$ //$NON-NLS-2$				
			}
		}	
		if(op!=null){
			switch (op) {
			case ADDNEWWHITELISTENTRY: {
				addWhiteListEntry(session, request, manager);					
				listWhiteListOperation(session, manager);
				doForward(request, response, faddToWhiteListPage);
				break;
			}
			case VIEWWHITELIST: {
				listWhiteListOperation(session, manager);
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
				removeWidget(session, request, properties, manager);										
				listOperation(session, manager, false);					
				doForward(request, response, fListWidgetsForDeletePage);					
				break;
			}
			case REVISETYPES: {
				reviseTypes(session, request, manager);					
				doForward(request, response, fUpLoadResultsPage);
				break;
			}
			case ADDNEWSERVICE: {
				addNewService(session, request, manager);	
				retrieveServices(session, manager);
				doForward(request, response, fAddNewServicesPage);
				break;
			}
			case UPLOADWIDGET: {
				uploadOperation(request, properties, manager, session);						
				doForward(request, response, fUpLoadResultsPage);
				break;
			}
			case LISTWIDGETS: {
				if(request.getParameter("param").equalsIgnoreCase("remove")){ //$NON-NLS-1$ //$NON-NLS-2$
					listOperation(session, manager, false);					
					doForward(request, response, fListWidgetsForDeletePage);
				}
				else{
					listOperation(session, manager, true);					
					doForward(request, response, fListWidgetsPage);						
				}
				break;
			}
			case LISTSERVICES: {
				retrieveServices(session, manager);
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
				updateWidgetTypes(session, request, manager);
				listOperation(session, manager, true);											
				doForward(request, response, fListWidgetsPage);
				break;
			}				
			case REMOVESERVICE:{
				removeServiceOperation(session, request, manager);
				doForward(request, response, fRemoveServicesPage);
				break;
			}
			case REMOVEWHITELISTENTRY: {
				removeWhiteListEntry(session, request, manager);										
				listWhiteListOperation(session, manager);
				doForward(request, response, fremoveWhiteListPage);
				break;
			}
			case REMOVESINGLEWIDGETTYPE:{
				removeSingleWidgetTypeOperation(session, request, manager);
				listOperation(session, manager, true);
				doForward(request, response, fListWidgetsPage);
				break;
			}
			case SETDEFAULTWIDGET:{
				setDefaultWidgetOperation(session, request, manager);
				doForward(request, response, fListWidgetsPage);
				break;
			}
			case REGISTERGADGET:{
				registerOperation(request, properties, manager, session);
				doForward(request, response,fRegisterGadgetPage);
				break;
			}
			case LISTAPIKEYS:{
				listAPIKeysOperation(session, request, manager);
				doForward(request, response, fListAPIKeysPage);
				break;
			}
			case REVOKEAPIKEY:{
				revokeAPIKeyOperation(session, request, manager);
				doForward(request, response, fListAPIKeysPage);
				break;
			}

			default: {
				session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.5"));// need to i18n this //$NON-NLS-1$ //$NON-NLS-2$ 
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

	private void listOperation(HttpSession session, IWidgetAdminManager manager, boolean getDefaults){
		retrieveWidgets(session, manager);	
		if(getDefaults){
			Hashtable<String, Integer> defaultHash = new Hashtable<String, Integer>();
			WidgetDefault[] wds = WidgetDefault.findAll();
			if (wds != null){
				for(WidgetDefault defaultWidget : wds){
					defaultHash.put(defaultWidget.getWidgetContext(), defaultWidget.getWidgetId());				
				}	
			}
			session.setAttribute("widget_defaults", defaultHash); //$NON-NLS-1$
		}
	}

	private void listWhiteListOperation(HttpSession session, IWidgetAdminManager manager) {
		session.setAttribute("whitelist", Whitelist.findAll()); //$NON-NLS-1$
	}  	

	private void removeServiceOperation(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager) {
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		String serviceId = request.getParameter("serviceId"); //$NON-NLS-1$
		if(manager.removeServiceAndReferences(Integer.parseInt(serviceId))){
			session.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.6")); 				 //$NON-NLS-1$ //$NON-NLS-2$ 
		}
		else{
			session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.7")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}
		retrieveServices(session, manager);
	}

	private void removeSingleWidgetTypeOperation(HttpSession session,
			HttpServletRequest request, IWidgetAdminManager manager) {
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		String widgetId = request.getParameter("widgetId"); //$NON-NLS-1$
		String widgetType = request.getParameter("widgetType");	 //$NON-NLS-1$
		if(manager.removeSingleWidgetType(Integer.parseInt(widgetId), widgetType)){
			session.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.8")); 				 //$NON-NLS-1$ //$NON-NLS-2$ 
		}
		else{
			session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.9")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}	
		session.setAttribute("widgets", null);						 //$NON-NLS-1$
	}

	private void removeWhiteListEntry(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager) {
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		String entryId = request.getParameter("entryId"); //$NON-NLS-1$
		if(manager.removeWhiteListEntry(Integer.parseInt(entryId))){
			session.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.10")); 				 //$NON-NLS-1$ //$NON-NLS-2$ 
		}
		else{
			session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.11")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}				
	}

	private void removeWidget(HttpSession session, HttpServletRequest request, Configuration properties, IWidgetAdminManager manager) {
		final String WIDGETFOLDER = request.getSession().getServletContext().getRealPath(properties.getString("widget.widgetfolder"));//$NON-NLS-1$
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		String widgetId = request.getParameter("widgetId"); //$NON-NLS-1$
		Widget widget = Widget.findById(Integer.parseInt(widgetId));
		String guid = widget.getGuid();
		if(manager.removeWidgetAndReferences(Integer.parseInt(widgetId))){
			if(WidgetPackageUtils.removeWidgetResources(WIDGETFOLDER, guid)){			
				session.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.12"));			 //$NON-NLS-1$ //$NON-NLS-2$ 
			}
			else{
				session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.13")); //$NON-NLS-1$ //$NON-NLS-2$ 
			}
		}
		else{
			session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.14")); //$NON-NLS-1$ //$NON-NLS-2$ 
		}
	}		

	private void retrieveServices(HttpSession session, IWidgetAdminManager manager){						
		session.setAttribute("services", WidgetService.findAll());						 //$NON-NLS-1$
	}

	private void retrieveWidgets(HttpSession session, IWidgetAdminManager manager){
		session.setAttribute("widgets", Widget.findAll()); //$NON-NLS-1$
	}



	private void reviseTypes(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager) {
		retrieveServices(session, manager);
		session.setAttribute("hasValidated", Boolean.valueOf(true)); //$NON-NLS-1$
		session.setAttribute("closeWindow", Boolean.valueOf(false)); //$NON-NLS-1$
		String dbkey = request.getParameter("dbkey"); //$NON-NLS-1$
		boolean isMaxable = manager.isWidgetMaximized(Integer.parseInt(dbkey));
		session.setAttribute("isMaxable", Boolean.valueOf(isMaxable)); //$NON-NLS-1$
		session.setAttribute("dbkey", Integer.parseInt(dbkey)); //$NON-NLS-1$
	}

	private void setDefaultWidgetOperation(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager){
		String widgetId = request.getParameter("widgetId"); //$NON-NLS-1$
		String widgetType = request.getParameter("widgetType");		 //$NON-NLS-1$
		manager.setDefaultWidget(Integer.parseInt(widgetId), widgetType);
		listOperation(session, manager, true);
	}

	private void updateWidgetTypes(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager) throws IOException{
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		boolean canMax = false;

		String maximize = request.getParameter("max"); //$NON-NLS-1$
		if(maximize!=null){
			canMax = Boolean.valueOf(maximize);
		}
		int dbKey = Integer.parseInt(request.getParameter("dbkey")); //$NON-NLS-1$
		String[] widgetTypes = request.getParameterValues("widgetTypes"); //$NON-NLS-1$
		manager.setWidgetTypesForWidget(dbKey, widgetTypes, canMax);
		session.setAttribute("message_value", localizedMessages.getString("WidgetAdminServlet.15")); 		 //$NON-NLS-1$ //$NON-NLS-2$ 
	}  

	private void registerOperation(HttpServletRequest request, Configuration properties, IWidgetAdminManager manager, HttpSession session){
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		Widget widget = null;
		session.setAttribute("metadata", null); //$NON-NLS-1$
		try {
			widget = GadgetUtils.createWidget(request);
			if (widget.save()){
				session.setAttribute("metadata", localizedMessages.getString("WidgetAdminServlet.16")); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				session.setAttribute("metadata", localizedMessages.getString("WidgetAdminServlet.17")); //$NON-NLS-1$ //$NON-NLS-2$			
			}
		} catch (Exception e1) {
			session.setAttribute("metadata", localizedMessages.getString("WidgetAdminServlet.18")); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	private void listAPIKeysOperation(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager){
		session.setAttribute("keys", ApiKey.findAll());
	}

	private void revokeAPIKeyOperation(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager){
		String value = request.getParameter("key");
		if (WidgetKeyManager.revokeKey(value)){
			session.setAttribute("message_value", "Key revoked");
		} else {
			session.setAttribute("error_value", "Key could not be revoked");
		}
	}

	private void uploadOperation(HttpServletRequest request, Configuration properties, IWidgetAdminManager manager, HttpSession session) {

		final String WIDGETFOLDER = request.getSession().getServletContext().getRealPath(properties.getString("widget.widgetfolder"));//$NON-NLS-1$
		final String UPLOADFOLDER = request.getSession().getServletContext().getRealPath(properties.getString("widget.useruploadfolder"));//$NON-NLS-1$
		
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		session.setAttribute("hasValidated", Boolean.valueOf(false)); //$NON-NLS-1$
		session.setAttribute("closeWindow", Boolean.valueOf(true)); //$NON-NLS-1$
		File zipFile;
		try {
			zipFile = WidgetPackageUtils.dealWithUploadFile(UPLOADFOLDER, request);
		} 
		catch (Exception ex) {
			session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.28") + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return;
		}

		try {	
			if(zipFile.exists()){
				IManifestModel widgetModel = WidgetPackageUtils.processWidgetPackage(zipFile,properties.getString("widget.widgetfolder"), WIDGETFOLDER,UPLOADFOLDER);//$NON-NLS-1$
				if(!Widget.exists(widgetModel.getIdentifier())){	
					// ADD
					int dbkey = manager.addNewWidget(widgetModel, new String[]{});
					// widget added
					session.setAttribute("message_value", "'"+ widgetModel.getLocalName("en") +"' - " + localizedMessages.getString("WidgetAdminServlet.19")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					retrieveServices(session, manager);
					session.setAttribute("hasValidated", Boolean.valueOf(true));																	 //$NON-NLS-1$
					session.setAttribute("dbkey", dbkey); //$NON-NLS-1$
					boolean isMaxable = manager.isWidgetMaximized(dbkey);
					session.setAttribute("isMaxable", Boolean.valueOf(isMaxable)); //$NON-NLS-1$
				}	
				else{
					// UPDATE
					// TODO - call the manager to update required resources
					// widget updated
					session.setAttribute("message_value", "'"+ widgetModel.getLocalName("en") +"' - " + localizedMessages.getString("WidgetAdminServlet.20")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				}					
			}
			else{
				// no file found to be uploaded - shouldn't happen
				session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.24")); //$NON-NLS-1$ //$NON-NLS-2$
			}						
		} 		 
		catch (InvalidStartFileException ex){
			_logger.error(ex);
			session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.27") + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$			
		}
		catch (BadManifestException ex) {
			_logger.error(ex);		
			String message = ex.getMessage();
			if (ex.getMessage() == null || ex.getMessage().equals("")) message = localizedMessages.getString("WidgetAdminServlet.28"); //$NON-NLS-1$
			if (ex instanceof InvalidContentTypeException) message = localizedMessages.getString("WidgetAdminServlet.30");//$NON-NLS-1$
			session.setAttribute("error_value", message); //$NON-NLS-1$
		}
		catch (BadWidgetZipFileException ex) {
			_logger.error(ex);	
			String message = ex.getMessage();
			if (ex.getMessage() == null || ex.getMessage().equals("")) message = localizedMessages.getString("WidgetAdminServlet.29"); //$NON-NLS-1$
			session.setAttribute("error_value", message); //$NON-NLS-1$
		}
		catch (Exception ex) {
			_logger.error(ex);			
			session.setAttribute("error_value", localizedMessages.getString("WidgetAdminServlet.25") + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}


	}



}
