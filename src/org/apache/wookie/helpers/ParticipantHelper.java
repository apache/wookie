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
package org.apache.wookie.helpers;

import org.apache.wookie.beans.IParticipant;

/**
 * A helper for Participants
 * @author scott wilson
 *
 */
public class ParticipantHelper {
	
	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	/**
	 * Generate a Participants representation doc in XML for a single participant
	 * @param participant
	 * @return
	 */
	public static String createXMLParticipantsDocument(IParticipant participant){
		IParticipant[] participants = {participant};
		return createXMLParticipantsDocument(participants);
	}
	
	/**
	 * Generate a Widgets representation doc in XML for an array of widgets, e.g. a catalogue
	 * 
	 * @param widgets
	 * @param localIconPath
	 * @return
	 */
	public static String createXMLParticipantsDocument(IParticipant[] participants){
		String document = XMLDECLARATION;
		document += "\n<participants>\n"; //$NON-NLS-1$
		for (IParticipant participant:participants){
			document += toXml(participant);
		}
		document += "</participants>\n"; //$NON-NLS-1$
		return document;
	}
	
	public static String createJSONParticipantDocument(IParticipant participant){
		 return "{\"Participant\":"+ParticipantHelper.toJson(participant)+"}"; //$NON-NLS-1$
	}
	
	public static String createJSONParticipantsDocument(IParticipant[] participants){
		String json = "{\"Participants\":[";//$NON-NLS-1$
		String delimit = "";
		for (IParticipant participant: participants){
			json+=delimit+toJson(participant);
			delimit = ","; //$NON-NLS-1$
		}
		json+="]}"; //$NON-NLS-1$
		return json;
	}
	
	/**
	 * Returns an XML representation of the given participant.
	 * 
	 * @param participant the participant to represent
	 * @return the XML representation of the participant
	 */
	public static String toXml(IParticipant participant){
			return "<participant id=\""+participant.getParticipantId()+
								"\" display_name=\""+participant.getParticipantDisplayName()+
								"\" thumbnail_url=\""+participant.getParticipantThumbnailUrl()+
					"\" />"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	/**
	 * Converts a participant to JSON representation
	 * @param participant
	 * @return
	 */
	public static String toJson(IParticipant participant){
		String json = "{"+
		"\"participant_id\":\""+participant.getParticipantId()+
		"\", \"participant_display_name\":\""+participant.getParticipantDisplayName()+
		"\", \"participant_thumbnail_url\":\""+participant.getParticipantThumbnailUrl()+"\"}";
		return json; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

}
