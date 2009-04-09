<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% String version = (String)session.getAttribute("version");
// "version" is lazy loaded and will be empty until a request is made to the server
if (version==null){
	version="";
}
%>
<html>
<head>
<title>Wookie Widget Server <%=version%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../style.css" rel="stylesheet" type="text/css">
</head>

<body>
<table width="800" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr align="center" valign="middle">
    <td height="40" align="center" valign="middle" class="legend">
    <img src="../shared/images/furry_white.png">       
  </tr>
  <tr>
    <td height="40" align="center" class="legend"><a href="index.jsp" class="hypercolourwhite">menu</a></td>
  </tr>
  <tr>  	
    <td valign="top" bgcolor="#FFFFFF" class="tbody">
	<h3>Main Menu</h3>
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
	<h4>Options</h4>
	<p><a href="WidgetWebMenuServlet?operation=LISTWIDGETS" class="hypercolour"><img src="../shared/images/view.gif" width="16" height="16" border="0">&nbsp;View Widget Gallery</a></p>	   	
    <p><a href="../admin"><img src="../shared/images/unlock.gif" width="16" height="16" border="0">&nbsp;Login as administrator</a></p>
	<p><a href="WidgetWebMenuServlet?operation=INSTANTIATE"><img src="../shared/images/run.gif" width="16" height="16" border="0">&nbsp;Instantiate a widget</a><br></p>    
	<p><a href="requestapikey.jsp"><img src="../shared/images/mail.png" width="16" height="16" border="0">&nbsp;Request an API key for your application</a></p>
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

