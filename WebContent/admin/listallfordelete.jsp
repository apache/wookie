<%@ page import='org.tencompetence.widgetservice.beans.Widget,org.tencompetence.widgetservice.beans.WidgetType,java.util.Set,java.util.Hashtable;' %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="style.css" rel="stylesheet" type="text/css">
<title>Current Widgets</title>
<script>
<!--
function confirmDeleteWidget(widgetId, widgetName){
var answer = confirm("Are you sure you want to delete this widget?\n\n"+widgetName+"\n\nBy choosing 'OK', you will be removing all widget instances refering to this widget and losing any current preferences/shared data for the widget stored by users. This operation is irreversible once you click 'OK'");
	if (answer){
		window.location.href="./WidgetAdminServlet?operation=REMOVEWIDGET&widgetId="+widgetId;
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
	<h3>Remove a Widget</h3>
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
	<%
	Widget[] widgets = (Widget[])session.getAttribute("widgets");	
	if(widgets!=null){
	%>			
	<%
		for (int i = 1; i < widgets.length; i++) {
			Widget widget = (Widget) widgets[i];
	%>		 
			<table border=1 width="798" bordercolor="#ffffff">      
		        <tr><td width=70>Title</td><td><%=widget.getWidgetTitle()%></td></tr>
		        <tr><td>Url</td><td><%=widget.getUrl()%></td></tr>
		        <tr><td>Guid</td><td><%=widget.getGuid()%></td></tr>		        
		        <tr><td colspan="2" align="center"><a href="javascript: confirmDeleteWidget('<%=widget.getId()%>','<%=widget.getWidgetTitle()%>');"><img src="../shared/images/cancel.gif" width="16" height="16" border="0">&nbsp;Delete this widget</a></td></tr>
		     </table>
		     <br>
		     
	<%}%>
<%}%>
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