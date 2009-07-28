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
 * @version $Id: WidgetServiceServlet.java,v 1.3 2009-07-28 16:05:23 scottwilson Exp $ 
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