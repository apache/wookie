<%--
/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */
--%>
<%@ page import='org.apache.wookie.helpers.FlashMessage' %>
<%@ page import='org.apache.wookie.Messages;'%>
<% Messages localizedMessages = (Messages)session.getAttribute(Messages.class.getName()); %>
<!DOCTYPE html>
<% String version = (String)request.getAttribute("version");
// "version" is lazy loaded and will be empty until a request is made to the server
if (version==null){
	version="";
}
%>
<html>
<head>
<title><%=localizedMessages.getString("webmenu.index.0")%> <%=version%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link type="text/css" href="/wookie/shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />    
  <link type="text/css" href="../layout.css" rel="stylesheet" />
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-1.3.2.min.js"></script>
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>
</head>

<body>
    <div id="header">
 		<div id="banner">
    		<div style="float:left;">
    			<img style="margin: 4 8px;" border="0" src="../shared/images/furry_white.png">
    		</div>
    		<div id="menu"></div>
    	</div> 
    	<div id="pagetitle">
    		<h3><%=localizedMessages.getString("webmenu.index.6")%></h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content">    
	
	<% String errors = FlashMessage.getErrors(session, request);%>
	<% String messages = FlashMessage.getMessages(session, request);%>
	<%if(errors!=null){%>
      <p><img src="../shared/images/cancel.gif" width="16" height="16"><font color=red> <%=errors%> </font> </p>
	<%}%>
	<%if(messages!=null){%>
	<p><img src="../shared/images/greentick.gif" width="16" height="16">
		<font color=green>
		<%=messages%>
		</font>
	</p>
	<%}%>
	<h4><%=localizedMessages.getString("webmenu.index.1")%></h4>
	<br/>
	
	<div style="background:#ffffff;padding-top: 4px;">
	
	
	<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>	
						<div class="adminLayerDetail"><p><a href="WidgetWebMenuServlet?operation=LISTWIDGETS" ><img src="../shared/images/view_1.gif" width="20" height="20" border="0">&nbsp;<%=localizedMessages.getString("webmenu.index.2")%></a></p></div>
					<b class="rbottom">
						<b class="r4"></b>
						<b class="r3"></b>
						<b class="r2"></b>
						<b class="r1"></b>
					</b>
	</div>
	<div id="spacer"></div>

	<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>	
						<div class="adminLayerDetail"><p><a href="../admin"><img src="../shared/images/unlock.gif" width="20" height="20" border="0">&nbsp;<%=localizedMessages.getString("webmenu.index.3")%></a></p></div>
					<b class="rbottom">
						<b class="r4"></b>
						<b class="r3"></b>
						<b class="r2"></b>
						<b class="r1"></b>
					</b>
	</div>
	<div id="spacer"></div>	
	
	<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>	
						<div class="adminLayerDetail"><p>
						<a href="WidgetWebMenuServlet?operation=INSTANTIATE"><img src="../shared/images/run.gif" width="18" height="18" border="0">&nbsp;<%=localizedMessages.getString("webmenu.index.4")%></a></p></div>
					<b class="rbottom">
						<b class="r4"></b>
						<b class="r3"></b>
						<b class="r2"></b>
						<b class="r1"></b>
					</b>
	</div>
	<div id="spacer"></div>	
	
	<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>	
						<div class="adminLayerDetail"><p>
						<a href="requestapikey.jsp"><img src="../shared/images/mail.png" width="16" height="16" border="0">&nbsp;<%=localizedMessages.getString("webmenu.index.5")%></a></p></div>
					<b class="rbottom">
						<b class="r4"></b>
						<b class="r3"></b>
						<b class="r2"></b>
						<b class="r1"></b>
					</b>
	</div>
	<div id="spacer"></div>	

</div></div>
	
<div id="footer">
	<div style="text-align:right">&nbsp;</div>
</div>

</body>
</html>
<% FlashMessage.clearErrorsAndMessages(session);%>
