<%@ page import='org.tencompetence.widgetservice.beans.Widget,java.util.Set;' %>
<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body>
<h2>Add new widget</h2>
<hr>
<% String[] services = (String[])session.getAttribute("services");
if(services!=null){
%>

<form name="form1" method="post" action="WidgetAdminServlet">
  <input type="hidden" name="operation" value="ADD">
  <table width="500" border="1" cellpadding="2" cellspacing="2" bordercolor="#333333">
    <tr> 
      <td>Description</td>
      <td><input type="text" name="description" size="50" maxlength="100"></td>
    </tr>
    <tr> 
      <td>URL</td>
      <td><input type="text" name="url" size="60" maxlength="200"></td>
    </tr>
    <tr> 
		<td valign="top">Widget Types<br><i>(multiple allowed)</i></td>
	    <td>
      	<select name="widgetTypes" size="<%=services.length%>" multiple>
      	<%for (String service : services){%>
  			<option value="<%=service%>"><%=service%></option>
  		<%}%>
		</select>
		</td>
    </tr>
    <tr> 
      <td>Height</td>
      <td><input type="text" name="height" size="4" maxlength="4"></td>
    </tr>
    <tr> 
      <td>Width</td>
      <td><input type="text" name="width" size="4" maxlength="4"></td>
    </tr>
    <tr> 
      <td colspan="2"><div align="center">
          <input type="submit" name="Submit" value="Submit">
        </div></td>
    </tr>
  </table>
</form>
<%}
else{%>
<h3>No services Defined!</h3>
<%}%>
<hr>
<p><a href="index.jsp">Menu</a></p>
</body>
</html>
