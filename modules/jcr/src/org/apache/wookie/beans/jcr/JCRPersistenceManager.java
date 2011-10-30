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

package org.apache.wookie.beans.jcr;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.nodetype.NodeTypeManager;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.atomictypeconverter.impl.DefaultAtomicTypeConverterProvider;
import org.apache.jackrabbit.ocm.manager.cache.ObjectCache;
import org.apache.jackrabbit.ocm.manager.cache.impl.RequestObjectCacheImpl;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.manager.objectconverter.impl.ObjectConverterImpl;
import org.apache.jackrabbit.ocm.manager.objectconverter.impl.ProxyManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.apache.jackrabbit.ocm.query.Filter;
import org.apache.jackrabbit.ocm.query.Query;
import org.apache.jackrabbit.ocm.query.QueryManager;
import org.apache.wookie.beans.IAccessRequest;
import org.apache.wookie.beans.IApiKey;
import org.apache.wookie.beans.IBean;
import org.apache.wookie.beans.IDescription;
import org.apache.wookie.beans.IFeature;
import org.apache.wookie.beans.ILicense;
import org.apache.wookie.beans.IName;
import org.apache.wookie.beans.IParam;
import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.IPreferenceDefault;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IStartFile;
import org.apache.wookie.beans.IToken;
import org.apache.wookie.beans.IWhitelist;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetDefault;
import org.apache.wookie.beans.IWidgetIcon;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.IWidgetService;
import org.apache.wookie.beans.IWidgetType;
import org.apache.wookie.beans.jcr.impl.AccessRequestImpl;
import org.apache.wookie.beans.jcr.impl.ApiKeyImpl;
import org.apache.wookie.beans.jcr.impl.DescriptionImpl;
import org.apache.wookie.beans.jcr.impl.FeatureImpl;
import org.apache.wookie.beans.jcr.impl.LicenseImpl;
import org.apache.wookie.beans.jcr.impl.NameImpl;
import org.apache.wookie.beans.jcr.impl.ParamImpl;
import org.apache.wookie.beans.jcr.impl.ParticipantImpl;
import org.apache.wookie.beans.jcr.impl.PreferenceDefaultImpl;
import org.apache.wookie.beans.jcr.impl.PreferenceImpl;
import org.apache.wookie.beans.jcr.impl.SharedDataImpl;
import org.apache.wookie.beans.jcr.impl.StartFileImpl;
import org.apache.wookie.beans.jcr.impl.TokenImpl;
import org.apache.wookie.beans.jcr.impl.WhitelistImpl;
import org.apache.wookie.beans.jcr.impl.WidgetDefaultImpl;
import org.apache.wookie.beans.jcr.impl.WidgetIconImpl;
import org.apache.wookie.beans.jcr.impl.WidgetImpl;
import org.apache.wookie.beans.jcr.impl.WidgetInstanceImpl;
import org.apache.wookie.beans.jcr.impl.WidgetServiceImpl;
import org.apache.wookie.beans.jcr.impl.WidgetTypeImpl;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceCommitException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JCRPersistenceManager - JCR IPersistenceManager implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public class JCRPersistenceManager implements IPersistenceManager
{
    private static final Logger logger = LoggerFactory.getLogger(JCRPersistenceManager.class);
    
    public static final String WIDGET_REPOSITORY_JNDI_REPOSITORY_NAME = "jcr/widgetrepo";
    public static final String WIDGET_REPOSITORY_JNDI_REPOSITORY_FULL_NAME = "java:comp/env/"+WIDGET_REPOSITORY_JNDI_REPOSITORY_NAME;

    public static final String PERSISTENCE_MANAGER_USER_PROPERTY_NAME = "widget.persistence.manager.user";
    public static final String PERSISTENCE_MANAGER_PASSWORD_PROPERTY_NAME = "widget.persistence.manager.password";
    public static final String PERSISTENCE_MANAGER_ROOT_PATH_PROPERTY_NAME = "widget.persistence.manager.rootpath";
    public static final String PERSISTENCE_MANAGER_WORKSPACE_PROPERTY_NAME = "widget.persistence.manager.workspace";

    private static final Map<Class<?>,Class<?>> INTERFACE_TO_CLASS_MAP = new HashMap<Class<?>,Class<?>>();
    private static final Map<Class<? extends IBean>,Class<? extends IBean>> BEAN_INTERFACE_TO_CLASS_MAP = new HashMap<Class<? extends IBean>,Class<? extends IBean>>();
    private static final Map<String,String> IMPLEMENTATION_FIELD_MAP = new HashMap<String,String>();
    static
    {
        INTERFACE_TO_CLASS_MAP.put(IAccessRequest.class, AccessRequestImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IApiKey.class, ApiKeyImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IDescription.class, DescriptionImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IFeature.class, FeatureImpl.class);
        INTERFACE_TO_CLASS_MAP.put(ILicense.class, LicenseImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IName.class, NameImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IParam.class, ParamImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IParticipant.class, ParticipantImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IPreference.class, PreferenceImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IPreferenceDefault.class, PreferenceDefaultImpl.class);
        INTERFACE_TO_CLASS_MAP.put(ISharedData.class, SharedDataImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IStartFile.class, StartFileImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IToken.class, TokenImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IWhitelist.class, WhitelistImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IWidget.class, WidgetImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IWidgetDefault.class, WidgetDefaultImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IWidgetIcon.class, WidgetIconImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IWidgetInstance.class, WidgetInstanceImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IWidgetService.class, WidgetServiceImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IWidgetType.class, WidgetTypeImpl.class);

        BEAN_INTERFACE_TO_CLASS_MAP.put(IAccessRequest.class, AccessRequestImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IApiKey.class, ApiKeyImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IParticipant.class, ParticipantImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IWhitelist.class, WhitelistImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IWidget.class, WidgetImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IWidgetDefault.class, WidgetDefaultImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IWidgetInstance.class, WidgetInstanceImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IWidgetService.class, WidgetServiceImpl.class);
        
        IMPLEMENTATION_FIELD_MAP.put("widget", "widgetImpl");
    }

    private static final List<Class> CLASS_LIST = new ArrayList<Class>(INTERFACE_TO_CLASS_MAP.values());

    private static String repositoryUser;
    private static String repositoryPassword;
    private static String repositoryWorkspace;
    private static String rootPath;
    private static String repositoryRootPath;
    
    private static Map<Class<? extends IBean>,String> beanClassNodeRootPaths = new HashMap<Class<? extends IBean>,String>();
    private static GenericObjectPool ocmPool;
    
    public static final String REPOSITORY_USER_PROPERTY_NAME = "wookie.repository.user";
    public static final String REPOSITORY_PASSWORD_PROPERTY_NAME = "wookie.repository.password";
    public static final String REPOSITORY_ROOT_PATH_PROPERTY_NAME = "wookie.repository.rootpath";
    public static final String REPOSITORY_WORKSPACE_PROPERTY_NAME = "wookie.repository.workspace";

    /**
     * Initialize implementation with configuration.
     * 
     * @param configuration configuration properties
     * @param initializeStore truncate and initialize persistent store
     */
    public static void initialize(Configuration configuration, boolean initializeStore)
    {
        try
        {
            // configuration
            repositoryUser = configuration.getString(PERSISTENCE_MANAGER_USER_PROPERTY_NAME);
            repositoryPassword = configuration.getString(PERSISTENCE_MANAGER_PASSWORD_PROPERTY_NAME);
            repositoryWorkspace = configuration.getString(PERSISTENCE_MANAGER_WORKSPACE_PROPERTY_NAME);
            rootPath = configuration.getString(PERSISTENCE_MANAGER_ROOT_PATH_PROPERTY_NAME);
            for (Map.Entry<Class<? extends IBean>,Class<? extends IBean>> mapping : BEAN_INTERFACE_TO_CLASS_MAP.entrySet())
            {
                Class<? extends IBean> beanClass = mapping.getValue();
                Class<? extends IBean> beanInterface = mapping.getKey();
                String name = beanInterface.getSimpleName();
                if (name.startsWith("I"))
                {
                    name = name.substring(1);
                }
                if (!name.endsWith("s"))
                {
                    name = name+"s";
                }
                String nodeRootPath = rootPath+"/"+name;
                beanClassNodeRootPaths.put(beanClass, nodeRootPath);
            }
            
            // create JCR credentials session pool
            PoolableObjectFactory sessionFactory = new BasePoolableObjectFactory()
            {
                /* (non-Javadoc)
                 * @see org.apache.commons.pool.BasePoolableObjectFactory#passivateObject(java.lang.Object)
                 */
                public void passivateObject(Object obj) throws Exception
                {
                    // clear OCM object cache
                    ((ObjectContentManagerImpl)obj).setRequestObjectCache(new RequestObjectCacheImpl());
                }

                /* (non-Javadoc)
                 * @see org.apache.commons.pool.BasePoolableObjectFactory#makeObject()
                 */
                public Object makeObject() throws Exception
                {
                    // lookup JCR repository from context
                    Context initialContext = new InitialContext();
                    Repository repository = (Repository)initialContext.lookup(WIDGET_REPOSITORY_JNDI_REPOSITORY_FULL_NAME);
                    
                    // create and login JCR session
                    Credentials credentials = new SimpleCredentials(repositoryUser, repositoryPassword.toCharArray());
                    Session session = ((repositoryWorkspace != null) ? repository.login(credentials, repositoryWorkspace) : repository.login(credentials));
                    
                    // return session object content manager for session
                    return new SessionObjectContentManagerImpl(session, new AnnotationMapperImpl(CLASS_LIST));
                }

                /* (non-Javadoc)
                 * @see org.apache.commons.pool.BasePoolableObjectFactory#destroyObject(java.lang.Object)
                 */
                public void destroyObject(Object obj) throws Exception
                {
                    // logout and close object content manager and session
                    ((ObjectContentManagerImpl)obj).logout();
                }
            };
            ocmPool = new GenericObjectPool(sessionFactory, 0, GenericObjectPool.WHEN_EXHAUSTED_GROW, 0, 5);
            ocmPool.setTimeBetweenEvictionRunsMillis(60000);
            ocmPool.setMinEvictableIdleTimeMillis(300000);
            
            // initialize persistent store
            if (initializeStore)
            {
                // borrow object content manager and initialization session from pool
                ObjectContentManager ocm = (ObjectContentManager)ocmPool.borrowObject();
                Session session = ocm.getSession();
                
                // initialize root path in repository
                
                // Jackrabbit/JCR 2.X
                //boolean rootPathNodeExists = session.nodeExists(rootPath);

                // Jackrabbit/JCR 1.X
                boolean rootPathNodeExists = session.itemExists(rootPath);
                
                if (rootPathNodeExists)
                {
                    // delete nodes of root path node

                    // Jackrabbit/JCR 2.X
                    //Node rootNode = session.getNode(rootPath);

                    // Jackrabbit/JCR 1.X
                    Node rootNode = (Node)session.getItem(rootPath);
                    
                    NodeIterator nodesIter = rootNode.getNodes();
                    while (nodesIter.hasNext())
                    {
                        nodesIter.nextNode().remove();
                    }
                }
                else
                {
                    // create unstructured node hierarchy
                    int rootPathParentIndex = -1;
                    int rootPathIndex = rootPath.indexOf('/', 1);
                    while (rootPathIndex != -1)
                    {
                        // Jackrabbit/JCR 2.X
                        //Node parentNode = session.getNode(rootPath.substring(0, ((rootPathParentIndex != -1) ? rootPathParentIndex : 1)));

                        // Jackrabbit/JCR 1.X
                        Node parentNode = (Node)session.getItem(rootPath.substring(0, ((rootPathParentIndex != -1) ? rootPathParentIndex : 1)));

                        String nodeName = rootPath.substring(((rootPathParentIndex != -1) ? rootPathParentIndex+1 : 1), rootPathIndex);
                        parentNode.addNode(nodeName, "nt:unstructured");                        
                        rootPathParentIndex = rootPathIndex;
                        rootPathIndex = rootPath.indexOf('/', rootPathIndex+1);
                    }

                    // Jackrabbit/JCR 2.X
                    //Node parentNode = session.getNode(rootPath.substring(0, ((rootPathParentIndex != -1) ? rootPathParentIndex : 1)));

                    // Jackrabbit/JCR 1.X
                    Node parentNode = (Node)session.getItem(rootPath.substring(0, ((rootPathParentIndex != -1) ? rootPathParentIndex : 1)));

                    String nodeName = rootPath.substring(((rootPathParentIndex != -1) ? rootPathParentIndex+1 : 1));
                    parentNode.addNode(nodeName, "nt:unstructured");
                }
                // create bean class node root paths

                // Jackrabbit/JCR 2.X
                //Node rootNode = session.getNode(rootPath);

                // Jackrabbit/JCR 1.X
                Node rootNode = (Node)session.getItem(rootPath);

                for (String nodeRootPath : beanClassNodeRootPaths.values())
                {
                    String nodeName = nodeRootPath.substring(rootPath.length()+1);
                    rootNode.addNode(nodeName, "nt:unstructured");
                }
                session.save();

                // register/reregister repository node types
                NodeTypeManager nodeTypeManager = session.getWorkspace().getNodeTypeManager();
                InputStream nodeTypesCNDStream = JCRPersistenceManager.class.getResourceAsStream("wookie-schema.cnd");
                if (nodeTypesCNDStream == null)
                {
                    throw new IllegalArgumentException("Unable to load node types configuration: wookie-schema.cnd");                    
                }
                
                // Jackrabbit/JCR 2.X
                //Reader nodeTypesCNDReader = new InputStreamReader(nodeTypesCNDStream);
                //NamespaceRegistry namespaceRegistry = session.getWorkspace().getNamespaceRegistry();
                //ValueFactory valueFactory = session.getValueFactory();
                //CndImporter.registerNodeTypes(nodeTypesCNDReader, "wookie-schema.cnd", nodeTypeManager, namespaceRegistry, valueFactory, true);

                // Jackrabbit/JCR 1.X
                ((NodeTypeManagerImpl)nodeTypeManager).registerNodeTypes(nodeTypesCNDStream, NodeTypeManagerImpl.TEXT_X_JCR_CND, true);

                // save session used to load node types
                session.save();
                logger.info("Persistent store initialized at "+rootPath);

                // return object content manager and initialization session to pool
                ocmPool.returnObject(ocm);
            }
            
            logger.info("Initialized");
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to initialize: "+e, e);
        }
    }

    /**
     * Terminate implementation.
     */
    public static void terminate()
    {
        try
        {
            // close JCR credentials session pool
            ocmPool.close();
            
            logger.info("Terminated");
        }
        catch (Exception e)
        {            
            throw new RuntimeException("Unable to terminate: "+e, e);
        }
    }
    
    public SessionObjectContentManagerImpl ocm;
    public SessionObjectCacheImpl ocmCache;
    public boolean ocmWrite;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#begin()
     */
    public void begin()
    {
        // validate object content manager transaction
        if (ocm != null)
        {
            throw new IllegalStateException("Transaction already initiated");
        }

        // create object content manager session and start transaction
        try
        {
            // borrow session object content manager from pool
            ocm = (SessionObjectContentManagerImpl)ocmPool.borrowObject();
            ocmCache = new SessionObjectCacheImpl();
            ocm.setRequestObjectCache(ocmCache);
            ocmWrite = false;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unexpected exception: "+e, e);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#close()
     */
    public void close()
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // rollback transaction and close object content manager session
        try
        {
            if (!ocmWrite)
            {
                // return session object content manager to pool
                ocmCache.reset();
                ocmPool.returnObject(ocm);
            }
            else
            {
                // logout and close object content manager: do not return to pool
                ocmCache.reset();
                ocm.logout();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unexpected exception: "+e, e);
        }
        finally
        {
            ocm = null;
            ocmCache = null;
            ocmWrite = false;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#commit()
     */
    public void commit() throws PersistenceCommitException
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // commit object content manager session transaction
        try
        {
            // save object content manager writes to repository
            ocm.save();
            ocmWrite = false;
        }
        catch (Exception e)
        {
            throw new PersistenceCommitException("Object content manager write exception: "+e, e);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#delete(org.apache.wookie.beans.IBean)
     */
    public boolean delete(IBean bean)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // track object content manager write
        ocmWrite = true;
        
        // delete bean
        try
        {
            String nodePath = ((IPathBean)bean).getNodePath();
            if (nodePath != null)
            {
                // notify pre-delete listener
                if (bean instanceof IPersistenceListener)
                {
                    if (!((IPersistenceListener)bean).preDelete(this))
                    {
                        throw new RuntimeException("Pre-delete listener invocation failed");
                    }
                }
                
                // delete node
                ocm.remove(nodePath);
                ocmCache.removeObject(nodePath);

                // notify post-delete listener
                if (bean instanceof IPersistenceListener)
                {
                    if (!((IPersistenceListener)bean).postDelete(this))
                    {
                        throw new RuntimeException("Post-delete listener invocation failed");
                    }
                }                
                return true;
            }
        }
        catch (Exception e)
        {
            logger.error("Unexpected exception: "+e, e);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#delete(org.apache.wookie.beans.IBean[])
     */
    public boolean delete(IBean[] beans)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // track object content manager write
        ocmWrite = true;
        
        // remove beans
        try
        {
            for (IBean bean : beans)
            {
                String nodePath = ((IPathBean)bean).getNodePath();
                if (nodePath != null)
                {
                    // notify pre-delete listener
                    if (bean instanceof IPersistenceListener)
                    {
                        if (!((IPersistenceListener)bean).preDelete(this))
                        {
                            throw new RuntimeException("Pre-delete listener invocation failed");
                        }
                    }
                    
                    // delete node
                    ocm.remove(nodePath);
                    ocmCache.removeObject(nodePath);

                    // notify post-delete listener
                    if (bean instanceof IPersistenceListener)
                    {
                        if (!((IPersistenceListener)bean).postDelete(this))
                        {
                            throw new RuntimeException("Post-delete listener invocation failed");
                        }
                    }                
                }
            }
            return true;
        }
        catch (Exception e)
        {
            logger.error("Unexpected exception: "+e, e);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findAll(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T extends IBean> T[] findAll(Class<T> beansInterface)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // validate bean interface
        Class<? extends IBean> beanClass = BEAN_INTERFACE_TO_CLASS_MAP.get(beansInterface);
        if (beanClass == null)
        {
            throw new IllegalArgumentException("Invalid bean interface specified");            
        }

        // get persistent bean extent by root path
        try
        {
            // construct query filter
            QueryManager queryManager = ocm.getQueryManager();
            Filter filter = queryManager.createFilter(beanClass);
            filter.setScope(beanClassNodeRootPaths.get(beanClass)+"//");
            Query query = queryManager.createQuery(filter);
            
            // invoke query
            Collection<? extends IBean> beansExtentList = ocm.getObjects(query);
            if ((beansExtentList != null) && !beansExtentList.isEmpty())
            {
                return beansExtentList.toArray((T [])Array.newInstance(beansInterface, beansExtentList.size()));
            }
        }
        catch (Exception e)
        {
            logger.error("Unexpected exception: "+e, e);
        }
        return (T [])Array.newInstance(beansInterface, 0);
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findApplicableAccessRequests(org.apache.wookie.beans.IWidget)
     */
    public IAccessRequest [] findApplicableAccessRequests(IWidget widget)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // get applicable access requests for widget using custom query
        if (widget != null)
        {
            try
            {
                Map<String, Object> values = new HashMap<String, Object>();
                values.put("widget", widget);
                values.put("granted", Boolean.TRUE);
                return findByValues(IAccessRequest.class, values);
            }
            catch (Exception e)
            {
                logger.error("Unexpected exception: "+e, e);
            }
        }
        return new IAccessRequest[0];
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findById(java.lang.Class, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public <T extends IBean> T findById(Class<T> beanInterface, Object id)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // validate bean interface
        Class<? extends IBean> beanClass = BEAN_INTERFACE_TO_CLASS_MAP.get(beanInterface);
        if (beanClass == null)
        {
            throw new IllegalArgumentException("Invalid bean interface specified");            
        }

        // get persistent bean by primary id path
        try
        {
            if (!(id instanceof String) && (id != null))
            {
                id = id.toString();
            }
            return (T)ocm.getObject((String)id);
        }
        catch (Exception e)
        {
            logger.error("Unexpected exception: "+e, e);
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findByValue(java.lang.Class, java.lang.String, java.lang.Object, java.lang.String, boolean)
     */
    @SuppressWarnings("unchecked")
    public <T extends IBean> T[] findByValue(Class<T> beansInterface, String name, Object value, String orderBy, boolean ascending)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // validate bean interface
        Class<? extends IBean> beanClass = BEAN_INTERFACE_TO_CLASS_MAP.get(beansInterface);
        if (beanClass == null)
        {
            throw new IllegalArgumentException("Invalid bean interface specified");            
        }

        // get persistent beans by root path and filter
        try
        {
            // construct query filter
            QueryManager queryManager = ocm.getQueryManager();
            Filter filter = queryManager.createFilter(beanClass);
            filter.setScope(beanClassNodeRootPaths.get(beanClass)+"//");
            if (name != null)
            {
                String mappedName = IMPLEMENTATION_FIELD_MAP.get(name);
                name = ((mappedName != null) ? mappedName : name);
                if (value instanceof IUuidBean)
                {
                    value = ((IUuidBean)value).getUuid(); 
                }
                filter = ((value != null) ? filter.addEqualTo(name, value) : filter.addIsNull(name));
            }            
            Query query = queryManager.createQuery(filter);
            if (orderBy != null)
            {
                String mappedOrderBy = IMPLEMENTATION_FIELD_MAP.get(orderBy);
                orderBy = ((mappedOrderBy != null) ? mappedOrderBy : orderBy);
                if (ascending)
                {
                    query.addOrderByAscending(orderBy);
                }
                else
                {
                    query.addOrderByDescending(orderBy);
                }
            }

            // invoke query
            Collection<? extends IBean> beansExtentList = ocm.getObjects(query);
            if ((beansExtentList != null) && !beansExtentList.isEmpty())
            {
                return beansExtentList.toArray((T [])Array.newInstance(beansInterface, beansExtentList.size()));
            }
        }
        catch (Exception e)
        {
            logger.error("Unexpected exception: "+e, e);
        }
        return (T [])Array.newInstance(beansInterface, 0);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findByValue(java.lang.Class, java.lang.String, java.lang.Object)
     */
    public <T extends IBean> T[] findByValue(Class<T> beansInterface, String name, Object value)
    {
        return findByValue(beansInterface, name, value, null, true);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findByValues(java.lang.Class, java.util.Map, java.lang.String, boolean)
     */
    @SuppressWarnings("unchecked")
    public <T extends IBean> T[] findByValues(Class<T> beansInterface, Map<String, Object> values, String orderBy, boolean ascending)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // validate bean interface
        Class<? extends IBean> beanClass = BEAN_INTERFACE_TO_CLASS_MAP.get(beansInterface);
        if (beanClass == null)
        {
            throw new IllegalArgumentException("Invalid bean interface specified");            
        }

        // get persistent beans by root path and filter
        try
        {
            // construct query filter
            QueryManager queryManager = ocm.getQueryManager();
            Filter filter = queryManager.createFilter(beanClass);
            filter.setScope(beanClassNodeRootPaths.get(beanClass)+"//");
            if ((values != null) && !values.isEmpty())
            {
                for (Map.Entry<String,Object> value : values.entrySet())
                {
                    String valueName = value.getKey();
                    String mappedValueName = IMPLEMENTATION_FIELD_MAP.get(valueName);
                    valueName = ((mappedValueName != null) ? mappedValueName : valueName);
                    Object valueValue = value.getValue();
                    if (valueValue instanceof IUuidBean)
                    {
                        valueValue = ((IUuidBean)valueValue).getUuid(); 
                    }
                    filter = ((valueValue != null) ? filter.addEqualTo(valueName, valueValue) : filter.addIsNull(valueName));
                }
            }
            Query query = queryManager.createQuery(filter);
            if (orderBy != null)
            {
                String mappedOrderBy = IMPLEMENTATION_FIELD_MAP.get(orderBy);
                orderBy = ((mappedOrderBy != null) ? mappedOrderBy : orderBy);
                if (ascending)
                {
                    query.addOrderByAscending(orderBy);
                }
                else
                {
                    query.addOrderByDescending(orderBy);
                }
            }

            // invoke query
            Collection<? extends IBean> beansExtentList = ocm.getObjects(query);
            if ((beansExtentList != null) && !beansExtentList.isEmpty())
            {
                return beansExtentList.toArray((T [])Array.newInstance(beansInterface, beansExtentList.size()));
            }
        }
        catch (Exception e)
        {
            logger.error("Unexpected exception: "+e, e);
        }
        return (T [])Array.newInstance(beansInterface, 0);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findByValues(java.lang.Class, java.util.Map)
     */
    public <T extends IBean> T[] findByValues(Class<T> beansInterface, Map<String, Object> values)
    {
        return findByValues(beansInterface, values, null, true);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findWidgetByGuid(java.lang.String)
     */
    public IWidget findWidgetByGuid(String guid)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // get widget by GUID
        if (guid != null)
        {
            try
            {
                IWidget [] widget = findByValue(IWidget.class, "guid", guid);
                if (widget.length == 1)
                {
                    return widget[0];
                }
            }
            catch (Exception e)
            {
                logger.error("Unexpected exception: "+e, e);
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findWidgetDefaultByType(java.lang.String)
     */
    public IWidget findWidgetDefaultByType(String widgetContext)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // get default widget by type
        if (widgetContext != null)
        {
            try
            {
                IWidgetDefault [] widgetDefault = findByValue(IWidgetDefault.class, "widgetContext", widgetContext);
                if (widgetDefault.length == 1)
                {
                    return widgetDefault[0].getWidget();
                }
            }
            catch (Exception e)
            {
                logger.error("Unexpected exception: "+e, e);
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findWidgetInstance(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public IWidgetInstance findWidgetInstance(String apiKey, String userId, String sharedDataKey, String serviceContext)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // get widget instance
        if ((apiKey != null) && (userId != null) && (sharedDataKey != null) && (serviceContext != null))
        {
            try
            {
                // get candidate widget instances
                Map<String, Object> values = new HashMap<String, Object>();
                values.put("apiKey", apiKey);
                values.put("userId", userId);
                values.put("sharedDataKey", sharedDataKey);
                IWidgetInstance [] widgetInstances = findByValues(IWidgetInstance.class, values);

                // filter widget instances by widget type/context
                IWidgetInstance foundWidgetInstance = null;
                for (IWidgetInstance widgetInstance : widgetInstances)
                {
                    // check widget type/context
                    boolean hasServiceContext = false;
                    for (IWidgetType widgetType : widgetInstance.getWidget().getWidgetTypes())
                    {
                        if (widgetType.getWidgetContext().equals(serviceContext))
                        {
                            hasServiceContext = true;
                            break;
                        }
                    }
                    if (hasServiceContext)
                    {
                        // validate search matches only one widget
                        if (foundWidgetInstance != null)
                        {
                            foundWidgetInstance = null;
                            break;
                        }
                        foundWidgetInstance = widgetInstance;
                    }
                }

                // return single matching widget instance
                if (foundWidgetInstance != null)
                {
                    return foundWidgetInstance;
                }
            }
            catch (Exception e)
            {
                logger.error("Unexpected exception: "+e, e);
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findWidgetInstanceByGuid(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public IWidgetInstance findWidgetInstanceByGuid(String apiKey, String userId, String sharedDataKey, String widgetGuid)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // get widget instance
        if ((apiKey != null) && (userId != null) && (sharedDataKey != null) && (widgetGuid != null))
        {
            try
            {
                // find widget by GUID
                IWidget widget = findWidgetByGuid(widgetGuid);
                if (widget != null)
                {
                    // get matching widget instance for widget
                    Map<String, Object> values = new HashMap<String, Object>();
                    values.put("apiKey", apiKey);
                    values.put("userId", userId);
                    values.put("sharedDataKey", sharedDataKey);
                    values.put("widget", widget);
                    IWidgetInstance [] widgetInstance = findByValues(IWidgetInstance.class, values);
                    if (widgetInstance.length == 1)
                    {
                        return widgetInstance[0];
                    }                
                }
            }
            catch (Exception e)
            {
                logger.error("Unexpected exception: "+e, e);
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findWidgetInstanceByIdKey(java.lang.String)
     */
    public IWidgetInstance findWidgetInstanceByIdKey(String idKey)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // get widget instance
        if (idKey != null)
        {
            try
            {
                IWidgetInstance [] widgetInstance = findByValue(IWidgetInstance.class, "idKey", idKey);
                if (widgetInstance.length == 1)
                {
                    return widgetInstance[0];
                }
            }
            catch (Exception e)
            {
                logger.error("Unexpected exception: "+e, e);
            }
        }
        return null;
    }

    public IWidget[] findWidgetsByType(String widgetContext)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }
        
        // get widgets by type
        if (widgetContext != null)
        {
            try
            {
                // get candidate widgets
                IWidget [] widgets = findAll(IWidget.class);
                
                // filter widgets by widget type/context
                Collection<IWidget> foundWidgets = new ArrayList<IWidget>();
                for (IWidget widget : widgets)
                {
                    // check widget type/context
                    for (IWidgetType widgetType : widget.getWidgetTypes())
                    {
                        if (widgetType.getWidgetContext().equals(widgetContext))
                        {
                            foundWidgets.add(widget);
                            break;
                        }
                    }
                }
                return foundWidgets.toArray(new IWidget[foundWidgets.size()]);
            }
            catch (Exception e)
            {
                logger.error("Unexpected exception: "+e, e);
            }
        }
        return new IWidget[0];
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#newInstance(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T> T newInstance(Class<T> instanceInterface)
    {
        // validate instance interface
        Class<?> instanceClass = INTERFACE_TO_CLASS_MAP.get(instanceInterface);
        if (instanceClass == null)
        {
            throw new IllegalArgumentException("Invalid instance interface specified");            
        }

        // create new instance of persistent class
        try
        {
            return (T)instanceClass.newInstance();
        }
        catch (Exception e)
        {
            logger.error("Unexpected exception: "+e, e);
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#rollback()
     */
    public void rollback()
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // rollback object content manager session transaction
        try
        {
            if (ocmWrite)
            {
                // logout and close object content manager: do not return to pool
                ocmCache.reset();
                ocm.logout();
            }
            // borrow session object content manager from pool
            ocm = (SessionObjectContentManagerImpl)ocmPool.borrowObject();
            ocmCache = new SessionObjectCacheImpl();
            ocm.setRequestObjectCache(ocmCache);
            ocmWrite = false;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unexpected exception: "+e, e);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#save(org.apache.wookie.beans.IBean)
     */
    public boolean save(IBean bean)
    {
        // validate object content manager transaction
        if (ocm == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }

        // track object content manager write
        ocmWrite = true;
        
        // persist new or update existing bean
        boolean newNode = false;
        IPathBean pathBean = (IPathBean)bean;
        String nodePath = pathBean.getNodePath();
        if (nodePath == null)
        {
            nodePath = pathBean.setNodePath(beanClassNodeRootPaths.get(pathBean.getClass()));
            newNode = true;
        }
        try
        {
            // notify pre-save listener
            if (bean instanceof IPersistenceListener)
            {
                if (!((IPersistenceListener)bean).preSave(this))
                {
                    throw new RuntimeException("Pre-save listener invocation failed");
                }
            }
            
            // save node
            if (newNode)
            {
                // construct parent nodes if necessary
                constructParentNodes(nodePath);
                // insert new node
                ocm.insert(pathBean);
            }
            else
            {
                // update existing node
                ocm.update(pathBean);
            }
            ocmCache.cache(pathBean.getNodePath(), pathBean);

            // notify post-save listener
            if (bean instanceof IPersistenceListener)
            {
                if (!((IPersistenceListener)bean).postSave(this))
                {
                    throw new RuntimeException("Post-save listener invocation failed");                    
                }
            }
            return true;
        }
        catch (Exception e)
        {
            logger.error("Unexpected exception: "+e, e);
            return false;
        }
    }
    
    /**
     * Create parent node hierarchy if necessary.
     * 
     * @param nodePath node path
     */
    private Node constructParentNodes(String nodePath) throws RepositoryException
    {
        // recursively create parent nodes
        Session session = ocm.getSession();
        int parentNodePathEndIndex = nodePath.lastIndexOf('/');
        if (parentNodePathEndIndex > 0)
        {
            String parentNodePath = nodePath.substring(0, parentNodePathEndIndex);
            if (!parentNodePath.equals(rootPath))
            {
                // Jackrabbit/JCR 2.X
                //boolean parentNodeExists = session.nodeExists(parentNodePath);
                
                // Jackrabbit/JCR 1.X
                boolean parentNodeExists = session.itemExists(parentNodePath);

                if (!parentNodeExists)
                {
                    // create parent parent nodes
                    Node parentNode = constructParentNodes(parentNodePath);
                    // create parent node
                    String nodeName = parentNodePath.substring(parentNodePath.lastIndexOf('/')+1);
                    return parentNode.addNode(nodeName, "nt:unstructured");
                }
            }

            // Jackrabbit/JCR 2.X
            //return session.getNode(parentNodePath);

            // Jackrabbit/JCR 1.X
            return (Node)session.getItem(parentNodePath);
        }
        return session.getRootNode();
    }
    
    /**
     * Escape invalid JCR characters in name.
     * 
     * @param name original name
     * @return escaped or original name
     */
    public static String escapeJCRName(String name)
    {
        StringBuilder escapedName = null;
        if (name != null)
        {
            for (int i = 0, limit = name.length(); (i < limit); i++)
            {
                char nameChar = name.charAt(i);
                switch (nameChar)
                {
                    case '%' :
                    case '/' :
                    case ':' :
                    case '[' :
                    case ']' :
                    case '|' :
                    case '*' :
                        if (escapedName == null)
                        {
                            escapedName = new StringBuilder(name.substring(0, i));
                        }
                        escapedName.append('%');
                        escapedName.append(Character.toUpperCase(Character.forDigit(nameChar/16, 16)));
                        escapedName.append(Character.toUpperCase(Character.forDigit(nameChar%16, 16)));
                        break;
                    default :
                        if (escapedName != null)
                        {
                            escapedName.append(nameChar);
                        }
                        break;
                }
            }
        }
        return ((escapedName != null) ? escapedName.toString() : name);
    }

    /**
     * Unescape invalid JCR characters in name.
     * 
     * @param escapedName escaped name
     * @return original name
     */
    public static String unescapeJCRName(String escapedName)
    {
        StringBuilder unescapedName = null;
        if (escapedName != null)
        {
            for (int i = 0, limit = escapedName.length(); (i < limit); i++)
            {
                char escapedNameChar = escapedName.charAt(i);
                if (escapedNameChar == '%')
                {
                    if (unescapedName == null)
                    {
                        unescapedName = new StringBuilder(escapedName.substring(0, i));
                    }
                    int high = Character.digit(escapedName.charAt(++i), 16);
                    int low = Character.digit(escapedName.charAt(++i), 16);
                    unescapedName.append((char)(high*16+low));
                }
                else if (unescapedName != null)
                {
                    unescapedName.append(escapedNameChar);
                }
            }
        }
        return ((unescapedName != null) ? unescapedName.toString() : escapedName);        
    }

    /**
     * Extended OCM that supports setting of object cache after construction.
     */
    private static class SessionObjectContentManagerImpl extends ObjectContentManagerImpl
    {
        /**
         * Base OCM constructor with session and mapper.
         * 
         * @param session session for OCM to manage
         * @param mapper object mapper component
         */
        public SessionObjectContentManagerImpl(Session session, Mapper mapper)
        {
            super(session, mapper);
        }
        
        /* (non-Javadoc)
         * @see org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl#setRequestObjectCache(org.apache.jackrabbit.ocm.manager.cache.ObjectCache)
         */
        public void setRequestObjectCache(ObjectCache requestObjectCache)
        {
            super.setObjectConverter(new ObjectConverterImpl(mapper, new DefaultAtomicTypeConverterProvider(), new ProxyManagerImpl(), requestObjectCache));
            super.setRequestObjectCache(requestObjectCache);
        }
    }
    
    /**
     * Customized OCM object cache that is not cleared between requests.
     */
    private static class SessionObjectCacheImpl implements ObjectCache
    {
        private Map<String,Object> cache = new HashMap<String,Object>();
        
        /* (non-Javadoc)
         * @see org.apache.jackrabbit.ocm.manager.cache.ObjectCache#cache(java.lang.String, java.lang.Object)
         */
        public void cache(String path, Object object)
        {
            // add to cache
            cache.put(path, object);
        }

        /* (non-Javadoc)
         * @see org.apache.jackrabbit.ocm.manager.cache.ObjectCache#clear()
         */
        public void clear()
        {
            // do not clear cache on OCM request
        }

        /* (non-Javadoc)
         * @see org.apache.jackrabbit.ocm.manager.cache.ObjectCache#getObject(java.lang.String)
         */
        public Object getObject(String path)
        {
            // retrieve from cache
            return cache.get(path);
        }

        /* (non-Javadoc)
         * @see org.apache.jackrabbit.ocm.manager.cache.ObjectCache#isCached(java.lang.String)
         */
        public boolean isCached(String path)
        {
            // cache check
            return cache.containsKey(path);
        }

        /**
         * Clear cache on reset.
         */
        public void reset()
        {
            // clear cache
            cache.clear();
        }

        /**
         * Remove object from cache.
         * 
         * @param path object path
         * @return removed object
         */
        public Object removeObject(String path)
        {
            // remove from cache
            return cache.remove(path);
        }
    }
}
