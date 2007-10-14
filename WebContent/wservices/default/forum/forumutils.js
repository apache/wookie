/*
****************   DEFAULT FORUM WIDGET **************
******************************************************
*/
var instanceid_key;
var proxyUrl="";
var widgetAPIUrl="";
var username="";
var forumText="";
var currentPost=null;

// set the local username
function setLocalUsername(p){
	username = p;	 
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
	 	dwr.engine.setActiveReverseAjax(true);
	 	widget.preferenceForKey(instanceid_key, "LDUsername", setLocalUsername);	
		forum.getNodeTree(getTreeData);
}

function getTreeData(param){
// wipe the current list before rebuilding it
	forumText="";
	buildTree(param);
	dwr.util.setValue("content", forumText, { escapeHtml:false });
	
	var toolsStr= "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#\" onclick=\"postNewTopic(0)\"><img border=\"0\" src=\"/wookie/shared/images/plus.gif\">&nbsp;Post new Topic</a>";
	
	dwr.util.setValue("foot", toolsStr, { escapeHtml:false });
}

function buildTree(postlist){				
	forumText+="<ul>";
	for (var data in postlist) {
    		forumText+= "<li><b><a href=\"#\" onclick=\"forum.getPost('"+postlist[data].id+"', openPost)\";>" + 
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
		forum.getNodeTree(getTreeData);
	}
	else{	
		var toolsStr="";
		var viewPostStr = getViewPostLayout(openPost);		
		dwr.util.setValue("content", viewPostStr, { escapeHtml:false });
		currentPost = openPost;
		toolsStr +="&&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		toolsStr +="<a href=\"#\" onclick=\"getReplyToPostLayout()\"><img border=\"0\" src=\"/wookie/shared/images/go.gif\">&nbsp;Reply</a>&nbsp;<a href=\"#\" onclick=\"forum.getNodeTree(getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/cancel.gif\">&nbsp;Cancel</a>";
		dwr.util.setValue("foot", toolsStr, { escapeHtml:false });
	}	
}

function getReplyToPostLayout(){
	//openPost = currentPost;	
	var replyStr="";
	replyStr+=getViewPostLayout(currentPost)
	//replyStr+="<hr>";
	replyStr+=getNewPostLayout(currentPost.id);
	dwr.util.setValue("content", replyStr, { escapeHtml:false });
	
	var postNewTopicContent="";
	postNewTopicContent+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";	
  	postNewTopicContent+="<a href=\"#\" onclick=\"postIt("+currentPost.id+")\"><img border=\"0\" src=\"/wookie/shared/images/go.gif\">&nbsp;Post</a>";  	
  	postNewTopicContent+="&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#\" onclick=\"forum.getNodeTree(getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/cancel.gif\">&nbsp;Cancel</a>";
	dwr.util.setValue("foot", postNewTopicContent, { escapeHtml:false });
	//currentPost=null;
}

function postNewTopic(parentPostId){
	var newPostStr = getNewPostLayout(parentPostId);
	dwr.util.setValue("content", newPostStr, { escapeHtml:false });
	var postNewTopicContent="";
	postNewTopicContent+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
  	postNewTopicContent+="<a href=\"#\" onclick=\"postIt("+parentPostId+")\"><img border=\"0\" src=\"/wookie/shared/images/go.gif\">&nbsp;Post</a>";  	
  	postNewTopicContent+="&nbsp;&nbsp;<a href=\"#\" onclick=\"forum.getNodeTree(getTreeData);\"><img border=\"0\" src=\"/wookie/shared/images/cancel.gif\">&nbsp;Cancel</a>";
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
	
/*
	
	openPostStr+="<div>";
	openPostStr+="<p>Title: ";
	openPostStr+=openPost.title
	openPostStr+="</p>";
	openPostStr+="<p>Postee: ";
  	openPostStr+=openPost.userId;
	openPostStr+="</p>";
	openPostStr+="<p>Posted at: ";
  	openPostStr+=formatDate(openPost.publishDate);
	openPostStr+="</p>";
	openPostStr+="<p>Message: ";
  	openPostStr+=openPost.content;
	openPostStr+="</p>";
	openPostStr+="</div>";
	*/
	return openPostStr;
}

function getNewPostLayout(parentPostId){
	var postNewTopicContent="";
	
	postNewTopicContent+="<fieldset id=\"wf_GuardianInformati\" class=\"\">";
	postNewTopicContent+="	<legend>Compose Reply</legend>";
	postNewTopicContent+="		<span class=\"oneField\">";
	postNewTopicContent+="			<label for=\"wf_Email\" class=\"preField\">Title</label>";
	postNewTopicContent+="			<input type=\"text\" id=\"title\" name=\"title\" value=\"\" size=\"50\" class=\"validate-email\">";
	postNewTopicContent+="			<br>";
	postNewTopicContent+="		</span>";
	postNewTopicContent+="		<span class=\"oneField\">";
	postNewTopicContent+="			<label for=\"wf_Address\" class=\"preField\">Message</label>";
	postNewTopicContent+="			<textarea id=\"textcontent\" name=\"textcontent\" class=\"required\" cols=\"38\" rows=\"5\"></textarea>";
	postNewTopicContent+="			<br>";
	postNewTopicContent+="		</span>";
	postNewTopicContent+="</fieldset>";
	
	/*
	postNewTopicContent+="<div id=\"newpost\">";	
  	postNewTopicContent+="<p>Title: ";
    postNewTopicContent+="<input id=\"title\" name=\"title\" type=\"text\" size=\"40\" maxlength=\"80\">";
  	postNewTopicContent+="</p>";  	
	postNewTopicContent+="<p>Message: ";
  	postNewTopicContent+="<textarea id=\"textcontent\" name=\"content\"></textarea>";
	postNewTopicContent+="</p>";
	postNewTopicContent+="</div>";
	*/
	
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
	forum.newPost(param,username,title,content, outcomeOfPost);
}

function outcomeOfPost(param){
	var outcomeContent="";
	var toolsContent="";
	var linkText = " Click <a href=\"#\" onclick=\"forum.getNodeTree(getTreeData);\">here</a> to continue"
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
