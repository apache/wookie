package org.tencompetence.widgetservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetDefault;
import org.tencompetence.widgetservice.manager.IWidgetAdminManager;
import org.tencompetence.widgetservice.manager.impl.WidgetAdminManager;

public class WidgetAdvertiseServlet extends HttpServlet implements Servlet {
	
	private static final String XMLDECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	private static final String CONTENT_TYPE = "text/xml;charset=\"UTF-8\""; 

	private static final long serialVersionUID = 1L;
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		URL urlWidgetIcon = null;
		Widget widget = null;
		String iconPath = null;
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.println(XMLDECLARATION);		
		IWidgetAdminManager manager = new WidgetAdminManager();
		out.println("<widgets>");	
		for(WidgetDefault widgetDefault : manager.getAllDefaultWidgets()){
			widget = manager.getWidget(widgetDefault.getWidgetId());
			if(widget.getId()!=1){
				out.println("\t<widget identifier=\""+widget.getGuid()+"\">");			
				out.println("\t\t<title>"+widget.getWidgetTitle()+"</title>");
				out.println("\t\t<description>"+widget.getWidgetDescription()+"</description>");
				
				iconPath = widget.getWidgetIconLocation();
				urlWidgetIcon = new URL(request.getScheme() ,
						request.getServerName() ,
						request.getServerPort() , iconPath);
				
				
				out.println("\t\t<icon>"+urlWidgetIcon.toString()+"</icon>");
				out.println("\t\t<parameter>widget=" + widgetDefault.getWidgetContext()+"</parameter>");
				out.println("\t</widget>");
			}
		}						
		out.println("</widgets>");
	}
	
	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
