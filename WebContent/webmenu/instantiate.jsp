<%@ page import='org.apache.wookie.Messages,org.apache.wookie.beans.WidgetDefault' %>
<% Messages localizedMessages = (Messages)session.getAttribute(Messages.class.getName()); %>
<% WidgetDefault[] defaults = (WidgetDefault[])session.getAttribute("defaults"); %>
<!DOCTYPE html>
<html>
<head>
<title><%=localizedMessages.getString("webmenu.instantiate.0")%></title>
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
    		<div id="menu"><a class="menulink" href="index.jsp"><%=localizedMessages.getString("webmenu.instantiate.1")%>&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		<h3><%=localizedMessages.getString("webmenu.instantiate.2")%></h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content">   	
	<p><%=localizedMessages.getString("webmenu.instantiate.3")%></p>
	
	<form name="form1" method="post" action="/wookie/WidgetServiceServlet">
	
		<table width="300" class="ui-widget ui-widget-content" align="center">  
		<tr class="ui-widget-header"><td colspan="2">&nbsp;<%=localizedMessages.getString("webmenu.instantiate.4")%></td></tr>		
		<tr><td>
		
		
		
		
				<div>
				
				
					<div id="nifty">
					<b class="rtop">
					<b class="r1"></b>
					<b class="r2"></b>
					<b class="r3"></b>
					<b class="r4"></b>
					</b>	
					<div><div class="adminLayerTitle"><%=localizedMessages.getString("webmenu.instantiate.5")%></div><div class="adminLayerDetail"><input class="ui-widget input ui-state-default ui-corner-all" name="api_key" type="text" id="api_key" value="TEST"></div></div>
					<b class="rbottom">
					<b class="r4"></b>
					<b class="r3"></b>
					<b class="r2"></b>
					<b class="r1"></b>
					</b>
					</div>
				
					<div id="spacer"></div>
					
					<div id="nifty">
					<b class="rtop">
					<b class="r1"></b>
					<b class="r2"></b>
					<b class="r3"></b>
					<b class="r4"></b>
					</b>	
					<div><div class="adminLayerTitle"><%=localizedMessages.getString("webmenu.instantiate.6")%></div><div class="adminLayerDetail"><input class="ui-widget input ui-state-default ui-corner-all" name="userid" type="text" id="userid" value="testuser"></div></div>
					<b class="rbottom">
					<b class="r4"></b>
					<b class="r3"></b>
					<b class="r2"></b>
					<b class="r1"></b>
					</b>
					</div>
				
					<div id="spacer"></div>
						
					<div id="nifty">
					<b class="rtop">
					<b class="r1"></b>
					<b class="r2"></b>
					<b class="r3"></b>
					<b class="r4"></b>
					</b>		
			    	<div><div class="adminLayerTitle" ><%=localizedMessages.getString("webmenu.instantiate.7")%></div><div class="adminLayerDetail"><input class="ui-widget input ui-state-default ui-corner-all" name="shareddatakey" type="text" id="shareddatakey" value="mysharedkey"></div></div>
			    	<b class="rbottom">
					<b class="r4"></b>
					<b class="r3"></b>
					<b class="r2"></b>
					<b class="r1"></b>
					</b>
					</div>
	
					<div id="spacer"></div>
						
					<div id="nifty">
					<b class="rtop">
					<b class="r1"></b>
					<b class="r2"></b>
					<b class="r3"></b>
					<b class="r4"></b>
					</b>		
			    		<div>
			    		<div class="adminLayerTitle"><%=localizedMessages.getString("webmenu.instantiate.8")%></div>
			    		<div class="adminLayerDetail">
				    		
								  <select class="ui-widget input ui-state-default ui-corner-all" name="servicetype"  id="servicetype">
								  <% 
								  for (WidgetDefault def : defaults){ 
									  if(!def.getWidgetContext().equalsIgnoreCase("unsupported")){
								  %>
								  <option><%=def.getWidgetContext()%></option>
								  <%} %>
								  <%} %>
								  </select>
							
			    		
			    		</div>
			    		</div>
			    	<b class="rbottom">
					<b class="r4"></b>
					<b class="r3"></b>
					<b class="r2"></b>
					<b class="r1"></b>
					</b>
					</div>
	
					<div id="spacer"></div>
						
					<div id="nifty">
					<b class="rtop">
					<b class="r1"></b>
					<b class="r2"></b>
					<b class="r3"></b>
					<b class="r4"></b>
					</b>		
			    	<div style="text-align:center"><input class="ui-button ui-state-default ui-corner-all" type="submit" name="Submit" value="<%=localizedMessages.getString("webmenu.instantiate.9")%>"><input type="hidden" name="requestid" value="getwidget"></div>
			    	<b class="rbottom">
					<b class="r4"></b>
					<b class="r3"></b>
					<b class="r2"></b>
					<b class="r1"></b>
					</b>
					</div>
 		</div>			
  </td></tr>	
</table>
 </form>
  </div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp"><%=localizedMessages.getString("webmenu.instantiate.1")%>&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>
</body>
</html>