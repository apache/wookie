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

package org.apache.wookie.beans.jpa;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.apache.commons.configuration.Configuration;
import org.apache.openjpa.persistence.Extent;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.QueryResultCache;
import org.apache.openjpa.persistence.StoreCache;
import org.apache.wookie.beans.IAccessRequest;
import org.apache.wookie.beans.IApiKey;
import org.apache.wookie.beans.IAuthor;
import org.apache.wookie.beans.IBean;
import org.apache.wookie.beans.IDescription;
import org.apache.wookie.beans.IFeature;
import org.apache.wookie.beans.ILicense;
import org.apache.wookie.beans.IName;
import org.apache.wookie.beans.IOAuthToken;
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
import org.apache.wookie.beans.jpa.impl.AccessRequestImpl;
import org.apache.wookie.beans.jpa.impl.ApiKeyImpl;
import org.apache.wookie.beans.jpa.impl.AuthorImpl;
import org.apache.wookie.beans.jpa.impl.DescriptionImpl;
import org.apache.wookie.beans.jpa.impl.FeatureImpl;
import org.apache.wookie.beans.jpa.impl.LicenseImpl;
import org.apache.wookie.beans.jpa.impl.NameImpl;
import org.apache.wookie.beans.jpa.impl.OAuthTokenImpl;
import org.apache.wookie.beans.jpa.impl.ParamImpl;
import org.apache.wookie.beans.jpa.impl.ParticipantImpl;
import org.apache.wookie.beans.jpa.impl.PreferenceDefaultImpl;
import org.apache.wookie.beans.jpa.impl.PreferenceImpl;
import org.apache.wookie.beans.jpa.impl.SharedDataImpl;
import org.apache.wookie.beans.jpa.impl.StartFileImpl;
import org.apache.wookie.beans.jpa.impl.TokenImpl;
import org.apache.wookie.beans.jpa.impl.WhitelistImpl;
import org.apache.wookie.beans.jpa.impl.WidgetDefaultImpl;
import org.apache.wookie.beans.jpa.impl.WidgetIconImpl;
import org.apache.wookie.beans.jpa.impl.WidgetImpl;
import org.apache.wookie.beans.jpa.impl.WidgetInstanceImpl;
import org.apache.wookie.beans.jpa.impl.WidgetServiceImpl;
import org.apache.wookie.beans.jpa.impl.WidgetTypeImpl;
import org.apache.wookie.beans.util.DatabaseUtils;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceCommitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JPAPersistenceManager - JPA IPersistenceManager implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public class JPAPersistenceManager implements IPersistenceManager
{
    private static final Logger logger = LoggerFactory.getLogger(JPAPersistenceManager.class);
    
    public static final String WIDGET_DATABASE_JNDI_DATASOURCE_NAME = "jdbc/widgetdb";
    public static final String WIDGET_DATABASE_JNDI_DATASOURCE_FULL_NAME = "java:comp/env/"+WIDGET_DATABASE_JNDI_DATASOURCE_NAME;

    public static final String PERSISTENCE_MANAGER_CACHE_SIZE_PROPERTY_NAME = "widget.persistence.manager.cachesize";
    public static final String PERSISTENCE_MANAGER_DB_TYPE_PROPERTY_NAME = "widget.persistence.manager.dbtype";
    
    private static final Map<Class<?>,Class<?>> INTERFACE_TO_CLASS_MAP = new HashMap<Class<?>,Class<?>>();
    private static final Map<Class<? extends IBean>,Class<? extends IBean>> BEAN_INTERFACE_TO_CLASS_MAP = new HashMap<Class<? extends IBean>,Class<? extends IBean>>();
    private static final Map<Class<? extends IBean>,Class<?>> BEAN_INTERFACE_TO_ID_FIELD_TYPE_MAP = new HashMap<Class<? extends IBean>,Class<?>>();
    private static final Map<String,String> DB_TYPE_TO_JPA_DICTIONARY_MAP = new HashMap<String,String>();
    static
    {
        INTERFACE_TO_CLASS_MAP.put(IAccessRequest.class, AccessRequestImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IApiKey.class, ApiKeyImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IDescription.class, DescriptionImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IFeature.class, FeatureImpl.class);
        INTERFACE_TO_CLASS_MAP.put(ILicense.class, LicenseImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IName.class, NameImpl.class);
        INTERFACE_TO_CLASS_MAP.put(IAuthor.class, AuthorImpl.class);
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
        INTERFACE_TO_CLASS_MAP.put(IOAuthToken.class, OAuthTokenImpl.class);

        BEAN_INTERFACE_TO_CLASS_MAP.put(IAccessRequest.class, AccessRequestImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IApiKey.class, ApiKeyImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IParticipant.class, ParticipantImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IPreference.class, PreferenceImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(ISharedData.class, SharedDataImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IWhitelist.class, WhitelistImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IWidget.class, WidgetImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IWidgetDefault.class, WidgetDefaultImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IWidgetInstance.class, WidgetInstanceImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IWidgetService.class, WidgetServiceImpl.class);
        BEAN_INTERFACE_TO_CLASS_MAP.put(IOAuthToken.class, OAuthTokenImpl.class);

        BEAN_INTERFACE_TO_ID_FIELD_TYPE_MAP.put(IAccessRequest.class, Integer.class);
        BEAN_INTERFACE_TO_ID_FIELD_TYPE_MAP.put(IApiKey.class, Integer.class);
        BEAN_INTERFACE_TO_ID_FIELD_TYPE_MAP.put(IParticipant.class, Integer.class);
        BEAN_INTERFACE_TO_ID_FIELD_TYPE_MAP.put(IWhitelist.class, Integer.class);
        BEAN_INTERFACE_TO_ID_FIELD_TYPE_MAP.put(IWidget.class, Integer.class);
        BEAN_INTERFACE_TO_ID_FIELD_TYPE_MAP.put(IWidgetDefault.class, String.class);
        BEAN_INTERFACE_TO_ID_FIELD_TYPE_MAP.put(IWidgetInstance.class, Integer.class);
        BEAN_INTERFACE_TO_ID_FIELD_TYPE_MAP.put(IWidgetService.class, Integer.class);
        BEAN_INTERFACE_TO_ID_FIELD_TYPE_MAP.put(IOAuthToken.class, Integer.class);
        
        DB_TYPE_TO_JPA_DICTIONARY_MAP.put("db2", "db2");
        DB_TYPE_TO_JPA_DICTIONARY_MAP.put("derby", "derby");
        DB_TYPE_TO_JPA_DICTIONARY_MAP.put("hsqldb", "hsql");
        DB_TYPE_TO_JPA_DICTIONARY_MAP.put("mssql", "sqlserver");
        DB_TYPE_TO_JPA_DICTIONARY_MAP.put("mysql", "mysql");
        DB_TYPE_TO_JPA_DICTIONARY_MAP.put("mysql5", "mysql");
        DB_TYPE_TO_JPA_DICTIONARY_MAP.put("oracle", "oracle");
        DB_TYPE_TO_JPA_DICTIONARY_MAP.put("oracle9", "oracle");
        DB_TYPE_TO_JPA_DICTIONARY_MAP.put("oracle10", "oracle");
        DB_TYPE_TO_JPA_DICTIONARY_MAP.put("postgresql", "postgres");
        DB_TYPE_TO_JPA_DICTIONARY_MAP.put("sybase", "sybase");
    }

    private static String cacheSize;
    private static String dbType;
    private static String dictionaryType;
    private static OpenJPAEntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    
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
            cacheSize = configuration.getString(PERSISTENCE_MANAGER_CACHE_SIZE_PROPERTY_NAME);
            dbType = configuration.getString(PERSISTENCE_MANAGER_DB_TYPE_PROPERTY_NAME);
            dictionaryType = ((dbType != null) ? DB_TYPE_TO_JPA_DICTIONARY_MAP.get(dbType) : null);
            if ((dbType != null) && (dictionaryType == null)) 
            {
                throw new IllegalArgumentException("Unsupported database type: "+dbType);
            }
            
            // if we are not initializing the store and we are using derby, then check to see if the DB files
            // exist in the filesystem. If they do not then override and create them.
            if(!initializeStore && dbType.equals("derby")){
                initializeStore = DatabaseUtils.derbyDatabaseDoesNotExist();
            }

            // initialize persistent store
            if (initializeStore && (dbType != null))
            {
                // get datasource connection
                Context initialContext = new InitialContext();
                DataSource dataSource = (DataSource)initialContext.lookup(WIDGET_DATABASE_JNDI_DATASOURCE_FULL_NAME);
                Connection connection = dataSource.getConnection();
                connection.setAutoCommit(true);

                // execute initialization scripts
                String sqlScriptResource = dbType+"-wookie-schema.sql";
                InputStream sqlStream = JPAPersistenceManager.class.getResourceAsStream(sqlScriptResource);
                if (sqlStream == null)
                {
                    throw new IllegalArgumentException("Unsupported persistent store initialization script: "+sqlScriptResource);                    
                }
                SQLScriptReader reader = new SQLScriptReader(sqlStream);
                int statementCount = 0;
                int statementErrorCount = 0;
                for (;;)
                {
                    String scriptStatement = reader.readSQLStatement();
                    if (scriptStatement != null)
                    {
                        Statement statement = connection.createStatement();
                        statementCount++;
                        try
                        {
                            statement.execute(scriptStatement);
                        }
                        catch (SQLException sqle)
                        {
                            statementErrorCount++;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
                logger.info("Persistent store initialized from "+sqlScriptResource+", ("+statementCount+" statements, "+statementErrorCount+" errors)");
                    
                // close datasource connection
                connection.close();
            }
            
            // initialize entity manager factory
            Properties persistenceProperties = new Properties();
            InputStream propertiesStream = JPAPersistenceManager.class.getResourceAsStream("persistence.properties");
            if (propertiesStream == null)
            {
                throw new IllegalArgumentException("Unable to load configuration: persistence.properties");
            }            
            persistenceProperties.load(propertiesStream);
            if (cacheSize != null)
            {
                int dataCacheSize = Integer.parseInt(cacheSize);
                persistenceProperties.setProperty("openjpa.DataCache", "true(CacheSize="+dataCacheSize+",SoftReferenceSize=0)");                
                int queryCacheSize = Integer.parseInt(cacheSize)/10;
                persistenceProperties.setProperty("openjpa.QueryCache", "CacheSize="+queryCacheSize+",SoftReferenceSize=0");                
            }
            if (dictionaryType != null)
            {
                persistenceProperties.setProperty("openjpa.jdbc.DBDictionary", dictionaryType);
            }
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("wookie", persistenceProperties);
            entityManagerFactory = OpenJPAPersistence.cast(factory);

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
            // evict all caches
            StoreCache cache = entityManagerFactory.getStoreCache();
            cache.evictAll();
            QueryResultCache queryCache = entityManagerFactory.getQueryResultCache();
            queryCache.evictAll();
            
            // close entity manager factory
            entityManagerFactory.close();
            entityManagerFactory = null;
            
            logger.info("Terminated");
        }
        catch (Exception e)
        {            
            throw new RuntimeException("Unable to terminate: "+e, e);
        }
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#begin()
     */
    public void begin()
    {
        // validate entity manager transaction
        if (entityManager != null)
        {
            throw new IllegalStateException("Transaction already initiated");
        }

        // create entity manager and start transaction
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive())
        {
            transaction.begin();
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#close()
     */
    public void close()
    {
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // rollback transaction and close entity manager
        EntityTransaction transaction = entityManager.getTransaction();
        if (transaction.isActive())
        {
            transaction.rollback();
        }
        entityManager.clear();
        entityManager.close();
        entityManager = null;            
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#commit()
     */
    public void commit() throws PersistenceCommitException
    {
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // commit transaction
        EntityTransaction transaction = entityManager.getTransaction();
        if (transaction.isActive())
        {
            try
            {
                transaction.commit();
            }
            catch (RollbackException re)
            {
                throw new PersistenceCommitException("Transaction commit exception: "+re, re);
            }
            catch (OptimisticLockException ole)
            {
                throw new PersistenceCommitException("Transaction locking/version commit exception: "+ole, ole);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#delete(org.apache.wookie.beans.IBean)
     */
    public boolean delete(IBean bean)
    {
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // remove bean
        try
        {
            entityManager.remove(bean);
            return true;
        }
        catch (Exception e)
        {
            logger.error("Unexpected exception: "+e, e);
            return false;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#delete(org.apache.wookie.beans.IBean[])
     */
    public boolean delete(IBean[] beans)
    {
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // remove beans
        try
        {
            for (IBean bean : beans)
            {
                entityManager.remove(bean);
            }
            return true;
        }
        catch (Exception e)
        {
            logger.error("Unexpected exception: "+e, e);
            return false;
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findAll(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T extends IBean> T [] findAll(Class<T> beansInterface)
    {
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // validate bean interface
        Class<? extends IBean> beanClass = BEAN_INTERFACE_TO_CLASS_MAP.get(beansInterface);
        if (beanClass == null)
        {
            throw new IllegalArgumentException("Invalid bean interface specified");            
        }

        // get persistent bean extent
        try
        {
            OpenJPAEntityManager manager = OpenJPAPersistence.cast(entityManager);
            Extent<? extends IBean> beansExtent = manager.createExtent(beanClass, true);
            List<? extends IBean> beansExtentList = beansExtent.list();
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
    @SuppressWarnings("unchecked")
    public IAccessRequest [] findApplicableAccessRequests(IWidget widget)
    {
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // get applicable access requests for widget using custom query
        if (widget != null)
        {
            try
            {
                Query query = entityManager.createNamedQuery("ACCESS_REQUESTS");
                query.setParameter("widget", widget);
                List<IParticipant> accessRequestsList = query.getResultList();
                if ((accessRequestsList != null) && !accessRequestsList.isEmpty())
                {
                    return accessRequestsList.toArray(new IAccessRequest[accessRequestsList.size()]);
                }
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
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // validate bean interface
        Class<? extends IBean> beanClass = BEAN_INTERFACE_TO_CLASS_MAP.get(beanInterface);
        Class<?> idFieldType = BEAN_INTERFACE_TO_ID_FIELD_TYPE_MAP.get(beanInterface);
        if ((beanClass == null) || (idFieldType == null))
        {
            throw new IllegalArgumentException("Invalid bean interface specified");            
        }

        // get persistent bean by primary key
        try
        {
            if (id != null)
            {
                if ((idFieldType == Integer.class) && !(id instanceof Integer))
                {
                    try
                    {
                        id = new Integer(id.toString());
                    }
                    catch (NumberFormatException nfe)
                    {
                        return null;
                    }
                }
                else if ((idFieldType == String.class) && !(id instanceof String))
                {
                    id = id.toString();
                }
            }
            return (T)entityManager.find(beanClass, id);
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
    public <T extends IBean> T [] findByValue(Class<T> beansInterface, String name, Object value, String orderBy, boolean ascending)
    {
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // validate bean interface
        Class<? extends IBean> beanClass = BEAN_INTERFACE_TO_CLASS_MAP.get(beansInterface);
        if (beanClass == null)
        {
            throw new IllegalArgumentException("Invalid bean interface specified");            
        }

        // get persistent beans by criteria
        try
        {
            // construct query criteria
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<? extends IBean> criteriaQuery = criteriaBuilder.createQuery(beanClass);
            Root<? extends IBean> beanRoot = criteriaQuery.from(beanClass);
            if (name != null)
            {
                criteriaQuery.where((value != null) ? criteriaBuilder.equal(beanRoot.get(name), value) : criteriaBuilder.isNull(beanRoot.get(name)));
            }
            if (orderBy != null)
            {
                criteriaQuery.orderBy(ascending ? criteriaBuilder.asc(beanRoot.get(orderBy)) : criteriaBuilder.desc(beanRoot.get(orderBy)));
            }
            
            // invoke query
            Query query = entityManager.createQuery(criteriaQuery);
            List<? extends IBean> beansList = query.getResultList();
            if ((beansList != null) && !beansList.isEmpty())
            {
                return beansList.toArray((T [])Array.newInstance(beansInterface, beansList.size()));
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
    public <T extends IBean> T [] findByValue(Class<T> beansInterface, String name, Object value)
    {
        return findByValue(beansInterface, name, value, null, true);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findByValues(java.lang.Class, java.util.Map, java.lang.String, boolean)
     */
    @SuppressWarnings("unchecked")
    public <T extends IBean> T [] findByValues(Class<T> beansInterface, Map<String, Object> values, String orderBy, boolean ascending)
    {
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // validate bean interface
        Class<? extends IBean> beanClass = BEAN_INTERFACE_TO_CLASS_MAP.get(beansInterface);
        if (beanClass == null)
        {
            throw new IllegalArgumentException("Invalid bean interface specified");            
        }

        // get persistent beans by criteria
        try
        {
            // construct query criteria
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<? extends IBean> criteriaQuery = criteriaBuilder.createQuery(beanClass);
            Root<? extends IBean> beanRoot = criteriaQuery.from(beanClass);
            if ((values != null) && !values.isEmpty())
            {
                Predicate predicate = null;
                for (Map.Entry<String,Object> value : values.entrySet())
                {
                    Predicate valuePredicate = ((value.getValue() != null) ? criteriaBuilder.equal(beanRoot.get(value.getKey()), value.getValue()) : criteriaBuilder.isNull(beanRoot.get(value.getKey())));
                    predicate = ((predicate != null) ? criteriaBuilder.and(predicate, valuePredicate) : valuePredicate);
                }
                criteriaQuery.where(predicate);
            }
            if (orderBy != null)
            {
                criteriaQuery.orderBy(ascending ? criteriaBuilder.asc(beanRoot.get(orderBy)) : criteriaBuilder.desc(beanRoot.get(orderBy)));
            }
            
            // invoke query
            Query query = entityManager.createQuery(criteriaQuery);
            List<? extends IBean> beansList = query.getResultList();
            if ((beansList != null) && !beansList.isEmpty())
            {
                return beansList.toArray((T [])Array.newInstance(beansInterface, beansList.size()));
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
    public <T extends IBean> T [] findByValues(Class<T> beansInterface, Map<String, Object> values)
    {
        return findByValues(beansInterface, values, null, true);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findWidgetByGuid(java.lang.String)
     */
    public IWidget findWidgetByGuid(String guid)
    {
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // get widget by GUID using custom query
        if (guid != null)
        {
            try
            {
                Query query = entityManager.createNamedQuery("WIDGET");
                query.setParameter("guid", guid);
                return (IWidget)query.getSingleResult();
            }
            catch (NoResultException nre)
            {
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
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // get default widget by type using custom query
        if (widgetContext != null)
        {
            try
            {
                Query query = entityManager.createNamedQuery("DEFAULT_WIDGET");
                query.setParameter("widgetContext", widgetContext);
                return (IWidget)query.getSingleResult();
            }
            catch (NoResultException nre)
            {
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
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // get widget instance using custom query
        if ((apiKey != null) && (userId != null) && (sharedDataKey != null) && (serviceContext != null))
        {
            try
            {
                Query query = entityManager.createNamedQuery("WIDGET_INSTANCE");
                query.setParameter("apiKey", apiKey);
                query.setParameter("userId", userId);
                query.setParameter("sharedDataKey", sharedDataKey);
                query.setParameter("widgetContext", serviceContext);
                return (IWidgetInstance)query.getSingleResult();
            }
            catch (NoResultException nre)
            {
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
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // get widget instance using custom query
        if ((apiKey != null) && (userId != null) && (sharedDataKey != null) && (widgetGuid != null))
        {
            try
            {
                Query query = entityManager.createNamedQuery("WIDGET_INSTANCE_GUID");
                query.setParameter("apiKey", apiKey);
                query.setParameter("userId", userId);
                query.setParameter("sharedDataKey", sharedDataKey);
                query.setParameter("guid", widgetGuid);
                return (IWidgetInstance)query.getSingleResult();
            }
            catch (NoResultException nre)
            {
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
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // get widget instance using custom query
        if (idKey != null)
        {
            try
            {
                Query query = entityManager.createNamedQuery("WIDGET_INSTANCE_ID");
                query.setParameter("idKey", idKey);
                return (IWidgetInstance)query.getSingleResult();
            }
            catch (NoResultException nre)
            {
            }
            catch (Exception e)
            {
                logger.error("Unexpected exception: "+e, e);
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#findWidgetsByType(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public IWidget[] findWidgetsByType(String widgetContext)
    {
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // get widgets by type using custom query
        if (widgetContext != null)
        {
            try
            {
                Query query = entityManager.createNamedQuery("WIDGETS");
                query.setParameter("widgetContext", widgetContext);
                List<IWidget> widgetsList = query.getResultList();
                if ((widgetsList != null) && !widgetsList.isEmpty())
                {
                    return widgetsList.toArray(new IWidget[widgetsList.size()]);
                }
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
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // commit transaction
        EntityTransaction transaction = entityManager.getTransaction();
        if (transaction.isActive())
        {
            transaction.rollback();
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IPersistenceManager#save(org.apache.wookie.beans.IBean)
     */
    public boolean save(IBean bean)
    {
        // validate entity manager transaction
        if (entityManager == null)
        {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        // persist new or update existing bean
        try
        {
            entityManager.persist(bean);
            return true;
        }
        catch (Exception e)
        {
            logger.error("Unexpected exception: "+e, e);
            return false;
        }
    }

	public IOAuthToken findOAuthToken(IWidgetInstance widgetInstance) {

        if (entityManager == null) {
            throw new IllegalStateException("Transaction not initiated or already closed");
        }        

        if (widgetInstance != null) {
            try {
                Query query = entityManager.createNamedQuery("ACCESS_TOKEN");
                query.setParameter("widgetInstance", widgetInstance);
                return (IOAuthToken) query.getSingleResult();
            } catch (Exception e) {
            }
        }
        return null;
	}
}
