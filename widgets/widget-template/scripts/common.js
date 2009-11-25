var city = “Manchester”;

function init(){
	Widget.preferenceForKey(instanceid_key, "city", setLocation);
}

function setLocation(obj){
	if (obj && obj != "null") city = obj;
	startFetching();
}

function setCity(){
	var select = document.getElementById("city_selector");
	city = select.options[select.selectedIndex].value;
	widget.setPreferenceForKey("city", city, null);
	fetchWeatherData();
}

function startFetchingWeather() {
	fetchWeatherData();
	timer = setInterval ('fetchWeatherData();', 240000);
}

var cities = new Array;
cities['belfast'] = '1';
cities['birmingham'] = '2';
cities['manchester'] = '9';
cities['london'] = '8';

function fetchWeatherData() {
	var loc = "http://feeds.bbc.co.uk/weather/feeds/rss/5day/world/"+cities[city]+".xml";
	loc = Widget.proxify(loc);
	var xml_request = new XMLHttpRequest();
	xml_request.open("GET", loc, true);
	xml_request.onreadystatechange = function() {
		if(xml_request.readyState == 4 && xml_request.status == 200){
			parseXMLData(xml_request.responseXML);
			buildOutput();
		}
	}
	xml_request.setRequestHeader("Cache-Control", "no-cache");
	xml_request.send(null);
}

var iconTable = new Array;
iconTable['sunny'] = 'images/sunny.png';
iconTable['sunnyintervals'] = 'images/sunny.png';
iconTable['rainy'] = 'images/rainy.png';
iconTable['lightshowers'] = 'images/rainy.png';
iconTable['drizzle'] = 'images/rainy.png';
iconTable['lightrain'] = 'images/rainy.png';
iconTable['heavyrain'] = 'images/rainy.png';
iconTable['snowy'] = 'images/snowy.png';
iconTable['cloudy'] = 'images/cloudy.png';

function parseXMLData(xmlobject) {
	// The BBC formats weather feeds like this:
	// <title>Wednesday: cloudy, Max Temp: 10&#xB0;C (50&#xB0;F), Min Temp: 10&#xB0;C (50&#xB0;F)</title>
	var root = xmlobject.getElementsByTagName('rss')[0];
	var channel = root.getElementsByTagName('channel')[0];
	var item = channel.getElementsByTagName("item")[0];
	var title = item.getElementsByTagName("title")[0].firstChild.nodeValue;
	var desc = title.split(":")[1];
	desc = desc.split(",")[0];
	desc = desc.split(" ").join(""); //remove whitespace
	var temperature = title.split(":")[2];
	temperature = temperature.split(",")[0];
	celsius = temperature.split("(")[0];
	fahrenheit = temperature.split("(")[1];
	icon = '<img src="'+iconTable[desc]+'"/>';
}

function updateDisplay() {
	var iconDiv = document.getElementById("weather");
	iconDiv.innerHTML = icon;
	var locationDiv = document.getElementById("city");
	locationDiv.innerHTML = city;
	var tempDiv = document.getElementById("temperature");
	tempDiv.innerHTML = "<p>"+celsius+"</p>";
}