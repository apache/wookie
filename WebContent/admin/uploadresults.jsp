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
<%@ page import='org.apache.wookie.beans.WidgetService' %>
<% 
Integer dbkey = (Integer)session.getAttribute("dbkey");
String errors = (String)session.getAttribute("error_value");
String messages = (String)session.getAttribute("message_value");
Boolean hasValidated = (Boolean)session.getAttribute("hasValidated");
Boolean isMaxable = (Boolean)session.getAttribute("isMaxable");
Boolean closeWindow = (Boolean)session.getAttribute("closeWindow");
WidgetService[] services = (WidgetService[])session.getAttribute("services");
%>
<!DOCTYPE html>
<html>
<head>
<title>Add widget services close:<%=closeWindow %></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link type="text/css" href="/wookie/shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />  
  <link type="text/css" href="../layout.css" rel="stylesheet" />
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-1.3.2.min.js"></script>
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>
</head>
<%if (closeWindow){ %>
<body onload="javascript:pro = window.open('/upload.htm','UploadStatus'); pro.close();">
<%}else{%>
<body>
<%} %>
    <div id="header">
 		<div id="banner">
    		<div style="float:left;">
    			<img style="margin: 8 8px;" border="0" src="../shared/images/furry_white.png">
    		</div>
    		<div id="menu"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		<h3>Add types</h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content">  
    
    
	<%if(errors!=null){%>
      <p id="error" title="<%=errors%>"><img src="../shared/images/cancel.gif" width="16" height="16">There was 
        a problem encountered when trying to upload the package. <br>
        Cause: <font color=red> <%=errors%> </font> </p>
	<%}%>
	<%if(messages!=null){%>
	  <p id="message"><img src="../shared/images/greentick.gif" width="16" height="16">
		<font color=green>
		<%=messages%>
		</font>
	</p>
	<%}%>



<%if(hasValidated){%>
<p>A widget can be seen as having several "contexts" or "types".  The same widget may be referred to as both a "discussion" and a "forum" type of tool, for example.  
When a client of the widget system asks for a widget, it will ask using one of these type names.  Please choose from the list below what type or types this widget can act as.
</p>
<form name="form1" method="post" action="WidgetAdminServlet">
  <input type="hidden" name="operation" value="SETWIDGETTYPES">
   <input type="hidden" name="dbkey" value="<%=dbkey%>">
	<table width="500" class="ui-widget ui-widget-content" align="center">  
	<tr class="ui-widget-header">
		<td colspan="2">Please choose the type/s for this widget</td>
	</tr>
	 <tr> 
		<td valign="top">Widget Types<br><i>(multiple allowed)</i></td>
	    <td>
      	<select name="widgetTypes" size="<%=services.length%>" multiple>      	
      	<%for (int i=1; i<services.length;i++){%>
  			<option value="<%=services[i].getServiceName()%>"><%=services[i].getServiceName()%></option>
  		<%}%>
		</select>
		</td>
    </tr>
    <tr>
    	<td>Can maximize?</td>
    	<td>
    	<%if(isMaxable){%>
    	<input type="checkbox" name="max" value="true" checked  class="ui-button ui-state-default ui-corner-all">
    	<%} else { %>
    	<input type="checkbox" name="max" value="true"  class="ui-button ui-state-default ui-corner-all">
    	<%}%>
    	</td>
    </tr>
     <tr> 
		<td colspan="2"><div align="center"><input  class="ui-button ui-state-default ui-corner-all" type="submit" name="Submit" value="Submit"></div>
        </td>
    </tr>
	</table>
	</form>
<%}%>




</div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>

</body>
</html>
<% session.setAttribute("error_value", null); %>
<% session.setAttribute("message_value", null);%>