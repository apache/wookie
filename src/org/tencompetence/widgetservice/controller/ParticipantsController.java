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
package org.tencompetence.widgetservice.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.beans.Participant;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.helpers.Notifier;
import org.tencompetence.widgetservice.helpers.WidgetKeyManager;

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
		} else {
			HttpSession session = request.getSession(true);						
			String participant_id = request.getParameter("participant_id"); //$NON-NLS-1$
			String participant_display_name = request.getParameter("participant_display_name"); //$NON-NLS-1$
			String participant_thumbnail_url = request.getParameter("participant_thumbnail_url"); //$NON-NLS-1$
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