<%@ page import='org.tencompetence.widgetservice.beans.Widget,org.tencompetence.widgetservice.beans.WidgetType,java.util.ArrayList,java.util.Set,java.util.Enumeration,java.util.Hashtable;' %>
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
	<h3 class="section">Gallery</h3>	
	<p>			
	<table align="center">	
	<tr align="center">
	<%	
	Hashtable widgetsHash = (Hashtable)session.getAttribute("widgetsHash");
	ArrayList<String> guids = new ArrayList<String>();
	int count = -1;
	for(Enumeration e = widgetsHash.keys(); e.hasMoreElements();){				
		String widgetType = (String) e.nextElement();
		if (!widgetType.equalsIgnoreCase("unsupported")){
			Widget widget  = (Widget)widgetsHash.get(widgetType);
			String guid = widget.getGuid();
			if(!guids.contains(guid)){
				guids.add(guid);
				++count;
				%>
				<td class="td-widget" align="center" valign="top">		
					<table border="0" class="wookie-widget" bordercolor="#ffffff" align="center">									 			    
						<tr align="center">
							<td height="120" align="center" valign="top">			
								<div align="center" class="wookie-icon-area"><img align="center" class="wookie-icon" src="<%=widget.getWidgetIconLocation()%>" width="75" height="75"></div>
							</td>
						</tr>
				    	<tr align="center"><td class="wookie-title" align="center" valign="top"><%=widgetType%></td></tr>
				    	<tr align="center"><td class="wookie-description" align="center" valign="top"><%=widget.getWidgetDescription()%></td></tr>				    			    
				    </table>			    
				 </td> 		 		   
				<%
				if (count==2){
					%></tr><tr align="center"><%
					count=-1;
				}				
			}			
		}				         			        
	}%>		
	</tr>	
	</table>								
			    
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

