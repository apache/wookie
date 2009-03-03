<%@ page import='org.tencompetence.widgetservice.beans.Widget,org.tencompetence.widgetservice.beans.WidgetType,java.util.Set,java.util.Hashtable;' %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../style.css" rel="stylesheet" type="text/css">
<title>Current Widgets</title>
<script>
<!--
function confirmDefaultWidgetupdate(widgetId, widgetName, widgetType){
var answer = confirm("Are you sure you wish to change the default widget type...\n\n<" +widgetType+ ">\n\n...to this widget... \n\n<"+widgetName+"> ?");
	if (answer){
		window.location.href="./WidgetAdminServlet?operation=SETDEFAULTWIDGET&widgetId="+widgetId+"&widgetType="+widgetType;
	}
	else{}
}

function confirmDeleteServiceType(widgetId, widgetName, widgetType){
var answer = confirm("Are you sure you wish to remove the service type...\n\n<" +widgetType+ ">\n\n...from this widget... \n\n<"+widgetName+"> ?");
	if (answer){
		window.location.href="./WidgetAdminServlet?operation=REMOVESINGLEWIDGETTYPE&widgetId="+widgetId+"&widgetType="+widgetType;
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
	<h3>Current Widgets</h3>
	<p>This page lists the widgets that have been imported into the Widget Server.  You can also change set the type of a widget here to be the the default one.
	The current default widget types are in <font color="red">Red</font>. If you wish to set a widget to be the default for another type, then click its <font color=blue>title</font> to set it.
	If you wish to remove one of the service types for a particular widget, then click the (<img src="../shared/images/cancel.gif" width="16" height="16">) icon next to it.
	If you wish to add another service type to an existing widget, then click the (Add new<img src="../shared/images/plus1.gif">) icon for that particular widget.</p>
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
	Hashtable widgetHashDefaults = (Hashtable)session.getAttribute("widget_defaults");
	if(widgets!=null){	
		for (int i = 1; i < widgets.length; i++) {
			Widget widget = (Widget) widgets[i];
	%>		 
			<table border=1 width="798" bordercolor="#ffffff">      
		        <tr><td width=70>Title</td><td><%=widget.getWidgetTitle()%></td></tr>
		        <tr><td>Description</td><td><%=widget.getWidgetDescription()%></td></tr>
		        <tr><td>Url</td><td><%=widget.getUrl()%></td></tr>
		        <tr><td>Guid</td><td><%=widget.getGuid()%></td></tr>
		        <tr><td>Author</td><td><%=widget.getWidgetAuthor()%></td></tr>
		        <tr><td>Height</td><td><%=widget.getHeight()%></td></tr>
		        <tr><td>Width</td><td><%=widget.getWidth()%></td></tr>
		        <tr><td valign="top">Types</td><td>
		        <%
		  	     Set<WidgetType> types = widget.getWidgetTypes();
		         WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);		        
		         for(int j=0;j<widgetTypes.length;++j){
		        	 	if(j!=0){
		        	 		%>,&nbsp;<%
		        	 	}
		             	// first need to check the hash contains a key for this service
		               	if(widgetHashDefaults.containsKey(widgetTypes[j].getWidgetContext())){
		               		// if it does contain this service then is it this widget that is default?
		        	        	if(widgetHashDefaults.get(widgetTypes[j].getWidgetContext()).equals(widget.getId())){ //###
		        					%>(<font color="red"><%=widgetTypes[j].getWidgetContext()%></font><a href="javascript:confirmDeleteServiceType('<%=widget.getId()%>','<%=widget.getWidgetTitle()%>','<%=widgetTypes[j].getWidgetContext()%>');"><img src="../shared/images/cancel.gif" width="16" height="16" border="0"></a>)<%
		        				}
		        				else{
				        			%>(<a href="javascript:confirmDefaultWidgetupdate('<%=widget.getId()%>','<%=widget.getWidgetTitle()%>','<%=widgetTypes[j].getWidgetContext()%>');"><%=widgetTypes[j].getWidgetContext()%></a><a href="javascript:confirmDeleteServiceType('<%=widget.getId()%>','<%=widget.getWidgetTitle()%>','<%=widgetTypes[j].getWidgetContext()%>');"><img src="../shared/images/cancel.gif" width="16" height="16" border="0"></a>)<%
			        			}
		        		}
		        		else{
			        		%>(<a href="javascript:confirmDefaultWidgetupdate('<%=widget.getId()%>','<%=widget.getWidgetTitle()%>','<%=widgetTypes[j].getWidgetContext()%>');"><%=widgetTypes[j].getWidgetContext()%></a><a href="javascript:confirmDeleteServiceType('<%=widget.getId()%>','<%=widget.getWidgetTitle()%>','<%=widgetTypes[j].getWidgetContext()%>');"><img src="../shared/images/cancel.gif" width="16" height="16" border="0"></a>)<%
		        		}	
		        }
		        %>
		        (<a href="/wookie/admin/WidgetAdminServlet?operation=REVISETYPES&dbkey=<%=widget.getId() %>">Add new<img border="0" src="../shared/images/plus1.gif"></a>)</td></tr>
		    </table>		        
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
<% session.setAttribute("widget_defaults", null);%>
<% session.setAttribute("widgets", null);%>