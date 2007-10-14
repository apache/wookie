/*
Copyright ＿ 2005, Apple Computer, Inc.  All rights reserved.
NOTE:  Use of this source code is subject to the terms of the Software
License Agreement for Mac OS X, which accompanies the code.  Your use
of this source code signifies your agreement to such license terms and
conditions.  Except as expressly granted in the Software License Agreement
for Mac OS X, no other copyright, patent, or other intellectual property
license or right is granted, either expressly or by implication, by Apple.
*/

// returns an anonymous object like so
// object
//		error: 	Boolean false for success
//		errorString: failure string
//		quotes: array[n] of anonymous objects like so
//			object
//				symbol:		stock symbol
//				lasttrade:	last trade for symbol
//				change:		delta change for symbol
//				pctchange:	percent change for symbol
//				open:		true if market is open, closed if not

function fetchStockData (callback, symbols)
{
	//var url = 'http://apple.quote.com/fq/applewidgets/quote.asp?key=tHisIsApplewidgeTs&symbols=';
	var url = 'http://wu.apple.com/fq/applewidgets/quote.asp?key=tHisIsApplewidgeTs&symbols=';
	var xml_request = new XMLHttpRequest();
	xml_request.onload = function(e) {xml_loaded(e, xml_request, callback);}
	xml_request.overrideMimeType("text/xml");
	xml_request.open("GET", url+symbols);
	xml_request.setRequestHeader("Cache-Control", "no-cache");
	xml_request.send(null);
}

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

function constructError (string)
{
	return {error:true, errorString:string};
}

function xml_loaded (event, request, callback)
{
	if (request.responseXML)
	{
		try {
		var obj = {error:false, errorString:null, quotes:new Array, open:true};
		var INDEXSET = findChild (request.responseXML, "INDEXSET");
		if (INDEXSET == null) {callback(constructError("no <INDEXSET>")); return;}
		
		for (var child = INDEXSET.firstChild; child != null; child = child.nextSibling)
		{
			if (child.nodeName == "QUOTE" || child.nodeName == "FUNDQUOTE")
			{
				var symbol = findChild(child, "SYMBOL");
				var last = findChild (child, "LASTTRADE");
				var change = findChild (child, "CHANGE");
				var pct = findChild (child, "PCTCHANGE");
				var MKT_STATUS = findChild(child, "MKT_STATUS");

				if (symbol == null) {callback(constructError("missing <SYMBOL>")); return;}
				if (last == null)
				{   // could be a mutual fund, check NAV
					last = findChild (child, "NAV");
					
					if (last == null) {callback(constructError("missing <LASTTRADE> or <NAV>")); return;}
				}
				if (change == null) {callback(constructError("missing <CHANGE>")); return;}
				if (pct == null) {callback(constructError("missing <PCTCHANGE>")); return;}
				
				if (MKT_STATUS != null)
					MKT_STATUS = MKT_STATUS.firstChild.data != '0';
				else
					MKT_STATUS = true;
					
				if (!MKT_STATUS)
					obj.open = false;
				
				obj.quotes[obj.quotes.length] = {symbol:symbol.firstChild.data, lasttrade:last.firstChild.data,
											   change:change.firstChild.data, pctchange:pct.firstChild.data,
											   open:MKT_STATUS};
			}
		}
		
			try {
				callback (obj);
			} catch (ex) {}
			
		}
		catch (ex)
		{
			callback ({error:true, errorString:"exception thrown when parsing stock data: " + ex});
		}
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
//		symbols: array[n] of anonymous objects like so
//			object
//				symbol:		stock symbol
//				name:		company name for symbol
// 				exchange:	the exchange for symbol

function validateSymbol (callback, symbol)
{
	//var url = 'http://apple.quote.com/fq/applewidgets/ticker.asp?key=tHisIsApplewidgeTs&search=';
	var url = 'http://wu.apple.com/fq/applewidgets/ticker.asp?key=tHisIsApplewidgeTs&search=';
	var xml_request = new XMLHttpRequest();
	xml_request.onload = function(e) {validate_xml_loaded(e, xml_request, callback);}
	xml_request.overrideMimeType("text/xml");
	xml_request.open("GET", url+symbol);
	xml_request.setRequestHeader("Cache-Control", "no-cache");
	xml_request.send(null);
	
	return xml_request;
}

function validate_xml_loaded (event, request, callback)
{
	if (request.responseXML)
	{
		try {
			var obj = {error:false, errorString:null, symbols:new Array, open:true};
			var INDEXSET = findChild (request.responseXML, "INDEXSET");
			if (INDEXSET == null) {callback(constructError("no <INDEXSET>")); return;}
			
			for (var child = INDEXSET.firstChild; child != null; child = child.nextSibling)
			{
				if (child.nodeName == "TICKER")
				{
					var name = findChild (child, "COMPANYNAME");
					var symbol = findChild(child, "SYMBOL");
					var exchange = findChild(child, "EXCHANGE");
					
					if (symbol == null) {callback(constructError("missing <SYMBOL>")); return;}
					
					// if we do not have a company name that is ok.
					if (name != null)
						name = name.firstChild.data;
					
					if (exchange != null)
						exchange = exchange.firstChild.data;
											
					obj.symbols[obj.symbols.length] = {symbol:symbol.firstChild.data, name:name, exchange:exchange};
				}
			}
			
			try {
				callback (obj);
			} catch (ex) {}
		}
		catch (ex)
		{
			callback ({error:true, errorString:"exception thrown when parsing stock symbol lookup data: " + ex});
		}
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
//		data: array[n] of anonymous objects like so
//			object
//				date:		Date js object
//				close:		close price of stock
function fetchChartData (symbol, code, callback)
{
	//var url = 'http://apple.quote.com/fq/applewidgets/chart.asp?key=tHisIsApplewidgeTs&symbols=' + symbol + "&interval=";
	var url = 'http://wu.apple.com/fq/applewidgets/chart.asp?key=tHisIsApplewidgeTs&symbols=' + symbol + "&interval=";
	var endstr = "";
	switch (code)
	{
		case "1d": endstr = "15"; break;
		case "1w": endstr = "60&period=7:D"; break;
		case "1m": endstr = "D&period=1:M"; break;
		case "3m": endstr = "D&period=3:M"; break;
		case "6m": endstr = "D&period=6:M"; break;
		case "1y": endstr = "D&period=12:M"; break;
		case "2y": endstr = "D&period=24:M"; break;
	}
	
	url += endstr;
	
	var xml_request = new XMLHttpRequest();
	xml_request.onload = function(e) {chartdata_xml_loaded(e, xml_request, callback);}
	xml_request.overrideMimeType("text/xml");
	xml_request.open("GET", url);
	xml_request.setRequestHeader("Cache-Control", "no-cache");
	xml_request.send(null);
	
	return xml_request;
}

function chartdata_xml_loaded (event, request, callback)
{
	if (request.responseXML)
	{
		try
		{
			var obj = {error:false, errorString:null, data:new Array};
	
			var INDEXSET = findChild(request.responseXML, "INDEXSET");
			if (INDEXSET == null) {callback(constructError("no <INDEXSET>")); return;}
	
			var CHART = findChild (INDEXSET, "CHART");
			if (CHART == null) {callback(constructError("no <CHART>")); return;}
			var QUOTES = findChild (CHART, "QUOTES");
			if (QUOTES == null) {callback(constructError("no <QUOTES>")); return;}
			
			var array = obj.data;
					
	
			for (var child = QUOTES.firstChild; child != null; child = child.nextSibling)
			{
				if (child.nodeName == "QUOTE")
				{
					var DATETIME = findChild (child, "DATETIME");
					var CLOSE = findChild(child, "CLOSE");
	
					if (DATETIME == null || CLOSE == null)
					{
						callback(constructError("<QUOTE> is missing a <DATETIME> or <CLOSE<"));
						return;
					}
					
					array[array.length] = {date:createDateFromDATETIME(DATETIME.firstChild.data),
										  close: parseFloat (CLOSE.firstChild.data)};
				}
			}
			
			try {
				callback (obj);
			} catch (ex) {
				// we do not want any eceptions to fire our exceptions from the callback
				// to fire our exception handler
			}
		}
		catch (ex)
		{
			callback ({error:true, errorString:"exception thrown when parsing stock chart data: " + ex});
		}
	}
	else
	{
		callback ({error:true, errorString:"XML request failed. no responseXML"});
	}
}

function createDateFromDATETIME (string)
{
	var hour = parseInt (string.substr(0, 2), 10);
	var minute = parseInt (string.substr(2, 2), 10);
	// skip seconds
	var month = parseInt(string.substr(7,2), 10) - 1;
	var day = parseInt(string.substr(9,2), 10);
	var year = parseInt(string.substr(11,4), 10);
	
	var date =  new Date(year, month, day, hour, minute);
	
	return date;
}
