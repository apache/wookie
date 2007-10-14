<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<h2>Admin</h2>
<hr>
<% String errors = (String)session.getAttribute("error_value");
if(errors!=null){
%><font color=red>
<%=errors%>
</font>
<%}%>
<p>1. <a href="WidgetAdminServlet?operation=LIST">List Existing Widgets</a></p>
<p>2. <a href="WidgetAdminServlet?operation=RETRIEVE">Add new widget to Server</a></p>
<p>3. <a href="WidgetAdminServlet?operation=ATEST">This should cause a error response</a></p>
<hr/>
<p>&nbsp;</p>
</body>
</html>
