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
<%@ page import='org.apache.wookie.Messages,org.apache.wookie.beans.IWidget' %>
<% Messages localizedMessages = (Messages)session.getAttribute(Messages.class.getName()); %>
<% IWidget[] widgets = (IWidget[])request.getAttribute("widgets"); %>
<!DOCTYPE html>
<html>
<head>
<title><%=localizedMessages.getString("webmenu.instantiate.header.title")%></title>
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
    			<img style="margin: 8 8px;" border="0" src="../shared/images/furry_white.png">
    		</div>
    		<div id="menu"><a class="menulink" href="index.jsp"><%=localizedMessages.getString("webmenu.instantiate.menu")%>&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		<h3><%=localizedMessages.getString("webmenu.instantiate.page.title")%></h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content">   	
	<p><%=localizedMessages.getString("webmenu.instantiate.intro")%></p>
	
	<form name="form1" method="post" action="/wookie/WidgetServiceServlet">
	
		<table width="300" class="ui-widget ui-widget-content" align="center">  
		<tr class="ui-widget-header"><td colspan="2">&nbsp;<%=localizedMessages.getString("webmenu.instantiate.form.title")%></td></tr>		
		<tr><td>
		
		
		
		
				<div>
				
				
					<div id="nifty">
					<b class="rtop">
					<b class="r1"></b>
					<b class="r2"></b>
					<b class="r3"></b>
					<b class="r4"></b>
					</b>	
					<div><div class="adminLayerTitle"><%=localizedMessages.getString("webmenu.instantiate.api.key")%></div><div class="adminLayerDetail"><input class="ui-widget input ui-state-default ui-corner-all" name="api_key" type="text" id="api_key" value="TEST"></div></div>
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
					<div><div class="adminLayerTitle"><%=localizedMessages.getString("webmenu.instantiate.user.id")%></div><div class="adminLayerDetail"><input class="ui-widget input ui-state-default ui-corner-all" name="userid" type="text" id="userid" value="testuser"></div></div>
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
			    	<div><div class="adminLayerTitle" ><%=localizedMessages.getString("webmenu.instantiate.sharedDataKey")%></div><div class="adminLayerDetail"><input class="ui-widget input ui-state-default ui-corner-all" name="shareddatakey" type="text" id="shareddatakey" value="mysharedkey"></div></div>
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
			    		<div>
			    		<div class="adminLayerTitle"><%=localizedMessages.getString("webmenu.instantiate.service.type")%></div>
			    		<div class="adminLayerDetail">
				    		
								  <select class="ui-widget input ui-state-default ui-corner-all" name="widgetid"  id="widgetid">
								  <% 
								  for (IWidget widget : widgets){ 
									  if(!widget.getGuid().equalsIgnoreCase("http://notsupported")){
								  %>
								  <option value="<%=widget.getGuid()%>"><%=widget.getWidgetTitle()%></option>
								  <%} %>
								  <%} %>
								  </select>
							
			    		
			    		</div>
			    		</div>
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
			    	<div style="text-align:center"><input class="ui-button ui-state-default ui-corner-all" type="submit" name="Submit" value="<%=localizedMessages.getString("webmenu.instantiate.submit")%>"><input type="hidden" name="requestid" value="getwidget"></div>
			    	<b class="rbottom">
					<b class="r4"></b>
					<b class="r3"></b>
					<b class="r2"></b>
					<b class="r1"></b>
					</b>
					</div>
 		</div>			
  </td></tr>	
</table>
 </form>
  </div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp"><%=localizedMessages.getString("webmenu.instantiate.menu")%>&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>
</body>
</html>
