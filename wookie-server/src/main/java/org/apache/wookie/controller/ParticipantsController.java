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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.SharedContext;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.ParticipantHelper;
import org.apache.wookie.services.WidgetMetadataService;

/**
 * Implementation of the REST API for working with Participants. For a description of the methods implemented by this controller see 
 * http://incubator.apache.org/wookie/docs/api.html
 *
 */
public class ParticipantsController extends Controller {

	private static final long serialVersionUID = 308590474406800659L;		
	static Logger _logger = Logger.getLogger(ParticipantsController.class.getName());	 	

	/**
	 * We only override doGet as we don't use a REST resource but the instance params to 
	 * locate the resource
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{					
		try {
			show("", request, response);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (ResourceNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (UnauthorizedAccessException e){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}
	
	// Implementation
	
	@Override
	public void show(String resourceId,HttpServletRequest request, HttpServletResponse response) throws UnauthorizedAccessException,ResourceNotFoundException, IOException{
		
		AuthToken authToken = getAuthTokenFromRequest(request);
		if (authToken == null) throw new ResourceNotFoundException();
		IParticipant[] participants = new SharedContext(authToken).getParticipants();
		
		switch (format(request)) {
		case XML: returnXml(ParticipantHelper.createXMLParticipantsDocument(participants),response);break;
		case JSON: returnJson(ParticipantHelper.createJSONParticipantsDocument(participants),response);break;
		default: returnXml(ParticipantHelper.createXMLParticipantsDocument(participants),response);break;
		}
	}

	@Override
	protected boolean create(String resourceId, HttpServletRequest request, HttpServletResponse response)
			throws ResourceDuplicationException, InvalidParametersException,
			UnauthorizedAccessException {
		return create(request);
	}
		
	/**
	 * Add a participant to a widget.
	 * 
	 * @param request
	 * @return
	 * @throws ResourceDuplicationException
	 * @throws InvalidParametersException
	 * @throws UnauthorizedAccessException
	 */
	public static boolean create(HttpServletRequest request)
			throws ResourceDuplicationException, InvalidParametersException,
			UnauthorizedAccessException {

		//
		// Verify the auth token
		//
		AuthToken authToken = getAuthTokenFromRequest(request);
		if (authToken == null) throw new InvalidParametersException();
		
		//
		// Check the widget is real
		//
		if (WidgetMetadataService.Factory.getInstance().getWidget(authToken.getWidgetId()) == null) throw new InvalidParametersException();
		
		HttpSession session = request.getSession(true);						
		String participantId = request.getParameter("participant_id"); //$NON-NLS-1$
		String participantDisplayName = request.getParameter("participant_display_name"); //$NON-NLS-1$
		String participantThumbnailUrl = request.getParameter("participant_thumbnail_url"); //$NON-NLS-1$
		String participantRole = request.getParameter("participant_role"); 
		
		// Check required params
		if (participantId == null || participantId.trim().equals("")) {
			_logger.error("participant_id parameter cannot be null");
			throw new InvalidParametersException();
		}

		if (new SharedContext(authToken).addParticipant(participantId, participantDisplayName, participantThumbnailUrl, participantRole)){
			Notifier.notifyWidgets(session, authToken, Notifier.PARTICIPANTS_UPDATED);
			_logger.debug("added user to widget instance: " + participantId);
			return true;
		} else {
			// No need to create a new participant, it already existed
			return false;
		}
	}
	
	@Override
	protected boolean remove(String resourceId, HttpServletRequest request)
	throws ResourceNotFoundException, UnauthorizedAccessException,
	InvalidParametersException {
		return remove(request);
	}
	public static boolean remove(HttpServletRequest request)
			throws ResourceNotFoundException, UnauthorizedAccessException,
			InvalidParametersException {
		
		//
		// Verify the auth token
		//
		AuthToken authToken = getAuthTokenFromRequest(request);
		if (authToken == null) throw new InvalidParametersException();
		
		//
		// Check the widget is real
		//
		if (WidgetMetadataService.Factory.getInstance().getWidget(authToken.getWidgetId()) == null) throw new InvalidParametersException();
		
		HttpSession session = request.getSession(true);						
		String participantId = request.getParameter("participant_id"); //$NON-NLS-1$
		if(new SharedContext(authToken).removeParticipant(participantId)){
			Notifier.notifyWidgets(session, authToken, Notifier.PARTICIPANTS_UPDATED);
			return true;
		}else{
			throw new ResourceNotFoundException();				
		}
	}
}