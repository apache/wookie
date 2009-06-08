/*
****************   DEFAULT FORUM WIDGET **************
******************************************************
*/
var isDebug = false;
var instanceid_key;
var username="";
var forumText="";
var currentPost=null;
var isLocked = false;
var sharedDataKey = null;
var isAdmin = false;
var isdActive=false;


// on start up set some values & init with the server
function init() {
if (isDebug) DebugHelper.debug("<function init start>");				
 	instanceid_key = Widget.getInstanceKey();
 	if (isDebug) DebugHelper.debug("<function init start 2>"); 		 
 	if (Widget.hasFeature("polling")){
 		// carry on
		Widget.preferenceForKey("username", setLocalUsername);
	}
	else{
		//error
		WidgetUtil.setValue("chatlog", "The polling feature is unavailable.", { escapeHtml:false }); 
	}	
}


function isAdminUser(){
	return isAdmin;
}

// set the local username
function setLocalUsername(p){
	if (isDebug) DebugHelper.debug("<function setLocalUsername start>| param="+p);
	if(username == ""){
		if(p == "null"){
			username = "User "+ Math.floor((Math.random() * 900) + 100);
		}
		else {
			username = p;
		}
	}	 
	Widget.preferenceForKey("conference-manager", getConferenceManagerRole);
}

function getConferenceManagerRole(p){
	if (isDebug) DebugHelper.debug("<function setConferenceManagerRole start>| param="+p);
	if(p == "true") isAdmin = true;
	Widget.preferenceForKey("moderator", getModeratorRole);
}

function getModeratorRole(p){	
	if (isDebug) DebugHelper.debug("<function setModeratorRole start>| param="+p);
	if(p == "true") isAdmin = true;
	Widget.preferenceForKey("sharedDataKey", initSharedKey);
}

function initSharedKey(sharedKey){
	if (isDebug) DebugHelper.debug("<function initsharedkey start>| param="+sharedKey);
	sharedDataKey = sharedKey;
	Widget.sharedDataForKey("isLocked", setupInput);
}

function setupInput(isLockedValue){
	if (isDebug) DebugHelper.debug("<function setupinput start>| param="+isLockedValue);
	if(isLockedValue!="true"){		
		handleUnlocked(sharedDataKey); 
	}
	else{
		handleLocked(sharedDataKey);
	}	
}

function getAdminInactiveToolsStr(){
	var adminInactiveToolsStr = "<a href=\"#\" onclick=\"unlockforum()\"><img border=\"0\" src=\"/wookie/shared/images/unlock.gif\" alt=\"" + LanguageHelper.getLocalizedString('Unlock this widget') + "\"></a>";
	return adminInactiveToolsStr;
}

function getAdminActiveToolsStr(){
	var adminActiveToolsStr = "&nbsp;&nbsp;";
	adminActiveToolsStr += "<a href=\"#\" onclick=\"lockforum()\"><img border=\"0\" src=\"/wookie/shared/images/lock.gif\" alt=\"" + LanguageHelper.getLocalizedString('Lock this widget') + "\">&nbsp;" + LanguageHelper.getLocalizedString('Lock this widget') + "</a>";
	return adminActiveToolsStr;
}

function getInactiveToolsStr(){
	var inactiveToolsStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>" + LanguageHelper.getLocalizedString('This widget has been locked') + "</b>&nbsp;";
	if(isAdminUser()){
		inactiveToolsStr += getAdminInactiveToolsStr();
	}
	return inactiveToolsStr;
}

function getActiveToolsStr(){
	var activeToolsStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + LanguageHelper.getLangOpts(getActiveToolsStrUpdated);
	activeToolsStr += "<a href=\"#\" onclick=\"postNewTopic(-1)\"><img border=\"0\" src=\"/wookie/shared/images/plus.gif\">&nbsp;" + LanguageHelper.getLocalizedString('Post new topic') + "</a>";
	activeToolsStr += "&nbsp;&nbsp;<a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"', getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/refresh.gif\">&nbsp;" + LanguageHelper.getLocalizedString('Refresh') + "</a>";
	if(isAdminUser()){
		activeToolsStr += getAdminActiveToolsStr();
	}	
	return activeToolsStr;
}

function getActiveToolsStrUpdated(){
	var resStr = getActiveToolsStr();
	dwr.util.setValue("foot", resStr, { escapeHtml:false }); 
}

function getTreeData(param){

// wipe the current list before rebuilding it
	forumText="";
	buildTree(param);
	dwr.util.setValue("content", forumText, { escapeHtml:false });
		
	var toolsStr = "";
	if (isLocked){
		toolsStr = getInactiveToolsStr();
	}
	else{
		toolsStr = getActiveToolsStr();
	}
	dwr.util.setValue("foot", toolsStr, { escapeHtml:false });
}

function buildTree(postlist){				
	forumText+="<ul>";
	for (var data in postlist) {
    		forumText+= "<li><b><a href=\"#\" onclick=\"forum.getPost('"+instanceid_key+"','"+postlist[data].id+"', openPost)\";>" + 
    		dwr.util.escapeHtml(postlist[data].title) + "</a></b><i>&nbsp;&nbsp;" +    		
    		dwr.util.escapeHtml("added "+formatDate(postlist[data].publishDate)) +"&nbsp;by " +
    		dwr.util.escapeHtml(postlist[data].userId) + "&nbsp;" +		 
    		 "</i></li>";      		
    		buildTree(postlist[data].posts);  	
    }
	forumText+="</ul>"; 
}

// we ask the server for this in case its been deleted & we try to add a child.
function openPost(openPost){
	if(openPost==null){
		alert(LanguageHelper.getLocalizedString('Record no longer exists in Database, probably deleted by another user'));
		forum.getNodeTree(instanceid_key, getTreeData);
	}
	else{	
		var toolsStr="";
		var viewPostStr = getViewPostLayout(openPost);		
		dwr.util.setValue("content", viewPostStr, { escapeHtml:false });
		currentPost = openPost;
		toolsStr +="&&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		if(!isLocked){
			toolsStr +="<a href=\"#\" onclick=\"getReplyToPostLayout()\"><img border=\"0\" src=\"/wookie/shared/images/go.gif\">&nbsp;" + LanguageHelper.getLocalizedString('Reply') + "</a>&nbsp;";
		}
		toolsStr +="<a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"', getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/cancel.gif\">&nbsp;" + LanguageHelper.getLocalizedString('Cancel') + "</a>";				
		dwr.util.setValue("foot", toolsStr, { escapeHtml:false });
	}	
}

function getReplyToPostLayout(){	
	var replyStr="";
	replyStr+=getViewPostLayout(currentPost)
	replyStr+=getNewPostLayout(currentPost.id);
	dwr.util.setValue("content", replyStr, { escapeHtml:false });
	
	var postNewTopicContent="";
	postNewTopicContent+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";	
  	postNewTopicContent+="<a href=\"#\" onclick=\"postIt("+currentPost.id+")\"><img border=\"0\" src=\"/wookie/shared/images/go.gif\">&nbsp;" + LanguageHelper.getLocalizedString('Post') + "</a>";  	
  	postNewTopicContent+="&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"',getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/cancel.gif\">&nbsp;" + LanguageHelper.getLocalizedString('Cancel') + "</a>";
	dwr.util.setValue("foot", postNewTopicContent, { escapeHtml:false });
}

function postNewTopic(parentPostId){
	var newPostStr = getNewPostLayout(parentPostId);
	dwr.util.setValue("content", newPostStr, { escapeHtml:false });
	var postNewTopicContent="";
	postNewTopicContent+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
  	postNewTopicContent+="<a href=\"#\" onclick=\"postIt("+parentPostId+")\"><img border=\"0\" src=\"/wookie/shared/images/go.gif\">&nbsp;" + LanguageHelper.getLocalizedString('Post') + "</a>";  	
  	postNewTopicContent+="&nbsp;&nbsp;<a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"',getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/cancel.gif\">&nbsp;" + LanguageHelper.getLocalizedString('Cancel') + "</a>";
	dwr.util.setValue("foot", postNewTopicContent, { escapeHtml:false });
}

function getViewPostLayout(openPost){
	var openPostStr="";
	openPostStr+="<fieldset id=\"wf_ParticipantInform-sessur\" class=\"repeat\">";
	openPostStr+="	<legend>"+openPost.userId+" " + LanguageHelper.getLocalizedString('wrote on') + " " + formatDate(openPost.publishDate)+"</legend>";
	openPostStr+="		<span class=\"oneField\">";
	openPostStr+="			<span>" + openPost.content + "</span>";
	openPostStr+="		</span>";
	openPostStr+="</fieldset>";
	return openPostStr;
}

function getNewPostLayout(parentPostId){

	var titleText;
	var reText="";
	if(parentPostId==-1){
		titleText = LanguageHelper.getLocalizedString('Post');
	}
	else{
		titleText = LanguageHelper.getLocalizedString('Reply');
		if(currentPost.title.substr(0,3)!="Re:"){
			reText = "Re:" + currentPost.title;
		}
		else{
			reText = currentPost.title;
		}
	}
	var compoText = LanguageHelper.getLocalizedString('Compose');
	var postNewTopicContent="";	
	postNewTopicContent+="<fieldset id=\"wf_GuardianInformati\" class=\"\">";
	postNewTopicContent+="	<legend>" + compoText + " " + titleText + "</legend>";
	postNewTopicContent+="		<span class=\"oneField\">";
	postNewTopicContent+="			<label for=\"wf_Email\" class=\"preField\">"+LanguageHelper.getLocalizedString('Title')+"</label>";
	postNewTopicContent+="			<input type=\"text\" id=\"title\" name=\"title\" value=\""+reText+"\" size=\"50\" class=\"validate-email\">";
	postNewTopicContent+="			<br>";
	postNewTopicContent+="		</span>";
	postNewTopicContent+="		<span class=\"oneField\">";
	postNewTopicContent+="			<label for=\"wf_Address\" class=\"preField\">"+LanguageHelper.getLocalizedString('Message')+"</label>";
	postNewTopicContent+="			<textarea id=\"textcontent\" name=\"textcontent\" class=\"required\" cols=\"38\" rows=\"5\"></textarea>";
	postNewTopicContent+="			<br>";
	postNewTopicContent+="		</span>";
	postNewTopicContent+="</fieldset>";
	return postNewTopicContent;
}

function postIt(param){
	var title = document.getElementById("title").value;
	var content = document.getElementById("textcontent").value;
	if(title.length<1){
		alert(LanguageHelper.getLocalizedString('You must specify a Title for this post'));
		return;
	}
	if(content.length<1){
		alert(LanguageHelper.getLocalizedString('Empty posts are not allowed'));
		return;
	}
	forum.newPost(instanceid_key, param, username, title, content, outcomeOfPost);
}

function outcomeOfPost(param){
	var outcomeContent="";
	var toolsContent="";
	var linkText = " <a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"',getTreeData);\">" + LanguageHelper.getLocalizedString('Click here to continue') + "</a>"
	outcomeContent+="<div>"
	if (param==true){
		outcomeContent+="<p>" + LanguageHelper.getLocalizedString('Post was successfully added')+ linkText+"</p>"
		
	}
	else{
		outcomeContent+="<p>" + LanguageHelper.getLocalizedString('Unable to add new post')+ linkText+"</p>"
	}
	outcomeContent+="</div>"	
	dwr.util.setValue("content", outcomeContent, { escapeHtml:false });
	dwr.util.setValue("foot", toolsContent, { escapeHtml:false });
}

function formatDate(d){
	var t_date = d.getDate();      // Returns the day of the month
	var t_mon = d.getMonth();      // Returns the month as a digit
	var t_year = d.getFullYear();  // Returns 4 digit year
	var t_hour = d.getHours();     // Returns hours
	var curr_min = d.getMinutes();
	curr_min = curr_min + "";
	if (curr_min.length == 1){curr_min = "0" + curr_min;}	
	var curr_sec = d.getSeconds(); 
	curr_sec = curr_sec + "";
	if (curr_sec.length == 1){curr_sec = "0" + curr_sec;}				
	return t_date+"/"+t_mon+"/"+t_year+" "+ t_hour + ":" + curr_min + ":" + curr_sec
}

function handleLocked(sdkey){
 if(sdkey == sharedDataKey){
	isdActive = false;	
	var resStr = getInactiveToolsStr();	
    dwr.util.setValue("foot", resStr, { escapeHtml:false });    
    isLocked = true;
    forum.getNodeTree(instanceid_key, getTreeData);
 }
}

function handleUnlocked(sdkey){	
 if(sdkey == sharedDataKey){
	if (isDebug) DebugHelper.debug("<start handle unlocked> ");
	isdActive = true;
	isLocked = false;
	var resStr = getActiveToolsStr();
	dwr.util.setValue("foot", resStr, { escapeHtml:false }); 
	forum.getNodeTree(instanceid_key, getTreeData); 
    if (isDebug) DebugHelper.debug("<end handle unlocked> ");
  } 
}

function lockforum(){
	if(isdActive){	
		isdActive = false;		
	}
	Widget.lock();
}

function unlockforum(){
	isdActive = true;	
	Widget.unlock();	
}

Widget.onLocked = handleLocked;
Widget.onUnlocked = handleUnlocked;