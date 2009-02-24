<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% String version = (String)session.getAttribute("version");
if (version == null){
	version="";
}
%>
<html>
<head>
<title>Widget Server Administration <%=version%></title>
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
    <td height="40" align="center" class="legend"></td>
  </tr>
  <tr>  	
    <td valign="top" bgcolor="#FFFFFF" class="tbody">
	<h3>Administrator Menu</h3>
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
	<h4>Widgets</h4>
	<p><a href="WidgetAdminServlet?operation=LISTWIDGETS&param=list" class="hypercolour"><img src="../shared/images/view.gif" width="16" height="16" border="0">&nbsp;View existing widgets</a></p>	
    <p><a href="selectupload.htm" class="hypercolour"><img src="../shared/images/plus1.gif" width="16" height="16" border="0">&nbsp;Add new widget</a></p>	
    <p><a href="registergadget.jsp" class="hypercolour"><img src="../shared/images/plus1.gif" width="16" height="16" border="0">&nbsp;Add new Google Gadget/OpenSocial app</a></p>
    <p><a href="WidgetAdminServlet?operation=LISTWIDGETS&param=remove" class="hypercolour"><img src="../shared/images/cancel.gif" width="16" height="16" border="0">&nbsp;Remove widget from system</a></p>
	<h4>Service types</h4>
	<p><a href="WidgetAdminServlet?operation=LISTSERVICES&param=list" class="hypercolour"><img src="../shared/images/view.gif" width="16" height="16" border="0">&nbsp;View existing widget services</a></p>
	<p><a href="WidgetAdminServlet?operation=LISTSERVICES&param=add" class="hypercolour"><img src="../shared/images/plus1.gif" width="16" height="16" border="0">&nbsp;Add new widget service</a></p>
    <p><a href="WidgetAdminServlet?operation=LISTSERVICES&param=remove" class="hypercolour"><img src="../shared/images/cancel.gif" width="16" height="16" border="0">&nbsp;Remove service from system</a></p>
    <h4>White list</h4>
    <p><a href="WidgetAdminServlet?operation=VIEWWHITELIST&param=list" class="hypercolour"><img src="../shared/images/view.gif" width="16" height="16" border="0">&nbsp;View white list</a></p>
    <p><a href="WidgetAdminServlet?operation=VIEWWHITELIST&param=add" class="hypercolour"><img src="../shared/images/plus1.gif" width="16" height="16" border="0">&nbsp;Add entry to white list</a></p>
    <p><a href="WidgetAdminServlet?operation=VIEWWHITELIST&param=remove" class="hypercolour"><img src="../shared/images/cancel.gif" width="16" height="16" border="0">&nbsp;Remove entry from white list</a></p>
	<h4>Other</h4>
	<p><a href="../webmenu/index.jsp" class="hypercolour"><img src="../shared/images/go.gif" width="16" height="16" border="0">&nbsp;Back to main menu</a></p>
	<p></p>
	</td>
  </tr>
  <tr>
    <td height="40" align="center" class="legend"></td>
  </tr>
</table>

</body>
</html>
<% session.setAttribute("error_value", null); %>
<% session.setAttribute("message_value", null);%>

