<%@ page import='org.tencompetence.widgetservice.beans.Widget,org.tencompetence.widgetservice.beans.WidgetType,java.util.Set,java.util.Enumeration,java.util.Hashtable;' %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Widget Gallery</title>
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
	<h3>Gallery</h3>
	
	<p>
	
	
	
	<table  align="center">
	
	<%	
	Hashtable widgetsHash = (Hashtable)session.getAttribute("widgetsHash");
	int count = -1;
	for(Enumeration e = widgetsHash.keys(); e.hasMoreElements();){
		count++;
		if (count==0){
			%><tr><%			
		}		
		String widgetType = (String) e.nextElement();
		if (!widgetType.equalsIgnoreCase("unsupported")){
		%>
		<td class="widget-gallery" align="center" valign="top">
		
			<table border="0" class="wookie-widget" bordercolor="#ffffff" align="center">			
				<%Widget widget  = (Widget)widgetsHash.get(widgetType);%>		 			    
				<tr>
					<td height="120" align="center" valign="top">			
						<div align="center" class="wookie-icon-area"><img class="wookie-icon" src="<%=widget.getWidgetIconLocation()%>" width="75" height="75"/></div>
					</td>
				</tr>
		    	<tr><td class="wookie-title" align="center" valign="top"><%=widget.getWidgetTitle()%></td></tr>
		    	<tr><td class="wookie-description" align="left" valign="top"><%=widget.getWidgetDescription()%></td></tr>
		    	<tr><td class="wookie-choose" valign="top"><input type="button" name="widgettype" class="wookie-button" value="select widget" id=widgetid"></td></tr>		    
		    </table>	
		    
		 </td> 		 
		   
		<%}%>	
				 <%
		 if (count==2){
			%></tr><%
			count=-1;
		 }
		 %>        			        
	<%}%>	
	
	</tr>
	</table>								
	</p>
		    
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

