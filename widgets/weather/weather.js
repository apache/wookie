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
// A widget for showing the weather in a given location.

//var serviceLocation = "http://feeds.bbc.co.uk/weather/feeds/rss/5day/world/"
var serviceLocation = "http://newsrss.bbc.co.uk/weather/forecast/";
var city="manchester";

var iconTable = new Array;
iconTable['sunny'] = 'images/sunny.png';
iconTable['sunnyintervals'] = 'images/sunny.png';
iconTable['rainy'] = 'images/rainy.png';
iconTable['drizzle'] = 'images/rainy.png';
iconTable['sleet'] = 'images/rainy.png';
iconTable['lightrain'] = 'images/rainy.png';
iconTable['heavyrain'] = 'images/rainy.png';
iconTable['lightshowers'] = 'images/rainy.png';
iconTable['heavyshowers'] = 'images/rainy.png';
iconTable['thunderyshowers'] = 'images/rainy.png';
iconTable['snowy'] = 'images/snowy.png';
iconTable['lightsnow'] = 'images/snowy.png';
iconTable['heavysnow'] = 'images/snowy.png';
iconTable['cloudy'] = 'images/cloudy.png';

var cities = new Array;
cities['belfast'] = '1';
cities['birmingham'] = '2';
cities['bristol'] = '3';
cities['cardiff'] = '4';
cities['edinburgh'] = '5';
cities['glasgow'] = '6';
cities['jersey'] = '7';
cities['london'] = '8';
cities['manchester'] = '9';
cities['newcastle'] = '10';
cities['southampton'] = '11';
cities['lerwick'] = '12';
cities['plymouth'] = '13';
cities['aberdeen'] = '14';
cities['st andrews'] = '15';
cities['inverness'] = '16';
cities['york'] = '17';
cities['leeds'] = '18';
cities['peterborough'] = '19';
cities['aberystwyth'] = '20';

var icon;
var celsius;
var fahrenheit;


function getDropDownList(){	
	var htmlText = "<select id=\"city_selector\" name=\"city_selector\" onchange=\"setCity(this.options[this.selectedIndex].value)\">";	
	for (i in cities) { 
		if(i==city){
			htmlText+="<option value='"+i+"' selected>"+ i +"</option>"
		}
		else{
			htmlText+="<option value='"+i+"'>"+ i +"</option>"
		}
	}
	htmlText += "</select>";
	htmlText += "<input type=\"submit\" class=\"groovybutton\" id =\"done\" value=\"Done\" onClick=\"showFront()\">";
	return htmlText;
}

function init(){	
	var pref = widget.preferences.getItem("city");
    setLocation(pref);
}

function setLocation(obj){
    var thecity = null;
    if (obj && obj != "null") thecity = obj.toLowerCase();
    if (thecity == null) thecity = "manchester";
    if (cities[thecity] != null) city = thecity;
    widget.preferences.setItem("city",city);
    startFetchingWeather();
}

function showBack(event)
{
	var front = document.getElementById("front");
	var back = document.getElementById("back");
	front.style.display="none";
	back.style.display="block";	
	document.getElementById("controls").innerHTML = getDropDownList();
	//WidgetUtil.setValue("controls", getDropDownList(),  { escapeHtml:false });
}

function showFront(event)
{
	var front = document.getElementById("front");
	var back = document.getElementById("back");
	front.style.display="block";
	back.style.display="none";	
}

function startFetchingWeather()
{
	fetchWeatherData();
    timer = setInterval ('fetchWeatherData();', 240000);
}

function fetchWeatherData(){
	var loc = serviceLocation + cities[city]+"/Next3DaysRSS.xml";    
    if (window.widget && typeof window.widget.proxify == 'function'){
        loc = widget.proxify(loc);
    }
    var xml_request = new XMLHttpRequest();
	xml_request.open("GET", loc, true);	
	xml_request.onreadystatechange = function()
	{
		if(xml_request.readyState == 4 && xml_request.status == 200){
			parseXMLData(xml_request.responseXML);
			updateDisplay();
		}
	}
	xml_request.setRequestHeader("Cache-Control", "no-cache");
	xml_request.send(null);
}

function parseXMLData(xmlobject){
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

function updateDisplay()
{
	var iconDiv = document.getElementById("weather");
	iconDiv.innerHTML = icon;
	var locationDiv = document.getElementById("city");
	locationDiv.innerHTML = city;
    var tempDiv = document.getElementById("temperature");
	tempDiv.innerHTML = "<p>"+celsius+"</p>";
}

function setCity(theCity){
	city = theCity.toLowerCase();    
    widget.preferences.setItem("city", city);
    fetchWeatherData();
}
