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
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.jcr.Repository;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.log4j.Logger;
import org.apache.wookie.beans.jcr.JCRPersistenceManager;
import org.apache.wookie.beans.jpa.JPAPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.plus.naming.Resource;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.UserRealm;
import org.mortbay.jetty.webapp.WebAppContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Start {
	static final private Logger logger = Logger.getLogger(Start.class);
	private static int port = 8080;
	
    public static final String DB_USER_PROPERTY_NAME = "wookie.db.user";
    public static final String DB_PASSWORD_PROPERTY_NAME = "wookie.db.password";
    public static final String DB_DRIVER_CLASS_PROPERTY_NAME = "wookie.db.driver";
    public static final String DB_URI_PROPERTY_NAME = "wookie.db.uri";
    public static final String DB_TYPE_PROPERTY_NAME = "wookie.db.type";
    public static final String REPOSITORY_USER_PROPERTY_NAME = "wookie.repository.user";
    public static final String REPOSITORY_PASSWORD_PROPERTY_NAME = "wookie.repository.password";
    public static final String REPOSITORY_ROOT_PATH_PROPERTY_NAME = "wookie.repository.rootpath";
    public static final String REPOSITORY_WORKSPACE_PROPERTY_NAME = "wookie.repository.workspace";
    public static final String PERSISTENCE_MANAGER_TYPE_PROPERTY_NAME = "wookie.persistence.manager.type";
    public static final String PERSISTENCE_MANAGER_TYPE_JPA = "jpa";
    public static final String PERSISTENCE_MANAGER_TYPE_JCR = "jcr";

    private static String persistenceManagerType;
    private static String dbUser;
    private static String dbPassword;
    private static String dbDriver;
    private static String dbUri;
    private static String dbType;
    private static String repositoryUser;
    private static String repositoryPassword;
    private static String repositoryRootPath;
    private static String repositoryWorkspace;
	private static Server server;

	public static void main(String[] args) throws Exception {
	    boolean initDB = true;
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			System.out.println("Runtime argument: " + arg);
			if (arg.startsWith("port=")) {
			  port = new Integer(arg.substring(5));
			} else if (arg.startsWith("initDB=")) {
			  initDB = !arg.substring(7).toLowerCase().equals("false");
			} else {
			  System.out.println("argument UNRECOGNISED - ignoring");
			}
		}

		// load configuration from environment
        persistenceManagerType = getSystemProperty(PERSISTENCE_MANAGER_TYPE_PROPERTY_NAME, PERSISTENCE_MANAGER_TYPE_JPA);
        dbUser = getSystemProperty(DB_USER_PROPERTY_NAME, "java");
        dbPassword = getSystemProperty(DB_PASSWORD_PROPERTY_NAME, "java");
        dbDriver = getSystemProperty(DB_DRIVER_CLASS_PROPERTY_NAME, "org.apache.derby.jdbc.EmbeddedDriver");
        dbUri = getSystemProperty(DB_URI_PROPERTY_NAME, "jdbc:derby:widgetDatabase/widgetDB;create=true");
        dbType = getSystemProperty(DB_TYPE_PROPERTY_NAME, "derby");
        repositoryUser = getSystemProperty(REPOSITORY_USER_PROPERTY_NAME, "java");
        repositoryPassword = getSystemProperty(REPOSITORY_PASSWORD_PROPERTY_NAME, "java");
        repositoryRootPath = getSystemProperty(REPOSITORY_ROOT_PATH_PROPERTY_NAME, "/wookie");
        repositoryWorkspace = getSystemProperty(REPOSITORY_WORKSPACE_PROPERTY_NAME, "default");

        // set configuration properties
        if (persistenceManagerType.equals(PERSISTENCE_MANAGER_TYPE_JPA)) {
            System.setProperty(PersistenceManagerFactory.PERSISTENCE_MANAGER_CLASS_NAME_PROPERTY_NAME, JPAPersistenceManager.class.getName());
        } else if (persistenceManagerType.equals(PERSISTENCE_MANAGER_TYPE_JCR)) {
            System.setProperty(PersistenceManagerFactory.PERSISTENCE_MANAGER_CLASS_NAME_PROPERTY_NAME, JCRPersistenceManager.class.getName());
        }
		if (initDB) {
		    System.setProperty(PersistenceManagerFactory.PERSISTENCE_MANAGER_INITIALIZE_STORE_PROPERTY_NAME, "true");
		}
		System.setProperty(JPAPersistenceManager.PERSISTENCE_MANAGER_DB_TYPE_PROPERTY_NAME, dbType);
        System.setProperty(JCRPersistenceManager.PERSISTENCE_MANAGER_USER_PROPERTY_NAME, repositoryUser);
        System.setProperty(JCRPersistenceManager.PERSISTENCE_MANAGER_PASSWORD_PROPERTY_NAME, repositoryPassword);
        System.setProperty(JCRPersistenceManager.PERSISTENCE_MANAGER_ROOT_PATH_PROPERTY_NAME, repositoryRootPath);
        System.setProperty(JCRPersistenceManager.PERSISTENCE_MANAGER_WORKSPACE_PROPERTY_NAME, repositoryWorkspace);

		// configure and start server
		configureServer();
		startServer();
	}

	private static void startServer() throws Exception, InterruptedException {
		logger.info("Starting Wookie Server");
		logger.info("point your browser at http://localhost:" + port + "/wookie");
		// The monitor thread will end this server instance when it receives a \n\r on port 8079
		Thread monitor = new MonitorThread();
	    monitor.start();
		server.start(); 			
		server.join();  			
		monitor = null;
		System.exit(0);
	}

	private static void configureServer() throws Exception {
	    // create embedded jetty instance
		logger.info("Configuring Jetty server");
		server = new Server(port);

		// configure embedded jetty to handle wookie web application
		WebAppContext context = new WebAppContext();
		context.setServer(server);
		context.setContextPath("/wookie");
		context.setWar("build/webapp/wookie");
		
		// enable and configure JNDI container resources
        context.setConfigurationClasses(new String[]{"org.mortbay.jetty.webapp.WebInfConfiguration",
                                                     "org.mortbay.jetty.plus.webapp.EnvConfiguration",
                                                     "org.mortbay.jetty.plus.webapp.Configuration",
                                                     "org.mortbay.jetty.webapp.JettyWebXmlConfiguration",
                                                     "org.mortbay.jetty.webapp.TagLibConfiguration"});
        if (persistenceManagerType.equals(PERSISTENCE_MANAGER_TYPE_JPA)) {
            logger.info("Configuring JPA persistence manager");

            // setup derby database directory and logging properties
            if (dbType.equals("derby") && dbUri.startsWith("jdbc:derby:")) {
                int dbUriArgsIndex = dbUri.indexOf(";", 11);
                if (dbUriArgsIndex == -1) {
                    dbUriArgsIndex = dbUri.length();
                }
                String databasePath = dbUri.substring(11, dbUriArgsIndex);
                int databaseDirIndex = databasePath.lastIndexOf(File.separatorChar);
                if ((databaseDirIndex == -1) && (File.separatorChar != '/')) {
                    databaseDirIndex = databasePath.lastIndexOf('/');                    
                }
                if (databaseDirIndex != -1) {
                    String databaseDir = databasePath.substring(0, databaseDirIndex);
                    File databaseDirFile = new File(databaseDir);
                    if (!databaseDirFile.exists()) {
                        databaseDirFile.mkdirs();
                    }
                    String derbyLog = databaseDirFile.getAbsolutePath()+File.separator+"derby.log";
                    System.setProperty("derby.stream.error.file", derbyLog);
                }
            }

            // setup C3P0 JPA database connection pool JNDI resource
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setJdbcUrl(dbUri);
            dataSource.setDriverClass(dbDriver);
            dataSource.setUser(dbUser);
            dataSource.setPassword(dbPassword);
            dataSource.setAcquireIncrement(1);
            dataSource.setIdleConnectionTestPeriod(200);
            dataSource.setMaxPoolSize(80);
            dataSource.setMaxStatements(0);
            dataSource.setMinPoolSize(5);
            dataSource.setMaxIdleTime(80);
            new Resource(JPAPersistenceManager.WIDGET_DATABASE_JNDI_DATASOURCE_NAME, dataSource);
        } else if (persistenceManagerType.equals(PERSISTENCE_MANAGER_TYPE_JCR)) {
            logger.info("Configuring JCR persistence manager");

            // setup repository directory and derby logging properties
            File repositoryDirFile = new File("widgetRepository");
            if (!repositoryDirFile.exists()) {
                repositoryDirFile.mkdirs();
            }
            String derbyLog = repositoryDirFile.getAbsolutePath()+File.separator+"derby.log";
            System.setProperty("derby.stream.error.file", derbyLog);

            // setup Jackrabbit JCR repository JNDI resource
            String repositoryConfig = repositoryDirFile.getAbsolutePath()+File.separator+"repository.xml";
            Repository repository = new TransientRepository(repositoryConfig, repositoryDirFile.getAbsolutePath());
            new Resource(JCRPersistenceManager.WIDGET_REPOSITORY_JNDI_REPOSITORY_NAME, repository);
        }

        // configure embedded jetty web application handler
		server.addHandler(context);
		
        // configure embedded jetty authentication realm
		HashUserRealm authedRealm = new HashUserRealm("Authentication Required","etc/jetty-realm.properties");
		server.setUserRealms(new UserRealm[]{authedRealm});

		logger.info("Configured Jetty server");
	}
	
	private static class MonitorThread extends Thread {

		private ServerSocket socket;

		public MonitorThread() {
			setDaemon(true);
			setName("StopMonitor");
			try {
				socket = new ServerSocket(8079, 1, InetAddress.getByName("127.0.0.1"));
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void run() {
			System.out.println("*** running jetty 'stop' thread");
			Socket accept;
			try {
				accept = socket.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
				reader.readLine();
				System.out.println("*** stopping jetty embedded server");
				server.stop();
				accept.close();
				socket.close();	                	                
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
     * Get configuration system property.
     * 
     * @param name property name
     * @param defaultValue default property value
     * @return property value
     */
    private static String getSystemProperty(String name, String defaultValue)
    {
        String value = System.getProperty(name);
        return (((value != null) && (value.length() > 0) && !value.startsWith("$")) ? value : defaultValue);
    }
}
