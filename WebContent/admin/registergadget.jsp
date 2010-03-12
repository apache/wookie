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
<title>Add an OpenSocial gadget</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link type="text/css" href="/wookie/shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />  
<link type="text/css" href="../layout.css" rel="stylesheet" />

<div id="header">
 		<div id="banner">
    		<div style="float:left;">
    			<img style="margin: 8 8px;" border="0" src="../shared/images/furry_white.png">
    		</div>
    		<div id="menu"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
    	</div> 
    	<div id="pagetitle">
    		<h3>Add new widget</h3>
    	</div>
    	<!--  END HEADER -->
</div>
    
<div id="content"> 
	<p>Enter the URL of an OpenSocial gadget, and then click the 'Publish' button to add the gadget to Wookie.</p>
    <br>
	
<form method="post" action="/wookie/admin/WidgetAdminServlet?operation=REGISTERGADGET"> 	 
<table id="newwidget" class="ui-widget ui-widget-content" align="center">
		<thead>
			<tr class="ui-widget-header">
				<th>Enter the URL of the gadget (.xml) to register:</th>				
			</tr>
		</thead>
		<tbody>
			<tr>	
			 	<td><input type="text" size="100" id="url" name="url" class="ui-widget input ui-state-default ui-corner-all"></td> 	
			</tr>
			<tr>
			 	<td align="center"><input class="ui-button ui-state-default ui-corner-all" type="submit" name="Submit" value="Publish">
			</tr>
		</tbody>
	</table>
</form>
		 
	</div>

<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>

</body>
</html>

