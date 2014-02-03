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
        // Populate the feed settings form
        $("#input-url").val(Properties.getFeedURL());
        
        // Load the feed
		Controller.update();
	},

	/**
	 * Load the feed
	 */
	update:function() {
		$.mobile.showPageLoadingMsg();
        if (widget.proxify){
            var feedUrl = widget.proxify(Properties.getFeedURL());
        } else {
            var feedUrl = Properties.getFeedURL();
        }
        $.getFeed({
            url: feedUrl, 
            success: Controller.updateFeed
        });
	},
    
    /*
     * Refresh the feed list
     */
    updateFeed: function(feed){
        $.mobile.hidePageLoadingMsg();
        var index = $('#stories-listview');
        index.empty();
        var items = feed.items
        Controller.items = items;
        $.each(items, function(key, item) {
            index.append($("<li/>")
                         .append($("<a/>")
                                .text(item.title || "Untitled" )
                                .attr("onClick", "Controller.gotoStory(" + key + ")")
                                )
                         )
        });
        index.listview("refresh");
    },
	
	/**
	 * Update the story details page for a given story and navigate to it.
	 */
	gotoStory:function(key) {
		var item = Controller.items[key];
        $("#story-detail-title").text(item.title);
        $("#story-detail-content").empty();
        $("#story-detail-content")
            .append($('<h2>' + item.title + '</h2>'))
            .append($('<div>' + item.description + '</div>')
            .append($('<div><a href="' + item.link + '">Read original</a>')))
		$.mobile.changePage("#story-detail");
	}		
}