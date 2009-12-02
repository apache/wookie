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
<%String metadata = (String)session.getAttribute("metadata"); %>
<html>
<head>
<title>Widget Service Types</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../style.css" rel="stylesheet" type="text/css">
</head>
<body>


<%if(metadata!=null){%>
    <%=metadata%>
<%} else {%>
    <form method="post" action="/wookie/admin/WidgetAdminServlet?operation=REGISTERGADGET">Enter URL of Gadget: <input type="text" size="255" id="url" name="url"></form>
<%}%>


</body>
</html>
<% session.setAttribute("metadata", null); %>