<%@ page import='org.tencompetence.widgetservice.beans.WidgetService' %>
<%String metadata = (String)session.getAttribute("metadata"); %>
<html>
<head>
<title>Widget Service Types</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="style.css" rel="stylesheet" type="text/css">
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