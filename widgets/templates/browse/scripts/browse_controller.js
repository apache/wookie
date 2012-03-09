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
/**
 * The ${widget.shortname}_controller object
 * This is used to wire up the view and model with actions
 */ 
var ${widget.shortname}_browse_controller = {
    init:function() {
        //
        // If there is no URL for searching, don't show 
        // the search panel
        //
        var query = "";
        var searchUrl = '${browse.search.url}'; 
        if(searchUrl === "") $("#searchPanel").hide();
        //
        // If there is a "requiresLogin" attribute, don't
        // trigger search just yet
        //
        if(!${widget.shortname}_browse_controller.requiresLogin)
        ${widget.shortname}_browse_controller.search()
    },
    
    /**
     * Search the remote service and sort results if possible.
     */
    search:function(query, sort, order) {
	if (sort === undefined) {
	    sort = "${browse.sort}";
	}
    	if (query === undefined || query == "") {
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
	var sort = $('#sort').val();
	var order = ($("input[@name='sortOrder']:checked").val());
	${widget.shortname}_browse_controller.search(query, sort, order);
    },

   /**
    * Get the current title for this widget.
    */
    getTitle:function() {
        return "${widget.name}";
    },

   /**
    * Populate the results list with data from a given URL.
    */
    populate:function(sourceUrl) {
        $('#results').remove();
        var html = ${widget.shortname}_browse_controller.transform(sourceUrl, "list");      
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
        var sourceUrl = widget.proxify(${browse.get.detail.url});
	    $.mobile.showPageLoadingMsg();
        var html = ${widget.shortname}_browse_controller.transform(sourceUrl, "detail");
        detailElement.html(html);    
        
        /**
         * Add events
         */ 
	    detailElement.click(function() {
	    var event = { widget: "${widget.shortname}", type: "clickItem", itemId: itemId};	    
	    ${widget.shortname}_controller.executeCallbacks(event);
	});
	$.mobile.hidePageLoadingMsg();
    }			  
}

/**
 * Initialise page layout
 */
$('#home').live('pageinit',function(event) {
   ${widget.shortname}_browse_controller.init(); 
});


/**
 * Display the content of a result item when it is expanded.
 */
$('div.result').live('expand', function(event) {
    var wid = $(this).attr("wid");
    ${widget.shortname}_browse_controller.displaySummary(wid);
    var itemId = wid.substr(wid.indexOf("id=")+3);
    var event = { widget: "${widget.shortname}", type: "expandItem", itemId: itemId};	    
    ${widget.shortname}_controller.executeCallbacks(event );
});


/**
 * load XML data and transform it into HTML
 * @param src the URL to load
 * @param type the type of data transform requested, either "detail" or "list"
 */
${widget.shortname}_browse_controller.transform = function(src, type){
 var output = "";
 $.support.cors = true; // force cross-site scripting (as of jQuery 1.5)
 $.ajax({
  url: src,
  dataType: ${browse.format},
  async: false,
  success: function(xml){
    output = ${widget.shortname}_browse_controller.transformXml(xml, type); 
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
${widget.shortname}_browse_controller.transformXml = function(xml, type){

    if (type === "detail"){
      template = "${browse.item.detail.template}";
    } else {
      template = "${browse.item.summary.template}";
    }
    
    var output = "";
    var items = "";
    
    /**
     * For each element that matches itemName, create a new ItemTemplate and
     * replace placeholders in the template with values from the XML using the ItemElements 
     * and ItemAttrobutes lists
     */
    var elements = ${widget.shortname}_browse_controller.find(xml,${browse.item.name});
    $(elements).each(
       function(){
         var item = template;
         var elements = ${browse.item.elements}.split(",");
         for (var i=0;i<elements.length;i++){
            var element = elements[i]; 
            var pattern = "\\$\{" + element.toUpperCase() +"\}";
            item = item.replace(new RegExp(pattern,'g'), $(this).find(element).first().text());
         }
         var attributes = ${browse.item.attributes}.split(",");
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
       output = ${browse.collection.template}.replace("${ITEMS}", items);
    } else {
        output = items;
    }
    return output;
}

/**
 * Obtain collection of objects to operate on, either
 * XML elements or JSON objects
 */ 
${widget.shortname}_browse_controller.find = function(data, name){
  if (${browse.format}==="xml"){
    return $(data).find(name);
  } else {
    if (!name || name === "") return data;
    return data[name];
  }
}