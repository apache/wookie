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
 *  limitations under the License.
 */

package org.apache.wookie.tests.beans;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.wookie.beans.jpa.JPAPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JPAPersistenceTest - JPA persistence implementation tests.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public class JPAPersistenceTest extends AbstractPersistenceTest
{
    private static final Logger logger = LoggerFactory.getLogger(JPAPersistenceTest.class);
    
    public static final String DB_USER_PROPERTY_NAME = "wookie.test.db.user";
    public static final String DB_PASSWORD_PROPERTY_NAME = "wookie.test.db.password";
    public static final String DB_DRIVER_CLASS_PROPERTY_NAME = "wookie.test.db.driver";
    public static final String DB_URI_PROPERTY_NAME = "wookie.test.db.uri";
    public static final String DB_TYPE_PROPERTY_NAME = "wookie.test.db.type";
    
    private String dbUser;
    private String dbPassword;
    private String dbDriver;
    private String dbUri;
    private String dbType;
    private ObjectPool connectionPool;
    private Context rootContext;
    
    /**
     * Set up JPA persistence runtime test environment.
     * 
     * @throws Exception
     */
    @Before
    public void setUpPerTest() throws Exception
    {
        logger.info("JPA set up test");

        // load database configuration from environment
        String testDatabaseDir = (System.getProperty("user.dir")+"/build/test-database").replace(File.separatorChar, '/');
        dbUser = getSystemProperty(DB_USER_PROPERTY_NAME, "java");
        dbPassword = getSystemProperty(DB_PASSWORD_PROPERTY_NAME, "java");
        dbDriver = getSystemProperty(DB_DRIVER_CLASS_PROPERTY_NAME, "org.apache.derby.jdbc.EmbeddedDriver");
        dbUri = getSystemProperty(DB_URI_PROPERTY_NAME, "jdbc:derby:"+testDatabaseDir+"/widgetdb;create=true");
        dbType = getSystemProperty(DB_TYPE_PROPERTY_NAME, "derby");
        
        // load driver
        Class.forName(dbDriver);
        
        // test connection
        Connection conn = DriverManager.getConnection(dbUri, dbUser, dbPassword);
        conn.close();
        
        // construct pooling datasource
        connectionPool = new GenericObjectPool(null, 0, GenericObjectPool.WHEN_EXHAUSTED_GROW, 0, 5);
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(dbUri, dbUser, dbPassword);
        new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
        DataSource dataSource = new PoolingDataSource(connectionPool);            
        
        // create JNDI context
        rootContext = new InitialContext();
        Context namingContext = lookupOrCreateNamingContext(rootContext, "java:comp");
        namingContext = lookupOrCreateNamingContext(namingContext, "env");
        lookupOrCreateNamingContext(namingContext, "jdbc");
        
        // bind datasource to JNDI context
        rootContext.rebind(JPAPersistenceManager.WIDGET_DATABASE_JNDI_DATASOURCE_FULL_NAME, dataSource);

        // initialize persistence manager factory and persistence manager;
        // truncate and initialize persistent store
        Configuration configuration = new PropertiesConfiguration();
        configuration.setProperty(PersistenceManagerFactory.PERSISTENCE_MANAGER_CLASS_NAME_PROPERTY_NAME, JPAPersistenceManager.class.getName());
        configuration.setProperty(PersistenceManagerFactory.PERSISTENCE_MANAGER_INITIALIZE_STORE_PROPERTY_NAME, "true");
        configuration.setProperty(JPAPersistenceManager.PERSISTENCE_MANAGER_CACHE_SIZE_PROPERTY_NAME, "1000");
        configuration.setProperty(JPAPersistenceManager.PERSISTENCE_MANAGER_DB_TYPE_PROPERTY_NAME, dbType);
        PersistenceManagerFactory.initialize(configuration);

        logger.info("JPA test set up");
        configured = true;
    }

    /**
     * Tear down JPA persistence runtime test environment.
     * 
     * @throws Exception
     */
    @After
    public void tearDownPerTest() throws Exception
    {
        configured = false;
        logger.info("JPA tear down test");

        // terminate persistence manager factory
        PersistenceManagerFactory.terminate();

        // unbind datasource from JNDI context
        rootContext.unbind(JPAPersistenceManager.WIDGET_DATABASE_JNDI_DATASOURCE_FULL_NAME);
        
        // shutdown datasource pool
        connectionPool.close();
        
        // special shutdown handling for derby
        if (dbDriver.equals("org.apache.derby.jdbc.EmbeddedDriver") && dbUri.startsWith("jdbc:derby:") && dbType.equals("derby"))
        {
            // derby shutdown connection
            String shutdownDBUri = dbUri;
            int parametersIndex = shutdownDBUri.indexOf(";");
            if (parametersIndex != -1)
            {
                shutdownDBUri = shutdownDBUri.substring(0, parametersIndex);
            }
            shutdownDBUri += ";shutdown=true";
            try
            {
                DriverManager.getConnection(shutdownDBUri, dbUser, dbPassword);
                throw new SQLException("Derby database not shutdown");
            }
            catch (SQLException sqle)
            {
                if (!sqle.getSQLState().equals("08006") && !sqle.getSQLState().equals("XJ015"))
                {
                    throw sqle;
                }
            }
        }

        logger.info("JPA test torn down");
    }
}
