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
<html>
<head>
<title>Policies</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link type="text/css" href="../shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />  
<link type="text/css" href="../layout.css" rel="stylesheet" />
<script type="text/javascript" src="../shared/js/jquery/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>
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
    		<h3>Policies</h3>	
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content">  
    
    <div id="errors">
	<% String errors = FlashMessage.getErrors(session, request);%>
	<%if(errors!=null){%>
      <p><img src="../shared/images/cancel.gif" width="16" height="16"><font color=red> <%=errors%> </font> </p>
	<%}%>
	</div>
	<div id="messages">
	<% String messages = FlashMessage.getMessages(session, request);%>
	<%if(messages!=null){%>
	   <p><img src="../shared/images/greentick.gif" width="16" height="16"><font color=green><%=messages%></font></p>
	<%}%>
	</div>
	<p>
	These policies determine whether the Wookie server-side proxy will allow a request by a widget for a resource from an origin.
	</p>
	
	<p>
	Each policy consists of:
	</p>
	
	<div style="font-size:12px;font-family: Verdana,Arial,sans-serif">
	<ul>
	 <li>a <em>scope</em> which may be either a wilcard (*) that applies to any widget, or the URI for a specific widget that the policy should
	only apply to</li>
	 <li>an <em>origin</em> which is the origin that the policy applies to, which is either a wildcard (*) that applies to any request, or an origin consisting of 
	 a scheme and a host. Note that the host can begin with a wildcard, which indicates the policy can apply to subdomains.</li>
	 <li>a <em>directive</em> which is one of ALLOW or DENY. 
	</ul>
	</div>
	
	<p>
	Where multiple policies apply to a given request, DENY takes precedence over ALLOW.
	</p>
	
	<div id="policylist"></div>
	
	<h3>Add a policy</h3>
	<p><input type="text" id="addPolicyField"><input type="button" value="add" onclick="addPolicy()"></input></p>
	
</div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>

<script type="text/javascript">

  function addPolicy(){
    var policy = $("#addPolicyField").val();
    $.ajax({
      type: 'POST',
      url: "../policies",
      data: policy,
      success: addOk,
      error: addError
   });
  }
  
  function removePolicy(obj){
    var policy = $(obj).parent().parent().children().append(" ").text(); 
    $.ajax({
      type: 'DELETE',
      url: "../policies/"+encodeURIComponent(policy),
      success: removeOk,
      error: removeError
    });
  }
  
  function refresh(){
      $.get("../policies", function(data){ 
      
        $("#policylist").html(data);
        $('.policy-line').each( 
          function(index, item){
           $(item).append("<td><input type=\"button\" value=\"remove\" onclick=\"removePolicy(this)\"></td>");
          }
        );
        
      });
  }
 
  function addOk(){
    $("#errors").empty;
    $("#errors").html("<p style='color:green'><img src='../shared/images/greentick.gif'>The policy was successfully added</p>");
    refresh();
  }
  
  function removeOk(){
    $("#errors").empty;
    $("#errors").html("<p style='color:green'><img src='../shared/images/greentick.gif'>The policy was successfully deleted</p>");
    refresh();
  }
  
  function addError(){
    $("#errors").empty;
    $("#errors").html("<p style='color:red'><img src='../shared/images/cancel.gif'>The policy could not be added</p>");
  }
  
  function removeError(){
    $("#errors").empty;
    $("#errors").html("<p style='color:red'><img src='../shared/images/cancel.gif'>The policy could not be removed</p>");
  }
  
  refresh();
  
</script>

</body>
</html>
<% FlashMessage.clearErrorsAndMessages(session);%>
