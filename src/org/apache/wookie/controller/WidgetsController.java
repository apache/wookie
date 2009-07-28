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

package org.apache.wookie.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wookie.beans.PreferenceDefault;
import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetDefault;

/**
 * REST API for Widgets
 * Methods:
 * 	GET widgets (index)
 *  GET widgets/{id} (show widget)
 * @author scott
 *
 */
public class WidgetsController extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 8759704878105474902L;

	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	private static final String CONTENT_TYPE = "text/xml;charset=\"UTF-8\""; 

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resource_id = locateRESTname(request);
		if (resource_id != null && resource_id.length() > 0){
			show(resource_id, request, response);
		} else {
			index(request, response);
		}
		
	}
	
	private void show(String id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Widget widget = null;
		widget = Widget.findById(Integer.valueOf(id));
		if (widget == null){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			response.setContentType(CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			out.println(XMLDECLARATION);		
			out.println("<widgets>");	
			out.println(widgetToXml(widget, request));
			out.println("</widgets>");
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}

	private void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Widget widget = null;
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.println(XMLDECLARATION);		
		out.println("<widgets>\n");	

		if (request.getParameter("all")!= null){
			for(Widget thewidget : Widget.findAll()){
				widget = thewidget;
				if(widget.getId()!=1){
					out.println(widgetToXml(widget, request));
				}
			}						
		} else {
			for(WidgetDefault widgetDefault : WidgetDefault.findAll()){
				widget = Widget.findById(widgetDefault.getWidgetId());
				if(widget.getId()!=1){
					out.println(widgetToXml(widget, request));
				}
			}						
		}
		out.println("</widgets>");
	}
	
	private String widgetToXml(Widget widget, HttpServletRequest request) throws MalformedURLException{
		String out = "";
		URL urlWidgetIcon = null;
		out+="\t<widget identifier=\""+widget.getGuid()+"\" version=\""+widget.getVersion()+"\">\n";			
		out+="\t\t<title>"+widget.getWidgetTitle()+"</title>\n";
		out+="\t\t<description>"+widget.getWidgetDescription()+"</description>\n";
		String iconPath = widget.getWidgetIconLocation();
		// If local...
		if (!iconPath.startsWith("http")){
			 urlWidgetIcon = new URL(request.getScheme() ,
					request.getServerName() ,
					request.getServerPort() , iconPath);
		} else {
			 urlWidgetIcon = new URL(iconPath);
		}
		out+="\t\t<icon>"+urlWidgetIcon.toString()+"</icon>\n";
		
		// Do preference defaults
		PreferenceDefault[] prefs = PreferenceDefault.findByValue("widget", widget);
		for (PreferenceDefault pref:prefs){
			out+="\t\t<preference name=\""+pref.getPreference()+"\"/>";						
		}
		out+="\t</widget>\n";
		
		return out;
	}
	
	// note that pathinfo starts with a / for some reason
	String locateRESTname( HttpServletRequest req ){
		String path = req.getPathInfo();  // may be null, plain name or name plus params
		if( path == null ){
			return null ;
		}
		// extract REST name
		if( path.startsWith("/") ){ path = path.substring(1) ; }
		int p = path.indexOf('/');
		if( p > 0 ){
			path = path.substring( 0, p ); // name isolated
		}
		if (path != null) path = path.trim();
		return path ;
	}    
}
