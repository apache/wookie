<%@ page import='org.tencompetence.widgetservice.beans.WidgetService' %>
<%WidgetService[] services = (WidgetService[])session.getAttribute("services"); %>
<html>
<head>
<title>Widget Service Types</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../style.css" rel="stylesheet" type="text/css">
<script>
<!--
var jServices = new Array();

<%for(int i=0; i<services.length; i++){%>
	jServices[<%=i%>]="<%=services[i].getServiceName()%>";
<%}%>

function serviceAlreadyExist(sValue){
	for(var x=0;x<jServices.length;x++){	
		if(sValue.toLowerCase() == jServices[x].toLowerCase()){
			return true;
		}
	}
	return false;
}

function checkServiceValue(){
	if (document.addnewform.newservice.value.length < 1){
        alert("New value cannot be empty");
        return;      
 	}
 	
	if(serviceAlreadyExist(document.addnewform.newservice.value)){
		alert("This type of service already exists, please choose another name/title.");
	}
  	else {  	
		document.addnewform.submit();	
  	}	
}


//-->
</script>
</head>
<body>
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
	<h3>Add new Service Type</h3>
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

	<p>
	Here you can add a new service type for the widget server. 
	</p>
	<br>
		<table border="1" width="798">
		<tr><td width="50%" align="center">Existing types</td>
		<td align="center">Add new type</td></tr>
		<tr><td>
			<table border="0">
			<%for (int i=1; i<services.length; i++){%>
		  		<tr><td><h3><%=services[i].getServiceName()%></h3></td></tr>
	  		<%}%>
			</table>		
		</td>
		<td align="center">
			<form method="post" name="addnewform" action="/wookie/admin/WidgetAdminServlet?operation=ADDNEWSERVICE" >
				<input type="text" name="newservice">
				<input type="button" name="Submit" value="add" onClick="checkServiceValue()">
			</form>		
		</td>
		</tr>
		</table>
	
	
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