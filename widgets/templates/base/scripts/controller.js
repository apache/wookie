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
 * The @widget.shortname@_controller object
 * This is used to wire up the view and model with actions
 */ 
var ${widget.shortname}_controller = {
    init:function() {
        ${widget.shortname}_controller.update();
    },

    /**
     * Update the display
     */
    update:function() { 
	var viewport =  ${widget.shortname}_controller.getViewport();
	var header = $(".header").outerHeight(true);
	var footer = $(".footer").outerHeight(true);
	var space = $(".ui-content").outerHeight(true) - $(".ui-content").height();
	$(".content-primary").height(viewport.height - space - header - footer);
    },

   /**
    * Get the current title for this widget.
    */
    getTitle:function() {
        return "${widget.name}";
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
    }
};

$('#home').live('pageshow',function(event) {
   ${widget.shortname}_controller.init(); 
});