/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.apache.wookie.beans.jpa;

import java.io.File;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.DataSourceConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.wookie.beans.util.IModule;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.mortbay.jetty.plus.naming.Resource;

/**
 * JPA persistence module
 */
public class JPAModule implements IModule{

  private static String dbUser;
  private static String dbPassword;
  private static String dbDriver;
  private static String dbUri;
  private static String dbType;

  public static final String DB_USER_PROPERTY_NAME = "wookie.db.user";
  public static final String DB_PASSWORD_PROPERTY_NAME = "wookie.db.password";
  public static final String DB_DRIVER_CLASS_PROPERTY_NAME = "wookie.db.driver";
  public static final String DB_URI_PROPERTY_NAME = "wookie.db.uri";
  public static final String DB_TYPE_PROPERTY_NAME = "wookie.db.type";

  public static final String PERSISTENCE_MANAGER_TYPE_PROPERTY_NAME = "wookie.persistence.manager.type";
  public static final String PERSISTENCE_MANAGER_TYPE_JPA = "jpa";

  public void configure(){
    dbUser = getSystemProperty(DB_USER_PROPERTY_NAME, "java");
    dbPassword = getSystemProperty(DB_PASSWORD_PROPERTY_NAME, "java");
    dbDriver = getSystemProperty(DB_DRIVER_CLASS_PROPERTY_NAME, "org.apache.derby.jdbc.EmbeddedDriver");
    dbUri = getSystemProperty(DB_URI_PROPERTY_NAME, "jdbc:derby:widgetDatabase/widgetDB;create=true");
    dbType = getSystemProperty(DB_TYPE_PROPERTY_NAME, "derby");

    System.setProperty(PersistenceManagerFactory.PERSISTENCE_MANAGER_CLASS_NAME_PROPERTY_NAME, JPAPersistenceManager.class.getName());
    System.setProperty(JPAPersistenceManager.PERSISTENCE_MANAGER_DB_TYPE_PROPERTY_NAME, dbType);
  }

  public void setup() throws NamingException{

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

    // Setup a database connection resource using DBCP
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName(dbDriver);
    dataSource.setUrl(dbUri);
    dataSource.setUsername(dbUser);
    dataSource.setPassword(dbPassword);
    dataSource.setMaxActive(80);
    dataSource.setMaxIdle(80);
    dataSource.setInitialSize(5);
    dataSource.setMaxOpenPreparedStatements(0);

    // Set up connection pool
    GenericObjectPool pool = new GenericObjectPool();
    // setup factory and pooling DataSource
    DataSourceConnectionFactory factory = new DataSourceConnectionFactory(dataSource);
    @SuppressWarnings("unused")
    PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(factory,pool,null,null,false,true);
    DataSource poolingDataSource = new PoolingDataSource(pool);


    new Resource(JPAPersistenceManager.WIDGET_DATABASE_JNDI_DATASOURCE_NAME, poolingDataSource);
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
