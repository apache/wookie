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

package org.apache.wookie.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.helpers.WidgetFactory;
import org.apache.wookie.helpers.WidgetAdvertHelper;
import org.apache.wookie.helpers.WidgetImportHelper;
import org.apache.wookie.server.LocaleHandler;
import org.apache.wookie.util.NewWidgetBroadcaster;
import org.apache.wookie.util.W3CWidgetFactoryUtils;
import org.apache.wookie.util.WidgetFileUtils;
import org.apache.wookie.util.WidgetJavascriptSyntaxAnalyzer;
import org.apache.wookie.util.gadgets.GadgetUtils;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.exceptions.InvalidContentTypeException;
import org.apache.wookie.w3c.exceptions.InvalidStartFileException;

/**
 * <p>Controller for widget resources.</p>
 * 
 * <p>Supports the following methods:</p>
 * 
 * <ul>
 * <li>GET widgets - index </li>
 * <li>GET widgets/{id} - show widget</li> 
 * <li>GET widgets/{category} - index widgets for category</li>
 * <li>POST widgets {.wgt file} - import a widget</li>
 * </ul>
 * @author scott
 * 
 */
public class WidgetsController extends Controller{

  private static final long serialVersionUID = 8759704878105474902L;
	
	// Implementation
	
  /* (non-Javadoc)
   * @see org.apache.wookie.controller.Controller#update(java.lang.String, javax.servlet.http.HttpServletRequest)
   */
  @Override
  protected void update(String resourceId, HttpServletRequest request, HttpServletResponse response)
      throws ResourceNotFoundException, InvalidParametersException,
      UnauthorizedAccessException {
    
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    IWidget widget = persistenceManager.findWidgetByGuid(resourceId);
    // attempt to get specific widget by id
    if (widget == null) {
      persistenceManager = PersistenceManagerFactory.getPersistenceManager();
      widget = persistenceManager.findById(IWidget.class, resourceId);
    }
    // return widget result
    if (widget == null) throw new ResourceNotFoundException();
    
    try {
      create(resourceId, request, response);
    } catch (ResourceDuplicationException e) {
      e.printStackTrace();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.wookie.controller.Controller#show(java.lang.String,
   * javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse)
   */
  protected void show(String resourceId, HttpServletRequest request,
      HttpServletResponse response) throws ResourceNotFoundException,
      IOException {

    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    // attempt to get specific widget by URI
    IWidget widget = persistenceManager.findWidgetByGuid(resourceId);
    // attempt to get specific widget by id
    if (widget == null) {
      persistenceManager = PersistenceManagerFactory.getPersistenceManager();
      widget = persistenceManager.findById(IWidget.class, resourceId);
    }
    // return widget result
    if (widget == null)
      throw new ResourceNotFoundException();

    switch (format(request)) {
    case XML:returnXml(WidgetAdvertHelper.createXMLWidgetsDocument(widget,getWookieServerURL(request, "").toString(), getLocales(request)), response);break;
    case WIDGET:returnWidget(widget, response);break;
    default:returnXml(WidgetAdvertHelper.createXMLWidgetsDocument(widget,getWookieServerURL(request, "").toString(), getLocales(request)), response);
    }
  }

  /**
   * Return the specified widget's original .wgt package in the response from a
   * servlet
   * 
   * @param widget
   *          the widget to return
   * @param response
   *          the servlet response
   * @throws IOException
   */
  protected void returnWidget(IWidget widget, HttpServletResponse response)
      throws IOException {
    File widgetFile = new File(widget.getPackagePath());
    InputStream in = new FileInputStream(widgetFile);
    OutputStream out = response.getOutputStream();
    response.setContentType(WIDGET_CONTENT_TYPE);
    response.setHeader("Content-Disposition", "attachment;filename="+widgetFile.getName());
    IOUtils.copy(in, out);
  }

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#index(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void index(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		index("", request, response);
	}

	/**
	 * Returns a list of widgets
	 * @param resourceId
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void index(String resourceId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    IWidget[] widgets = persistenceManager.findAll(IWidget.class);
		returnXml(WidgetAdvertHelper.createXMLWidgetsDocument(widgets, getWookieServerURL(request, "").toString(), getLocales(request)),response);
	}
	
  /* (non-Javadoc)
   * @see org.apache.wookie.controller.Controller#remove(java.lang.String, javax.servlet.http.HttpServletRequest)
   */
  @Override
  protected boolean remove(String resourceId, HttpServletRequest request)
      throws ResourceNotFoundException, UnauthorizedAccessException,
      InvalidParametersException {

    
    //
    // Identify the widget to delete
    //
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    IWidget widget = persistenceManager.findWidgetByGuid(resourceId);
    // attempt to get specific widget by id
    if (widget == null) {
      persistenceManager = PersistenceManagerFactory.getPersistenceManager();
      widget = persistenceManager.findById(IWidget.class, resourceId);
    }
    
    if (widget == null) throw new ResourceNotFoundException();
    
    //
    // Delete the widget
    //
    Configuration properties = (Configuration) getServletContext().getAttribute("properties"); //$NON-NLS-1$
    final String WIDGETFOLDER = getServletContext().getRealPath(properties.getString("widget.widgetfolder"));//$NON-NLS-1$
    return WidgetFactory.destroy(widget, WIDGETFOLDER);
  }

	/**
	 * Install a new Widget by uploading and installing it
	 * Note: a Widget must have a .wgt extension!
	 */
  /* (non-Javadoc)
   * @see org.apache.wookie.controller.Controller#create(java.lang.String, javax.servlet.http.HttpServletRequest)
   */
  @Override
  protected boolean create(String resourceId, HttpServletRequest request, HttpServletResponse response)
      throws ResourceDuplicationException, InvalidParametersException,
      UnauthorizedAccessException {

    //
    // Check for a "url" parameter in the request, indicating this is a remote widget or opensocial gadget xml file 
    //
    String url = request.getParameter("url");
    if (url != null && url.trim().length() != 0){
      return createGadget(request, url);
    }
    
    //
    // Get the path to the upload folder, and the widget install folder
    //
    Configuration properties = (Configuration) getServletContext().getAttribute("properties"); //$NON-NLS-1$
    final String UPLOADFOLDER = getServletContext().getRealPath(properties.getString("widget.useruploadfolder"));//$NON-NLS-1$
    
    //
    // Get localized messages so we can return errors
    //
    Messages localizedMessages = LocaleHandler.localizeMessages(request);
    
    //
    // Try to obtain a zipfile from the request. 
    //
    File zipFile;
    try {
      zipFile = WidgetFileUtils.upload(UPLOADFOLDER, request);
    } catch (Exception ex) {
      throw new InvalidParametersException(localizedMessages.getString("widgets.invalid-config-xml") + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ 
    }
    
    //
    // No file uploaded
    //
    if(zipFile == null || !zipFile.exists()){
      throw new InvalidParametersException(localizedMessages.getString("widgets.no-widget-file-uploaded")); //$NON-NLS-1$
    }
    
    try {

        //
        // Parse and validate the zip as a widget
        //
        W3CWidgetFactory fac = W3CWidgetFactoryUtils.createW3CWidgetFactory(getServletContext(), properties, localizedMessages);        
        W3CWidget widgetModel = fac.parse(zipFile);
        
        String widgetId = widgetModel.getIdentifier();
        
        //
        // If we have a generated ID and resourceId, use the resourceId to override the generated widget identifier. This is a fix for WOOKIE-383
        //
        if (widgetModel.getIdentifier().startsWith("http://incubator.apache.org/wookie/generated/") && resourceId != null){
        	widgetId = resourceId;
        }
                
        new WidgetJavascriptSyntaxAnalyzer(fac.getUnzippedWidgetDirectory());
        
        //
        // Check if the widget model corresponds to an existing installed widget
        //
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        if (persistenceManager.findWidgetByGuid(widgetId) == null) {

            //
            // A new widget was created, so return 201
            //
            WidgetFactory.addNewWidget(widgetModel, zipFile, true);
            NewWidgetBroadcaster.broadcast(properties, widgetModel.getIdentifier());
            returnXml(WidgetImportHelper.createXMLWidgetDocument(widgetModel, new File(fac.getUnzippedWidgetDirectory(), "config.xml"), getWookieServerURL(request, "").toString(), true), response);
            return true;

        } else {

            //
            // Widget already exists, so update the widget metadata and configuration details
            // and return 200
            //
            WidgetFactory.update(widgetModel,persistenceManager.findWidgetByGuid(widgetId), true, zipFile);
            returnXml(WidgetImportHelper.createXMLWidgetDocument(widgetModel, new File(fac.getUnzippedWidgetDirectory(), "config.xml"), getWookieServerURL(request, "").toString(), true), response);
            return false;

        }

        //
        // Catch specific parsing and validation errors and throw exception with error message
        //
    } catch (InvalidStartFileException ex) {
        _logger.error(ex);
        throw new InvalidParametersException(
                localizedMessages.getString("widgets.no-start-file") + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$     
    } catch (BadManifestException ex) {
        _logger.error(ex);
        String message = ex.getMessage();
        if (ex.getMessage() == null || ex.getMessage().equals(""))message = localizedMessages.getString("widgets.invalid-config-xml"); //$NON-NLS-1$
        if (ex instanceof InvalidContentTypeException)
            message = localizedMessages.getString("widgets.unsupported-content-type");//$NON-NLS-1$
        throw new InvalidParametersException(message);
    } catch (BadWidgetZipFileException ex) {
        _logger.error(ex);
        String message = ex.getMessage();
        if (ex.getMessage() == null || ex.getMessage().equals(""))message = localizedMessages.getString("widgets.bad-zip-file"); //$NON-NLS-1$
        throw new InvalidParametersException(message);
    } catch (Exception ex) {
        _logger.error(ex);
        throw new InvalidParametersException(
                localizedMessages.getString("widgets.cant-parse-config-xml") + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

  }
	
	/**
	 * Register a gadget
	 * @param request
	 * @param gadgetUrl
	 * @return true if the gadget is added; false if it was already registered
	 * @throws InvalidParametersException 
	 * @throws Exception
	 */
	public boolean createGadget(HttpServletRequest request, String gadgetUrl) throws InvalidParametersException{

	  //
	  // Create a new widget from the gadget URL
	  //
	  W3CWidget widget;
	  try {
	    widget = GadgetUtils.createWidget(request);
	  } catch (Exception e) {
	    throw new InvalidParametersException();
	  }

	  //
	  // If the gadget is not already registered, add it
	  //
	  IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
	  if(persistenceManager.findWidgetByGuid(widget.getIdentifier()) == null){
	    WidgetFactory.addNewWidget(widget);
	    return true;
	  } else {
	    return false;
	  }
	}
    
}
