/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var DEFAULT_VALUE = "wookie-test-value-";
var TEST_SEPARATOR_START = "<chat>";
var TEST_SEPARATOR_END = "</chat><br/>";

function bulkTestPreferences(prefName, numberToTest){
	var outputStr = "";
	for(i=0;i<numberToTest;i++){
		Widget.preferences.setItem(prefName, DEFAULT_VALUE + i);
		outputStr+=widget.preferences.getItem(prefName) + "<br>";
	}	
	appendToLog(outputStr);
}

function bulkTestSharedData(prefName, numberToTest){
	var TEST_START = TEST_SEPARATOR_START + "<b>" + username + ":</b>";			
	if (document.forms['frm1'].useappend.checked == true){
		for(i=0;i<numberToTest;i++){
			appendSharedDataName(prefName, TEST_START +  DEFAULT_VALUE + i + TEST_SEPARATOR_END);	
		}
	}
	else{
		for(i=0;i<numberToTest;i++){
			setSharedDataName(prefName, TEST_START +  DEFAULT_VALUE + i + TEST_SEPARATOR_END);				
		}		
	}
}

function pauseBeforeGettingPreferences(prefName){
	var t=setTimeout("getPreferenceName('"+prefName+"')",5000);
}

function getPreferenceName(prefName){
	appendToLog("<i>(Widget.getPreference):"+ widget.preferences.getItem(prefName) + "</i><br/>");
	return false;
}

function setPreferenceName(prefName, prefValue){
	Widget.preferences.setItem(prefName, prefValue);
}

function getsharedData(dName){
	Widget.sharedDataForKey(dName, sharedDataCallback);
}

function sharedDataCallback(p){
	appendToLog("<i>(Widget.getSharedData):" + p + "</i><br/>");
}

function appendSharedDataName(dName, dValue){
	Widget.appendSharedDataForKey(dName, dValue);
}

function setSharedDataName(dName, dValue){
	Widget.setSharedDataForKey(dName, dValue);
}

function handleSharedDataUpdate(p){
	if (document.forms['frm1'].logsharedupdate.checked == true){
		appendToLog("<i>(Widget.onSharedUpdate) shareddatakey:" + p + "</i><br/>");
	}
}

function resetLog(){
	var fcanvas = document.getElementById('fcontent');
	fcanvas.innerHTML = "";
}

function resetWookieValue(dName){
	if(isTestingProperties()){
		setPreferenceName(dName, null)
	}
	else{
		Widget.setSharedDataForKey(dName, null);
	}
}

function getWookieValue(prefName){
	if(isTestingProperties()){
		getPreferenceName(prefName);
	}
	else{
		getsharedData(prefName);
	}
}

function setWookieValue(prefName, prefValue){
	if(isTestingProperties()){
		setPreferenceName(prefName, prefValue);
	}
	else{
		if (document.forms['frm1'].useappend.checked == true){
			appendSharedDataName(prefName, prefValue);
		}
		else{
			setSharedDataName(prefName, prefValue);
		}
	}
}

function startTest(prefName, numTimes){
	if(isTestingProperties()){
		alert("This test will now attempt to set the preferences " + numTimes + " times in succession");
		bulkTestPreferences(prefName, numTimes);
	}
	else{
		alert("This test will now attempt to set the shared data " + numTimes + " times in succession");
		bulkTestSharedData(prefName, numTimes);
	}
}

function isTestingProperties(){
	if (document.forms['frm1'].ptype[0].checked == true){
		return true;
	}
	else{
		return false;
	}
}

function appendToLog(str){
	var fcanvas = document.getElementById('fcontent');
	var existing = fcanvas.innerHTML;
	fcanvas.innerHTML = existing +str;
	fcanvas.scrollTop = fcanvas.scrollHeight;
}

function rnd_no(max){
    return Math.floor(Math.random()*max);
};

function init(){
	if (wave.getViewer() != null){
		username = wave.getViewer().getDisplayName();        
    }
    if (username == null || username == ""){
        username = "blitz_" + rnd_no(9999);        
    }
}

Widget.onSharedUpdate = handleSharedDataUpdate;