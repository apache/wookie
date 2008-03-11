/*
****************   DEFAULT CHAT WIDGET **************
The presence list is appended to the shareddata by using tags
Each online member is appended to the list by using the following syntax...

<member>Fred Bloggs<member>

The chat log is done the same way...

<chat>Hi there!<chat>
******************************************************
*/
var isDebug = false;
var instanceid_key;
var proxyUrl;
var widgetAPIUrl;
var isActive=false;
var username="";
var chatSeparator = "<chat>";
var memberSeparator = "<member>";
var userList="";
var thisUserUnlocked = false;
var isAdmin = false;

var inputactivediv = "<input id=\"text\" size=\"30\" onkeypress=\"dwr.util.onReturn(event, sendMessage)\"/>";
	inputactivediv += "<input type=\"button\" value=\"Send\" onclick=\"sendMessage()\"/>&nbsp;";	
var adminLockText = "<a href=\"#\" onclick=\"lockchat()\"><img border=\"0\" src=\"/wookie/shared/images/lock.gif\" alt=\"Lock this widget\"></a>";	
	adminLockText += "<a href=\"#\" onclick=\"confirmClearChat()\"><img border=\"0\" src=\"/wookie/shared/images/trash.gif\" alt=\"Clear chat log\"></a>";
		
var inputinactivediv = "<b>This widget has been locked</b>&nbsp;";
var adminUnlockText = "<a href=\"#\" onclick=\"unlockchat()\"><img border=\"0\" src=\"/wookie/shared/images/unlock.gif\" alt=\"Unlock this widget\"></a>";


function confirmClearChat(){
var answer = confirm("Are you sure you want to clear the chat log?\n\nBy choosing 'OK', you will be removing all of the current chat log data. This operation is irreversible once you click 'OK'");
	if (answer){
		widget.setSharedDataForKey(instanceid_key, "defaultChatLog", "");
	}
	else{}
}

// initialise the presence & log
function doInitPresence(presenceList){
if (isDebug) alert("start doInitPresence");
	var currentUser = memberSeparator + username + memberSeparator;		
	if(presenceList==null||presenceList.indexOf(currentUser)==-1){	
		// add this user to chat
		widget.appendSharedDataForKey(instanceid_key, "defaultChatPresence", memberSeparator + username + memberSeparator);
		widget.appendSharedDataForKey(instanceid_key, "defaultChatLog", chatSeparator + "<User " + username + " has joined the chatroom>" + chatSeparator);    	
	if (isDebug) alert("doInitPresence 1:" +instanceid_key +" : "+username);
	
	}
	else{
		// get existing chat data
		widget.sharedDataForKey(instanceid_key, "defaultChatPresence", refreshMemberList);
		widget.sharedDataForKey(instanceid_key, "defaultChatLog", refreshchatlog);	
	if (isDebug) alert("doInitPresence 2");
	}
if (isDebug) alert("end doInitPresence");	
}

// set the local username
function setLocalUsername(p){
	username = p;	 
	if(isAdminUser()){
		inputactivediv+=adminLockText;
		inputinactivediv+=adminUnlockText;
	}
	widget.sharedDataForKey(instanceid_key, "isLocked", setupInput);
}

function isAdminUser(){
	if (username.indexOf("staff")!=-1){
		return true;
	}
	else if(username.indexOf("teacher")!=-1){
		return true;
	}
	return false;
}

function setupInput(isLockedValue){
	if(isLockedValue!="true"){		
		handleUnlocked(); 
	}
	else{
		handleLocked();
	}
		
}

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
					//alert("idkey="+instanceid_key);
				}
				if(argname=="proxy"){
					proxyUrl = pairs[i].substring(pos+1);
					//alert("proxy="+proxyUrl);
				}
				if(argname=="serviceapi"){
					widgetAPIUrl = pairs[i].substring(pos+1);
					//alert("serviceapi="+widgetAPIUrl);
				}				
			}
		}	
		isActive = true;
		// this line tells DWR to use call backs (i.e. will call onsharedupdate() when an event is recevied for shared data
	 	dwr.engine.setActiveReverseAjax(true);
	 	widget.preferenceForKey(instanceid_key, "LDUsername", setLocalUsername);	 	
 	}
}

function cleanup() {
	if(isActive){	
		isActive = false;
		var currentUser = memberSeparator + username + memberSeparator;		
		var removed = userList.replace(currentUser,"");
		widget.setSharedDataForKey(instanceid_key, "defaultChatPresence",removed);	
		widget.appendSharedDataForKey(instanceid_key, "defaultChatLog", chatSeparator + "<User " + username + " has left the chatroom>" + chatSeparator);		
	}
}



// send a new message
function sendMessage() {
 	var text = dwr.util.getValue("text");
 	dwr.util.setValue("text", "");
 	// append this message to the shared data on the server
 	widget.appendSharedDataForKey(instanceid_key, "defaultChatLog", chatSeparator + username + ": " + text + chatSeparator);
}

// update the chat log
function refreshchatlog(messages){
	var chatlog = "";
	var messageArray = messages.split(chatSeparator);
	for (var data in messageArray) {
		// put each message in a new div
    	chatlog = "<div>" + dwr.util.escapeHtml(messageArray[data]) + "</div>" + chatlog;
    }
    // now set the chatlog to the new set of divs
    dwr.util.setValue("chatlog", chatlog, { escapeHtml:false });
}

// update the presence list
function refreshMemberList(members){
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
}

// Note: Not currently used - but it is possible for the server to pass a parameter 
// back to this method from, for example onsharedupdate().
// was this "messages" parameter causing a problem?
function handleSharedUpdate(messages){
if (isDebug) alert("handle sharedupdate");
	// update the chat presence list
	widget.sharedDataForKey(instanceid_key, "defaultChatPresence", refreshMemberList);
	// update the chat log
	widget.sharedDataForKey(instanceid_key, "defaultChatLog", refreshchatlog);
}

function handleLocked(){
if (isDebug) alert("handle Locked");
	isActive = false;		
    dwr.util.setValue("joined", inputinactivediv, { escapeHtml:false });
    widget.sharedDataForKey(instanceid_key, "defaultChatLog", refreshchatlog);	    
   // dwr.engine.setActiveReverseAjax(false);
}

function handleUnlocked(){	
if (isDebug) alert("start handle Unlocked");
	isActive = true;
	if(thisUserUnlocked){
		thisUserUnlocked = false;
		widget.appendSharedDataForKey(instanceid_key, "defaultChatLog", chatSeparator + "<User " + username + " has UNLOCKED the chatroom>" + chatSeparator);
	}	
	// get the shared data for the presencelist
	widget.sharedDataForKey(instanceid_key, "defaultChatPresence", doInitPresence);	
    dwr.util.setValue("joined", inputactivediv, { escapeHtml:false });   
   // dwr.engine.setActiveReverseAjax(true);
    if (isDebug) alert("end handle Unlocked"); 
}

function lockchat(){
	if(isActive){	
		isActive = false;		
		//remove everybody becuase they wont be able update the shareddate once its locked
		widget.setSharedDataForKey(instanceid_key, "defaultChatPresence","");	
		widget.appendSharedDataForKey(instanceid_key, "defaultChatLog", chatSeparator + "<User " + username + " has LOCKED the chatroom>" + chatSeparator);		
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
