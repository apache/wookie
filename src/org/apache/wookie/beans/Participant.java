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

package org.apache.wookie.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * A participant entity
 * @author Scott Wilson
 * @version $Id: Participant.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $ 
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
	/// Special queries
	public static Participant[] getParticipants(WidgetInstance instance) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());	//$NON-NLS-1$
		map.put("widgetGuid", instance.getWidget().getGuid());	//$NON-NLS-1$
		return findByValues(map);
	}

	public static Participant getViewer(WidgetInstance instance) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());	//$NON-NLS-1$
		map.put("widgetGuid", instance.getWidget().getGuid());	//$NON-NLS-1$
		map.put("participant_id", instance.getUserId());	//$NON-NLS-1$
		Participant[] participants = findByValues(map);
		if (participants == null || participants.length != 1) return null;
		return participants[0];
	}

	
}