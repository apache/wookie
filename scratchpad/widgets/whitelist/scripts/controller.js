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
 * The Controller object
 * This is used to wire up the view and model with actions
 */ 
var Controller = {
	init:function() {
		Controller.update();
	},

	/**
	 * Update whitelist entries from the REST API
	 */
	update:function() {
        $.getJSON("/wookie/whitelist?format=json", Controller.updateList);

	},
    
    updateList:function(data){
        var index = $('#entries-listview');
        index.empty();
        for(entry in data.entries){
            index.append($("<li/>")
                        .append($("<a/>")
                            .attr("onClick", "Controller.viewEntry(" + data.entries[entry].id +",\"" + data.entries[entry].url + "\")")
                                .text(data.entries[entry].url)
                            )
                        );
        }
        index.listview("refresh");
    },
    
    viewEntry:function(id, url){
        $("#detail-url").text(url);
        $("#revoke-button").unbind();
        $("#revoke-button").click(function(){
            Controller.revokeEntry(id);
        });
        $.mobile.changePage("#detail");
    },
    
    revokeEntry: function(id){
        $.ajax({
            url: "/wookie/whitelist/"+id,
            type:"DELETE",
            success: function(){
                Controller.update();
                $.mobile.changePage("#home", "slide", true);
            }
        });
    },
    
    add: function(){
        var url = $("#input-url").val();
        $.ajax({
            url: "/wookie/whitelist/",
            data: {"url": url},
            type:"POST",
            success: function(){
                Controller.update();
                $.mobile.changePage("#home", "pop", true);
            }
        });
    },
    
}