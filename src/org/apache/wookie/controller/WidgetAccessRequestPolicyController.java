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

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wookie.beans.IAccessRequest;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.helpers.AccessRequestHelper;
import org.apache.wookie.w3c.util.IRIValidator;

/**
 * Controller for working with widget access request policies (WARP)
 */
public class WidgetAccessRequestPolicyController extends Controller {

	private static final long serialVersionUID = 6926162644101308215L;

	@Override
	protected void index(HttpServletRequest request,
			HttpServletResponse response) throws UnauthorizedAccessException,
			IOException {

		IAccessRequest[] accessRequests = null;
		
		String widgetId = request.getParameter("widgetId");
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        if (widgetId == null){
            accessRequests = persistenceManager.findAll(IAccessRequest.class);
        }
		if (widgetId != null && widgetId.trim().length()>0){
			// Filter by widgetId
			IWidget widget = persistenceManager.findById(IWidget.class, widgetId);
            if (widget != null) accessRequests = persistenceManager.findByValue(IAccessRequest.class, "widget", widget);
		}
		
		switch (format(request)) {
			case XML: returnXml(AccessRequestHelper.createXMLAccessRequestDocument(accessRequests),response);break;
			case HTML: returnHtml(AccessRequestHelper.createAccessRequestHTMLTable(accessRequests),response);break;
			case JSON: returnJson(AccessRequestHelper.createJSON(accessRequests),response);break;
		}
	}

	@Override
	protected void show(String resourceId, HttpServletRequest request,
			HttpServletResponse response) throws ResourceNotFoundException,
			UnauthorizedAccessException, IOException {
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IAccessRequest ar = persistenceManager.findById(IAccessRequest.class, resourceId);
		if (ar == null) throw new ResourceNotFoundException();
		
		switch (format(request)) {
			case XML: returnXml(AccessRequestHelper.createXMLAccessRequestDocument(new IAccessRequest[]{ar}),response);break;
			case HTML: returnHtml(AccessRequestHelper.createAccessRequestHTMLTable(new IAccessRequest[]{ar}),response);break;
			case JSON: returnJson(AccessRequestHelper.createJSON(new IAccessRequest[]{ar}),response);break;
		}
	}

	@Override
	protected void update(String resourceId, HttpServletRequest request)
			throws ResourceNotFoundException, InvalidParametersException,
			UnauthorizedAccessException {
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IAccessRequest ar = persistenceManager.findById(IAccessRequest.class, resourceId);
		if (ar == null) throw new ResourceNotFoundException();
		String granted = request.getParameter("granted");
		if (granted == null) throw new InvalidParametersException();
		if (!granted.equals("true") && !granted.equals("false")) throw new InvalidParametersException();
		if (granted.equals("true")) grantAccess(persistenceManager, ar);
		if (granted.equals("false")) revokeAccess(persistenceManager, ar);
	}
	
	@Override
	protected boolean create(String resourceId, HttpServletRequest request)
			throws ResourceDuplicationException, InvalidParametersException,
			UnauthorizedAccessException {
		
		// FIXME check for duplicate policies before adding
		
		String origin;
		try {
			origin = checkOrigin(request.getParameter("origin"));
		} catch (Exception e) {
			throw new InvalidParametersException();
		}
		
		String subdomains = request.getParameter("subdomains");
		
		String widgetId = request.getParameter("widgetId");
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidget widget = persistenceManager.findById(IWidget.class, widgetId);
		if (widget == null) throw new InvalidParametersException();
		
		IAccessRequest ar = persistenceManager.newInstance(IAccessRequest.class);
		ar.setOrigin(origin);
		if (subdomains.equals("true")) ar.setSubdomains(true);
		ar.setGranted(false);
		ar.setWidget(widget);
		return persistenceManager.save(ar);
	}

	@Override
	protected boolean remove(String resourceId, HttpServletRequest request)
			throws ResourceNotFoundException, UnauthorizedAccessException,
			InvalidParametersException {
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IAccessRequest ar = persistenceManager.findById(IAccessRequest.class, resourceId);
		if (ar == null) throw new ResourceNotFoundException();
		return persistenceManager.delete(ar);
	}

	private void grantAccess(IPersistenceManager persistenceManager, IAccessRequest ar){
		ar.setGranted(true);
        persistenceManager.save(ar);
	}
	
	private void revokeAccess(IPersistenceManager persistenceManager, IAccessRequest ar){
		ar.setGranted(false);
		persistenceManager.save(ar);
	}
	
	/**
	 * Checks whether a supplied origin parameter is valid, and returns the processed result
	 * @param origin
	 * @return a processed origin with extraneous elements removed
	 * @throws Exception if the origin is not valid
	 */
	private String checkOrigin(String origin) throws Exception{
		if (origin.equals("*")) return origin;
		if (!IRIValidator.isValidIRI(origin)) throw new Exception("origin is not a valid IRI");
		URI uri = new URI(origin);
		if (uri.getHost() == null) throw new Exception("origin has no host");
		if (uri.getUserInfo()!=null) throw new Exception("origin has userinfo");
		URI processedURI = new URI(uri.getScheme(),null,uri.getHost(),uri.getPort(),null,null,null);
		return processedURI.toString();
	}
}
