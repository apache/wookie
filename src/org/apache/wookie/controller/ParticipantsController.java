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
import org.apache.wookie.beans.Participant;
import org.apache.wookie.beans.WidgetInstance;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.WidgetKeyManager;

/**
 * Participant REST Implementation
 * Methods: 
 * 	POST (participant, instance) add a participant
 *  DELETE (participant) deletes a participant
 * Security:
 * 	Requires API Key
 * @author Scott Wilson
 *
 */
public class ParticipantsController extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private static final long serialVersionUID = 308590474406800659L;		
	static Logger _logger = Logger.getLogger(ParticipantsController.class.getName());	

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ParticipantsController() {
		super();
	}   	

	/**
	 * There is no default action for GET; we check for a command param and re-route to POST or DELETE
	 */
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response){					
		if (!WidgetKeyManager.isValidRequest(request)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			try {
				String requestId = request.getParameter("requestid"); //$NON-NLS-1$
				if (requestId == null || requestId.equals("")){
					response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				} else {
					if(requestId.equals("addparticipant")){ //$NON-NLS-1$
						doPost(request, response );
					} else if(requestId.equals("removeparticipant")){ //$NON-NLS-1$
						doDelete(request, response );
					} else {
						response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					}
				}
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}


	/**
	 * The POST method is used to create a new Participant
	 */
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		addParticipant(request, response);
	}

	/**
	 * DELETE removes a participant
	 */
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		removeParticipant(request, response);
	}
	
	// Implementation
	
	public static void addParticipant(HttpServletRequest request, HttpServletResponse response){
		if (!WidgetKeyManager.isValidRequest(request)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		} else {
			HttpSession session = request.getSession(true);						
			String participant_id = request.getParameter("participant_id"); //$NON-NLS-1$
			String participant_display_name = request.getParameter("participant_display_name"); //$NON-NLS-1$
			String participant_thumbnail_url = request.getParameter("participant_thumbnail_url"); //$NON-NLS-1$
			// Check required params
			if (participant_id == null || participant_id.trim().equals("")){
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			
			WidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
			if(instance != null){
				if (addParticipantToWidgetInstance(instance, participant_id, participant_display_name, participant_thumbnail_url)){
					Notifier.notifyWidgets(session, instance, Notifier.PARTICIPANTS_UPDATED);
					response.setStatus(HttpServletResponse.SC_CREATED);
				} else {
					// No need to create a new participant, it already existed
					response.setStatus(HttpServletResponse.SC_OK);
				}
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}
	
	public static void removeParticipant(HttpServletRequest request, HttpServletResponse response){
		if (!WidgetKeyManager.isValidRequest(request)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			HttpSession session = request.getSession(true);						
			String participant_id = request.getParameter("participant_id"); //$NON-NLS-1$
			WidgetInstance instance = WidgetInstancesController.findWidgetInstance(request);
			if(instance != null){
				if(removeParticipantFromWidgetInstance(instance, participant_id)){
					Notifier.notifyWidgets(session, instance, Notifier.PARTICIPANTS_UPDATED);
					response.setStatus(HttpServletResponse.SC_OK);
				}else{
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);					
				}
			}else{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	public static boolean addParticipantToWidgetInstance(WidgetInstance instance,
			String participantId, String participantDisplayName,
			String participantThumbnailUrl) {

		// Does participant already exist?
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());
		map.put("widgetGuid", instance.getWidget().getGuid());
		map.put("participant_id", participantId);
		if (Participant.findByValues(map).length != 0) return false;		

		// Add participant
		Participant participant = new Participant();
		participant.setParticipant_id(participantId);
		participant.setParticipant_display_name(participantDisplayName);
		participant.setParticipant_thumbnail_url(participantThumbnailUrl);
		participant.setSharedDataKey(instance.getSharedDataKey());
		participant.setWidgetGuid(instance.getWidget().getGuid());
		participant.save();
		return true;
	}

	public static boolean removeParticipantFromWidgetInstance(WidgetInstance instance,
			String participantId) {
		Participant[] participants;
		// Does participant exist?
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());
		map.put("widgetGuid", instance.getWidget().getGuid());
		map.put("participant_id", participantId);
		participants = Participant.findByValues(map);
		if (participants.length != 1) return false;	
		// Remove participant
		Participant.delete(participants[0]);
		return true;
	}
}