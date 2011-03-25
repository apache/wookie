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
	  osapi.people =  {};

      /**
      * Function to get Viewer profile.
      * Options specifies parameters to the call as outlined in the
      * JSON RPC Opensocial Spec
      * http://www.opensocial.org/Technical-Resources/opensocial-spec-v081/rpc-protocol
      * @param {object.<JSON>} The JSON object of parameters for the specific request
      */
      osapi.people.getViewer = function(options) {
       	var returnObject = {};
    	returnObject.execute = function(callback){
    		callback(wave.getViewer());
    	}
    	return returnObject;
      };

      /**
      * Function to get Viewer's friends'  profiles.
      * Options specifies parameters to the call as outlined in the
      * JSON RPC Opensocial Spec
      * http://www.opensocial.org/Technical-Resources/opensocial-spec-v081/rpc-protocol
      * @param {object.<JSON>} The JSON object of parameters for the specific request
      */
      osapi.people.getViewerFriends = function(options) {
    	var returnObject = {};
    	returnObject.execute = function(callback){
    		callback(wave.getParticipants());
    	}
    	return returnObject;
      };      

      /**
      * Function to get Owner profile.
      * Options specifies parameters to the call as outlined in the
      * JSON RPC Opensocial Spec
      * http://www.opensocial.org/Technical-Resources/opensocial-spec-v081/rpc-protocol
      * @param {object.<JSON>} The JSON object of parameters for the specific request
      */
      osapi.people.getOwner = function(options) {
         	var returnObject = {};
        	returnObject.execute = function(callback){
        		callback(wave.getHost());
        	}
        	return returnObject;
      };
      
      /**
      * Function to get Owner's friends' profiles.
      * Options specifies parameters to the call as outlined in the
      * JSON RPC Opensocial Spec
      * http://www.opensocial.org/Technical-Resources/opensocial-spec-v081/rpc-protocol
      * @param {object.<JSON>} The JSON object of parameters for the specific request
      */
      osapi.people.getOwnerFriends = function(options) {
       	var returnObject = {};
    	returnObject.execute = function(callback){
    		callback(wave.getParticipants());
    	}
    	return returnObject;
      };
