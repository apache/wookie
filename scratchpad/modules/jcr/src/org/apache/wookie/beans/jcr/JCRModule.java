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

package org.apache.wookie.beans.jcr;

import java.io.File;
import java.io.IOException;

import javax.naming.NamingException;

import org.apache.wookie.beans.util.IModule;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.jackrabbit.core.TransientRepository;
import javax.jcr.Repository;
import org.mortbay.jetty.plus.naming.Resource;

/**
 * Persistence module for JCR
 */
public class JCRModule implements IModule {
  
  public static final String REPOSITORY_USER_PROPERTY_NAME = "wookie.repository.user";
  public static final String REPOSITORY_PASSWORD_PROPERTY_NAME = "wookie.repository.password";
  public static final String REPOSITORY_ROOT_PATH_PROPERTY_NAME = "wookie.repository.rootpath";
  public static final String REPOSITORY_WORKSPACE_PROPERTY_NAME = "wookie.repository.workspace";

  public static final String PERSISTENCE_MANAGER_TYPE_JCR = "jcr";
  
  private static String repositoryUser;
  private static String repositoryPassword;
  private static String repositoryRootPath;
  private static String repositoryWorkspace;

  /* (non-Javadoc)
   * @see org.apache.wookie.beans.util.IModule#configure()
   */
  public void configure() {
    repositoryUser = getSystemProperty(REPOSITORY_USER_PROPERTY_NAME, "java");
    repositoryPassword = getSystemProperty(REPOSITORY_PASSWORD_PROPERTY_NAME, "java");
    repositoryRootPath = getSystemProperty(REPOSITORY_ROOT_PATH_PROPERTY_NAME, "/wookie");
    repositoryWorkspace = getSystemProperty(REPOSITORY_WORKSPACE_PROPERTY_NAME, "default");
    
    System.setProperty(JCRPersistenceManager.PERSISTENCE_MANAGER_USER_PROPERTY_NAME, repositoryUser);
    System.setProperty(JCRPersistenceManager.PERSISTENCE_MANAGER_PASSWORD_PROPERTY_NAME, repositoryPassword);
    System.setProperty(JCRPersistenceManager.PERSISTENCE_MANAGER_ROOT_PATH_PROPERTY_NAME, repositoryRootPath);
    System.setProperty(JCRPersistenceManager.PERSISTENCE_MANAGER_WORKSPACE_PROPERTY_NAME, repositoryWorkspace);
    
    System.setProperty(PersistenceManagerFactory.PERSISTENCE_MANAGER_CLASS_NAME_PROPERTY_NAME, JCRPersistenceManager.class.getName());
    
  }

  /* (non-Javadoc)
   * @see org.apache.wookie.beans.util.IModule#setup()
   */
  public void setup() throws NamingException, IOException {
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
