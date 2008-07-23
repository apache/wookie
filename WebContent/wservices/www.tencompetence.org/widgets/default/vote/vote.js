/*
****************   DEFAULT VOTE WIDGET **************

******************************************************
*/
var isDebug = false;
var currentLanguage = "en";
var supportedLanguages = new Array("bu","en","fr","nl");
var instanceid_key;
var proxyUrl;
var widgetAPIUrl;
var isActive=false;
var username="";
var thisUserUnlocked = false;
var isAdmin = false;
var isQuestionSet = false;
var answerSeparator = "<answer>";
var question = null;
var answers = null;
var lastVote = 0;
var responseCount=0; // count amount of formfields
var responseArr = new Array();


// on start up set some values & init with the server
function init() {
	if(!isActive){
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
				}
				if(argname=="proxy"){
					proxyUrl = pairs[i].substring(pos+1);
				}
				if(argname=="serviceapi"){
					widgetAPIUrl = pairs[i].substring(pos+1);
				}				
			}
		}	
		isActive = true;
		// this line tells DWR to use call backs (i.e. will call onsharedupdate() when an event is recevied for shared data
	 	dwr.engine.setActiveReverseAjax(true);
	 	widget.preferenceForKey(instanceid_key, "LDUsername", setLocalUsername);	 	
 	}
}

// set the local username
function setLocalUsername(p){
	username = p;	
	widget.preferenceForKey(instanceid_key, "conference-manager", getConferenceManagerRole);
}

function getConferenceManagerRole(p){
	if(p == "true") isAdmin = true;
	widget.preferenceForKey(instanceid_key, "moderator", getModeratorRole);
}

function getModeratorRole(p){	
	if(p == "true") isAdmin = true;
	widget.sharedDataForKey(instanceid_key, "isSet", doSetup);
}	

function doSetup(isSet){
    if(isSet == null){
		if(isAdminUser()){
			createQuestions();
		}
		else{
			showNotReadyYet();
		}
	}
	else{
		widget.preferenceForKey(instanceid_key, "hasVoted", showOrVoteScreen);
	
	}
} 

function isAdminUser(){
	return isAdmin;
}

function doLanguageUpdate(la){
	currentLanguage = la;	
	isActive = false;
	responseCount=0; //reset responses
	init();
}

function getLocalizedString(key) {
	try {
		var evalString = currentLanguage + "_" + "localizedStrings['"+key+"'];";		
		var ret = eval(evalString);
		if (ret === undefined)
			ret = key;
		return ret;
	}
	catch (ex) {
	}
	return key;
}

function getLangOpts(){
	var langOptionStr = "<select name=\"select_lang\" id=\"select_lang\" onchange=\"doLanguageUpdate(this.options[this.selectedIndex].value);\">" ;
 	var optStr; 
 	for (alang in supportedLanguages){
 		if(supportedLanguages[alang] == currentLanguage){
	 		optStr = "<option value=\"" + supportedLanguages[alang] + "\" selected>" + supportedLanguages[alang] + "</option>";
	 	}
	 	else{
 			optStr = "<option value=\"" + supportedLanguages[alang] + "\">" + supportedLanguages[alang] + "</option>";
 		}
 		langOptionStr += optStr;
	}
	langOptionStr += "</select>";
	return langOptionStr;
}

// #########################setting up the vote setion #################################





function showOrVoteScreen(hasVoted){
// show questions
	if(isAdminUser()){
		showVoteDisplayStageOne();		
	}
	else {	
		if(hasVoted!="true"){
			setupVoteDisplayStageOne();
		}
		else{
			showVoteDisplayStageOne();	
		}
	}
}


function createNewInput(){
	for(var k=1;k<=responseCount;k++){
		responseArr[k-1] =  findObj("response"+k).value;
	}
	responseCount++
	findObj("dynForm").innerHTML += getLocalizedString('Response')+ " "+responseCount+"<input type='text'  size='50' maxlength='100' id='response" + responseCount + "'><br>";
	for(k=1;k<responseCount;k++){
  		findObj("response"+k).value = responseArr[k-1];
	}
}

function doValidation(){
	var validResponseCount = 0;
	var responseXML = "";
	var responses ="";
			
	var q = findObj("questionfield").value;
	if (q.length < 1){
		alert(getLocalizedString('Step one: You have not set a question'));
		return;
	}

	for(var j=1;j<=responseCount;j++){
		rText =  findObj("response"+j).value;
		if(rText != ""){
			responses += getLocalizedString('Response') + " "+ j + ":  " + rText;
			responseXML+= answerSeparator + rText;
			// here we set all the number of votes of these to be zero			
			validResponseCount++;
		}
		else{
			responses += getLocalizedString('Response') + " "+ j + ":  " + "<" + getLocalizedString('Response is empty so will not be added') + ">";

		}
		responses += "\n";
	}

	if(validResponseCount < 2){
			alert(getLocalizedString('Step two: You need at least two responses'));
			return;
	}

	var confStr = getLocalizedString('Are you sure you have finished and want to set the vote?');
	confStr += "\n\n" + getLocalizedString('Choose OK to finish and make the vote available');
	confStr += "\n" + getLocalizedString('Choose Cancel to carry on editing') + "\n";
	
	var answer =  confirm(confStr + getLocalizedString('Question') + ": " + q + "\n" +responses);

	if (answer){
		widget.setSharedDataForKey(instanceid_key, "question", q);
		widget.setSharedDataForKey(instanceid_key, "answers", responseXML);
		widget.setSharedDataForKey(instanceid_key, "isSet", "true");
		// make a db entry for each of these options/responses, set to zero
		for(var j=1;j<=validResponseCount;j++){
			widget.setSharedDataForKey(instanceid_key, "response"+j, "0");
		}
		widget.setSharedDataForKey(instanceid_key, "totalvotes", "0");
		//now redirect to see the results page	
		showVoteDisplayStageOne();		
	}
	else{}

}

function showNotReadyYet(){
	var notReadytext = "<div id='legendDiv'>" + getLocalizedString('This vote instance has not yet been initialised') + "</div>";
	notReadytext += "<div id='responseDivFull'></div>";
	notReadytext += "<div id='submitDiv'><div align=\"left\" id=\"lang_opts\">" + getLangOpts() + "</div></div>";
	dwr.util.setValue("maincanvas", notReadytext, { escapeHtml:false });
}

function createQuestions(){
	var authorVoteText = "<div id='legendDiv'>" + getLocalizedString('Setup vote widget') + "</div>";
	authorVoteText += "<div id='questionDiv'>" + getLocalizedString('Step One: Enter the question here') + ":&nbsp;<input type='text' size='50' maxlength='100' id='questionfield'></div>";
	authorVoteText += "<div id='responseDiv'>" + getLocalizedString('Step Two: Create the responses') + ":&nbsp;(<a href='#' onClick='createNewInput()'>" + getLocalizedString('Click here to add more response fields') + "</a>)<div id='dynForm'></div></div>";
	authorVoteText += "<div id='submitDiv'><div align=\"left\" id=\"lang_opts\">" + getLangOpts() + "&nbsp;</div><a href='#' onClick='doValidation()'>" + getLocalizedString('Step Three: Click here to check and finish') + "</a></div>";
	dwr.util.setValue("maincanvas", authorVoteText, { escapeHtml:false });
}

function setupVoteDisplayStageOne(){
	widget.sharedDataForKey(instanceid_key, "question", setupVoteDisplayStageTwo);
}

function setupVoteDisplayStageTwo(pQuestion){
	question = pQuestion;
	widget.sharedDataForKey(instanceid_key, "answers", setupVoteDisplayStageThree);
}

function setupVoteDisplayStageThree(pAnswers){
	answers = pAnswers;
	
	var questionText = "<div id='questionDiv'><img border='0' src='/wookie/shared/images/vote.png'>"+question+"</div>";
	var answerText = "<div id='responseDiv'><form name='responseform'>";	
	var answerArray = pAnswers.split(answerSeparator);	
	var count=0;
	for (var data in answerArray) {		
		// put each answer in a new div
		if(answerArray[data].length>0){
			count++;
    		answerText += "<div>&nbsp;&nbsp;<INPUT TYPE=RADIO NAME='answer' VALUE='"+count+"'>&nbsp;" + dwr.util.escapeHtml(answerArray[data]) + "</div>";
    	}
    }
    answerText += "</form></div>";    
    var submitText= questionText+ answerText + "<div id='submitDiv'><div align=\"left\" id=\"lang_opts\">" + getLangOpts() + "&nbsp;</div><a href='#' onClick='doVote()'>" + getLocalizedString('Click here to vote') + "</a></div>";		
	dwr.util.setValue("maincanvas", submitText, { escapeHtml:false });
	var respDiv = dwr.util.byId('responseDiv');
	respDiv.style.height = "200px"; 
}

function doVote(){	
	var respValue = checkradioform();
	//check that the user has chosen a response
	if(respValue == -1){
		alert(getLocalizedString('Please select a response'));
		return;
	}
	else{
		// stop user clicking again
		var submitPressedText = getLocalizedString('Click here to vote');
		dwr.util.setValue("submitDiv", submitPressedText, { escapeHtml:false });
		// update the response count
		lastVote = respValue;
		widget.sharedDataForKey(instanceid_key, "response"+ respValue, doVoteUpdate);		
		widget.sharedDataForKey(instanceid_key, "totalvotes", doTotalVoteUpdate);
		// set in prefs that user has now voted
		widget.setPreferenceForKey(instanceid_key, "hasVoted", "true");
		// now show the vote	
		showVoteDisplayStageOne();		
	}
}

function doVoteUpdate(respCountVal){
	var actualIntCount = parseInt(respCountVal);   
	actualIntCount++;
	widget.setSharedDataForKey(instanceid_key, "response"+ lastVote, actualIntCount);
}

function doTotalVoteUpdate(totalCountVal){
	var totalIntCount = parseInt(totalCountVal);   
	totalIntCount++;
	widget.setSharedDataForKey(instanceid_key, "totalvotes", totalIntCount);
}

//######show the vote ########

function showVoteDisplayStageOne(){
	widget.sharedDataForKey(instanceid_key, "question", showVoteDisplayStageTwo);
}

function showVoteDisplayStageTwo(pQuestion){
	question = pQuestion;
	widget.sharedDataForKey(instanceid_key, "answers", showVoteDisplayStageThree);
}


function newClosure(someNum, someRef) {
  return function(x) {    	
          getResults(x, someNum);
    }     
}

function showVoteDisplayStageThree(pAnswers){
	answers = pAnswers;
	var submitDiv = null;
	var questionText = "<div id='questionDiv'><img border='0' src='/wookie/shared/images/vote.png'>"+question+"</div>";
	var answerText = "<div id='responseDiv'><form name='responseform'><table border='0'>";	
	var answerArray = pAnswers.split(answerSeparator);	
	var count=0;
	for (var data in answerArray) {		
		// put each answer in a new div
		if(answerArray[data].length>0){
			count++;			
    		answerText += "<tr><td width='290px'><div id='tt"+count+"'>&nbsp;" + dwr.util.escapeHtml(answerArray[data]) + "</div></td>";
    		answerText += "<td width='100px'><div id='bar"+count+"' style=\"width: 0px; background-color:#"+genHex()+";\">&nbsp;</div></td>";
    		answerText += "<td width='40px'><div id='percentDiv"+count+"'></div></td>";
    		answerText += "<td width='70px'><div id='sresponse"+count+"'></div></td></tr>";
    	}
    }
    answerText += "</table></form></div>";  
    submitDiv = "<div id='submitDiv'><div align=\"left\" id=\"lang_opts\">" + getLangOpts() + "&nbsp;</div><a href='#' onClick='showVoteDisplayStageOne()'><img border=\"0\" src=\"/wookie/shared/images/refresh.gif\">&nbsp;" + getLocalizedString('Refresh') + "</a></div>";
      
    var endText= questionText+ answerText + submitDiv;		
	dwr.util.setValue("maincanvas", endText, { escapeHtml:false });
		
	for(var k=1;k<=count;k++){	
		/* 		
		  ############ Description of this section of code ####################
		  Firstly, the problem occurs with using a version of DWR pre 3.0.
		  
		  You need to define & setup the function which will call 'getResults()' so that it gets 
		  the correct index from this loop.  In effect you are creating the function instance on the next line initialised 
		  with this loop index - 'k'.  The widget.sharedDataForKey call then calls this function instance as a callback from dwr.
		  If we didnt do this, then each call to 'getResults()' would pass the incorrect loop index -ie the final one
		  once it has completed.
		  
		  The closures described here for pre DWR 3.0(http://directwebremoting.org/dwr/browser/extradata) do not work for
		  situations where the data to be passed includes a looping variable.
		  
		  The approach here also does not work (http://nicklothian.com/blog/2007/05/18/dwr-callback-closures-inside-loops/)
		  		  
		  Example 5 here -(http://blog.morrisjohns.com/javascript_closures_for_dummies.html)
		  also shows how NOT to do this. 
		  Example 7 shows a similar problem, but not using loops.  It is this method of instance closures
		  that makes this call work for looping indexes - i.e. 'k' here, so study Example 7.
		  #####################################################################
		 */
		closure = newClosure(k, {somevar : 'closure'+k});	// create the instance, passing the k index as argument
		widget.sharedDataForKey(instanceid_key, "response"+k, closure);		// hand this instance as a callback to be executed once the shareddataforkey has completed
	}
}


function getResults(server, index){
	var textResults = "<div>&nbsp;("+server+" " + getLocalizedString('votes') + ")</div>";
	dwr.util.setValue("sresponse"+index, textResults, { escapeHtml:false });
	/*
	var mcallMetaData = { 
  			callback:updatePercentages, 
  			args: server+"#"+index  // specify an argument to pass to the callback and exceptionHandler  			
		};
	*/
	
	var mcallbackProxy = function(dataFromServer) {
	  updatePercentages(dataFromServer, server+"#"+index);
	};
			
	var mcallMetaData = { callback:mcallbackProxy };
	
	widget.sharedDataForKey(instanceid_key, "totalvotes", mcallMetaData);
}



/*
function returnObjById( id )
{
    if (document.getElementById)
        var returnVar = document.getElementById(id);
    else if (document.all)
        var returnVar = document.all[id];
    else if (document.layers)
        var returnVar = document.layers[id];
    return returnVar;
}
*/

function updatePercentages(total, oneResponse){
	
	
	var temp = new Array();
	temp = oneResponse.split('#');
	var tot = parseInt(total);	
	var thisResponse = parseInt(temp[0]);
	
	if(tot==0&&thisResponse==0){
		res = 0;
	}
	else{
		result = (thisResponse / tot) * 100;	
		res = roundNumber(result,2);
	}
		
	dwr.util.setValue("percentDiv"+temp[1], res+"%", { escapeHtml:false });
	
	
	var w = document.getElementById('bar1');

	//alert("this reposne="+temp[0] + "\tbar"+temp[1]);
	
	var bar = dwr.util.byId('bar'+temp[1]); //####################################################
	//var bar =  document.getElementById('bar'+temp[1]);
	bar.style.width = res+"%";								 
	var respDiv = dwr.util.byId('responseDiv');
	//var respDiv = document.getElementById('responseDiv');
	//respDiv.style.height = "250px"; 
	respDiv.style.height = "180px";
	
}

//########################################################

function cleanup() {
	if(isActive){	
	}
}

// Note: Not currently used - but it is possible for the server to pass a parameter 
// back to this method from, for example onsharedupdate().
// was this "messages" parameter causing a problem?
function handleSharedUpdate(messages){
	if (isDebug) alert("handle sharedupdate");
	}

function handleLocked(){
if (isDebug) alert("handle Locked");
	isActive = false;		
}

function handleUnlocked(){	
if (isDebug) alert("start handle Unlocked");
	isActive = true;
	if(thisUserUnlocked){
		thisUserUnlocked = false;		
	}	
    if (isDebug) alert("end handle Unlocked"); 
}

function findObj(n, d) { //v4.0
	var p,i,x; if(!d) d=document;
	if((p=n.indexOf("?"))>0&&parent.frames.length) {
		d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);
	}
	if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++)
		x=d.forms[i][n];
		for(i=0;!x&&d.layers&&i<d.layers.length;i++)
			x=findObj(n,d.layers[i].document);
		if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function checkradioform(){	
	// Loop from zero to the one minus the number of radio button selections
	for (counter = 0; counter < document.responseform.answer.length; counter++){
	// If a radio button has been selected it will return true
	// (If not it will return false)
		if (document.responseform.answer[counter].checked){
			return document.responseform.answer[counter].value;
		}
	}	
	return -1;
}

function roundNumber(num, dec) {
	var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
	return result;
}

function genHex(){
	colors = new Array(14)
	colors[0]="0"
	colors[1]="1"
	colors[2]="2"
	colors[3]="3"
	colors[4]="4"
	colors[5]="5"
	colors[5]="6"
	colors[6]="7"
	colors[7]="8"
	colors[8]="9"
	colors[9]="a"
	colors[10]="b"
	colors[11]="c"
	colors[12]="d"
	colors[13]="e"
	colors[14]="f"

	digit = new Array(5)
	color=""
	for (i=0;i<6;i++){
		digit[i]=colors[Math.round(Math.random()*14)]
		color = color+digit[i]
	}
	return color;
}

// handleUpdate is our local implementation of onSharedUpdate
widget.onSharedUpdate = handleSharedUpdate;
widget.onLocked = handleLocked;
widget.onUnlocked = handleUnlocked;
onunload = cleanup;
