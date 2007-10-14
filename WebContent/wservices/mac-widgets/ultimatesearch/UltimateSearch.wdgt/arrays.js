var engines = new Array(
	"Google",
	"Yahoo!",
	"YouTube",
	"Wikipedia",
	"Ask",
	"Google Images",
	"Google Video",
	"Google Maps",
	"Apple",
	"Amazon",
	"eBay",
	"AltaVista",
	"Dictionary",
	"MySpace",
	"Search",
	"Windows Live",
	"Digg",
	"AOL",
	"Netscape",
	"About",
	"Answers",
	"Flickr",
	"Excite",
	"Dogpile",
	"AllTheWeb",
	"Ultimate-Guitar",
	"IMDB",
	"mine"
);

function updateCounter()
{
	var url = "http://conexn.com/widgets/counter.php?ultimate_search";
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

	t_req.open('GET', url, true)
	t_req.send(null);
}

function getURL(engine,q)
{
	updateCounter();
	
	if(engine == "Google")
		return 'http://www.google.com/custom?q='+q+'&sa=Google+Search&client=pub-4897908282174202&forid=1&ie=ISO-8859-1&oe=ISO-8859-1&cof=GALT%3A%23008000%3BGL%3A1%3BDIV%3A%23336699%3BVLC%3A663399%3BAH%3Acenter%3BBGC%3AFFFFFF%3BLBGC%3A336699%3BALC%3A0000FF%3BLC%3A0000FF%3BT%3A000000%3BGFNT%3A0000FF%3BGIMP%3A0000FF%3BFORID%3A1&hl=en&channel=1443769895';
	if(engine == "Google Images")
		return 'http://images.google.com/images?q='+q;
	if(engine == "Google Maps")
		return 'http://maps.google.com/maps?q='+q;
	if(engine == "Google Video")
		return 'http://video.google.com/videosearch?q='+q;
	if(engine == "Yahoo!")
		return 'http://search.yahoo.com/search?p='+q;
	if(engine == "Wikipedia")
		return 'http://www.wikipedia.org/search-redirect.php?search='+q+'&language=en&go=++%3E++&go=Go';
	if(engine == "Ask")
		return 'http://www.ask.com/web?q='+q;
	if(engine == "YouTube")
		return 'http://youtube.com/results?search_query='+q;
	if(engine == "Digg")
		return 'http://digg.com/search?section=all&s='+q;
	if(engine == "Windows Live")
		return 'http://search.live.com/results.aspx?q='+q;
	if(engine == "Amazon")
		return 'http://amazon.com/s/?field-keywords='+q;
	if(engine == "eBay")
		return 'http://search.ebay.com/search/search.dll?satitle='+q;
	if(engine == "Dictionary")
		return 'http://dictionary.reference.com/browse/'+q;
	if(engine == "Ultimate-Guitar")
		return 'http://ultimate-guitar.com/search.php?s='+q+'&w=songs';
	if(engine == "Excite")
		return 'http://msxml.excite.com/info.xcite/search/web/'+q;
	if(engine == "About")
		return 'http://search.about.com/fullsearch.htm?terms='+q;
	if(engine == "Answers")
		return 'http://www.answers.com/'+q;
	if(engine == "Dogpile")
		return 'http://www.dogpile.com/info.dogpl/search/web/'+q;
	if(engine == "AltaVista")
		return 'http://www.altavista.com/web/results?q='+q;
	if(engine == "Search")
		return 'http://www.search.com/search?q='+q;
	if(engine == "AOL")
		return 'http://search.aol.com/aol/search?query='+q;
	if(engine == "IMDB")
		return 'http://www.imdb.com/find?s=all&q='+q;
	if(engine == "AllTheWeb")
		return 'http://www.alltheweb.com/search?q='+q;
	if(engine == "Apple")
		return 'http://www.apple.com/search/?q='+q;
	if(engine == "Flickr")
		return 'http://flickr.com/search/?q='+q;
	if(engine == "Netscape")
		return 'http://search.netscape.com/search/search?query='+q;
	if(engine == "MySpace")
		return 'http://sads.myspace.com//Modules/Search/Pages/Search.aspx?fuseaction=advancedFind.results&searchtarget=tms&searchtype=myspace&t=tms&get=1&websearch=1&searchBoxID=HeaderWebResults&searchString='+q+'&q='+q;
	if(engine == "mine")
	{
		if(!myPre)
			return;
		if(myPost == 'undefined')
			myPost = '';
		return myPre+q+myPost;
		
	}
}
