<%@ page import='org.tencompetence.widgetservice.Messages,org.tencompetence.widgetservice.beans.Widget,org.tencompetence.widgetservice.beans.WidgetType,java.util.ArrayList,java.util.Set,java.util.Enumeration,java.util.Hashtable;' %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% Messages localizedMessages = (Messages)session.getAttribute(Messages.class.getName()); %>
<!DOCTYPE html>
<html>
<head>
<title><%=localizedMessages.getString("webmenu.listall.0")%></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
 <link type="text/css" href="/wookie/shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />    
  <link type="text/css" href="../layout.css" rel="stylesheet" />
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-1.3.2.min.js"></script>
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>
</head>
<body>
    <div id="header">
 		<div id="banner">
    		<div style="float:left;">
    			<img style="margin: 8 8px;" border="0" src="../shared/images/furry_white.png">
    		</div>
    		<div id="menu"><a class="menulink" href="index.jsp"><%=localizedMessages.getString("webmenu.listall.1")%>&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		<h3><%=localizedMessages.getString("webmenu.listall.2")%></h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content">   
	

		
	<table align="center">	
	<tr align="center">
	<%	
	Hashtable widgetsHash = (Hashtable)session.getAttribute("widgetsHash");
	ArrayList<String> guids = new ArrayList<String>();
	int count = -1;
	for(Enumeration e = widgetsHash.keys(); e.hasMoreElements();){				
		String widgetType = (String) e.nextElement();		
		if (!widgetType.equalsIgnoreCase("http://www.tencompetence.org/widgets/default/notsupported")){
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
				    	<tr align="center"><td class="wookie-title" align="center" valign="top"><%=widget.getWidgetTitle()%></td></tr>
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
			    
</div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp"><%=localizedMessages.getString("webmenu.listall.1")%>&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>
</body>
</html>
<% session.setAttribute("error_value", null); %>
<% session.setAttribute("message_value", null);%>

