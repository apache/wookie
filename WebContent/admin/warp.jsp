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
<html>
<head>
<title>Widget Access Request Policies</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <link type="text/css" href="/wookie/shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />  
  <link type="text/css" href="../layout.css" rel="stylesheet" />
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-1.3.2.min.js"></script>
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>
</head>
<script>
	function grant(id){
		$.ajax({
          type: "PUT",
          url: '../warp/'+id+'?granted=true',
          datatype:'json',
          success: function(msg) {
           refresh();
          }
		});
	}
	function revoke(id){
		$.ajax({
          type: "PUT",
          url: '../warp/'+id+'?granted=false',
          datatype:'html',
          success: function(msg) {
            refresh();
          }
		});
	}
	function refresh(){
		$.get("../warp",
   		function(data){
   			$("#table").html(data);
   		});
	}
</script>
<body onload="refresh()">
     <div id="header">
 		<div id="banner">
    		<div style="float:left;">
    			<img style="margin: 8 8px;" border="0" src="../shared/images/furry_white.png">
    		</div>
    		<div id="menu"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		<h3>Widget Access Request Policies</h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content"> 
	<p>Here you can view, grant and revoke widget access request policies.</p>
	<br>
	<div id="table"></table>
	</div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>
</body>
</html>
</body>
</html>