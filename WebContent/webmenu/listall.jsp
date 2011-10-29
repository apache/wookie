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
<%@ page import='org.apache.wookie.Messages,org.apache.wookie.beans.IWidget,java.util.ArrayList;' %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% Messages localizedMessages = (Messages)session.getAttribute(Messages.class.getName()); %>
<!DOCTYPE html>
<html>
<head>
<title><%=localizedMessages.getString("webmenu.listall.0")%></title>
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
    		<div id="menu"><a class="menulink" href="index.jsp"><%=localizedMessages.getString("webmenu.listall.1")%>&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		<h3><%=localizedMessages.getString("webmenu.listall.2")%></h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content">   
	

		
	<table align="center">	
	<tr align="center">
	<%	
	IWidget[] widgets = (IWidget[])request.getAttribute("widgets");
	ArrayList<String> guids = new ArrayList<String>();
	int count = -1;
		
	for(IWidget widget: widgets){						
		if (!widget.getGuid().equalsIgnoreCase("http://www.tencompetence.org/widgets/default/notsupported")){
			String guid = widget.getGuid();
			if(!guids.contains(guid)){
				guids.add(guid);
				++count;
				%>
				<td class="td-widget" align="center" valign="top">		
					<table border="0" class="wookie-widget" bordercolor="#ffffff" align="center">									 			    
						<tr align="center">
							<td height="120" align="center" valign="top">			
								<div align="center" class="wookie-icon-area"><img align="center" class="wookie-icon" src="<%=widget.getWidgetIconLocation()%>" width="75" height="75"></div>
							</td>
						</tr>
				    	<tr align="center"><td class="wookie-title" align="center" valign="top"><%=widget.getWidgetTitle()%></td></tr>
				    	<tr align="center"><td class="wookie-description" align="center" valign="top"><%=widget.getWidgetDescription()%></td></tr>
				    	<tr align="right"><td class="wookie-demo-link"><a href="WidgetWebMenuServlet?operation=DEMO_WIDGET&amp;widgetId=<%=widget.getGuid()%>">Demo</a></td></tr>
				    </table>			    
				 </td> 		 		   
				<%
				if (count==2){
					%></tr><tr align="center"><%
					count=-1;
				}				
			}			
		}				         			        
	}%>		
	</tr>	
	</table>								
			    
</div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp"><%=localizedMessages.getString("webmenu.listall.1")%>&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>
</body>
</html>
<% FlashMessage.clearErrorsAndMessages(session);%>
