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

import org.apache.wookie.beans.AccessRequest;
import org.apache.wookie.beans.Widget;
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

		AccessRequest[] accessRequests = null;
		
		String widgetId = request.getParameter("widgetId");
		if (widgetId == null){
			accessRequests = AccessRequest.findAll();
		}
		if (widgetId != null && widgetId.trim().length()>0){
			// Filter by widgetId
			Widget widget = Widget.findById(widgetId);
			if (widget != null) accessRequests = AccessRequest.findByValue("widget",widget);
		}
		
		switch (format(request)) {
			case XML: returnXml(AccessRequestHelper.createXMLAccessRequestDocument(accessRequests),response);break;
			case HTML: returnHtml(AccessRequestHelper.createAccessRequestHTMLTable(accessRequests),response);break;
		}
	}

	@Override
	protected void show(String resourceId, HttpServletRequest request,
			HttpServletResponse response) throws ResourceNotFoundException,
			UnauthorizedAccessException, IOException {
		AccessRequest ar = AccessRequest.findById(Integer.valueOf(resourceId));
		if (ar == null) throw new ResourceNotFoundException();
		
		switch (format(request)) {
			case XML: returnXml(AccessRequestHelper.createXMLAccessRequestDocument(new AccessRequest[]{ar}),response);break;
			case HTML: returnHtml(AccessRequestHelper.createAccessRequestHTMLTable(new AccessRequest[]{ar}),response);break;
		}
	}

	@Override
	protected void update(String resourceId, HttpServletRequest request)
			throws ResourceNotFoundException, InvalidParametersException,
			UnauthorizedAccessException {
		AccessRequest ar = AccessRequest.findById(Integer.valueOf(resourceId));
		if (ar == null) throw new ResourceNotFoundException();
		String granted = request.getParameter("granted");
		if (granted == null) throw new InvalidParametersException();
		if (!granted.equals("true") && !granted.equals("false")) throw new InvalidParametersException();
		if (granted.equals("true")) grantAccess(ar);
		if (granted.equals("false")) revokeAccess(ar);
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
		Widget widget = Widget.findById(Integer.valueOf(widgetId));
		if (widget == null) throw new InvalidParametersException();
		
		AccessRequest ar = new AccessRequest();
		ar.setOrigin(origin);
		if (subdomains.equals("true")) ar.setSubdomains(true);
		ar.setGranted(false);
		ar.setWidget(widget);
		return ar.save();
	}

	@Override
	protected boolean remove(String resourceId, HttpServletRequest request)
			throws ResourceNotFoundException, UnauthorizedAccessException,
			InvalidParametersException {
		AccessRequest ar = AccessRequest.findById(Integer.valueOf(resourceId));
		if (ar == null) throw new ResourceNotFoundException();
		return ar.delete();
	}

	private void grantAccess(AccessRequest ar){
		ar.setGranted(true);
		ar.save();
	}
	
	private void revokeAccess(AccessRequest ar){
		ar.setGranted(false);
		ar.save();
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
