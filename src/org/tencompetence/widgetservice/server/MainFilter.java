package org.tencompetence.widgetservice.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.DBManagerInterface;


public class MainFilter implements Filter {
	
	static final private Logger logger = Logger.getLogger(MainFilter.class);

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) 
	throws IOException, ServletException {
		try {
		//final HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		/** Get a DBManager for this thread. */
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();				
		dbManager.beginTransaction();
		
		//
			//logger.info("@@@@@@START : " + httpRequest.getParameter("action")+ "\t" +httpRequest.getParameter("key")+ "\t" +httpRequest.getParameter("value"));
			chain.doFilter(request, response);
		
		dbManager.commitTransaction();		
		//logger.info("@@@@@END : " +  httpRequest.getParameter("action")+ "\t" +httpRequest.getParameter("key")+ "\t" +httpRequest.getParameter("value"));
		}
		catch (Exception e) {
					logger.error("error: " + e.getCause());
						//	+ "\n " + e.getCause().getClass()
							//+ "\n " + e.getCause().getLocalizedMessage()
						//	+ "\n " + e.getCause().getMessage()
					//);
					e.printStackTrace();
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
