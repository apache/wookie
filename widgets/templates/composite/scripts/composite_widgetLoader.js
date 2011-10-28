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

var ${widget.shortname}_WidgetLoader = {
	baseURL : "http://${composite.wookie.domain}:${composite.wookie.port}/${composite.wookie.path}",

	init : function(widgets, properties) {
		${widget.shortname}_WidgetLoader.totalWidgets = widgets.length;
		${widget.shortname}_WidgetLoader.properties = properties;
		for ( var i in widgets) {
			${widget.shortname}_WidgetLoader.getWidget(widgets[i]);
		}
	},

	widgets : [],
	id : 0,
	properties : [],
	propertiesSet : 0,

	getWidget : function(widgetId) {
		var userId = "${composite.wookie.userid}";
		var sharedDataKey = "${composite.wookie.shareddatakey}";
		var apiKey = "${composite.wookie.apikey}";
		var instanceUrl = ${widget.shortname}_WidgetLoader.baseURL + "widgetinstances";
		$.ajax({
			type : 'POST',
			url : instanceUrl,
			data : {
				"userid" : userId,
				"shareddatakey" : sharedDataKey,
				"api_key" : apiKey,
				"widgetid" : widgetId
			},
			success : ${widget.shortname}_WidgetLoader.parseWidgetData,
			error: function (xhr, ajaxOptions, thrownError){
                alert("Error loading widget with id " + widgetId + " from " + instanceUrl + "\n\nStatus code is " + xhr.status);
            },
			dataType : "xml"
		});
	},

	parseWidgetData : function(xml) {
		$(xml).find("widgetdata").each(function() {
			var widget = {};

			widget.url = $(this).find('url').text();
			widget.height = $(this).find('height').text();
			widget.width = $(this).find('width').text();
			widget.title = $(this).find('title').text();
			widget.id = ${widget.shortname}_WidgetLoader.id;
			${widget.shortname}_WidgetLoader.id++;
			${widget.shortname}_WidgetLoader.addWidget(widget);
		});
	},

	showWidget : function(xml) {
		$(xml).find("widgetdata").each(
				function() {
					var url = $(this).find('url').text();
					var height = $(this).find('height').text();
					var width = $(this).find('width').text();

					$("#workspace").append(
							$("<iframe/>").attr("src", url).attr("height",
									height).attr("width", width).attr("class",
									"widget"))
				});
	},

	addWidget : function(widget) {
		${widget.shortname}_WidgetLoader.widgets.push(widget);
		// Picker.add(widget);
		if (${widget.shortname}_WidgetLoader.widgets.length == ${widget.shortname}_WidgetLoader.totalWidgets)
			${widget.shortname}_WidgetLoader.setProperties();
	},

	setProperties : function() {
		if (${widget.shortname}_WidgetLoader.properties.length == 0)
			${widget.shortname}_WidgetLoader.showWidgets();
		for (i in ${widget.shortname}_WidgetLoader.properties) {
			var property = ${widget.shortname}_WidgetLoader.properties[i];
			${widget.shortname}_WidgetLoader.setProperty(property);
		}
	},

	setProperty : function(property) {
		var userId = "admin";
		var sharedDataKey = "admin";
		var apiKey = "TEST";
		$.ajax({
			type : 'POST',
			url : ${widget.shortname}_WidgetLoader.baseURL + "properties",
			data : {
				"userid" : userId,
				"shareddatakey" : sharedDataKey,
				"api_key" : apiKey,
				"widgetid" : property.widget,
				"propertyname" : property.key,
				"propertyvalue" : property.value
			},
			success : ${widget.shortname}_WidgetLoader.propertySet(),
			dataType : "xml"
		});
	},

	propertySet : function() {
		${widget.shortname}_WidgetLoader.propertiesSet++;
		if (${widget.shortname}_WidgetLoader.propertiesSet == ${widget.shortname}_WidgetLoader.properties.length)
			${widget.shortname}_WidgetLoader.showWidgets();
	},

	showWidgets : function() {
		for ( var i in ${widget.shortname}_WidgetLoader.widgets) {
			var widget = ${widget.shortname}_WidgetLoader.widgets[i];
			$("article").append(
					$("<iframe/>").attr("id", widget.id)
							.attr("src", widget.url).attr("height",
									widget.height).attr("width", widget.width)
							.attr("class", "widget")
							.attr("role", "application").attr("title",
									widget.title))
		}
		// Make sure the first widget is visible
		$("#0").show();
		/* $("#current-widget-title").text(${widget.shortname}_WidgetLoader.widgets[0].title); */
	}
}
