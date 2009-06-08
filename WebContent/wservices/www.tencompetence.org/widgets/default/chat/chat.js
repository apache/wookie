/*
****************   DEFAULT CHAT WIDGET **************
The presence list is appended to the shareddata by using tags
Each online member is appended to the list by using the following syntax...

<member>Fred Bloggs<member>

The chat log is done the same way...

<chat>Hi there!<chat>
******************************************************
*/

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
		isActive = true;
	 	WidgetUtil.setValue("memberslegend", LanguageHelper.getLocalizedString('online users'),  { escapeHtml:false });
		Widget.preferenceForKey("username", setLocalUsername);
 	}
}

// set the local username
function setLocalUsername(p){	
	if(username == ""){
		if(p == "null" ||  p == "No matching key found"){
			username = "User "+ WidgetUtil.generate3DigitRandomNumber();
		}
		else {
			username = p;
		}
	}
	Widget.preferenceForKey("conference-manager", getConferenceManagerRole);
}

function getConferenceManagerRole(p){
	if(p == "true") isAdmin = true;
	Widget.preferenceForKey("moderator", getModeratorRole);
}

function getModeratorRole(p){	
	if(p == "true") isAdmin = true;
	Widget.preferenceForKey("sharedDataKey", initSharedKey);
}

function initSharedKey(sharedKey){
	sharedDataKey = sharedKey;
	Widget.sharedDataForKey("isLocked", setupInput);
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
	var adminUnlockText = "<a href=\"#\" onclick=\"unlockchat()\"><img border=\"0\" src=\"/wookie/shared/images/unlock.gif\" alt=\"" + LanguageHelper.getLocalizedString('Unlock this Widget') + "\"></a>";
	return adminUnlockText;
}

function getAdminLockText(){
	var adminLockText = "<a href=\"#\" onclick=\"lockchat()\"><img border=\"0\" src=\"/wookie/shared/images/lock.gif\" alt=\"" + LanguageHelper.getLocalizedString('Lock this Widget') + "\"></a>";	
	adminLockText += "<a href=\"#\" onclick=\"confirmClearChat()\"><img border=\"0\" src=\"/wookie/shared/images/trash.gif\" alt=\"";
	adminLockText += LanguageHelper.getLocalizedString('Clear chat log');
	adminLockText +=  "\"></a>";
	return adminLockText;
}

function getInputInactiveDiv() {
	var inputinactivediv = "<b>" + LanguageHelper.getLocalizedString('This widget has been locked') + "</b>&nbsp;";
	if(isAdminUser()){
		inputinactivediv += getAdminUnlockText();
	}
	return inputinactivediv;
}

function getInputActiveDiv(){
	var inputactivediv = LanguageHelper.getLangOpts(updateLanguage) + "<input id=\"text\" size=\"30\" onkeypress=\"WidgetUtil.onReturn(event, sendMessage)\"/>";
	inputactivediv += "<input type=\"button\" value=\"" + LanguageHelper.getLocalizedString('Send') + "\" onclick=\"sendMessage()\"/>&nbsp;";
	if(isAdminUser()){
		inputactivediv += getAdminLockText();
	}
	return inputactivediv;
}

function updateLanguage(){
	WidgetUtil.setValue("memberslegend", LanguageHelper.getLocalizedString('online users'),  { escapeHtml:false });
	// check to see if we are locked or not first and call callback function
	Widget.sharedDataForKey("isLocked", updateLanguageLockedOrNot);
}

function updateLanguageLockedOrNot(isLockedValue){
	if(isLockedValue!="true"){	
		WidgetUtil.setValue("joined", getInputActiveDiv(),  { escapeHtml:false });
	}
	else{
		WidgetUtil.setValue("joined", getInputInactiveDiv(), { escapeHtml:false });
	}		
}

function confirmClearChat(){
	var confirmText = LanguageHelper.getLocalizedString('Are you sure you want to clear the chat log?');
	confirmText += "\n\n";
	confirmText += LanguageHelper.getLocalizedString('By choosing OK, you will be removing all of the current chat log data'); 
	confirmText += LanguageHelper.getLocalizedString('This operation is irreversible once you click OK');

	var answer = confirm(confirmText);
	if (answer){
		thisUserClearedLog = true;
		Widget.setSharedDataForKey("defaultChatLog", "");
	}	
}

// initialise the presence & log
function doInitPresence(presenceList){
if (isDebug) DebugHelper.debug("start doInitPresence");
	var currentUser = memberSeparator + username + memberSeparator;		
	if(presenceList==null||presenceList.indexOf(currentUser)==-1){	
		// add this user to chat
		Widget.appendSharedDataForKey("defaultChatPresence", memberSeparator + username + memberSeparator);
		Widget.appendSharedDataForKey("defaultChatLog", chatSeparator + "<" + username + " " + LanguageHelper.getLocalizedString('has joined the chatroom') + ">" + chatSeparator);    		
	}
	else{
		// get existing chat data
		Widget.sharedDataForKey("defaultChatPresence", refreshMemberList);
		Widget.sharedDataForKey("defaultChatLog", refreshchatlog);			
	}	
}

function cleanup() {
	if(isActive){	
		if (isDebug) DebugHelper.debug("<function cleanup> start");
		isActive = false;
		var currentUser = memberSeparator + username + memberSeparator;		
		var removed = userList.replace(currentUser,"");
		Widget.setSharedDataForKey("defaultChatPresence",removed);	
		Widget.appendSharedDataForKey("defaultChatLog", chatSeparator + "<" + username + " " + LanguageHelper.getLocalizedString('has left the chatroom') + ">" + chatSeparator);
		if(!BrowserDetect.isBrowser('Firefox')){
			alert(LanguageHelper.getLocalizedString('You have now logged out'));
		}
		if (isDebug) DebugHelper.debug("<function cleanup> end");																							
	}
}


// send a new message
function sendMessage() {
if (isDebug) DebugHelper.debug("<function start> sendMessage()");
	var text = WidgetUtil.getValue("text");
	if (isDebug) DebugHelper.debug("<code>sendMessage(): text=" + text ); 	
 	WidgetUtil.setValue("text", "");
 	// append this message to the shared data on the server
 	Widget.appendSharedDataForKey("defaultChatLog", chatSeparator + username + ": " + text + chatSeparator);
 	if (isDebug) DebugHelper.debug("<function end> sendMessage()");     	
}

// update the chat log
function refreshchatlog(messages){
if (isDebug) DebugHelper.debug("<function start> refreshchatlog(): param:messages=" + messages);
	var chatlog = "";
	var messageArray = messages.split(chatSeparator);
	for (var data in messageArray) {
		// put each message in a new div
    	chatlog = "<div>" + WidgetUtil.escapeHtml(messageArray[data]) + "</div>" + chatlog;
    }
    // now set the chatlog to the new set of divs
    WidgetUtil.setValue("chatlog", chatlog, { escapeHtml:false }); 
if (isDebug) DebugHelper.debug("<function end> refreshchatlog()");    
}

// update the presence list
function refreshMemberList(members){
if (isDebug) DebugHelper.debug("<function start> refreshMemberList(): param:memebers=" + members);
	userList = members;
	var memberList = "";
	var memberArray = members.split(memberSeparator);
	for (var data in memberArray) {			
		if(memberArray[data]==username){	
			// this users entry	- italic	
		   	memberList = "<div><i>" + WidgetUtil.escapeHtml(memberArray[data]) + "</i></div>" + memberList;
		}
		else{
			// otherwise just add it non italic
		  	memberList = "<div>" + WidgetUtil.escapeHtml(memberArray[data]) + "</div>" + memberList;
		}
    }
    // now set the presence list
	WidgetUtil.setValue("members", memberList, { escapeHtml:false });
	if (isDebug) DebugHelper.debug("<function end> refreshMemberList()");    
}

function handleSharedUpdate(sdkey){
if (isDebug) DebugHelper.debug("<function start> handleSharedUpdate(): param:sdkey=" + sdkey);
 // only respond to other shared instances - ignore others
 if(sdkey == sharedDataKey){

	if (isDebug) DebugHelper.debug("<code>handleSharedUpdate(): sharedupdate call is for this Widget");
	if(thisUserClearedLog == true){
		if (isDebug) DebugHelper.debug("<code>handleSharedUpdate(): this user cleared the chatlog");
		thisUserClearedLog = false;
		Widget.appendSharedDataForKey("defaultChatLog", chatSeparator + "<" + username + " " + LanguageHelper.getLocalizedString('has cleared the chat log') + ">" + chatSeparator);																					
	}	
	// update the chat presence list
	Widget.sharedDataForKey("defaultChatPresence", refreshMemberList);
	// update the chat log
	Widget.sharedDataForKey("defaultChatLog", refreshchatlog);
 }
 if (isDebug) DebugHelper.debug("<function end> handleSharedUpdate()"); 
}

function handleLocked(sdkey){
if (isDebug) DebugHelper.debug("<function start> handleLocked(): param:sdkey=" + sdkey);
 if(sdkey == sharedDataKey){ 
 	if (isDebug) DebugHelper.debug("<code>handleLocked(): unlock call is for this Widget");
	isActive = false;		
	WidgetUtil.setValue("joined", getInputInactiveDiv(), { escapeHtml:false });
    Widget.sharedDataForKey("defaultChatLog", refreshchatlog);	    
 }
if (isDebug) DebugHelper.debug("<function end> handleLocked()");  
}

function handleUnlocked(sdkey){	
if (isDebug) DebugHelper.debug("<function start> handleUnlocked(): param:sdkey=" + sdkey);
 if(sdkey == sharedDataKey){ 
	if (isDebug) DebugHelper.debug("<code> handleUnlocked(): unlock call is for this Widget");
	isActive = true;
	if(thisUserUnlocked){		
		thisUserUnlocked = false;
		Widget.appendSharedDataForKey("defaultChatLog", chatSeparator + "<" + username + " " + LanguageHelper.getLocalizedString('has unlocked the chatroom') + ">" + chatSeparator);																		
	}	
	// get the shared data for the presencelist
	Widget.sharedDataForKey("defaultChatPresence", doInitPresence);	
	WidgetUtil.setValue("joined", getInputActiveDiv(), { escapeHtml:false });   
    if (isDebug) DebugHelper.debug("<function end> handleUnlocked()"); 
 }
}

function lockchat(){
	if(isActive){	
		isActive = false;		
		//remove everybody because they wont be able update the shareddata once its locked
		Widget.setSharedDataForKey("defaultChatPresence","");	
		Widget.appendSharedDataForKey("defaultChatLog", chatSeparator + "<" + username + " " + LanguageHelper.getLocalizedString('has locked the chatroom') + ">" + chatSeparator);		
	}
	Widget.lock();
}

function unlockchat(){
	isActive = true;
	thisUserUnlocked = true;	
	Widget.unlock();	
}

// handleUpdate is our local implementation of onSharedUpdate
Widget.onSharedUpdate = handleSharedUpdate;
Widget.onLocked = handleLocked;
Widget.onUnlocked = handleUnlocked;
// onunload tell the system that this page is no longer active. 
//(fires when browser closes or user navigates away from page)
onunload = cleanup;