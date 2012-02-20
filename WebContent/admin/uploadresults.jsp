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
<% String errors = FlashMessage.getErrors(session, request);%>
<% String messages = FlashMessage.getMessages(session, request);%>
<% 
Integer dbkey = (Integer)request.getAttribute("dbkey");
Boolean hasValidated = (Boolean)request.getAttribute("hasValidated");
Boolean closeWindow = (Boolean)request.getAttribute("closeWindow");
%>
<!DOCTYPE html>
<html>
<head>
<title>Add widget services close:<%=closeWindow %></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link type="text/css" href="../shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />  
  <link type="text/css" href="../layout.css" rel="stylesheet" />
  <script type="text/javascript" src="../shared/js/jquery/jquery-1.3.2.min.js"></script>
  <script type="text/javascript" src="../shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>
</head>
<%if (closeWindow){ %>
<body onload="javascript:pro = window.open('/upload.htm','UploadStatus'); pro.close();">
<%}else{%>
<body>
<%} %>
    <div id="header">
 		<div id="banner">
    		<div style="float:left;">
    			<img style="margin: 8 8px;" border="0" src="../shared/images/furry_white.png">
    		</div>
    		<div id="menu"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		<h3>Add types</h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content">  
    
    
	<%if(errors!=null){%>
      <p id="error" title="<%=errors%>"><img src="../shared/images/cancel.gif" width="16" height="16">There was 
        a problem encountered when trying to upload the package. <br>
        Cause: <font color=red> <%=errors%> </font> </p>
	<%}%>
	<%if(messages!=null){%>
	  <p id="message"><img src="../shared/images/greentick.gif" width="16" height="16">
		<font color=green>
		<%=messages%>
		</font>
	</p>
	<%}%>



<%if(hasValidated){%>
<%}%>




</div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>

</body>
</html>
<% FlashMessage.clearErrorsAndMessages(session);%>
