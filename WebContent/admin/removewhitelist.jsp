<%@ page import='org.tencompetence.widgetservice.beans.Whitelist' %>
<%Whitelist[] list = (Whitelist[])session.getAttribute("whitelist"); %>
<html>
<head>
<title>Remove White list entry</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../style.css" rel="stylesheet" type="text/css">
<script>
<!--
function confirmDeleteEntry(entryId, entryName){
var answer = confirm("Are you sure you want to delete this entry?\n\n" + entryName);
	if (answer){
		window.location.href="./WidgetAdminServlet?operation=REMOVEWHITELISTENTRY&entryId="+entryId;
	}
	else{}
}
//-->
</script>
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
	<h3>Remove White list entry</h3>
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
	<br>
	<blockquote>	
		<table border="0" width="100%">
		<%for (int i=1; i<list.length; i++){%>
	  		<tr><td ><h3><%=list[i].getfUrl()%></h3></td><td width="200"><h3><a href="javascript: confirmDeleteEntry('<%=list[i].getId()%>','<%=list[i].getfUrl()%>');"><img src="../shared/images/cancel.gif" width="16" height="16" border="0">&nbsp;Delete this entry</a></h3></td></tr>
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