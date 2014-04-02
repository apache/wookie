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

jQuery.expr[':'].Contains = function(a,i,m){
    return (a.textContent || a.innerText || "").toUpperCase().indexOf(m[3].toUpperCase())>=0;
};

$.extend({
  getUrlVars: function(){
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
      hash = hashes[i].split('=');
      vars.push(hash[0]);
      vars[hash[0]] = hash[1];
    }
    return vars;
  },
  getUrlVar: function(name){
    return $.getUrlVars()[name];
  }
});

//
// Get the current widgets installed and
// show in the browse list
//
function getWidgets(){
  var form = $("<form>").attr({"class":"filterform","action":"#"}),
    input = $("<input>").attr({"id":"filter","class":"filterinput","type":"text"});

  $(form).append(input);
  $('#widget_header').append(form);

  $(input).change( function () {
    var filter = $(this).val();
    if (filter) {
      $('#widget_list').find("li:not(:Contains(" + filter + "))").slideUp();
      $('#widget_list').find("li:Contains(" + filter + ")").slideDown();
    } else {
      $('#widget_list').find("li").slideDown();
    }
  }).keyup( function () {
    $(this).change();
  });
  Wookie.configureConnection("/wookie", "TEST", "mysharedkey");
  Wookie.getWidgets(updateWidgets);

  var id = $.getUrlVar("id");
  if(id) {
      showWidget(id);
  }  
}

function updateWidgets(widgets){
    for (var i=0;i<widgets.length;i++){
        var info = widgets[i].name + "\n" + widgets[i].id;
        var widgetEntry = $("<li id=\""+widgets[i].id+"\"class=\"widget\"><img src=\""+widgets[i].icon+"\" title='" + info + "'>"+widgets[i].name+"</li>");
        var id = widgets[i].id;
        $(widgetEntry).click(function(){
            showWidget($(this).attr("id"));
        });
        $("#widget_list").append(widgetEntry);
    }
    $('#filter').val($.getUrlVar("filter"));
    $('#filter').change();
}

function showWidget(id){
    Wookie.setCurrentUser("alice","alice","/wookie/demo/alice.png", "host");
    
    var title = "No title";
    for (i in Wookie.widgets){
       if (Wookie.widgets[i].id==id) title = Wookie.widgets[i].name;
    }
    
    $('#preview_info').html('<br/>' +name + '<br/>' + id);
    
    Wookie.getOrCreateInstance(id, renderWidget, "#preview_alice_widget");
    Wookie.getOrCreateInstance(id, renderWidget, "#preview_alice_tile");
    Wookie.setPreference(id, "conference-manager","true");
    
    Wookie.setCurrentUser("bob","bob","/wookie/demo/bob.png");
    Wookie.getOrCreateInstance(id, renderWidget, "#preview_bob_widget");
    Wookie.getOrCreateInstance(id, renderWidget, "#preview_bob_tile");
}

function renderWidget(widgetInstance, element){
    $(element).html('');
    $(element).append("<iframe src='"+widgetInstance.url+"' height='"+widgetInstance.height+"' width='"+widgetInstance.width+"'></iframe>");
}
