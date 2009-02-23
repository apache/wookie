<%@ page import='org.tencompetence.widgetservice.beans.WidgetService' %>
<% 
Integer dbkey = (Integer)session.getAttribute("dbkey");
String errors = (String)session.getAttribute("error_value");
String messages = (String)session.getAttribute("message_value");
Boolean hasValidated = (Boolean)session.getAttribute("hasValidated");
Boolean isMaxable = (Boolean)session.getAttribute("isMaxable");
Boolean closeWindow = (Boolean)session.getAttribute("closeWindow");
WidgetService[] services = (WidgetService[])session.getAttribute("services");
%>
<html>
<head>
<title>Add widget services close:<%=closeWindow %></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../style.css" rel="stylesheet" type="text/css">
</head>
<%if (closeWindow){ %>
<body onload="javascript:pro = window.open('/upload.htm','UploadStatus'); pro.close();">
<%}else{%>
<body>
<%} %>
<table width="800" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr align="center" valign="middle">
    <td height="40" align="center" valign="middle" class="legend">
        <h2>Widget Server Administration</h2></td>
  </tr>
    <tr>
    <td height="40" align="center" class="legend"><a href="index.jsp" class="hypercolourwhite">menu</a></td>
  </tr>
  <tr>  	
    <td valign="top" bgcolor="#FFFFFF" class="tbody">
	<h3>Add types</h3>	
	<%if(errors!=null){%>
      <p><img src="../shared/images/cancel.gif" width="16" height="16">There was 
        a problem encountered when trying to upload the package. <br>
        Cause: <font color=red> <%=errors%> </font> </p>
	<%}%>
	<%if(messages!=null){%>
	<p><img src="../shared/images/greentick.gif" width="16" height="16">
		<font color=green>
		<%=messages%>
		</font>
	</p>
	<%}%>



<%if(hasValidated){%>
<p>A widget can be seen as having several "contexts" or "types".  The same widget may be referred to as both a "discussion" and a "forum" type of tool, for example.  
When a client of the widget system asks for a widget, it will ask using one of these type names.  Please choose from the list below what type or types this widget can act as.
</p>
<form name="form1" method="post" action="WidgetAdminServlet">
  <input type="hidden" name="operation" value="SETWIDGETTYPES">
   <input type="hidden" name="dbkey" value="<%=dbkey%>">
	<table border=1 width="798">  
	<tr>
		<td colspan="2">Please choose the type/s for this widget</td>
	</tr>
	 <tr> 
		<td valign="top">Widget Types<br><i>(multiple allowed)</i></td>
	    <td>
      	<select name="widgetTypes" size="<%=services.length%>" multiple>      	
      	<%for (int i=1; i<services.length;i++){%>
  			<option value="<%=services[i].getServiceName()%>"><%=services[i].getServiceName()%></option>
  		<%}%>
		</select>
		</td>
    </tr>
    <tr>
    	<td>Can maximize?</td>
    	<td>
    	<%if(isMaxable){%>
    	<input type="checkbox" name="max" value="true" checked>
    	<%} else { %>
    	<input type="checkbox" name="max" value="true">
    	<%}%>
    	</td>
    </tr>
     <tr> 
		<td colspan="2"><div align="center"><input type="submit" name="Submit" value="Submit"></div>
        </td>
    </tr>
	</table>
	</form>
<%}%>




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