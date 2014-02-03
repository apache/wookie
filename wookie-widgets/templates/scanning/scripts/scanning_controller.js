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
 * The @widget.shortname@_scanning_controller object This is used to
 * allow users using a switch device to interact efficiently with a
 * widget.
 */ 
var ${widget.shortname}_scanning_controller = {
    scanning: ${scanning.start}, // indicates if we are currently scanning
    delay: ${scanning.delay}, // time in milliseconds between focus change
    interval: null, // The interval object controlling the scan
    scanElements: null, // The elements to scan over
    currentElementIdx: 0, // The index of the currently scanning element

    init:function() {
        ${widget.shortname}_scanning_controller.scanElements = $('[data-scanOrder]');
        if (${widget.shortname}_scanning_controller.scanning) {
	    ${widget.shortname}_scanning_controller.startScanning();
        };
    },

    /**
     * Action to take when switch is released.
     * The event contains details supplied by the switch.
     */
    switchUp:function(event) {
	if (event.keyCode === 32) {
	    ${widget.shortname}_scanning_controller.scanElements[${widget.shortname}_scanning_controller.currentElementIdx].click();
	}
    },

    nextElement:function() {
	var idx = ${widget.shortname}_scanning_controller.currentElementIdx;
	var elements = ${widget.shortname}_scanning_controller.scanElements;

	var oldElement = $(elements[idx]);
	oldElement.fadeIn('slow', function() {
	    // Animation complete.
	});

	idx += 1;
	if (idx >= elements.length) {
	    idx = 0
	};

	var newElement = $(elements[idx]);
	newElement.fadeOut('slow', function() {
	    // Animation complete.
	});
	newElement.fadeIn('slow', function() {
	    // Animation complete.
	});
	${widget.shortname}_scanning_controller.currentElementIdx = idx;
    },

    startScanning:function() {
	${widget.shortname}_scanning_controller.scanning = true;
	${widget.shortname}_scanning_controller.interval = 
	    window.setInterval(${widget.shortname}_scanning_controller.nextElement, 
			       ${widget.shortname}_scanning_controller.delay);
	$(document).keyup(${widget.shortname}_scanning_controller.switchUp);
    },

    stopScanning:function() {
	${widget.shortname}_scanning_controller.scanning = false;
	window.clearInterval(${widget.shortname}_scanning_controller.interval);
    }

};

$('#home').live('pageshow',function(event) {
    ${widget.shortname}_scanning_controller.init(); 
});
