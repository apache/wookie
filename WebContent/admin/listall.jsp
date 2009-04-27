<!DOCTYPE html>
<%@ page import='org.tencompetence.widgetservice.beans.Widget,org.tencompetence.widgetservice.beans.WidgetType,java.util.Set,java.util.Hashtable;' %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% String errors = (String)session.getAttribute("error_value");%>
<% String messages = (String)session.getAttribute("message_value");%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Current Widgets</title>   
  <link type="text/css" href="/wookie/shared/js/jquery/themes/redmond/jquery-ui-1.7.1.custom.css" rel="stylesheet" />  
  <link type="text/css" href="../layout.css" rel="stylesheet" />
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-1.3.2.min.js"></script>
  <script type="text/javascript" src="/wookie/shared/js/jquery/jquery-ui-1.7.custom.min.js"></script>

  <script type="text/javascript">
  $(document).ready(function(){
    $("#accordion").accordion();
    $('a.opendialog').click(function() {        
        var originalLink = this;  
        
        $("#confirm").dialog({
        	bgiframe: true,
            autoOpen: false,
            buttons: {
                "Yes": function() {
        		alert("1@"+originalLink.href);
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
  
  function updateDialogText(widgetName, widgetType, requestType){                
  	var x=document.getElementById("confirm");
  	var newText = "<span class=\"ui-icon ui-icon-alert\" style=\"float:left;\"></span>"; 
  	if(requestType){
  		newText+=	"Are you sure you wish to change the default widget type";
  	}
  	else{
  		newText+=	"Are you sure you wish to remove the service type";
  	}
  	newText+= "...<br><br><b>";
  	newText+=	widgetType + "</b><br><br>...";
  	if(requestType){
  		newText+=	"to this widget";
  	}
  	else{
  		newText+=	"from this widget";
  	}
  	newText+=	"...<br><br><b>" + widgetName + "</b>?";
	x.innerHTML = newText;
	return;				
  }
  </script>
  
<script>
<!--
function confirmDefaultWidgetupdate(widgetId, widgetName, widgetType){
var answer = confirm("Are you sure you wish to change the default widget type...\n\n<" +widgetType+ ">\n\n...to this widget... \n\n<"+widgetName+"> ?");
	if (answer){
		window.location.href="./WidgetAdminServlet?operation=SETDEFAULTWIDGET&widgetId="+widgetId+"&widgetType="+widgetType;
	}
	else{}
}

function confirmDeleteServiceType(widgetId, widgetName, widgetType){
var answer = confirm("Are you sure you wish to remove the service type...\n\n<" +widgetType+ ">\n\n...from this widget... \n\n<"+widgetName+"> ?");
	if (answer){
		window.location.href="./WidgetAdminServlet?operation=REMOVESINGLEWIDGETTYPE&widgetId="+widgetId+"&widgetType="+widgetType;
	}
	else{}
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
    		<h3>Current Widgets</h3>
    	</div>
    	<!--  END HEADER -->
	</div>
     
    <div id="content">     	
		
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
		
		<p>This page lists the widgets that have been imported into the Widget Server.  You can also change set the type of a widget here to be the the default one.
		The current default widget types are in <font color="red">Red</font>. If you wish to set a widget to be the default for another type, then click its <font color=blue>title</font> to set it.
		If you wish to remove one of the service types for a particular widget, then click the delete (<img src="../shared/images/delete_1.gif" width="12" height="12">) icon next to it.
		If you wish to add another service type to an existing widget, then click the (Add new<img src="../shared/images/add_1.gif"  width="12" height="12">) icon for that particular widget.</p>

		<div id="accordion">
		
			<%
			Widget[] widgets = (Widget[])session.getAttribute("widgets");
			Hashtable widgetHashDefaults = (Hashtable)session.getAttribute("widget_defaults");
			if(widgets!=null){	
				for (int i = 1; i < widgets.length; i++) {
					Widget widget = (Widget) widgets[i];
					
			%>		 
			<h3><a href="#"><img height="20" width="20" border="0" src="<%=widget.getWidgetIconLocation()%>"/>&nbsp;<%=widget.getWidgetTitle()%></a></h3>
			<div>

				<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>	
						<div><div style="float:left;" class="adminLayerTitle">Description&nbsp;</div><div class="adminLayerDetail"><%=widget.getWidgetDescription()%></div></div>
					<b class="rbottom">
						<b class="r4"></b>
						<b class="r3"></b>
						<b class="r2"></b>
						<b class="r1"></b>
					</b>
				</div>
				<div id="spacer"></div>
		        
				<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>
						<div><div style="float:left;" class="adminLayerTitle">Url</div><div class="adminLayerDetail"><%=widget.getUrl()%></div></div>
					<b class="rbottom">
						<b class="r4"></b>
						<b class="r3"></b>
						<b class="r2"></b>
						<b class="r1"></b>
					</b>
				</div>
				<div id="spacer"></div>
			
				<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>	         
						<div><div style="float:left;" class="adminLayerTitle">Guid</div><div class="adminLayerDetail"><%=widget.getGuid()%></div></div>
					<b class="rbottom">
						<b class="r4"></b>
						<b class="r3"></b>
						<b class="r2"></b>
						<b class="r1"></b>
					</b>
				</div>
				<div id="spacer"></div>		
	
				<div id="nifty">
					<b class="rtop">
					<b class="r1"></b>
					<b class="r2"></b>
					<b class="r3"></b>
					<b class="r4"></b>
				</b>	        
					<div><div style="float:left;" class="adminLayerTitle">Author</div><div class="adminLayerDetail"><%=widget.getWidgetAuthor()%></div></div>
					<b class="rbottom">
					<b class="r4"></b>
					<b class="r3"></b>
					<b class="r2"></b>
					<b class="r1"></b>
					</b>
				</div>
				<div id="spacer"></div>			        
			        
				<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>	        
						<div><div style="float:left;" class="adminLayerTitle">Height</div><div class="adminLayerDetail"><%=widget.getHeight()%></div></div>
					<b class="rbottom">
						<b class="r4"></b>
						<b class="r3"></b>
						<b class="r2"></b>
						<b class="r1"></b>
					</b>
				</div>
				<div id="spacer"></div>	
	
				<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>	        
						<div><div style="float:left;" class="adminLayerTitle">Width</div><div class="adminLayerDetail"><%=widget.getWidth()%></div></div>
					<b class="rbottom">
						<b class="r4"></b>
						<b class="r3"></b>
						<b class="r2"></b>
						<b class="r1"></b>
					</b>
				</div>
				<div id="spacer"></div>			

				<div id="nifty">
					<b class="rtop">
						<b class="r1"></b>
						<b class="r2"></b>
						<b class="r3"></b>
						<b class="r4"></b>
					</b>	        
						<div><div style="float:left;" class="adminLayerTitle">Types</div><div class="adminLayerDetail">
				        <%
				  	     Set<WidgetType> types = widget.getWidgetTypes();
				         WidgetType[] widgetTypes = types.toArray(new WidgetType[types.size()]);		        
				         for(int j=0;j<widgetTypes.length;++j){
				        	 	if(j!=0){
				        	 		%>&nbsp;<%
				        	 	}
				             	// first need to check the hash contains a key for this service
				               	if(widgetHashDefaults.containsKey(widgetTypes[j].getWidgetContext())){
				               		// if it does contain this service then is it this widget that is default?
				        	        	if(widgetHashDefaults.get(widgetTypes[j].getWidgetContext()).equals(widget.getId())){ //###
				        					%>(<font color="red"><%=widgetTypes[j].getWidgetContext()%></font><a class="opendialog" href="./WidgetAdminServlet?operation=REMOVESINGLEWIDGETTYPE&widgetId=<%=widget.getId()%>&widgetType=<%=widgetTypes[j].getWidgetContext()%>" onclick="updateDialogText('<%=widget.getWidgetTitle()%>','<%=widgetTypes[j].getWidgetContext()%>',false);"><img src="../shared/images/delete_1.gif" width="16" height="16" border="0"></a>)<%
				        				}
				        				else{
						        			%>(<a class="opendialog" href="./WidgetAdminServlet?operation=SETDEFAULTWIDGET&widgetId=<%=widget.getId()%>&widgetType=<%=widgetTypes[j].getWidgetContext()%>" onclick="updateDialogText('<%=widget.getWidgetTitle()%>','<%=widgetTypes[j].getWidgetContext()%>',true);"><%=widgetTypes[j].getWidgetContext()%></a>
						        			
						        				
											<a class="opendialog" href="./WidgetAdminServlet?operation=REMOVESINGLEWIDGETTYPE&widgetId=<%=widget.getId()%>&widgetType=<%=widgetTypes[j].getWidgetContext()%>" onclick="updateDialogText('<%=widget.getWidgetTitle()%>','<%=widgetTypes[j].getWidgetContext()%>',false);"><img src="../shared/images/delete_1.gif" width="16" height="16" border="0"></a>)<%
																	        			
					        			}
				        		}
				        		else{
					        		%>(<a class="opendialog" href="./WidgetAdminServlet?operation=SETDEFAULTWIDGET&widgetId=<%=widget.getId()%>&widgetType=<%=widgetTypes[j].getWidgetContext()%>" onclick="updateDialogText('<%=widget.getWidgetTitle()%>','<%=widgetTypes[j].getWidgetContext()%>',true);"><%=widgetTypes[j].getWidgetContext()%></a><a class="opendialog" href="./WidgetAdminServlet?operation=REMOVESINGLEWIDGETTYPE&widgetId=<%=widget.getId()%>&widgetType=<%=widgetTypes[j].getWidgetContext()%>" onclick="updateDialogText('<%=widget.getWidgetTitle()%>','<%=widgetTypes[j].getWidgetContext()%>',false);"><img src="../shared/images/delete_1.gif" width="16" height="16" border="0"></a>)<%
				        		}	
				        }
				        %>
				        (<a href="/wookie/admin/WidgetAdminServlet?operation=REVISETYPES&dbkey=<%=widget.getId() %>">Add new<img border="0" src="../shared/images/add_1.gif"></a>)</div>
				    	
				    </div></div>
				    	<b class="rbottom">
				<b class="r4"></b>
				<b class="r3"></b>
				<b class="r2"></b>
				<b class="r1"></b>
			</b>
		</div>	        
		<%}%>
		
	<%}%>
	</div></div>
	
<div id="footer">
	<div style="text-align:right"><a class="menulink" href="index.jsp">menu&nbsp;<img border="0" src="../shared/images/book.gif"></a>&nbsp;</div>
</div>
<div id="confirm" style="display:none;" title="Warning"></div>
</body>
</html>
<% session.setAttribute("error_value", null); %>
<% session.setAttribute("message_value", null);%>
<% session.setAttribute("widget_defaults", null);%>
<% session.setAttribute("widgets", null);%>