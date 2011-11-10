<!DOCTYPE html>
<%--
/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */
--%>
<%@ page import='org.apache.wookie.helpers.FlashMessage' %>
<%@ page import='org.apache.wookie.beans.IWidgetService' %>
<%IWidgetService[] services = (IWidgetService[])request.getAttribute("services"); %>
<html>
<head>
<title>Remove Service Type</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link type="text/css" href="../shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />  
  <link type="text/css" href="../layout.css" rel="stylesheet" />
  <script type="text/javascript" src="../shared/js/jquery/jquery-1.3.2.min.js"></script>
  <script type="text/javascript" src="../shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>
<script>
<!--
$(document).ready(function(){    
    $('a.opendialog').click(function() {
        var originalLink = this;  
        $("#confirm").dialog({
        	bgiframe: true,
            autoOpen: false,
            buttons: {
                "Yes": function() {
            	window.location.href = originalLink.href;
            },
                "No": function() { $(this).dialog("close"); }
            },
            resizable: false,                        
            modal: true,
            overlay: {
				backgroundColor: '#00000',
				opacity: 0.25
			},
    	});
    $("#confirm").dialog('open');
    return false;
	});       
  });
  
  function updateDialogText(serviceName){  	                
  	var x=document.getElementById("confirm");
  	var newText = "<span class=\"ui-icon ui-icon-alert\" style=\"float:left;\"></span>"; 
  	newText+=	"Are you sure you want to delete this service?...<br><br><b>";
  	newText+=	serviceName + "</b><br><br>";
  	newText+=	"By choosing 'OK', you will be removing all widget instances refering to this service and losing any current preferences/shared data for the widget stored by users. This operation is irreversible once you click 'OK'";
	x.innerHTML = newText;
	return;				
  }
//-->
</script>
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
    		<h3>Remove a service type</h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content"> 
	
	<% String errors = FlashMessage.getErrors(session, request);%>
	<% String messages = FlashMessage.getMessages(session, request);%>
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
<p>Here you can remove any existing service types.</p>
	<br>
	
		<table width="500" class="ui-widget ui-widget-content" align="center">
		<tr class="ui-widget-header"><td colspan="2">Services</td></tr>   
		<%for (int i=0; i<services.length; i++) if (!services[i].getServiceName().equals("unsupported")) {%>
	  		<tr><td>
	  		<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>
	  		&nbsp;&nbsp;&nbsp;&nbsp;<%=services[i].getServiceName()%>
	  		<b class="rbottom">
						<b class="r4"></b>
						<b class="r3"></b>
						<b class="r2"></b>
						<b class="r1"></b>
					</b>
				</div>
	  		</td>
	  		<td width="130">	
	  			<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>
					<font size="-2">		  			  		
				  		<a class="opendialog" href="./WidgetAdminServlet?operation=REMOVESERVICE&serviceId=<%=services[i].getId()%>"
				  		onclick="updateDialogText('<%=services[i].getServiceName()%>');">
				  		<img src="../shared/images/delete_1.gif" width="16" height="16" border="0">&nbsp;Delete this service</a>
				  		</font>
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
<div id="confirm" style="display:none;" title="Delete service"></div>
</body>
</html>
<% FlashMessage.clearErrorsAndMessages(session);%>
