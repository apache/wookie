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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.wookie.util.hibernate.DBManagerFactory;
import org.apache.wookie.util.hibernate.IDBManager;
import org.hibernate.SQLQuery;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.UserRealm;
import org.mortbay.jetty.webapp.WebAppContext;

public class Start {
	static final private Logger logger = Logger.getLogger(Start.class);

	private static Server server;

	public static void main(String[] args) throws Exception {
		configureDatabase();
		configureServer();
		startServer();
	}

	/**
	 * Create the database by reading in the file widgetdb_derby.sql and executing all SQL found within.
	 * 
	 * @throws IOException  if the file is not found or is unreadable
	 */
	private static void configureDatabase() throws IOException {
		logger.debug("Configuring Derby Database");
		URL sqlFile = Start.class.getClassLoader().getResource("widgetdb.sql");
		StringBuilder fileData = new StringBuilder(1000);
		FileReader in = new FileReader(sqlFile.getFile());
		BufferedReader br = new BufferedReader(in);
		char[] buf = new char[1024];
        int numRead=0;
        while((numRead=br.read(buf)) != -1){
        	fileData.append(buf, 0, numRead);

        }
        br.close();
        String sqlScript = fileData.toString();
		
        final IDBManager dbManager = DBManagerFactory.getDBManager();
		StringTokenizer st = new StringTokenizer(sqlScript, ";");
		while (st.hasMoreTokens()) {
			String sql = st.nextToken();
			logger.debug("Running SQL snippet: " + sql);
	        dbManager.beginTransaction();
			SQLQuery query = dbManager.createSQLQuery(sql);
			query.executeUpdate();
			dbManager.commitTransaction();
		}
	}

	private static void startServer() throws Exception, InterruptedException {
		logger.info("Starting Wookie Server");
		server.start();  
		server.join();  
		logger.info("point your browser at http://localhost:8080/wookie");
	}

	private static void configureServer() throws Exception {
		logger.info("Configuring Jetty server");
		server = new Server(8080);
		WebAppContext context = new WebAppContext();
		context.setServer(server);
		context.setContextPath("/wookie");
		context.setWar("WebContent");
		server.addHandler(context);
		
		HashUserRealm authedRealm = new HashUserRealm("Authentication Required","etc/jetty-realm.properties");
		server.setUserRealms(new UserRealm[]{authedRealm});
	}
}
