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

	update:function() {
        $.get("/wookie/warp?format=xml", Controller.parseTopLevel);
	},
    
    // Parse the complete policy list for all Widgets
    // and construct a list of widgets with a count of the
    // number of policies that apply to them
    parseTopLevel: function(xml){
        var widgets = new Array();
        $(xml).find("policy").each(function(){
            var widget = {};
            widget.id = $(this).attr("widget");            
            widget.title = $(this).attr("widget_title");
            widget.count = 1;
            var exists = false;
            for(var i=0;i<widgets.length;i++){
                if (widgets[i].id == widget.id){
                    widgets[i].count++;
                    exists = true;
                }
            }
            if (!exists){
                widgets.push(widget);
            }
        });
        Controller.widgets = widgets;
        Controller.updateListView(widgets);
    },
    
    // Update the list of widgets and the number of policies that they have
    // and attach an OnClick even to view that widget
    updateListView: function(widgets){
        var index = $('#widgets-listview');
        index.empty();
        for(var i=0;i<widgets.length;i++){
            var widget = widgets[i];
            index.append($("<li/>")
                        .append($("<a/>")
                            .attr("onClick", "Controller.viewWidget(" + widget.id + ")")
                                .text(widget.title)
                            )
                        .append($("<span/>")
                            .attr("class","ui-li-count")
                            .text(widget.count)
                        ));
            index.listview("refresh");
        }
    },
    
    // At Widget level we can see the list of policies
    
    // Set the current widget, and load its policies from the server
    viewWidget: function(widget_id){
        var widget = Controller.getWidget(widget_id);
        Controller.widget = widget;
        $(".widget-title").text(widget.title);
        $.mobile.changePage("#widget");
        $.get("/wookie/warp?format=xml&widgetId="+widget.id, Controller.parseWidgetLevel);
    },
    
    // Refresh the list view of policies for the current Widget
    parseWidgetLevel: function(xml){
        var index = $('#policies-listview');
        index.empty();
        $(xml).find("policy").each(
            function(){
                var id = $(this).attr("id");
                var origin = $(this).attr("origin");
                var granted = $(this).attr("granted");
                var theme = "";
                var decorator = "";
                if (granted == "false") theme = "e";
                if (granted == "false") decorator = "(not granted)";
                index.append( $("<li/>")
                        .attr("data-theme", theme)
                        .append($("<a/>")
                            .text(origin)
                            .attr("onClick", "Controller.policy = Controller.viewPolicy("+id+",\""+granted+"\",\""+origin+"\")")
                        )
                        .append($("<p/>").attr("class", "ul-li-aside").text(decorator))
                      )

            });
        index.listview('refresh');
    },
    
    // Utility method for getting the cached widget from its id
    getWidget: function(id){
        if (!Controller.widgets) return null;
        for (var i=0;i<Controller.widgets.length;i++){
            if (Controller.widgets[i].id == id) return Controller.widgets[i];
        }
    },
    
    // At Policy level we can grant/revoke
    
    // Set the current policy id, and update the form
    // buttons to reflect its status. Finally, re-bind
    // an onChange event to the status radio buttons
    viewPolicy: function(id, granted, origin){  
        Controller.policy_id = id;
        $("#policy-origin").text(origin);
        // Remove any existing change event bound to this control before
        // we change its value
        $("input[name='radio-view']").unbind("change");
        
        $("input[name='radio-view']:checked").removeAttr("checked");
        if (granted == "true"){
            $("input[name='radio-view']:eq(0)").attr("checked","checked");
            $("input[name='radio-view']:eq(1)").removeAttr("checked");
        } else {
            $("input[name='radio-view']:eq(0)").removeAttr("checked");
            $("input[name='radio-view']:eq(1)").attr("checked","checked");
        }

        $("input[name='radio-view']").change(function(){
            if ($("input[name='radio-view']:checked").val() == 'granted')
                Controller.put("true");        
            else 
                Controller.put("false");
        });
        $.mobile.changePage("#policy");
        $("input[name='radio-view']").checkboxradio("refresh");  
    },
 
    // Set the status of a policy (granted or not granted)
    // When complete, refreshes the policy list
    put: function(state){
        $.ajax({
          type: "PUT",
          url: '/wookie/warp/'+Controller.policy_id+'?granted='+state,
          datatype:'json',
          success: function(msg) {
            $.get("/wookie/warp?format=xml&widgetId="+Controller.widget.id, Controller.parseWidgetLevel);
          }
        });
    }
}