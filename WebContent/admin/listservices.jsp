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
<%@ page import='org.apache.wookie.helpers.FlashMessage' %>
<%@ page import='org.apache.wookie.beans.IWidgetService' %>
<%IWidgetService[] services = (IWidgetService[])request.getAttribute("services"); %>
<html>
<head>
<title>Widget Service Types</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link type="text/css" href="/wookie/shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />  
<link type="text/css" href="../layout.css" rel="stylesheet" />
<script type="text/javascript" src="/wookie/shared/js/jquery/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/wookie/shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>
<script>
$(document).ready(function(){
	$('#quicklinks tr.ui-widget-header').siblings().toggle();
	$('#quicklinks tr.ui-widget-header').click(function() {
		$(this).siblings().toggle();
			return false;
	})
});
</script>
</head>
<body>
    <div id="header">
 		<div id="banner">
    		<div style="float:left;">
    			<img style="margin: 8 8px;" border="0" src="../shared/images/furry_white.png">
    		</div>
    		<div id="menu"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		
    		<table width="200" id="quicklinks" style="margin-top:4px" class="ui-widget ui-widget-content" align="right">  
		<tr class="ui-widget-header"><td>&nbsp;Quick links</td></tr>
		<tr><td width="100%"><a href="./WidgetAdminServlet?operation=LISTSERVICES&param=add"><img src="../shared/images/add_1.gif" width="16" height="16" border="0">&nbsp;Add new service</a></td></tr>
		<tr><td width="100%"><a href="./WidgetAdminServlet?operation=LISTSERVICES&param=remove"><img src="../shared/images/delete_1.gif" width="16" height="16" border="0">&nbsp;Remove existing service</a></td></tr>
	</table><h3>Widget Service Types</h3>
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
	<p>A widget can be seen as having several "contexts" or "types". The same widget may be referred to as both a "discussion" and a "forum" type of tool, for example. When a client of the widget system asks for a widget, it will ask using one of these type names. A widget type cannot be assigned to a widget unless it has been entered in this list.</p>
	
	
	
	<br>
		
				
		<table width="500" class="ui-widget ui-widget-content" align="center">  
		<tr class="ui-widget-header"><td>&nbsp;Existing service types</td></tr>
		<%for (int i=0; i<services.length; i++) if (!services[i].getServiceName().equals("unsupported")) {%>
	  		<tr><td width="100%">
	  		
	  		<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>	        
						
	  		
	  		&nbsp;&nbsp;&nbsp;&nbsp;<%=services[i].getServiceName()%>
	  		
	  		<b class="rbottom">
						<b class="r4"></b>
						<b class="r3"></b>
						<b class="r2"></b>
						<b class="r1"></b>
					</b>
				</div>
				
	  		
	  		</td></tr>
	  	<%}%>
		</table>
	
</div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>

</body>
</html>
<% FlashMessage.clearErrorsAndMessages(session);%>
