<%
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
%>
var ${widget.shortname}_settings_controller = {
    init:function() {

    },

    /**
     * Build and show the settings page.
     * FIXME: Need to make this a JQuery function
     */
    showSettings: function(group) {
	${widget.shortname}_settings_controller.createForm(group);
        $.mobile.changePage("#settings");
    },

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
     * to the group names returned by ${widget.shortname}_settings_controller.getSettings()
     * 
     * @return an array containing a number of Setting objects
     */
    getSettings: function(group) {
	var settings = [];
	if (group == undefined || group == "about") {
	    settings[0] = new ${widget.shortname}_Setting("name", "Name", "The name you wish to be known by.", "text");
	    settings[1] = new ${widget.shortname}_Setting("description", "Description", "A short description that will be shown to people viewing your details.", "textarea");
	} else if (group == "contact") {
	    settings[0] = new ${widget.shortname}_Setting("homepageURL", "Homepage URL", "Your homepage URL.", "email");
	    settings[1] = new ${widget.shortname}_Setting("email", "EMail", "Your email address.", "email");
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
	var settings = ${widget.shortname}_settings_controller.getSettings(group);
	var value;
	for (key in settings) {
	    value = $("#" + settings[key].id).attr("value");
	    widget.preferences.setItem(settings[key].id, value);
        };
        $.mobile.changePage("#home");
        $.mobile.loadPage( "settings.html" );
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
	    ${widget.shortname}_settings_controller.createGroupsIndex();
	} else {
	    var settings = ${widget.shortname}_settings_controller.getSettings(group);
            // FIXME: JQuerify the action	    
	    var form = $("<form action='javascript:${widget.shortname}_settings_controller.submitSettingsForm(\"" + group + "\")'>");
			
	    var title = $("<h3>Settings: " + group + "</h3>");
	    
	    for (key in settings) {
		var label = $("<label>" + settings[key].label + "</label>");
		label.attr("for", settings[key].id);
		form.append(label);
		
		var input = $("<input>");
		input.type = settings[key].type;
		input.attr("id", settings[key].id);
		input.attr("placeholder", settings[key].placeholder)
		var value = widget.preferences.getItem(settings[key].id);
		if (value != undefined) {
		    input.attr("value", value);
		}
		form.append(input);
	        
		form.append($("<br>"));
	    };
	    
	    var submit = $("<input>");
	    submit.attr("type", "submit");
	    submit.attr("value", "Save");
	    submit.attr("class", "form-button");
	    form.append(submit);
	    
            var settingsElem = $("#settings-primary");
            settingsElem.empty();
	    title.appendTo(settingsElem);
	    form.appendTo(settingsElem).trigger("create");
	}
    },
	
    /**
     * Populate the <div id="settings" page with an index to the various settings
     * groups.
     */
    createGroupsIndex: function() {
	var groups = ${widget.shortname}_settings_controller.getGroups();
	
	var title = $("<h3>Settings Groups<h3>");
	
	var list = $("<ul data-role='listview'>");
	for (key in groups) {
            var item = $("<li><a href='javascript:${widget.shortname}_settings_controller.showSettings(\"" + groups[key] + "\");'>" + groups[key] + "</a></li>");
	    list.append(item);
            // FIXME: JQuerify
	    // groupLink.href = "javascript:${widget.shortname}_settings_controller.showSettings('" + groups[key] + "')"
	}
	
	var settingsElem = $("#settings-primary");
        settingsElem.empty();
	settingsElem.append(title);
	settingsElem.append(list);
    }
};

$('#home').live('pageinit',function(event) { 
  $.mobile.loadPage( "settings.html" );
  $('#showSettings').on('click',function(event){
      ${widget.shortname}_settings_controller.showSettings();
  });
});

$('#settings').live('pageinit',function(event) { 
    ${widget.shortname}_settings_controller.init();
});

/**
 * A setting to represent in the settings form.
 * 
 * @param id is used to identiy the setting and the form element created for it.
 * @param label is the text to place in the label for the form element.
 * @param placeholder is the text that will appear in the form element if the setting has not been set
 * @param type is the type of data to be stored in this setting, it is used to define the form element to use
 */
function ${widget.shortname}_Setting(id, label, placeholder, type){
    this.id = id;
    this.label = label;
    this.placeholder = placeholder
    this.type = type;
}
