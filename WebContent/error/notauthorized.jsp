<%
response.addHeader("WWW-Authenticate", "BASIC realm=\"wookie\"");
response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
%>
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

<body>
   <div id="header">
 		<div id="banner">
    		<div style="float:left;">
    			<img style="margin: 8 8px;" border="0" src="../shared/images/furry_white.png">
    		</div>
    		<div id="menu"><a class="menulink" href="../webmenu/index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		<h3>Invalid username/password</h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content">  
	
<p>
<img src="../shared/images/cancel.gif" width="16" height="16"><font color=red> You are not authorized to enter this area.</font>  <BR><br>Click <a href="../webmenu/index.jsp">here</a> to continue.
</p>

 
</div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="../webmenu/index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>
</body>
</html>