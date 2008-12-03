/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.widgetservice;

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
import org.tencompetence.widgetservice.beans.WidgetDefault;
import org.tencompetence.widgetservice.manager.IWidgetAdminManager;
import org.tencompetence.widgetservice.manager.impl.WidgetAdminManager;
import org.tencompetence.widgetservice.util.ManifestHelper;
import org.tencompetence.widgetservice.util.StartPageJSParser;
import org.tencompetence.widgetservice.util.ZipUtils;

/**
 * Servlet implementation class for Servlet: WidgetAdminServlet
 * 
 * This servlet handles all requests for Admin tasks
 * 
 * @author Paul Sharples
 * @version $Id: WidgetAdminServlet.java,v 1.8 2008-12-03 15:31:53 ps3com Exp $ 
 *
 */
public class WidgetAdminServlet extends HttpServlet implements Servlet {
	
	// our list of allowed operations
	private enum Operation {
		ADDNEWSERVICE, ADDNEWWHITELISTENTRY, LISTSERVICES, LISTSERVICESFORADDITION, 
		LISTWIDGETS, REMOVESERVICE, REMOVESINGLEWIDGETTYPE, REMOVEWHITELISTENTRY, REMOVEWIDGET,  
		REVISETYPES, SETDEFAULTWIDGET, SETWIDGETTYPES, UPLOADWIDGET, VIEWWHITELIST
	}	
	 	 	
	// Get the logger
	static Logger _logger = Logger.getLogger(WidgetAdminServlet.class.getName());	
	
	// jsp page handles
	private static final String fAddNewServicesPage = "/admin/addnewservice.jsp";	
	private static final String faddToWhiteListPage = "/admin/addtowhitelist.jsp";
	private static final String fListServicesPage = "/admin/listservices.jsp";
	private static final String fListWidgetsForDeletePage = "/admin/listallfordelete.jsp";	
	private static final String fListWidgetsPage = "/admin/listall.jsp";
	private static final String fMainPage = "/admin/index.jsp";
	private static final String fRemoveServicesPage = "/admin/removeservice.jsp";
	private static final String fremoveWhiteListPage = "/admin/removewhitelist.jsp";	
	private static final String fUpLoadResultsPage = "/admin/uploadresults.jsp";
	private static final String fViewWhiteListPage = "/admin/viewwhitelist.jsp";
	
	
	private static final long serialVersionUID = -3026022301561798524L;;	
			
	
    /*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public WidgetAdminServlet() {
		super();
	}   	
	

	private void addNewService(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager) {
		String serviceName = request.getParameter("newservice");
		if(manager.addNewService(serviceName)){	
			session.setAttribute("message_value", "New Service Type was added.");
		}
		else{ 
			session.setAttribute("error_value", "There was a problem adding the new service.");
		}
	}




	private void addWhiteListEntry(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager) {
		String uri = request.getParameter("newuri");
		if(manager.addWhiteListEntry(uri)){
			session.setAttribute("message_value", "New uri was added.");
		}
		else{
			session.setAttribute("error_value", "There was a problem adding the entry.");
		}
	}


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
				case ADDNEWWHITELISTENTRY: {
					addWhiteListEntry(session, request, manager);					
					listWhiteListOperation(session, manager);
					doForward(request, response, faddToWhiteListPage);
					break;
				}
				case VIEWWHITELIST: {
					listWhiteListOperation(session, manager);
					if(request.getParameter("param").equalsIgnoreCase("list")){											
						doForward(request, response, fViewWhiteListPage);
					}
					else if(request.getParameter("param").equalsIgnoreCase("add")){					
						doForward(request, response, faddToWhiteListPage);
					}
					else if(request.getParameter("param").equalsIgnoreCase("remove")){					
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
					if(request.getParameter("param").equalsIgnoreCase("remove")){
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
					if(request.getParameter("param").equalsIgnoreCase("list")){											
						doForward(request, response, fListServicesPage);
					}
					else if(request.getParameter("param").equalsIgnoreCase("add")){					
						doForward(request, response, fAddNewServicesPage);
					}
					else if(request.getParameter("param").equalsIgnoreCase("remove")){					
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
					doForward(request, response, fListWidgetsPage);
					break;
			    }
				case SETDEFAULTWIDGET:{
					setDefaultWidgetOperation(session, request, manager);
					doForward(request, response, fListWidgetsPage);
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
	
	private void listOperation(HttpSession session, IWidgetAdminManager manager, boolean getDefaults){
		retrieveWidgets(session, manager);	
		if(getDefaults){
			Hashtable<String, Integer> defaultHash = new Hashtable<String, Integer>();
			for(WidgetDefault defaultWidget :manager.getAllDefaultWidgets()){
				defaultHash.put(defaultWidget.getWidgetContext(), defaultWidget.getWidgetId());				
			}		
			session.setAttribute("widget_defaults", defaultHash);
		}
	}

	private void listWhiteListOperation(HttpSession session, IWidgetAdminManager manager) {
		session.setAttribute("whitelist", manager.getWhiteList());
	}  	
		
	private void removeServiceOperation(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager) {		
		String serviceId = request.getParameter("serviceId");
		if(manager.removeServiceAndReferences(Integer.parseInt(serviceId))){
			session.setAttribute("message_value", "Service was successfully removed."); 				
		}
		else{
			session.setAttribute("error_value", "A problem occured removing this service.");
		}
		retrieveServices(session, manager);
	}
	
	private void removeSingleWidgetTypeOperation(HttpSession session,
			HttpServletRequest request, IWidgetAdminManager manager) {
		String widgetId = request.getParameter("widgetId");
		String widgetType = request.getParameter("widgetType");	
		manager.removeSingleWidgetType(Integer.parseInt(widgetId), widgetType);
		//
		session.setAttribute("widgets", null);
		listOperation(session, manager, true);		
	}
	
	private void removeWhiteListEntry(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager) {
		String entryId = request.getParameter("entryId");
		if(manager.removeWhiteListEntry(Integer.parseInt(entryId))){
			session.setAttribute("message_value", "Entry was successfully removed."); 				
		}
		else{
			session.setAttribute("error_value", "A problem occured removing this entry.");
		}				
	}
	
	private void removeWidget(HttpSession session, HttpServletRequest request, Configuration properties, IWidgetAdminManager manager) {
		String widgetId = request.getParameter("widgetId");
		String guid = manager.getWidgetGuid(Integer.parseInt(widgetId));
		if(manager.removeWidgetAndReferences(Integer.parseInt(widgetId))){
			if(ManifestHelper.removeWidgetResources(request, properties, guid)){			
				session.setAttribute("message_value", "The widget and its resource was successfully deleted.");			
			}
			else{
				session.setAttribute("error_value", "There was a problem deleting the widget resources.");
			}
		}
		else{
			session.setAttribute("error_value", "There was a problem removing the widget from the system.");
		}
	}		
	
	private void retrieveServices(HttpSession session, IWidgetAdminManager manager){						
		session.setAttribute("services", manager.getAllServices());						
	}
	
	private void retrieveWidgets(HttpSession session, IWidgetAdminManager manager){
		session.setAttribute("widgets", manager.getAllWidgets());
	}

	
	
	private void reviseTypes(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager) {
		retrieveServices(session, manager);
		session.setAttribute("hasValidated", Boolean.valueOf(true));
		session.setAttribute("closeWindow", Boolean.valueOf(false));
		String dbkey = request.getParameter("dbkey");
		boolean isMaxable = manager.isWidgetMaximized(Integer.parseInt(dbkey));
		session.setAttribute("isMaxable", Boolean.valueOf(isMaxable));
		session.setAttribute("dbkey", Integer.parseInt(dbkey));
	}
	
	private void setDefaultWidgetOperation(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager){
		String widgetId = request.getParameter("widgetId");
		String widgetType = request.getParameter("widgetType");		
		manager.setDefaultWidget(Integer.parseInt(widgetId), widgetType);
		listOperation(session, manager, true);
	}
	
	private void updateWidgetTypes(HttpSession session, HttpServletRequest request, IWidgetAdminManager manager) throws IOException{
		boolean canMax = false;
	   
	    String maximize = request.getParameter("max");
	    if(maximize!=null){
	    	canMax = Boolean.valueOf(maximize);
	    }
	    int dbKey = Integer.parseInt(request.getParameter("dbkey"));
	    String[] widgetTypes = request.getParameterValues("widgetTypes");
	    manager.setWidgetTypesForWidget(dbKey, widgetTypes, canMax);
		session.setAttribute("message_value", "Widget types were successfully set"); 		
	}  
	
	
	private void uploadOperation(HttpServletRequest request, Configuration properties, IWidgetAdminManager manager, HttpSession session) {	
		session.setAttribute("hasValidated", Boolean.valueOf(false));
		session.setAttribute("closeWindow", Boolean.valueOf(true));
		try {						
			File zipFile = ManifestHelper.dealWithUploadFile(request, properties);						
			if(zipFile.exists()){
				if(ZipUtils.hasZipEntry(zipFile, ManifestHelper.MANIFEST_FILE)){
					Hashtable<String,String> results = ManifestHelper.dealWithManifest(ZipUtils.extractZipEntry(zipFile, ManifestHelper.MANIFEST_FILE));
					// check if the start file exists
					String src = results.get(ManifestHelper.SOURCE_ATTRIBUTE);
					if(ZipUtils.hasZipEntry(zipFile, src)){
						String uid = results.get(ManifestHelper.UID_ATTRIBUTE);
						if( uid != null && uid != ""){
							File newWidgetFolder = ManifestHelper.createUnpackedWidgetFolder(request, properties, uid);
							ZipUtils.unpackZip(zipFile, newWidgetFolder);
							// get the url to the start page
							String relativestartUrl = (ManifestHelper.getURLForWidget(properties, uid, src));
							String relativeIconUrl=null;
							// get the url path to the icon file
							
							File startFile = new File(newWidgetFolder.getCanonicalPath() + File.separator + src);							
							if(startFile.exists()){
								StartPageJSParser parser = new StartPageJSParser(startFile);
							}
							
							// this is a hack!
							String iconURL = results.get(ManifestHelper.ICON_SOURCE+"_1");
							if (iconURL == null ) {
								relativeIconUrl = "";
							}
							else {
								if (!iconURL.startsWith("http://")){
									relativeIconUrl = (ManifestHelper.getURLForWidget(properties, uid, iconURL));								
								}
								else{
									relativeIconUrl = iconURL;
								}
							}
							// check to see if this widget already exists in the DB - using the ID (guid) key from the manifest
							if(!manager.doesWidgetAlreadyExistInSystem(uid)){	
								
								int dbkey = manager.addNewWidget(relativeIconUrl, relativestartUrl, results, new String[]{});
								session.setAttribute("message_value", "Widget '"+ results.get(ManifestHelper.NAME_ELEMENT) +"' was successfully imported into the system.");
								retrieveServices(session, manager);
								session.setAttribute("hasValidated", Boolean.valueOf(true));																	
								session.setAttribute("dbkey", dbkey);
								boolean isMaxable = manager.isWidgetMaximized(dbkey);
								session.setAttribute("isMaxable", Boolean.valueOf(isMaxable));
							}	
							else{
								session.setAttribute("message_value", "Widget '"+ results.get(ManifestHelper.NAME_ELEMENT) +"' was successfully updated in the system.");
							}
						}
						else{							
							session.setAttribute("error_value", "The id of this widget cannot be empty - please modifiy the manifest xml attribute 'id' of the widget element and try again.");
						}
					}
					else{						
						session.setAttribute("error_value","Referenced start page not found in zip file");
					}
				}
				else{					
					session.setAttribute("error_value","Unable to find manifest file in uploaded content.");
				}
			}
			else{				
				session.setAttribute("error_value","No file found uploaded to server");
			}						
		} 		 
		catch (Exception ex) {
			//ex.printStackTrace();
			_logger.error(ex);			
			session.setAttribute("errors", ex.getMessage());
		}
	}


	
}
 