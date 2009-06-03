/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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

/**
 * Servlet to advertise the existing widgets in the system
 * @author Paul Sharples
 * @version $Id: WidgetAdvertiseServlet.java,v 1.7 2009-06-03 15:46:31 scottwilson Exp $ 
 *
 */
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
		out.println("<widgets>");	

		if (request.getParameter("all")!= null){
			for(Widget thewidget : Widget.findAll()){
				widget = thewidget;
				if(widget.getId()!=1){
					out.println("\t<widget identifier=\""+widget.getGuid()+"\">");			
					out.println("\t\t<title>"+widget.getWidgetTitle()+"</title>");
					out.println("\t\t<description>"+widget.getWidgetDescription()+"</description>");

					iconPath = widget.getWidgetIconLocation();
					// If local...
					if (!iconPath.startsWith("http")){
					urlWidgetIcon = new URL(request.getScheme() ,
							request.getServerName() ,
							request.getServerPort() , iconPath);
					} else {
						urlWidgetIcon = new URL(iconPath);
					}
					
					out.println("\t\t<icon>"+urlWidgetIcon.toString()+"</icon>");
					out.println("\t\t<parameter>widget=unknown</parameter>");
					out.println("\t</widget>");
				}
			}						
		} else {
			for(WidgetDefault widgetDefault : WidgetDefault.findAll()){
				widget = Widget.findById(widgetDefault.getWidgetId());
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
