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

var ${widget.shortname}_message_controller = {
    init:function() {
	$('#messageForm').submit(function(event) {
	    var subject = $('#subject').val();
	    var message = $('#message').val();
	    ${widget.shortname}_message_controller.send(subject, message);
	});
	$('#cancel').click(function(event) {
	    ${widget.shortname}_message_controller.cancel();
	});

    },

    send:function(subject, message) {
	var proxy = widget.proxify(${message.url});
	var form = $("#messageForm");
	$.ajax({
	    type: 'POST',
	    url: proxy,
	    data: form.serialize,
	    success: function(data){
		alert("form submitted, response: " + data);
	    },
	    error: function(xhr, error) {
		alert("Sorry, there was an error sending your message");
	    }
	});
    },

    cancel:function() {
	history.back();
	return false;
    }
}

$('#home').live('pageshow',function(event) {
    ${widget.shortname}_message_controller.init(); 
});
