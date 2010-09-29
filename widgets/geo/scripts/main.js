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


// The current position

var latlng = null;

// Set up the map

var mapcanvas = document.createElement('div');
mapcanvas.id = 'mapcanvas';
mapcanvas.style.height = '400px';
mapcanvas.style.width = '540px';
document.querySelector('article').appendChild(mapcanvas);

// The map markers
var markers = new Array();

// Set up wave (once we implement that part)
if (window.wave){
 // setup callbacks here
}

//
// Add a marker for the current position and update the map
//
function addMarker(lat,lng,comment){
	if (window.wave){
		addSharedMarker(lat,lng,comment);
	} else {
		addSingleUserMarker(lat,lng,comment);
		updateSingleUserMap();
	}
}

//
// Use JSON to store marker data for current position
//
function addSingleUserMarker(lat,lng,comment){
	var pref = widget.preferences.getItem("markers");
	if (pref){
		var pmarkers = JSON.parse(pref);
	} else {
		var pmarkers = new Array();
	}
	var marker = {
      lat: lat,
      lng: lng, 
      title: "<p>"+comment + "</p><p style='color:gray'>" + new Date()+"</p>"
    };
    pmarkers.push(marker);
    widget.preferences.setItem("markers", JSON.stringify(pmarkers));
}

//
// To implement for a funky multi-user version
//
function addSharedMarker(marker){
}

//
// Update the map view in single-user (non-wave) mode
//
function updateSingleUserMap(){
	// Reset map
	clearMarkers();
	var myOptions = {
    	zoom: 15,
    	center: new google.maps.LatLng(50,0),
    	mapTypeControl: false,
    	navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
    	mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	
	var map = new google.maps.Map(document.getElementById("mapcanvas"), myOptions);
  
  	// Get markers
  	var pmarkers = widget.preferences.getItem("markers");
  	
  	// No existing markers
  	if (!pmarkers){
  		latlng = new google.maps.LatLng(53.435719,-2.285156);
  	} else {
  		pmarkers = JSON.parse(pmarkers);
	  	for (var m in pmarkers){
    		var infowindow = new google.maps.InfoWindow({
    			content: pmarkers[m].title
			});
	  		
  			latlng = new google.maps.LatLng(pmarkers[m].lat, pmarkers[m].lng);
  			var marker = new google.maps.Marker({
      			position: latlng, 
      			map: map
    		});
  			infowindow.open(map,marker);
    		markers.push(marker);
  		}
  	}
  	map.setCenter(latlng);
}

//
// To do for funky multi-user version
//
function updateSharedMap(){  	
}

//
// Clears any existing markers
//
function clearMarkers(){
    for(var i=0; i < markers.length; i++){
        markers[i].setMap(null);
    }
    markers = new Array();
}

//
// Got location, make a marker
//
var success = function(position) {
	setStatus("found where you are, posting comment","success");
  	var comment = document.querySelector('#comment').value;
  	addMarker(position.coords.latitude, position.coords.longitude, comment);
}

//
// Didn't get a location
//
function error(msg) {
	setStatus("Couldn't find you","error");
}

//
// Post a message
//
var post = function(){
	if (navigator.geolocation) {
	  	setStatus("checking","checking");
  		navigator.geolocation.getCurrentPosition(success, error);
	} else {
		setStatus("Geolocation isn't supported in your browser","error");
  		error('not supported');
	}
}


//
// Set status message
//
function setStatus(message, theclass){
  var s = document.querySelector('#status'); 
  s.innerHTML = message;
  s.className = theclass;
}

//
// Display the map when first loaded
//
if (window.wave){
	updateSharedMap();
} else {
	updateSingleUserMap();
}

