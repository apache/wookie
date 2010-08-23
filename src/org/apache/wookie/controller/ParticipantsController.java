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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.ParticipantHelper;
import org.apache.wookie.helpers.WidgetKeyManager;

/**
 * Implementation of the REST API for working with Participants. For a description of the methods implemented by this controller see 
 * http://incubator.apache.org/wookie/wookie-rest-api.html 
 * @author Scott Wilson
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
		if (!WidgetKeyManager.isValidRequest(request)) throw new UnauthorizedAccessException();
		IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
		if (instance == null) throw new ResourceNotFoundException();
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IParticipant[] participants = persistenceManager.findParticipants(instance);
		returnXml(ParticipantHelper.createXMLParticipantsDocument(participants), response);
	}

	@Override
	protected boolean create(String resourceId, HttpServletRequest request)
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
		if (!WidgetKeyManager.isValidRequest(request)) throw new UnauthorizedAccessException();

		IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
		if (instance == null) throw new InvalidParametersException();
		
		HttpSession session = request.getSession(true);						
		String participantId = request.getParameter("participant_id"); //$NON-NLS-1$
		String participantDisplayName = request.getParameter("participant_display_name"); //$NON-NLS-1$
		String participantThumbnailUrl = request.getParameter("participant_thumbnail_url"); //$NON-NLS-1$
		
		// Check required params
		if (participantId == null || participantId.trim().equals("")) {
			_logger.error("participant_id parameter cannot be null");
			throw new InvalidParametersException();
		}

		if (addParticipantToWidgetInstance(instance, participantId, participantDisplayName, participantThumbnailUrl)){
			Notifier.notifyWidgets(session, instance, Notifier.PARTICIPANTS_UPDATED);
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
		if (!WidgetKeyManager.isValidRequest(request)) throw new UnauthorizedAccessException();
		IWidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
		if (instance == null) throw new InvalidParametersException();
		HttpSession session = request.getSession(true);						
		String participantId = request.getParameter("participant_id"); //$NON-NLS-1$
		if(removeParticipantFromWidgetInstance(instance, participantId)){
			Notifier.notifyWidgets(session, instance, Notifier.PARTICIPANTS_UPDATED);
			return true;
		}else{
			throw new ResourceNotFoundException();				
		}
	}

	/**
	 * Add a participant to a widget instance
	 * @param instance the widget instance
	 * @param participantId the id property of the participant to add
	 * @param participantDisplayName the display name property of the participant to add
	 * @param participantThumbnailUrl the thumbnail url property of the participant to add
	 * @return true if the participant was successfully added, otherwise false
	 */
	public static boolean addParticipantToWidgetInstance(IWidgetInstance instance,
			String participantId, String participantDisplayName,
			String participantThumbnailUrl) {

		// Does participant already exist?
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());//$NON-NLS-1$
		map.put("widget", instance.getWidget());//$NON-NLS-1$
		map.put("participantId", participantId);//$NON-NLS-1$
		if (persistenceManager.findByValues(IParticipant.class, map).length != 0) return false;		

		// Add participant
		IParticipant participant = persistenceManager.newInstance(IParticipant.class);
		participant.setParticipantId(participantId);
		participant.setParticipantDisplayName(participantDisplayName);
		participant.setParticipantThumbnailUrl(participantThumbnailUrl);
		participant.setSharedDataKey(instance.getSharedDataKey());
		participant.setWidget(instance.getWidget());
		persistenceManager.save(participant);
		return true;
	}

	/**
	 * Removes a participant from a widget instance
	 * @param instance the instance from which to remove the participant
	 * @param participantId the id property of the participant
	 * @return true if the participant is successfully removed, otherwise false
	 */
	private static boolean removeParticipantFromWidgetInstance(IWidgetInstance instance,
			String participantId) {
		IParticipant[] participants;
		// Does participant exist?
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());//$NON-NLS-1$
		map.put("widget", instance.getWidget());//$NON-NLS-1$
		map.put("participantId", participantId);//$NON-NLS-1$
		participants = persistenceManager.findByValues(IParticipant.class, map);
		if (participants.length != 1) return false;	
		// Remove participant
		persistenceManager.delete(participants[0]);
		return true;
	}
}