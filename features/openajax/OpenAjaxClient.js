/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

//
// Simple OpenAjax pubsub client wrapper for widgets.
// 
// To use call "hub.publish(topic, message)" and
// "hub.subscribe(topic, callback)"
//

window.hub = {};

window.hub.isConnected = false;
window.hub.subscriptions = [];
window.hub.publishings = [];

window.hub.connect = function(){
    if(typeof window.hub.oahub === 'undefined'){
        window.hub.oahub = new OpenAjax.hub.IframeHubClient({
            HubClient: {
              onSecurityAlert: function(){
                  console.log("OpenAjax Security Error!");
                  }
            }
        });	
        
        // Connect to the ManagedHub with callback function for asynchronous communication
        window.hub.oahub.connect(window.hub.onConnect);
    }
};

window.hub.onConnect = function(hub, success, error){
    if (success){
        window.hub.isConnected = true;
        window.hub.loadpending();
    } else {
        console.log(error);
        setTimeout(window.hub.connect, 1000);
    }
};

window.hub.subscribe = function(topic, callback){
  //
  // If the hub isn't connected yet, queue the action
  //
  if (window.hub.isConnected){
    window.hub.oahub.subscribe(topic, callback);
  } else {
    window.hub.subscriptions.push({"topic":topic, "callback":callback});
  }
};

window.hub.publish = function(topic, message){
  //
  // If the hub isn't connected yet, queue the action
  //
  if (window.hub.isConnected){
    window.hub.oahub.publish(topic,message);
  } else {
    window.hub.publishings.push({"topic":topic, "message":message});
  }
};

//
// Perform any pub/sub actions that were queued while the hub was being loaded
//
hub.loadpending = function(){
  for (var i=0;i<window.hub.subscriptions.length;i++){
    window.hub.oahub.subscribe(window.hub.subscriptions[i].topic, window.hub.subscriptions[i].callback);
  }
  window.hub.subscriptions = [];
  
  for (var j=0;j<window.hub.publishings.length;j++){
    window.hub.oahub.publish(window.hub.publishings[j].topic, window.hub.publishings[j].message);
  }
  window.hub.publishings = [];
};

window.hub.connect();



