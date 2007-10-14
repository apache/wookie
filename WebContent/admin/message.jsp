<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body>
<h2>Add new widget</h2>
<hr>
<% 
String errors = (String)session.getAttribute("error_value");
String messages = (String)session.getAttribute("message_value");
if(errors!=null){%>
	<font color=red>
	<%=errors%>
	</font>
<%}
%><br><%
if(messages!=null){%>
	<font color=green>
	<%=messages%>
	</font>
<%}%>
<p>&nbsp;</p>
<hr>
<p><a href="index.jsp">Menu</a></p>
</body>
</html>
