var proxyUrl="http://localhost:8081/wookie/proxy?url=";
var instanceid_key="idkeythingy";

var page_request = false

function get_tvar()
{
	var t_req;
	var myId = this.video_id;
	var myLen = this.length;

	var url = proxyUrl + "http://conexn.com/widgets/codes/youtube/youtube.php?lookup&id=" + myId
	if (window.XMLHttpRequest)
		t_req = new XMLHttpRequest()
	else if (window.ActiveXObject){
		try {
			t_req = new ActiveXObject("Msxml2.XMLHTTP")
		} 
		catch (e){
		try{
			t_req = new ActiveXObject("Microsoft.XMLHTTP")
		}
			catch (e){}
		}
	}

	t_req.onreadystatechange = function() {
		if (t_req.readyState == 4 && (t_req.status==200 || window.location.href.indexOf("http")==-1))
			openlink(myLen,myId,t_req.responseText)
	};

	t_req.open('GET', url, true)
	t_req.send(null);
}


function closeVideo()
{
	window.resizeTo(374,298);
	document.getElementById('video').innerHTML = '';
	document.getElementById('video_container').style.display = 'none'
}

function openSite(url)
{
	widget.openURL(url)
}

var str;

function resizeVideo()
{
	document.getElementById('theVideo').parentNode.setAttribute('width',window.innerWidth-60);
	document.getElementById('theVideo').parentNode.setAttribute('height',window.innerHeight-78);
}

function load_page(url)
{
	if (window.XMLHttpRequest)
		page_request = new XMLHttpRequest()
	else if (window.ActiveXObject){
		try {
			page_request = new ActiveXObject("Msxml2.XMLHTTP")
		} 
		catch (e){
		try{
			page_request = new ActiveXObject("Microsoft.XMLHTTP")
		}
			catch (e){}
		}
	}
	
	page_request.open('GET', url, true)
	page_request.send(null);
}

function openlink(length,video_id,t)
{

	window.resizeTo(500,500);
			
	str = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,28,0">'
	str += '<param name="movie" value="http://conexn.com/widgets/codes/youtube/player.swf?len='+length+'&id='+video_id+'&t='+t+'">'
	str += '<param name="quality" value="high">'
	str += '<embed src="http://conexn.com/widgets/codes/youtube/player.swf?len='+length+'&id='+video_id+'&t='+t+'" quality="high" pluginspage="http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash" type="application/x-shockwave-flash" id="theVideo" width="100%" height="100%"></embed></object>'

	document.getElementById('goto_image').value = video_id;

	document.getElementById('video_container').style.display = 'block'	
	document.getElementById('video_container').style.width = '100%'	
	document.getElementById('video_container').style.height = '100%'	
		
	setTimeout("document.getElementById('video').innerHTML = str; resizeVideo();",500);

	load_page('http://conexn.com/widgets/codes/youtube/youtube.php?player');

	//widget.openURL(this.linker)
}

function addRow(title,mylink,desc,added,views,myImage,myLength,video_id)
{
	var table = document.createElement('table')
	table.width = '100%'
	table.className = 'item_table'
	table.cellSpacing = 0;
	table.cellPadding = 0;
	table.linker = mylink;
	table.length = myLength;
	table.video_id = video_id;
	table.onclick = get_tvar;

	var titlerow = document.createElement('tr')
	var inforow = document.createElement('tr')
	var descrow = document.createElement('tr')
	
	var imagecell = document.createElement('td')
	imagecell.innerHTML = "<img width='105px' src='"+myImage+"' />"
	imagecell.width = '105px'
	imagecell.rowSpan = 3;
	imagecell.className = 'image_cell'
	
	var titlecell = document.createElement('td')
	titlecell.innerHTML = "<span class='title'>"+title+"</span>"
	titlecell.style.verticalAlign = 'top'

	var infocell = document.createElement('td')
	infocell.innerHTML = views + ' views - ' + added
	infocell.className = 'info'
	infocell.style.verticalAlign = 'top'

	var desccell = document.createElement('td')
	desccell.innerHTML = desc
	desccell.className = 'desc'
	desccell.style.verticalAlign = 'top'
	
	descrow.appendChild(desccell)
	inforow.appendChild(infocell)
	
	titlerow.appendChild(imagecell)
	titlerow.appendChild(titlecell)
	
	table.appendChild(titlerow)
	table.appendChild(inforow)
	table.appendChild(descrow)
	
	document.getElementById('result_table').appendChild(table)
	// commented out below by PAUL S
	//scrollArea.object.refresh();
	
}

function build_table(results)
{
	document.getElementById('content').innerHTML = "<table width='100%' cellpadding=0 cellspacing=0 border=0><tr><td id='result_table'></td></tr></table>"
	for(var x=0; x<results.length; x++)
	{
		var info = results[x].childNodes
		for(var i=0; i<info.length; i++)
		{
			var node = info.item(i).nodeName
			if(node == "title")
				var title = info.item(i).childNodes[0].nodeValue
			if(node == "link")
				var mylink = info.item(i).childNodes[0].nodeValue
			if(node == "description")
				var desc = info.item(i).childNodes[0].nodeValue
			if(node == "added")
				var added = info.item(i).childNodes[0].nodeValue
			if(node == "views")
				var views = info.item(i).childNodes[0].nodeValue
			if(node == "image")
				var myImage = info.item(i).childNodes[0].nodeValue
			if(node == "myLength")
				var myLength = info.item(i).childNodes[0].nodeValue
			if(node == "video_id")
				var video_id = info.item(i).childNodes[0].nodeValue
		}
		addRow(title,mylink,desc,added,views,myImage,myLength,video_id)	
	}
}

function processChange()
{
	if (page_request.readyState == 4 && (page_request.status==200 || window.location.href.indexOf("http")==-1))
	{
		var xmldoc = page_request;
		if (document.implementation && document.implementation.createDocument){
			xmldoc = page_request.responseXML;
		}
		var results = xmldoc.getElementsByTagName('result')
		
		if(results.length == 0)
		{
			document.getElementById('content').innerHTML = '<div align=center><i><b>No Results</b></i></div>'
			return;
		}
		
		build_table(results)
	}
}

function loader(url)
{
	
	if (window.XMLHttpRequest)
		page_request = new XMLHttpRequest()
	else if (window.ActiveXObject){
		try {
			page_request = new ActiveXObject("Msxml2.XMLHTTP")
		} 
		catch (e){
		try{
			page_request = new ActiveXObject("Microsoft.XMLHTTP")
		}
			catch (e){}
		}
	}
	
	page_request.onreadystatechange = processChange;

	page_request.open('GET', proxyUrl+url, true)
	
	page_request.send(null);
}


