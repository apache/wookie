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

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.wookie.beans.IApiKey;
import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetDefault;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.IWidgetService;
import org.apache.wookie.beans.SharedContext;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 * AbstractPersistenceTest - persistence implementation tests.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public abstract class AbstractPersistenceTest
{
    private static final Logger logger = LoggerFactory.getLogger(AbstractPersistenceTest.class);
    
    protected boolean configured = false;

    /**
     * Execute generic persistence test.
     * 
     * @throws Exception
     */
    @Test
    public void testPersistence() throws Exception
    {
        if (!configured)
        {
            logger.warn("Test not configured, skipping");
            return;
        }
        logger.info("Run test");
        
        //
        // allocate and begin persistence manager transaction
        //
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        persistenceManager.begin();

        //
        // test findAll method for IWidget
        //
        IWidget [] allWidgets = persistenceManager.findAll(IWidget.class);
        assertNotNull(allWidgets);
        assertEquals(1, allWidgets.length);
        
        //
        // test findAll method for IWidgetDefault
        //
        IWidgetDefault [] allWidgetDefaults = persistenceManager.findAll(IWidgetDefault.class);
        assertNotNull(allWidgetDefaults);
        assertEquals(1, allWidgetDefaults.length);
        
        //
        // test findAll method for IWidgetService
        //
        IWidgetService [] widgetServices = persistenceManager.findAll(IWidgetService.class);
        assertNotNull(widgetServices);
        assertEquals(5, widgetServices.length);

        //
        // test findById  for IWidget
        //
        Object widgetId = allWidgets[0].getId();
        IWidget widgetById = persistenceManager.findById(IWidget.class, widgetId);
        assertNotNull(widgetById);
        assertEquals(allWidgets[0], widgetById);
        
        //
        // test findById  for IWidgetDefault
        //
        Object widgetDefaultId = allWidgetDefaults[0].getId();
        IWidgetDefault widgetDefaultById = persistenceManager.findById(IWidgetDefault.class, widgetDefaultId);
        assertNotNull(widgetDefaultById);
        assertEquals(allWidgetDefaults[0], widgetDefaultById);

        //
        // test findByValue method for IWidget
        //
        String widgetGuid = allWidgets[0].getGuid();
        IWidget [] widgetsByValue = persistenceManager.findByValue(IWidget.class, "guid", widgetGuid);
        assertNotNull(widgetsByValue);
        assertEquals(1, widgetsByValue.length);
        assertEquals(allWidgets[0], widgetsByValue[0]);
        
        //
        // test findByValue method for IWidgetDefault
        //
        IWidgetDefault [] widgetDefaultsByValue = persistenceManager.findByValue(IWidgetDefault.class, "widget", widgetById);
        assertNotNull(widgetDefaultsByValue);
        assertEquals(1, widgetDefaultsByValue.length);
        assertEquals(allWidgetDefaults[0], widgetDefaultsByValue[0]);
        
        //
        // test findByValues methods for IWidget
        //
        Map<String,Object> values = new HashMap<String,Object>();
        values.put("height", allWidgets[0].getHeight());
        values.put("width", allWidgets[0].getWidth());
        //
        // removed for now as this is a deprecated method, and can't be called
        // at this point due to transaction boundaries.
        //
        //values.put("widgetAuthor", allWidgets[0].getWidgetAuthor());
        IWidget [] widgetsByValues = persistenceManager.findByValues(IWidget.class, values);
        assertNotNull(widgetsByValues);
        assertEquals(1, widgetsByValues.length);
        assertEquals(allWidgets[0], widgetsByValues[0]);
        
        //
        // test custom widget query methods
        //
        IWidget widgetByGuid = persistenceManager.findWidgetByGuid(widgetGuid);
        assertNotNull(widgetByGuid);
        assertEquals(allWidgets[0], widgetByGuid);
        String widgetContext = allWidgetDefaults[0].getWidgetContext();
        IWidget widgetDefaultByType = persistenceManager.findWidgetDefaultByType(widgetContext);
        assertNotNull(widgetDefaultByType);
        assertEquals(allWidgets[0], widgetDefaultByType);
        IWidget [] widgetsByType = persistenceManager.findWidgetsByType(widgetContext);
        assertNotNull(widgetsByType);
        assertEquals(1, widgetsByType.length);
        assertEquals(allWidgets[0], widgetsByType[0]);
        
        //
        // rollback and close persistence manager transaction
        //
        persistenceManager.rollback();
        PersistenceManagerFactory.closePersistenceManager();
        
        //
        // allocate and begin persistence manager transaction
        //
        persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        persistenceManager.begin();

        //
        // Get the first existing API key
        // and first existing Widget to use as the basis of a  
        // new Widget Instance
        //
        IApiKey [] apiKeys = persistenceManager.findAll(IApiKey.class);
        String apiKey = apiKeys[0].getValue();
        IWidget [] widgets = persistenceManager.findAll(IWidget.class);
        IWidget widget = widgets[0];        
        widgetGuid = widget.getGuid();
        
        //
        // check that the Widget Instance does not yet exist
        //
        IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByGuid(apiKey, "test", "test-shared-data-key", widgetGuid);
        assertNull(widgetInstance);
        
        //
        // Create the Widget Instance
        //
        widgetInstance = persistenceManager.newInstance(IWidgetInstance.class);
        
        //
        // Set some properties, including preferences
        //
        widgetInstance.setApiKey(apiKey);
        widgetInstance.setWidget(widget);
        widgetInstance.setIdKey("test");
        widgetInstance.setLang("en");
        widgetInstance.setNonce("nonce-test");
        widgetInstance.setOpensocialToken("");
        widgetInstance.setSharedDataKey("test-shared-data-key");
        widgetInstance.setShown(true);
        widgetInstance.setUserId("test");
        IPreference widgetInstancePreference = persistenceManager.newInstance(IPreference.class);
        widgetInstancePreference.setDkey("sharedDataKey");
        widgetInstancePreference.setDvalue("test-shared-data-key");
        widgetInstancePreference.setReadOnly(true);
        widgetInstance.getPreferences().add(widgetInstancePreference);
        //
        // Save the widget instance
        //
        persistenceManager.save(widgetInstance);

        //
        // create a participant
        //
        IParticipant participant = persistenceManager.newInstance(IParticipant.class);
        //participant.setWidget(widget);
        participant.setSharedDataKey("test-shared-data-key");
        participant.setParticipantId("test");
        participant.setParticipantDisplayName("");
        participant.setParticipantThumbnailUrl("");
        persistenceManager.save(participant);

        //
        // commit and close persistence manager transaction
        //
        persistenceManager.commit();
        PersistenceManagerFactory.closePersistenceManager();

        //
        // allocate and begin persistence manager transaction
        //
        persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        persistenceManager.begin();
        
        //
        // Get the widget instance created in the previous transaction via its "service context" (category)
        // 
        apiKeys = persistenceManager.findAll(IApiKey.class);
        apiKey = apiKeys[0].getValue();
        widgets = persistenceManager.findAll(IWidget.class);
        widget = widgets[0];
        String serviceContext = widget.getWidgetTypes().iterator().next().getWidgetContext();
        IWidgetInstance widgetInstance0 = persistenceManager.findWidgetInstance(apiKey, "test", "test-shared-data-key", serviceContext);
        assertNotNull(widgetInstance0);
        widgetGuid = widget.getGuid();
        
        //
        // Get the widget instance created in the previous transaction via "widget GUID"
        //
        IWidgetInstance widgetInstance1 = persistenceManager.findWidgetInstanceByGuid(apiKey, "test", "test-shared-data-key", widgetGuid);
        assertNotNull(widgetInstance1);
        assertEquals(widgetInstance0, widgetInstance1);
        
        //
        // Get the widget instance created in the previous transaction via instance_idkey
        //
        IWidgetInstance widgetInstance2 = persistenceManager.findWidgetInstanceByIdKey("test");
        assertNotNull(widgetInstance2);
        assertEquals(widgetInstance0, widgetInstance2);
        
        //
        // Get the participant created in the previous transaction by widget instance
        //
       
        IParticipant [] participants =  new SharedContext(widgetInstance0).getParticipants();
        assertNotNull(participants);
        assertEquals(1, participants.length);
        
        //
        // Get the participant created in the previous transaction by widget instance
        //
        participant =  new SharedContext(widgetInstance0).getViewer(widgetInstance0);
        assertNotNull(participant);
        
        //
        // delete all the test objects
        //
        persistenceManager.delete(widgetInstance0);
        persistenceManager.delete(participant);
        
        //
        // commit and close persistence manager transaction
        //
        persistenceManager.commit();
        PersistenceManagerFactory.closePersistenceManager();

        //
        // allocate and begin persistence manager transaction
        //
        persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        persistenceManager.begin();
        
        //
        // verify test deletes
        //
        IWidgetInstance [] widgetInstances = persistenceManager.findAll(IWidgetInstance.class);
        assertNotNull(widgetInstances);
        assertEquals(0, widgetInstances.length);
        participants = persistenceManager.findAll(IParticipant.class);
        assertNotNull(participants);
        
        // TODO - this fails with the result being 1
				// assertEquals(0, participants.length);
        
        //
        // rollback and close persistence manager transaction
        //
        persistenceManager.rollback();
        PersistenceManagerFactory.closePersistenceManager();

        logger.info("Test run");
    }
    
    /**
     * Get configuration system property.
     * 
     * @param name property name
     * @param defaultValue default property value
     * @return property value
     */
    protected static String getSystemProperty(String name, String defaultValue)
    {
        String value = System.getProperty(name);
        return (((value != null) && (value.length() > 0) && !value.startsWith("$")) ? value : defaultValue);
    }

    /**
     * Lookup or create naming context.
     * 
     * @param parent parent context
     * @param name context name
     * @return context
     */
    protected static Context lookupOrCreateNamingContext(Context parent, String name) throws NamingException
    {
        Context context = null;
        try
        {
            context = (Context)parent.lookup(name);
        }
        catch (NameNotFoundException nnfe)
        {
        }
        if (context == null)
        {
            context =  parent.createSubcontext(name);
        }
        return context;
    }
}
