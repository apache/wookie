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
var isDebug = false;
var instanceid_key;
var proxyUrl;
var WidgetAPIUrl;
var isActive=false;
var username="";
var usercolor="";
var chatSeparator = "<chat>";
var dataSeparator = "<data>";
var memberSeparator = "<member>";
var userList="";
var thisUserUnlocked = false;
var thisUserClearedLog = false;
var isAdmin = false;
var sharedDataKey = null;
var showMembers = false;

var colours=new Array("#FF0000","#66FF00","#333399","#FF9900","#990099","#A15867","#FA265D","#4491A6","#61AA0C","#463462","#0E24D3","#1DA881","#588636","#A62462","#F01B0E","#6982B1","#8CA64B","#AE237F","#F5A00E","#270D7C","#456D1D","#061095","#9A2CA0","#51A0DC","#611E08","#130992","#493895","#5E1160","#6633FF","#0072C7","#1E919F","#E35A08","#F853C0","#430D80","#D3124A","#E78821","#CCB11F","#3086A0","#43C515","#42992A","#663300","#666633","#009999","#E46B21","#580B78");

function getRandomColour(){
    return colours[rnd_no(colours.length)];
};

function rnd_no(max){
    return Math.floor(Math.random()*max);
};

function flip(){
    if (showMembers){
        showMembers = false;
        hidediv("members");
        showdiv("chatlog");
        var objDiv = document.getElementById("chatlog");
        objDiv.scrollTop = objDiv.scrollHeight;
    } else {
        showMembers = true;
        hidediv("chatlog");
        showdiv("members");
    }
}

function showBack(event)
{
	hidediv("front");
	showdiv("back");
}

function showFront(event)
{
	hidediv("back");
	showdiv("front");	
}


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
	
	
// on start up set some values & init with the server
function init() {
    CreateInfoButton('infobutton', { foregroundStyle: 'white', backgroundStyle: 'black', onclick: 'showBack', frontID: 'front' });
    setLangOpts();
	if(!isActive){
		isActive = true;
		Widget.preferenceForKey("LDUsercolor", setLocalUsercolor);			 
 	}
}

// set the local username
function setLocalUsername(p){	
	if (!p || p == "null" ||  p == "No matching key found"){
	   username = "natterer" + rnd_no(9999);
	   Widget.setPreferenceForKey("username", username, null);
	} else {
		username = p;	 
	}
	Widget.preferenceForKey("conference-manager", getConferenceManagerRole);
}

// set the local colour
function setLocalUsercolor(p){
    if (!p || p == "null"){
         usercolor = getRandomColour();
         Widget.setPreferenceForKey("LDUsercolor", usercolor, null);
    } else {
        usercolor = p;    
    }
    Widget.preferenceForKey("username", setLocalUsername);
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
	var adminUnlockText = "<a href=\"#\" onclick=\"unlockchat()\"><img border=\"0\" src=\"/wookie/shared/images/unlock.gif\" alt=\"" + getLocalizedString('Unlock this Widget') + "\"></a>";
	return adminUnlockText;
}

function getAdminLockText(){
	var adminLockText = "<div id=\"lock\"><a href=\"#\" onclick=\"lockchat()\"><img border=\"0\" src=\"Images/lock.png\" alt=\"" + getLocalizedString('Lock this Widget') + "\"></a>";	
	adminLockText += "<a href=\"#\" onclick=\"confirmClearChat()\"><img border=\"0\" src=\"Images/trash.png\" alt=\"";
	adminLockText += getLocalizedString('Clear chat log');
	adminLockText +=  "\"></a></div>";
	return adminLockText;
}

function getInputInactiveDiv() {
	var inputinactivediv = "<b>" + getLocalizedString('This Widget has been locked') + "</b>&nbsp;";
	if(isAdminUser()){
		inputinactivediv += getAdminUnlockText();
	}
	return inputinactivediv;
}

function getInputActiveDiv(){
	var inputactivediv = "<input id=\"text\" onkeypress=\"dwr.util.onReturn(event, sendMessage)\"/>";
	
	//var inputactivediv = getLangOpts() + "<input id=\"text\" size=\"30\" onkeypress=\"dwr.util.onReturn(event, sendMessage)\"/>";
	//inputactivediv += "<input type=\"button\" value=\"" + getLocalizedString('Send') + "\" onclick=\"sendMessage()\"/>&nbsp;";
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

function setLangOpts(){
    dwr.util.setValue("langopts", getLangOpts(), { escapeHtml:false });
}

function getLangOpts(){
	var langOptionStr = "Select language: <select name=\"select_lang\" id=\"select_lang\" onchange=\"doLanguageUpdate(this.options[this.selectedIndex].value);\">" ;
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
		Widget.setSharedDataForKey("defaultChatLog", "");
	}
	else{}
}

// initialise the presence & log
function doInitPresence(presenceList){
if (isDebug) alert("start doInitPresence");
	var currentUser = memberSeparator + username + dataSeparator + usercolor + memberSeparator;		
	if(presenceList==null||presenceList.indexOf(currentUser)==-1){	
		// add this user to chat
		Widget.appendSharedDataForKey("defaultChatPresence", memberSeparator + username +  dataSeparator + usercolor + memberSeparator);
		post(username, "joined the chat", "gray");  		
	}
	else{
		// get existing chat data
		Widget.sharedDataForKey("defaultChatPresence", refreshMemberList);
		Widget.sharedDataForKey("defaultChatLog", refreshchatlog);			
	}	
}

function cleanup() {
	if(isActive){	
		isActive = false;
		var currentUser = memberSeparator + username + memberSeparator;		
		var removed = userList.replace(currentUser,"");
		Widget.setSharedDataForKey("defaultChatPresence",removed);	
		post(username, "left the chat", "gray");																					
	}
}

// send a new message
function sendMessage() {
 	var text = dwr.util.getValue("text");
 	dwr.util.setValue("text", "");
    // append this message to the shared data on the server
 	post(username,text,usercolor);
}

function post(user,text,color){
    text = dwr.util.escapeHtml(text);
    text = replaceTextSmileys(text);
 	Widget.appendSharedDataForKey("defaultChatLog",  chatSeparator + user + dataSeparator + text + dataSeparator + color + chatSeparator);
}

// update the chat log
function refreshchatlog(messages){
	var chatlog = "";
	var messageArray = messages.split(chatSeparator);
	messageArray.reverse();
	for (var data in messageArray) {
	   message = messageArray[data].split(dataSeparator);
		// put each message in a new div
		if (message.length == 3) chatlog = "<div> <span style='font-weight:bold;color:"+message[2]+"'>" + message[0] + "</span>: " + message[1] + "</div>" + chatlog;
    }
    // now set the chatlog to the new set of divs
    dwr.util.setValue("chatlog", chatlog, { escapeHtml:false });
    var objDiv = document.getElementById("chatlog");
    objDiv.scrollTop = objDiv.scrollHeight;
}

// update the presence list
function refreshMemberList(members){
	userList = members;
	var memberList = "";
	var memberArray = members.split(memberSeparator);
	for (var data in memberArray) {	
	    var member = memberArray[data].split(dataSeparator);		
		if(member[0]==username){	
			// this users entry		
		   	memberList = "<div style='color:"+member[1]+"'><i>" + dwr.util.escapeHtml(member[0]) + "</i></div>" + memberList;
		}
		else{
			// otherwise just add it in italic
		  	memberList = "<div style='color:"+member[1]+"'>" + dwr.util.escapeHtml(member[0]) + "</div>" + memberList;
		}
    }
    // now set the presence list
    dwr.util.setValue("members", memberList, { escapeHtml:false });
}

function handleSharedUpdate(sdkey){
if (isDebug) alert("handlesharedUpdate" + sdkey + "    :      " + sharedDataKey );
 // only respond to other shared instances - ignore others
 if(sdkey == sharedDataKey){

	if (isDebug) alert("handle sharedupdate");
	if(thisUserClearedLog == true){
		thisUserClearedLog = false;
		post(username, getLocalizedString('has cleared the chat log'), "gray");																			
	}	
	// update the chat presence list
	Widget.sharedDataForKey("defaultChatPresence", refreshMemberList);
	// update the chat log
	Widget.sharedDataForKey("defaultChatLog", refreshchatlog);
 }
}

function handleLocked(sdkey){
if (isDebug) alert("handleLocked" + sdkey + "    :      " + sharedDataKey );
 if(sdkey == sharedDataKey){ 
 	if (isDebug) alert("handle Locked");
	isActive = false;		
    dwr.util.setValue("joined", getInputInactiveDiv(), { escapeHtml:false });
    Widget.sharedDataForKey("defaultChatLog", refreshchatlog);	    
 }
}

function handleUnlocked(sdkey){	
if (isDebug) alert("handleUnlocked" + sdkey + "    :      " + sharedDataKey );
 if(sdkey == sharedDataKey){ 
	if (isDebug) alert("start handle Unlocked");
	isActive = true;
	if(thisUserUnlocked){
		thisUserUnlocked = false;
		post(username, getLocalizedString('has unlocked the chatroom'), "gray");														
	}	
	// get the shared data for the presencelist
	Widget.sharedDataForKey("defaultChatPresence", doInitPresence);	
    dwr.util.setValue("joined", getInputActiveDiv(), { escapeHtml:false });   
    if (isDebug) alert("end handle Unlocked"); 
 }
}

function lockchat(){
	if(isActive){	
		isActive = false;		
		//remove everybody because they wont be able update the shareddata once its locked
		Widget.setSharedDataForKey("defaultChatPresence","");	
		post(username, getLocalizedString('has locked the chatroom'), "gray");	
	}
	Widget.lock(instanceid_key);
}

function unlockchat(){
	isActive = true;
	thisUserUnlocked = true;	
	Widget.unlock(instanceid_key);	
}

// handleUpdate is our local implementation of onSharedUpdate
Widget.onSharedUpdate = handleSharedUpdate;
Widget.onLocked = handleLocked;
Widget.onUnlocked = handleUnlocked;
// onunload tell the system that this page is no longer active. 
//(fires when browser closes or user navigates away from page)
onunload = cleanup;