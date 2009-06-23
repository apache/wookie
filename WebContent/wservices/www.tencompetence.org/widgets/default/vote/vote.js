/*
****************   DEFAULT VOTE WIDGET **************

******************************************************
*/
var isDebug = false;
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
		isActive = true;
	 	Widget.preferenceForKey("username", setLocalUsername);	 	
 	}
}

// set the local username
function setLocalUsername(p){
	username = p;	
	Widget.preferenceForKey("conference-manager", getConferenceManagerRole);
}

function getConferenceManagerRole(p){
	if(p == "true") isAdmin = true;
	Widget.preferenceForKey("moderator", getModeratorRole);
}

function getModeratorRole(p){	
	if(p == "true") isAdmin = true;
	Widget.sharedDataForKey("isSet", doSetup);
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
		Widget.preferenceForKey("hasVoted", showOrVoteScreen);
	
	}
} 

function isAdminUser(){
	return isAdmin;
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
		responseArr[k-1] =  WidgetUtil.findObj("response"+k).value;
	}
	responseCount++
	WidgetUtil.findObj("dynForm").innerHTML += LanguageHelper.getLocalizedString('Response')+ " "+responseCount+"<input type='text'  size='50' maxlength='100' id='response" + responseCount + "'><br>";
	for(k=1;k<responseCount;k++){
		WidgetUtil.findObj("response"+k).value = responseArr[k-1];
	}
}

function doValidation(){
	var validResponseCount = 0;
	var responseXML = "";
	var responses ="";
			
	var q = WidgetUtil.findObj("questionfield").value;
	if (q.length < 1){
		alert(LanguageHelper.getLocalizedString('Step one: You have not set a question'));
		return;
	}

	for(var j=1;j<=responseCount;j++){
		rText =  WidgetUtil.findObj("response"+j).value;
		if(rText != ""){
			responses += LanguageHelper.getLocalizedString('Response') + " "+ j + ":  " + rText;
			responseXML+= answerSeparator + rText;
			// here we set all the number of votes of these to be zero			
			validResponseCount++;
		}
		else{
			responses += LanguageHelper.getLocalizedString('Response') + " "+ j + ":  " + "<" + LanguageHelper.getLocalizedString('Response is empty so will not be added') + ">";

		}
		responses += "\n";
	}

	if(validResponseCount < 2){
			alert(LanguageHelper.getLocalizedString('Step two: You need at least two responses'));
			return;
	}

	var confStr = LanguageHelper.getLocalizedString('Are you sure you have finished and want to set the vote?');
	confStr += "\n\n" + LanguageHelper.getLocalizedString('Choose OK to finish and make the vote available');
	confStr += "\n" + LanguageHelper.getLocalizedString('Choose Cancel to carry on editing') + "\n";
	
	var answer =  confirm(confStr + LanguageHelper.getLocalizedString('Question') + ": " + q + "\n" +responses);

	if (answer){
		Widget.setSharedDataForKey("question", q);
		Widget.setSharedDataForKey("answers", responseXML);
		Widget.setSharedDataForKey("isSet", "true");
		// make a db entry for each of these options/responses, set to zero
		for(var j=1;j<=validResponseCount;j++){
			Widget.setSharedDataForKey("response"+j, "0");
		}
		Widget.setSharedDataForKey("totalvotes", "0");
		//now redirect to see the results page	
		showVoteDisplayStageOne();		
	}
	else{}

}

function showNotReadyYet(){
	var notReadytext = "<div id='legendDiv'>" + LanguageHelper.getLocalizedString('This vote instance has not yet been initialised') + "</div>";
	notReadytext += "<div id='responseDivFull'></div>";
	notReadytext += "<div id='submitDiv'><div align=\"left\" id=\"lang_opts\">" + LanguageHelper.getLangOpts(showNotReadyYetLangChanged) + "</div></div>";
	dwr.util.setValue("maincanvas", notReadytext, { escapeHtml:false });
}

function showNotReadyYetLangChanged(){
	showNotReadyYet();
}

function createQuestions(){
	var authorVoteText = "<div id='legendDiv'>" + LanguageHelper.getLocalizedString('Setup vote widget') + "</div>";
	authorVoteText += "<div id='questionDiv'>" + LanguageHelper.getLocalizedString('Step One: Enter the question here') + ":&nbsp;<input type='text' size='50' maxlength='100' id='questionfield'></div>";
	authorVoteText += "<div id='responseDiv'>" + LanguageHelper.getLocalizedString('Step Two: Create the responses') + ":&nbsp;(<a href='#' onClick='createNewInput()'>" + LanguageHelper.getLocalizedString('Click here to add more response fields') + "</a>)<div id='dynForm'></div></div>";
	authorVoteText += "<div id='submitDiv'><div align=\"left\" id=\"lang_opts\">" + LanguageHelper.getLangOpts(createQuestionsLangChanged) + "&nbsp;</div><a href='#' onClick='doValidation()'>" + LanguageHelper.getLocalizedString('Step Three: Click here to check and finish') + "</a></div>";
	dwr.util.setValue("maincanvas", authorVoteText, { escapeHtml:false });
}

function createQuestionsLangChanged(){
	responseCount = 0; //reset responses
	createQuestions();
}

function setupVoteDisplayStageOne(){
	Widget.sharedDataForKey("question", setupVoteDisplayStageTwo);
}

function setupVoteDisplayStageTwo(pQuestion){
	question = pQuestion;
	Widget.sharedDataForKey("answers", setupVoteDisplayStageThree);
}

function setupVoteDisplayStageThree(pAnswers){
	answers = pAnswers;	
	var questionText = "<div id='questionDiv'><img  height='32' width='32' border='0' src='/wookie/shared/images/vote.png'>"+question+"</div>";
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
    var submitText= questionText+ answerText + "<div id='submitDiv'><div align=\"left\" id=\"lang_opts\">" + LanguageHelper.getLangOpts(setupVoteDisplayStageOne) + "&nbsp;</div><a href='#' onClick='doVote()'>" + LanguageHelper.getLocalizedString('Click here to vote') + "</a></div>";		
	dwr.util.setValue("maincanvas", submitText, { escapeHtml:false });
	var respDiv = dwr.util.byId('responseDiv');
	respDiv.style.height = "200px"; 
}

function doVote(){	
	var respValue = checkradioform();
	//check that the user has chosen a response
	if(respValue == -1){
		alert(LanguageHelper.getLocalizedString('Please select a response'));
		return;
	}
	else{
		// stop user clicking again
		var submitPressedText = LanguageHelper.getLocalizedString('Click here to vote');
		dwr.util.setValue("submitDiv", submitPressedText, { escapeHtml:false });
		// update the response count
		lastVote = respValue;
		Widget.sharedDataForKey("response"+ respValue, doVoteUpdate);		
		Widget.sharedDataForKey("totalvotes", doTotalVoteUpdate);
		// set in prefs that user has now voted
		Widget.setPreferenceForKey("hasVoted", "true");
		// now show the vote	
		showVoteDisplayStageOne();		
	}
}

function doVoteUpdate(respCountVal){
	var actualIntCount = parseInt(respCountVal);   
	actualIntCount++;
	Widget.setSharedDataForKey("response"+ lastVote, actualIntCount);
}

function doTotalVoteUpdate(totalCountVal){
	var totalIntCount = parseInt(totalCountVal);   
	totalIntCount++;
	Widget.setSharedDataForKey("totalvotes", totalIntCount);
}

//######show the vote ########

function showVoteDisplayStageOne(){
	Widget.sharedDataForKey("question", showVoteDisplayStageTwo);
}

function showVoteDisplayStageTwo(pQuestion){
	question = pQuestion;
	Widget.sharedDataForKey("answers", showVoteDisplayStageThree);
}

function newClosure(someNum, someRef) {
	return function(x) {    	
		getResults(x, someNum);
    }     
}

function showVoteDisplayStageThree(pAnswers){
	answers = pAnswers;
	var submitDiv = null;
	var questionText = "<div id='questionDiv'><img  height='32' width='32' border='0' src='/wookie/shared/images/vote.png'>"+question+"</div>";
	var answerText = "<div id='responseDiv'><form name='responseform'><table border='0'>";	
	var answerArray = pAnswers.split(answerSeparator);	
	var count=0;
	for (var data in answerArray) {		
		// put each answer in a new div
		if(answerArray[data].length>0){
			count++;			
    		answerText += "<tr><td width='290px'><div id='tt"+count+"'>&nbsp;" + dwr.util.escapeHtml(answerArray[data]) + "</div></td>";
    		answerText += "<td width='100px'><div id='bar"+count+"' style=\"width: 0px; background-color:#"+WidgetUtil.generateRandomHexColor()+";\">&nbsp;</div></td>";
    		answerText += "<td width='40px'><div id='percentDiv"+count+"'></div></td>";
    		answerText += "<td width='70px'><div id='sresponse"+count+"'></div></td></tr>";
    	}
    }
    answerText += "</table></form></div>";  
    submitDiv = "<div id='submitDiv'><div align=\"left\" id=\"lang_opts\">" + LanguageHelper.getLangOpts(showVoteDisplayStageOne) + "&nbsp;</div><a href='#' onClick='showVoteDisplayStageOne()'><img border=\"0\" src=\"/wookie/shared/images/refresh.gif\">&nbsp;" + LanguageHelper.getLocalizedString('Refresh') + "</a></div>";
      
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
		Widget.sharedDataForKey("response"+k, closure);		// hand this instance as a callback to be executed once the shareddataforkey has completed
	}
}


function getResults(server, index){
	var textResults = "<div>&nbsp;("+server+" " + LanguageHelper.getLocalizedString('votes') + ")</div>";
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
	Widget.sharedDataForKey("totalvotes", mcallMetaData);
}

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
		res = WidgetUtil.roundNumber(result,2);
	}		
	dwr.util.setValue("percentDiv"+temp[1], res+"%", { escapeHtml:false });		
	//var w = document.getElementById('bar1');	
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

function handleSharedUpdate(messages){
}

function handleLocked(){
	isActive = false;		
}

function handleUnlocked(){	
	isActive = true;
	if(thisUserUnlocked){
		thisUserUnlocked = false;		
	}	 
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

// handleUpdate is our local implementation of onSharedUpdate
Widget.onSharedUpdate = handleSharedUpdate;
Widget.onLocked = handleLocked;
Widget.onUnlocked = handleUnlocked;
onunload = cleanup;
