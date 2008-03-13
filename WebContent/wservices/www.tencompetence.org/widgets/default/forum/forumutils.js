/*
****************   DEFAULT FORUM WIDGET **************
******************************************************
*/
var isDebug = false;
var instanceid_key;
var proxyUrl="";
var widgetAPIUrl="";
var username="";
var forumText="";
var currentPost=null;
var isLocked = false;
var sharedDataKey = null;

var activeToolsStr = null; // note this string needs to be set once the instanceid_key has been set in the init() method
var adminActiveToolsStr ="&nbsp;&nbsp;<a href=\"#\" onclick=\"lockforum()\"><img border=\"0\" src=\"/wookie/shared/images/lock.gif\" alt=\"Lock this widget\">&nbsp;Lock the forum</a>";

var inactiveToolsStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>This widget has been locked</b>&nbsp;";
var adminInactiveToolsStr= "<a href=\"#\" onclick=\"unlockforum()\"><img border=\"0\" src=\"/wookie/shared/images/unlock.gif\" alt=\"Unlock this widget\"></a>";

function isAdminUser(){
	if (username.indexOf("staff")!=-1){
		return true;
	}
	else if(username.indexOf("teacher")!=-1){
		return true;
	}
	return false;
}

// set the local username
function setLocalUsername(p){
	username = p;	 
	if(isAdminUser()){
		activeToolsStr+=adminActiveToolsStr;
		inactiveToolsStr +=adminInactiveToolsStr;
	}
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
	 	// have to set this string AFTER the instanceid_key has been set above
	 	activeToolsStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#\" onclick=\"postNewTopic(-1)\"><img border=\"0\" src=\"/wookie/shared/images/plus.gif\">&nbsp;Post new topic</a>&nbsp;&nbsp;<a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"', getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/refresh.gif\">&nbsp;Refresh</a>";
	 	widget.preferenceForKey(instanceid_key, "LDUsername", setLocalUsername);	 	
		
}

function getTreeData(param){

// wipe the current list before rebuilding it
	forumText="";
	buildTree(param);
	dwr.util.setValue("content", forumText, { escapeHtml:false });
		
	var toolsStr = "";
	if (isLocked){
		toolsStr = inactiveToolsStr;
	}
	else{
		toolsStr = activeToolsStr;
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
		alert("Record no longer exists in Database, probably deleted by another user");
		forum.getNodeTree(instanceid_key, getTreeData);
	}
	else{	
		var toolsStr="";
		var viewPostStr = getViewPostLayout(openPost);		
		dwr.util.setValue("content", viewPostStr, { escapeHtml:false });
		currentPost = openPost;
		toolsStr +="&&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		if(!isLocked){
			toolsStr +="<a href=\"#\" onclick=\"getReplyToPostLayout()\"><img border=\"0\" src=\"/wookie/shared/images/go.gif\">&nbsp;Reply</a>&nbsp;";
		}
		toolsStr +="<a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"', getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/cancel.gif\">&nbsp;Cancel</a>";				
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
  	postNewTopicContent+="<a href=\"#\" onclick=\"postIt("+currentPost.id+")\"><img border=\"0\" src=\"/wookie/shared/images/go.gif\">&nbsp;Post</a>";  	
  	postNewTopicContent+="&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"',getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/cancel.gif\">&nbsp;Cancel</a>";
	dwr.util.setValue("foot", postNewTopicContent, { escapeHtml:false });
}

function postNewTopic(parentPostId){
	var newPostStr = getNewPostLayout(parentPostId);
	dwr.util.setValue("content", newPostStr, { escapeHtml:false });
	var postNewTopicContent="";
	postNewTopicContent+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
  	postNewTopicContent+="<a href=\"#\" onclick=\"postIt("+parentPostId+")\"><img border=\"0\" src=\"/wookie/shared/images/go.gif\">&nbsp;Post</a>";  	
  	postNewTopicContent+="&nbsp;&nbsp;<a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"',getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/cancel.gif\">&nbsp;Cancel</a>";
	dwr.util.setValue("foot", postNewTopicContent, { escapeHtml:false });
}

function getViewPostLayout(openPost){
	var openPostStr="";
	openPostStr+="<fieldset id=\"wf_ParticipantInform-sessur\" class=\"repeat\">";
	openPostStr+="	<legend>"+openPost.userId+" wrote on "+formatDate(openPost.publishDate)+"</legend>";
	openPostStr+="		<span class=\"oneField\">";
	openPostStr+="			<span>"+openPost.content+"</span>";
	openPostStr+="		</span>";
	openPostStr+="</fieldset>";
	return openPostStr;
}

function getNewPostLayout(parentPostId){

	var titleText;
	var reText="";
	if(parentPostId==-1){
		titleText = "Post";
	}
	else{
		titleText = "Reply";
		if(currentPost.title.substr(0,3)!="Re:"){
			reText = "Re:" + currentPost.title;
		}
		else{
			reText = currentPost.title;
		}
	}
	var postNewTopicContent="";	
	postNewTopicContent+="<fieldset id=\"wf_GuardianInformati\" class=\"\">";
	postNewTopicContent+="	<legend>Compose "+titleText+"</legend>";
	postNewTopicContent+="		<span class=\"oneField\">";
	postNewTopicContent+="			<label for=\"wf_Email\" class=\"preField\">Title</label>";
	postNewTopicContent+="			<input type=\"text\" id=\"title\" name=\"title\" value=\""+reText+"\" size=\"50\" class=\"validate-email\">";
	postNewTopicContent+="			<br>";
	postNewTopicContent+="		</span>";
	postNewTopicContent+="		<span class=\"oneField\">";
	postNewTopicContent+="			<label for=\"wf_Address\" class=\"preField\">Message</label>";
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
		alert("You must specify a Title for this post");
		return;
	}
	if(content.length<1){
		alert("Empty posts are not allowed");
		return;
	}
	forum.newPost(instanceid_key, param, username, title, content, outcomeOfPost);
}

function outcomeOfPost(param){
	var outcomeContent="";
	var toolsContent="";
	var linkText = " Click <a href=\"#\" onclick=\"forum.getNodeTree('"+instanceid_key+"',getTreeData);\">here</a> to continue"
	outcomeContent+="<div>"
	if (param==true){
		outcomeContent+="<p>Post was successfully added."+linkText+"</p>"
		
	}
	else{
		outcomeContent+="<p>Unable to add new post."+linkText+"</p>"
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
 //if (isDebug) alert("handle Locked");
	isActive = false;		
    dwr.util.setValue("foot", inactiveToolsStr, { escapeHtml:false });    
    // todo: make sure the posts are not editable
    isLocked = true;
    forum.getNodeTree(instanceid_key, getTreeData);
 }
}

function handleUnlocked(sdkey){	
 if(sdkey == sharedDataKey){
	if (isDebug) alert("start handle Unlocked");
	isActive = true;
	isLocked = false;
	dwr.util.setValue("foot", activeToolsStr, { escapeHtml:false }); 
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