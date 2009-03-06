// A widget for showing the weather in a given location.

var serviceLocation = "http://feeds.bbc.co.uk/weather/feeds/rss/5day/world/"
var city="Manchester";

var iconTable = new Array;
iconTable['sunny'] = 'images/sunny.png';
iconTable['sunnyintervals'] = 'images/sunny.png';
iconTable['rainy'] = 'images/rainy.png';
iconTable['lightshowers'] = 'images/rainy.png';
iconTable['drizzle'] = 'images/rainy.png';
iconTable['lightrain'] = 'images/rainy.png';
iconTable['heavyrain'] = 'images/rainy.png';
iconTable['snowy'] = 'images/snowy.png';
iconTable['lightsnow'] = 'images/snowy.png';
iconTable['heavysnow'] = 'images/snowy.png';
iconTable['cloudy'] = 'images/cloudy.png';

var cities = new Array;
cities['Belfast'] = '1';
cities['Birmingham'] = '2';
cities['Bristol'] = '3';
cities['Cardiff'] = '4';
cities['Edinburgh'] = '5';
cities['Glasgow'] = '6';
cities['Jersey'] = '7';
cities['London'] = '8';
cities['Manchester'] = '9';
cities['Newcastle'] = '10';
cities['Southampton'] = '11';
cities['Lerwick'] = '12';
cities['Plymouth'] = '13';
cities['Aberdeen'] = '14';
cities['St Andrews'] = '15';
cities['Inverness'] = '16';
cities['York'] = '17';
cities['Leeds'] = '18';
cities['Peterborough'] = '19';
cities['Aberystwyth'] = '20';

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
	Widget.preferenceForKey("city", setLocation);
}

function setLocation(obj){
    if (obj && obj != "null") city = obj;
    startFetchingWeather();
}

function showBack(event)
{
	var front = document.getElementById("front");
	var back = document.getElementById("back");
	front.style.display="none";
	back.style.display="block";	
	WidgetUtil.setValue("controls", getDropDownList(),  { escapeHtml:false });
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
	var loc = serviceLocation + cities[city]+".xml";    
    loc = Widget.proxify(loc);
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
	city = theCity;   
    Widget.setPreferenceForKey("city", city, null);
    fetchWeatherData();
}