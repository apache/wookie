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
/*
****************   DEFAULT VOTE WIDGET **************

******************************************************
*/
var isActive=false;
var isLocked= false;
var isAdmin = false;
var isQuestionSet = false;
var hasVoted = false;
var question = null;
var answers = null;
var viewer = null;

// on start up set some values & init with the server
function init() {
	if(!isActive){
		isActive = true;
        viewer = wave.getViewer().getId();
        wave.setStateCallback(update);
        wave.setParticipantCallback(function(){});
		if (Widget.preferences.getItem("conference-manager") == "true") isAdmin = true; 	
		if (Widget.preferences.getItem("moderator") == "true") isAdmin = true; 
        initLock(wave.getState().get("isLocked"));
        doSetup();
 	}
}

function doSetup(){
    isQuestionSet = wave.getState().get("isSet");
    hasVoted = wave.getState().get(viewer+"_hasVoted");
    if (!isQuestionSet || isQuestionSet == null) isQuestionSet = false;
	if (!hasVoted || hasVoted == null) hasVoted = false;
    if(isAdminUser() == true){
        if (isQuestionSet == "true"){
            showResults();
        } else {
            showForm();
        }
	} else {
		if (hasVoted == false){
			showQuestion();
		} else {
			showResults();
		}
	}
} 

function isAdminUser(){
	return isAdmin;
}
//########################################################
//  View utils

function hidediv(id) {
	//safe function to hide an element with a specified id
	if (document.getElementById) { // DOM3 = IE5, NS6
		document.getElementById(id).style.display = 'none';
	}
	else {
		if (document.layers) { // Netscape 4
			document.id.display = 'none';
		}
		else { // IE 4
			document.all.id.style.display = 'none';
		}
	}
}

function showdiv(id) {
	//safe function to show an element with a specified id
	if (document.getElementById) { // DOM3 = IE5, NS6
		document.getElementById(id).style.display = 'block';
	}
	else {
		if (document.layers) { // Netscape 4
			document.id.display = 'block';
		}
		else { // IE 4
			document.all.id.style.display = 'block';
		}
	}
}

//########################################################
// Locking

var clearText = "<a href=\"#\" onclick=\"resetAll()\"><img border=\"0\" src=\"reset.png\" alt=\"Clear this vote\"></a>";

var adminUnlockText = "<div id=\"lock\"><a href=\"#\" onclick=\"unlockvote()\"><img border=\"0\" src=\"lock.png\" alt=\"Unlock this Widget\"></a>"+clearText+"</div>";

var adminLockText = "<div id=\"lock\"><a href=\"#\" onclick=\"lockvote()\"><img border=\"0\" src=\"unlocked.png\" alt=\"Lock this Widget\"></a>"+clearText+"</div>";

function initLock(locked){
	if (locked == "true"){
		handleLocked();
	} else {
		handleUnlocked();
	}
}

function handleLocked(){
	isLocked = true;	
	if(isAdminUser()) dwr.util.setValue("admin", adminUnlockText, { escapeHtml:false });	  
	dwr.util.setValue("question-notice","<p>The administrator has locked this vote</p>", {escapeHtml:false});
	disableButtons();
}

function handleUnlocked(){	
	isLocked = false;
	if(isAdminUser()) dwr.util.setValue("admin", adminLockText, { escapeHtml:false });;
	dwr.util.setValue("question-notice","", {});
	enableButtons();
}

function lockvote(){
	Widget.lock();
	handleLocked();
}

function unlockvote(){
	Widget.unlock();
	handleUnlocked()
}

//########################################################
// The Admin View

function showForm(){
	hidediv("results");
	hidediv("question");
	showdiv("form");
}

function setupQuestion(){
	var answers = "";
	var question = document.getElementById("question-input").value;
	if (question.length > 65){
		alert("That question is a bit long - try something briefer");
	} else {
		for (var i=1;i<5;i++){
			var a = document.getElementById("answer_"+i).value;
			if (a) answers += a+"|";
		}
        var delta={};
        delta["question"] = question;
        delta["answers"] = answers;
        delta["isSet"] = "true";
        wave.getState().submitDelta(delta);
	}
}

function resetAll(){
        var delta={};
        delta["question"] = "";
        delta["answers"] = "";
        delta["isSet"] = "false";
        var keys = wave.getState().getKeys()
        for (object in keys){
            if (keys[object].indexOf("_hasVoted")!=-1 || keys[object].indexOf("_result")!=-1){
                delta[keys[object]] = null;
            }
        }
        wave.getState().submitDelta(delta);
}

//########################################################
// The Participant View

function showQuestion(){
	hidediv("results");
	hidediv("form");
	showdiv("question");
}

function sendAnswer(i){
    var delta  = {};
    delta[viewer+"_result"] = i;
    delta[viewer+"_hasVoted"] = "true";
    wave.getState().submitDelta(delta);
	showResults();
}

function setQuestion(){
	question = wave.getState().get("question");
    if (question && question != null){
        dwr.util.setValue("question-text", question, { escapeHtml:false });
    } else {
        dwr.util.setValue("question-text","There isn't a vote set yet",{ escapeHtml:false });
    }
    
}

function setAnswers(){
    var a = wave.getState().get("answers");
    if (a && a!= null){
        answers = a.split("|");
        enableButtons();
    } else {
        answers = {};
        enableButtons();
    }
}

function disableButtons(){
	var text = "";
	for (i in answers){
		if (i){
			var answer = answers[i];
			if (answer && answer != ""){
				var style = (answer.length > 10) ? "longanswer" : "";
				text += "<button class=\"answer-button disabled "+style+"\" onclick=\"\">"+answer+"</button><br/>";
			}
		}
	}
    dwr.util.setValue("answers", text, { escapeHtml:false });
}

function enableButtons(){
	var text = "";
	for (i in answers){
		if (i){
			var answer = answers[i];
			if (answer && answer != ""){
				var style = (answer.length > 10) ? "longanswer" : "";
				text += "<button class=\"answer-button "+style+"\" onclick=\"sendAnswer('"+answer+"')\">"+answer+"</button><br/>";
			}
		}
	}
	dwr.util.setValue("answers", text, { escapeHtml:false });
}

//########################################################
// The Results View

function showResults(){
	hidediv("question");
	hidediv("form");
	showdiv("results");
}

function setResults(){
    var results={};
    var x = 0;
    var keys = wave.getState().getKeys();
    for (object in keys){
        if (keys[object].indexOf("_result")!=-1){
            results[x] = wave.getState().get(keys[object]);
            x++;
        }
    }

	dwr.util.setValue("results","",{escapeHtml:false});
	var num_votes = 0;
	var sorted_results = {};
	if (!results) return false;
	
	for (i in results){
		if (sorted_results[results[i]]){
			if (results[i] != ""){sorted_results[results[i]]++;num_votes++};
		} else {
			if (results[i] != ""){sorted_results[results[i]] = 1;num_votes++};
		}
	}
	
	sorted_results = sortByNumericValue(sorted_results);
	
	var text = "<table id=\"result-table\">";
	text+="<caption>"+question+"</caption>";
	text+="<tbody>";
	var i = 1;
	for (result in sorted_results){
		var percent = (sorted_results[result]/num_votes*100)
		if (percent < 100) percent = percent.toPrecision(2);
		var style = (result.length > 10) ? "longanswer" : "";
		text+="<tr id=\"answer"+i+"\"><td class=\"cell-answer answer"+i+" "+style+" \">"+result+"</td><td class=\"cell-result\">"+percent+"%</td></tr>";
		i++;
	}
	text+="</tbody>";
	text+="</table>";
	text+="<p>"+num_votes+" votes cast</p>";
	
	dwr.util.setValue("results",text,{escapeHtml:false});
}

function sortByNumericValue(A){
  B = [];
  result = {};
  for (i in A) B.push({ "v": i, "c": A[i] });
  B.sort(function (x, y){return y.c - x.c;});
  for (i in B){result[B[i].v] = B[i].c;}
  return result;
}

//########################################################


function update(){
    setQuestion();
    setAnswers();
    setResults();
    doSetup();
}

Widget.onLocked = handleLocked;
Widget.onUnlocked = handleUnlocked;
