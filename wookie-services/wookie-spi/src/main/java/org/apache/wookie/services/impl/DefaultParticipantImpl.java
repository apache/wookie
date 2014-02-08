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
package org.apache.wookie.services.impl;

import org.apache.wookie.beans.IParticipant;

public class DefaultParticipantImpl implements IParticipant{

	private String participantId;
	private String participantName;
	private String participantThumbnailUrl;
	private String role;
	
	
	public DefaultParticipantImpl(String id, String name, String src, String role){
		this.participantId = id;
		this.participantName = name;
		this.participantThumbnailUrl = src;
		this.role = role;
	}
	
	/**
	 * @return the participantId
	 */
	public String getParticipantId() {
		return participantId;
	}
	/**
	 * @param participantId the participantId to set
	 */
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	/**
	 * @return the participantName
	 */
	public String getParticipantDisplayName() {
		return participantName;
	}
	/**
	 * @param participantName the participantName to set
	 */
	public void setParticipantDisplayName(String participantName) {
		this.participantName = participantName;
	}
	/**
	 * @return the participantThumbnailUrl
	 */
	public String getParticipantThumbnailUrl() {
		return participantThumbnailUrl;
	}
	/**
	 * @param participantThumbnailUrl the participantThumbnailUrl to set
	 */
	public void setParticipantThumbnailUrl(String participantThumbnailUrl) {
		this.participantThumbnailUrl = participantThumbnailUrl;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public Object getId() {
		return null;
	}
	@Override
	public String getSharedDataKey() {
		return null;
	}
	@Override
	public void setSharedDataKey(String sharedDataKey) {		
	}

}
