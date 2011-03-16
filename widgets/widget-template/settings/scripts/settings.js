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
 * A setting to represent in the settings form.
 * 
 * @param id is used to identiy the setting and the form element created for it.
 * @param label is the text to place in the label for the form element.
 * @param placeholder is the text that will appear in the form element if the setting has not been set
 * @param type is the type of data to be stored in this setting, it is used to define the form element to use
 */
function Setting(id, label, placeholder, type){
	this.id = id;
    this.label = label;
    this.placeholder = placeholder
    this.type = type;
}

/**
 * The SettingService is where we retrieve and store details about the Settings in
 * the widget.
 */
var SettingsService = {
	/**
	 * Get the names of the available settings groups.
	 * Groups are used to describe related sets of settings. When the
	 * settings page is requested the user will be presented with a
	 * list of links to individual settings pages for each group. If
	 * no groups are defined then all settings will be displayed on a 
	 * single settings page.
	 * 
	 * @return an array setting group names or an empty array if no groups are defined.
	 */
	getGroups: function() {
		var groups = [];
		groups[0] = "about";
		groups[1] = "contact";
		return groups;
    },
	    
	/**
	 * Get the settings in a given group. Note that the group names here must correspond
	 * to the group names returned by SettingsService.getSettings()
	 * 
	 * @return an array containing a number of Setting objects
	 */
	getSettings: function(group) {
		var settings = [];
		if (group == undefined || group == "about") {
			settings[0] = new Setting("name", "Name", "The name you wish to be known by.", "text");
			settings[1] = new Setting("description", "Description", "A short description that will be shown to people viewing your details.", "textarea");
		} else if (group == "contact") {
			settings[0] = new Setting("homepageURL", "Homepage URL", "Your homepage URL.", "email");
			settings[1] = new Setting("email", "EMail", "Your email address.", "email");
		}
		return settings;
    },
    
	/**
	 * Process a settings form that has been submitted by storing all the settings
	 * in widget preferences. If settings groups are being used then we only process
	 * settings in that group.
	 * 
	 * @param group the name of the group to process if groups are being used
	 */
	submitSettingsForm: function(group) {
	  var settings = SettingsService.getSettings(group);
	  var value;
	  for (key in settings) {
	    value = document.getElementById(settings[key].id).value;
		Widget.preferences.setItem(settings[key].id, value);
      };
      Controller.showMain();
	},
	
	/**
	 * Create the settings form and populate <div id="settings"> with it.
	 * 
	 * Groups are used to describe related sets of settings. When the
	 * settings page is requested the user will be presented with a
	 * list of links to individual settings pages for each group. If
	 * no groups are defined then all settings will be displayed on a 
	 * single settings page.
	 * 
	 * @param group the name of the group of settings we need a form to represent
	 */
	createForm: function(group) {
		if (group == undefined) {
			SettingsService.createGroupsIndex();
		} else {
			var settings = SettingsService.getSettings(group);
			
			var form = document.createElement("form");
			if (group == undefined) {
				form.setAttribute("action", "javascript:SettingsService.submitSettingsForm()");
			} else {
				form.setAttribute("action", "javascript:SettingsService.submitSettingsForm('" + group + "')");
			}
			
			var title = document.createElement("h3");
			if (group == undefined) {
			  title.innerHTML = "Settings";
			} else {
			  title.innerHTML = "Settings: " + group;
			}
			
			for (key in settings) {
			  var label = document.createElement("label");
			  label.setAttribute("for", settings[key].id);
			  label.innerHTML = settings[key].label;
			  form.appendChild(label);
			  
			  var input = document.createElement("input");
			  input.type = settings[key].type;
			  input.id = settings[key].id;
			  input.setAttribute("placeholder", settings[key].placeholder)
			  var value = Widget.preferences.getItem(settings[key].id);
			  if (value != undefined) {
			    input.value = value;
			  }
			  form.appendChild(input);
	
			  form.appendChild(document.createElement("br"));
			};
			
			var submit = document.createElement("input");
			submit.type = "submit";
			submit.value = "Save";
			submit.setAttribute("class", "form-button");
			form.appendChild(submit);
			
			var settingsDiv = document.getElementById("settings");
			while (settingsDiv.hasChildNodes()) {
			    settingsDiv.removeChild(settingsDiv.lastChild);
			}
			settingsDiv.appendChild(title);
			settingsDiv.appendChild(form);
		}
	},
	
	/**
	 * Populate the <div id="settings" page with an index to the various settings
	 * groups.
	 */
	createGroupsIndex: function() {
		var groups = SettingsService.getGroups();
		
		var title = document.createElement("h3");
	    title.innerHTML = "Settings Groups";
	    
		var list = document.createElement("ol");
	    for (key in groups) {
	    	var item = document.createElement("li");
	    	var groupLink = document.createElement("a");
	    	groupLink.href = "javascript:Controller.showSettings('" + groups[key] + "')"
	    	groupLink.innerHTML = groups[key];
	    	item.appendChild(groupLink);
	    	list.appendChild(item);
	    }
	    
	    var settingsDiv = document.getElementById("settings");
		while (settingsDiv.hasChildNodes()) {
		    settingsDiv.removeChild(settingsDiv.lastChild);
		}
	    
	    settingsDiv.appendChild(title);
	    settingsDiv.appendChild(list);
	}
}