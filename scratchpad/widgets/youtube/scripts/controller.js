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
	user: {},
	updateUser:function() {
        if (typeof wave != 'undefined'){
           if (wave.getViewer() != null){
			    Controller.user.id = wave.getViewer().getId();
			    Controller.user.username = wave.getViewer().getDisplayName();
			    Controller.user.thumbnail  = wave.getViewer().getThumbnailUrl();
		   }
        }
		if (Controller.user.thumbnail == "" || Controller.user.thumbnail == null) { 
			Controller.user.thumbnail = "anon.png";
		}
		if (Controller.user.username == null || Controller.user.username == "") {
			Controller.user.username = "anonymous";        
			Controller.user.id = "anonymous";
		}
	},
	
    
    //
    // Use Wave to synch video between users
    // TODO let users turn this off!
    //
	stateUpdated:function() {
       if(Controller.sync){
           var state = wave.getState().get("player_state");
           var time = wave.getState().get("player_time");
           var video = wave.getState().get("player_video");
           
           
           //
           // Received a "pause" event.
           // If we aren't already paused, pause the player and seek
           //
           if (state === "paused" && Controller.getState()!==2){
               $("#youtube-player-container").tubeplayer("pause");
               $("#youtube-player-container").tubeplayer("seek",time);  
           }
           //
           // Received a "play" event.
           // If we aren't already playing, seek and play
           //
           if (state === "playing" && Controller.getState()!==1){
               $("#youtube-player-container").tubeplayer("seek",time);  
               $("#youtube-player-container").tubeplayer("play");
                
           }
           if (state === "cued"){
               if (Controller.video !== video){
                $("#youtube-player-container").tubeplayer("cue",video);
               }
           }
        }
	},
    
    getState: function() {
            ytplayer = $("#youtube-player-container").tubeplayer('player');
            var state = ytplayer.getPlayerState(); 
            return state;    
    },
    
    getTime: function() {
            ytplayer = $("#youtube-player-container").tubeplayer('player');
            var time = 0;
            if (typeof ytplayer !== 'undefined' && typeof ytplayer.getCurrentTime() !== 'undefined' && ytplayer.getCurrentTime() !== null){
                var time = ytplayer.getCurrentTime().toString(); 
            }
            return time;
    },
        
    onPause:function(){
        if (typeof wave != 'undefined' && Controller.sync){  
            if (wave.getState().get("player_state") !== "paused"){
                var delta = {};
                delta["player_state"] = "paused";
                delta["player_time"] = Controller.getTime();
                wave.getState().submitDelta(delta);
            }
        }
    },
    
    onPlay:function(){
        if (typeof wave != 'undefined' && Controller.sync){
            if (wave.getState().get("player_state") !== "playing"){
                var delta = {};
                delta["player_state"] = "playing";
                delta["player_time"] = Controller.getTime();
                wave.getState().submitDelta(delta);
            }
        }  
    },
    
    onCued:function(){
        if (typeof wave != 'undefined' && Controller.sync){
            var delta = {};
            delta["player_state"] = "cued";
            delta["player_video"] = Controller.video;
            delta["player_time"] = Controller.getTime();
            wave.getState().submitDelta(delta);
        }  
    },
		
	participantsUpdated:function() {
	},
	    
	init:function() {
        Controller.video = "TQIwEZlOzp4"; // initial video to cue
        Controller.sync = true; // whether to sync actions across instances
		Controller.updateUser();
        
        //
        // If Wave is enabled, bind the sync control
        // 
        if (typeof wave != 'undefined'){
            $('#sync').change(Controller.setSync);
        } else {
            $('#sync-control').hide();
        }

        //
        // Setup the player
        //
        jQuery("#youtube-player-container").tubeplayer({
        width: 585, // the width of the player
        height: 440, // the height of the player
        allowFullScreen: "true", // true by default, allow user to go full screen
        initialVideo: Controller.video, // the video that is loaded into the player
        preferredQuality: "default",// preferred quality: default, small, medium, large, hd720
        onPlayerPaused: this.onPause, // after the pause method is called
        onPlayerPlaying: this.onPlay, // after the player is playing
        });
        
                
        //
        // If Wave is available, set up video synching
        //
        if (typeof wave != 'undefined'){
            wave.getState().submitValue("update-count", JSON.stringify(Controller.count));
            /**
            * Register the event handlers with the Wave feature
            */
            wave.setStateCallback(Controller.stateUpdated);
            wave.setParticipantCallback(Controller.participantsUpdated);
        }
        

	}
}

/**
 * Search YouTube with a JSONP result
 * Inspired by examples by Yvo Schaap: http://www.yvoschaap.com/youtube.html
 */
Controller.search = function(q){
    //
    // Get the search term
    //
    var q = $("#youtubeid").val();
    
    //
    // Create a javascript element that returns our JSON data.
    //
    var script = document.createElement('script');
    script.setAttribute('id', 'jsonScript');
    script.setAttribute('type', 'text/javascript');
    var orderby = 'relevance';
    var sortorder = 'descending';
    script.setAttribute('src', 'http://gdata.youtube.com/feeds/videos?vq='+q+'&max-results=4&alt=json-in-script&callback=Controller.showResults&orderby='+orderby+'&sortorder='+sortorder);
    
    //
    // Attach script to page, this will load the data into our page and call the function Controller.results
    //
    document.documentElement.firstChild.appendChild(script);
}

/**
 * Show the results
 */ 
Controller.showResults = function(results){
  //
  // Clear old results and create new element
  //
  $('#search-results-content').empty();
  var ol = document.createElement('ol');
  
  //
  // Iterate over entries in the JSON feed
  //
  if(results.feed.entry){
	  for (var i = 0; i < results.feed.entry.length; i++) {
	    var entry = results.feed.entry[i];
        
        //
        // Locate the actual video link URL
        //
	    for (var j = 0; j < entry.link.length; j++) {
	      if (entry.link[j].rel == 'alternate') {
	        url = entry.link[j].href;
	        break;
	      }
	    }
        
        //
        // Construct the preview <li> element
        //
        var thumb = entry['media$group']['media$thumbnail'][1].url;
        var description = entry['media$group']['media$description'].$t;
        var li = document.createElement('li');
        li.innerHTML = '<div class="video-result-entry"><img class="video-result-thumb" src="'+thumb+'" alt="'+entry.title.$t+'" onclick="Controller.showVideo(\''+url+'\')"><div class="video-result-title"><a href="#" onclick="Controller.showVideo(\''+url+'\')">'+entry.title.$t+'</a></div><div class="video-result-description">'+description+'</div></div>';
        ol.appendChild(li);
      }
   }
   
   //
   // Add the results to the search results page, and switch visibility of the pages
   //
   $('#search-results-content').append(ol);
   $('#search-results-page').show();
   $('#video-player-page').hide();
}

Controller.cancelSearch = function(){
   $('#search-results-page').hide();
   $('#video-player-page').show();
}

/**
 * Show a video from the results
 */
Controller.showVideo = function(url){

    //
    // Id extraction pattern taken from examples by Yvo Schaap: http://www.yvoschaap.com/youtube.html
    //
    var theURL = url.replace(/&feature=youtube_gdata/gi, '');    
    var match = theURL.lastIndexOf('=');
    if (match) {
      id = theURL.substring(match+1);
      
      Controller.video = id;
      
      //
      // Switch visibility of pages, and cue the player
      //
      $('#search-results-page').hide();
      $('#video-player-page').show();
      $("#youtube-player-container").tubeplayer('cue', id);
      Controller.onCued();
    }
}

Controller.setSync = function(){
  if (Controller.sync){
    Controller.sync = false;
  } else {
    Controller.sync = true;
  }
}
