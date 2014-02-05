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

package org.apache.wookie.beans.util;

import java.lang.reflect.Method;

import org.apache.commons.configuration.Configuration;
import org.apache.wookie.w3c.IAuthor;
import org.apache.wookie.w3c.IDescription;
import org.apache.wookie.w3c.IName;
import org.apache.wookie.w3c.IContent;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.w3c.IIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PersistenceManagerFactory - factory to create and manage IPersistenceManager
 *                             instances per application thread.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public class PersistenceManagerFactory
{
    private static final Logger logger = LoggerFactory.getLogger(PersistenceManagerFactory.class);

    public static final String PERSISTENCE_MANAGER_CLASS_NAME_PROPERTY_NAME = "widget.persistence.manager.classname";
    public static final String PERSISTENCE_MANAGER_CLASS_NAME_DEFAULT = "org.apache.wookie.beans.jpa.JPAPersistenceManager";
    public static final String PERSISTENCE_MANAGER_INITIALIZE_STORE_PROPERTY_NAME = "widget.persistence.manager.initstore";
    
    private static boolean initialized;
    private static Class<?> persistenceManagerClass;
    private static ThreadLocal<IPersistenceManager> threadPersistenceManager = new ThreadLocal<IPersistenceManager>();
    
    /**
     * Initialize factory with configuration.
     * 
     * @param configuration configuration properties
     */
    public static void initialize(Configuration configuration)
    {
        if (!initialized)
        {
            // initialize persistence manager implementation and
            // initialize persistent store if specified
            String className = configuration.getString(PERSISTENCE_MANAGER_CLASS_NAME_PROPERTY_NAME, PERSISTENCE_MANAGER_CLASS_NAME_DEFAULT);
            boolean initializeStore = configuration.getBoolean(PERSISTENCE_MANAGER_INITIALIZE_STORE_PROPERTY_NAME, false);
            try
            {
                persistenceManagerClass = Class.forName(className);

                Method initializeMethod = persistenceManagerClass.getMethod("initialize", Configuration.class, boolean.class);
                initializeMethod.invoke(null, configuration, initializeStore);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Unable to load or initialize PersistenceManager class: "+e, e);                
            }
            
            initialized = true;

            // initialize/verify/validate persistence store using persistence manager
            try
            {
                // allocate and begin persistence manager transaction
                IPersistenceManager persistenceManager = getPersistenceManager();
                persistenceManager.begin();

                // Widget
                boolean initializing = true;
                IWidget widget = persistenceManager.findWidgetByGuid("http://notsupported");
                if (widget == null)
                {
                    // required: always create if not found
                    widget = persistenceManager.newInstance(IWidget.class);
                    widget.setHeight(350);
                    widget.setWidth(500);
                    widget.setIdentifier("http://notsupported");
                    
                    IAuthor author = persistenceManager.newInstance(IAuthor.class);
                    author.setAuthorName("Paul Sharples");
                    author.setEmail("p.sharples@bolton.ac.uk");
                    author.setHref("http://iec.bolton.ac.uk");
                    widget.setAuthor(author);
                    widget.setVersion("v1.0");
                    IName widgetName = persistenceManager.newInstance(IName.class);
                    widgetName.setName("Unsupported widget widget");
                    widgetName.setShort("Unsupported");
                    widget.getNames().add(widgetName);
                    IDescription widgetDescription = persistenceManager.newInstance(IDescription.class);
                    widgetDescription.setDescription("This widget is a placeholder for when no corresponding widget is found for a given type");
                    widget.getDescriptions().add(widgetDescription);
                    IContent widgetStartFile = persistenceManager.newInstance(IContent.class);
                    
                    //
                    // I've had to remove this code as it created a reverse dependency on the runtime helper in wookie-server.
                    // However, we need to reinstate this functionality of not hard-coding the context path.
                    //
                    //widgetStartFile.setSrc(WidgetRuntimeHelper.getWebContextPath() + "/deploy/notsupported/index.htm");
                    //widget.getContentList().add(widgetStartFile);
                    //IContent widgetBUStartFile = persistenceManager.newInstance(IContent.class);
                    //widgetBUStartFile.setSrc(WidgetRuntimeHelper.getWebContextPath() + "/deploy/notsupported/locales/bu/index.htm");
                    //widgetBUStartFile.setLang("bu");
                    //widget.getContentList().add(widgetBUStartFile);
                    //IContent widgetFRStartFile = persistenceManager.newInstance(IContent.class);
                    //widgetFRStartFile.setSrc(WidgetRuntimeHelper.getWebContextPath() + "/deploy/notsupported/locales/fr/index.htm");
                    //widgetFRStartFile.setLang("fr");
                    //widget.getContentList().add(widgetFRStartFile);
                    //IContent widgetENStartFile = persistenceManager.newInstance(IContent.class);
                    //widgetENStartFile.setSrc(WidgetRuntimeHelper.getWebContextPath() + "/deploy/notsupported/locales/en/index.htm");
                    
                    
                    widgetStartFile.setSrc("/wookie/deploy/notsupported/index.htm");
                    widget.getContentList().add(widgetStartFile);
                    IContent widgetBUStartFile = persistenceManager.newInstance(IContent.class);
                    widgetBUStartFile.setSrc("/wookie/deploy/notsupported/locales/bu/index.htm");
                    widgetBUStartFile.setLang("bu");
                    widget.getContentList().add(widgetBUStartFile);
                    IContent widgetFRStartFile = persistenceManager.newInstance(IContent.class);
                    widgetFRStartFile.setSrc("/wookie/deploy/notsupported/locales/fr/index.htm");
                    widgetFRStartFile.setLang("fr");
                    widget.getContentList().add(widgetFRStartFile);
                    IContent widgetENStartFile = persistenceManager.newInstance(IContent.class);
                    widgetENStartFile.setSrc("/wookie/deploy/notsupported/locales/en/index.htm");
                    widgetENStartFile.setLang("en");
                    widget.getContentList().add(widgetENStartFile);
                    IIcon widgetIcon = persistenceManager.newInstance(IIcon.class);
                    widgetIcon.setSrc("/wookie/shared/images/defaultwidget.png");
                    widgetIcon.setHeight(80);
                    widgetIcon.setWidth(80);
                    widgetIcon.setLang("en");
                    widget.getIcons().add(widgetIcon);
                    persistenceManager.save(widget);
                }
                else
                {
                    initializing = false;
                }
                
                // commit persistence manager transaction
                try
                {
                    persistenceManager.commit();
                }
                catch (PersistenceCommitException pce)
                {
                    throw new RuntimeException("Initialization exception: "+pce, pce);
                }

                if (initializing)
                {
                    logger.info("Initialized persistent store with seed data");
                }
                else
                {
                    logger.info("Validated persistent store seed data");                    
                }
            }
            finally
            {
                // close persistence manager transaction
                closePersistenceManager();
            }
            
            logger.info("Initialized with "+className);
        }
        else
        {
            throw new RuntimeException("PersistenceManagerFactory already initialized");
        }
    }
    
    /**
     * Get persistence manager associated with current thread,
     * allocate new persistence manager if required.
     * 
     * @return persistence manager
     */
    public static IPersistenceManager getPersistenceManager()
    {
        if (initialized)
        {
            // get persistence manager associated with current thread or
            // create new persistence manager instance for current thread
            IPersistenceManager persistenceManager = threadPersistenceManager.get();
            if (!persistenceManagerClass.isInstance(persistenceManager))
            {
                try
                {
                    persistenceManager = (IPersistenceManager)persistenceManagerClass.newInstance();
                    threadPersistenceManager.set(persistenceManager);
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Unable to instantiate PersistenceManager: "+e, e);
                }
            }
            return persistenceManager;
        }
        else
        {
            throw new RuntimeException("PersistenceManagerFactory not initialized");            
        }
    }
    
    /**
     * Close and remove persistence manager associated with current
     * thread.
     */
    public static void closePersistenceManager()
    {
        if (initialized)
        {
            // close and remove persistence manager associated with current thread
            IPersistenceManager persistenceManager = threadPersistenceManager.get();
            if (persistenceManager != null)
            {
                persistenceManager.close();
                threadPersistenceManager.remove();
            }
            else
            {
                throw new RuntimeException("PersistenceManager already closed or not allocated for thread");
            }
        }
        else
        {
            throw new RuntimeException("PersistenceManagerFactory not initialized");            
        }
    }

    /**
     * Terminate factory.
     */
    public static void terminate()
    {
        if (initialized)
        {
            initialized = false;

            // terminate persistence manager implementation
            try
            {
                Method terminateMethod = persistenceManagerClass.getMethod("terminate", (Class [])null);
                terminateMethod.invoke(null, (Object [])null);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Unable to terminate PersistenceManager class: "+e, e);                
            }

            logger.info("Terminated");
        }
        else
        {
            throw new RuntimeException("PersistenceManagerFactory not initialized");
        }
    }
}
