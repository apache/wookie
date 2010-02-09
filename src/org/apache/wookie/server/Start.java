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

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.wookie.util.hibernate.DBManagerFactory;
import org.apache.wookie.util.hibernate.IDBManager;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.UserRealm;
import org.mortbay.jetty.webapp.WebAppContext;

public class Start {
	static final private Logger logger = Logger.getLogger(Start.class);
	private static int port = 8080;

	private static Server server;

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < args.length; i++) {
			String arg = args[0];
			if (arg.startsWith("port=")) {
			  port = new Integer(arg.substring(5));
			}
		}
		try {
			configureDatabase();
		} catch (Exception e) {
			if (e.getCause().getMessage().contains("duplicate key value")){ 
				throw new IOException("There was a problem setting up the database.\n If this is not the first time you are running Wookie in" + 
						" standalone mode, then you should run \"ant clean-db\" before \"ant run\" to clear the database.");
			} else {
				throw e;
			}
		}
		configureServer();
		startServer();
	}

	/**
	 * Create the database by reading in the file widgetdb_derby.sql and executing all SQL found within.
	 * 
	 * @throws IOException  if the file is not found or is unreadable
	 */
	private static void configureDatabase() throws Exception {
		logger.debug("Configuring Derby Database");
		String sqlScript = IOUtils.toString(Start.class.getClassLoader().getResourceAsStream("widgetdb.sql"));
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		StringTokenizer st = new StringTokenizer(sqlScript, ";"); 
		while (st.hasMoreTokens()) { 
			dbManager.beginTransaction(); 
			dbManager.createSQLQuery(st.nextToken()).executeUpdate(); 
			dbManager.commitTransaction(); 
		} 
	}

	private static void startServer() throws Exception, InterruptedException {
		logger.info("Starting Wookie Server");
		server.start();  
		server.join();  
		logger.info("point your browser at http://localhost:" + port + "/wookie");
	}

	private static void configureServer() throws Exception {
		logger.info("Configuring Jetty server");
		server = new Server(port);
		WebAppContext context = new WebAppContext();
		context.setServer(server);
		context.setContextPath("/wookie");
		context.setWar("build/webapp/wookie");
		server.addHandler(context);
		
		HashUserRealm authedRealm = new HashUserRealm("Authentication Required","etc/jetty-realm.properties");
		server.setUserRealms(new UserRealm[]{authedRealm});
	}
}
