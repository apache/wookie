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
package org.apache.wookie.updates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.beans.IServerFeature;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.controller.Controller;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.helpers.FlashMessage;
import org.apache.wookie.helpers.WidgetFactory;
import org.apache.wookie.util.html.StartPageProcessor;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.exceptions.InvalidContentTypeException;
import org.apache.wookie.w3c.updates.UpdateDescriptionDocument;
import org.apache.wookie.w3c.updates.UpdateUtils;

/**
 * Controller for managing widget updates
 * 
 * GET: gets the list of updates available
 * GET/{internal_widget_id} : redirects you to the UDD for the widget
 * POST: attempts to apply ALL available updates
 * PUT/{internal_widget_id} : applies available update to specified widget only
 */
public class UpdatesController extends Controller {

	private static final long serialVersionUID = 5891956245633379750L;
	
	static Logger _logger = Logger.getLogger(UpdatesController.class.getName());

	/**
	 * A GET request with no path arguments will return the list of all updates available for
	 * all widgets. 
	 * TODO Note that this call can take a while to complete so it may be better in future to make the call asynchronous, or to schedule update checks and then cache the results
	 */
	@Override
	protected void index(HttpServletRequest request,
			HttpServletResponse response) throws UnauthorizedAccessException,
			IOException {
		List<UpdateInformation> updates = getAllUpdates();
		response.setStatus(HttpServletResponse.SC_OK);
		returnXml(UpdatesHelper.createXML(updates),response);
	}

	/**
	 * A GET request with a resource part requests a redirect to the Update Description Document for
	 * the widget specified in the resource path, if one is available - otherwise the call will return 
	 * a 404 status code.
	 */
	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#show(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void show(String resourceId, HttpServletRequest request,
			HttpServletResponse response) throws ResourceNotFoundException,
			UnauthorizedAccessException, IOException {
		// attempt to get specific widget by id; note that this is the internal
		// widget integer ID and not the widget URI
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidget widget = persistenceManager.findById(IWidget.class, resourceId);
		if (widget == null) throw new ResourceNotFoundException();
		// redirect to the UDD
		if (widget.getUpdateLocation() ==  null) throw new ResourceNotFoundException();
		response.sendRedirect(widget.getUpdateLocation());
	}

	/**
	 * A POST  requests all updates available to be installed.
	 * TODO Note that this call can take a while to complete so it may be better in future to make the call asynchronous and spawn a background task to complete the update process
	 */
	@Override
	protected boolean create(String resourceId, HttpServletRequest request)
			throws ResourceDuplicationException, InvalidParametersException,
			UnauthorizedAccessException {
			// Check to see if we're requiring updates over HTTPS - if not output a warning
			boolean onlyUseHttps = Boolean.parseBoolean(request.getParameter("use-https"));
			if (!onlyUseHttps) _logger.warn("checking for updates using non-secure method");
			// Get all installed widgets
			IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
			IWidget[] widgets = persistenceManager.findAll(IWidget.class);
			// Create a W3CWidget factory for the current context
			W3CWidgetFactory factory  = getFactory(request.getSession().getServletContext());
			// Iterate over the widgets and attempt to install updates
			for (IWidget widget: widgets){
				try {
					installUpdate(factory, widget, onlyUseHttps);
				} catch (Exception e) {
					_logger.warn(e.getMessage(), e);
				}
			}	
			return true;
	}

	/**
	 * A PUT requests a single widget to be updated if there is an update available.
	 */
	@Override
	protected void update(String resourceId, HttpServletRequest request)
			throws ResourceNotFoundException, InvalidParametersException,
			UnauthorizedAccessException {
			// attempt to get specific widget by id
			IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
			IWidget widget = persistenceManager.findById(IWidget.class, resourceId);
			if (widget == null) throw new ResourceNotFoundException();
			// FIXME localize error messages
			try {
				W3CWidgetFactory factory  = getFactory(request.getSession().getServletContext());
				installUpdate(factory, widget, false);
			} catch (IOException e) {
				_logger.warn("Problem updating "+resourceId+": widget couldn't be downloaded");
				throw new InvalidParametersException();
			} catch (InvalidContentTypeException e) {
				_logger.warn("Problem updating "+resourceId+": incorrect content type");
				throw new InvalidParametersException();
			} catch (BadWidgetZipFileException e) {
				_logger.warn("Problem updating "+resourceId+": update is an invalid widget package");
				throw new InvalidParametersException();
			} catch (BadManifestException e) {
				_logger.warn("Problem updating "+resourceId+": update has an invalid config.xml");
				throw new InvalidParametersException();
			} catch (Exception e) {
				_logger.warn("Problem updating "+resourceId+": "+e.getMessage());
				throw new InvalidParametersException();
			}
	}
	
	/**
	 * Installs an update only for the specified widget
	 * @param factory a W3CWidgetFactory configured for this server
	 * @param widget the Widget to update
	 * @param doc the UpdateDescriptionDocument for the Widget update
	 * @param onlyUseHttps true to only install over HTTPS
	 * @throws Exception 
	 * @throws IOException 
	 * @throws BadManifestException 
	 * @throws BadWidgetZipFileException 
	 * @throws InvalidContentTypeException 
	 */
	// FIXME localize messages
	private void installUpdate(W3CWidgetFactory factory, IWidget widget, boolean onlyUseHttps) throws InvalidContentTypeException, BadWidgetZipFileException, BadManifestException, IOException, Exception{
		W3CWidget updatedWidget = UpdateUtils.getUpdate(factory, widget.getGuid(), widget.getUpdateLocation(), widget.getVersion(), onlyUseHttps);
		if (updatedWidget != null){
			WidgetFactory.update(updatedWidget, widget, false);
			_logger.info("Successfully updated "+widget.getGuid()+" to version "+updatedWidget.getVersion());
			FlashMessage.getInstance().message("Successfully updated "+widget.getGuid()+" to version "+updatedWidget.getVersion());
		}
	}
	
	/**
	 * Obtain a W3CWidgetFactory configured for this servlet context
	 * @param context
	 * @return the factory
	 * @throws IOException
	 */
	private W3CWidgetFactory getFactory(ServletContext context){
		Configuration properties = (Configuration) context.getAttribute("properties"); //$NON-NLS-1$
		W3CWidgetFactory factory = new W3CWidgetFactory();
		final String[] locales = properties.getStringArray("widget.locales");
		factory.setLocales(locales);
		factory.setLocalPath(context.getContextPath()+properties.getString("widget.widgetfolder"));
		final String WIDGETFOLDER = context.getRealPath(properties.getString("widget.widgetfolder"));//$NON-NLS-1$
		try {
			factory.setOutputDirectory(WIDGETFOLDER);
		} catch (IOException e) {
			_logger.error(e);
		}
		// Configure the widget factory with the installed feature set
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IServerFeature[] features = persistenceManager.findAll(IServerFeature.class);
		String[] featureNames = new String[features.length];
		for (int i=0;i<features.length;i++){
			featureNames[i] = features[i].getFeatureName();
		}
		factory.setFeatures(featureNames);
		factory.setStartPageProcessor(new StartPageProcessor());
		return factory;
	}
	
	/**
	 * Get available updates for all installed widgets. Note that this method takes a long
	 * time to return as it has to poll all the available update sites, so where possible
	 * we ought to cache the returned updates
	 * @return a list containing all the updates available.
	 */
	public List<UpdateInformation> getAllUpdates(){
		ArrayList<UpdateInformation> updates = new ArrayList<UpdateInformation>();
		// Get all installed widgets
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidget[] widgets = persistenceManager.findAll(IWidget.class);
		for (IWidget widget: widgets){
			// Check for a valid update document; if there is one, create a new UpdateInformation object and add to list
			UpdateDescriptionDocument udd = UpdateUtils.checkForUpdate(widget.getUpdateLocation(), widget.getVersion());
			if (udd != null){
				UpdateInformation info = new UpdateInformation();
				info.setUpdateDescriptionDocument(udd);
				info.setWidget(widget);
				updates.add(info);
			}
		}	
		return updates;
	}
	

}
