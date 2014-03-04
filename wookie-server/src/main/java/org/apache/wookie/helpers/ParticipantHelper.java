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

import org.apache.log4j.Logger;
import org.apache.wookie.beans.IParticipant;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A helper for Participants
 */
public class ParticipantHelper {
  
  static Logger logger = Logger.getLogger(ParticipantHelper.class.getName());
	
	/**
	 * Create XML representation for a single participant
	 * @param participant
	 * @return a String representing the participant in XML
	 */
	public static String createXMLParticipantsDocument(IParticipant participant){
		IParticipant[] participants = {participant};
		return createXMLParticipantsDocument(participants);
	}
	
	/**
	 * Create XML representation of an array of participants
	 * 
	 * @param participants the participants
	 * @return a String representing the participants in XML 
	 */
	public static String createXMLParticipantsDocument(IParticipant[] participants){
		
		Document document = new Document();
		Element root = new Element("participants");
		document.setRootElement(root);
		for (IParticipant participant:participants){
		  root.addContent(toXml(participant));
		}
		return new XMLOutputter().outputString(document);
	}
	
	/**
	 * Create a JSON representation of a single participant
	 * @param participant
	 * @return JSON output as a String of the participant
	 */
	public static String createJSONParticipantDocument(IParticipant participant){
    JSONObject json = new JSONObject();
    try {
      json.put("Participant",toJson(participant));
    } catch (JSONException e) {
      logger.error("Problem marshalling participants to JSON", e);
    }
    return json.toString();
	}
	
	/**
	 * Create a JSON representation of an array of participants
	 * @param participants
	 * @return JSON output as a String
	 */
	public static String createJSONParticipantsDocument(IParticipant[] participants){
	  JSONArray arr = new JSONArray();
		try {
      for (IParticipant participant: participants){
          arr.put(toJson(participant));
      }
      JSONObject json = new JSONObject();
      json.put("Participants", arr);
      return json.toString();
    } catch (JSONException e) {
      logger.error("Problem marshalling participants to JSON", e);
      return null;
    }

	}
	
	/**
	 * Returns an XML element for the given participant.
	 * @param participant
	 * @return an XML Element representing the participant
	 */
	private static Element toXml(IParticipant participant){
	  Element element = new Element("participant");
	  element.setAttribute("id", participant.getParticipantId());
	  element.setAttribute("display_name", participant.getParticipantDisplayName());
	  if (participant.getParticipantThumbnailUrl() != null) element.setAttribute("thumbnail_url", participant.getParticipantThumbnailUrl());
	  if (participant.getRole() != null) element.setAttribute("role", participant.getRole());
	  return element;
	}

	/**
	 * Creates a JSONObject for the given participant
	 * @param participant
	 * @return a JSONObject representing the participant
	 * @throws JSONException 
	 */
	private static JSONObject toJson(IParticipant participant) throws JSONException{
    JSONObject obj = new JSONObject();
    obj.put("participant_id", participant.getParticipantId());
    obj.put("participant_display_name", participant.getParticipantDisplayName());
    obj.put("participant_thumbnail_url", participant.getParticipantThumbnailUrl());
    if (participant.getRole() != null) obj.put("participant_role", participant.getRole());
    return obj;
	}

}
