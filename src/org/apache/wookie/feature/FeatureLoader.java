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
package org.apache.wookie.feature;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.wookie.beans.IServerFeature;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceCommitException;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.w3c.util.IRIValidator;

import java.util.Iterator;

/**
 * Loads installed features when server is initialized
 *
 */
public class FeatureLoader {

	static Logger _logger = Logger.getLogger(FeatureLoader.class.getName());

	/**
	 * Loads features based on the properties configuration supplied
	 * @param config
	 */
	public static void loadFeatures(PropertiesConfiguration config){
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        persistenceManager.begin();
		
		// Remove existing features
		for (IServerFeature sf: persistenceManager.findAll(IServerFeature.class)){
			persistenceManager.delete(sf);
		}
		
		// Add features in properties configuration
		Iterator i = config.getKeys();
		while (i.hasNext()){
			String klass = (String) i.next();
			String name = config.getString(klass);
			try {
				IServerFeature sf = createFeature(name, klass);
				// Only install it if there isn't an existing
				// feature with the same name
				if (persistenceManager.findServerFeatureByName(name) == null){
				    persistenceManager.save(sf);
					_logger.info("Installed feature:"+name);					
				} else {
					_logger.error("Error installing feature: "+name+" was already installed");
				}

			} catch (Exception e) {
				_logger.error("Error installing feature:"+e.getMessage());
			}
		}
		
        try
        {
            persistenceManager.commit();
        }
        catch (PersistenceCommitException pce)
        {
            throw new RuntimeException("Feature loading exception: "+pce, pce);
        }
        PersistenceManagerFactory.closePersistenceManager();
	}

    /**
     * Validates a feature for the supplied parameters, or throws
     * an exception if the feature specified is not a valid.
     * @param name the name of the feature, which must be a valid IRI
     * @param klass the Class name of the feature, which must implement the IFeature interface
     */
    public static void validateFeature(String name, String klass) throws Exception{
        // Are required parameters missing?
        if (name == null || klass == null){
            throw new Exception("Invalid feature");
        }
        // Does the class exist?
        Class theClass;
        try {
            theClass = Class.forName(klass);
        } catch (Exception e) {
            throw new Exception("Invalid feature: class not found");
        }
        // Does the class implement IFeature?
        boolean implementsFeature = false;
        Class[] interfaces = theClass.getInterfaces();
        if (interfaces != null) {
            if (interfaces.length > 0){
                if (interfaces[0].getName().equals("org.apache.wookie.feature.IFeature")) implementsFeature = true;
            }
        }
        if (!implementsFeature) throw new Exception("Invalid feature: class is not a Feature class");
        
        // Does the feature name match that in the class?
        if (!((IFeature) theClass.newInstance()).getName().equals(name)) throw new Exception("Invalid feature: feature name supplied and name in the class do not match");;

        // Is the feature name a valid IRI?
        if (!IRIValidator.isValidIRI(name)){
            throw new Exception("Invalid feature: name is not a valid IRI");            
        }
    }

    /**
	 * Returns a valid ServerFeature for the supplied parameters, or throws
	 * an exception if the feature specified is not a valid ServerFeature.
	 * @param name the name of the feature, which must be a valid IRI
	 * @param klass the Class name of the feature, which must implement the IFeature interface
	 */
	public static IServerFeature createFeature(String name, String klass) throws Exception{
		// validate feature
	    validateFeature(name, klass);
	    
		// All is well, create the SF and return it
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IServerFeature sf = persistenceManager.newInstance(IServerFeature.class);
		sf.setClassName(klass);
		sf.setFeatureName(name);
		return sf;
	}

}
