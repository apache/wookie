<%
response.addHeader("WWW-Authenticate", "BASIC realm=\"wookie\"");
response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
%>
<html>
<head>
<title>Wookie Widget Server</title>
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
    <td height="40" align="center" class="legend"><a href="../webmenu/index.jsp" class="hypercolourwhite">menu</a></td>
  </tr>
  <tr>  	
    <td valign="top" bgcolor="#FFFFFF" class="tbody">
	<h3>Invalid username/password</h3>

<body>
<p>
<img src="../shared/images/cancel.gif" width="16" height="16"><font color=red> You are not authorized to enter this area.</font>  <BR><br>Click <a href="../webmenu/index.jsp">here</a> to continue.

  <tr>
    <td height="40" align="center" class="legend"><a href="../webmenu/index.jsp" class="hypercolourwhite">menu</a></td>
  </tr>
</table>

</body>
</html>