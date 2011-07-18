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
 
 /*
 * State class
 * Implements Google Wave API for gadget wave.State object
 * Would be easier if Google in their wisdom had reused HTML5 Storage
 */
 state = new function State(){
    this.map = null;
    
    this.get = function(key, opt_default){
        map = state.map;
        if (!map||map==null||typeof map == 'undefined'){
            if (opt_default) return opt_default;
            return null;
        }
        obj = map[key];
        if(!obj || obj === null || typeof obj == 'undefined'){
            obj = opt_default;
        }
        return obj;
    }
    
    this.getKeys = function(){
        var keys = [];
        var idx = 0;
        for (key in state.map){
            keys[idx] = key;
            idx++;
        }
        return keys;
    }
    
    this.submitDelta = function(delta){
        wave.submitDelta(delta);
    }
    
    this.submitValue = function(key,value){
    	var delta = {};
    	delta[key] = value;
    	wave.submitDelta(delta);
    }
    
    this.clear = function(){
        var delta =  {};
        for (key in state.map){
            delta[key] = null;
        }
        wave.submitDelta(delta);
    }
    
    this.toString = function(){
        var str = "";
        for (key in state.map){
            str+=key+":"+state.get(key);
        }
        return str;
    }
    
    this.__setState = function(object){
        state.map = object;
    }
 }

/*
 * Wave, singleton class
 * Implements Google Wave API for "gadgets"
 *
 */
 wave = new function Wave(){
 	this.participants = null;
 	this.viewer = null;
    this.callback = null;
    this.pcallback = null;
    this.onParticipantUpdate = null;
    this.isInWaveContainer = function(){ return true};
    
    this.init = function(){
        // Instantiate a Wave object, and load all values
		// Note we do this synchronously, as widgets are likely
		// to ask for a handle on this as an onLoad() event
		dwr.engine.beginBatch();
		WaveImpl.getParticipants(Widget.instanceid_key, this.setParticipants);
		WaveImpl.getViewer(Widget.instanceid_key, this.setViewer);
        WaveImpl.state(Widget.instanceid_key, this.setState);
		dwr.engine.endBatch({async:false});		
        dwr.engine.setActiveReverseAjax(true);
    }
    
    this.setState = function(data){
        state.__setState(data);
    }
    
    this.setParticipants = function(parts){
        var json = parts;
        if (json && json!=null && json!=""){
            var obj = eval('('+json+')');
            wave.participants = obj.Participants;
            for(participant in wave.participants){
                wave.participants[participant].getDisplayName = function(){return this.participant_display_name}
                wave.participants[participant].getThumbnailUrl = function(){return this.participant_thumbnail_url};
                wave.participants[participant].getId = function(){return this.participant_id};        
            }
        }
    }
    
    this.setViewer = function(v){
        if (v && v!=null && v!=""){ 
            var vobj = eval('('+v+')');
            wave.viewer = vobj.Participant;
            wave.viewer.getDisplayName = function(){return this.participant_display_name};
            wave.viewer.getThumbnailUrl = function(){return this.participant_thumbnail_url};
            wave.viewer.getId = function(){return this.participant_id};
        }
	}

    
    //////////////////////////////////////////////////
    // State Management
    
    this.getState = function(){
        return state;
    }
    
    this.submitDelta = function(delta){
        if (delta && delta!=null){
            // hack to force into a map
            var thedelta = {};
            for (object in delta){
                thedelta[object] = delta[object];
            }
            WaveImpl.submitDelta(Widget.instanceid_key, thedelta);
        }
	}
    
    // Sets the state callback; at this point we'll also do a 
    // state initial load - before this its a bit mean as the
    // widget won't know if its changed from its initial state.
    this.setStateCallback = function(callback, opt_context){
        wave.callback = callback;
        Widget.onSharedUpdate = wave.__callback;
        wave.__callback();
    }
    
    // We have to capture the callback method and wrap it in our private functions
    this.__callback = function(){
        WaveImpl.state(Widget.instanceid_key, wave.__update);
    }
    this.__update = function(data){
        state.__setState(data);
        wave.callback();
    }
    
    //////////////////////////////////////////////////
    // Participants
    
    this.getParticipants = function(){
 		return this.participants;
 	}
    
 	this.getViewer = function(){
 		return this.viewer;
 	}
    
    this.getHost = function(){
        return null; // NOT IMPLEMENTED
    }
    
    this.getParticipantById = function(id){
        for (x=0;x<this.participants.length;x++){
            if (this.participants[x].getId() == id) return this.participants[x];
        }
        return null;
    }
    
    this.setParticipantCallback = function(callback, opt_context){
        wave.pcallback = callback;
        wave.onParticipantUpdate = wave.__pcallback;
        wave.__pcallback();
    }
    // We have to capture the callback method and wrap it in our private functions
    this.__pcallback = function(){
    	WaveImpl.getParticipants(Widget.instanceid_key, wave.__pupdate);
    }
    this.__pupdate = function(data){
        wave.setParticipants(data);
        wave.pcallback();
    }
    
    //////////////////////////////////////////////////
    // Playback = NOT YET IMPLEMENTED
    this.getTime = function(){
        return null;
    }
    
    this.isPlayback = function(){ return false};
 }
 
 // very important !
wave.init();