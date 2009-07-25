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
package org.apache.wookie;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.wookie.controller.ParticipantsController;
import org.apache.wookie.controller.PropertiesController;
import org.apache.wookie.controller.WidgetInstancesController;
import org.apache.wookie.helpers.WidgetKeyManager;

/**
 * Servlet implementation class for Servlet: WidgetService
 * NOTE this class only exists for legacy plugin support. New plugins
 * should implement the REST interface.
 * @author Paul Sharples
 * @version $Id: WidgetServiceServlet.java,v 1.2 2009-07-25 21:19:39 scottwilson Exp $ 
 *
 */
public class WidgetServiceServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	private static final long serialVersionUID = 308590474406800659L;		
	static Logger _logger = Logger.getLogger(WidgetServiceServlet.class.getName());	

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public WidgetServiceServlet() {
		super();
	}   	

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response){					
		if (!WidgetKeyManager.isValidRequest(request)){
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} else {
			try {
				String requestId = request.getParameter("requestid"); //$NON-NLS-1$
				if(requestId.equals("getwidget")){ //$NON-NLS-1$
					WidgetInstancesController.doGetWidget(request, response);
				}
				else if(requestId.equals("stopwidget")){ //$NON-NLS-1$
					WidgetInstancesController.doStopWidget(request, response);
				}
				else if(requestId.equals("resumewidget")){ //$NON-NLS-1$
					WidgetInstancesController.doResumeWidget(request, response);
				}
				else if(requestId.equals("setpublicproperty")){ //$NON-NLS-1$
					PropertiesController.doSetProperty(request, response, false);
				}		
				else if(requestId.equals("setpersonalproperty")){ //$NON-NLS-1$
					PropertiesController.doSetProperty(request, response, true );
				}
				else if(requestId.equals("addparticipant")){ //$NON-NLS-1$
					ParticipantsController.addParticipant(request, response );
				}
				else if(requestId.equals("removeparticipant")){ //$NON-NLS-1$
					ParticipantsController.removeParticipant(request, response );
				}
				else if(requestId.equals("cloneshareddata")){ //$NON-NLS-1$
					WidgetInstancesController.cloneSharedData(request, response );
				}
				else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}

			} 
			catch (Exception ex) {					
				_logger.error("Error in doGet():", ex); //$NON-NLS-1$
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	

}