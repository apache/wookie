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
 * The ${widget.shortname}_controller object
 * This is used to wire up the view and model with actions
 */ 
var ${widget.shortname}_browse_controller = {
    init:function() {
        ${widget.shortname}_browse_controller.update();
        ${widget.shortname}_browse_controller.search()
    },
    
    /**
     * Search the myExperiment system and display the results
     */
    search:function(query) {
		// Load data into the initial results list
    	if (query === undefined) {
		  var url = widget.proxify(${browse.index.url});
    	} else {
    		var url = widget.proxify(${browse.search.url});
    	}
    	${widget.shortname}_browse_controller.populate(url);
	},
	
	
	/**
	 * Extract the values from the search form, do the search and display the results.
	 */
	submitSearchForm:function() {
		var query = $('#searchString').val();
		${widget.shortname}_browse_controller.search(query);
	},

    /**
     * Update the display
     */
    update:function() {
        $('#view_title').html(${widget.shortname}_browse_controller.getTitle());
    },

   /**
    * Get the current title for this widget.
    */
    getTitle:function() {
        return "${widget.name}";
    },

   /**
    * Populate the results list with data from a given URL. The data is transformed using the "index2html.xsl" stylesheet.
    */
    populate:function(sourceUrl) {
        $('#results').remove();
        var html = $.XSLTransform({
            xmlurl:sourceUrl,
            xslurl:${browse.index.xsl.url}
        });
        $('#content-primary').html(html).trigger("create");
    },
    
    /**
     * Retrieve the details of an item and display them in the detail section.
     */
    displaySummary:function(itemId){
        $(".detail").html("<p>Loading...</p>");
        var sourceUrl = widget.proxify(${browse.get.detail.url});
        var html = $.XSLTransform({
            xmlurl:sourceUrl,
            xslurl:${browse.detail.xsl.url}
        });
        $(".detail").html(html);    
    }
}

/**
 * Initialise page layout
 */
$('#home').live('pageshow',function(event) {
   ${widget.shortname}_browse_controller.init(); 
});


/**
 * Display the content of a result item when it is expanded.
 */
$('div.result').live('expand', function(event) {
	${widget.shortname}_browse_controller.displaySummary($(this).attr("wid"))
});