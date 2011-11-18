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

import java.util.Map;

import org.apache.wookie.beans.IBean;
import org.apache.wookie.beans.IOAuthToken;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;

/**
 * IPersistenceManager - beans persistence manager interface.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IPersistenceManager
{
    /**
     * Begin persistence manager transaction.
     */
    void begin();
    
    /**
     * Commit persistence manager transaction.
     * 
     * @throws PersistenceCommitException
     */
    void commit() throws PersistenceCommitException;
    
    /**
     * Rollback persistence manager transaction
     */
    void rollback();
    
    /**
     * Close persistence manager.
     */
    void close();
    
    /**
     * Find persistent bean by generic id.
     * 
     * @param beanInterface interface of bean to retrieve
     * @param id id object of bean to retrieve
     * @return retrieved bean instance or null if not found
     */
    <T extends IBean> T findById(Class<T> beanInterface, Object id);
    
    /**
     * Find persistent beans with matching value for specified field.
     * 
     * @param beansInterface interface of beans to retrieve
     * @param name name of field to match
     * @param value value of field to match
     * @return retrieved matching beans array or empty array if none found
     */
    <T extends IBean> T [] findByValue(Class<T> beansInterface, String name, Object value);

    /**
     * Find ordered persistent beans with matching value for specified field.
     * 
     * @param beansInterface interface of beans to retrieve
     * @param name name of field to match
     * @param value value of field to match
     * @param orderBy name of field to order matching beans
     * @param ascending ascending order flag
     * @return retrieved matching beans array or empty array if none found
     */
    <T extends IBean> T [] findByValue(Class<T> beansInterface, String name, Object value, String orderBy, boolean ascending);
    
    /**
     * Find persistent beans with matching values for specified fields.
     * 
     * @param beansInterface interface of beans to retrieve
     * @param values name/value map of fields to match
     * @return retrieved matching beans array or empty array if none found
     */
    <T extends IBean> T [] findByValues(Class<T> beansInterface, Map<String,Object> values);

    /**
     * Find ordered persistent beans with matching values for specified fields.
     * 
     * @param beansInterface interface of beans to retrieve
     * @param values name/value map of fields to match
     * @param orderBy name of field to order matching beans
     * @param ascending ascending order flag
     * @return retrieved matching beans array or empty array if none found
     */
    <T extends IBean> T [] findByValues(Class<T> beansInterface, Map<String,Object> values, String orderBy, boolean ascending);

    /**
     * Find all persistent beans.
     * 
     * @param beansInterface interface of beans to retrieve
     * @return retrieved matching beans array or empty array if none found
     */
    <T extends IBean> T [] findAll(Class<T> beansInterface);

    /**
     * Create new instance of persistent class.
     * 
     * @param instanceInterface interface of persistent class to create.
     * @return new instance
     */
    <T> T newInstance(Class<T> instanceInterface);

    /**
     * Save persistent bean.
     * 
     * @param bean previously retrieved bean to save.
     * @return operation success flag
     */
    boolean save(IBean bean);
    
    /**
     * Delete persistent bean.
     * 
     * @param bean previously retrieved bean to delete.
     * @return operation success flag
     */
    boolean delete(IBean bean);
    
    /**
     * Delete persistent beans.
     * 
     * @param bean previously retrieved beans to delete.
     * @return operation success flag
     */
    boolean delete(IBean [] beans);
    
    /**
     * Custom GUID IWidget query.
     *  
     * @param guid GUID matching query value
     * @return retrieved IWidget bean instance or null if not found
     */
    IWidget findWidgetByGuid(String guid);

    /**
     * Custom default widget type IWidget query.
     *  
     * @param widgetContext default widget type matching query value
     * @return retrieved IWidget bean instance or null if not found
     */
    IWidget findWidgetDefaultByType(String widgetContext);

    /**
     * Custom widget type IWidgets query.
     *  
     * @param widgetContext widget type matching query value
     * @return retrieved matching IWidget beans array or empty array if none found
     */
    IWidget [] findWidgetsByType(String widgetContext);

    /**
     * Custom service type IWidgetInstance query.
     * 
     * @param apiKey API key matching query value
     * @param userId user id matching query value
     * @param sharedDataKey shared data key matching query value
     * @param serviceContext service type matching query value
     * @return retrieved IWidgetInstance bean instance or null if not found
     */
    IWidgetInstance findWidgetInstance(String apiKey, String userId, String sharedDataKey, String serviceContext);

    /**
     * Custom widget GUID IWidgetInstance query.
     * 
     * @param apiKey API key matching query value
     * @param userId user id matching query value
     * @param sharedDataKey shared data key matching query value
     * @param widgetGuid widget GUID matching query value
     * @return retrieved IWidgetInstance bean instance or null if not found
     */
    IWidgetInstance findWidgetInstanceByGuid(String apiKey, String userId, String sharedDataKey, String widgetGuid);

    /**
     * Custom id key IWidgetInstance query.
     * 
     * @param idKey id key matching query value
     * @return retrieved IWidgetInstance bean instance or null if not found
     */
    IWidgetInstance findWidgetInstanceByIdKey(String idKey);
    
    /**
     * Find oAuth token for widget instance
     * @param widgetInstance
     * @return
     */
    IOAuthToken findOAuthToken(IWidgetInstance widgetInstance);
 
}
