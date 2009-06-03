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
package org.tencompetence.widgetservice.beans;

import java.util.Map;

/**
 * A participant entity
 * @author Scott Wilson
 */
public class Participant extends AbstractKeyBean<Participant> {

	private static final long serialVersionUID = 1L;

	public Participant() {
	}

	private String sharedDataKey;
	private String widgetGuid;
	private String participant_id;
	private String participant_display_name;
	private String participant_thumbnail_url;


	/**
	 * @return the participant_id
	 */
	public String getParticipant_id() {
		return participant_id;
	}

	/**
	 * @param participantId the participant_id to set
	 */
	public void setParticipant_id(String participantId) {
		participant_id = participantId;
	}

	/**
	 * @return the participant_display_name
	 */
	public String getParticipant_display_name() {
		return participant_display_name;
	}

	/**
	 * @param participantDisplayName the participant_display_name to set
	 */
	public void setParticipant_display_name(String participantDisplayName) {
		participant_display_name = participantDisplayName;
	}

	/**
	 * @return the participant_thumbnail_url
	 */
	public String getParticipant_thumbnail_url() {
		return participant_thumbnail_url;
	}

	/**
	 * @param participantThumbnailUrl the participant_thumbnail_url to set
	 */
	public void setParticipant_thumbnail_url(String participantThumbnailUrl) {
		participant_thumbnail_url = participantThumbnailUrl;
	}

	/**
	 * @return the sharedDataKey
	 */
	public String getSharedDataKey() {
		return sharedDataKey;
	}

	/**
	 * @param sharedDataKey the sharedDataKey to set
	 */
	public void setSharedDataKey(String sharedDataKey) {
		this.sharedDataKey = sharedDataKey;
	}

	/**
	 * @return the widgetGuid
	 */
	public String getWidgetGuid() {
		return widgetGuid;
	}

	/**
	 * @param widgetGuid the widgetGuid to set
	 */
	public void setWidgetGuid(String widgetGuid) {
		this.widgetGuid = widgetGuid;
	}

	/// Active record methods
	public static Participant findById(Object id){
		return (Participant) findById(Participant.class, id);
	}
	
	public static Participant[] findByValue(String key, Object value) {
		return (Participant[]) findByValue(Participant.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static Participant[] findByValues(Map map) {
		return (Participant[]) findByValues(Participant.class, map);
	}
	
	public static Participant[] findAll(){
		return (Participant[]) findAll(Participant.class);
	}

	
}