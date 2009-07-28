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

package org.apache.wookie.server;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.apache.wookie.util.hibernate.HibernateUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
/**
 * ContextListener - does some init work and makes certain things are available 
 * to resources under this context
 * 
 * @author Paul Sharples
 * @version $Id: ContextListener.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $ 
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
		// start hibernate now, not on first request
		HibernateUtil.getSessionFactory();
		try {
			ServletContext context = event.getServletContext();
			/* 
			 *  load the widgetserver.properties file and put it into this context
			 *  as an attribute 'properties' available to all resources
			 */
			Configuration configuration = new PropertiesConfiguration("widgetserver.properties");
		 	context.setAttribute("properties", (Configuration) configuration);
			/* 
			 *  load the opensocial.properties file and put it into this context
			 *  as an attribute 'opensocial' available to all resources
			 */
			Configuration opensocialConfiguration = new PropertiesConfiguration("opensocial.properties");
		 	context.setAttribute("opensocial", (Configuration) opensocialConfiguration);
		 	/*
		 	 * Initialise the locale handler
		 	 */
		 	LocaleHandler.getInstance().initialize(configuration);
		} 
		catch (ConfigurationException ex) {
			_logger.error("ConfigurationException thrown: "+ ex.toString());
		}					
	}

	public void contextDestroyed(ServletContextEvent event){
		HibernateUtil.getSessionFactory().close(); // Free all resources
	}
}
