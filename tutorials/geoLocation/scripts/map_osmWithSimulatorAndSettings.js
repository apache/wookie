/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

var last_position = null;
    
var zoom = 17;
 
var map;
var epsg4326;
var route;
var routeMarkerIcon;
var req;

/**
 * Initialise the widget. The map will be created an tracking will be started.
 * 
 * @return
 */
function initMapWidget() {
	var options = {
   		maxExtent: new OpenLayers.Bounds(-20037508.34,-20037508.34,20037508.34,20037508.34),
   		units:'meters', 
   		projection: "EPSG:900913",
   		displayProjection: new OpenLayers.Projection("EPSG:4326"),
   		
    }; 
    map = new OpenLayers.Map('map', options);

    var mapnik = new OpenLayers.Layer.OSM.Mapnik("OpenStreetMap", {sphericalMercator:true});
    var gmap = new OpenLayers.Layer.Google("Google", {sphericalMercator:true, numZoomLevels: 20});
    var gphy = new OpenLayers.Layer.Google(
            "Google Physical",
            {type: G_PHYSICAL_MAP, sphericalMercator:true, numZoomLevels: 20}
        );
    var ghyb = new OpenLayers.Layer.Google(
        "Google Hybrid",
        {type: G_HYBRID_MAP, numZoomLevels: 20, sphericalMercator:true, numZoomLevels: 20}
    );
    var gsat = new OpenLayers.Layer.Google(
        "Google Satellite",
        {type: G_SATELLITE_MAP, numZoomLevels: 20, sphericalMercator:true, numZoomLevels: 20}
    );

    map.addLayers([mapnik, gmap, gphy, ghyb, gsat]);
	
    route = new OpenLayers.Layer.Markers( "Route" );
    map.addLayer(route);
    var size = new OpenLayers.Size(20,34);
    var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
    routeMarkerIcon = new OpenLayers.Icon('images/map/person.png', size, offset);

    map.addControl( new OpenLayers.Control.LayerSwitcher() );
    
    epsg4326 = new OpenLayers.Projection("EPSG:4326");

	startTracking();
}

/**
 * Start tracking using a simulated GPS for development.
 * 
 */
function startTracking() {
	if(geo_position_js.init()){
		// Set the simulated route
		locations=new Array();
		locations.push({ coords:{latitude:51.760768,longitude:-1.260123},duration:5000 });
		locations.push({ coords:{latitude:51.758643,longitude:-1.259823},duration:5000 });
		locations.push({ coords:{latitude:51.757872,longitude:-1.25978},duration:4000 });
		locations.push({ coords:{latitude:51.757766,longitude:-1.260381},duration:6000 });
		locations.push({ coords:{latitude:51.757421,longitude:-1.260166},duration:5000 });
		locations.push({ coords:{latitude:51.756757,longitude:-1.259866},duration:5000 });
		locations.push({ coords:{latitude:51.756491,longitude:-1.261368},duration:5000 });
		locations.push({ coords:{latitude:51.756039,longitude:-1.261711},duration:5000 });
		locations.push({ coords:{latitude:51.754871,longitude:-1.261325},duration:5000 });
		geo_position_js_simulator.init(locations);
		
		var lonLat = new OpenLayers.LonLat(locations[0].coords.longitude, locations[0].coords.latitude);
		lonLat.transform( epsg4326, map.getProjectionObject());
		map.setCenter(lonLat, zoom);
		setPosition(locations[0]);
		setInterval(retrieveCurrentLocation, 2000);
	} else{
		document.getElementById("message").innerHTML = "Geo-Location functionality not available";
	}
}

/**
 * Retrieve the current location and, if successful, set the current position on
 * the map.
 */
function retrieveCurrentLocation() {
	document.getElementById('message').innerHTML="Receiving...";
	geo_position_js.getCurrentPosition(setPosition, locationErrorCallback, {enableHighAccuracy:true});
}

/**
 * Set the position on the map. The map will be centred on this position.
 * 
 * @param p -
 *            the position object, providing coords.longitde and coords.latitude
 *            fields
 */
function setPosition(p) {
	if (p == null) {
		document.getElementById("message").innerHTML = "Failed to locate you.";
		return;
	}
	
	if(last_position && last_position.coords.latitude==p.coords.latitude && last_position.coords.longitude==p.coords.longitude) {
		document.getElementById('message').innerHTML="User has not moved.";
		return;
	}
	
	last_position = p;
	document.getElementById('message').innerHTML="User has moved.";

	var lonLat = new OpenLayers.LonLat(p.coords.longitude, p.coords.latitude);
	lonLat.transform( epsg4326, map.getProjectionObject());
	
    route.addMarker(new OpenLayers.Marker(lonLat, routeMarkerIcon.clone()));
	map.panTo(lonLat);
}

/**
 * Display an error message if there is a problem retrieving the current
 * location.
 * 
 * @param p
 */
function locationErrorCallback(p) {
	document.getElementById("message").innerHTML = p.message;
}		

/**
 * Display the settings page and hide the map page.
 * 
 * @param event
 *            the event that triggered this change in the display
 */
function showSettingsPage(event) {
	var mapPage = document.getElementById("mapPage");
	var settingsPage = document.getElementById("settingsPage");
	mapPage.style.display="none";
	
  	var htmlText = "<input type=\"checkbox\" id=\"displayStatus\" name=\"displayStatus\" onchange=\"Widget.preferences.setItem(\'displayStatus\', this.checked)\"";
  	if (Widget.preferences.getItem("displayStatus")) {
  		htmlText += " checked";
  	}
  	htmlText += "/>";
  	htmlText += "<label for=\"displayStatus\">Display status</p>";
  	htmlText += "<input type=\"submit\" class=\"groovybutton\" id =\"done\" value=\"Done\" onClick=\"showMapPage()\">";
	WidgetUtil.setValue("controls", htmlText,  { escapeHtml:false });
	settingsPage.style.display="block";	
}

/**
 * Display the map page and hide the settings page.
 * 
 */
function showMapPage() {
	var mapPage = document.getElementById("mapPage");
	var settingsPage = document.getElementById("settingsPage");
	mapPage.style.display="block";
	settingsPage.style.display="none";	

	if (Widget.preferences.getItem("displayStatus")) {
		document.getElementById("message").style.display="block";
	} else {
		document.getElementById("message").style.display="none";
	}
}