/*
Copyright ＿ 2005, Apple Computer, Inc.  All rights reserved.
NOTE:  Use of this source code is subject to the terms of the Software
License Agreement for Mac OS X, which accompanies the code.  Your use
of this source code signifies your agreement to such license terms and
conditions.  Except as expressly granted in the Software License Agreement
for Mac OS X, no other copyright, patent, or other intellectual property
license or right is granted, either expressly or by implication, by Apple.
*/

var gFirstRequest = true;
var	gMinReloadTime 	= 60*60*1000;	// one hour
var gRetryTime		= 30*1000;		// thirty seconds

function patched_onshown () {
	if (Categories[defaultCatIndex].name == 'Time') 
	{
		drawClockBackground();
		installTimeTimer();
	}
	if (Categories[defaultCatIndex].name == 'Currency') 
	{
		patched_loadCurrencyExchangeRates();
	}
}

function patched_updateCurrencyUIElements()
{
	var catName = Categories[defaultCatIndex].name;
	// For Currency show status text and flipper, for other categories
	// hide the text and the flipped
	if (catName == 'Currency') {
		displayCurrencyStatusText();
		document.getElementById('flip').style.display = "block";
		flipShown = true;
	} else {
		document.getElementById('currency-status').innerText = "";
		document.getElementById('flip').style.display = "none";
		flipShown = false;
	}
	// if currency data is not available, only update Category
	// so user can switch if data is not available.
	if( catName == 'Currency' && currencyDataAvailable == -1) {
		populateUnitSelect(document.getElementById('fromUnit-popup'));
		populateUnitSelect(document.getElementById('toUnit-popup'));
		updatePopupLabels(false);
	} else {
		populateUnitSelect(document.getElementById('fromUnit-popup'));
		populateUnitSelect(document.getElementById('toUnit-popup'));
		// select the default or preferred units for the given category
		setUnitsFromPreferences();
		document.getElementById ('fromUnit-popup').options[defaultFromIndex].selected = true;
		document.getElementById ('toUnit-popup').options[defaultToIndex].selected = true;
		updatePopupLabels(true);
		document.getElementById('convertAmount-input').focus();
	}
}

function patched_setCategory ()
{
	var catName = Categories[defaultCatIndex].name;
	if (catName == 'Currency') {
		patched_loadCurrencyExchangeRates();
	}
	// Show the image for the current category
	// Time has a live clock, install timer and get the clock ticking...
	if (catName == 'Time') {
		drawClockBackground();
		installTimeTimer();
	}
	else document.getElementById ('catImage').src = "Images/"+Categories[defaultCatIndex].name+".png";
	// clear the input fields
	document.getElementById('converted-input').value = "";
	document.getElementById('convertAmount-input').value ="" ;
	patched_updateCurrencyUIElements();
}

function patched_setCurrencyTimer()
{
//	setCurrencyTimer should not be called"
}

function patched_loadCurrencyExchangeRates() 
{
	removeCurrencyTimer();
	var now = new Date().valueOf();
	var nextUpdateTime = Categories[defaultCatIndex].nextUpdate;
	if ((nextUpdateTime == null) || (nextUpdateTime < now)) {
		currencyLoadTries = 0;
		currencyDataAvailable = -1;
		fetchCurrencyData();
	}
}

function patched_exchangeRatesLoaded(object)
{
	removeCurrencyTimer();	// do this here as well just cuz we're paranoid
	gLastUnitConverterXMLRequest = null;
	var currencyCategory = Categories[currencyCatIndex];
	var now = new Date().valueOf();
	if (!object.error && (object.data)) {
		currencyCategory.array = object.data;
		currencyDataAvailable = 1;
		patched_updateCurrencyUIElements();
		var nextUpdateTime = object.nextUpdate;
		if (nextUpdateTime >  (now+gMinReloadTime)) {
			currencyCategory.nextUpdate = object.nextUpdate;
		} else {
			currencyCategory.nextUpdate = now+gMinReloadTime;
		}
		// save the rates once they are available, in case we need them later
		saveRatesToPreferences();
	}
	else {
		// there was an error
		// check if we already loaded the exchange rates from preferences
		if (currencyDataAvailable == -1) {
			currencyDataAvailable = ratesFromPreferences();
			if (currencyDataAvailable == 0) {
				patched_updateCurrencyUIElements();
				currencyCategory.nextUpdate = now+gMinReloadTime;
			}
		}
		// is data still not available then display messages 
		if (currencyDataAvailable == -1) {
			if (currencyLoadTries < 8) {
				document.getElementById('currency-status').innerText = getLocalizedString("Data unavailable.");
				timerCurrencyInterval = setInterval("patched_loadCurrencyExchangeRates()",gRetryTime);
			}
			Categories[currencyCatIndex].nextUpdate = null;
		}
	}
}



function performXMLRequest (callback)
{

	if (gFirstRequest) {
		window.setCategory = patched_setCategory;
		window.widget.onshow = patched_onshown;
		window.setCurrencyTimer = patched_setCurrencyTimer;
		window.exchangeRatesLoaded = patched_exchangeRatesLoaded;
		window.loadCurrencyExchangeRates = patched_loadCurrencyExchangeRates;
		callback = patched_exchangeRatesLoaded;
		gFirstRequest = false;
	}
	
	// var url = "http://apple.quote.com/fq/applewidgets/forex.asp?key=tHisIsApplewidgeTs";
	var url = "http://wu.apple.com/fq/applewidgets/forex.asp?key=tHisIsApplewidgeTs";

    var xml_request = new XMLHttpRequest();
    xml_request.onload = function(e) {xml_loaded(e, xml_request, callback);}
    xml_request.onerror = function() {alert("xml request failed");}
    xml_request.overrideMimeType("text/xml");
    xml_request.open("GET", url);
    xml_request.setRequestHeader("Cache-Control", "no-cache");
    xml_request.send(null);
    
    return xml_request;
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
			var currencyObj = {error:false, errorString:null, nextUpdate:null, data:new Array};
			var INDEXSET = findChild (request.responseXML, "INDEXSET");
			if (INDEXSET == null) {callback(constructError("no <INDEXSET>")); return;}
			
			var FOREX = findChild (INDEXSET, "FOREX");
			if (FOREX == null) {callback(constructError("no <FOREX>")); return;}
								
			var nextUpdateDate = findChild(FOREX, "NEXT_UPDATE_DATE");
			var nextUpdateTime = findChild(FOREX, "NEXT_UPDATE_TIME");				
			currencyObj.nextUpdate = createNextUpdateDateFromDateTime( nextUpdateDate.firstChild.data, nextUpdateTime.firstChild.data);

			var RATELIST = findChild (FOREX, "RATELIST");
			if (RATELIST == null) {callback(constructError("no <RATELIST>")); return;}
						
			var rateDateTime = null;
		
			for( var child = RATELIST.firstChild; child != null; child = child.nextSibling)
			{
				if( child.nodeName == "RATE" )
				{
					var rateDate = findChild(child, "DATE");
					var rateTime = findChild(child, "TIME");
										
					// var country = findChild(child, "COUNTRY");				//not using COUNTRY although available
					// var currency = findChild(child, "CURRENCY");				//not using CURRENCY although available
										
					var iso = findChild(child, "ISO");
					var currencyName = ConverterPlugin.currencyNameForCode( iso.firstChild.data );
					var flag = findChild(child, "IN_USD");					// not using FLAG
					var rateTo = findChild(child, "US_UNIT");
					var rateToUSDollar = rateTo.firstChild.data;
					
					
					// if feed provides value for country currency use it, otherwise compute it
					if( flag.firstChild.data == "false") 
					{
						var rateFrom = findChild(child, "CURRENCY_UNIT");
						var rateFromUSDollar = rateFrom.firstChild.data;
					}	
					else
						if ( rateToUSDollar != 0 ) 
							rateFromUSDollar = 1.0 / rateToUSDollar;
					rateDateTime = createDateFromDateTime( rateDate.firstChild.data, rateTime.firstChild.data);
					var symPrecision = computeSymmetricPrecision(rateToUSDollar,rateFromUSDollar);
					var newData = {name: currencyName, iso: iso.firstChild.data, lastUpdated: rateDateTime, toBase: rateToUSDollar, fromBase: rateFromUSDollar, precision:symPrecision};
					currencyObj.data[currencyObj.data.length] = newData;
				}
			}
			
			// Add US Dollar since the feed does not provide it
			var currencyName = ConverterPlugin.currencyNameForCode( "USD" );
			currencyObj.data[currencyObj.data.length] = 
				{name:currencyName,iso:"USD",lastUpdated:rateDateTime,toBase:1.0,fromBase:1.0, precision:computeSymmetricPrecision(1,1)};	
			currencyObj.data.sort();
			
			try {
				var now = new Date().valueOf();
				callback (currencyObj);
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


function createDateFromDateTime (dateString, timeString)
{	
	var array = dateString.split("-");

	var hour = parseFloat(timeString.substr(0,2));
	var min= parseFloat(timeString.substr(3,2));
	var sec = parseFloat(timeString.substr(6,2))
	var meridiam = timeString.substr(9, 2);

	var year = parseFloat(array[0]);
	var month = parseFloat(array[1]);
	var day = parseFloat(array[2]);
	if (isNaN(year) || isNaN(month) || isNaN(day) || isNaN(hour) || isNaN(min)  || isNaN(sec))
		return new Date(Infinity);
	var date =  new Date();
	date.setUTCFullYear(year);
	date.setUTCMonth(month-1);
	date.setUTCDate(day);
	date.setUTCHours(hour);
	date.setUTCMinutes(min);
	date.setUTCSeconds(sec);
	return date;
}


function createNextUpdateDateFromDateTime(dateString, timeString)
{	
	var array = dateString.split("/");
	
	var hour = parseInt (timeString.substr(0, 2), 10);
	var minute = parseInt (timeString.substr(3, 2), 10);
	var meridiam = timeString.substr(9, 2);
	
	var date =  new Date(array[2], array[0]-1, array[1], hour, minute); 	
	return date.valueOf();
}
