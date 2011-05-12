/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
	
	  var osapi = osapi || {};
	  osapi.appdata =  {};
   
      osapi.appdata.get = function(options) {
       	var returnObject = {};
       	var data = {};
       	if (options.keys){
       		for (idx in options.keys){
       			data[options.keys[idx]] = wave.getState().get(options.keys[idx]);
       		}
       	} else {
       		data = wave.getState();
       	}
    	returnObject.execute = function(callback){
    		callback(data);
    	}
    	return returnObject;
      };
      
      osapi.appdata.update = function(options) {
         	var returnObject = {};
         	var data = options.data;

         	returnObject.execute = function(callback){
         		wave.getState().submitDelta(data);
         		osapi.appdata.__addCallback(callback);
         	}
         	return returnObject;
      };
  
      
      osapi.appdata.Delete = function(options) {
         	var returnObject = {};
           	var data = {};
           	if (options.keys){
           		for (idx in options.keys){
           			data[options.keys[idx]] = null;
           		}
           	}
      	returnObject.execute = function(callback){
       		wave.getState().submitDelta(data);
     		osapi.appdata.__addCallback(callback);
      	}
      	return returnObject;
      };
      
      // Push a callback onto the stack
      osapi.appdata.__addCallback = function(callback){
    	  osapi.appdata.callbacks = osapi.appdata.callbacks || [];
    	  osapi.appdata.callbacks.push(callback);
      }
      // Call all callbacks when state updated
      osapi.appdata.__updated = function(){
    	osapi.appdata.callbacks = osapi.appdata.callbacks || [];
  		while (osapi.appdata.callbacks.length > 0){
  		  var callback = osapi.appdata.callbacks.pop();
    	  setTimeout(callback,100); 
  		}
      }
   
      wave.setStateCallback( osapi.appdata.__updated);