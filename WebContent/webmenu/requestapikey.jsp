<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
    <td height="40" align="center" class="legend"><a href="index.jsp" class="hypercolourwhite">menu</a></td>
  </tr>
  <tr>  	
    <td valign="top" bgcolor="#FFFFFF" class="tbody">
	<h3>Request an API key</h3>

<body>
<p>
To request an API key, enter your email address and click Request
<form name="form1" method="post" action="WidgetWebMenuServlet">
<br>
<table align="center" border="0">
<tr>  
  <td>Email</td> 
  <td><input name="email" type="text" id="email" value=""></td>
</tr>
<tr>  
  <td colspan="2" align="center">
    <input type="submit" name="Submit" value="Request"><input type="hidden" name="operation" value="REQUESTAPIKEY">
  </td>  
</tr>
</table>
</form>
  <tr>
    <td height="40" align="center" class="legend"><a href="index.jsp" class="hypercolourwhite">menu</a></td>
  </tr>
</table>

</body>
</html>