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

package org.apache.wookie.beans.jcr.tests;

import java.io.File;

import javax.jcr.Repository;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.jackrabbit.core.TransientRepository;
import org.apache.wookie.beans.jcr.JCRPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.tests.beans.AbstractPersistenceTest;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JCRPersistenceTest - JCR persistence implementation tests.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public class JCRPersistenceTest extends AbstractPersistenceTest
{
    private static final Logger logger = LoggerFactory.getLogger(JCRPersistenceTest.class);
    
    public static final String REPOSITORY_USER_PROPERTY_NAME = "wookie.test.repository.user";
    public static final String REPOSITORY_PASSWORD_PROPERTY_NAME = "wookie.test.repository.password";
    public static final String REPOSITORY_DIRECTORY_PROPERTY_NAME = "wookie.test.repository.dir";
    public static final String REPOSITORY_ROOT_PATH_PROPERTY_NAME = "wookie.test.repository.rootpath";
    public static final String REPOSITORY_WORKSPACE_PROPERTY_NAME = "wookie.test.repository.workspace";
    
    private String repositoryDir;
    private String repositoryUser;
    private String repositoryPassword;
    private String repositoryRootPath;
    private String repositoryWorkspace;
    private Context rootContext;
    
    /**
     * Set up JCR persistence runtime test environment.
     * 
     * @throws Exception
     */
    @Before
    public void setUpPerTest() throws Exception
    {
        logger.info("JCR set up test");

        // load repository configuration from environment
        String testRepositoryDir = System.getProperty("user.dir")+"/build/test-repository";
        repositoryDir = getSystemProperty(REPOSITORY_DIRECTORY_PROPERTY_NAME, testRepositoryDir);
        repositoryUser = getSystemProperty(REPOSITORY_USER_PROPERTY_NAME, "java");
        repositoryPassword = getSystemProperty(REPOSITORY_PASSWORD_PROPERTY_NAME, "java");
        repositoryRootPath = getSystemProperty(REPOSITORY_ROOT_PATH_PROPERTY_NAME, "/wookie");
        repositoryWorkspace = getSystemProperty(REPOSITORY_WORKSPACE_PROPERTY_NAME, "default");
        
        // construct JCR remote client repository instance
        File repositoryDirFile = new File(repositoryDir);
        if (!repositoryDirFile.exists())
        {
            repositoryDirFile.mkdirs();
        }
        String derbyLog = repositoryDir+File.separator+"derby.log";
        System.setProperty("derby.stream.error.file", derbyLog);
        String repositoryConfig = repositoryDir+File.separator+"repository.xml";
        Repository repository = new TransientRepository(repositoryConfig, repositoryDir);
        
        // create JNDI context
        rootContext = new InitialContext();
        Context namingContext = lookupOrCreateNamingContext(rootContext, "java:comp");
        namingContext = lookupOrCreateNamingContext(namingContext, "env");
        lookupOrCreateNamingContext(namingContext, "jcr");
        
        // bind datasource to JNDI context
        rootContext.rebind(JCRPersistenceManager.WIDGET_REPOSITORY_JNDI_REPOSITORY_FULL_NAME, repository);

        // initialize persistence manager factory and persistence manager;
        // truncate and initialize persistent store
        Configuration configuration = new PropertiesConfiguration();
        configuration.setProperty(PersistenceManagerFactory.PERSISTENCE_MANAGER_CLASS_NAME_PROPERTY_NAME, JCRPersistenceManager.class.getName());
        configuration.setProperty(PersistenceManagerFactory.PERSISTENCE_MANAGER_INITIALIZE_STORE_PROPERTY_NAME, "true");
        configuration.setProperty(JCRPersistenceManager.PERSISTENCE_MANAGER_USER_PROPERTY_NAME, repositoryUser);
        configuration.setProperty(JCRPersistenceManager.PERSISTENCE_MANAGER_PASSWORD_PROPERTY_NAME, repositoryPassword);
        configuration.setProperty(JCRPersistenceManager.PERSISTENCE_MANAGER_ROOT_PATH_PROPERTY_NAME, repositoryRootPath);
        configuration.setProperty(JCRPersistenceManager.PERSISTENCE_MANAGER_WORKSPACE_PROPERTY_NAME, repositoryWorkspace);
        PersistenceManagerFactory.initialize(configuration);

        logger.info("JCR test set up");
        configured = true;
    }

    /**
     * Tear down JCR persistence runtime test environment.
     * 
     * @throws Exception
     */
    @After
    public void tearDownPerTest() throws Exception
    {
        configured = false;
        logger.info("JCR tear down test");

        // terminate persistence manager factory
        PersistenceManagerFactory.terminate();

        // unbind repository from JNDI context
        rootContext.unbind(JCRPersistenceManager.WIDGET_REPOSITORY_JNDI_REPOSITORY_FULL_NAME);
        
        logger.info("JCR test torn down");
    }
}
