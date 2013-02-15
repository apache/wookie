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
 
/*
 *  Javascript from template: browse
 */ 

/**
 * The twitter_controller object
 * This is used to wire up the view and model with actions
 */ 
var twitter_browse_controller = {
    init:function() {
        //
        // If there is no URL for searching, don't show 
        // the search panel
        //
        query = "TheASF";// this is the default account to show
        var searchUrl = '"http://api.twitter.com/1/statuses/user_timeline.xml?screen_name=" + query + "&include_entities=true"'; 
        if(searchUrl === "") $("#searchPanel").hide();
        //
        // If there is a "requiresLogin" attribute, don't
        // trigger search just yet
        //
        if(!twitter_browse_controller.requiresLogin)
        twitter_browse_controller.search()
    },
    
    /**
     * Search the remote service and sort results if possible.
     */
    search:function(query, sort, order) {
        if (sort === undefined) {
            sort = "updated";
        }
        
    	if (!query || query === undefined || query == "") {
            query = "TheASF";
        }
        
        var url = widget.proxify("http://api.twitter.com/1/statuses/user_timeline.xml?screen_name=" + query + "&include_entities=true");
    	twitter_browse_controller.populate(url);
    },
	
	
    /**
     * Extract the values from the search form, do the search and display the results.
     */
    submitSearchForm:function() {
	var query = $('#searchString').val();
	var sort = $('#sort').val();
	var order = ($("input[@name='sortOrder']:checked").val());
	twitter_browse_controller.search(query, sort, order);
    },

   /**
    * Get the current title for this widget.
    */
    getTitle:function() {
        return "Twidget";
    },

   /**
    * Populate the results list with data from a given URL.
    */
    populate:function(sourceUrl) {
        $('#results').remove();
        var html = twitter_browse_controller.transform(sourceUrl, "list");      
        $('#content-primary').html(html).trigger("create");
        $('body').trigger("results_updated");
	    $('.result:first').trigger('expand');
    },
        
    /**
     * Retrieve the details of an item and display them in the detail section.
     */
    displaySummary:function(itemId){
    
        /**
         * Locate the detail element to update
         */
        var detailElement = $(".result[wid='"+itemId+"']").find(".detail");
        
        /**
         * Load the data, showing a loading message while waiting
         */
        detailElement.html("<p>Loading...</p>");
        var sourceUrl = widget.proxify("http://localhost:8080/wookie/widgets/" + itemId);
	    $.mobile.showPageLoadingMsg();
        var html = twitter_browse_controller.transform(sourceUrl, "detail");
        detailElement.html(html);    
        
        /**
         * Add events
         */ 
	    detailElement.click(function() {
	    var event = { widget: "twitter", type: "clickItem", itemId: itemId};	    
	    twitter_controller.executeCallbacks(event);
	});
	$.mobile.hidePageLoadingMsg();
    }			  
}

/**
 * Initialise page layout
 */
$('#home').live('pageinit',function(event) {
   twitter_browse_controller.init(); 
});


/**
 * Display the content of a result item when it is expanded.
 */
$('div.result').live('expand', function(event) {
    var wid = $(this).attr("wid");
    twitter_browse_controller.displaySummary(wid);
    var itemId = wid.substr(wid.indexOf("id=")+3);
    var event = { widget: "twitter", type: "expandItem", itemId: itemId};	    
    twitter_controller.executeCallbacks(event );
});


/**
 * load XML data and transform it into HTML
 * @param src the URL to load
 * @param type the type of data transform requested, either "detail" or "list"
 */
twitter_browse_controller.transform = function(src, type){
 var output = "";
 $.support.cors = true; // force cross-site scripting (as of jQuery 1.5)
 $.ajax({
  url: src,
  dataType: "xml",
  async: false,
  success: function(xml){
    output = twitter_browse_controller.transformXml(xml, type); 
  }, //end success function
    error: function(xhr, textStatus, errorThrown) {
        alert(errorThrown);
    }
 });
 
 return output;
}

/**
 * Transform data into HTML
 */ 
twitter_browse_controller.transformXml = function(xml, type){

    if (type === "detail"){
      template = "<div class='result' data-role='collapsible' wid='${ID}'>  <h3>${TITLE}</h3>  <div class='detail'>    <p>${DESCRIPTION}</p>  </div></div>";
    } else {
      template = "<!--The template used for item summaries, representing a single record but beforethe item detail has been retrieved. We override this to provide all the info in the summaryand not the detail template - this is because the Twitter API is rate limited, and we canpopulate the whole thing directly in one call.Note the OnClick event handler that triggers a search for the user by their screen_name. Thisis missing quotes as these are added at some point in the processing of the parameter - if youinclude a quote or an escape character then between Ant and JQuery something will go wrong :(Still, it works as is)--><li class='tweet result' wid='${ID}'>  <a href='#' onclick=twitter_browse_controller.search('${SCREEN_NAME}')><img class='tweet_avatar' alt='Avatar for ${SCREEN_NAME}; click to read more tweets from this user' title='See more tweets from ${SCREEN_NAME}'  src='${PROFILE_IMAGE_URL}'></a>  <span class='tweet_text'>${TEXT}</span>  <br clear='left'>  <abbr class='timeago' title='${CREATED_AT}'>${CREATED_AT}</abbr>   <div class='detail'></div></li>";
    }
    
    var output = "";
    var items = "";
    
    /**
     * For each element that matches itemName, create a new ItemTemplate and
     * replace placeholders in the template with values from the XML using the ItemElements 
     * and ItemAttrobutes lists
     */
    var elements = twitter_browse_controller.find(xml,"status");
    $(elements).each(
       function(){
         var item = template;
         var elements = "id,name,screen_name,text,profile_image_url,created_at,source".split(",");
         for (var i=0;i<elements.length;i++){
            var element = elements[i]; 
            var pattern = "\\$\{" + element.toUpperCase() +"\}";
            item = item.replace(new RegExp(pattern,'g'), $(this).find(element).first().text());
         }
         var attributes = "id".split(",");
         for (var i=0;i<attributes.length;i++){
            var attribute = attributes[i]; 
            var pattern = '\\$\{'+attribute.toUpperCase()+'\}';
            item = item.replace(new RegExp(pattern,'g'), $(this).attr(attribute));
         }
         items += item;
       }
    );
    
    /**
     * If we are returning a list, wrap the result in the collection template, otherwise
     * just return the item
     */
    if (type === "list"){
       output = "<div id='results' data-role='collapsible-set'><ul class='tweet_list'>${ITEMS}</ul></div>".replace("${ITEMS}", items);
    } else {
        output = items;
    }
    return output;
}

/**
 * Obtain collection of objects to operate on, either
 * XML elements or JSON objects
 */ 
twitter_browse_controller.find = function(data, name){
  if ("xml"==="xml"){
    return $(data).find(name);
  } else {
    if (!name || name === "") return data;
    return data[name];
  }
}
/*
*  Javascript from template: twoColumn
*/ 


var twitter_twoColumn_controller = {
    init:function() {
      twitter_twoColumn_controller.update();
    },

    /**
     * Update the display
     */
    update:function() {
	// expand the search box on screens that are either wide enough (alongside results) or tall enough
	if ($.mobile.media("only screen and (min-width : 801px)")) {
	    $('#content-secondary .ui-collapsible').each(
		function() {
		    $(this).trigger('expand');
		});
	} else if ($.mobile.media("only screen and (min-width : 641px) and (max-width : 1024px)") || $.mobile.media("only screen and (min-width : 1801px)")) {
	    $('#content-secondary .ui-collapsible').each(
		function() {
		    $(this).trigger('expand');
		});
	} else {
	    $('#content-secondary .ui-collapsible').each(
		function() {
		    $(this).trigger('collapse');
		});
	}
    },

}

$('#home').live('pageinit',function(event) {
   twitter_twoColumn_controller.init();
})

/*
*  Javascript from template: base
*/ 


/**
 * The @widget.shortname@_controller object
 * This is used to wire up the view and model with actions
 */ 
var twitter_controller = {
    /**
     * A dictionary of callback functions that are called whenever an
     * event is fired by this widget.
     */
    callbacks: {},

    init:function() {
        twitter_controller.update();
    },

    /**
     * Update the display
     */
    update:function() { 
    },

   /**
    * Get the current title for this widget.
    */
    getTitle:function() {
        return "Twidget";
    },

    /**
     * Get the viewport width and height.
     * returns an object with width and height properties.
     */
    getViewport:function() {
	var e = window
	, a = 'inner';
	if ( !( 'innerWidth' in window ) ) {
	    a = 'client';
	    e = document.documentElement || document.body;
	}
	return { width : e[ a + 'Width' ] , height : e[ a + 'Height' ] }
    },

    /**
     * Register a callback function. 
     *
     * type: the name of the type of event to respond to 
     *
     * widget: [optional] the name of the widget where the event must
     * occur for the callback to be executed. This allows callbacks to
     * be register so that they will only respond to events in
     * specific widgets, or to all events.
     */
    register:function(event, callback, widget) {
	if (widget === undefined) {
	    var cbks = twitter_controller.callbacks[event];
	} else {
	    var cbks = twitter_controller.callbacks[widget + "." + event];
	}
	if (cbks === undefined) {
	    cbks = new Array();
	}
	cbks.push(callback);
	twitter_controller.callbacks[event] = cbks;
    },

    /**
     * Execute all the callbacks registered for a given event.
     *
     * The event object is passed directly to the callback function
     * and can contani any number of properties. Two properties that
     * important to the callback functionality are:
     *
     * type: the name of the type of event to respond to 
     *
     * widget: [optional] the name of the widget where the event
     * occured. This allows callbacks to be register so that they will
     * only respond to events in specific widgets, or to all events.
     */
    executeCallbacks:function(event) {
	// Execute all callbacks not restricted to a widget
	var cbks = twitter_controller.callbacks[event.type];
	if (cbks === undefined) return;
	for (var i = 0; i < cbks.length; i++) {
	    cbks[i](event);
	}

	// Execute all callbacks restricted to a widget
	if (event.widget === undefined) return;
	var cbks = twitter_controller.callbacks[event.widget + "." + event.type];
	if (cbks === undefined) return;
	for (var i = 0; i < cbks.length; i++) {
	    cbks[i](event);
	}
    }
};

/**
 * Provides a basic shim for opening the widget without a widget object (e.g. directly in browsers)
 */
if (!window.widget){
    window.widget = {};
}

/**
 * Adds in the "proxify" method if it isn't in the widget object, e.g. as we're opening the widget
 * directly in a browser, or using a widget runtime other than Wookie e.g. Opera, PhoneGap etc
 */
if (!window.widget.proxify){
    window.widget.proxify = function(url){ return url };
}

$('#home').live('pageshow',function(event) {
   twitter_controller.init(); 
});

$('body').bind('orientationchange',function(event){
    twitter_controller.update();
})


/*
 * Change created times into friendly strings like "2 minutes ago"
 * using the TimeAgo JQuery plugin. Also "linkify" any @screenname
 * found in tweets
 *
 * We trigger this on document
 * load and whenever the results are updated.
 */
$(document).ready(function() {
 $('abbr.timeago').timeago();
 linkify();
 $('body').bind('results_updated', function() {
   $('abbr.timeago').timeago();
   linkify();
 });

});


/*
 * Replace @screen_name in tweets with links that update the results with that user's timeline
 */
function linkify(){
  $('.tweet_text').each(function(){
     var html = $(this).text().replace(/(^|)@(\w+)/gi, function (s) {
        return '<a href="#" onclick="twitter_browse_controller.search(\''+s+'\')">'+s+'</a>';
     });
     $(this).html(html);
  });
}
