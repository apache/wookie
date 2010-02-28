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
<%@ page
	import='org.apache.wookie.Messages,org.apache.wookie.beans.Widget,org.apache.wookie.beans.WidgetType,java.util.ArrayList,java.util.Set,java.util.Enumeration,java.util.Hashtable;'%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	Messages localizedMessages = (Messages) session
			.getAttribute(Messages.class.getName());
%>
<!DOCTYPE html>
<html>
<head>
<title><%=localizedMessages.getString("webmenu.demo.title")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link type="text/css"
	href="/wookie/shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css"
	rel="stylesheet" />
<link type="text/css" href="../layout.css" rel="stylesheet" />
<script type="text/javascript"
	src="/wookie/shared/js/jquery/jquery-1.3.2.min.js"></script>
<script type="text/javascript"
	src="/wookie/shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>
</head>
<body>
<div id="header">
<div id="banner">
<div style="float: left;"><img style="margin: 8 8px;" border="0"
	src="../shared/images/furry_white.png"></div>
    <div id="menu"><a class="menulink" href="index.jsp"><%=localizedMessages.getString("webmenu.home.menu")%>&nbsp;<img
	border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>
<div id="pagetitle">
<h3><%=localizedMessages.getString("webmenu.demo.title")%></h3>
</div>
<!--  END HEADER --></div>

<div id="content">
  <p>Below are two instances of the widget. They are both instantiated with anonymous users, 
  a well implemented widget has sensible fallback behaviour for unknown users. This page
  allows you to both see the widgets in operation and, during development, you can test
  them here too.</p>
  
  <p>Remember, not all widgets are designed to be multi-user and thus you may not see any
  interaction between the two widget instances.</p>
  
  <h1>IMPORTANT NOTE</h1>
  
  <p>The code for this page is currently in development and at the time of writing both
  widgets instances are owned by the same user.</p>
  
  <table>
      <tr>
        <td>
          <h2>Widget for user Test User</h2>
		  <iframe width="<%= request.getAttribute("widgetWidth") %>" height="<%= request.getAttribute("widgetHeight") %>" src="<%= request.getAttribute("widgetURL") %>?idkey=<%= request.getParameter("idkey") %>&proxy=<%=request.getAttribute("proxy")%>">
		  </iframe>
		</td>
        <td>
          <h2>Widget for user Test User</h2>
		  <iframe width="<%= request.getAttribute("widgetWidth") %>" height="<%= request.getAttribute("widgetHeight") %>" src="<%= request.getAttribute("widgetURL") %>?idkey=<%= request.getParameter("idkey") %>&proxy=<%=request.getAttribute("proxy")%>">
		  </iframe>
        </td>
      </tr>
  </table>
</div>
 
<div id="footer">
<div style="text-align: right"><a class="menulink"
	href="index.jsp"><%=localizedMessages.getString("webmenu.home.menu")%>&nbsp;<img
	border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>
</body>
</html>
<%
	session.setAttribute("error_value", null);
%>
<%
	session.setAttribute("message_value", null);
%>

