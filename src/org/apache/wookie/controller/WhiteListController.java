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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wookie.beans.IWhitelist;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.helpers.WhitelistHelper;
import org.apache.wookie.w3c.util.IRIValidator;

/**
 * Controller for Whitelist entries
 *
 */
public class WhiteListController extends Controller {

	private static final long serialVersionUID = 5024023589608473984L;

	@Override
	protected void index(HttpServletRequest request,
			HttpServletResponse response) throws UnauthorizedAccessException,
			IOException {
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWhitelist[] entries = persistenceManager.findAll(IWhitelist.class);
        
		switch (format(request)) {
			case XML: returnXml(WhitelistHelper.createXMLDocument(entries),response);break;
			case JSON: returnHtml(WhitelistHelper.createJSON(entries),response);break;
			case HTML: returnHtml(WhitelistHelper.createHTML(entries),response);break;
		}
	}

	@Override
	protected boolean create(String resourceId, HttpServletRequest request)
			throws ResourceDuplicationException, InvalidParametersException,
			UnauthorizedAccessException {
		
		String url = request.getParameter("url");
		if (url == null || url.trim().length() == 0) throw new InvalidParametersException();
		if (!IRIValidator.isValidIRI(url)) throw new InvalidParametersException();
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		// Check for duplicates
		IWhitelist[] matches = persistenceManager.findByValue(IWhitelist.class, "fUrl", url);
		if (matches.length != 0) throw new ResourceDuplicationException();
		IWhitelist entry = persistenceManager.newInstance(IWhitelist.class);
		entry.setfUrl(url);
		return persistenceManager.save(entry);
	}

	@Override
	protected boolean remove(String resourceId, HttpServletRequest request)
			throws ResourceNotFoundException, UnauthorizedAccessException,
			InvalidParametersException {
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWhitelist entry = persistenceManager.findById(IWhitelist.class, resourceId);
        if (entry == null)throw new ResourceNotFoundException();
        return persistenceManager.delete(entry);     	
	}
	
	

}
