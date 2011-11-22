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
  * The ${widget.shortname}_controller object is used to manage authentication.
  */
var ${widget.shortname}_auth_controller = {

    init:function() {
	$('#loginForm').submit(function(event) {
	    var username = $('#username').val();
	    var password = $('#password').val();
	    ${widget.shortname}_auth_controller.basic_auth(username, password);
	});
    },

  basic_auth:function(username, password) {
      var payload = "<session><username>" + username + "</username>";
      payload = payload + "<password>" + password + "</password></session>";
      var proxy = widget.proxify("http://www.myexperiment.org/session/create");
      $.mobile.showPageLoadingMsg() 
      $.ajax({
	  beforeSend: function() { $.mobile.showPageLoadingMsg(); },
          complete: function() { $.mobile.hidePageLoadingMsg() },
          url: proxy, 
          type: "POST",
          data: payload,     
          cache: false,
          success: function (html, statusText, response) {
	      if (html.indexOf("Invalid username or password") > -1) {
		  alert("There seems to be a problem with your login details.");
	      } else {
		  var headers = response.getAllResponseHeaders();
		  session_controller.setSessionId(${widget.shortname}_auth_controller.get_cookie("_m2_session_id"));;
		  alert("logged in");
	      }
          },
          error: function (xhr, error) {
              alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
              alert("responseText: "+xhr.responseText);
          }
      });
  },

    get_cookie:function ( cookie_name ) {
	var results = document.cookie.match ( '(^|;) ?' + cookie_name + '=([^;]*)(;|$)' );

	if ( results )
	    return ( unescape ( results[2] ) );
	else
	    return null;
    }

};

$('#home').live('pageshow',function(event) {
    ${widget.shortname}_auth_controller.init(); 
});
