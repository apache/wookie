package org.tencompetence.widgetservice.server;
/**
 * TODO - Header
 */
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
/**
 * ContextListener - does some init work and makes certain things are available 
 * to resources under this context
 * 
 * @author Paul Sharples
 * @version $Id: ContextListener.java,v 1.1 2007-10-14 10:58:39 ps3com Exp $ 
 *
 */
public class ContextListener implements ServletContextListener {
	/*
	 * In the case of the 'log4j.properties' file used within a server environment
	 * there is no need to explicitly load the file.  It will be automatically loaded as
	 * long as it is placed at the root of the source code. This way it eventually is found under...
	 * 
	 * 			'/webappname/WEB-INF/classes'  ...at runtime.
	 */
	static Logger _logger = Logger.getLogger(ContextListener.class.getName());
	
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext context = event.getServletContext();
			/* 
			 *  load the widgetserver.properties file and put it into this context
			 *  as an attribute 'properties' available to all resources
			 */
			Configuration configuration = new PropertiesConfiguration("widgetserver.properties");
		 	context.setAttribute("properties", (Configuration) configuration);		 	
		} 
		catch (ConfigurationException ex) {
			_logger.error("@@@ConfigurationException thrown: "+ ex.toString());
		}					
	}

	public void contextDestroyed(ServletContextEvent event){}
}
