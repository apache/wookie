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
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.apache.wookie.beans.util.IModule;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.UserRealm;
import org.mortbay.jetty.webapp.WebAppContext;

public class Start {
  static final private Logger logger = Logger.getLogger(Start.class);
  private static int port = 8080;
  private static int shutdownPort = 8079;

  public static final String PERSISTENCE_MANAGER_TYPE_PROPERTY_NAME = "wookie.persistence.manager.type";
  public static final String PERSISTENCE_MANAGER_TYPE_JPA = "jpa";
  public static final String PERSISTENCE_MANAGER_TYPE_JCR = "jcr";

  private static String persistenceManagerType;
  private static Server server;

  public static void main(String[] args) throws Exception {
    boolean initDB = true;
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      logger.info("Runtime argument: " + arg);
      if (arg.startsWith("port=")) {
        port = new Integer(arg.substring(5));
      } else if (arg.startsWith("shutdownport=")) {
        shutdownPort = new Integer(arg.substring(13));
        logger.info("Shutdown port set:to "+shutdownPort);
      } else if (arg.startsWith("initDB=")) {
        initDB = !arg.substring(7).toLowerCase().equals("false");
      } else {
        logger.info("argument UNRECOGNISED - ignoring");
      }
    }

    // load configuration from environment
    persistenceManagerType = getSystemProperty(PERSISTENCE_MANAGER_TYPE_PROPERTY_NAME, PERSISTENCE_MANAGER_TYPE_JPA);

     // Configure system properties specific to the persistence implementation
    IModule module = getModule();
    module.configure();
    
    if (initDB) {
      System.setProperty(PersistenceManagerFactory.PERSISTENCE_MANAGER_INITIALIZE_STORE_PROPERTY_NAME, "true");
    }

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

    IModule module = getModule();
    module.setup();

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
        socket = new ServerSocket(shutdownPort, 1, InetAddress.getByName("127.0.0.1"));
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

  /**
   * Get persistence module. 
   * TODO use a more reliable and extensible approach than fixed class names
   * @return a persistence module
   */
  private static IModule getModule(){
    IModule module = null;
    try {
      if (persistenceManagerType.equals(PERSISTENCE_MANAGER_TYPE_JCR)){
        module = (IModule) Class.forName("org.apache.wookie.beans.jcr.JCRModule").newInstance();
      } else {
        module = (IModule) Class.forName("org.apache.wookie.beans.jpa.JPAModule").newInstance();
      }
    } catch (Exception e) {
      logger.error("Could not load persistence module", e);
    }
    return module;
  }
}
