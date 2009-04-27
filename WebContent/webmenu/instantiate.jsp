<%@ page import='org.tencompetence.widgetservice.beans.WidgetDefault' %>
<!DOCTYPE html>
<html>
<head>
<title>Wookie Widget Server</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
 <link type="text/css" href="/wookie/shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />    
  <link type="text/css" href="../layout.css" rel="stylesheet" />
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-1.3.2.min.js"></script>
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>
</head>
<%WidgetDefault[] defaults = (WidgetDefault[])session.getAttribute("defaults");%>
<body>
   <div id="header">
 		<div id="banner">
    		<div style="float:left;">
    			<img style="margin: 8 8px;" border="0" src="../shared/images/furry_white.png">
    		</div>
    		<div id="menu"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		<h3>Instantiate test</h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content">   	
	<p>To instantiate a widget, you must supply a few parameters.</p>
	
	<form name="form1" method="post" action="/wookie/WidgetServiceServlet">
	
		<table width="300" class="ui-widget ui-widget-content" align="center">  
		<tr class="ui-widget-header"><td colspan="2">&nbsp;Request instance</td></tr>		
		<tr><td>
		
		
		
		
				<div>
				
				
					<div id="nifty">
					<b class="rtop">
					<b class="r1"></b>
					<b class="r2"></b>
					<b class="r3"></b>
					<b class="r4"></b>
					</b>	
					<div><div class="adminLayerTitle">API key</div><div class="adminLayerDetail"><input class="ui-widget input ui-state-default ui-corner-all" name="api_key" type="text" id="api_key" value="TEST"></div></div>
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
					<div><div class="adminLayerTitle">userid</div><div class="adminLayerDetail"><input class="ui-widget input ui-state-default ui-corner-all" name="userid" type="text" id="userid" value="testuser"></div></div>
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
			    	<div><div class="adminLayerTitle" >sharedDataKey</div><div class="adminLayerDetail"><input class="ui-widget input ui-state-default ui-corner-all" name="shareddatakey" type="text" id="shareddatakey" value="mysharedkey"></div></div>
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
			    		<div class="adminLayerTitle">service type</div>
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
			    	<div style="text-align:center"><input class="ui-button ui-state-default ui-corner-all" type="submit" name="Submit" value="Submit"><input type="hidden" name="requestid" value="getwidget"></div>
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
	<div style="text-align:right"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>
</body>
</html>