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
    	var id = Widget.preferences.getItem("itemId");
    	if (id === undefined) {
    		id = ${itemDetail.default.itemId}
    	}
        ${widget.shortname}_browse_controller.populate(id);
    },
    
   /**
    * Populate the results list with data from a given URL. The data is transformed using the "index2html.xsl" stylesheet.
    */
    populate:function(itemId) {
		var url = widget.proxify(${itemDetail.get.url});
        $('#detail').remove();
        var html = $.XSLTransform({
            xmlurl:url,
            xslurl:${itemDetail.xsl.url}
        });
        $('#content-primary').html(html).trigger("create");
    }
}

/**
 * Initialise page
 */
$('#home').live('pageshow',function(event) {
   ${widget.shortname}_browse_controller.init(); 
});

