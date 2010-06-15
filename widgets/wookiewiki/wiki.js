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
var wiki = new function Wiki(){

    this.currentPage = "WelcomePage";
    
    this.creole = new Parse.Simple.Creole( {
            forIE: document.all,
            interwiki: {
                WikiCreole: 'http://www.wikicreole.org/wiki/',
                Wikipedia: 'http://en.wikipedia.org/wiki/'
            }, linkFormat: ''});

    this.init = function(){
        wave.setStateCallback(wiki.updated);
        // Has the user viewed a page yet?
        page = Widget.preferences.getItem("currentPage");
        if(page != null && page != ""){
            wiki.currentPage = page;
        } else {
            wiki.currentPage = "WelcomePage";
            Widget.preferences.setItem("currentPage",wiki.currentPage);
        }
        wiki.load(wiki.currentPage);
    }
    
    // Clicking an interlink does this
    this.link = function(name){
        wiki.currentPage = name;
        wiki.hideEditor();
        wiki.load(name);
    }

    this.save = function(){
        // Save whats in the edit area, redisplay the display area
        wiki.display();
        var src = document.getElementById('editarea').value;
        var page = wiki.currentPage;
        var delta = [];
        // Save edits
        delta[page] = src;
        // Save metadata
        var date = new Date();
        var month = date.getMonth()+1;
        var history = wave.getState().get(page+"_history");
        if (!history) history = "";
        if (wave.getViewer().getDisplayName()!=""){
            delta[page+"_edited_by"] = wave.getViewer().getId();
            delta[page+"_history"] = history+"<br/>"+ wiki.getTime()+" : "+wave.getViewer().getDisplayName()+" ";
        } else {
            delta[page+"_history"] = history+"<br/>" + wiki.getTime()+" : anonymous ";        
        }
        wave.getState().submitDelta(delta);
    }
    
    this.load = function(page){
        var thepage = wave.getState().get(page);
        if (thepage == null || thepage == ""){
            if (page == "WelcomePage"){
                thepage = wiki.resetStartPage();
            } else {
                thepage = wiki.newPage(page);
            }
        }
        
        // Load edit area
        wiki.displayEdit(thepage);
        
        // Load display area
        wiki.display();
        
        // Load history
        wiki.updateHistory();
                
        // Set pref
        Widget.preferences.setItem("currentPage",wiki.currentPage);

    }
    
    this.display = function(){
        var input = document.getElementById('editarea');
        var displayarea = document.getElementById('displayarea');
        displayarea.innerHTML = '';
        wiki.creole.parse(displayarea, input.value);
    }
    
    this.displayEdit = function(page){
        var input = document.getElementById('editarea');
        input.value = page;
    }
    
    this.updateHistory = function(){
        var elem = document.getElementById('history_text');
        var history = wave.getState().get(wiki.currentPage+"_history");
        if (history){
            elem.innerHTML = "<h2>"+wiki.currentPage+":History</h2>" + history;
        }
    }
    
    this.updated = function(){
        wiki.updateHistory();
    }
    
    this.resetStartPage = function(){
        return "== Welcome! == \n Edit to get started";
    }
    
    this.newPage = function(name){
        return "== " + name + " ==";
    }
    
    this.hideEditor = function(){
        document.getElementById("edit").style.display = "None";
    }
    
    this.showEditor = function(){
        document.getElementById("edit").style.display = "Block";
    }

    this.showHistory = function(){
        document.getElementById("article").style.display = "None";
        document.getElementById("edit").style.display = "None";
        document.getElementById("history").style.display = "Block";
    }
    
    this.showArticle = function(){
        document.getElementById("history").style.display = "None";
        document.getElementById("article").style.display = "Block";
    }
    
    this.getTime = function(){
        var time = "";
        
        var currentTime = new Date();
        var month = currentTime.getMonth()+1;
        var year = currentTime.getFullYear();
        var day = currentTime.getDate();
        
        time = day+"/"+month+"/"+year+" ";
        
        var hours = currentTime.getHours()
        var minutes = currentTime.getMinutes()
        if (minutes < 10){
            minutes = "0" + minutes
        }
        time += hours + ":" + minutes + " ";
        if(hours > 11){
            time += "PM";
        } else {
            time += "AM";
        }
        return time;
    }

}
