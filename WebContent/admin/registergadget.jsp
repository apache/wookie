<%@ page import='org.tencompetence.widgetservice.beans.WidgetService' %>
<%String metadata = (WidgetService[])session.getAttribute("metadata"); %>
<html>
<head>
<title>Widget Service Types</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="style.css" rel="stylesheet" type="text/css">
</head>
<body>

<%=metadata%>

</body>
</html>
<% session.setAttribute("error_value", null); %>
<% session.setAttribute("message_value", null);%>