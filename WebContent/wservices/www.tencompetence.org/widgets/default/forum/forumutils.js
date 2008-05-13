/*
****************   DEFAULT FORUM WIDGET **************
******************************************************
*/
var isDebug = false;
var currentLanguage = "en";
var supportedLanguages = new Array("bu","en","fr","nl");
var instanceid_key;
var proxyUrl="";
var widgetAPIUrl="";
var username="";
var forumText="";
var currentPost=null;
var isLocked = false;
var sharedDataKey = null;
var isAdmin = false;


// on start up set some values & init with the server
function init() {
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
		// this line tells DWR to use call backs (i.e. will call onsharedupdate() when an event is recevied for shared data
		// below - commented out - is this needed when there is no need to poll the server?
	 	dwr.engine.setActiveReverseAjax(true);	 		
	 	widget.preferenceForKey(instanceid_key, "LDUsername", setLocalUsername);	 	
		
}

function isAdminUser(){
	return isAdmin;
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

function doLanguageUpdate(la){
	currentLanguage = la;	
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

function getAdminInactiveToolsStr(){
	var adminInactiveToolsStr = "<a href=\"#\" onclick=\"unlockforum()\"><img border=\"0\" src=\"/wookie/shared/images/unlock.gif\" alt=\"" + getLocalizedString('Unlock this widget') + "\"></a>";
	return adminInactiveToolsStr;
}

function getAdminActiveToolsStr(){
	var adminActiveToolsStr = "&nbsp;&nbsp;";
	adminActiveToolsStr += "<a href=\"#\" onclick=\"lockforum()\"><img border=\"0\" src=\"/wookie/shared/images/lock.gif\" alt=\"" + getLocalizedString('Lock this widget') + "\">&nbsp;" + getLocalizedString('Lock this widget') + "</a>";
	return adminActiveToolsStr;
}

function getInactiveToolsStr(){
	var inactiveToolsStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>" + getLocalizedString('This widget has been locked') + "</b>&nbsp;";
	if(isAdminUser()){
		inactiveToolsStr += getAdminInactiveToolsStr();
	}
	return inactiveToolsStr;
}

function getActiveToolsStr(){
	var activeToolsStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + getLangOpts();
	activeToolsStr += "<a href=\"#\" onclick=\"postNewTopic(-1)\"><img border=\"0\" src=\"/wookie/shared/images/plus.gif\">&nbsp;" + getLocalizedString('Post new topic') + "</a>";
	activeToolsStr += "&nbsp;&nbsp;<a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"', getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/refresh.gif\">&nbsp;" + getLocalizedString('Refresh') + "</a>";
	if(isAdminUser()){
		activeToolsStr += getAdminActiveToolsStr();
	}	
	return activeToolsStr;
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
		alert(getLocalizedString('Record no longer exists in Database, probably deleted by another user'));
		forum.getNodeTree(instanceid_key, getTreeData);
	}
	else{	
		var toolsStr="";
		var viewPostStr = getViewPostLayout(openPost);		
		dwr.util.setValue("content", viewPostStr, { escapeHtml:false });
		currentPost = openPost;
		toolsStr +="&&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		if(!isLocked){
			toolsStr +="<a href=\"#\" onclick=\"getReplyToPostLayout()\"><img border=\"0\" src=\"/wookie/shared/images/go.gif\">&nbsp;" + getLocalizedString('Reply') + "</a>&nbsp;";
		}
		toolsStr +="<a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"', getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/cancel.gif\">&nbsp;" + getLocalizedString('Cancel') + "</a>";				
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
  	postNewTopicContent+="<a href=\"#\" onclick=\"postIt("+currentPost.id+")\"><img border=\"0\" src=\"/wookie/shared/images/go.gif\">&nbsp;" + getLocalizedString('Post') + "</a>";  	
  	postNewTopicContent+="&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"',getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/cancel.gif\">&nbsp;" + getLocalizedString('Cancel') + "</a>";
	dwr.util.setValue("foot", postNewTopicContent, { escapeHtml:false });
}

function postNewTopic(parentPostId){
	var newPostStr = getNewPostLayout(parentPostId);
	dwr.util.setValue("content", newPostStr, { escapeHtml:false });
	var postNewTopicContent="";
	postNewTopicContent+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
  	postNewTopicContent+="<a href=\"#\" onclick=\"postIt("+parentPostId+")\"><img border=\"0\" src=\"/wookie/shared/images/go.gif\">&nbsp;" + getLocalizedString('Post') + "</a>";  	
  	postNewTopicContent+="&nbsp;&nbsp;<a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"',getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/cancel.gif\">&nbsp;" + getLocalizedString('Cancel') + "</a>";
	dwr.util.setValue("foot", postNewTopicContent, { escapeHtml:false });
}

function getViewPostLayout(openPost){
	var openPostStr="";
	openPostStr+="<fieldset id=\"wf_ParticipantInform-sessur\" class=\"repeat\">";
	openPostStr+="	<legend>"+openPost.userId+" " + getLocalizedString('wrote on') + " " + formatDate(openPost.publishDate)+"</legend>";
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
		titleText = getLocalizedString('Post');
	}
	else{
		titleText = getLocalizedString('Reply');
		if(currentPost.title.substr(0,3)!="Re:"){
			reText = "Re:" + currentPost.title;
		}
		else{
			reText = currentPost.title;
		}
	}
	var compoText = getLocalizedString('Compose');
	var postNewTopicContent="";	
	postNewTopicContent+="<fieldset id=\"wf_GuardianInformati\" class=\"\">";
	postNewTopicContent+="	<legend>" + compoText + " " + titleText + "</legend>";
	postNewTopicContent+="		<span class=\"oneField\">";
	postNewTopicContent+="			<label for=\"wf_Email\" class=\"preField\">"+getLocalizedString('Title')+"</label>";
	postNewTopicContent+="			<input type=\"text\" id=\"title\" name=\"title\" value=\""+reText+"\" size=\"50\" class=\"validate-email\">";
	postNewTopicContent+="			<br>";
	postNewTopicContent+="		</span>";
	postNewTopicContent+="		<span class=\"oneField\">";
	postNewTopicContent+="			<label for=\"wf_Address\" class=\"preField\">"+getLocalizedString('Message')+"</label>";
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
		alert(getLocalizedString('You must specify a Title for this post'));
		return;
	}
	if(content.length<1){
		alert(getLocalizedString('Empty posts are not allowed'));
		return;
	}
	forum.newPost(instanceid_key, param, username, title, content, outcomeOfPost);
}

function outcomeOfPost(param){
	var outcomeContent="";
	var toolsContent="";
	var linkText = " <a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"',getTreeData);\">" + getLocalizedString('Click here to continue') + "</a>"
	outcomeContent+="<div>"
	if (param==true){
		outcomeContent+="<p>" + getLocalizedString('Post was successfully added')+ linkText+"</p>"
		
	}
	else{
		outcomeContent+="<p>" + getLocalizedString('Unable to add new post')+ linkText+"</p>"
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
	isActive = false;	
	var resStr = getInactiveToolsStr();	
    dwr.util.setValue("foot", resStr, { escapeHtml:false });    
    isLocked = true;
    forum.getNodeTree(instanceid_key, getTreeData);
 }
}

function handleUnlocked(sdkey){	
 if(sdkey == sharedDataKey){
	if (isDebug) alert("start handle Unlocked");
	isActive = true;
	isLocked = false;
	var resStr = getActiveToolsStr();
	dwr.util.setValue("foot", resStr, { escapeHtml:false }); 
	forum.getNodeTree(instanceid_key, getTreeData); 
    if (isDebug) alert("end handle Unlocked");
  } 
}

function lockforum(){
	if(isActive){	
		isActive = false;		
	}
	widget.lock(instanceid_key);
}

function unlockforum(){
	isActive = true;	
	widget.unlock(instanceid_key);	
}

widget.onLocked = handleLocked;
widget.onUnlocked = handleUnlocked;