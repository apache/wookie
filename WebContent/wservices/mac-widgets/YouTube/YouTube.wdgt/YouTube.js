var thisversion = '1.0';
var instanceid_key;
var proxyUrl;
var widgetAPIUrl;

function refresh()
{
	document.getElementById('content').innerHTML = "<center>Generating page<br /><img src='Images/loading.gif' /></center>"
	var code = document.getElementById('popup').value
	var url = "http://conexn.com/widgets/codes/youtube/youtube.php?default&code="+code
	loader(url);
	if(document.getElementById('CollapsiblePanel1').status == 'down')
	{
		CollapsiblePanel1.open();
		document.getElementById('CollapsiblePanel1').setAttribute('status','up')
		document.getElementById('img').src = 'Images/arrowup.png'
		setTimeout("document.getElementById('scrollArea').style.display = ''; scrollArea.object.refresh();",500)
	}
}

function toggle()
{
	if(document.getElementById('CollapsiblePanel1').getAttribute('status') == 'down')
	{
		CollapsiblePanel1.open();
		document.getElementById('img').src = 'Images/arrowup.png'
		document.getElementById('CollapsiblePanel1').setAttribute('status','up')
		setTimeout("document.getElementById('scrollArea').style.display = '';scrollArea.object.refresh();",500)
	}
	else if(document.getElementById('CollapsiblePanel1').getAttribute('status') == 'up')
	{
		document.getElementById('scrollArea').style.display = 'none'
		CollapsiblePanel1.close();
		document.getElementById('img').src = 'Images/arrowdown.png'
		document.getElementById('CollapsiblePanel1').setAttribute('status','down')
	}
}

function search(val)
{
	document.getElementById('content').innerHTML = "<center>Generating search results<br /><img src='Images/loading.gif' /></center>"
	var url = "http://conexn.com/widgets/codes/youtube/youtube.php?q="+val
	loader(url)
	
	if(document.getElementById('CollapsiblePanel1').getAttribute('status') == 'down')
	{
		CollapsiblePanel1.open();
		document.getElementById('img').src = 'Images/arrowup.png'
		document.getElementById('CollapsiblePanel1').setAttribute('status','up')
		setTimeout("document.getElementById('scrollArea').style.display = '';scrollArea.object.refresh();",500)
	}
	scrollArea.object.refresh();
}

function versioncheck(e,request)
{
	var widgets = request.responseXML.getElementsByTagName('widget').length;
	var ad = request.responseXML.getElementsByTagName('ad').item(0).getAttribute('content');
	var link = request.responseXML.getElementsByTagName('ad').item(0).getAttribute('link');
	document.getElementById('text9').innerHTML = ad;
	document.getElementById('text9').onclick = function() { openSite(link); }
	
	for(var i=0; i<widgets; i++)
	{
		var id = request.responseXML.getElementsByTagName('widget').item(i).getAttribute('name');
		if(id == "YouTube")
		{
			var version = request.responseXML.getElementsByTagName('widget').item(i).getAttribute('version');
			break;
		}
	}
	if(version != thisversion)
		document.getElementById('text8').style.display = "block";

}

function loadVersions()
{
		xml_request = new XMLHttpRequest();
		xml_request.onload = function(e) {versioncheck(e, xml_request);}
		xml_request.overrideMimeType("text/xml");
		
		xml_request.open("GET", proxyUrl+"http://conexn.com/widgets/versions.php");
		xml_request.setRequestHeader("Cache-Control", "no-cache");
		xml_request.send(null);
}


function load()
{
		// This gets the id_key and assigns it to instanceid_key
		// This page url will be called with e.g. idkey=4j45j345jl353n5lfg09cw03f05
		// so grab that key to use as authentication against the server
		var query = window.location.search.substring(1);
		var pairs = query.split("&");
		for (var i=0;i<pairs.length;i++){
			var pos = pairs[i].indexOf('=');
			if (pos >= 0){				
				var argname = pairs[i].substring(0,pos);
				if(argname=="idkey"){
					instanceid_key = pairs[i].substring(pos+1);
					//alert("idkey="+instanceid_key);
				}
				if(argname=="proxy"){
					proxyUrl = pairs[i].substring(pos+1) + "?url=";
					//alert("proxy="+proxyUrl);
				}
				if(argname=="serviceapi"){
					widgetAPIUrl = pairs[i].substring(pos+1);
					//alert("serviceapi="+widgetAPIUrl);
				}				
			}
		}
	setupParts();
	var def = widget.preferenceForKey("def");
	document.getElementById('popup').value = def;

	if(document.getElementById('content').innerHTML == '')
	{
		document.getElementById('content').innerHTML = "<center>Generating page<br /><img src='Images/loading.gif' /></center>"
		refresh();
	}
	
	loadVersions();
}

function remove()
{
	// your widget has just been removed from the layer
	// remove any preferences as needed
	// widget.setPreferenceForKey(instanceid_key, createInstancePreferenceKey("your-key"), null);
}

function hide()
{
	closeVideo()
	// your widget has just been hidden stop any timers to
	// prevent cpu usage
}

function show()
{
	// your widget has just been shown.  restart any timers
	// and adjust your interface as needed
}

function showBack(event)
{
	// your widget needs to show the back

	var front = document.getElementById("front");
	var back = document.getElementById("back");

	if (window.widget)
		widget.prepareForTransition("ToBack");

	front.style.display="none";
	back.style.display="block";
	
	if (window.widget)
		setTimeout('widget.performTransition();', 0);
}

function showFront(event)
{
	// your widget needs to show the front

	widget.setPreferenceForKey(instanceid_key, "def", document.getElementById('popup').value);

	var front = document.getElementById("front");
	var back = document.getElementById("back");

	if (window.widget)
		widget.prepareForTransition("ToFront");

	front.style.display="block";
	back.style.display="none";
	
	if (window.widget)
		setTimeout('widget.performTransition();', 0);
	
	refresh();

}

if (window.widget)
{
	widget.onremove = remove;
	widget.onhide = hide;
	widget.onshow = show;
}