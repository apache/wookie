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
	init:function() {
		Controller.update();
	},

	/**
	 * Update the hello world message, and the updated time
	 */
	items: [],
	update:function() {
		$.mobile.pageLoading();
		jQuery.getFeed({
	        url: Widget.proxify(Properties.getFeedURL()),
	        success: function(feed) {
				Controller.items = feed.items;
			 	var index = $('#stories-listview');
	            index.empty();
			 	$.each(Controller.items, function(key, item) {
	            	index.append($("<li/>")
	                             .append($("<a/>")
	            						 .attr("onClick", "Controller.gotoStory(" + key + ")")
	            						 .text(item.title)));
		        })
		        index.listview("refresh");
	        }
	    });
		$.mobile.pageLoading(true);
	},
	
	/**
	 * Create the page for a given story and navigate to it.
	 */
	gotoStory:function(key) {
		item = Controller.items[key];
		var header = $('<div data-role="header" data-position="fixed"></div>')
          .append("<h4>" + item.title + "</h4>")
		//var footer = $('<div data-role="footer" data-position="fixed"><h4>Footer<h4></div>');
		var content = $("<div data-role='content'/>")
		 .append($('<h2>' + item.title + '</h2>'))
		 .append($('<div>' + item.description + '</div>')
	     .append($('<div><a href="' + item.link + '">Read original</a>')))
		 
		$.mobile.pageContainer.append($("<div data-role='page'/>")
			  				  .attr("id", key)
			  				  .attr("data-url", key)
			  				  .append(header)
			  				  .append(content));
		$('#' + key).page();
		$.mobile.changePage("#" + key);
	}		
}