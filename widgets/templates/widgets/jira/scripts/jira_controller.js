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
${widget.shortname}_browse_controller.requiresLogin=true;

//
// Hide the search panel until logged in
//
$('#home').live( 'pageinit',function(event){
    $('#searchPanel').hide(); 
});

/**
 * Login action
 *
 * Sets the username and password to use during this browser session
 * and toggles the login panel and search panel
 */
${widget.shortname}_browse_controller.submitLoginForm = function(){

    $.mobile.showPageLoadingMsg();
    ${widget.shortname}_browse_controller.username = $('#usernameString').val();
    ${widget.shortname}_browse_controller.password = $('#passwordString').val();
    
    //
    // Check if the credentials can pull down session info; if so they are
    // correct and we can use them. Note we don't actually create a session, we
    // use Basic Auth for each request, and this is just to check credential
    // validity
    //
    // If the credentials are OK, we hide the login form and show the search
    // form.
    //
    $.ajax({
      url: window.widget.proxify('https://issues.apache.org:443/jira/rest/auth/1/session')+"&username="+ ${widget.shortname}_browse_controller.username + "&password="+${widget.shortname}_browse_controller.password,
      dataType: 'json',
      success: function(data){
        $.mobile.hidePageLoadingMsg();
        ${widget.shortname}_browse_controller.requiresLogin=false;
        $('#loginPanel').hide();
        $('#searchPanel').trigger('expand');  
        $('#searchPanel').show(); 
      },
      
      error: function(){
        $.mobile.hidePageLoadingMsg();
        $('#loginMessage').text("login rejected, please try again");
      }
    });
    
    
}
