<!DOCTYPE html>
<%@ page import='org.tencompetence.widgetservice.beans.Whitelist' %>
<%Whitelist[] services = (Whitelist[])session.getAttribute("whitelist"); %>
<html>
<head>
<title>Server white list</title>
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
    		<div id="menu"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		<h3>White list</h3>	
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
	<p>The white list represents a list of remote content (web addresses) that a widget may want access to. By default this content may be blocked by your web browser, but by adding the web address of the content, it may be possible to bypass this restriction.</p>
	
		
		<table width="500" class="ui-widget ui-widget-content" align="center">  
		<tr class="ui-widget-header"><td>&nbsp;Existing entries</td></tr>
		<%for (int i=2; i<services.length; i++){%>
	  		<tr><td width="100%">
	  		<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>&nbsp;&nbsp;&nbsp;&nbsp;
	  				<%=services[i].getfUrl()%>
	  				<b class="rbottom">
						<b class="r4"></b>
						<b class="r3"></b>
						<b class="r2"></b>
						<b class="r1"></b>
					</b>
				</div>
	  		</td></tr>
	  	<%}%>
		</table>
		
</div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>

</body>
</html>
<% session.setAttribute("error_value", null); %>
<% session.setAttribute("message_value", null); %>