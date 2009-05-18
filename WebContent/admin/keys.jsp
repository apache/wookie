<%@ page import='org.tencompetence.widgetservice.beans.ApiKey' %>
<% ApiKey[] keys = (ApiKey[])session.getAttribute("keys"); %>
<html>
<head>
<title>Widget Service Types</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../style.css" rel="stylesheet" type="text/css">
</head>
<body>
<table width="800" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr align="center" valign="middle">
    <td height="40" align="center" valign="middle" class="legend">
        <h2>Widget Server Administration</h2></td>
  </tr>
    <tr>
    <td height="40" align="center" class="legend"><a href="index.jsp" class="hypercolourwhite">menu</a></td>
  </tr>
  <tr>  	
    <td valign="top" bgcolor="#FFFFFF" class="tbody">
	<h3>API Keys</h3>
	<% String errors = (String)session.getAttribute("error_value");%>
	<% String messages = (String)session.getAttribute("message_value");%>
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
	<p>Below are all currently valid API keys and who requested them.</p>
	<br>
	<blockquote>	
		<table border="0" width="100%">
		<%if(keys!=null){%>
		<%for (int i=1; i<keys.length; i++){%>
	  		<tr>
	  		  <td width="100%"><h3><%=keys[i].getValue()%></h3></td>
	  		  <td><h3><%=keys[i].getEmail()%></h3></td>
	  		  <td width="200"><h3><a href="WidgetAdminServlet?operation=REVOKEAPIKEY&key=<%=keys[i].getValue()%>"><img src="../shared/images/cancel.gif" width="16" height="16" border="0">&nbsp;Revoke this key</a></h3></td>
	  	   </tr>
	  	<%}%>
	  	<%}%>
		</table>
	</blockquote>
</td>
  </tr>
  <tr>
    <td height="40" align="center" class="legend"><a href="index.jsp" class="hypercolourwhite">menu</a></td>
  </tr>
</table>

</body>
</html>
<% session.setAttribute("error_value", null); %>
<% session.setAttribute("message_value", null);%>