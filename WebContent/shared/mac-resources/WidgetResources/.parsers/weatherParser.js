/*
Copyright ＿ 2005, Apple Computer, Inc.  All rights reserved.
NOTE:  Use of this source code is subject to the terms of the Software
License Agreement for Mac OS X, which accompanies the code.  Your use
of this source code signifies your agreement to such license terms and
conditions.  Except as expressly granted in the Software License Agreement
for Mac OS X, no other copyright, patent, or other intellectual property
license or right is granted, either expressly or by implication, by Apple.
*/

var accuweatherIcons = 
[
	["sun"], 					// 1 Sunny
	["sun"],						// 2 Mostly Sunny
	["partlycloudy"],			// 3 Partly Sunny
	["partlycloudy"],			// 4 Intermittent Clouds
	["sun", "haze"],				// 5 Hazy Sunshine
	["partlycloudy"],			// 6 Mostly Cloudy
	["clouds"],					// 7 Cloudy (am/pm)
	["clouds"],					// 8 Dreary (am/pm)
	null,						// 9 retired
	null,						// 10 retired
	["fog"],						// 11 fog (am/pm)
	["rain"],					// 12 showers (am/pnm)
	["rain&clouds"],				// 13 Mostly Cloudy with Showers
	["rain&sun"],				// 14 Partly Sunny with Showers
	["lightening"],				// 15 Thunderstorms (am/pm)
	["lightening"],				// 16 Mostly Cloudy with Thunder Showers
	["lightening"],				// 17 Partly Sunnty with Thunder Showers
	["rain"],					// 18 Rain (am/pm)
	["flurries"],				// 19 Flurries (am/pm)
	["flurries"],				// 20 Mostly Cloudy with Flurries
	["flurries"],				// 21 Partly Sunny with Flurries
	["snow"],					// 22 Snow (am/pm)
	["snow"],					// 23 Mostly Cloudy with Snow
	["ice"],						// 24 Ice (am/pm)
	["hail"],					// 25 Sleet (am/pm)
	["hail"],					// 26 Freezing Rain (am/pm)
	null,						// 27 retired
	null,						// 28 retired
	["rain&snow"],				// 29 Rain and Snow Mixed (am/pm)
	["sun"],						// 30 Hot (am/pm)
	["sun"],						// 31 Cold (am/pm)
	["wind"],					// 32 Windy (am/pm)
	// Night only Icons
	["moon"],					// 33 Clear
	["moon"],					// 34 Mostly Clear
	["moon", "partlycomboclouds"],// 35 Partly Cloudy
	["moon", "partlycomboclouds"],// 36 Intermittent Clouds
	["moon", "haze"],			// 37 Hazy
	["moon", "partlycomboclouds"],// 38 Mostly Cloudy
	["rain&clouds"],				// 39 Partly Cloudy with Showers
	["rain&clouds"], 			// 40 Mostly Cloudy with Showers
	["lightening"],				// 41 Partly Cloudy with Thunder Showers
	["lightening"],				// 42 Mostly Cloudy with Thunder Showers
	["snow"],					// 43 Mostly Cloudy with Flurries
	["snow"]						// 44 Mostly Cloudy with Flurries
];

var accuweatherMiniIcons = 
[
	"sun", 						// 1 Sunny
	"sun",						// 2 Mostly Sunny
	"suncloud",					// 3 Partly Sunny
	"suncloud",					// 4 Intermittent Clouds
	"sunhaze",					// 5 Hazy Sunshione
	"suncloud",					// 6 Mostly Cloudy
	"clouds",					// 7 Cloudy (am/pm)
	"clouds",					// 8 Dreary (am/pm)
	null,						// 9 retired
	null,						// 10 retired
	"fog",						// 11 fog (am/pm)
	"rain",						// 12 showers (am/pnm)
	"cloudrain",					// 13 Mostly Cloudy with Showers
	"sunrain",					// 14 Partly Sunny with Showers
	"lightening",				// 15 Thunderstorms (am/pm)
	"lightening",				// 16 Mostly Cloudy with Thunder Showers
	"lightening",				// 17 Partly Sunnty with Thunder Showers
	"rain",						// 18 Rain (am/pm)
	"flurries",					// 19 Flurries (am/pm)
	"flurries",					// 20 Mostly Cloudy with Flurries
	"flurries",					// 21 Partly Sunny with Flurries
	"snow",						// 22 Snow (am/pm)
	"snow",						// 23 Mostly Cloudy with Snow
	"ice",						// 24 Ice (am/pm)
	"hail",						// 25 Sleet (am/pm)
	"hail",						// 26 Freezing Rain (am/pm)
	null,						// 27 retired
	null,						// 28 retired
	"snowrain",					// 29 Rain and Snow Mixed (am/pm)
	"sun",						// 30 Hot (am/pm)
	"sun",						// 31 Cold (am/pm)
	"wind",						// 32 Windy (am/pm)
	// Night only Icons (shouldn't get these);
	null,						// 33 Clear
	null,						// 34 Mostly Clear
	null,						// 35 Partly Cloudy
	null	,						// 36 Intermittent Clouds
	null,						// 37 Hazy
	null,						// 38 Mostly Cloudy
	null,						// 39 Partly Cloudy with Showers
	null,			 			// 40 Mostly Cloudy with Showers
	null,						// 41 Partly Cloudy with Thunder Showers
	null,						// 42 Mostly Cloudy with Thunder Showers
	null	,						// 43 Mostly Cloudy with Flurries
	null							// 44 Mostly Cloudy with Flurries
];

var MoonMap=[1, 2, 3, 4, 5, 6, 7, 7, 8, 9, 10, 11, 12, 13, 13, 14, 15, 16, 17, 18, 19, 19, 20, 21, 22, 23, 24];

if (window.timerInterval != 300000)
	window.timerInterval = 300000; // 5 minutes

function findChild (element, nodeName)
{
	var child;
	
	for (child = element.firstChild; child != null; child = child.nextSibling)
	{
		if (child.nodeName == nodeName)
			return child;
	}
	
	return null;
}

function applyFunctionToChildrenWhoMatch (element, nodeName, func, data)
{
	var child;
	
	for (child = element.firstChild; child != null; child = child.nextSibling)
	{
		if (child.nodeName == nodeName)
		{
			func (child, data);
		}
	}
}

function trimWhiteSpace (string)
{
	return string.replace(/^\s*/, '').replace(/\s*$/, '');
}

// returns an anonymous object like so
// object
//		error: 	Boolean false for success
//		errorString: failure string
//		hi:		Fahrenheit
//		lo: 		Fahrenheit
//		temp: 	Fahrenheit
//		icon	:	accuweather icon code
//		icons:	our icons to display
//		description:	accuweather description
//		city:	City (first caps)
//		time:	time 24 hours(nn:nn)
//		sunset:	time 24 hours (nn:nn)
//		sunrise: time 24 hours (nn:nn)
//		phases: array[7] of integers; -1 means no phase data 1-24
//		forcast: array[6] of anonymous objects like so
//			object
//				hi:		Fahrenheit
//				lo: 		Fahrenheit
//				icon:	accuweather icon code
//				ouricon:	our icon code to display
//				description: accuweather description
//				daycode:	(MON/TUE/WED/THU/FRI/SAT/SUN)

function fetchWeatherData (callback, zip)
{
	//var url = 'http://apple.accuweather.com/adcbin/apple/Apple_Weather_Data.asp?zipcode=';
	var url = 'http://wu.apple.com/adcbin/apple/Apple_Weather_Data.asp?zipcode=';
	
	if (window.timerInterval != 300000)
		window.timerInterval = 300000; // 5 minutes

	var xml_request = new XMLHttpRequest();
	xml_request.onload = function(e) {xml_loaded(e, xml_request, callback);}
	xml_request.overrideMimeType("text/xml");
	xml_request.open("GET", url+zip);
	xml_request.setRequestHeader("Cache-Control", "no-cache");
	xml_request.setRequestHeader("wx", "385");
	xml_request.send(null);
	
	return xml_request;
}

function constructError (string)
{
	return {error:true, errorString:string};
}

// parses string of the form nn:nn
function parseTimeString(string)
{
	var obj = null;
	try {
		var array = string.match (/\d{1,2}/g);
		
		obj = {hour:parseInt(array[0], 10), minute:parseInt(array[1],10)};
	}
	catch (ex)
	{
		// ignore
	}
	
	return obj;
}

function parseDayCode (dayCode)
{
	return trimWhiteSpace(dayCode).substr (0, 3).toUpperCase();
}

function xml_loaded (event, request, callback)
{
	if (request.responseXML)
	{
		var obj = {error:false, errorString:null};
		var adc_Database = findChild (request.responseXML, "adc_Database");
		if (adc_Database == null) {callback(constructError("no <adc_Database>")); return;}
		
		var CurrentConditions = findChild (adc_Database, "CurrentConditions");
		if (CurrentConditions == null) {callback(constructError("no <CurrentConditions>")); return;}
		
		var tag = findChild (CurrentConditions, "Time");
		if (tag != null)
			obj.time = parseTimeString (tag.firstChild.data);
		else
			obj.time = null;

		tag = findChild (CurrentConditions, "City");
		if (tag == null) {callback(constructError("no <City>")); return;}
		obj.city =  trimWhiteSpace(tag.firstChild.data.toString()).toLowerCase();

		tag = findChild (CurrentConditions, "Temperature");
		if (tag == null) {callback(constructError("no <Temperature>")); return;}
		obj.temp = parseInt (tag.firstChild.data);
		
		tag = findChild (CurrentConditions, "WeatherText");
		if (tag == null)
			obj.description = null;
		else
			obj.description = trimWhiteSpace(tag.firstChild.data);
					
		tag = findChild (CurrentConditions, "WeatherIcon");
		if (tag == null) {callback(constructError("no <WeatherIcon>")); return;}
		obj.icon = parseInt (tag.firstChild.data, 10);
		if (obj.icon < 1 || obj.icon > 44) {callback(constructError("icon code invalid, out of range (1-44) " + obj.icon)); return;}
		obj.icons = accuweatherIcons[obj.icon-1];
		
		if (demoOverride != null)
		{
			obj.icon = demoOverride;
			obj.icons = accuweatherIcons[obj.icon];
		}

		obj.sunset = null;
		obj.sunrise = null;
		var Planets = findChild (adc_Database, "Planets");
		if (Planets != null)
		{
			tag = findChild (Planets, "Sun");
			if (Planets != null)
			{
				var rise = tag.getAttribute("rise");
				var set = tag.getAttribute("set");
				
				if (rise != null && set != null)
				{
					obj.sunset = parseTimeString (set);
					obj.sunrise = parseTimeString(rise);
				}
			}
		}
		
		var Moon = findChild (adc_Database, "Moon");
		obj.phases = new Array;
		if (Moon != null)
		{
			var child;
			var j =0;
			
			for (child = Moon.firstChild; child != null; child = child.nextSibling)
			{
				if (child.nodeName == 'Phase')
				{
					var phase = parseInt(child.firstChild.data, 10);
					if (phase < 1 || phase > 27 || isNaN (phase))
						phase = 15;
					phase--; // 0 base array
					phase = MoonMap[phase];
					obj.phases[j++] = phase;
					if (j == 7) break;
				}
			}
		}
		
		obj.forecast = new Array;
		var Forecast = findChild (adc_Database, "Forecast");
		if (Forecast == null) {callback(constructError("no <Forecast>")); return;}
		
		// assume the days are in order, 1st entry is today
		var child;
		var j=0;
		var firstTime = true;
		
		for (child = Forecast.firstChild; child != null; child = child.nextSibling)
		{
			if (child.nodeName == 'day')
			{
				if (firstTime) // today
				{
					/*tag = findChild(child, 'TXT_Long');
					if (tag != null) // replace the old description 
						obj.description = tag.firstChild.data;
					*/
					
					obj.hi = 0;
					tag = findChild(child, 'High_Temperature');
					if (tag != null)
						obj.hi = parseInt (tag.firstChild.data);
					
					obj.lo = 0;
					tag = findChild(child, 'Low_Temperature');
					if (tag != null)
						obj.lo = parseInt (tag.firstChild.data);
					
					firstTime = false;
				}

				var foreobj = {description:null, hi:0, lo:0, icon:-1};
				
				tag = findChild(child, 'TXT_Long');
				if (tag != null) // replace the old description 
					foreobj.description = trimWhiteSpace(tag.firstChild.data);
				
				tag = findChild(child, 'High_Temperature');
				if (tag != null)
					foreobj.hi = parseInt (tag.firstChild.data);
					
				tag = findChild(child, 'Low_Temperature');
				if (tag != null)
					foreobj.lo = parseInt (tag.firstChild.data);					
				
				tag = findChild(child, 'WeatherIcon');
				if (tag != null)
				{
					foreobj.icon = parseInt (tag.firstChild.data, 10);
					foreobj.ouricon = accuweatherMiniIcons[foreobj.icon-1];
				}
					
				tag = findChild (child, "DayCode");
				if (tag != null)
					foreobj.daycode = parseDayCode(tag.firstChild.data);
				else
					foreobj.daycode = null;
					
				obj.forecast[j++]=foreobj;
				
				if (j == 7) break; // only look ahead 7 days
			}
		}
		
		callback (obj);
		
	}
	else
	{
		callback ({error:true, errorString:"XML request failed. no responseXML"});
	}
}

// returns an anonymous object like so
// object
//		error: 	Boolean false for success
//		errorString: failure string
//		cities:	array (alphabetical by name)
//			object
//				name: city name
//				zip: postal code
//				state: city state
//		refine: boolean - true if the search is too generic
function validateWeatherLocation (location, callback)
{
	//var url = 'http://apple.accuweather.com/adcbin/apple/Apple_find_city.asp?location=';
	var url = 'http://wu.apple.com/adcbin/apple/Apple_find_city.asp?location=';
	
	var xml_request = new XMLHttpRequest();
	xml_request.onload = function(e) {xml_validateloaded(e, xml_request, callback);}
	xml_request.overrideMimeType("text/xml");
	xml_request.open("GET", url+location);
	xml_request.setRequestHeader("Cache-Control", "no-cache");
	xml_request.send(null);
}

function xml_validateloaded (event, request, callback)
{
	if (request.responseXML)
	{
		var obj = {error:false, errorString:null, cities:new Array, refine:false};
		var adc_Database = findChild (request.responseXML, "adc_Database");
		if (adc_Database == null) {callback(constructError("no <adc_Database>")); return;}
		
		var CityList = findChild (adc_Database, "CityList");
		if (CityList == null) {callback(constructError("no <CityList>")); return;}
		
		if (CityList.getAttribute('extra_cities') == '1')
			obj.refine = true;

		for (child = CityList.firstChild; child != null; child = child.nextSibling)
		{
			if (child.nodeName == "location")
			{
				var city = child.getAttribute("city");
				var state = child.getAttribute("state");
				var zip = child.getAttribute("postal");
				
				if (city && state && zip)
				{
					obj.cities[obj.cities.length] = {name:city, state:state, zip:zip};
				}
			}
		}
		
		callback (obj);
	}
	else
	{
		callback ({error:true, errorString:"XML request failed. no responseXML"});
	}
}

function createGoToURL (location)
{
	return 'http://apple.accuweather.com/adcbin/apple/Apple_weather.asp?location=' + location;
}
