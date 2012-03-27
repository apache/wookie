/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */ 
 
 $(document).ready(getWidgets);

//
// Get the current widgets installed and
// show in the browse list
//
function getWidgets(){
    Wookie.getWidgets(updateWidgets);
}

function updateWidgets(widgets){
    for (var i=0;i<widgets.length;i++){
        var widgetEntry = $("<div id=\""+widgets[i].id+"\"class=\"widget\"><p><img src=\""+widgets[i].icon+"\">"+widgets[i].name+"</p></div>");
        var id = widgets[i].id;
        $(widgetEntry).click(function(){
            showWidget($(this).attr("id"));
        });
        $("#navigation").append(widgetEntry);
    }
}

function showWidget(id){
    Wookie.setCurrentUser("alice","alice","http://localhost:8080/wookie/demo/alice.png");
    var widgetInstance = Wookie.getOrCreateInstance(id);
    $("#preview_alice_widget").html('');
    $("#preview_alice_widget").append("<iframe src='"+widgetInstance.url+"' height='"+widgetInstance.height+"' width='"+widgetInstance.width+"'></iframe>");
    $("#alice_url").html("<a href='" + widgetInstance.url + "' target='_blank'>Full Screen</a>");
    Wookie.setPreference(id, "conference-manager","true");
    
    Wookie.setCurrentUser("bob","bob","http://localhost:8080/wookie/demo/bob.png");
    var widgetInstance = Wookie.getOrCreateInstance(id);
    $("#preview_bob_widget").html('');
    $("#preview_bob_widget").append("<iframe src='"+widgetInstance.url+"' height='"+widgetInstance.height+"' width='"+widgetInstance.width+"'></iframe>");
    $("#bob_url").html("<a href='" + widgetInstance.url + "' target='_blank'>Full Screen</a>");
}
