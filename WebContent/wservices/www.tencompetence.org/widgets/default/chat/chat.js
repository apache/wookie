/*
****************   DEFAULT CHAT WIDGET **************
The presence list is appended to the shareddata by using tags
Each online member is appended to the list by using the following syntax...

<member>Fred Bloggs<member>

The chat log is done the same way...

<chat>Hi there!<chat>
******************************************************
*/
var currentLanguage = "en";
var supportedLanguages = new Array("bu","en","fr","nl");

var instanceid_key;
var proxyUrl;
var widgetAPIUrl;
var isActive=false;
var username="";
var chatSeparator = "<chat>";
var memberSeparator = "<member>";
var userList="";
var thisUserUnlocked = false;
var thisUserClearedLog = false;
var isAdmin = false;
var sharedDataKey = null;

var isDebug = false;
	
	
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
	 	dwr.util.setValue("memberslegend", getLocalizedString('online users'), { escapeHtml:false });
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
	widget.preferenceForKey(instanceid_key, "sharedDataKey", initSharedKey);
}

function initSharedKey(sharedKey){
	sharedDataKey = sharedKey;
	widget.sharedDataForKey(instanceid_key, "isLocked", setupInput);
}

function setupInput(isLockedValue){
	if(isLockedValue!="true"){		
		handleUnlocked(sharedDataKey); 
	}
	else{
		handleLocked(sharedDataKey);
	}		
}


function isAdminUser(){	
	return isAdmin;
}	
		
function getAdminUnlockText(){
	var adminUnlockText = "<a href=\"#\" onclick=\"unlockchat()\"><img border=\"0\" src=\"/wookie/shared/images/unlock.gif\" alt=\"" + getLocalizedString('Unlock this widget') + "\"></a>";
	return adminUnlockText;
}

function getAdminLockText(){
	var adminLockText = "<a href=\"#\" onclick=\"lockchat()\"><img border=\"0\" src=\"/wookie/shared/images/lock.gif\" alt=\"" + getLocalizedString('Lock this widget') + "\"></a>";	
	adminLockText += "<a href=\"#\" onclick=\"confirmClearChat()\"><img border=\"0\" src=\"/wookie/shared/images/trash.gif\" alt=\"";
	adminLockText += getLocalizedString('Clear chat log');
	adminLockText +=  "\"></a>";
	return adminLockText;
}

function getInputInactiveDiv() {
	var inputinactivediv = "<b>" + getLocalizedString('This widget has been locked') + "</b>&nbsp;";
	if(isAdminUser()){
		inputinactivediv += getAdminUnlockText();
	}
	return inputinactivediv;
}

function getInputActiveDiv(){
	var inputactivediv = getLangOpts() + "<input id=\"text\" size=\"30\" onkeypress=\"dwr.util.onReturn(event, sendMessage)\"/>";
	inputactivediv += "<input type=\"button\" value=\"" + getLocalizedString('Send') + "\" onclick=\"sendMessage()\"/>&nbsp;";
	if(isAdminUser()){
		inputactivediv += getAdminLockText();
	}
	return inputactivediv;
}

function doLanguageUpdate(la){
	currentLanguage = la;
	isActive = false;	
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

function confirmClearChat(){
	var confirmText = getLocalizedString('Are you sure you want to clear the chat log?');
	confirmText+="\n\n";
	confirmText+=getLocalizedString('By choosing OK, you will be removing all of the current chat log data'); 
	confirmText+=getLocalizedString('This operation is irreversible once you click OK');

	var answer = confirm(confirmText);
	if (answer){
		thisUserClearedLog = true;
		widget.setSharedDataForKey(instanceid_key, "defaultChatLog", "");
	}
	else{}
}

// initialise the presence & log
function doInitPresence(presenceList){
if (isDebug) debug("start doInitPresence");
	var currentUser = memberSeparator + username + memberSeparator;		
	if(presenceList==null||presenceList.indexOf(currentUser)==-1){	
		// add this user to chat
		widget.appendSharedDataForKey(instanceid_key, "defaultChatPresence", memberSeparator + username + memberSeparator);
		widget.appendSharedDataForKey(instanceid_key, "defaultChatLog", chatSeparator + "<" + username + " " + getLocalizedString('has joined the chatroom') + ">" + chatSeparator);    		
	}
	else{
		// get existing chat data
		widget.sharedDataForKey(instanceid_key, "defaultChatPresence", refreshMemberList);
		widget.sharedDataForKey(instanceid_key, "defaultChatLog", refreshchatlog);			
	}	
}



function cleanup() {
	if(isActive){	
		if (isDebug) debug("<function cleanup> start");
		isActive = false;
		var currentUser = memberSeparator + username + memberSeparator;		
		var removed = userList.replace(currentUser,"");
		widget.setSharedDataForKey(instanceid_key, "defaultChatPresence",removed);	
		widget.appendSharedDataForKey(instanceid_key, "defaultChatLog", chatSeparator + "<" + username + " " + getLocalizedString('has left the chatroom') + ">" + chatSeparator);
		if(!BrowserDetect.isBrowser('Firefox')){
			alert("You have now logged out of this chat session");
		}
		if (isDebug) debug("<function cleanup> end");																							
	}
}


// send a new message
function sendMessage() {
if (isDebug) debug("<function start> sendMessage()");
	var text = dwr.util.getValue("text");
	if (isDebug) debug("<code>sendMessage(): text=" + text ); 	
 	dwr.util.setValue("text", "");
 	// append this message to the shared data on the server
 	widget.appendSharedDataForKey(instanceid_key, "defaultChatLog", chatSeparator + username + ": " + text + chatSeparator);
if (isDebug) debug("<function end> sendMessage()");     	
}

// update the chat log
function refreshchatlog(messages){
if (isDebug) debug("<function start> refreshchatlog(): param:messages=" + messages);
	var chatlog = "";
	var messageArray = messages.split(chatSeparator);
	for (var data in messageArray) {
		// put each message in a new div
    	chatlog = "<div>" + dwr.util.escapeHtml(messageArray[data]) + "</div>" + chatlog;
    }
    // now set the chatlog to the new set of divs
    dwr.util.setValue("chatlog", chatlog, { escapeHtml:false });
if (isDebug) debug("<function end> refreshchatlog()");    
}

// update the presence list
function refreshMemberList(members){
if (isDebug) debug("<function start> refreshMemberList(): param:memebers=" + members);
	userList = members;
	var memberList = "";
	var memberArray = members.split(memberSeparator);
	for (var data in memberArray) {			
		if(memberArray[data]==username){	
			// this users entry		
		   	memberList = "<div><i>" + dwr.util.escapeHtml(memberArray[data]) + "</i></div>" + memberList;
		}
		else{
			// otherwise just add it in italic
		  	memberList = "<div>" + dwr.util.escapeHtml(memberArray[data]) + "</div>" + memberList;
		}
    }
    // now set the presence list
    dwr.util.setValue("members", memberList, { escapeHtml:false });
if (isDebug) debug("<function end> refreshMemberList()");    
}

function handleSharedUpdate(sdkey){
if (isDebug) debug("<function start> handleSharedUpdate(): param:sdkey=" + sdkey);
 // only respond to other shared instances - ignore others
 if(sdkey == sharedDataKey){

	if (isDebug) debug("<code>handleSharedUpdate(): sharedupdate call is for this widget");
	if(thisUserClearedLog == true){
		if (isDebug) debug("<code>handleSharedUpdate(): this user cleared the chatlog");
		thisUserClearedLog = false;
		widget.appendSharedDataForKey(instanceid_key, "defaultChatLog", chatSeparator + "<" + username + " " + getLocalizedString('has cleared the chat log') + ">" + chatSeparator);																					
	}	
	// update the chat presence list
	widget.sharedDataForKey(instanceid_key, "defaultChatPresence", refreshMemberList);
	// update the chat log
	widget.sharedDataForKey(instanceid_key, "defaultChatLog", refreshchatlog);
 }
 if (isDebug) debug("<function end> handleSharedUpdate()"); 
}




function handleLocked(sdkey){
if (isDebug) debug("<function start> handleLocked(): param:sdkey=" + sdkey);
 if(sdkey == sharedDataKey){ 
 	if (isDebug) debug("<code>handleLocked(): unlock call is for this widget");
	isActive = false;		
    dwr.util.setValue("joined", getInputInactiveDiv(), { escapeHtml:false });
    widget.sharedDataForKey(instanceid_key, "defaultChatLog", refreshchatlog);	    
 }
if (isDebug) debug("<function end> handleLocked()");  
}

function handleUnlocked(sdkey){	
if (isDebug) debug("<function start> handleUnlocked(): param:sdkey=" + sdkey);
 if(sdkey == sharedDataKey){ 
	if (isDebug) debug("<code> handleUnlocked(): unlock call is for this widget");
	isActive = true;
	if(thisUserUnlocked){		
		thisUserUnlocked = false;
		widget.appendSharedDataForKey(instanceid_key, "defaultChatLog", chatSeparator + "<" + username + " " + getLocalizedString('has unlocked the chatroom') + ">" + chatSeparator);																		
	}	
	// get the shared data for the presencelist
	widget.sharedDataForKey(instanceid_key, "defaultChatPresence", doInitPresence);	
    dwr.util.setValue("joined", getInputActiveDiv(), { escapeHtml:false });   
    if (isDebug) debug("<function end> handleUnlocked()"); 
 }
}

function lockchat(){
	if(isActive){	
		isActive = false;		
		//remove everybody because they wont be able update the shareddata once its locked
		widget.setSharedDataForKey(instanceid_key, "defaultChatPresence","");	
		widget.appendSharedDataForKey(instanceid_key, "defaultChatLog", chatSeparator + "<" + username + " " + getLocalizedString('has locked the chatroom') + ">" + chatSeparator);		
	}
	widget.lock(instanceid_key);
}

function unlockchat(){
	isActive = true;
	thisUserUnlocked = true;	
	widget.unlock(instanceid_key);	
}

// handleUpdate is our local implementation of onSharedUpdate
widget.onSharedUpdate = handleSharedUpdate;
widget.onLocked = handleLocked;
widget.onUnlocked = handleUnlocked;
// onunload tell the system that this page is no longer active. 
//(fires when browser closes or user navigates away from page)
onunload = cleanup;