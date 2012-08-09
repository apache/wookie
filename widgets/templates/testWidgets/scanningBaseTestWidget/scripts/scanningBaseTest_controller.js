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
var ${widget.shortname}_scanningBaseTest_controller = {
    init:function() {
	$('#button1').click(function() {
	    alert("Button 1 clicked");
	});
	$('#button2').click(function() {
	    alert("Button 2 clicked");
	});
	$('#button3').click(function() {
	    alert("Button 3 clicked");
	});
	$('#button4').click(function() {
	    alert("Button 4 clicked");
	});
	$('#button5').click(function() {
	    alert("Button 5 clicked");
	});
	$('#button6').click(function() {
	    alert("Button 6 clicked");
	});

	${widget.shortname}_scanning_controller.scanElements = $('.scan');
    },
};

$('#home').live('pageshow',function(event) {
    ${widget.shortname}_scanningBaseTest_controller.init(); 
});
