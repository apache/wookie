/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
 /**
 * The Controller object
 * This is used to wire up the view and model with actions
 */ 
var Controller = {
	 	
	user: {},
	updateUser:function() {
		if (wave.getViewer() != null){
			Controller.user.id = wave.getViewer().getId();
			Controller.user.username = wave.getViewer().getDisplayName();
			Controller.user.thumbnail  = wave.getViewer().getThumbnailUrl();
		}
		if (Controller.user.thumbnail == "" || Controller.user.thumbnail == null) { 
			Controller.user.thumbnail = "anon.png";
		}
		if (Controller.user.username == null || Controller.user.username == "") {
			Controller.user.username = "anonymous";        
			Controller.user.id = "anonymous";
		}
	},
	
	stateUpdated:function() {
		Controller.count = parseInt(wave.getState().get("update-count"));
		var elemContent = document.getElementById("wookie-content");
		var countText = "<p>Update count is now " + Controller.count + "</p>";
		var updateButton = "<p><input type='submit' class='wookie-form-button' value='Update Count' onclick='Controller.updateCount()' /></p>";
		elemContent.innerHTML = countText + updateButton;
		
		var date = new Date();
		wookieSetFooter("State Updated " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
	},
	
	count: 0,
	updateCount:function() {
		Controller.count = Controller.count + 1;
		wave.getState().submitValue("update-count", JSON.stringify(Controller.count));
	},
	
	resetCount:function() {
		Controller.count = 0;
		wave.getState().submitValue("update-count", JSON.stringify(Controller.count));
	},
		
	participantsUpdated:function() {
		wookieSetFooter("Participants Updated");
	},
	    
	init:function() {
		Controller.updateUser();
		wave.getState().submitValue("update-count", JSON.stringify(Controller.count));
		/**
		 * Register the event handlers with the Wave feature
		 */
	    wave.setStateCallback(Controller.stateUpdated);
		wave.setParticipantCallback(Controller.participantsUpdated);
		Widget.preferences.setItem("content", "<p>Welcome to @widget.shortname@, " + Controller.user.username + "</p>");
		wookieShowMain(null);
	}
}