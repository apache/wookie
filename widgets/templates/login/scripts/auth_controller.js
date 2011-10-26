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
  basic_auth:function(username, password) {
      var payload = "<session><username>" + username + "</username>";
      payload = payload + "<password>" + password + "</password></session>";
      var proxy = widget.proxify("http://www.myexperiment.org/session/create");
      $.ajax({
          url: proxy, 
          type: "POST",
          contentType: "application/xml",
          dataType: "html",
          data: payload,     
          cache: false,
          xhrFields: { cookie:"_m2_session_id=b7821ca7a8a19110d2f0d3b74bed2f52" },
          success: function (response) {              
              if (response==1) {                  
                  alert("success reported");
              } else {
                  alert('Sorry, unexpected error. Please try again later.');                   
              }
          },
          error: function (xhr, error) {
              alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
              alert("responseText: "+xhr.responseText);
          }
      });
  }
}