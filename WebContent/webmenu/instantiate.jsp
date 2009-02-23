<%@ page import='org.tencompetence.widgetservice.beans.WidgetDefault' %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Wookie Widget Server</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../style.css" rel="stylesheet" type="text/css">
</head>
<%
WidgetDefault[] defaults = (WidgetDefault[])session.getAttribute("defaults");
%>
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
	<h3>Instantiate test</h3>

<body>
<p>
To instantiate a widget, you must supply a few parameters.
<form name="form1" method="post" action="/wookie/WidgetServiceServlet">
<br>
<table align="center" border="0">
<tr>  
  <td>userid</td> 
  <td><input name="userid" type="text" id="userid" value="testuser"></td>
</tr>
<tr>   
  <td>sharedDataKey</td>
  <td><input name="shareddatakey" type="text" id="shareddatakey" value="mysharedkey"></td>
</tr>
<tr>  
  <td>service type</td>
  <td>
  <div id="option_layer">
  <select name="servicetype" type="text" id="servicetype">
  <% 
  for (WidgetDefault def : defaults){ 
	  if(!def.getWidgetContext().equalsIgnoreCase("unsupported")){
  %>
  <option><%=def.getWidgetContext()%></option>
  <%} %>
  <%} %>
</select><img src="../shared/images/go.gif">
</div>
  
  </td>
</tr>
<tr>  
  <td colspan="2" align="center">
    <input type="submit" name="Submit" value="Submit"><input type="hidden" name="requestid" value="getwidget">
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