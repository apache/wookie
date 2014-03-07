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
 
//
// Wookie JavaScript Connector 
// @version 0.14
//
//
// Example usage:
//
// Wookie.configureConnection("http://myserver.com/wookie", "MyAPIKey", "MySharedDataKey");
// Wookie.setCurrentUser("bob","the bobster","http://bob.com/bob.png");
// var instance = Wookie.getOrCreateInstance(widgetURI);
//

var Wookie = {
    widgets: [],
    currentUser: null,
    instances: {},
    connection: null,
    
    //
    // Get available widgets, and send
    // to the callback function when retrieved
    //
    getWidgets: function(callback){
   
        //
        // Use default connection if not set
        //
        if (Wookie.connection === null){
            Wookie.configureConnection(null, null, null);
        }     
    
        $.ajax({
         url: Wookie.connection.url + "/widgets", 
         accept: "text/xml",
         success:function(xml){
            Wookie.widgets = [];
            $(xml).find("widget").each(function(){
                var widget = {};
                widget.id = $(this).attr("id");
                widget.name = $(this).find("name").text();
                
                if ($(this).find("icon").length > 0){
                    widget.icon = $(this).find("icon").attr("src");
                } else {
                    widget.icon = "../shared/images/defaultwidget.png";
                }
                Wookie.widgets.push(widget);
            });
            callback(Wookie.widgets);
         }, 
         error:function(err){
            alert("error retrieving widgets");
         }
        });
    
    },
    
    setPreference: function(id, key, value){
        //
        // Use default connection if not set
        //
        if (Wookie.connection === null){
            Wookie.configureConnection(null, null, null);
        }
        
        //
        // Use default user if not set
        //
        if (Wookie.currentUser === null){
            Wookie.setCurrentUser("test","test user",null);
        }
        
        var postdata = "api_key=";
        postdata = postdata + encodeURI(Wookie.connection.apiKey);
        postdata = postdata + "&nonce="+Math.random();
        postdata = postdata + "&shareddatakey=";
        postdata = postdata + encodeURI(Wookie.connection.sharedDataKey);
        postdata = postdata + "&timestamp="+new Date().toISOString();
        postdata = postdata + "&userid=";
        postdata = postdata + encodeURI(Wookie.currentUser.loginName);
        postdata = postdata + "&widgetid=";
        postdata = postdata + encodeURI(id);
        
        var payload = "{\"preferences\":[{\"name\": \""+key+"\", \"value\":\""+value+"\", \"readOnly\":false}]}";
        var uri = "/properties";
        var url = Wookie.connection.url +uri;
        var signature = Wookie.getSignature("POST", uri, "?" + postdata);
        $.ajax({
            type: 'POST',
            url: url + "?" + postdata,
            contentType: 'application/json',
            processData: false,
            data: payload,
            headers: {
                    "Authorization":signature
            },
            async: false
        });
    },

    getOrCreateInstance: function(id) {
    
        //
        // Use default connection if not set
        //
        if (Wookie.connection === null){
            Wookie.configureConnection(null, null, null);
        }
        
        //
        // Use default user if not set
        //
        if (Wookie.currentUser === null){
            Wookie.setCurrentUser("test","test user",null);
        }
        
        var key = id + ":" + Wookie.currentUser.loginName;
        
        if (this.instances[key] === undefined) {
            var postdata = "api_key=";
            postdata = postdata + encodeURI(Wookie.connection.apiKey);
            
            postdata = postdata + "&nonce="+Math.random();
            
            postdata = postdata + "&shareddatakey=";
            postdata = postdata + encodeURI(Wookie.connection.sharedDataKey);
            
            postdata = postdata + "&timestamp="+new Date().toISOString();
            
            postdata = postdata + "&userid=";
            postdata = postdata + encodeURI(Wookie.currentUser.loginName);
            postdata = postdata + "&widgetid=";
            postdata = postdata + encodeURI(id);
            
            var uri = "/widgetinstances";
            var url = Wookie.connection.url + uri;
            
            var signature = Wookie.getSignature("POST", uri, "?" + postdata);
            
            $.ajax({
                type: 'POST',
                url: url,
                data: postdata,
                headers: {
                    "Authorization":signature
                },
                success: function(doc) {
                    url = $(doc).find("url").text();
                    title = $(doc).find("title").text();
                    height = $(doc).find("height").text();
                    width = $(doc).find("width").text();
                    
                    var instance = {};
                    instance.url = url;
                    instance.id = id;
                    instance.title = title;
                    instance.height = height;
                    instance.width = width;
                    Wookie.instances[key]=instance;
                },
                async: false
            });
            
            var postdata = "api_key=";
            postdata = postdata + encodeURI(Wookie.connection.apiKey);
            postdata = postdata + "&nonce="+Math.random();
            postdata = postdata + "&participant_display_name=";
            postdata = postdata + encodeURI(Wookie.currentUser.screenName);
            postdata = postdata + "&participant_id=";
            postdata = postdata + encodeURI(Wookie.currentUser.loginName);
            postdata = postdata + "&participant_role=";
            postdata = postdata + encodeURI(Wookie.currentUser.role);
            postdata = postdata + "&participant_thumbnail_url=";
            postdata = postdata + encodeURI(Wookie.currentUser.thumbnailUrl);
            postdata = postdata + "&shareddatakey=";
            postdata = postdata + encodeURI(Wookie.connection.sharedDataKey);
            postdata = postdata + "&timestamp="+new Date().toISOString();
            postdata = postdata + "&userid=";
            postdata = postdata + encodeURI(Wookie.currentUser.loginName);
            postdata = postdata + "&widgetid=";
            postdata = postdata + encodeURI(id);
            
            var uri = "/participants";
            var url = Wookie.connection.url + uri;
            
            var signature = Wookie.getSignature("POST", uri, "?" + postdata);

            $.ajax({
                type: 'POST',
                url: url,
                data: postdata,
                headers: {
                    "Authorization":signature
                },
                success: function(data) {
                },
                async: false
            });
        }
        return Wookie.instances[key];
    },
    
    getSignature: function(method, uri, query){
        var host = Wookie.connection.host
        var data = method + "\n";
        data = data + host + "\n";
        data = data + Wookie.connection.url + uri + "\n";
        data = data + query.toLowerCase();
        var hash = CryptoJS.HmacSHA256(data, Wookie.connection.secret).toString(CryptoJS.enc.Base64);
        return Wookie.connection.apiKey + " " + hash;
    },
    
    setCurrentUser: function(loginName, screenName, thumbnailUrl, role){
        var user = {};
        user.loginName = loginName;
        user.screenName = screenName;
        user.role = role;
        if (!thumbnailUrl || typeof thumbnailUrl === "undefined") {
            user.thumbnailUrl = "";
        } else {
            user.thumbnailUrl = thumbnailUrl
        }        
        Wookie.currentUser = user;
    },
    
    configureConnection: function(url, apiKey, sharedDataKey, secret, host){
        Wookie.connection = {};
        
        
        
        if (!url || typeof url === "undefined") {
            Wookie.connection.url = "/wookie";
        } else {
            Wookie.connection.url = url;
        }
        
        if (!apiKey || typeof apiKey === "undefined") {
            Wookie.connection.apiKey = "TEST";
        } else {
            Wookie.connection.apiKey = apiKey;
        }
        
        if (!secret || typeof secret === "undefined"){
            Wookie.connection.secret = "test@127.0.0.1";
        } else {
            Wookie.connection.secret = secret;
        }
        
        if (!host || typeof host === "undefined"){
            if (url.lastIndexOf("http://",0)===0){
                Wookie.connection.host = url.split("http://")[1];
            } else {
                Wookie.connection.host = "localhost:8080";
            }
        } else {
            Wookie.connection.host = host;
        }
        
        if (!sharedDataKey || typeof sharedDataKey === "undefined") {
            Wookie.connection.sharedDataKey = "mysharedkey";
        } else {
            Wookie.connection.sharedDataKey = sharedDataKey;
        }
    }
}