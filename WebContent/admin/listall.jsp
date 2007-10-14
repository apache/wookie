<%@ page import='org.tencompetence.widgetservice.beans.Widget,org.tencompetence.widgetservice.beans.WidgetType,java.util.Set,java.util.Hashtable;' %>
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Insert title here</title>
<script>
<!--
function confirmDefaultWidgetupdate(widgetId, widgetName, widgetType){
var answer = confirm("Are you sure you wish to change the default widget type...\n\n<" +widgetType+ ">\n\n...to this widget... \n\n<"+widgetName+"> ?");
	if (answer){
		window.location.href="./WidgetAdminServlet?operation=SET_DEFAULT&widgetId="+widgetId+"&widgetType="+widgetType;
	}
	else{}
}
//-->
</script>
</head>
<body>
<h2>Admin</h2>
<hr/>

<%
Widget[] widgets = (Widget[])session.getAttribute("widgets");
Hashtable widgetHashDefaults = (Hashtable)session.getAttribute("widget_defaults");
if(widgets!=null){
%>
<font face="arial">
<table border="1" bordercolor="blue">
<tr><td><b>Description</b></td><td><b>Url</b></td><td><b>Height</b></td><td><b>Width</b></td><td><b>Types</b></td></tr>
<%
		        for (int i = 0; i < widgets.length; i++) {
		        Widget widget = (Widget) widgets[i];
%>
		       
		        <tr><td><%=widget.getWidgetName()%></td>
		        <td><%=widget.getUrl()%></td>
		        <td><%=widget.getHeight()%></td>
		        <td><%=widget.getWidth()%></td>
		        <td>
		        <%
		        		        Set<WidgetType> types = widget.getWidgetTypes();
		        		        WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);		        
		        		        for(int j=0;j<widgetTypes.length;j++){	
		        		        	// first need to check the hash contains a key for this service
		        		        	if(widgetHashDefaults.containsKey(widgetTypes[j].getWidgetContext())){
		        		        		// if it does contain this service then is it this widget that is default?
		        	        	if(widgetHashDefaults.get(widgetTypes[j].getWidgetContext()).equals(widget.getId())){ //###
		        %><font color="red"><i><%=widgetTypes[j].getWidgetContext()%></i></font><br/><%
		        		}
		        		else{
				        	%><a href="javascript:confirmDefaultWidgetupdate('<%=widget.getId()%>','<%=widget.getWidgetName()%>','<%=widgetTypes[j].getWidgetContext()%>');"><%=widgetTypes[j].getWidgetContext()%></a><br/><%
			        	}
		        	}
		        	else{
			        	%><a href="javascript:confirmDefaultWidgetupdate('<%=widget.getId()%>','<%=widget.getWidgetName()%>','<%=widgetTypes[j].getWidgetContext()%>');"><%=widgetTypes[j].getWidgetContext()%></a><br/><%
		        	}
		        }
		        %></td></tr><%
		    }

%></table></font>
<%}%>
<hr/>
<p><a href="index.jsp">Menu</a></p>
</body>
</html>
