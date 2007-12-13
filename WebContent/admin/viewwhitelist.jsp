<%@ page import='org.tencompetence.widgetservice.beans.Whitelist' %>
<%Whitelist[] services = (Whitelist[])session.getAttribute("whitelist"); %>
<html>
<head>
<title>Server white list</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="style.css" rel="stylesheet" type="text/css">
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
	<h3>White list</h3>
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
	<p>The white list represents a list of remote content (web addresses) that a widget may want access to. By default this content may be blocked by your web browser, but by adding the web address of the content here, it may be possible to bypass this restriction.</p>
	
	<blockquote>	
		<table border="0" width="100%">
		<%for (int i=0; i<services.length; i++){%>
	  		<tr><td><h3><%=services[i].getfUrl()%></h3></td></tr>
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
<% session.setAttribute("message_value", null); %>