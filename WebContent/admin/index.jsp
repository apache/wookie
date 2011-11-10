<!DOCTYPE html>
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
<%@ page import='org.apache.commons.configuration.Configuration' %>
<% String version = (String)request.getAttribute("version");
if (version == null){
	version="";
}
Boolean opensocialEnabled = ((Configuration)request.getSession(true).getServletContext().getAttribute("opensocial")).getBoolean("opensocial.enable");
if (opensocialEnabled == null) opensocialEnabled = false;
%>
<%@ page import='org.apache.wookie.helpers.FlashMessage' %>
<% FlashMessage.getInstance().appendFlashMessages(session);%>
<% String errors = FlashMessage.getErrors(session, request);%>
<% String messages = FlashMessage.getMessages(session, request);%>
<html>
<head>
<title>Widget Server Administration <%=version%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">	
	<link type="text/css" href="../shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />    
	<link rel="stylesheet" href="../layout.css" type="text/css">	
  	<script type="text/javascript" src="../shared/js/jquery/jquery-1.3.2.min.js"></script>
  	<script type="text/javascript" src="../shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>
  	
 	<script type="text/javascript">
 		$(document).ready(function(){
			$("#accordion").accordion();
		});
  	</script>
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
    		<h3>Administrator Menu</h3>
    	</div>
    	<!--  END HEADER -->
</div>


    
<div id="content"> 
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
		<p>
		This menu allows access to the administration of the widget system.  The links are broken up into sections.  
		The first section allows you to add/remove widgets and also add new service types to them. 
		The second section allows you to add/remove new service types. 
		The third section allows you to add/remove entries to the in-built proxy server.
		Finally the last section contains any other relevent links, such as returning to the main Wookie holding page. 
		</p>
		<div id="accordion">	
			<h3><a href="#"><img height="20" width="20" border="0" src="../shared/images/view_1.gif"/>Widgets</a></h3>
			<div>
				<div id="nifty">
				<b class="rtop">
				<b class="r1"></b>
				<b class="r2"></b>
				<b class="r3"></b>
				<b class="r4"></b>
				</b>	
				<div class="adminLayerDetail"><a href="WidgetAdminServlet?operation=LISTWIDGETS&param=list" class="hypercolour"><img src="../shared/images/view_1.gif" width="16" height="16" border="0">&nbsp;View existing widgets</a></div>
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
		    	<div class="adminLayerDetail"><a href="selectupload.htm" class="hypercolour"><img src="../shared/images/add_1.gif" width="16" height="16" border="0">&nbsp;Add new widget</a></div>
		    	<b class="rbottom">
				<b class="r4"></b>
				<b class="r3"></b>
				<b class="r2"></b>
				<b class="r1"></b>
				</b>
				</div>
				
				<%if(opensocialEnabled){%>
				<div id="spacer"></div>
	    	   
				<div id="nifty">
				<b class="rtop">
				<b class="r1"></b>
				<b class="r2"></b>
				<b class="r3"></b>
				<b class="r4"></b>
				</b>	
		    	<div class="adminLayerDetail"><a href="registergadget.jsp" class="hypercolour"><img src="../shared/images/add_2.gif" width="16" height="16" border="0">&nbsp;Add new Google Gadget/OpenSocial app</a></div>
		    	<b class="rbottom">
				<b class="r4"></b>
				<b class="r3"></b>
				<b class="r2"></b>
				<b class="r1"></b>
				</b>
				</div>
				<%}%>
			
				<div id="spacer"></div>
			    
				<div id="nifty">
				<b class="rtop">
				<b class="r1"></b>
				<b class="r2"></b>
				<b class="r3"></b>
				<b class="r4"></b>
				</b>
		    	<div class="adminLayerDetail"><a href="WidgetAdminServlet?operation=LISTWIDGETS&param=remove" class="hypercolour"><img src="../shared/images/delete_1.gif" width="16" height="16" border="0">&nbsp;Remove widget from system</a></div>
		    	<b class="rbottom">
				<b class="r4"></b>
				<b class="r3"></b>
				<b class="r2"></b>
				<b class="r1"></b>
				</b>
				</div>
		</div>
		
		
		
		<h3><a href="#"><img height="20" width="20" border="0" src="../shared/images/view_1.gif"/>Service types</a></h3>
		<div>
				<div id="nifty">
				<b class="rtop">
				<b class="r1"></b>
				<b class="r2"></b>
				<b class="r3"></b>
				<b class="r4"></b>
				</b>
				<div class="adminLayerDetail"><a href="WidgetAdminServlet?operation=LISTSERVICES&param=list" class="hypercolour"><img src="../shared/images/view_1.gif" width="16" height="16" border="0">&nbsp;View existing widget services</a></div>
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
				<div class="adminLayerDetail"><a href="WidgetAdminServlet?operation=LISTSERVICES&param=add" class="hypercolour"><img src="../shared/images/add_1.gif" width="16" height="16" border="0">&nbsp;Add new widget service</a></div>
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
		    	<div class="adminLayerDetail"><a href="WidgetAdminServlet?operation=LISTSERVICES&param=remove" class="hypercolour"><img src="../shared/images/delete_1.gif" width="16" height="16" border="0">&nbsp;Remove service from system</a></div>
				<b class="rbottom">
				<b class="r4"></b>
				<b class="r3"></b>
				<b class="r2"></b>
				<b class="r1"></b>
				</b>
				</div>
				
				
	    </div>	
				
	    <h3><a href="#"><img height="20" width="20" border="0" src="../shared/images/view_1.gif"/>White list</a></h3>
	    <div>
				<div id="nifty">
				<b class="rtop">
				<b class="r1"></b>
				<b class="r2"></b>
				<b class="r3"></b>
				<b class="r4"></b>
				</b>	    
			    <div class="adminLayerDetail"><a href="warp.jsp" class="hypercolour"><img src="../shared/images/view_1.gif" width="16" height="16" border="0">&nbsp;Manage widget access request policies</a></div>
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
			    <div class="adminLayerDetail"><a href="WidgetAdminServlet?operation=VIEWWHITELIST&param=list" class="hypercolour"><img src="../shared/images/view_1.gif" width="16" height="16" border="0">&nbsp;View white list</a></div>
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
			    <div class="adminLayerDetail"><a href="WidgetAdminServlet?operation=VIEWWHITELIST&param=add" class="hypercolour"><img src="../shared/images/add_1.gif" width="16" height="16" border="0">&nbsp;Add entry to white list</a></div>
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
			    <div class="adminLayerDetail"><a href="WidgetAdminServlet?operation=VIEWWHITELIST&param=remove" class="hypercolour"><img src="../shared/images/delete_1.gif" width="16" height="16" border="0">&nbsp;Remove entry from white list</a></div>
			    				<b class="rbottom">
				<b class="r4"></b>
				<b class="r3"></b>
				<b class="r2"></b>
				<b class="r1"></b>
				</b>
				</div>
								
	    </div>
	        
		<h3><a href="#"><img height="20" width="20" border="0" src="../shared/images/view_1.gif"/>Other</a></h3>
		<div>
				<div id="nifty">
				<b class="rtop">
				<b class="r1"></b>
				<b class="r2"></b>
				<b class="r3"></b>
				<b class="r4"></b>
				</b>		
				<div class="adminLayerDetail"><a href="../webmenu/index.jsp" class="hypercolour"><img src="../shared/images/go_1.gif" width="16" height="16" border="0">&nbsp;Back to main menu</a></div>
				<b class="rbottom">
				<b class="r4"></b>
				<b class="r3"></b>
				<b class="r2"></b>
				<b class="r1"></b>
				</b>
				</div>				
		</div>	    
	
	</div>
  
</div>

<div id="footer">
	<div style="text-align:right"></div>
</div>

</body>
</html>
<% FlashMessage.clearErrorsAndMessages(session);%>
