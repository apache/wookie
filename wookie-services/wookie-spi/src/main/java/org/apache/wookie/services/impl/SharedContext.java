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

import java.util.HashMap;

import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.ISharedData;

public class SharedContext {

	private HashMap<String, IParticipant> participants;
	
	private HashMap<String, ISharedData> sharedData;
	
	public SharedContext(){
		participants = new HashMap<String, IParticipant>();
		sharedData = new HashMap<String, ISharedData>();
	}

	/**
	 * @return the participants
	 */
	public HashMap<String, IParticipant> getParticipants() {
		return participants;
	}

	/**
	 * @return the sharedData
	 */
	public HashMap<String, ISharedData> getSharedData() {
		return sharedData;
	}

}
