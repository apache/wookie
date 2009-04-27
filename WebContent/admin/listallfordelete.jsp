<%@ page import='org.tencompetence.widgetservice.beans.Widget,org.tencompetence.widgetservice.beans.WidgetType,java.util.Set,java.util.Hashtable;' %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link type="text/css" href="/wookie/shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />  
  <link type="text/css" href="../layout.css" rel="stylesheet" />
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-1.3.2.min.js"></script>
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>
<title>Current Widgets</title>
<script>
<!--
$(document).ready(function(){
    $("#accordion").accordion();
    $('a.opendialog').click(function() {
        var originalLink = this;  
        $("#confirm").dialog({
        	bgiframe: true,
            autoOpen: false,
            buttons: {
                "Yes": function() {
            	window.location.href = originalLink.href;
            },
                "No": function() { $(this).dialog("close"); }
            },
            resizable: false,                        
            modal: true,
            overlay: {
				backgroundColor: '#00000',
				opacity: 0.25
			},
    	});
    $("#confirm").dialog('open');
    return false;
	});       
  });
  
  function updateDialogText(widgetName){                
  	var x=document.getElementById("confirm");
  	var newText = "<span class=\"ui-icon ui-icon-alert\" style=\"float:left;\"></span>"; 
  	newText+=	"Are you sure you want to delete this widget?...<br><br><b>";
  	newText+=	widgetName + "</b><br><br>";
  	newText+=	"By choosing 'OK', you will be removing all widget instances refering to this widget and losing any current preferences/shared data for the widget stored by users. This operation is irreversible once you click 'OK'";
	x.innerHTML = newText;
	return;				
  }
//-->
</script>
</head>
<body>
     <div id="header">
 		<div id="banner">
    		<div style="float:left;">
    			<img style="margin: 8 8px;" border="0" src="../shared/images/furry_white.png">
    		</div>
    		<div id="menu"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		<h3>Remove a Widget</h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content">     
	
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
			<table width="500" class="ui-widget ui-widget-content" align="center">      
		        <tr class="ui-widget-header"><td colspan="2"><img height="16" width="16" border="0" src="<%=widget.getWidgetIconLocation()%>"/>&nbsp;<%=widget.getWidgetTitle()%></td></tr>
		        <tr><td>Description</td><td><%=widget.getWidgetDescription()%></td></tr>
		        <tr><td>Url</td><td><%=widget.getUrl()%></td></tr>
		        <tr><td>Guid</td><td><%=widget.getGuid()%></td></tr>
		        <tr><td>Author</td><td><%=widget.getWidgetAuthor()%></td></tr>		        
		        <tr><td colspan="2" align="center">
					<a class="opendialog" href="./WidgetAdminServlet?operation=REMOVEWIDGET&widgetId=<%=widget.getId()%>" 
					onclick="updateDialogText('<%=widget.getWidgetTitle()%>');">
					<img src="../shared/images/delete_1.gif" width="16" height="16" border="0">&nbsp;Delete this widget
					</a>
		        </td></tr>
		     </table>
		     <br>
		     
	<%}%>
<%}%>
</div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>
<div id="confirm" style="display:none;" title="Delete widget"></div>
</body>
</html>
<% session.setAttribute("error_value", null); %>
<% session.setAttribute("message_value", null);%>