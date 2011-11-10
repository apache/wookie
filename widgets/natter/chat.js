/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
var usercolor="";
var chatSeparator = "<chat>";
var dataSeparator = "<data>";
var memberSeparator = "<member>";
var thumbnail="";
var userList="";
var thisUserUnlocked = false;
var thisUserClearedLog = false;
var isAdmin = false;
var sharedDataKey = null;
var showMembers = false;
var localizedStrings = null;
var FIXME_THUMBNAIL_URI = "http://fixme.org/thumbnail";
var DEFAULT_THUMBNAIL_URI = "Images/default_thumbnail.png";

function rnd_no(max){
    return Math.floor(Math.random()*max);
};

function flip(){
    if (showMembers){
        showMembers = false;
        $("#members").hide();
        $("#chatlog").show();
        var objDiv = document.getElementById("chatlog");
        objDiv.scrollTop = objDiv.scrollHeight;
    } else {
        showMembers = true;
        $("#members").show();
        $("#chatlog").hide();
    }
}
	
// on start up set some values & init with the server
function init() {
    localize();
    wave.setStateCallback(refreshChatLog);
    wave.setParticipantCallback(refreshMemberList);
    Widget.onLocked = handleLocked;
    Widget.onUnlocked = handleUnlocked;
	if(!isActive){
		isActive = true;
        if (wave.getViewer() != null){
            username = wave.getViewer().getDisplayName();
            thumbnail  = wave.getViewer().getThumbnailUrl();
        }
        if (thumbnail == "" || thumbnail == null|| thumbnail == FIXME_THUMBNAIL_URI) thumbnail = "Images/default_thumbnail.png";
        if (username == null || username == ""){
            username = "natterer" + rnd_no(9999);        
        }
        if (Widget.preferences.getItem("conference-manager") == "true" || Widget.preferences.getItem("moderator") == "true"){
            isAdmin = true;
        }
        if (wave.getState().get("isLocked") == "true"){
            handleLocked();
        } else {
            handleUnlocked();
        }
 	}
}

////// Localization
function localize(){
    jQuery.getScript( "locales/"+Widget.locale+"/localizedStrings.js");
}

function getLocalizedString(str){
    if (localizedStrings == null) return str;
    var locstr = localizedStrings[str];
    if (locstr == null || locstr == "") locstr = str;
    return locstr;
}

///////

function isAdminUser(){	
	return isAdmin;
}	
		
function getAdminUnlockText(){
	var adminUnlockText = "<div id=\"lock\"><a href=\"#\" onclick=\"unlockchat()\"><img border=\"0\" src=\"Images/unlock.png\" alt=\""+getLocalizedString('Unlock this widget')+"\"></a></div>";
	return adminUnlockText;
}

function getAdminLockText(){
	var adminLockText = "<div id=\"lock\"><a href=\"#\" onclick=\"lockchat()\"><img border=\"0\" src=\"Images/lock.png\" alt=\""+getLocalizedString('Lock this widget')+"\"></a>";
	adminLockText += "<a href=\"#\" onclick=\"confirmClearChat()\"><img border=\"0\" src=\"Images/trash.png\" alt=\"";
	adminLockText += "Clear chat log";
	adminLockText +=  "\"></a></div>";
	return adminLockText;
}

function getInputInactiveDiv() {
	var inputinactivediv = "<b>"+getLocalizedString('This widget has been locked')+"</b>&nbsp;";
	if(isAdminUser()){
		inputinactivediv += getAdminUnlockText();
	}
	return inputinactivediv;
}

function getInputActiveDiv(){
	var inputactivediv = "<input id=\"text\" onkeypress=\"dwr.util.onReturn(event, sendMessage)\"/>";
	if(isAdminUser()){
		inputactivediv += getAdminLockText();
	}
	return inputactivediv;
}

function confirmClearChat(){
	var confirmText = getLocalizedString('Are you sure you want to clear the chat log?');
	confirmText+="\n\n";
	confirmText+=getLocalizedString('By choosing OK, you will be removing all of the current chat log data'); 
	confirmText+=getLocalizedString('This operation is irreversible once you click OK');
	var answer = confirm(confirmText);
	if (answer){
		thisUserClearedLog = true;
        var clear = {};
        clear["chatLog"] = chatSeparator+username+dataSeparator+" "+getLocalizedString("has cleared the chat log") + dataSeparator + thumbnail +chatSeparator;
        wave.getState().submitDelta(clear);
	}
	else{}
}

///// Send a new message

function sendMessage() {
    if (isActive){
        var text = dwr.util.getValue("text");
        dwr.util.setValue("text", "");
        post(username, text, thumbnail);
    }
}

function post(user,text,url){
    text = dwr.util.escapeHtml(text);
    text = replaceTextSmileys(text);
 	Widget.appendSharedDataForKey("chatLog",  chatSeparator + user + dataSeparator + text + dataSeparator + url + chatSeparator);
}


///// Presence List

function refreshMemberList(){
	var localthumbnail = DEFAULT_THUMBNAIL_URI;
	participants = wave.getParticipants();
    viewer = wave.getViewer();
	var memberList = "";
    var viewerId = "";
    if (viewer!=null) viewerId=viewer.getId();
	for (participant in participants) {	
		var thisUserIcon = participants[participant].getThumbnailUrl();
		if(thisUserIcon == FIXME_THUMBNAIL_URI || thisUserIcon == ""){
			thisUserIcon = localthumbnail;
		}		
		if(participants[participant].getId() == viewerId){	
			// this users entry		
		   	memberList = "<div><img height=\"32\" width=\"32\" style=\"vertical-align: text-top; float:left;padding:2px;\" src=\""+thisUserIcon+"\"/><i>" + dwr.util.escapeHtml(participants[participant].getDisplayName()) + "</i><br clear=\"both\"/></div>" + memberList;
		}
		else{
		  	memberList = "<div><img height=\"32\" width=\"32\" style=\"vertical-align: text-top; float:left;padding:2px;\" src=\""+thisUserIcon+"\"/>" + dwr.util.escapeHtml(participants[participant].getDisplayName()) + "<br clear=\"both\"/></div>" + memberList;
		}
    }
    // add current non-member viewer
    if (viewer == null){
		  	memberList = "<div><img height=\"32\" width=\"32\" style=\"vertical-align: text-top; float:left;padding:2px;\" src=\""+DEFAULT_THUMBNAIL_URI+"\"/>" + username + "<br clear=\"both\"/></div>" + memberList;        
    }
    // now set the presence list
    dwr.util.setValue("members", memberList, { escapeHtml:false });
}

///// Chat List

function refreshChatLog(){
    var messages = wave.getState().get("chatLog");
    if (messages && messages != null){
        var chatlog = "";
        var messageArray = messages.split(chatSeparator);
        messageArray.reverse();
        for (var data in messageArray) {
           message = messageArray[data].split(dataSeparator);
            // put each message in a new div
            if (message.length == 3) chatlog = "<div style=\"padding:2px\"> <img height=\"32\" width=\"32\" style=\"vertical-align: text-top; float:left;padding:2px;\" src=\""+message[2]+"\"><span style='font-weight:bold;color:blue;'>" + message[0] + "</span> " + message[1] + "<br clear=\"both\"/></div>" + chatlog;
        }
    }
    dwr.util.setValue("chatlog", chatlog, { escapeHtml:false });
    var objDiv = document.getElementById("chatlog");
    objDiv.scrollTop = objDiv.scrollHeight;
}

////// Locking

function handleLocked(){
	isActive = false;		
    dwr.util.setValue("joined", getInputInactiveDiv(), { escapeHtml:false });
    refreshChatLog();	    
}

function handleUnlocked(sdkey){	
	isActive = true;
	if(thisUserUnlocked){
		thisUserUnlocked = false;
		post(username,getLocalizedString('has unlocked the chatroom'), thumbnail);														
	}	
    dwr.util.setValue("joined", getInputActiveDiv(), { escapeHtml:false });   
}

function lockchat(){
	if(isActive){	
		isActive = false;		
		post(username,getLocalizedString('has locked the chatroom'), thumbnail);	
	}
	Widget.lock();
}

function unlockchat(){
	isActive = true;
	thisUserUnlocked = true;	
	Widget.unlock();	
}
