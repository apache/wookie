<%@ page import='org.tencompetence.widgetservice.beans.WidgetService' %>
<%WidgetService[] services = (WidgetService[])session.getAttribute("services"); %>
<html>
<head>
<title>Remove Service Type</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="style.css" rel="stylesheet" type="text/css">
<script>
<!--
function confirmDeleteService(serviceId, serviceName){
var answer = confirm("Are you sure you want to delete this service?\n\n"+serviceName+"\n\nBy choosing 'OK', you will be removing all widget instances refering to this service and losing any current preferences/shared data for the widget stored by users. This operation is irreversible once you click 'OK'");
	if (answer){
		window.location.href="./WidgetAdminServlet?operation=REMOVESERVICE&serviceId="+serviceId;
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
	<h3>Widget Service Types</h3>
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
		<%for (int i=1; i<services.length; i++){%>
	  		<tr><td ><h3><%=services[i].getServiceName()%></h3></td><td width="200"><h3><a href="javascript: confirmDeleteService('<%=services[i].getId()%>','<%=services[i].getServiceName()%>');"><img src="../shared/images/cancel.gif" width="16" height="16" border="0">&nbsp;Delete this service</a></h3></td></tr>
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